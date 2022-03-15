package entity;

public class Product {
    private final String name;
    private final String costInRubles;

    public String getName() {
        return name;
    }

    public String getCostInRubles() {
        return costInRubles;
    }

    public Product(String name, String costInRubles) {
        this.name = name;
        this.costInRubles = costInRubles;
    }
}
