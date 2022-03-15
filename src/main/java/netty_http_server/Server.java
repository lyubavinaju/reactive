package netty_http_server;

import currency.Currency;
import entity.Product;
import entity.User;
import io.reactivex.netty.protocol.http.server.HttpServer;
import reactive_mongo_driver.Database;
import rx.Observable;

import java.util.Arrays;

public class Server {
    private static Server instance;

    private Server() {
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void start(Database database) {
        HttpServer.newServer(8080).start((req, resp) -> {
            String[] request = Arrays.stream(req.getDecodedPath().split("/")).filter(s -> !s.isBlank()).toList().toArray(new String[0]);
            Observable<String> response;
            switch (request[0]) {
                case "products" -> {
                    /* /products/{userName} */
                    String userName = request[1];
                    response = database.getUser(userName)
                            .first()
                            .map(User::getCurrency)
                            .flatMap(currency -> database.getProducts().map(product -> product.getName() + " "
                                    + (Integer.parseInt(product.getCostInRubles()) / Currency.getCoefficient(currency)) + "\n"));
                }
                case "register_user" -> {
                    /* /register_user/{name}/{currency} */
                    String name = request[1];
                    String currency = request[2];
                    response = database.registerUser(new User(name, currency)).map(userRegistered -> {
                        if (userRegistered) {
                            return "User registered";
                        } else {
                            return "Something wrong. Can't register user";
                        }
                    });
                }
                case "add_product" -> {
                    /* /add_product/{name}/{costInRubles} */
                    String name = request[1];
                    String costInRubles = request[2];
                    response = database.addProduct(new Product(name, costInRubles)).map(productAdded -> {
                        if (productAdded) {
                            return "Product added";
                        } else {
                            return "Something wrong. Can't add product";
                        }
                    });
                }
                default -> response = Observable.just("Unknown command");
            }
            return resp.writeString(response);
        }).awaitShutdown();
    }
}