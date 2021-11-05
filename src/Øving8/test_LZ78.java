package Øving8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class test_LZ78 {
    /*
    TODO: dictonary (størrelse = ?)
        distanse for å se tilbake = ?
        minimum søkeord størrelse = ?

        - bytes fra input fil
        - lagrer compressed bytes i output fil

     */

    private File inputfile;
    private File outputfile; // komprimert skal sendes til denne

    private byte[] bytesInput;
    private byte[] bytesCompressed;

    // Defines looking back size and search word size
    private final int SIZE_LOOKING_BACK = 127;
    private final int LENGTH_SEARCH_WORD = 4;

    public test_LZ78(File inputfile, File outputfile) throws IOException {
        this.inputfile = inputfile;
        this.outputfile = outputfile;
        // todo: se om jeg trenger å gjøre dette
        this.bytesInput = new byte[0];
        this.bytesCompressed = new byte[0];

        fillBytesInput();
    }

    public void fillBytesInput() throws IOException {
        bytesInput = Files.readAllBytes(inputfile.toPath());
    }

    /**
     * todo:
     *  dictionary??
     *  - leser fra bytes inputfile (lite repetering i starten)
     *  - lagre alt som kan komprimeres
     *  - må også lagre ting som ikke kan komprimeres
     */
    public void compressFile() {

        int compressedListIndex = 0;
        // int bytesDone;

        // begynner med å gå gjennom bytes array
        for (int i = 0; i < bytesInput.length ; i++) {

            List<Byte> bytes = new ArrayList<>(); // bytes to be analyzed

            boolean repeatedWordFound = false; // if repeated wrod to be compressed is found

            int lengthCompress = -1;
            int match = 0;

            // work through alle bytes that begins at index we have not analyzed
            for (int j = i; j < bytesInput.length ; j++) {

                bytes.add(bytesInput[j]);

                // size of list needs to be...
                if (bytes.size() >= LENGTH_SEARCH_WORD) {
                    int compressLocation = findMatch(bytesInput, j); // todo: skrive denne metoden...

                    // Hva må jeg gjøre nu som jeg har funnet lokasjonen??
                }

            }


        }
    }

    /**
     *
     * @param input
     * @param index
     * @return index if found, 0 if no matching byte sequnce is found
     *
     * todo: står på en index og må søke bakover i input
     * Hvor langt skal jeg søke bakover??
     * lengde på søkeord jeg skal matche med??
     *
     */
    public int findMatch(byte[] input, int index) {




        return 0;
    }


}
