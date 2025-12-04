package test;

import models.*;
import patterns.builder.ScheduleBuilder;
import patterns.factory.StudioFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CinemaTest {

    // test logika booking kursi
    @Test
    void testBookingKursi() {
        // regular studio harga dasar 30000
        Studio studio = new RegularStudio("Studio Test");

        // booking kursi baris 0 kolom 0
        boolean bookingSukses = studio.bookSeatByIndex(0, 0);
        Assertions.assertTrue(bookingSukses, "booking kursi kosong berhasil");

        // booking lagi di kursi yang sama
        boolean bookingGagal = studio.bookSeatByIndex(0, 0);
        Assertions.assertFalse(bookingGagal, "booking kursi yg sudah ada isi gagal");

        // cek status kursi
        Assertions.assertTrue(studio.isSeatBooked(0, 0));
    }

    // test factory pattern pembuatan studio
    @Test
    void testStudioFactory() {
        // coba bikin premiere studio dengan factory
        Studio s = StudioFactory.createStudio("Premiere", "Studio P1");

        Assertions.assertNotNull(s);
        Assertions.assertTrue(s instanceof PremiereStudio);
        Assertions.assertEquals(60000.0, s.getBasePrice());
    }

    //test jumlah kursi dalam studio
    @Test
    void testDimensiStudio() {
        // regular studio harusnya 10 baris x 15 kolom
        Studio regular = StudioFactory.createStudio("Regular", "Studio 1");
        Assertions.assertEquals(10, regular.getRowCount());
        Assertions.assertEquals(15, regular.getColCount());

        // premiere studio harusnya 5 baris x 8 kolom
        Studio premiere = StudioFactory.createStudio("Premiere", "The Premiere");
        Assertions.assertEquals(5, premiere.getRowCount());
        Assertions.assertEquals(8, premiere.getColCount());
    }

    // test booking kursi tidak valid
    @Test
    void testBookingTidakValid() {
        Studio studio = new RegularStudio("Test Studio");

        // booking kursi minus baris -1
        boolean bookingMinus = studio.bookSeatByIndex(-1, 0);
        Assertions.assertFalse(bookingMinus, "Booking index negatif l");

        // booking kursi di luar kapasitas baris 100
        boolean bookingJauh = studio.bookSeatByIndex(100, 100);
        Assertions.assertFalse(bookingJauh, "Booking di luar kapasitas");
    }

    // test validasi input durasi film negatif
    @Test
    void testValidasiDurasiFilm() {
        // test masukkan durasi negatif
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            new Movie("Film Pendek", "Drama", -10));
    }

    //test pembuatan jadwal dengan jam tidak valid
    @Test
    void testJadwalJamTidakValid() {
        Movie movie = new Movie("Horror Night", "Horror", 90);
        Studio studio = new RegularStudio("Studio C");

        // masukkan jam 25:00
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            new ScheduleBuilder()
                    .setMovie(movie)
                    .setStudio(studio)
                    .setDay("Friday")
                    .setTime("25:00")
                    .build()
        );
    }

    // test integrasi tiket dengan jadwal
    @Test
    void testIntegrasiTiketJadwal() {
        Movie movie = new Movie("agak laen", "Comedy", 100);
        Studio studio = new RegularStudio("Studio D");

        Schedule schedule = new ScheduleBuilder()
                .setMovie(movie)
                .setStudio(studio)
                .setDay("Tuesday")
                .setTime("15:00")
                .build();

        // buat tiket
        String bookingId = "T-123";
        String seatCode = "A1";
        double price = schedule.calculateFinalPrice(studio.getBasePrice());

        Ticket ticket = new Ticket(bookingId, schedule, seatCode, price);

        // cek apakah tiket terhubung ke jadwal yang benar
        Assertions.assertEquals(schedule, ticket.getSchedule());
        // cek apakah film di tiket sesuai
        Assertions.assertEquals("agak laen", ticket.getSchedule().getMovie().getTitle());
    }
}