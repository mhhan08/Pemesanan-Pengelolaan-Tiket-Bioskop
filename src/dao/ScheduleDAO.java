package dao;

import config.DBConnection;
import models.Schedule;
import models.Studio;
import models.Movie;
import patterns.builder.ScheduleBuilder;

import java.sql.*;

public class ScheduleDAO {

    // Simpan jadwal baru ke database
    public void save(Schedule schedule) {
        String sql = "INSERT INTO schedules (studio_name, movie_title, day, time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, schedule.getStudio().getName());
            stmt.setString(2, schedule.getMovie().getTitle());
            stmt.setString(3, schedule.getDay());
            stmt.setString(4, schedule.getTime());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error saving schedule: " + e.getMessage());
        }
    }

    // Ambil ID dari DB (diperlukan untuk booking)
    public int getScheduleId(String studioName, String day, String time) {
        String sql = "SELECT id FROM schedules WHERE studio_name=? AND day=? AND time=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studioName);
            stmt.setString(2, day);
            stmt.setString(3, time);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Load semua jadwal untuk satu studio agar masuk ke RAM
    public void loadSchedulesForStudio(Studio studio, MovieDAO movieDAO) {
        String sql = "SELECT * FROM schedules WHERE studio_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studio.getName());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String movieTitle = rs.getString("movie_title");
                String day = rs.getString("day");
                String time = rs.getString("time");

                // Cari objek Movie berdasarkan judul
                Movie movie = movieDAO.findByTitle(movieTitle);

                if (movie != null) {
                    // Re-create object pakai Builder
                    Schedule s = new ScheduleBuilder()
                            .setStudio(studio)
                            .setMovie(movie)
                            .setDay(day)
                            .setTime(time)
                            .build();

                    studio.addSchedule(s);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading schedules: " + e.getMessage());
        }
    }
}