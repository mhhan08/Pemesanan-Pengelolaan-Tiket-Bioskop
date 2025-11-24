package models;

import java.util.ArrayList;
import java.util.List;

public abstract class Studio {
    protected String name;
    protected int capacity;
    protected double basePrice;
    protected List<Schedule> schedules = new ArrayList<>();
    protected boolean[][] seats; // true = booked,false = available

    public void addSchedule(Schedule s) {
        schedules.add(s);
    }

    public Schedule getSchedule(String time) {
        for (Schedule s : schedules) {
            if (s.getTime().equals(time)) return s;
        }
        return null;
    }

    //untuk menerima angka yg di klik dari gui
    public boolean bookSeatByIndex(String time, int row, int col) {
        // validasi index kursi
        if (row >= 0 && row < seats.length && col >= 0 && col < seats[0].length) {
            // cek ketersediaan kursi
            if (!seats[row][col]) {
                seats[row][col] = true;

                return true;
            }
        }
        return false;
    }

    // cek status kursi
    public boolean isSeatBooked(int row, int col) {
        if (row >= 0 && row < seats.length && col >= 0 && col < seats[0].length) {
            return seats[row][col];
        }
        return false;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }
}