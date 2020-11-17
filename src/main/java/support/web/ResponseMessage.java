package support.web;

public class ResponseMessage {

    public static final ResponseMessage CLOSE = new ResponseMessage(Type.Error, null);

    public enum Type {
        Reply("+"),
        Error("-");
        private final String code;

        Type(String code) {
            this.code = code;
        }
    }

    private final Type type;
    private final String data;

    public ResponseMessage(Type type, String data) {
        this.type = type;
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public String toMessageString() {
        return new StringBuilder().
                append(type.code).append(' ').
                append(data).
                toString();
    }

    public static ResponseMessage reply(String data) {
        return new ResponseMessage(Type.Reply, data);
    }

    public static ResponseMessage error(String data) {
        return new ResponseMessage(Type.Error, data);
    }
}
