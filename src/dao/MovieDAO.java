package dao;

import config.DBConnection;
import models.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements GenericDAO<Movie> {

    @Override
    public void save(Movie movie) {
        String sql = "INSERT INTO movies (title, genre, duration) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setInt(3, movie.getDurationMinutes());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error saving movie: " + e.getMessage());
        }
    }

    @Override
    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Movie m = new Movie(
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("duration")
                );
                movies.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Error loading movies: " + e.getMessage());
        }
        return movies;
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM movies WHERE title = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting movie: " + e.getMessage());
        }
    }

    // --- INI METHOD YANG HILANG (PENYEBAB ERROR) ---
    public Movie findByTitle(String title) {
        String sql = "SELECT * FROM movies WHERE title = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Movie(
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("duration")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding movie: " + e.getMessage());
        }
        return null;
    }
}