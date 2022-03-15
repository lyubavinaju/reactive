package currency;

import java.util.Map;

public class Currency {
    private static final Map<String, Double> currencies =
            Map.of("rub", 1.0, "usd", 120.0, "eur", 150.0);

    public static boolean isAvailableCurrency(String currency) {
        return currencies.containsKey(currency);
    }

    public static Double getCoefficient(String currency) {
        if (!isAvailableCurrency(currency)) {
            throw new IllegalArgumentException();
        }
        return currencies.get(currency);
    }
}
