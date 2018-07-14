package rsa;

import java.util.ArrayList;
import java.math.BigInteger;

public class PadBitSequence {


    //Prepares each subset of the char[] for encryption
    public static ArrayList<Character[]> doChunking(Character[] message, PublicKeys pubKeys){
        int chunkSize = pubKeys.n.toString(2).length()/16 - 1; //chars are 16 bits,log(2^17-1)/log(10) = 5, each char is 5 digits in decimal
        ArrayList<Character[]> chunks = new ArrayList<>();
        int i = 0;
        while (i < message.length) {
            Character[] thisChunk = new Character[Math.min(chunkSize, message.length - i)];
            for (int j = 0; j < thisChunk.length; j++) {
                thisChunk[j] = message[i];
                i++;
            }
            chunks.add(thisChunk);
        }
        return chunks;

    }
    public static Character[] deChunk(ArrayList<Character[]> chunks) {
        ArrayList<Character> foo = new ArrayList<>();
        for (Character[] chs : chunks) {
            for (Character c : chs) {
                foo.add(c);
            }
        }
        return foo.toArray(new Character[foo.size()]);
    }
    private static BigInteger pad(Character[] chunk) {
        String bits = "";
        for (char ch : chunk) {
            String thisChar = Integer.toBinaryString(ch);
            while (thisChar.length() < 16) {
                thisChar = '0' + thisChar;
            }
            bits += thisChar;

        }
        //TODO: Random Padding for extra security
        bits = '1' + bits; //Throwing a '1' at the beginning to ensure proper parsing during decryption

        return new BigInteger(bits, 2);
    }

    private static Character[] dePad(BigInteger chunk) {
        String bits = chunk.toString(2);
        ArrayList<Character> thisChunk = new ArrayList<>();
        for (int i = 1; i <= bits.length() - 16; i+=16) {
            thisChunk.add((char) Integer.parseInt(bits.substring(i, i + 16), 2));
        }
        return thisChunk.toArray(new Character[thisChunk.size()]);
    }


    public static BigInteger encrypt(Character[] chunk, PublicKeys pubKeys) {
        BigInteger paddedPlain = pad(chunk);
        return paddedPlain.modPow(pubKeys.e, pubKeys.n);
    }

    public static Character[] decrypt(BigInteger chunk, PrivateKeys privKeys) {
        BigInteger decrypted = chunk.modPow(privKeys.d, privKeys.n);
        return dePad(decrypted);
    }


}
