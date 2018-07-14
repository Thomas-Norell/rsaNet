package client;

import server.Account;

public class ReadInbox extends Request {
    public ReadInbox(Account sender) {
        this.sender = sender;
    }
}
