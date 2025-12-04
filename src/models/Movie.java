package models;

public class Movie {
    private String title;
    private String genre;
    private int duration;

    public Movie(String title, String genre, int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Durasi film harus lebih dari 0 menit!");
        }
        this.title = title;
        this.genre = genre;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurationMinutes() {
        return duration;
    }
}