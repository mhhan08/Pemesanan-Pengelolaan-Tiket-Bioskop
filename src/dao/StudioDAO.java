package dao;

import config.DBConnection;
import models.Studio;
import patterns.factory.StudioFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudioDAO implements GenericDAO<Studio> {

    @Override
    public void save(Studio studio) {
        String sql = "INSERT INTO studios (name, type, capacity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studio.getName());

            // tipe studio yg dipilih
            String type = "Regular"; // default
            if (studio instanceof models.PremiereStudio) {
                type = "Premiere";
            }
            stmt.setString(2, type);

            // hitung kapasistas
            int capacity = studio.getRowCount() * studio.getColCount();
            stmt.setInt(3, capacity);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error saving studio: " + e.getMessage());
        }
    }

    @Override
    public List<Studio> getAll() {
        List<Studio> studios = new ArrayList<>();
        String sql = "SELECT * FROM studios";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");

                //buat objek dari string db dengan factory
                try {
                    Studio s = StudioFactory.createStudio(type, name);
                    studios.add(s);
                } catch (Exception e) {
                    System.out.println("Skip studio invalid: " + name);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading studios: " + e.getMessage());
        }
        return studios;
    }

    @Override
    public void delete(String name) {
        String sql = "DELETE FROM studios WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting studio: " + e.getMessage());
        }
    }

    // cari studio
    public Studio findByName(String name) {
        String sql = "SELECT * FROM studios WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String type = rs.getString("type");
                String dbName = rs.getString("name");
                return StudioFactory.createStudio(type, dbName);
            }
        } catch (SQLException e) {
            System.out.println("Error finding studio: " + e.getMessage());
        }
        return null;
    }
}