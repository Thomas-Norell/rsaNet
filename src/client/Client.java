package client;
import server.Response;
import server.Server;

import java.net.*;
import java.io.*;
public class Client {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response sendMessage(Request request) {
        try {
            out.writeObject(request);
            return (Response) in.readObject();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}