package user;

import client.*;
import rsa.Keys;
import rsa.PadBitSequence;
import rsa.PrivateKeys;
import rsa.PublicKeys;
import server.Account;
import server.Response;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Procedures {
    private static void validateResponse(Response r) {
        if (r instanceof Response.RequestFailure) {
            throw new Error("Something when't wrong!");
        }

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
    public static Account newAccount(String userName, Client client){
        Keys usersKeys = new Keys(1024);
        PrivateKeys prKeys = new PrivateKeys(usersKeys);
        PublicKeys puKeys = new PublicKeys(prKeys);
        ObjectWriter keysWriter = new ObjectWriter(userName + ".priv");
        keysWriter.writeObject(prKeys);

        Account user = new Account(userName, puKeys);
        ObjectWriter meWrite = new ObjectWriter( "me.contact");
        meWrite.writeObject(user);

        AccountRequest newUser = new AccountRequest(user);
        Response resp = client.sendMessage(newUser);
        validateResponse(resp);
        return new Account(userName, puKeys);
    }
    public static Account contactRequest(Account sender, String userName, Client client) {
        ContactRequest getFriend = new ContactRequest(sender, userName);
        Response resp = (Response) client.sendMessage(getFriend);
        validateResponse(resp);
        Account ra = (Account) resp.getContents();
        ObjectWriter cWrite = new ObjectWriter( ra.getUserName());
        cWrite.writeObject(ra);
        return ra;
    }
    public static void send(Account sender, Account recipient, String text, Client client) {
        Message m = new Message(sender, recipient, Compose.composeString(text, recipient.getPublicKeys()));
        Response resp = client.sendMessage(m);
        validateResponse(resp);
    }
    public static ArrayList<String> read(Account me, PrivateKeys keys, Client client) {
        ReadInbox readRequest = new ReadInbox(me);
        Response resp = client.sendMessage(readRequest);
        validateResponse(resp);
        List<Message> cipherText = (List<Message>) resp.getContents();
        ArrayList<String> messages = new ArrayList();
        for (Message m : cipherText) {
            messages.add(readString(m.getContent(), keys));
        }
        return messages;


    }


}
