package models;

// Import Strategy agar bisa menghitung harga
import patterns.strategy.PriceStrategy;

public class Schedule {
    private Movie movie;
    private Studio studio;
    private String day;
    private String time;
    private PriceStrategy priceStrategy;

    private boolean[][] seats;

    // constructor untuk dipanggil oleh builder
    public Schedule(Movie movie, Studio studio, String day, String time, PriceStrategy strategy) {
        this.movie = movie;
        this.studio = studio;
        this.day = day;
        this.time = time;
        this.priceStrategy = strategy;

        //inisialisasi kursi per jadwal
        int rows = studio.getRowCount();
        int cols = studio.getColCount();
        this.seats = new boolean[rows][cols];

        }

    // hitung harga final
    public double calculateFinalPrice(double basePrice) {
        return priceStrategy.calculate(basePrice);
    }

    public String getInfo() {
        return movie.getTitle() + " | " + studio.getName() + " | " + day + " " + time + " (" + priceStrategy.getName() + ")";
    }

    //kursi per jadwal
    
    public boolean bookSeat(int row, int col){
        if(row >= 0 && row < seats.length && col >= 0 && col < seats[0].length && !seats[row][col]){
            seats[row][col] = true;
            return true;
        }
        return false;
    }

    public boolean isSeatBooked(int row, int col){
        if (row >= 0 && row < seats.length && col >= 0 && col < seats[0].length) {
        return seats[row][col];
        }
        return false;
    }

    public boolean[][] getSeatsMatrix(){
        return seats;
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

    public void setCapacity(int capacity) {
        studio.setCapacity(capacity);
    }

    public int getBookedCount() {
        int count = 0;
        for (int r = 0; r < seats.length; r++) {
            for (int c = 0; c < seats[0].length; c++) {
                if (seats[r][c]) count++;
            }
        }
        return count;
    }

     public int getRowCount() {
        return seats.length;
    }

    public int getColCount() {
        return seats[0].length;
    }
}