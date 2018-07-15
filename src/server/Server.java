package server;
import client.Request;
import user.ObjectReader;

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    static volatile Database users;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public Account getUser(String userName) {
        return users.getUser(userName);
    }

    public void start(int port) {
        try {
            ObjectReader encryptedFile = new ObjectReader("users");
            users = (Database) encryptedFile.readObject();
        }
        catch (NullPointerException | FileNotFoundException e) {
            users = new Database();
            users.save();
            System.out.println("Creating database from scratch...");
        }

        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                new ClientHandler(serverSocket.accept(), this).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Server server=new Server();
        server.start(6666);
    }
}