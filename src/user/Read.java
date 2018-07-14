package user;
import rsa.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;

public class Read {
    public static void main(String[] args) throws FileNotFoundException{
        String filename;
        String privKeysFile;
        try {
            filename = args[1];
            privKeysFile = args[2];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("You must pass in a file to encrypt and the receiver's public keys");
        }
        ObjectReader encryptedFile = new ObjectReader(filename);
        ArrayList<BigInteger> encryptedMessage = (ArrayList<BigInteger>) encryptedFile.readObject();

        ObjectReader privKeys = new ObjectReader(privKeysFile);
        PrivateKeys myKeys = (PrivateKeys) privKeys.readObject();

        ArrayList<Character[]> chunks = new ArrayList<>();

        for (BigInteger n : encryptedMessage) {
            chunks.add(PadBitSequence.decrypt(n, myKeys));
        }
        Character[] message = PadBitSequence.deChunk(chunks);
        char[] rawMessage = new char[message.length];
        for (int i = 0; i < message.length; i++) {
            rawMessage[i] = message[i];
        }
        FileUtils.writeCharArray(filename.substring(0, filename.length()-4), rawMessage);
    }
    public static String readString(ArrayList<BigInteger> encryptedMessage, PrivateKeys key) {
        ArrayList<Character[]> chunks = new ArrayList<>();

        for (BigInteger n : encryptedMessage) {
            chunks.add(PadBitSequence.decrypt(n, key));
        }
        Character[] message = PadBitSequence.deChunk(chunks);
        char[] rawMessage = new char[message.length];
        for (int i = 0; i < message.length; i++) {
            rawMessage[i] = message[i];
        }
        return String.valueOf(rawMessage);
    }

}
