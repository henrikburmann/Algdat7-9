
import java.io.IOException;

public class DocumentDecompressor {

    public DocumentDecompressor(){
    }

    public void decompressDocument(String inPath, String outPath) throws IOException {
        Huffman huffman = new Huffman();
        byte[] decompressedBytes = huffman.decompress(inPath);
        LZ77 lz77 = new LZ77();
        lz77.deCompress(decompressedBytes, outPath);

    }
}