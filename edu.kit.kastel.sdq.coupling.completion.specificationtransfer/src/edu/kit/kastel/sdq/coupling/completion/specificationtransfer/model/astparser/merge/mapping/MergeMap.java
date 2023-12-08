package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.mapping;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.util.Pair;
import com.github.javaparser.ast.CompilationUnit;
import java.util.List;

/**
 *
 * @author Jonas
 */
public abstract interface MergeMap {

    public abstract List<Pair<CompilationUnit, CompilationUnit>> calculateMergeMap(List<CompilationUnit> classes);
}
