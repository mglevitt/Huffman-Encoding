/*
 * Name: Maxwell Levitt
 */

import static org.junit.Assert.*;

public class HCTreeTester {



    @org.junit.Test
    public void test_buildTree() {
        HCTree tester = new HCTree();
        int[] test_array = new int[256];
        String test_word = "abcdef\n";
        test_array[test_word.codePointAt(0)] = 17;
        test_array[test_word.codePointAt(1)] = 8;
        test_array[test_word.codePointAt(2)] = 7;
        test_array[test_word.codePointAt(3)] = 14;
        test_array[test_word.codePointAt(4)] = 9;
        test_array[test_word.codePointAt(5)] = 1;
        test_array[test_word.codePointAt(6)] = 1;
        tester.buildTree(test_array);
        tester.inorder(tester.getRoot());
    }

}