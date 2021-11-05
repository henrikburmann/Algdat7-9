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

            ArrayList<Byte> bytes = new ArrayList<>(); // bytes to be analyzed

            boolean repeatedWordFound = false; // if repeated wrod to be compressed is found

            int lengthCompress = -1;
            int match = 0;

            // work through alle bytes that begins at index we have not analyzed
            for (int j = i; j < bytesInput.length ; j++) {

                bytes.add(bytesInput[j]);

                // size of list needs to be...
                if (bytes.size() >= LENGTH_SEARCH_WORD) {
                    int compressLocation = findMatch(bytes, j); // todo: skrive denne metoden...

                    // Hva må jeg gjøre nu som jeg har funnet lokasjonen??
                }

            }


        }
    }

    /**
     *
     * @param input
     * @param indexStart
     * @return index if found, -1 if no matching byte sequnce is found
     *
     * todo: står på en index og må søke bakover i input
     * Hvor langt skal jeg søke bakover??
     * lengde på søkeord jeg skal matche med??
     *
     */
    public int findMatch(ArrayList<Byte> input, int indexStart) {

        int lookbackIndex = indexStart - SIZE_LOOKING_BACK;
        int distanceBack = SIZE_LOOKING_BACK;

        if (lookbackIndex < 0) { // if lookbackindex goes beyond startindex
            lookbackIndex = 0;
            distanceBack = indexStart;
        }

        int returnindex = -1;
        boolean found = false; // true if found matching byte sequnce

        // iterate through list of bytes to be analyzed

        for (int i = lookbackIndex; i < lookbackIndex + distanceBack ; i++) {

            found = true; // trenger jeg denne??
            int index = 0; // index we starter å lete på i input

            for (int j = 0; j < input.size() ; j++) {

                if (bytesInput[i+j] != input.get(indexStart)) {
                    found = false; // if iterated over and not found
                    break; // then we break out of loop
                }

                if (j == 0) {
                    returnindex = j+i;
                }
                index++;
            }
            // We have reached the end of input, break out of loop then
            if (index == input.size()) {
                break;
            }

            if (found) {
                return returnindex;
            }

        }
        return returnindex; // will return -1 if matching bytesequence is not found
    }


}
