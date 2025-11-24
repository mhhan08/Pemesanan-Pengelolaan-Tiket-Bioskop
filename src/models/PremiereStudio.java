package models;

public class PremiereStudio extends Studio {
    public PremiereStudio(String name) {
        this.name = name;
        this.basePrice = 60000.0;
        this.seats = new boolean[5][8];//kursi premiere 5 baris 8 kolom
        this.capacity = 40;
    }
}
