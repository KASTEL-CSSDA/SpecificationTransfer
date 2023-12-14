package edu.kit.kastel.sdq.coupling.completion.specificationtransfer;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.FileHandler;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source.DirectoriesSourceCitation;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.functions.FunctionsController;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.MergeProperties;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.TransferProperties;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.PropertyValue;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.reader.ConsolePropertyReader;

import java.io.File;
import java.io.IOException;

/**
 * Super-Class for the two different Main-Classes. (Command-Line or GUI
 * Version).
 *
 * @author Jonas
 */
public class Main {

	public static void main(String[] args) {
		ConsolePropertyReader reader = new ConsolePropertyReader();

		TransferProperties transferProperties = reader.readTransferProperties();
		MergeProperties mergeProperties = reader.readMergeProperties();
		DirectoriesSourceCitation sourceCitation = new DirectoriesSourceCitation();

		// TODO: Replace by CLI Arguments

		File specificationsSrcDir = null;
		File implementationSrcDir = null;
		try {
			specificationsSrcDir = FileHandler.getDirectoryFromPath(args[0]);
			implementationSrcDir = FileHandler.getDirectoryFromPath(args[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sourceCitation.addDirectory(specificationsSrcDir);
		sourceCitation.addDirectory(implementationSrcDir);

		String outputBasePath = args[2];

		transferProperties.setProperty(TransferProperties.OUTPUT_BASE_DIR_TRANSFER_PROPERTY,
				new PropertyValue<>(String.class, outputBasePath));

		FunctionsController.transfer(transferProperties, sourceCitation, mergeProperties);
	}
}
