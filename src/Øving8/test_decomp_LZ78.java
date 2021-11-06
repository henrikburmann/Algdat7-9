package Øving8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class test_decomp_LZ78 {

    private File inFile;
    private File outFile;

    private byte[] byteInFile;
    private byte[] byteOutFile;

    private int inputIndex;
    private int bytesLeft;
    private int outputIndex;

    public test_decomp_LZ78(File inFile, File outFile) throws IOException {
        this.inFile = inFile;
        this.outFile = outFile;
        this.byteInFile = new byte[0];
        this.byteOutFile = new byte[0];
        this.inputIndex = 0;
        this.outputIndex = 0;

        readInputFile();
    }

    public void decompress() {

    }

    public void readInputFile() throws IOException {
        byteInFile = Files.readAllBytes(inFile.toPath());
        int length = (int) (byteInFile.length * 1.75);
        // maybe we need to expand size of output array??
        byteOutFile = new byte[length];
    }

    // Expand output array
    public void expandOutputArray() {
        byte[] temp = new byte[byteOutFile.length * 2];
        for (int i = 0; i < temp.length ; i++) {
            temp[i] = byteOutFile[i];
        }
        // same data but twice the length
        byteOutFile = temp;
    }

    // Trim output array
    public void trimOutputArray() {
        byte[] temp = new byte[outputIndex];
        for (int i = 0; i < temp.length ; i++) {
            temp[i] = byteOutFile[i];
        }
        byteOutFile = temp;
    }

    public void writeToFile() throws IOException {

    }

}
