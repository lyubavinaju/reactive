import netty_http_server.Server;
import reactive_mongo_driver.Database;

public class Main {
    public static void main(String[] args) {
        Server.getInstance().start(new Database());
    }
}
