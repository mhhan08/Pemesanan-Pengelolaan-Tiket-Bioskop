package dao;

import config.DBConnection;
import models.PremiereStudio;
import models.Studio;
import patterns.factory.StudioFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudioDAO implements GenericDAO<Studio> {
    // logger untuk menggantikan system.out.println
    private static final Logger LOGGER = Logger.getLogger(StudioDAO.class.getName());

    @Override
    public void save(Studio studio) {
        String sql = "INSERT INTO studios (name, type, capacity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // pastikan auto commit nyala
            conn.setAutoCommit(true);

            stmt.setString(1, studio.getName());

            // tipe studio yg dipilih
            String type = "Regular";
            if (studio instanceof PremiereStudio) {
                type = "Premiere";
            }
            stmt.setString(2, type);

            // hitung kapasitas
            int capacity = studio.getRowCount() * studio.getColCount();
            stmt.setInt(3, capacity);

            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error saving studio", e);
        }
    }

    @Override
    public List<Studio> getAll() {
        List<Studio> studios = new ArrayList<>();
        // ganti select * dengan nama kolom spesifik
        String sql = "SELECT name, type FROM studios";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");

                // panggil method terpisah untuk buat objek studio
                Studio s = createStudioSafe(type, name);
                if (s != null) {
                    studios.add(s);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error loading studios", e);
        }
        return studios;
    }

    // method terpisah untuk menangani try-catch factory (memecah nested try block)
    private Studio createStudioSafe(String type, String name) {
        try {
            return StudioFactory.createStudio(type, name);
        } catch (Exception _) { // unnamed pattern pengganti e
            LOGGER.log(Level.WARNING, "skip studio invalid: {0}", name);
            return null;
        }
    }

    @Override
    public void delete(String name) {
        String sql = "DELETE FROM studios WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(true);
            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error deleting studio", e);
        }
    }
}