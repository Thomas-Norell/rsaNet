package server;
import user.ObjectWriter;

import java.io.Serializable;
import java.util.HashMap;

public class Database implements Serializable {
    private volatile HashMap<String, Account> users;
    public Database() {
        users = new HashMap();
    }
    public void addUser(Account user) {
        users.put(user.getUserName(), user);
        save();
    }
    public Account getUser(String userName) {
        return users.get(userName);

    }
    public void save() {
        ObjectWriter data = new ObjectWriter("users");
        data.writeObject(this);
    }
}
