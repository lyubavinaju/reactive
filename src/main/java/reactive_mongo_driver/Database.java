package reactive_mongo_driver;

import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoDatabase;
import currency.Currency;
import entity.Product;
import entity.User;
import org.bson.Document;
import rx.Observable;

public class Database {
    public static final String DB = "testdb";
    private static final MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase(DB);

    public Observable<Boolean> registerUser(User user) {
        if (!Currency.isAvailableCurrency(user.getCurrency())) {
            System.err.println("Unavailable currency " + user.getCurrency());
            return Observable.just(false);
        }
        Observable<Boolean> successObservable = database.getCollection("users")
                .insertOne(new Document("name", user.getName())
                        .append("currency", user.getCurrency()))
                .map(success -> true)
                .singleOrDefault(false);
        return successObservable;
    }

    public Observable<User> getUser(String name) {
        return database.getCollection("users")
                .find(Filters.eq("name", name))
                .toObservable()
                .map(document -> new User(document.getString("name"), document.getString("currency")));
    }

    public Observable<Product> getProducts() {
        return database.getCollection("products")
                .find()
                .toObservable()
                .map(document -> new Product(document.getString("name"), document.getString("costInRubles")));
    }

    public Observable<Boolean> addProduct(Product product) {
        try {
            Integer.parseInt(product.getCostInRubles());
        } catch (NumberFormatException e) {
            System.err.println("Incorrect number " + product.getCostInRubles());
            return Observable.just(false);
        }
        Observable<Boolean> successObservable = database.getCollection("products")
                .insertOne(new Document("name", product.getName())
                        .append("costInRubles", product.getCostInRubles()))
                .map(success -> true)
                .singleOrDefault(false);
        return successObservable;
    }
}
