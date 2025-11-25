package models;

// Import Strategy agar bisa menghitung harga
import patterns.strategy.PriceStrategy;

public class Schedule {
    private Movie movie;
    private Studio studio;
    private String day;
    private String time;
    private PriceStrategy priceStrategy;

    // constructor untuk dipanggil oleh builder
    public Schedule(Movie movie, Studio studio, String day, String time, PriceStrategy strategy) {
        this.movie = movie;
        this.studio = studio;
        this.day = day;
        this.time = time;
        this.priceStrategy = strategy;
    }

    // hitung harga final
    public double calculateFinalPrice(double basePrice) {
        return priceStrategy.calculate(basePrice);
    }

    public String getInfo() {
        return movie.getTitle() + " | " + studio.getName() + " | " + day + " " + time + " (" + priceStrategy.getName() + ")";
    }


    public String getTime() {
        return time;
    }

    public String getDay() {
        return day;
    }

    public Movie getMovie() {
        return movie;
    }

    public Studio getStudio() {
        return studio;
    }
}