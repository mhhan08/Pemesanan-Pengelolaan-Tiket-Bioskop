package dao;

import config.DBConnection;
import models.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieDAO implements GenericDAO<Movie> {

    // logger pengganti system.out
    private static final Logger LOGGER = Logger.getLogger(MovieDAO.class.getName());

    @Override
    public void save(Movie movie) {
        String sql = "INSERT INTO movies (title, genre, duration) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // agar data masuk db
            conn.setAutoCommit(true);

            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setInt(3, movie.getDurationMinutes());
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error saving movie", e);
        }
    }

    @Override
    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();

        String sql = "SELECT title, genre, duration FROM movies";

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
            LOGGER.log(Level.SEVERE, "error loading movies", e);
        }
        return movies;
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM movies WHERE title = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(true);

            stmt.setString(1, title);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error deleting movie", e);
        }
    }

    public Movie findByTitle(String title) {
        String sql = "SELECT title, genre, duration FROM movies WHERE title = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);

            // resultset dalam try agar tertutup otomatis
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Movie(
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getInt("duration")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error finding movie", e);
        }
        return null;
    }
}