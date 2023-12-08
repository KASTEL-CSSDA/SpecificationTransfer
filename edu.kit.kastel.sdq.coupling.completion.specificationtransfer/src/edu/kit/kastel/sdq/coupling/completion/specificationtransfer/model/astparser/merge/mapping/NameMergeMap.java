package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.mapping;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.helper.ClassNameHelper;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.util.Pair;
import com.github.javaparser.ast.CompilationUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jonas
 */
public class NameMergeMap implements MergeMap {

    @Override
    public List<Pair<CompilationUnit, CompilationUnit>> calculateMergeMap(List<CompilationUnit> classes) {
        List<Pair<CompilationUnit, CompilationUnit>> mergeMap = new ArrayList<>();

        final List<Pair<String, CompilationUnit>> classNameMap = this.calculateClassNameMap(classes);
        List<Boolean> alreadyMapped = new ArrayList<>(
                Collections.nCopies(classNameMap.size(), false) //Is Immutable ~> Pack into ArrayList
        );

        //Assumption: Only maximal 2 classes with the same Name.
        for (int i = 0; i < classNameMap.size(); i++) {
            if (!alreadyMapped.get(i)) {
                alreadyMapped.set(i, true);

                String className = classNameMap.get(i).getKey();
                CompilationUnit classCU = classNameMap.get(i).getValue();

                int curLowerBound = i + 1;
                Optional<Pair<String, CompilationUnit>> sameName = classNameMap.subList(curLowerBound, classNameMap.size())
                        .stream().filter((pair) -> {
                            Boolean wasAlreadyMapped = alreadyMapped.get(
                                    //classNameMap.indexOf(pair)
                                    getIndexInClassNameMapFromCurPair(classNameMap, pair, curLowerBound)
                            );
                            String curClassName = pair.getKey();
                            return !wasAlreadyMapped && curClassName.equals(className);
                        }).findFirst();

                if (sameName.isPresent()) {
                    mergeMap.add(new Pair<>(classCU, sameName.get().getValue()));

                    alreadyMapped.set(
                            //classNameMap.indexOf(sameName.get()),
                            getIndexInClassNameMapFromCurPair(classNameMap, sameName.get(), curLowerBound),
                            true
                    );
                } else {
                    mergeMap.add(new Pair<>(classCU, null));
                }
            }
        }

        return mergeMap;
    }

    private List<Pair<String, CompilationUnit>> calculateClassNameMap(List<CompilationUnit> classes) {
        List<Pair<String, CompilationUnit>> classNameMap = new ArrayList<>();
        for (CompilationUnit curClass : classes) {
            String className = ClassNameHelper.getClassName(curClass);
            if (className == null) {
                break;
            }

            classNameMap.add(new Pair<>(
                    className,
                    curClass
            ));
        }

        return classNameMap;
    }

    private int getIndexInClassNameMapFromCurPair(List<Pair<String, CompilationUnit>> classNameMap, Pair<String, CompilationUnit> curPair, int curLowerBound
    ) {
        //Remember: indexOf did only return the first Instance in the List, when looking for the same String-Name but different CU ...
        if (classNameMap == null || curPair == null) {
            return -1;
        }
        if (curLowerBound > classNameMap.size()) {
            return -1;
        }

        for (int i = curLowerBound; i < classNameMap.size(); i++) {
            if (classNameMap.get(i).getKey().equals(curPair.getKey())) {
                return i;
            }
        }

        return -1;
    }
}
