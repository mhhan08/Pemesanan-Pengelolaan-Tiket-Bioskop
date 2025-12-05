package controller;

import dao.BookingDAO;
import dao.MovieDAO;
import dao.ScheduleDAO;
import dao.StudioDAO;
import java.util.ArrayList;
import java.util.List;
import models.*;
import patterns.builder.ScheduleBuilder;
import patterns.factory.StudioFactory;

public class CinemaSystemFacade {
    private MovieDAO movieDAO = new MovieDAO();
    private StudioDAO studioDAO = new StudioDAO();
    private ScheduleDAO scheduleDAO = new ScheduleDAO();
    private BookingDAO bookingDAO = new BookingDAO(); //untuk ticket

    private List<Studio> loadedStudios;
    //ambil data dari db
    public CinemaSystemFacade() {
        refreshData();
    }

    public void refreshData() {
        this.loadedStudios = studioDAO.getAll();
        for (Studio s : loadedStudios) {
            scheduleDAO.loadSchedulesForStudio(s, movieDAO);
            bookingDAO.loadBookedSeats(s, scheduleDAO);
        }
    }

    //kelola movie
    public void addMovie(String title, String genre, int duration) {
        Movie movie = new Movie(title, genre, duration);
        movieDAO.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieDAO.getAll();
    }

    public void deleteMovie(int index) {
        List<Movie> all = getAllMovies();
        if (index >= 0 && index < all.size()) {
            Movie target = all.get(index);
            movieDAO.delete(target.getTitle());
        }
    }

    public Movie findMovie(String title) {
        return movieDAO.findByTitle(title);
    }

    public void addStudio(String type, String name) {
        try {
            Studio s = StudioFactory.createStudio(type, name);
            studioDAO.save(s);
            refreshData();
        } catch (Exception e) {
            System.out.println("Gagal membuat studio: " + e.getMessage());
        }
    }

    public List<Studio> getAllStudios() {
        return loadedStudios;
    }

    public void deleteStudio(int index) {
        if (index >= 0 && index < loadedStudios.size()) {
            Studio target = loadedStudios.get(index);
            studioDAO.delete(target.getName());
            refreshData();
        }
    }

    public Studio findStudio(String name) {
        for (Studio s : loadedStudios) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    //kelola jadwal
    public void createSchedule(String studioName, String movieTitle, String day, String time) {
        Studio studio = findStudio(studioName);
        Movie movie = findMovie(movieTitle);

        if (studio != null && movie != null) {
            //buat object schedule dengan builder
            Schedule schedule = new ScheduleBuilder()
                    .setMovie(movie)
                    .setStudio(studio)
                    .setDay(day)
                    .setTime(time)
                    .build();
            scheduleDAO.save(schedule);// simpan ke db
            //Masukkan ke Memori Studio agar langsung tampil
            studio.addSchedule(schedule);
        }
    }

    // helper untuk gui Admin menampilkan tabel jadwal
    public List<String[]> getAllSchedulesInfo() {
        List<String[]> rows = new ArrayList<>();
        for (Studio s : loadedStudios) {
            for (Schedule sch : s.getSchedules()) {
                double price = sch.calculateFinalPrice(s.getBasePrice());
                rows.add(new String[]{
                        s.getName(),
                        sch.getMovie().getTitle(),
                        sch.getDay() + " - " + sch.getTime(),
                        "Rp " + (int)price
                });
            }
        }
        return rows;
    }

    // Method  untuk menghapus jadwal
    public void deleteSchedule(String studioName, String day, String time) {
        // cari ID jadwal di database
        int id = scheduleDAO.getScheduleId(studioName, day, time);

        if (id != -1) {
            // hapus
            scheduleDAO.delete(id);
            // refresh memori agar tabel update
            refreshData();
        }
    }

    //booking ticket
    public Ticket bookTicket(String studioName, String time, int row, int col) {
        Studio studio = findStudio(studioName);
        if (studio != null) {
            // true jika kursi masih kosong
            // boolean success = studio.bookSeatByIndex(row, col);

            // if (success) {
            //     Schedule schedule = studio.getSchedule(time);

            //     if (schedule != null) {
            //         //hitung harga
            //         double finalPrice = schedule.calculateFinalPrice(studio.getBasePrice());

            //         // buat id transaksi
            //         String bookingId = "TRX-" + System.currentTimeMillis();

            //         // keonversi index array jadi kode kursi
            //         char rowChar = (char) ('A' + row);
            //         String seatCode = "" + rowChar + (col + 1);

            //         // buat object tiket
            //         Ticket ticket = new Ticket(bookingId, schedule, seatCode, finalPrice);

            //         // simpan ke db
            //         int dbScheduleId = scheduleDAO.getScheduleId(studioName, schedule.getDay(), time);

            //         if (dbScheduleId != -1) {
            //             bookingDAO.save(ticket, dbScheduleId);
            //         } else {
            //             System.out.println("Error: ID Jadwal tidak ditemukan di DB");
            //         }

            //         return ticket; //return ticket ke gui
            //     }
            // }

            Schedule schedule = studio.getSchedule(time);
            if (schedule != null) {
                boolean success = schedule.bookSeat(row, col);

                if (success) {
                    double finalPrice = schedule.calculateFinalPrice(studio.getBasePrice());

                    String bookingId = "TRX-" + System.currentTimeMillis();
                    char rowChar = (char) ('A' + row);
                    String seatCode = "" + rowChar + (col + 1);

                    Ticket ticket = new Ticket(bookingId, schedule, seatCode, finalPrice);

                    int dbScheduleId = scheduleDAO.getScheduleId(studioName, schedule.getDay(), time);
                    if (dbScheduleId != -1) {
                        bookingDAO.save(ticket, dbScheduleId);
                    }

                    return ticket;
                }
            }
            return null;


        }
        return null; // gagal
    }

    //helper gui
    public List<String> getStudioNames() {
        List<String> names = new ArrayList<>();
        for (Studio s : loadedStudios) names.add(s.getName());
        return names;
    }

    public List<String> getMovieTitles() {
        List<String> titles = new ArrayList<>();
        for (Movie m : movieDAO.getAll()) titles.add(m.getTitle());
        return titles;
    }
}