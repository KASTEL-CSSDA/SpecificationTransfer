package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.filetree;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.FunctionsParse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;

import javafx.scene.control.TreeItem;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.util.Pair;

/**
 *
 * @author Jonas
 */
public class FileParseTreeHelper {

    //Convertion:
    public static TreeItem<Pair<File, CompilationUnit>> convertFileTreeToFileCompileTree(TreeItem<File> root) {
        return convertFileTreeItemToFileCompileTreeItem(root);
    }

    private static TreeItem<Pair<File, CompilationUnit>> convertFileTreeItemToFileCompileTreeItem(TreeItem<File> fileTreeItem) {
        TreeItem<Pair<File, CompilationUnit>> curFileCompileTreeItem;

        if (fileTreeItem.isLeaf()) {
            Pair<File, CompilationUnit> curFileCompilePair;

            //Check if it's a leaf, because it's a file, or the directory doesn't have any Files in it...
            //Only consider .java Files as leaves
            if (fileTreeItem.getValue().isFile() && fileTreeItem.getValue().getAbsolutePath().toString().endsWith(".java")) {
                curFileCompilePair = new Pair<>(
                        fileTreeItem.getValue(),
                        FunctionsParse.parseSingleFile(fileTreeItem.getValue())
                );
            } else {
                curFileCompilePair = new Pair<>(
                        fileTreeItem.getValue(),
                        null
                );
            }

            curFileCompileTreeItem = new TreeItem<>(
                    curFileCompilePair
            );
        } else { //Consider the Children

            curFileCompileTreeItem = new TreeItem<>(new Pair<>(
                    fileTreeItem.getValue(),
                    null
            ));

            for (TreeItem<File> subTreeItem : fileTreeItem.getChildren()) {
                curFileCompileTreeItem.getChildren().add(
                        convertFileTreeItemToFileCompileTreeItem(subTreeItem)
                );
            }
        }

        return curFileCompileTreeItem;
    }

    //Tree-Traversing-Helpers:
    public static List<TreeItem<Pair<File, CompilationUnit>>> getSubDirectories(TreeItem<Pair<File, CompilationUnit>> curRoot) {
        if (curRoot.isLeaf()) {
            return new ArrayList<>();
        }

        return curRoot.getChildren().stream().filter((treeItem) -> {
            return treeItem.getValue().getKey().isDirectory();
        }).collect(Collectors.toList());
    }

    public static List<TreeItem<Pair<File, CompilationUnit>>> getDirectFiles(TreeItem<Pair<File, CompilationUnit>> curRoot) {
        if (curRoot.isLeaf()) {
            return new ArrayList<>();
        }

        return curRoot.getChildren().stream().filter((treeItem) -> {
            return treeItem.getValue().getKey().isFile();
        }).collect(Collectors.toList());
    }
}
