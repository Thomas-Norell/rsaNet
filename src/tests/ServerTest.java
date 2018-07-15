package tests;


import client.*;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rsa.PrivateKeys;
import rsa.PublicKeys;
import server.Account;
import server.Response;
import server.Server;
import user.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServerTest {
    Client client;
    Client client2;

    @Before
    public void setup() {
        client = new Client();
        client.startConnection("127.0.0.1", 6666);
        client2 = new Client();
        client2.startConnection("127.0.0.1", 6666);
    }

    @After
    public void tearDown() {
        client.stopConnection();
    }

    @Test
    public void testSetup() {
        //Setting up Thomas' account
        String userName = "thomas";
        String[] args = {"setup", userName};
        Setup.main(args);
        PublicKeys keys = null;
        try {
            ObjectReader keyFile = new ObjectReader(userName + ".pub");
            keys = (PublicKeys) keyFile.readObject();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Account thomas = new Account(userName, keys);
        AccountRequest newThomas = new AccountRequest(thomas);
        Response resp1 = client.sendMessage(newThomas);


        //Setting up Ava's account
        String user2 = "ava";
        String[] args2 = {"setup", user2};
        Setup.main(args2);
        try {
            ObjectReader keyFile = new ObjectReader(userName + ".pub");
            keys = (PublicKeys) keyFile.readObject();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectReader keyFile = new ObjectReader("ava.pub");
            keys = (PublicKeys) keyFile.readObject();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Account ava = new Account(user2, keys);
        AccountRequest newAva = new AccountRequest(ava);
        Response resp2 = client2.sendMessage(newAva);
        assertTrue(resp2 instanceof Response.RequestSuccess);


        //Thomas requests ava's keys
        ContactRequest getAva = new ContactRequest(thomas, "ava");
        Account resp3 = (Account) client.sendMessage(getAva).getContents();

        //Encrypt and transmit message to server
        Message toAva = new Message(thomas, resp3, Compose.composeString("I love you.", resp3.getPublicKeys()));
        Response resp4 = client.sendMessage(toAva);
        assertTrue(resp4 instanceof Response.RequestSuccess);

        ReadInbox seeMyMessageFromThomas = new ReadInbox(ava);
        ArrayList<Message> resp5 = (ArrayList<Message>) client2.sendMessage(seeMyMessageFromThomas).getContents();

        PrivateKeys avaPrivate = null;
        try {
            ObjectReader keyFile = new ObjectReader("ava.priv");
            avaPrivate = (PrivateKeys) keyFile.readObject();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String originalMessage  = Read.readString(resp5.get(0).getContent(), avaPrivate);
        assertEquals(originalMessage, "I love you.");

    }

    @Test
    public void testProduction() {
        String[] args = {"keygen", "thomas"};
        try {
            Main.main(args);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
