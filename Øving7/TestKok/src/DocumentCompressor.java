
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DocumentCompressor {
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public DocumentCompressor(){
    }

    public void compressDocument(String path, String outpath) throws IOException {
        LZ77 lz77 = new LZ77();
        byte[] compressedBytes = lz77.compress(path);

        Huffman huffman = new Huffman();
        huffman.compress(compressedBytes, outpath);
    }
}