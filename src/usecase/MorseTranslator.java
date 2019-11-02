package usecase;

import material.Position;
import material.tree.binarytree.LinkedBinaryTree;

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
        StringBuilder messageDecode = new StringBuilder();
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
                    messageDecode.append(' ');
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
                messageDecode.append(currentPos.getElement());
                term = false;
                space = false;
                currentPos = morseTranslatorTree.root();
            }
        }

        return messageDecode.toString();
    }

    /**
     * Receives a String with a message in plaintext. This message
     * may contain any character in the charset.
     *
     * @param plainText
     * @return a morse code message
     */
    public String encode(String plainText) {
        StringBuilder messageEncode = new StringBuilder();
        for (int i = 0; i < plainText.length(); i++) {
            StringBuilder currentEncode = new StringBuilder();
            char currentI = plainText.charAt(i);
            encoding(currentI, morseTranslatorTree.root(), currentEncode);
            messageEncode.append(currentEncode);
        }
        return messageEncode.toString();
    }

    /**
     * Recursive method to iterate the tree in preOrder. The encode param is updated in each iteration
     *
     * @param currentI current letter to encode
     * @param currentPos current position while iterating
     * @param encode current text encoded in this currentPos
     * @return the current letter encoded in morse
     */
    private StringBuilder encoding(char currentI, Position<Character> currentPos, StringBuilder encode) {
        if (currentI == ' ') {
            encode.append(' ');
            return encode;
        }
        if (currentPos.getElement() != null && currentPos.getElement() == currentI) {
            if (morseTranslatorTree.hasLeft(currentPos) || morseTranslatorTree.hasRight(currentPos)) {
                encode.append(' ');
            }
            return encode;
        }

        if (morseTranslatorTree.hasLeft(currentPos)) {
            encode.append('.');
            StringBuilder ab = encoding(currentI, morseTranslatorTree.left(currentPos), encode);
            if (ab == null && morseTranslatorTree.hasRight(currentPos)) {
                encode.deleteCharAt(encode.length() - 1);
                encode.append('-');
                ab = encoding(currentI, morseTranslatorTree.right(currentPos), encode);
            }
            if (ab == null) {
                encode.deleteCharAt(encode.length() - 1);
            }
            return ab;
        }

        return null;
    }

}
