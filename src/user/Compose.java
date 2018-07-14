package user;

import rsa.PadBitSequence;
import rsa.PublicKeys;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;

public class Compose {
    public static void main(String[] args) throws FileNotFoundException{
        String filename;
        String pubKeysFile;
        try {
            filename = args[1];
            pubKeysFile = args[2];
        }
        catch (ArrayIndexOutOfBoundsException e) {
              throw new IllegalArgumentException("You must pass in a file to encrypt and the receiver's public keys");
        }
        char[] text = FileUtils.readFile(filename);
        ObjectReader pubKeys = new ObjectReader(pubKeysFile);
        PublicKeys myKeys = (PublicKeys) pubKeys.readObject();
        Character[] readyToGo = new Character[text.length];
        for (int i = 0; i < text.length; i++) {
            readyToGo[i] = text[i];
        }

        ArrayList<Character[]> chunks = PadBitSequence.doChunking(readyToGo, myKeys);
        ArrayList<BigInteger> encoded = new ArrayList<>();
        for (Character[] ch : chunks) {
            encoded.add(PadBitSequence.encrypt(ch, myKeys));
        }
        ObjectWriter messageWriter = new ObjectWriter(filename + ".rsa");
        messageWriter.writeObject(encoded);
    }
    public static ArrayList<BigInteger> composeString(String m, PublicKeys keys) {
        char[] text = m.toCharArray();
        Character[] readyToGo = new Character[text.length];
        for (int i = 0; i < text.length; i++) {
            readyToGo[i] = text[i];
        }

        ArrayList<Character[]> chunks = PadBitSequence.doChunking(readyToGo, keys);
        ArrayList<BigInteger> encoded = new ArrayList<>();
        for (Character[] ch : chunks) {
            encoded.add(PadBitSequence.encrypt(ch, keys));
        }
        return encoded;

    }
}
