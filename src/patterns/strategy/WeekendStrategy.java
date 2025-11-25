package patterns.strategy;

public class WeekendStrategy implements PriceStrategy {
    @Override
    public double calculate(double basePrice) {
        return basePrice + 20.0; 
    }

    @Override
    public String getName() {
        return "Weekend";
    }
}