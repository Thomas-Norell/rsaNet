package server;

import client.*;

public class RequestHandler {
    public static Response handle(Request request, Server server) {

        if (request instanceof ContactRequest) {
            if (server.users.getUser(((ContactRequest) request).getUserName()) != null) {
                Account returnedUser = server.users.getUser(((ContactRequest) request).getUserName());
                return new Response(new Account(returnedUser.getUserName(), returnedUser.getPublicKeys())); //We don't want to send the requested user's inbox!
            }
            return new Response.RequestFailure();

        } else if (request instanceof AccountRequest) {
            if (server.users.getUser(((AccountRequest) request).getAccount().getUserName()) == null) {
                server.users.addUser(((AccountRequest) request).getAccount());
                return new Response.RequestSuccess();
            } else {
                return new Response.RequestFailure();
            }

        } else if (request instanceof Message) {
            if (server.users.getUser(request.getRecipient().getUserName()) == null) {
                return new Response.RequestFailure();
            }
            server.users.getUser(request.getRecipient().getUserName()).addInbox((Message) request);
            return new Response.RequestSuccess();

        } else if (request instanceof ReadInbox) {
            if (server.users.getUser(request.getSender().getUserName()) == null) {
                return new Response.RequestFailure();
            }
            return new Response(server.getUser(request.getSender().getUserName()).getInbox());
            //return new Response(server.users.getUser(request.getSender().getUserName()).getInbox());

        }
        return new Response.RequestFailure();
    }
}
