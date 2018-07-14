package server;
import client.Request;
import user.ObjectReader;

import java.net.*;
import java.io.*;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    protected Database users;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Server me;

    public void start(int port) {
        me = this;

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
                new ClientHandler(serverSocket.accept()).start();
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
    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
                Request clientRequest;
                while ((clientRequest = (Request) in.readObject()) != null && in.available() > 0) {
                    out.writeObject(RequestHandler.handle(clientRequest, me));
                    System.out.println("Handling Request from: " + clientRequest.getSender().getUserName());
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            stopClient();
        }

        public void stopClient() {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}