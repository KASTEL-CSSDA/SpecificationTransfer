package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.mapping;

import com.github.javaparser.ast.CompilationUnit;
import java.util.ArrayList;
import java.util.List;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.util.Pair;
/**
 *
 * @author Jonas
 */
public class SuccessiveMergeMap implements MergeMap {

    @Override
    public List<Pair<CompilationUnit, CompilationUnit>> calculateMergeMap(List<CompilationUnit> classes) {
        List<Pair<CompilationUnit, CompilationUnit>> mergeMap = new ArrayList<>();

        for (int i = 0; i < classes.size(); i += 2) {
            CompilationUnit curLeft = classes.get(i);
            CompilationUnit curRight;

            if (i + 1 <= classes.size() - 1) {
                curRight = classes.get(i + 1);
            } else {
                curRight = null;
            }

            mergeMap.add(new Pair<>(
                    curLeft,
                    curRight
            ));
        }

        return mergeMap;
    }
}
