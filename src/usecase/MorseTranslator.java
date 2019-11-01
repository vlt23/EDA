package usecase;


import material.Position;
import material.tree.binarytree.LinkedBinaryTree;

import java.util.ArrayList;
import java.util.Arrays;

public class MorseTranslator {

    private LinkedBinaryTree<Character> morseTranslatorTree;

    /**
     * Generates a new MorseTranslator instance given two arrays:
     * one with the character set and another with their respective
     * morse code.
     *
     * @param charset
     * @param codes
     */
    public MorseTranslator(char[] charset, String[] codes) {
        morseTranslatorTree = new LinkedBinaryTree<>();
        if (charset.length != codes.length) {
            throw new RuntimeException("???");
        }
        Position<Character> p0 = morseTranslatorTree.addRoot(null);
        Position<Character> leftP, rightP;

        for (int i = 0; i < charset.length; i++) {
            String code = codes[i];
            Position<Character> lastP = p0;
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '.') {
                    if (morseTranslatorTree.hasLeft(lastP)) {
                        leftP = morseTranslatorTree.left(lastP);
                    } else {
                        leftP = morseTranslatorTree.insertLeft(lastP, null);
                    }
                    lastP = leftP;
                } else if (code.charAt(j) == '-') {
                    if (morseTranslatorTree.hasRight(lastP)) {
                        rightP = morseTranslatorTree.right(lastP);
                    } else {
                        rightP = morseTranslatorTree.insertRight(lastP, null);
                    }
                    lastP = rightP;
                }
            }
            morseTranslatorTree.replace(lastP, charset[i]);
        }
    }

    /**
     * Decodes a String with a message in morse code and returns
     * another String in plaintext. The input String may contain
     * the characters: ' ', '-' '.'.
     *
     * @param morseMessage
     * @return a plain text translation of the morse code
     */
    public String decode(String morseMessage) {
        //ArrayList<Character> messageDecode = new ArrayList<>(morseMessage.length() / 2);
        Character[] messageDecode = new Character[morseMessage.length()];
        int messDecPos = 0;
        boolean term = false;
        boolean space = true;
        Position<Character> currentPos = morseTranslatorTree.root();
        for (int i = 0; i < morseMessage.length(); i++) {
            if (morseMessage.charAt(i) == '.' && morseTranslatorTree.hasLeft(currentPos)) {
                currentPos = morseTranslatorTree.left(currentPos);
                if (i == morseMessage.length() - 1 || morseMessage.charAt(i + 1) == ' ') {
                    term = true;
                } else {
                    space = true;
                }

            } else if (morseMessage.charAt(i) == '-' && morseTranslatorTree.hasRight(currentPos)) {
                currentPos = morseTranslatorTree.right(currentPos);
                if (i == morseMessage.length() - 1 || morseMessage.charAt(i + 1) == ' ') {
                    term = true;
                } else {
                    space = true;
                }

            } else {
                if (space) {
                    messageDecode[messDecPos] = ' ';
                    messDecPos++;
                    currentPos = morseTranslatorTree.root();
                }
                space = true;
            }

            if (i + 1 < morseMessage.length() &&
                    ((morseMessage.charAt(i + 1) == '.' && !morseTranslatorTree.hasLeft(currentPos))
                            || (morseMessage.charAt(i + 1) == '-' && !morseTranslatorTree.hasRight(currentPos)))) {
                term = true;
            }

            if (term) {
                messageDecode[messDecPos] = currentPos.getElement();
                term = false;
                space = false;
                messDecPos++;
                currentPos = morseTranslatorTree.root();
            }
        }
        //return Arrays.toString(messageDecode.toArray()).replace("[", "").replace("]", "");
        //return String.valueOf(messageDecode).replace("[", "").replace("]", "")
          //      .replaceAll(",", "");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < messDecPos; i++) {
            result.append(messageDecode[i]);
        }
        return result.toString();
    }


    /**
     * Receives a String with a message in plaintext. This message
     * may contain any character in the charset.
     *
     * @param plainText
     * @return a morse code message
     */
    public String encode(String plainText) {
        throw new RuntimeException("Not yet implemented");
    }


}
