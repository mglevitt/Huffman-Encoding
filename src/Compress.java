/*
 * Name: Maxwell Levitt
 */

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Compress the first given file to the second given file using Huffman coding
 *
 * @author Maxwell Levitt
 * @since 6/1/20
 */
public class Compress {

    private static final int EXP_ARG = 2; // number of expected arguments
    // alphabet size of extended ASCII
    private static final int NUM_CHARS = 256;

    public static void main(String[] args) throws IOException {

        // Check if the number of arguments is correct
        if (args.length != EXP_ARG) {
            System.out.println("Invalid number of arguments.\n" +
                    "Usage: ./compress <infile outfile>.\n");
            return;
        }

        // read all the bytes from the given file and make it to a byte array
        byte[] input = Files.readAllBytes(Paths.get(args[0]));

        FileOutputStream file = new FileOutputStream(args[1]);
        DataOutputStream out = new DataOutputStream(file);
        BitOutputStream bitOut = new BitOutputStream(out);

        int[] array = new int[NUM_CHARS];
        for (int i = 0; i < input.length; i ++) {
            int ascii = input[i] & 0xff;
            array[ascii] ++;
        }

        // construct HCTree from the file
        HCTree tree = new HCTree();
        tree.buildTree(array);
        // write number of bytes to out file
        out.writeInt(input.length);
        // encode HCTree and every byte
        tree.encodeHCTree(tree.getRoot(), bitOut);
        for (byte i : input) {
            tree.encode(i, bitOut);
        }


        // There might be several padding bits in the bitOut that we haven't written, so
        // flush it first.
        bitOut.flush();
        out.close();
        file.close();
    }
}