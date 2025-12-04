package patterns.builder;

import models.Movie;
import models.Schedule;
import models.Studio;
import patterns.strategy.PriceStrategy;
import patterns.strategy.WeekdayStrategy;
import patterns.strategy.WeekendStrategy;

public class ScheduleBuilder {
    private Movie movie;
    private Studio studio;
    private String day;
    private String time;
    private PriceStrategy strategy;

    public ScheduleBuilder() {
        // default strategy
        this.strategy = new WeekdayStrategy();
    }

    public ScheduleBuilder setMovie(Movie movie) {
        this.movie = movie;
        return this; //return this untuk method chaining
    }

    public ScheduleBuilder setStudio(Studio studio) {
        this.studio = studio;
        return this;
    }

    public ScheduleBuilder setDay(String day) {
        this.day = day;

        // memilih strategy berdasarkan nama hari
        if (day.equalsIgnoreCase("Saturday") || day.equalsIgnoreCase("Sunday") || day.equalsIgnoreCase("Minggu") || day.equalsIgnoreCase("Sabtu")) {
            this.strategy = new WeekendStrategy();
        } else {
            this.strategy = new WeekdayStrategy();
        }
        return this;
    }

    public ScheduleBuilder setTime(String time) {
        if (!time.matches("([01]?\\d|2[0-3]):[0-5]\\d")) {
            throw new IllegalArgumentException("jam tidak valid! gunakan format HH:MM");
        }

        this.time = time;
        return this;
    }

    // manual untuk otherstrategy
    public ScheduleBuilder setStrategy(PriceStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    // return objek schedule
    public Schedule build() {
        return new Schedule(movie, studio, day, time, strategy);
    }
}