package models;

import java.util.Date;

public class Ticket {
    private String bookingId;
    private Schedule schedule;
    private String seatCode;
    private double finalPrice;
    private Date bookingDate;

    public Ticket(String bookingId, Schedule schedule, String seatCode, double finalPrice) {
        this.bookingId = bookingId;
        this.schedule = schedule;
        this.seatCode = seatCode;
        this.finalPrice = finalPrice;
        this.bookingDate = new Date();
    }

    public String getTicketInfo() {
        return "=== TICKET (" + bookingId + ") ===\n" +
                "Movie: " + schedule.getMovie().getTitle() + "\n" +
                "Studio: " + "Cinema Studio" + "\n" +
                "Time: " + schedule.getDay() + " " + schedule.getTime() + "\n" +
                "Seat: " + seatCode + "\n" +
                "Price: Rp " + finalPrice + "\n" +
                "Date: " + bookingDate.toString();
    }

    public double getFinalPrice() {
        return finalPrice;
    }
    public String getBookingId() {
        return bookingId;
    }
    public String getSeatCode() {
        return seatCode;
    }
}