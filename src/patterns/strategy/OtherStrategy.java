package patterns.strategy;

public class OtherStrategy implements PriceStrategy {
    private double adjustment;
    private String eventName;

    public OtherStrategy(String eventName, double adjustment) {
        this.eventName = eventName;
        this.adjustment = adjustment;
    }

    @Override
    public double calculate(double basePrice) {
        return basePrice + adjustment;
    }

    @Override
    public String getName() {
        return eventName;
    }
}