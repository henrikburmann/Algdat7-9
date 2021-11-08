package Øving8;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        this.bytesLeft = byteInFile.length;

    }

    public void decompress() throws IOException {

        while (bytesLeft != 0) {

            int c = byteInFile[inputIndex];
            inputIndex++;

            if (c < 0) {
                int outputStart = outputIndex;

                if (byteOutFile.length <= outputIndex + (-c)) {
                    expandOutputArray();
                }

                for (int i = outputIndex; i < -c + outputStart; i ++, outputIndex ++, inputIndex ++) {

                    if (byteInFile.length <= inputIndex) {
                        break;
                    }

                    byteOutFile[outputIndex] = byteInFile[inputIndex];
                }
            } else { // if the number is negative, no compression
                if (byteInFile.length <= inputIndex) {
                    break;
                }
                int compressedContentLength = byteInFile[inputIndex];
                inputIndex++;
                // expands list if to small
                if (byteOutFile.length <= outputIndex + compressedContentLength) {
                    expandOutputArray();
                }
                int start = outputIndex;
                // extracts data from place in list referenced to and adds in ouput array
                for (int i = start-c; i < compressedContentLength + (start-c) ; i++, outputIndex++) {
                    if (byteOutFile.length <= outputIndex || byteOutFile.length <= i) {
                        break;
                    }
                    byteOutFile[outputIndex] = byteOutFile[i];
                }
            }
            setBytesLeft();
        }
        trimOutputArray();
        writeToFile();
    }

    public void setBytesLeft() {
        bytesLeft = byteInFile.length - inputIndex;
    }

    public void readInputFile() throws IOException {
        byteInFile = Files.readAllBytes(inFile.toPath());
        String in = new String(byteInFile);
        System.out.println(in);
        int length = (int) (byteInFile.length * 1.75);
        // maybe we need to expand size of output array??
        byteOutFile = new byte[length];
    }

    // Expand output array
    public void expandOutputArray() {
        byte[] temp = new byte[byteOutFile.length *2];
        for (int i = 0; i < outputIndex ; i++) {
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
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outFile.getPath()));
        String x = new String(byteOutFile, StandardCharsets.UTF_8);
        System.out.println(x); // ja funker ikke helt som det skal
        outputWriter.write(x);
        outputWriter.flush();
        outputWriter.close();
    }

    public static void main(String[] args) throws IOException {

        File fileIn = new File(String.valueOf(Paths.get("src/Øving8/test_files/compressed_test.txt")));
        String out = "src/Øving8/test_files/decompressed_test.txt";
        File fileOut = new File(out);

        if (fileOut.exists()) {
            fileOut.delete();
            fileOut = new File(out);
        }
        test_decomp_LZ78 test = new test_decomp_LZ78(fileIn, fileOut);
        test.decompress();

    }

}
