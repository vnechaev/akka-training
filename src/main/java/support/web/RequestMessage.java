package support.web;

public class RequestMessage {

    private final String command;
    private final String[] data;

    public RequestMessage(String command, String[] data) {
        this.command = command;
        this.data = data;
    }

    public String getCommand() {
        return command;
    }

    public String[] getData() {
        return data;
    }

    public static RequestMessage parse(String data) {
        int space = data.indexOf(' ');
        if (space > 0)
            return new RequestMessage(data.substring(0, space), data.substring(space + 1).split(" "));
        else
            return new RequestMessage(data, new String[0]);
    }
}
