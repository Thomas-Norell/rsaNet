package client;
/*
Asks the server for the public keys associated with a specific username
 */

import rsa.PublicKeys;
import server.Account;

public class ContactRequest extends Request {

    private String userName; //The userName of whom we're looking for

    public ContactRequest(Account sender, String userName) {
        this.sender = sender;
        this.recipient = new Account("Server", new PublicKeys());
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
