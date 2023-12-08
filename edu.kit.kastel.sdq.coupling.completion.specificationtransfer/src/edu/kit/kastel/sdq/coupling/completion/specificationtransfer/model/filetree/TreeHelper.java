/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.filetree;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Jonas
 */
public class TreeHelper {

    public static <T> List<TreeItem<T>> flatten(TreeItem<T> treeItem) {
        List<TreeItem<T>> allNodes = new ArrayList<>();

        if (treeItem != null) {
            allNodes.add(treeItem);

            if (!treeItem.isLeaf()) {
                for (TreeItem<T> child : treeItem.getChildren()) {
                    allNodes.addAll(flatten(child));
                }
            }
        }

        return allNodes;
    }

    //ToString:
    public static String toString(TreeItem<?> root) {
        if (root == null) {
            return null;
        }

        return toString(root, 0);
    }

    private static String toString(TreeItem<?> curTreeItem, int curDepth) {
        String strCurTreeItem = getTabsByDepth(curDepth) + curTreeItem.getValue().toString();

        for (TreeItem<?> child : curTreeItem.getChildren()) {
            strCurTreeItem += "\n" + getTabsByDepth(curDepth + 1) + toString(child, curDepth + 1);
        }

        return strCurTreeItem;
    }

    private static String getTabsByDepth(int curDepth) {
        String strTabs = "";
        for (int i = 0; i < curDepth; i++) {
            strTabs += "\t";
        }
        return strTabs;
    }

}
