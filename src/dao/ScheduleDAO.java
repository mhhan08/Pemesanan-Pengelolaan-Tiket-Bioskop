package dao;

import config.DBConnection;
import models.Movie;
import models.Schedule;
import models.Studio;
import patterns.builder.ScheduleBuilder;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleDAO {

    private static final Logger LOGGER = Logger.getLogger(ScheduleDAO.class.getName());

    // simpan jadwal baru ke database
    public void save(Schedule schedule) {
        String sql = "INSERT INTO schedules (studio_name, movie_title, day, time) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            stmt.setString(1, schedule.getStudio().getName());
            stmt.setString(2, schedule.getMovie().getTitle());
            stmt.setString(3, schedule.getDay());
            stmt.setString(4, schedule.getTime());
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error saving schedule", e);
        }
    }

    // ambil id dari db
    public int getScheduleId(String studioName, String day, String time) {
        String sql = "SELECT id FROM schedules WHERE studio_name=? AND day=? AND time=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studioName);
            stmt.setString(2, day);
            stmt.setString(3, time);

            // resultset dalam try agar tertutup otomatis
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error getting schedule id", e);
        }
        return -1;
    }

    // load semua jadwal untuk satu studio agar masuk ke ram
    public void loadSchedulesForStudio(Studio studio, MovieDAO movieDAO) {
        // pilih kolom spesifik biar lebih efisien daripada select *
        String sql = "SELECT movie_title, day, time FROM schedules WHERE studio_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studio.getName());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String movieTitle = rs.getString("movie_title");
                    String day = rs.getString("day");
                    String time = rs.getString("time");

                    // cari objek movie berdasarkan judul
                    Movie movie = movieDAO.findByTitle(movieTitle);

                    if (movie != null) {
                        // buat ulang object pakai builder
                        Schedule s = new ScheduleBuilder()
                                .setStudio(studio)
                                .setMovie(movie)
                                .setDay(day)
                                .setTime(time)
                                .build();

                        studio.addSchedule(s);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error loading schedules", e);
        }
    }

    public void delete(int scheduleId) {
        String sql = "DELETE FROM schedules WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(true);
            stmt.setInt(1, scheduleId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error deleting schedule", e);
        }
    }
}