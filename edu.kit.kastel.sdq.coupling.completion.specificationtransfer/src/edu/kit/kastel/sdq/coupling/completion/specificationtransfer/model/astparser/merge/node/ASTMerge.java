package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.helper.ClassNameHelper;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.callable.ConstructorDeclarationHelper;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.callable.ConstructorDeclarationMerge;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.callable.MethodDeclarationHelper;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.callable.MethodDeclarationMerge;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.field.FieldDeclarationHelper;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.field.FieldDeclarationMerge;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

/**
 * This Class provides all the Methods to merge two Compiled-Files together.
 *
 * @author Jonas
 */
public class ASTMerge {

    //ToDO: Handle null, as well as ast.get... = null !!!
    public static CompilationUnit mergeClasses(MergeType type, CompilationUnit astLeft, CompilationUnit astRight) {
        if (astLeft == null) {
            return astRight;
        }
        if (astRight == null) {
            return astLeft;
        }
        if (type == null) {
            return null;
        }

        //ClassOrInterfaceDeclaration cuMerge = new ClassOrInterfaceDeclaration();
        CompilationUnit cuMerge = new CompilationUnit();

        //Surroundings
        PackageDeclaration mergePackageDeclaration = mergePackageDeclaration(type, astLeft, astRight);
        if (mergePackageDeclaration != null) {
            cuMerge.setPackageDeclaration(
                    mergePackageDeclaration
            );
        }
        NodeList<ImportDeclaration> mergeImportDeclarations = mergeImportDeclarations(type, astLeft, astRight);
        if (mergeImportDeclarations != null) {
            cuMerge.setImports(
                    mergeImportDeclarations
            );
        }
        //Important Stuffs:
        String className = mergeClassNames(astLeft, astRight);
        ClassOrInterfaceDeclaration mergedClass;
        if (areInterfaces(astLeft, astRight)) {
            mergedClass = cuMerge.addInterface(
                    className
            );
        } else {
            mergedClass = cuMerge.addClass(
                    className
            );
        }
        
        
        
        //ToDo: Modifiers
        NodeList<ClassOrInterfaceType> mergeExtended = mergeExtended(astLeft, astRight);
        if (mergeExtended != null) {
            mergedClass.setExtendedTypes(mergeExtended);
        }
        NodeList<ClassOrInterfaceType> mergeImplemented = mergeImplemented(astLeft, astRight);
        if (mergeImplemented != null) {
            mergedClass.setImplementedTypes(mergeImplemented);
        }
        //ToDo: Check for Generics.

        //Variable-Fields:
        FieldDeclarationMerge fieldDeclarationMerge = new FieldDeclarationMerge(new FieldDeclarationHelper());
        NodeList<FieldDeclaration> mergeFieldDeclarations = fieldDeclarationMerge.merge(type, astLeft, astRight);
        if (mergeFieldDeclarations != null) {
            for (FieldDeclaration mergedFieldDeclaration : mergeFieldDeclarations) {
                mergedClass.getMembers().add(mergedFieldDeclaration);
                //There is currently no Method to add Fields (like for any of the other Parts...
                //@See: https://github.com/javaparser/javaparser/issues/1183
            }
        }

        //Constructors:
        ConstructorDeclarationMerge constructorDeclarationMerge = new ConstructorDeclarationMerge(new ConstructorDeclarationHelper());
        NodeList<ConstructorDeclaration> mergeConstructorDeclarations = constructorDeclarationMerge.merge(type, astLeft, astRight);
        if (mergeConstructorDeclarations != null) {
            for (ConstructorDeclaration mergedConstructor : mergeConstructorDeclarations) {
                mergedClass.addMember(mergedConstructor);
            }
        }

        //Methods:
        MethodDeclarationMerge methodDeclarationMerge = new MethodDeclarationMerge(new MethodDeclarationHelper());
        NodeList<MethodDeclaration> mergeMethodDeclarations = methodDeclarationMerge.merge(type, astLeft, astRight);
        if (mergeMethodDeclarations != null) {
            for (MethodDeclaration mergedMethod : mergeMethodDeclarations) {
                mergedClass.addMember(mergedMethod);
            }
        }

        return cuMerge;
    }

    private static PackageDeclaration mergePackageDeclaration(MergeType type, CompilationUnit astLeft, CompilationUnit astRight) {
        assert (type != null && astLeft != null && astRight != null);
        assert (astLeft.getPackageDeclaration() != null && astRight.getPackageDeclaration() != null);

        boolean leftPresent = astLeft.getPackageDeclaration().isPresent();
        boolean rightPresent = astRight.getPackageDeclaration().isPresent();

        switch (type) {
            default:
            case None:
                return null;
            case Left:
                if (leftPresent) {
                    return astLeft.getPackageDeclaration().get();
                } else {
                    return null;
                }
            case Right:
                if (rightPresent) {
                    return astRight.getPackageDeclaration().get();
                } else {
                    return null;
                }
            case Inner:
                if (leftPresent && rightPresent
                        && astLeft.getPackageDeclaration().get().equals(astRight.getPackageDeclaration().get())) {
                    return astLeft.getPackageDeclaration().get();
                } else {
                    return null;
                }
            case Outer:
                if (leftPresent || rightPresent) {
                    if (leftPresent && rightPresent
                            && astLeft.getPackageDeclaration().get().equals(astRight.getPackageDeclaration().get())) {
                        return astLeft.getPackageDeclaration().get();
                    } else {
                        //ToDo: Decide Actions for Outer-Merge for different Values.
                        return null;
                    }
                } else {
                    return null;
                }
        }
    }

    private static NodeList<ImportDeclaration> mergeImportDeclarations(MergeType type, CompilationUnit astLeft, CompilationUnit astRight) {
        assert (type != null && astLeft != null && astRight != null);
        assert (astLeft.getPackageDeclaration() != null && astRight.getPackageDeclaration() != null);

        NodeList<ImportDeclaration> leftImports = astLeft.getImports();
        NodeList<ImportDeclaration> rightImports = astRight.getImports();

        //Always use both Imports (Those can later be removed)
        NodeList<ImportDeclaration> imports = new NodeList<>();

        imports.addAll(leftImports);

        for (ImportDeclaration rightImport : rightImports) {
            if (!imports.contains(rightImport)) {
                imports.add(rightImport);
            }
        }

        return imports;
    }

    private static boolean areInterfaces(CompilationUnit astLeft, CompilationUnit astRight) {
        assert (astLeft != null && astRight != null);

        NodeList<TypeDeclaration<?>> leftTypes = astLeft.getTypes();
        NodeList<TypeDeclaration<?>> rightTypes = astRight.getTypes();

        //Assumption: Only one Class, ... per File!
        if (leftTypes.size() == 1 && rightTypes.size() == 1) {
            TypeDeclaration<?> leftType = leftTypes.get(0);
            TypeDeclaration<?> rightType = rightTypes.get(0);

            if (leftType.isClassOrInterfaceDeclaration()
                    && rightType.isClassOrInterfaceDeclaration()) {
                return ((ClassOrInterfaceDeclaration) leftType).isInterface()
                        && ((ClassOrInterfaceDeclaration) rightType).isInterface();
            }
        }

        return false;
    }

    private static String mergeClassNames(CompilationUnit astLeft, CompilationUnit astRight) {
        assert (astLeft != null && astRight != null);

        String leftName = ClassNameHelper.getClassName(astLeft);
        String rightName = ClassNameHelper.getClassName(astRight);

        if (leftName == null || rightName == null) {
            return null;
        }

        //Check if the Class-Names are identical, otherwise merge into a new name.
        if (leftName.equals(rightName)) {
            return leftName;
        } else {
            return "Merged" + leftName + "And" + rightName;
        }
    }

    private static NodeList<ClassOrInterfaceType> mergeExtended(CompilationUnit astLeft, CompilationUnit astRight) {
        assert (astLeft != null && astRight != null);

        NodeList<TypeDeclaration<?>> leftTypes = astLeft.getTypes();
        NodeList<TypeDeclaration<?>> rightTypes = astRight.getTypes();

        //Assumption: Only one Class, ... per File!
        if (leftTypes.size() == 1 && rightTypes.size() == 1) {
            TypeDeclaration<?> leftType = leftTypes.get(0);
            TypeDeclaration<?> rightType = rightTypes.get(0);

            if (leftType.isClassOrInterfaceDeclaration()
                    && rightType.isClassOrInterfaceDeclaration()) {
                NodeList<ClassOrInterfaceType> leftExtended = ((ClassOrInterfaceDeclaration) leftType).getExtendedTypes();
                NodeList<ClassOrInterfaceType> rightExtended = ((ClassOrInterfaceDeclaration) rightType).getExtendedTypes();

                //Assumption: Should be the same.
                if (leftExtended.containsAll(rightExtended) && rightExtended.containsAll(leftExtended)) {
                    return leftExtended;
                }
                //ToDo: Merge the Extended
            }
        }

        return null;
    }

    private static NodeList<ClassOrInterfaceType> mergeImplemented(CompilationUnit astLeft, CompilationUnit astRight) {
        assert (astLeft != null && astRight != null);

        NodeList<TypeDeclaration<?>> leftTypes = astLeft.getTypes();
        NodeList<TypeDeclaration<?>> rightTypes = astRight.getTypes();

        //Assumption: Only one Class, ... per File!
        if (leftTypes.size() == 1 && rightTypes.size() == 1) {
            TypeDeclaration<?> leftType = leftTypes.get(0);
            TypeDeclaration<?> rightType = rightTypes.get(0);

            if (leftType.isClassOrInterfaceDeclaration()
                    && rightType.isClassOrInterfaceDeclaration()) {
                NodeList<ClassOrInterfaceType> leftImplemented = ((ClassOrInterfaceDeclaration) leftType).getImplementedTypes();
                NodeList<ClassOrInterfaceType> rightImplemented = ((ClassOrInterfaceDeclaration) rightType).getImplementedTypes();

                //Assumption: Are equal
                if (leftImplemented.containsAll(rightImplemented) && rightImplemented.containsAll(leftImplemented)) {
                    return leftImplemented;
                }
                //ToDo: Merge the Extended
            }
        }

        return null;
    }

}
