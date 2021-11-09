import java.io.File;
import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        File test = new File("src/TestFiles/Test.txt");

        File LZ77Comp = new File("src/TestFiles/LZ77Comp.txt");
        File HuffmanComp = new File("src/TestFiles/HuffmanComp.txt");

        File HuffmanDecomp = new File("src/TestFiles/HuffmanDecomp.txt");
        File LZ77Decomp = new File("src/TestFiles/LZ77Decomp.txt");

        CompLZ77 compLZ77 = new CompLZ77(test, LZ77Comp);
        compLZ77.compressFile();

        HuffmanEncode.compress(LZ77Comp.toString(), HuffmanComp.toString());

        HuffmanDecode.deCompress(HuffmanComp.toString(), HuffmanDecomp.toString());

        DecompLZ77 decompLZ77 = new DecompLZ77(HuffmanDecomp, LZ77Decomp);
        decompLZ77.decompress();
    }
}
