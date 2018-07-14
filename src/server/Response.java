package server;

import java.io.Serializable;

public class Response implements Serializable{
    private Object contents;
    public Response(Object o) {
        this.contents = o;
    }

    public Object getContents() {
        return contents;
    }
    public static class RequestFailure extends Response {
        public RequestFailure() {
            super(false);
        }
    }
    public static class RequestSuccess extends Response {
        public RequestSuccess() {
            super(true);
        }
    }

}
