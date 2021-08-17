/*
 * Name: Maxwell Levitt
 */

import java.io.*;
import java.util.Stack;
import java.util.PriorityQueue;

/**
 * The Huffman Coding Tree
 */
public class HCTree {
    // alphabet size of extended ASCII
    private static final int NUM_CHARS = 256;
    // number of bits in a bytef
    private static final int BYTE_BITS = 8;

    // the root of HCTree
    private HCNode root;
    // the leaves of HCTree that contain all the symbols
    private HCNode[] leaves = new HCNode[NUM_CHARS];
    private PriorityQueue<HCNode> Q;

    /**
     * The Huffman Coding Node
     */
    protected class HCNode implements Comparable<HCNode> {

        byte symbol; // the symbol contained in this HCNode
        int freq; // the frequency of this symbol
        HCNode c0, c1, parent; // c0 is the '0' child, c1 is the '1' child

        /**
         * Initialize a HCNode with given parameters
         *
         * @param symbol the symbol contained in this HCNode
         * @param freq   the frequency of this symbol
         */
        HCNode(byte symbol, int freq) {
            this.symbol = symbol;
            this.freq = freq;
        }

        /**
         * Getter for symbol
         *
         * @return the symbol contained in this HCNode
         */
        byte getSymbol() {
            return this.symbol;
        }

        /**
         * Setter for symbol
         *
         * @param symbol the given symbol
         */
        void setSymbol(byte symbol) {
            this.symbol = symbol;
        }

        /**
         * Getter for freq
         *
         * @return the frequency of this symbol
         */
        int getFreq() {
            return this.freq;
        }

        /**
         * Setter for freq
         *
         * @param freq the given frequency
         */
        void setFreq(int freq) {
            this.freq = freq;
        }

        /**
         * Getter for '0' child of this HCNode
         *
         * @return '0' child of this HCNode
         */
        HCNode getC0() {
            return c0;
        }

        /**
         * Setter for '0' child of this HCNode
         *
         * @param c0 the given '0' child HCNode
         */
        void setC0(HCNode c0) {
            this.c0 = c0;
        }

        /**
         * Getter for '1' child of this HCNode
         *
         * @return '1' child of this HCNode
         */
        HCNode getC1() {
            return c1;
        }

        /**
         * Setter for '1' child of this HCNode
         *
         * @param c1 the given '1' child HCNode
         */
        void setC1(HCNode c1) {
            this.c1 = c1;
        }

        /**
         * Getter for parent of this HCNode
         *
         * @return parent of this HCNode
         */
        HCNode getParent() {
            return parent;
        }

        /**
         * Setter for parent of this HCNode
         *
         * @param parent the given parent HCNode
         */
        void setParent(HCNode parent) {
            this.parent = parent;
        }

        /**
         * Check if the HCNode is leaf (has no children)
         *
         * @return if it's leaf, return true. Otherwise, return false.
         */
        boolean isLeaf() {
            if (this.c0 == null) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * String representation
         *
         * @return string representation
         */
        public String toString() {
            return "Symbol: " + this.symbol + "; Freq: " + this.freq;
        }

        /**
         * Compare two nodes
         *
         * @param o node to compare
         * @return int positive if this node is greater
         */
        public int compareTo(HCNode o) {
            if (this.freq > o.getFreq()) {
                return 1;
            } else if (this.freq < o.getFreq()) {
                return -1;
            } else {
                if ((this.symbol & 0xff) > (o.getSymbol() & 0xff)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    }

    /**
     * Returns the root node
     *
     * @return root node
     */
    public HCNode getRoot() {
        return root;
    }

    /**
     * Sets the root node
     *
     * @param root node to set
     */
    public void setRoot(HCNode root) {
        this.root = root;
    }

    /**
     * A method to build a huffman search tree from a int array of the 256 frequencies of all the ascii values of the
     * characters.
     *
     * @param freq - A int array of the frequencies of all the ascii values of the characters.
     */
    public void buildTree(int[] freq) {
        char temp;
        HCNode curr = null;
        Q = new PriorityQueue<>();
        for (int i = 0; i < NUM_CHARS; i ++) {
            temp = (char) i;
            curr = new HCNode((byte) temp, freq[i]);
            leaves[i] = curr;
            if (curr.getFreq() > 0) {
                Q.add(curr);
            }

        }
        HCNode one;
        HCNode two;
        while(Q.size() > 1) {
            one = Q.poll();
            two = Q.poll();
            curr = new HCNode(one.getSymbol(), one.getFreq() + two.getFreq());
            curr.setC0(one);
            curr.setC1(two);
            one.setParent(curr);
            two.setParent(curr);
            Q.add(curr);
        }
        root = curr;
    }

    /**
     * Encodes a given symbol to a BitOutputStream
     *
     * @param symbol - The inputted symbol
     * @param out - The location the symbol will be encoded to.
     * @throws IOException
     */
    public void encode(byte symbol, BitOutputStream out) throws IOException {
        int ascii = symbol & 0xff;
        Stack<Integer> bits = new Stack<>();
        HCNode curr = leaves[ascii];
        HCNode parent;
        while (curr != this.root) {
            parent = curr.getParent();
            if (parent.getC0() == curr) {
                bits.push(0);
            } else {
                bits.push(1);
            }
            curr = parent;
        }
        while(bits.isEmpty() == false) {
            out.writeBit(bits.pop());
        }
    }

    /**
     * A method to decode a BitInputSteam
     *
     * @param in - The BitInputStream being decoded
     * @return Returns the symbol that is decoded from the bits.
     * @throws IOException
     */
    public byte decode(BitInputStream in) throws IOException {
        HCNode curr = this.root;
        while (curr.isLeaf() == false) {
            // A loop to go through all the bits in the input stream
            int temp = in.readBit();
            if (temp == 0) {
                curr = curr.getC0();
            } else {
                curr = curr.getC1();
            }
        }
        return curr.getSymbol();
    }

    /**
     * A method to encode a node to out stream of bits
     *
     * @param node - The inputted node to create bit stream for
     * @param out - The inputted BitOutputStream
     * @throws IOException
     */
    public void encodeHCTree(HCNode node, BitOutputStream out) throws IOException {
        if (node.isLeaf() == true) {
            out.writeBit(1);
            out.writeByte(node.getSymbol());
        } else {
            out.writeBit(0);
            encodeHCTree(node.getC0(), out);
            encodeHCTree(node.getC1(), out);
        }
    }

    /**
     * A method to decode a BitInputStream
     *
     * @param in - The BitInputStream of data to be decoded
     * @return Returns the Node found from the inputted data.
     * @throws IOException
     */
    public HCNode decodeHCTree(BitInputStream in) throws IOException {
        byte data;
        HCNode curr;
        HCNode child1;
        HCNode child2;
        if (in.readBit() == 0) {
            child1 = decodeHCTree(in);
            child2 = decodeHCTree(in);
            curr = new HCNode(child1.getSymbol(), 0);
            curr.setC0(child1);
            curr.setC1(child2);
            child1.setParent(curr);
            child2.setParent(curr);
            return curr;
        } else {
            return new HCNode(in.readByte(), 0);
        }

    }

    /**
     * A recursive inorder function to traverse tree.
     * @param root - The root of the tree
     */
    public void inorder(HCNode root) {
        if (root != null) {
            inorder(root.getC0());
            System.out.println(root.toString());
            inorder(root.getC1());
        }
        return;
    }
}