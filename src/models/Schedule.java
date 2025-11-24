package models;

public class Schedule {
    private Movie movie;
    private String day;
    private String time;

    public Schedule(Movie movie, String day, String time) {
        this.movie = movie;
        this.day = day;
        this.time = time;
    }


    public String getInfo() {
        return movie.getTitle() + " | " + day + " " + time + " | " ;
    }

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