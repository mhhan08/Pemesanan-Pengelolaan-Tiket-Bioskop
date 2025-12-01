package patterns.factory;

import models.Studio;
import models.RegularStudio;
import models.PremiereStudio;

public class StudioFactory {

    //constructor kosong karena tidak boleh diinisialisasi karena isinya hanya method static
    private StudioFactory() {
        throw new IllegalStateException("utility class");
    }

    // method static agar bisa dipanggil tanpa new StudioFactory()
    public static Studio createStudio(String type, String name) {
        if (type == null) {
            return null;
        }
        if (type.equalsIgnoreCase("Regular")) {
            return new RegularStudio(name);
        } else if (type.equalsIgnoreCase("Premiere")) {
            return new PremiereStudio(name);
        }
        // jika input salah
        throw new IllegalArgumentException("Tipe Studio tidak dikenal: " + type);
    }
}