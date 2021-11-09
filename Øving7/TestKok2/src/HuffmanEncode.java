import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class HuffmanEncode {
    private static final int ARRAY_SIZE = 256;
    private static Huffman huffman;

    public HuffmanEncode(){}
    public static void compress(String pathToFile, String pathToCompressed) throws IOException {
        File in = new File(pathToFile);
        File out = new File(pathToCompressed);
        int[] frequencyArray = getFrequencyArray(in);
        huffman = new Huffman(frequencyArray);
        String[] encodings = huffman.getEncodingArray();

        byte[] inputFile = Files.readAllBytes(in.toPath());

        String fileToString = "";
        FileWriter fileWriter = new FileWriter(out.getPath());
        for (int i = 0; i < frequencyArray.length; i++) {
            fileWriter.write(frequencyArray[i]);
        }

        fileWriter.close();
        FileOutputStream fileOutputStream = new FileOutputStream(out.getPath(), true);
        fileToString += encodings[256];
        for (byte b : inputFile) {
            fileToString += encodings[(b + ARRAY_SIZE) % ARRAY_SIZE];
            if (fileToString.length() % 64 == 0 && fileToString.length() > 0) {
                fileOutputStream.write(getEncodedByteArray(fileToString));
                fileToString = "";
            }
        }
        fileToString += encodings[ARRAY_SIZE];
        while ((fileToString).length() % 8 != 0) fileToString += "0";
        fileOutputStream.write(getEncodedByteArray(fileToString));
        fileOutputStream.close();
    }
    
    private static byte[] getEncodedByteArray(String fileToString) {
        ArrayList<Byte> encodedBytes = new ArrayList<>();

        while (fileToString.length() > 0) {
            encodedBytes.add((byte) Integer.parseInt((fileToString.length() >= 7 ? '1' + fileToString.substring(0, 7) : '1' + fileToString), 2));
            fileToString = fileToString.substring(Math.min(fileToString.length(), 7));
        }

        byte[] answer = new byte[encodedBytes.size()];
        for (int i = 0; i < encodedBytes.size(); i++) {
            answer[i] = encodedBytes.get(i);
        }
        return answer;
    }
    
    private static int[] getFrequencyArray(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        int[] frequencyArray = new int[ARRAY_SIZE + 1];
        
        for (int j = 0; j < file.length(); j++) {
            frequencyArray[(fileContent[j] + ARRAY_SIZE) % ARRAY_SIZE]++;
        }
        frequencyArray[frequencyArray.length - 1] = 1;
        return frequencyArray;
    }
}