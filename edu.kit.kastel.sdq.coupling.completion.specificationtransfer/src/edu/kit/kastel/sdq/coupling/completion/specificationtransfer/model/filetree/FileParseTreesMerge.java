/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.filetree;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.helper.ClassNameHelper;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.ASTMerge;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.MergeType;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.mapping.NameMergeMap;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.util.Pair;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;

import javafx.scene.control.TreeItem;

/**
 *
 * @author Jonas
 */
public class FileParseTreesMerge {

	public static TreeItem<Pair<File, CompilationUnit>> mergeFileCompileTrees(File targetFile, MergeType mergeType,
			TreeItem<Pair<File, CompilationUnit>> leftRoot, TreeItem<Pair<File, CompilationUnit>> rightRoot) {
		// Assumption: The given roots are similar. Meaning the sub-dirs and file are in
		// a similar structure and Naming
		return mergeSubDirectoriesTreeItem(targetFile, mergeType, leftRoot, rightRoot);
	}

	// Sub-Directories
	private static List<TreeItem<Pair<File, CompilationUnit>>> mergeAllSubDirectoriesTreeItem(File targetDir,
			MergeType mergeType, List<TreeItem<Pair<File, CompilationUnit>>> leftDirs,
			List<TreeItem<Pair<File, CompilationUnit>>> rightDirs) {
		HashMap<String, TreeItem<Pair<File, CompilationUnit>>> leftHash = mapSubDirsNameToTreeItems(leftDirs);
		HashMap<String, TreeItem<Pair<File, CompilationUnit>>> rightHash = mapSubDirsNameToTreeItems(rightDirs);

		HashMap<String, Pair<TreeItem<Pair<File, CompilationUnit>>, TreeItem<Pair<File, CompilationUnit>>>> mergedDirs = new HashMap<>();
		for (String leftKey : leftHash.keySet()) {
			TreeItem<Pair<File, CompilationUnit>> leftVal = leftHash.get(leftKey);
			TreeItem<Pair<File, CompilationUnit>> rightVal = rightHash.get(leftKey); // Might be null

			mergedDirs.put(leftKey, new Pair<>(leftVal, rightVal));
		}
		// Check for any left out right Keys
		for (String rightKey : rightHash.keySet()) {
			if (!mergedDirs.containsKey(rightKey)) {
				TreeItem<Pair<File, CompilationUnit>> rightVal = rightHash.get(rightKey);
				mergedDirs.put(rightKey, new Pair<>(null, rightVal));
			}
		}

		// ToDo: MergeType... (Should also be a ToDo for other Merges...)
		List<TreeItem<Pair<File, CompilationUnit>>> mergedSubDirTreeItems = new ArrayList<>();
		for (Entry<String, Pair<TreeItem<Pair<File, CompilationUnit>>, TreeItem<Pair<File, CompilationUnit>>>> mergeDir : mergedDirs
				.entrySet()) {
			TreeItem<Pair<File, CompilationUnit>> mergedSubDirectoriesTreeItem = mergeSubDirectoriesTreeItem(targetDir,
					mergeType, mergeDir.getValue().getKey(), mergeDir.getValue().getValue());

			mergedSubDirTreeItems.add(mergedSubDirectoriesTreeItem);
		}

		return mergedSubDirTreeItems;
	}

	private static HashMap<String, TreeItem<Pair<File, CompilationUnit>>> mapSubDirsNameToTreeItems(
			List<TreeItem<Pair<File, CompilationUnit>>> subDirs) {
		HashMap<String, TreeItem<Pair<File, CompilationUnit>>> subDirsMap = new HashMap<>();
		for (TreeItem<Pair<File, CompilationUnit>> subDir : subDirs) {
			subDirsMap.put(subDir.getValue().getKey().getName(), subDir);
		}

		return subDirsMap;
	}

	private static TreeItem<Pair<File, CompilationUnit>> mergeSubDirectoriesTreeItem(File targetDir,
			MergeType mergeType, TreeItem<Pair<File, CompilationUnit>> leftDir,
			TreeItem<Pair<File, CompilationUnit>> rightDir) {
		// Merge the Name
		String dirName;

		String leftName = leftDir.getValue().getKey().getName();
		String rightName = rightDir.getValue().getKey().getName();

		if (leftName.equalsIgnoreCase(rightName)) {
			dirName = leftName;
		} else {
			dirName = leftName + "_" + rightName;
		}

		// Create TreeItem
		TreeItem<Pair<File, CompilationUnit>> subDir = new TreeItem<>(new Pair<>(new File(targetDir, dirName), null // Because
																													// directory
		));

		// Split Children into Sub-Dirs and Files
		List<TreeItem<Pair<File, CompilationUnit>>> leftSubDirChilds = leftDir.getChildren().stream()
				.filter((treeItem) -> {
					return isDirectory(treeItem);
				}).collect(Collectors.toList());
		List<TreeItem<Pair<File, CompilationUnit>>> leftFileChilds = leftDir.getChildren().stream()
				.filter((treeItem) -> {
					return isFileLeaf(treeItem);
				}).collect(Collectors.toList());

		List<TreeItem<Pair<File, CompilationUnit>>> rightSubDirChilds = rightDir.getChildren().stream()
				.filter((treeItem) -> {
					return isDirectory(treeItem);
				}).collect(Collectors.toList());
		List<TreeItem<Pair<File, CompilationUnit>>> rightFileChilds = rightDir.getChildren().stream()
				.filter((treeItem) -> {
					return isFileLeaf(treeItem);
				}).collect(Collectors.toList());

		// Merge Sub-Dirs
		List<TreeItem<Pair<File, CompilationUnit>>> mergeSubDirTreeItems = mergeAllSubDirectoriesTreeItem(
				subDir.getValue().getKey(), mergeType, leftSubDirChilds, rightSubDirChilds);
		subDir.getChildren().addAll(mergeSubDirTreeItems);

		// Merge Files
		List<TreeItem<Pair<File, CompilationUnit>>> mergeFileCompileTreeItems = mergeMultipleFileCompileTreeItem(
				subDir.getValue().getKey(), mergeType, leftFileChilds, rightFileChilds);
		subDir.getChildren().addAll(mergeFileCompileTreeItems);

		return subDir;
	}

	// Files:
	private static List<TreeItem<Pair<File, CompilationUnit>>> mergeMultipleFileCompileTreeItem(File targetFile,
			MergeType mergeType, List<TreeItem<Pair<File, CompilationUnit>>> leftFiles,
			List<TreeItem<Pair<File, CompilationUnit>>> rightFiles) {
		List<TreeItem<Pair<File, CompilationUnit>>> mergedFileCompileTreeItem = new ArrayList<>();

		// A List of all compilation Units.
		List<CompilationUnit> parsedFiles = new ArrayList<>();

		// Check that all given TreeItems are in fact Files (No Directories)
		List<TreeItem<Pair<File, CompilationUnit>>> fileTreeItems = new ArrayList<>();
		fileTreeItems.addAll(leftFiles);
		fileTreeItems.addAll(rightFiles);
		for (TreeItem<Pair<File, CompilationUnit>> fileTreeItem : fileTreeItems) {
			if (!isFileLeaf(fileTreeItem)) {
				throw new IllegalArgumentException("The given TreeItem is not from a File and cannot be processed.");
			}
			parsedFiles.add(fileTreeItem.getValue().getValue());
		}

		// Maping:
		NameMergeMap mergeMap = new NameMergeMap();
		List<Pair<CompilationUnit, CompilationUnit>> mergeClasses = mergeMap.calculateMergeMap(parsedFiles);

		// Merge-Results:
		for (Pair<CompilationUnit, CompilationUnit> mergeClass : mergeClasses) {

			// ToDo: Check if really present!
			Optional<TreeItem<Pair<File, CompilationUnit>>> potentialLeftTreeItem = leftFiles.stream()
					.filter((treeItem) -> {
						return treeItem.getValue().getValue().equals(mergeClass.getKey());
						// ToDo: Check if equals works.
					}).findAny();// Assume that only exactly one are in that list

			Optional<TreeItem<Pair<File, CompilationUnit>>> potentialRightTreeItem = rightFiles.stream()
					.filter((treeItem) -> {
						return treeItem.getValue().getValue().equals(mergeClass.getValue());
						// ToDo: Check if equals works.
					}).findAny();// Assume that only exactly one are in that list

			TreeItem<Pair<File, CompilationUnit>> leftTreeItem;
			TreeItem<Pair<File, CompilationUnit>> rightTreeItem;
			TreeItem<Pair<File, CompilationUnit>> mergeFileCompileTreeItem = null;
			if (potentialLeftTreeItem.isPresent() && potentialRightTreeItem.isPresent()) {
				leftTreeItem = potentialLeftTreeItem.get();
				rightTreeItem = potentialRightTreeItem.get();

				mergeFileCompileTreeItem = mergeFileCompileTreeItem(targetFile, mergeType, leftTreeItem, rightTreeItem);
			} else if (potentialLeftTreeItem.isPresent()) {
				mergeFileCompileTreeItem = potentialLeftTreeItem.get();
			} else if (potentialRightTreeItem.isPresent()) {
				mergeFileCompileTreeItem = potentialRightTreeItem.get();
			}

			mergedFileCompileTreeItem.add(mergeFileCompileTreeItem);
		}

		return mergedFileCompileTreeItem;
	}

	private static TreeItem<Pair<File, CompilationUnit>> mergeFileCompileTreeItem(File targetFile, MergeType mergeType,
			TreeItem<Pair<File, CompilationUnit>> leftFile, TreeItem<Pair<File, CompilationUnit>> rightFile) {
		if (isFileLeaf(leftFile) && isFileLeaf(rightFile)) {
			CompilationUnit mergedClass = ASTMerge.mergeClasses(mergeType, leftFile.getValue().getValue(),
					rightFile.getValue().getValue());

			String fileName = ClassNameHelper.getClassName(mergedClass);

			return new TreeItem<>(new Pair<>(new File(targetFile, fileName + ".java"), mergedClass));
		} else {
			throw new IllegalArgumentException(
					"Cannot merge the File-Leaves because one of the given TreeItems is not one.");
		}
	}

	private static boolean isFileLeaf(TreeItem<Pair<File, CompilationUnit>> treeItem) {
		return treeItem != null && treeItem.isLeaf() && treeItem.getValue() != null
				&& treeItem.getValue().getKey() != null && treeItem.getValue().getKey().isFile();
	}

	private static boolean isDirectory(TreeItem<Pair<File, CompilationUnit>> treeItem) {
		return treeItem != null && treeItem.getValue() != null && treeItem.getValue().getKey() != null
				&& treeItem.getValue().getKey().isDirectory();
	}

	// Tree-Management
	public static TreeItem<Pair<File, CompilationUnit>> deepCopyTreeItem(
			TreeItem<Pair<File, CompilationUnit>> treeItem) {
		TreeItem<Pair<File, CompilationUnit>> copy = new TreeItem<>(treeItem.getValue());
		for (TreeItem<Pair<File, CompilationUnit>> child : treeItem.getChildren()) {
			copy.getChildren().add(deepCopyTreeItem(child));
		}
		return copy;
	}

}
