package client;

import server.Account;

import java.math.BigInteger;
import java.util.ArrayList;

public class Message extends Request {
    private ArrayList<BigInteger> content;
    private long timeStamp;
    public Message(Account sender, Account recipient, ArrayList<BigInteger> content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timeStamp = System.currentTimeMillis();
    }

    public ArrayList<BigInteger> getContent() {
        return content;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
