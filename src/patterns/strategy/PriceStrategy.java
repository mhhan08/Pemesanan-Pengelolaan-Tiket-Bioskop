package patterns.strategy;

public interface PriceStrategy {
    double calculate(double basePrice);
    String getName();
}
