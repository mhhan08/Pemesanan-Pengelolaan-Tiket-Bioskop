package dao;

import config.DBConnection;
import models.Studio;
import models.Ticket;
import java.sql.*;

public class BookingDAO {

    public void save(Ticket ticket, int scheduleId) {
        String sql = "INSERT INTO bookings (booking_trx_id, schedule_id, seat_code, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ticket.getBookingId());
            stmt.setInt(2, scheduleId);
            stmt.setString(3, ticket.getSeatCode());
            stmt.setDouble(4, ticket.getFinalPrice());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error saving booking: " + e.getMessage());
        }
    }

    // Load kursi yang sudah laku -> Tandai di Array Studio
    public void loadBookedSeats(Studio studio, ScheduleDAO scheduleDAO) {
        for (models.Schedule sch : studio.getSchedules()) {
            int scheduleId = scheduleDAO.getScheduleId(studio.getName(), sch.getDay(), sch.getTime());

            if (scheduleId != -1) {
                String sql = "SELECT seat_code FROM bookings WHERE schedule_id = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setInt(1, scheduleId);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        String seatCode = rs.getString("seat_code");
                        // Method ini mengubah boolean[][] jadi true (merah)
                        studio.bookSeat(sch.getTime(), seatCode);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}