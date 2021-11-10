package Øving8;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CompLZ77 {
    /*
          - bytes fra input fil
        - lagrer compressed bytes i output fil
     */

    private File inputfile;
    private File outputfile; // komprimert skal sendes til denne

    byte[] bytesInput;
    byte[] bytesCompressed;

    // Defines looking back size and search word size
    private final int SIZE_LOOKING_BACK = 127;
    private final int LENGTH_SEARCH_WORD = 4;

    public CompLZ77(File inputfile, File outputfile) throws IOException {
        this.inputfile = inputfile;
        this.outputfile = outputfile;
        this.bytesInput = new byte[0];
        this.bytesCompressed = new byte[0];
        fillBytesInput();

        System.out.println("bytesinput length: " + bytesInput.length);
    }

    public void fillBytesInput() throws IOException {

        bytesInput = Files.readAllBytes(inputfile.toPath());
        System.out.println(bytesInput.length);
        bytesCompressed = new byte[bytesInput.length];

    }

    /**

     */
    public void compressFile() throws IOException {

        int compressedListIndex = 0;
        int bytesDone = 0;
        int compressIndex = -1; // initialized value out of scope

        // begynner med å gå gjennom bytes array
        for (int i = 0; i < bytesInput.length; i++) {

            ArrayList<Byte> bytes = new ArrayList<>(); // bytes to be analyzed

            boolean repeatedSequnceFound = false; // if repeated wrod to be compressed is found

            int lengthCompress = -1;
            int match = 0;

            // work through alle bytes that begins at index we have not analyzed
            for (int j = i; j < bytesInput.length; j++) {

                bytes.add(bytesInput[j]);

                // size of list needs to be...
                if (bytes.size() >= LENGTH_SEARCH_WORD) {

                    int compressLocation = findMatch(bytes, i);
                    //System.out.println(compressLocation); // printer bare ut -1??

                    if (compressLocation >= 0) { // we have found location with repeating sequence

                        repeatedSequnceFound = true; //
                        compressIndex = i;
                        match = compressLocation;
                        lengthCompress = bytes.size();

                    } else {
                        break; // not found any matching bytes sequnce
                    }
                }
            }

            // System.out.println(repeatedSequnceFound);
            if (repeatedSequnceFound) {

                // amount of bytes that could not be compressed
                int notCompressed = compressIndex - bytesDone;

                // adds a byte that counts amount of bytes which is not compressed. minus in front.
                // increase based on wich index we are on in bytesCompressed list.
                if (!(bytesCompressed.length <= compressedListIndex)) {
                    bytesCompressed[compressedListIndex] = (byte) -notCompressed;
                    compressedListIndex++;
                }

                // loops and add bytes that could not be compressed
                for (int j = bytesDone; j < compressIndex; j++) {
                    if (bytesCompressed.length <= compressedListIndex || bytesInput.length <= j) {
                        break;
                    }
                    bytesCompressed[compressedListIndex] = bytesInput[j];
                    compressedListIndex++;
                }

                // how far back and to find compression and the leght of the retrival
                if (!(bytesCompressed.length <= compressedListIndex)) {
                    bytesCompressed[compressedListIndex] = (byte) (compressIndex - match);
                    compressedListIndex++;
                    bytesCompressed[compressedListIndex] = (byte) lengthCompress;
                    compressedListIndex++;
                }

                bytesDone = compressIndex + lengthCompress;
                i += lengthCompress;
            }
        }

        int notCompressed = bytesInput.length - bytesDone;

        // have to add all the bytes that were not compressed
        if (compressedListIndex < bytesCompressed.length) {
            bytesCompressed[compressedListIndex] = (byte) -notCompressed;
            compressedListIndex++;
        }

        for (int i = bytesDone; i < bytesInput.length; i++) {
            if (compressedListIndex >= bytesCompressed.length) {
                break;
            }
            bytesCompressed[compressedListIndex] = bytesInput[i];
            compressedListIndex++;
        }

        byte[] buffer = bytesCompressed;

        // fix by placing it in its own method
        fixEmptyBufferSize(buffer, compressedListIndex);

        writeFile();
    }

    public void fixEmptyBufferSize(byte[] buffer, int bufferLength) {
        bytesCompressed = new byte[bufferLength];
        for (int i = 0; i < bufferLength; i++) {
            bytesCompressed[i] = buffer[i];
        }
    }

    public void writeFile() throws IOException {
        Files.write(Paths.get(outputfile.getPath()), bytesCompressed);
    }

    /**
     * @param input
     * @param indexStart
     * @return index if found, -1 if no matching byte sequnce is found
     * <p>
     * Hvor langt skal jeg søke bakover??
     * lengde på søkeord jeg skal matche med??
     */
    public int findMatch(ArrayList<Byte> input, int indexStart) {

        int lookbackIndex = indexStart - SIZE_LOOKING_BACK;
        int distanceBack = SIZE_LOOKING_BACK;

        if (lookbackIndex < 0) { // if lookbackindex goes beyond startindex
            lookbackIndex = 0;
            distanceBack = indexStart;
        }

        int returnindex = 0;
        boolean found = false; // true if found matching byte sequnce

        // iterate through list of bytes to be analyzed

        for (int i = lookbackIndex; i < lookbackIndex + distanceBack; i++) {

            found = true;
            int index = 0; // index we starter å lete på i input

            for (int j = 0; j < input.size(); j++) {

                if (bytesInput[i + j] != input.get(index)) {
                    found = false; // if iterated over and not found
                    break; // then we break out of loop
                }

                if (j == 0) {
                    returnindex = j + i;
                }
                index++;
            }
            // We have reached the end of input, break out of loop then
            if (index == input.size()) {
                break;
            }

        }
        if (found) {
            return returnindex;
        }

        return -1; // if no match
    }
}