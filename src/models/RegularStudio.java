package models;

public class RegularStudio extends Studio {
    public RegularStudio(String name) {
        this.name = name;
        this.basePrice = 30000.0;
        this.seats = new boolean[10][15];//kursi regular 10 baris 15 kolom
        this.capacity = 150;
    }
}
