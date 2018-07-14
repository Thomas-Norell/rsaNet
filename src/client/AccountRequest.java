package client;
import rsa.PublicKeys;
import server.Account;
import server.Server;

public class AccountRequest extends Request {
    private Account account;
    public AccountRequest(Account newAccount) {
        this.account = newAccount;
        this.recipient = new Account("Server", new PublicKeys());
        this.sender = newAccount;
    }
    public Account getAccount() {
        return account;
    }
}
