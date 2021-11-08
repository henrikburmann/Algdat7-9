package Øving8;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class LZ78 {

    char[] data;
    private static final int BUFFERSIZE = (1 << 11) -1; // 11 bits looking back
    public static final int POINTERSIZE = (1 << 4) -1 ; // 4 bits search word
    public static final int MIN_SIZE_POINTER = 3;

    /*
   - Bytter ut en String med en pointer (referanse til den stringen)
    eller at man bruker en dictonary hvor slike stringer occur.
   - searching buffer
   - coding buffer.

    1. gjør om en fil til bytes format
    2. tekstsøk (hvor langt bak? hvor lange ord?
     */

    /**
     * @return character array
     */
    public void fileToByteArray(String filePath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            String content = new String(bytes);
            data = content.toCharArray();
            System.out.println(content);
            System.out.println("ukomprimert lendgde: " + data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] compress(String filePath) {
        fileToByteArray(filePath); // fills data-array with byte stream
        ArrayList<Byte> compressed = new ArrayList<>();

        StringBuilder incompressible = new StringBuilder();

        for (int i = 0; i < data.length ; i++) {

            Pointer pointer = findPointer(i); // pointer for current look ahead buffer

            if (pointer != null) { // pointer not found
                if (incompressible.length() != 0) {
                    compressed.add((byte) incompressible.length());
                    for (int j = 0; j < incompressible.length() ; j++) {
                        compressed.add((byte) incompressible.charAt(j));
                    }
                    incompressible = new StringBuilder(); // new clean and empty stringbuilder
                }

                compressed.add((byte) ((pointer.getDistance() >> 4) | (1 << 7)));
                compressed.add((byte) ((pointer.getDistance() & 0x0F) << 4 | (pointer.getLength() -1)));
                i += pointer.getLength();

            }else {
                incompressible.append(data[i]);

                if (incompressible.length() == 127) {
                    compressed.add((byte)(incompressible.length()));
                    for (int j = 0; j < incompressible.length() ; j++) {
                        compressed.add((byte) incompressible.charAt(j));
                    }
                }
                i += 1;
            }
        }

        if (incompressible.length() != 0) {
            compressed.add((byte) (incompressible.length())); // length of sequnce of inconpressible bytes
            for (int i = 0; i < incompressible.length() ; i++) {
                compressed.add((byte) incompressible.charAt(i));
            }
        }

        return toByteArray(compressed); // return compressed array
    }

    public byte[] toByteArray(ArrayList<Byte> arrayList) {
        byte[] array = new byte[arrayList.size()];
        for (int i = 0; i < arrayList.size() ; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    /**
     *
     * @param index
     * @return
     */
    private Pointer findPointer(int index) {
        Pointer pointer = new Pointer();
        int max = index + POINTERSIZE;

        if (max > data.length-1) {
            max = data.length-1;
        }

        int min = index - BUFFERSIZE; // min index of window
        if (min < 0) {
            min = 0;
        }

        char[] buffer = Arrays.copyOfRange(data, min, index); // buffer for searching
        int i = index + MIN_SIZE_POINTER -1; // at least from least to current index (both excluded)

        outer:
        while (i <= max) {
            char[] searchWord = Arrays.copyOfRange(data, index, i + 1);

            int j = 0;

            while (searchWord.length + j <= buffer.length) { // Do not compare variables outside the searchbuffer array
                int k = searchWord.length -1; // index where letters do not match
                while (k >= 0 && searchWord[k] == buffer[j+k]) {
                    k--;
                }
                if (k < 0) { // all characters in the search word mathced the search buffer (highly unlikely)
                    pointer.setDistance(buffer.length - j);
                    pointer.setLength(searchWord.length);
                    i++;
                    continue outer; // continues loop with additional character in search word until it fails.

                } else {
                    int l = k-1; // last index of failed character from buffer in the search word if any
                    while (l >= 0 && searchWord[l] != buffer[j+k]) {
                        l--;
                    }
                    j += k-l; // slide scope acording to Boyer Moore
                }
            }
            break; // No match found for the last search word
        }

        if (pointer.getLength() > 0) {
            return pointer;
        }
        return null; // no match
    }

    /**
     *
     * @param bytes
     * @param decompPath
     */
    public void deCompress(byte[] bytes, String decompPath) throws IOException {

        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(decompPath)));

        ArrayList<Byte> b = new ArrayList<>();
        int currentIndex = 0;

        int i = 0;

        while (i < bytes.length-1) {
            byte condition = bytes[i];
            if (condition >= 0) {
                // condition = number of uncompressed bytes
                for (int j = 0; j < condition ; j++) {
                    b.add(bytes[i+j+1]);
                }
                currentIndex += condition;
                i += condition + 1;
            }
            else {
                int jump = ((condition & 127) << 4) | ((bytes[i+1] >> 4) & 15);
                int length = (bytes[i+1] & 0x0F) + 1;

                for (int j = 0; j < length ; j++) {
                    b.add(b.get(currentIndex - jump + j));
                }
                currentIndex += length;
                i+= 2;
            }
        }
        for (int j = 0; j < currentIndex ; j++) {
            output.write(b.get(i));
        }
        output.flush();
        output.close();

    }

    /**
     * Pointer that references back to re-occuring streams?
     * where and how much to compress
     */
    private class Pointer {
        private int length;
        private int distance;

        public Pointer() {
            this(-1,-1);
        }

        public Pointer(int length, int distance) {
            super();
            this.length = length;
            this.distance = distance;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getLength() {
            return length;
        }

        public int getDistance() {
            return distance;
        }
    }
}