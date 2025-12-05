package models;

import java.util.ArrayList;
import java.util.List;

public abstract class Studio {
    protected String name;
    protected int capacity;
    protected double basePrice;
    protected List<Schedule> schedules = new ArrayList<>();
    protected boolean[][] seats; // true = booked,false = available

    // Tambahkan getter dan setter capacity supaya Schedule bisa panggil
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        // Jika seats lama dipakai, update ukuran array seats juga
        if(seats != null) {
            int rows = seats.length;
            int cols = seats[0].length;
            if (rows * cols != capacity) {
                seats = new boolean[rows][cols]; // reset seats lama
            }
        }
    }

    public void addSchedule(Schedule s) {
        schedules.add(s);
    }

    public Schedule getSchedule(String time) {
        for (Schedule s : schedules) {
            if (s.getTime().equals(time)) return s;
        }
        return null;
    }

    // getter list jadwal
    public List<Schedule> getSchedules() {
        return schedules;
    }

    //untuk menerima angka yg di klik dari gui
    public boolean bookSeatByIndex(int row, int col) {
        // validasi index kursi
        // cek ketersediaan kursi
        if (row >= 0 && row < seats.length && col >= 0 && col < seats[0].length && !seats[row][col]) {
            seats[row][col] = true;

            return true;
        }
        return false;
    }

    // cek status kursi
    public boolean isSeatBooked(int row, int col) {
        return row >= 0 && row < seats.length && col >= 0 && col < seats[0].length && seats[row][col];
    }

    public boolean bookSeat(String seatCode) {
        try {
            // konversi tempat duduk
            char rowChar = seatCode.toUpperCase().charAt(0);
            int row = rowChar - 'A';

            String colStr = seatCode.substring(1);
            int col = Integer.parseInt(colStr) - 1;

            // panggil method booking dengan index kursi yg sudah diubah
            return bookSeatByIndex(row, col);

        } catch (Exception _) {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    // getter ukuran array untuk loop tombol di gui
    public int getRowCount() {
        return seats.length;
    }
    public int getColCount() {
        return seats[0].length;
    }

     // Hitung jumlah kursi yang sudah dibooking (untuk testing lama)
    public int getBookedCount() {
        int count = 0;
        if(seats != null){
            for (int r = 0; r < seats.length; r++) {
                for (int c = 0; c < seats[0].length; c++) {
                    if (seats[r][c]) count++;
                }
            }
        }
        return count;
    }
}