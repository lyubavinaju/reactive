package entity;

public class User {
    private final String name;
    private final String currency;

    public User(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }
}
