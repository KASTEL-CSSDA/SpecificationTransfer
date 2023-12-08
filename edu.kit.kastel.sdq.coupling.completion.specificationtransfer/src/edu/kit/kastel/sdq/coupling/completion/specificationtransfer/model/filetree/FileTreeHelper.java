/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.filetree;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.DirectoryHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Jonas
 */
public class FileTreeHelper {

    public static TreeItem<File> createFileTreeFromDirectory(File dir) {
        TreeItem<File> rootItem = new TreeItem<>(dir);

        //TreeView<File> tree = new TreeView<>(rootItem);
        //Add all TreeItems
        rootItem.getChildren().addAll(
                getTreeItemsFromFileRecursive(dir)
        );

        return rootItem;
    }

    private static List<TreeItem<File>> getTreeItemsFromFileRecursive(File dir) {
        List<TreeItem<File>> treeItems = new ArrayList<>();

        for (File subDir : DirectoryHandler.getSubDirectoriesFromDirectory(dir)) {
            //This are the Sub-Directories (No Files)
            TreeItem<File> subDirItem = getTreeItemFromDirectory(subDir);
            subDirItem.getChildren().addAll(
                    getTreeItemsFromFileRecursive(subDir)
            );

            treeItems.add(subDirItem);
        }

        for (File file : DirectoryHandler.getDirectFilesFromDirectory(dir)) {
            //This are only normal Files (No Directories)
            treeItems.add(
                    getTreeItemFromFile(file)
            );
        }

        return treeItems;
    }

    private static TreeItem<File> getTreeItemFromFile(File f) {
        TreeItem<File> treeItemF = new TreeItem<>(f);

        return treeItemF;
    }

    private static TreeItem<File> getTreeItemFromDirectory(File dir) {
        TreeItem<File> treeItemF = new TreeItem<>(dir);
        treeItemF.setExpanded(true);

        return treeItemF;
    }

    //Tree-Traversing-Helpers:
    public static List<TreeItem<File>> getSubDirectories(TreeItem<File> curRoot) {
        if (curRoot.isLeaf()) {
            return new ArrayList<>();
        }

        return curRoot.getChildren().stream().filter((treeItem) -> {
            return treeItem.getValue().isDirectory();
        }).collect(Collectors.toList());
    }

    public static List<TreeItem<File>> getDirectFiles(TreeItem<File> curRoot) {
        if (curRoot.isLeaf()) {
            return new ArrayList<>();
        }

        return curRoot.getChildren().stream().filter((treeItem) -> {
            return treeItem.getValue().isFile();
        }).collect(Collectors.toList());
    }

}
