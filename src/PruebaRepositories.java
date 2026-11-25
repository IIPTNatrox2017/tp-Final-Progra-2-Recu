
import Controllers.OcupacionesController;
import Models.Ocupacion;

import java.time.LocalDate;
import java.util.Set;

public class PruebaRepositories {

    public static void main(String[] args) {

        OcupacionesController controller = new OcupacionesController();

        LocalDate fecha = LocalDate.of(2027, 2, 12);

        Set<Ocupacion> ocupaciones = controller.obtenerOcupacionesDelDia(fecha);

        System.out.println("=== OCUPACIONES DEL DÍA " + fecha + " ===");

        for (Ocupacion ocupacion : ocupaciones) {
            System.out.println("Ocupación ID: " + ocupacion.getIdOcupacion());
            System.out.println("Check-in: " + ocupacion.getCheckIn());
            System.out.println("Check-out: " + ocupacion.getCheckOut());
            System.out.println("--------------------");
        }
    }
}
