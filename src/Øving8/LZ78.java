package Øving8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    public char[] fileToByteArray() {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("src/Øving8/files/opg8-2021.pdf"));
            String content = new String(bytes);
            System.out.println(content);
            data = content.toCharArray();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Byte[] compress(String filePath) {
        return null;
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




    }





    public void deCompress(byte[] bytes, String decompPath) {

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


    public static void main(String[] args) {
        fileToByteArray();
    }
}
