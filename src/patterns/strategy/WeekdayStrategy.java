public class WeekdayStrategy implements PriceStrategy {
    @Override
    public double calculate(double basePrice) {
        return basePrice; // Harga normal
    }

    @Override
    public String getName() {
        return "Weekday";
    }
}