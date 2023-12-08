package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.functions;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source.DirectoriesSourceCitation;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source.FilesSourceCitation;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source.SingleFileSourceCitation;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source.SourceCitation;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.resourcebundle.ResourceBundleController;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.FunctionType;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.FunctionsParse;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.SourceCitationType;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.helper.ClassNameHelper;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.ASTMerge;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.MergeProperties;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.MergeType;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.mapping.MergeMap;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.mapping.MergeMapping;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.mapping.NameMergeMap;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.mapping.SuccessiveMergeMap;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.filetree.FileParseTreeHelper;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.filetree.FileParseTreesMerge;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.filetree.FileTreeHelper;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.filetree.TreeHelper;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.TransferProperties;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.util.Pair;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.TreeItem;

/**
 * This is the Controller responsible for executing the different Functions /
 * Operations / ... on the Sources.
 *
 * @author Jonas
 */
public class FunctionsController {

	/**
	 * This Method takes the properties and Source-Citation and based on that
	 * assigns it to the corresponding sub-method to execute the right function /
	 * operation.
	 *
	 * @param transferProperties The ISAJAM-Properties to get the settings for which
	 *                           Source-Citation Type to use.
	 * @param sourceCitation     The source to work on.
	 */
	public static void transfer(TransferProperties transferProperties, SourceCitation sourceCitation,
			MergeProperties mergeProperties) {
		if (transferProperties == null) {
			throw new IllegalArgumentException("The provided Properties shoudn't be null!");
		}
		if (sourceCitation == null) {
			throw new IllegalArgumentException("The provided SourceCitation (Sources to work with) shoudn't be null!");
		}

		FunctionType funcType = (FunctionType) transferProperties
				.getPropertyValue(TransferProperties.FUNCTION_TYPE_TRANSFER_PROPERTY).getValue();
		SourceCitationType srcType = (SourceCitationType) transferProperties
				.getPropertyValue(TransferProperties.SOURCE_CITATION_TYPE_TRANSFER_PROPERTY).getValue();
		Locale language = (Locale) transferProperties.getPropertyValue(TransferProperties.LANGUAGE_TRANSFER_PROPERTY)
				.getValue();
		String baseDirectory = (String) transferProperties
				.getPropertyValue(TransferProperties.OUTPUT_BASE_DIR_TRANSFER_PROPERTY).getValue();
		transfer(funcType, srcType, sourceCitation, mergeProperties, language, baseDirectory);
	}

	/**
	 * This Method takes some parameters for settings (That are also combined into
	 * the Properties) and Source-Citation and based on that assigns it to the
	 * corresponding sub-method to execute the right function / operation.
	 *
	 * @param funcType       The FunctionType which describes the Operation that
	 *                       should be executed.
	 * @param srcCiteType    The SourceCitationType to define the Type of the Input.
	 *                       (A Single-File, Multiple-Files, Directories, ...)
	 * @param sourceCitation The source to work on.
	 * 
	 * @param locale         The current Locale (for the used language at the
	 *                       GUI/Console).
	 */
	public static void transfer(FunctionType funcType, SourceCitationType srcCiteType, SourceCitation sourceCitation,
			MergeProperties mergeProperties, Locale locale, String baseDirectory) {
		if (funcType == null) {
			throw new IllegalArgumentException("The provided FunctionType shoudn't be null!");
		}
		if (srcCiteType == null) {
			throw new IllegalArgumentException("The provided SourceCitationType shoudn't be null!");
		}
		if (sourceCitation == null) {
			throw new IllegalArgumentException("The provided SourceCitation (Sources to work with) shoudn't be null!");
		}
		if (locale == null) {
			System.err.println("The provided Locale to define the used Language was null or invalid.\n"
					+ "The Program defined it to be the default one.");
			locale = (Locale) new TransferProperties().getPropertyValue("Language").getValue();
		}

		switch (srcCiteType) {
		case Files:
			transferFiles((FilesSourceCitation) sourceCitation, mergeProperties, locale, baseDirectory);
			break;
		case Directory:
			transferDirectories((DirectoriesSourceCitation) sourceCitation, mergeProperties, locale, baseDirectory);
			break;
		}
	}

	/**
	 * Execute the different functions / operations, by calling the corresponding
	 * methods for it. <br>
	 * The function is defined by the given functionType.
	 *
	 * @param fCite  The Files sources to work on. of the User.
	 * @param locale The current Locale (for the used language at the GUI/Console).
	 */
	public static void transferFiles(FilesSourceCitation fCite, MergeProperties mergeProperties, Locale locale,
			String baseDirectory) {
		List<CompilationUnit> parsedFiles;

		parsedFiles = parseFilesSourceCitation(fCite, locale);

		// Get the Merge Properties
		MergeType mergeType = (MergeType) mergeProperties.getPropertyValue("MergeType").getValue();
		MergeMapping mergeMapping = (MergeMapping) mergeProperties.getPropertyValue("MergeMapping").getValue();

		// Mapping:
		MergeMap mergeMap;
		switch (mergeMapping) {
		default:
		case Successive:
			mergeMap = new SuccessiveMergeMap();
			break;
		case Name:
			mergeMap = new NameMergeMap();
			break;
		}

		List<Pair<CompilationUnit, CompilationUnit>> mergeClasses = mergeMap.calculateMergeMap(parsedFiles);

		// Merge-Results:
		List<CompilationUnit> mergedClasses = new ArrayList<>();

		for (Pair<CompilationUnit, CompilationUnit> curMergeClasses : mergeClasses) {
			mergedClasses.add(ASTMerge.mergeClasses(mergeType, curMergeClasses.getKey(), curMergeClasses.getValue()));
		}

		for (CompilationUnit mergedClass : mergedClasses) {

			File outputDir = new File(baseDirectory);
			initDirectoryForSaving(outputDir);

			File output = new File(baseDirectory + ClassNameHelper.getClassName(mergedClass) + ".java");
			saveCompilationUnitToFile(output, mergedClass);
		}

	}

	private static List<CompilationUnit> parseFilesSourceCitation(FilesSourceCitation fCite, Locale locale) {

		List<CompilationUnit> parsedFiles = FunctionsParse.parseFiles(fCite.getFiles());


		return parsedFiles;
	}

	private static void initDirectoryForSaving(File outputDir) {
		if (outputDir == null) {
			return;
		}

		if (!outputDir.exists()) {
			outputDir.mkdirs();
			// Remember: Use mkdirs so that path1/path2 where both Folders are non-existent
			// can be created.
		}
	}

	private static void saveCompilationUnitToFile(File output, CompilationUnit cu) {
		try {
			PrintWriter out = new PrintWriter(output);
			out.write(cu.toString());
			out.close();

			System.out.println("Wrote File: " + output);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FunctionsController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Execute the different functions / operations, by calling the corresponding
	 * methods for it. <br>
	 * The function is defined by the given functionType.
	 *
	 * @param dirCite The Directories sources to work on. of the User.
	 * @param locale  The current Locale (for the used language at the GUI/Console).
	 */
	public static void transferDirectories(DirectoriesSourceCitation dirCite, MergeProperties mergeProperties,
			Locale locale, String baseDirectory) {
		List<TreeItem<File>> dirTrees;
		List<CompilationUnit> parsedFiles;
		List<TreeItem<Pair<File, CompilationUnit>>> dirTreesParsed;

		dirTrees = makeFileTreesFromDirectories(dirCite, locale);

		dirTreesParsed = parseDirectoryTrees(dirTrees, locale);

		MergeType mergeType = (MergeType) mergeProperties.getPropertyValue("MergeType").getValue();
		// Merge all Trees together into one.
		TreeItem<Pair<File, CompilationUnit>> mergedTree = mergeParsedTrees(dirTreesParsed, mergeType, baseDirectory);

		saveParseTree(mergedTree);

	}

	private static List<TreeItem<File>> makeFileTreesFromDirectories(DirectoriesSourceCitation dirCite, Locale locale) {

		List<TreeItem<File>> dirTrees = new ArrayList<>();
		for (File directory : dirCite.getDirectories()) {
			TreeItem<File> dirTree = FileTreeHelper.createFileTreeFromDirectory(directory);
			System.out.println(dirTree.toString());
			dirTrees.add(dirTree);
		}

		return dirTrees;
	}

	private static List<TreeItem<Pair<File, CompilationUnit>>> parseDirectoryTrees(List<TreeItem<File>> dirTrees,
			Locale locale) {

		List<TreeItem<Pair<File, CompilationUnit>>> dirTreesParsed = new ArrayList<>();
		for (TreeItem<File> dirTree : dirTrees) {
			dirTreesParsed.add(FileParseTreeHelper.convertFileTreeToFileCompileTree(dirTree));
		}

		return dirTreesParsed;
	}

	private static TreeItem<Pair<File, CompilationUnit>> mergeParsedTrees(
			List<TreeItem<Pair<File, CompilationUnit>>> dirTreesParsed, MergeType mergeType, String baseDirectory) {
		// Merge all Trees together into one.
		TreeItem<Pair<File, CompilationUnit>> mergedTree;

		if (!dirTreesParsed.isEmpty()) {
			mergedTree = FileParseTreesMerge.deepCopyTreeItem(dirTreesParsed.get(0));

			// Merge all other Trees into.
			for (int i = 1; i < dirTreesParsed.size(); i++) {// Skipp the 0th, because it was already used as the base.

				mergedTree = FileParseTreesMerge.mergeFileCompileTrees(new File(baseDirectory), mergeType, mergedTree, // Use
																														// the
																														// Base
						dirTreesParsed.get(i) // Use the cur Index
				);
			}

			return mergedTree;
		}

		return null;
	}

	private static void saveParseTree(TreeItem<Pair<File, CompilationUnit>> mergedTree) {
		TreeHelper.<Pair<File, CompilationUnit>>flatten(mergedTree).stream().map((treeItem) -> {
			return treeItem.getValue();
		}).forEach((pair) -> {
			File output = pair.getKey();
			CompilationUnit cu = pair.getValue();

			// Remember: isFile/Directory doesn't work on unexistant Files.
			if (cu == null) {
				initDirectoryForSaving(output);
			} else {
				saveCompilationUnitToFile(output, cu);
			}
		});
	}
}
