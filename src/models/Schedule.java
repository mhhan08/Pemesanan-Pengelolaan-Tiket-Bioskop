package models;

import patterns.strategy.PriceStrategy; 

public class Schedule {
    private Movie movie;
    private String day;
    private String time;
    private PriceStrategy priceStrategy;

    public Schedule(Movie movie, String day, String time, PriceStrategy strategy) {
        this.movie = movie;
        this.day = day;
        this.time = time;
        this.priceStrategy = strategy;
    }

    //hitung harga akhir dari harga normal tapi ngirim harga ke strategi
    public double calculateFinalPrice(double basePrice) {
        return priceStrategy.calculate(basePrice);
    }

    public String getInfo() {
        return movie.getTitle() + " | " + day + " " + time + " | " + priceStrategy.getName();
    }

    //get buat tiket    
    public String getTime() {
        return time;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getDay() {
        return day;
    }
}