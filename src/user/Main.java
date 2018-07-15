package user;

import client.Client;
import rsa.PrivateKeys;
import server.Account;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException{
        //keygen *username*
        //get *contact to get*
        //send *recipient username* *text*
        //readinbox
        //startchat *correspondant's username*
        Client client;
        client = new Client();
        client.startConnection("174.57.81.112", 6666);
        Client readerC;
        readerC = new Client();
        readerC.startConnection("174.57.81.112", 6666);
        String mode;
        try {
            mode = args[0];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Enter one of: keygen, read, compose");
        }
        if (mode.equals("keygen")) {
            Procedures.newAccount(args[1], client);
        }
        else if (mode.equals("get")) {
            Procedures.contactRequest(restoreAccount("me.contact"), args[1], client);
        }
        else if (mode.equals("send")) {
            Procedures.send(restoreAccount("me.contact"), restoreAccount(args[1]), args[2], client);

        }
        else if (mode.equals("readinbox")) {
            Account a = restoreAccount("me.contact");
            PrivateKeys k = null;
            try {
                ObjectReader rK = new ObjectReader(a.getUserName() + ".priv");
                k = (PrivateKeys) rK.readObject();
            }
            catch (FileNotFoundException e) {
                throw new Error("You must run keygen first!");
            }
            ArrayList<String> messages= Procedures.read(a, k, client);
            for (String m : messages) {
                System.out.println(m);
            }
        }
        else if (mode.equals("startchat")) {

            Account myAccount = restoreAccount("me.contact");
            PrivateKeys k = null;
            try {
                ObjectReader rK = new ObjectReader(myAccount.getUserName() + ".priv");
                k = (PrivateKeys) rK.readObject();
            }
            catch (FileNotFoundException e) {
                throw new Error("You must run keygen first!");
            }
            Account correspondantAccount = restoreAccount(args[1]);
            PrintInbox reader = new PrintInbox(myAccount, k, readerC);
            reader.start();
            InputStreamReader inStream = new InputStreamReader (System.in);
            BufferedReader userInput = new BufferedReader (inStream);
            String inputValues = userInput.readLine();


            while (!".".equals(inputValues)) {
                Procedures.send(myAccount, correspondantAccount, inputValues, client);
                //do your stuff
                //..and after done, read the next line of the user input.
                inputValues = userInput.readLine();


            }
        }
        else {
            throw new IllegalArgumentException("Enter one of: keygen, read, compose");
        }
    }
    private static Account restoreAccount(String name) {
        Account a = null;
        try {
            ObjectReader acc = new ObjectReader(name);
            a = (Account) acc.readObject();
        }
        catch (FileNotFoundException e) {
            throw new Error("You must run keygen first!");
        }
        return a;
    }
}
