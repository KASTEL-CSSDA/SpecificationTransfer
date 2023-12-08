package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.reader;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.MergeProperties;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.TransferProperties;


public class ConsolePropertyReader {

    //TODO: Support all options later, however, for now make default merging, saving, without showing, and directory.

    public TransferProperties readTransferProperties(){
       return new TransferProperties();
    }

    public MergeProperties readMergeProperties(){
        return new MergeProperties();
    }



}
