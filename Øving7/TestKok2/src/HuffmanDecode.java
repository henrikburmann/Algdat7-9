import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class HuffmanDecode {
    private static Huffman huffman;
    private static final int ARRAY_SIZE = 256;

    public HuffmanDecode(){}
    
    public static void deCompress(String pathToCompressed,
                             String pathToDecompressed) throws IOException {
        File input = new File(pathToCompressed);
        File output = new File(pathToDecompressed);
        byte[] decoded = Files.readAllBytes(input.toPath());
        int[] frequencyArray = readFrequencyArray(input);
        String encodedBits = "";
        huffman = new Huffman(frequencyArray);


        String[] encodings = huffman.getEncodingArray();
        for(int i = frequencyArray.length; i < decoded.length; i++){
            encodedBits += (Integer.toBinaryString((decoded[i]+ARRAY_SIZE)%ARRAY_SIZE)).substring(1);
        }
        encodedBits = encodedBits.substring(encodedBits.indexOf(encodings[ARRAY_SIZE]) + encodings[ARRAY_SIZE].length(),encodedBits.lastIndexOf(encodings[ARRAY_SIZE]));

        new FileOutputStream(output.getPath()).write(getDecodedByteArray(encodedBits));
    }

    private static int[] readFrequencyArray(File file) throws IOException {
        FileReader fileReader = new FileReader(file.getPath());
        int[] frequencyArray = new int[ARRAY_SIZE + 1];
        for (int i = 0; i < frequencyArray.length; i++) {
            frequencyArray[i] += fileReader.read();
        }
        fileReader.close();
        return frequencyArray;
    }

    private static byte[] getDecodedByteArray(String encodedBits) {
        ArrayList<Byte> unknownSizeByteArray = new ArrayList<>();
        for(int i = 0; i < encodedBits.length()+1; i++){
            if(huffman.decodeHuffmanCode(encodedBits.substring(0, i))!= null){
                unknownSizeByteArray.add(huffman.decodeHuffmanCode(encodedBits.substring(0,
                        i)).byteValue());
                encodedBits = encodedBits.substring(i);
                i=0;
            }
        }
        byte[] primitiveByteArray = new byte[unknownSizeByteArray.size()];
        for(int i = 0; i < unknownSizeByteArray.size(); i++){
            primitiveByteArray[i] = unknownSizeByteArray.get(i);
        }
        return primitiveByteArray;
    }

}