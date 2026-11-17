package datos;

import java.util.ArrayList;
import java.util.List;

import modelo.Estudiante;

public class DatosPrueba {

    public static List<Estudiante> crearLista() {
        List<Estudiante> lista = new ArrayList<>();
        lista.add(new Estudiante("E014", "Ana", 20, 8.5, "Computacion"));
        lista.add(new Estudiante("E003", "Luis", 19, 7.8, "Electronica"));
        lista.add(new Estudiante("E018", "Maria", 21, 9.2, "Computacion"));
        lista.add(new Estudiante("E007", "Pedro", 20, 8.5, "Mecatronica"));
        lista.add(new Estudiante("E001", "Sofia", 18, 9.5, "Computacion"));
        lista.add(new Estudiante("E012", "Diego", 22, 6.8, "Electronica"));
        lista.add(new Estudiante("E005", "Ana", 19, 8.5, "Computacion"));
        lista.add(new Estudiante("E020", "Carlos", 21, 7.8, "Mecatronica"));
        lista.add(new Estudiante("E009", "Lucia", 20, 9.2, "Electronica"));
        lista.add(new Estudiante("E016", "Mateo", 18, 7.0, "Computacion"));
        lista.add(new Estudiante("E002", "Elena", 22, 8.0, "Mecatronica"));
        lista.add(new Estudiante("E011", "Pablo", 21, 9.5, "Electronica"));
        lista.add(new Estudiante("E006", "Sara", 19, 6.8, "Computacion"));
        lista.add(new Estudiante("E019", "Juan", 20, 8.0, "Mecatronica"));
        lista.add(new Estudiante("E004", "Diana", 18, 9.2, "Electronica"));
        lista.add(new Estudiante("E015", "Bruno", 22, 7.0, "Computacion"));
        lista.add(new Estudiante("E008", "Carla", 21, 8.5, "Mecatronica"));
        lista.add(new Estudiante("E017", "Andres", 19, 9.5, "Electronica"));
        lista.add(new Estudiante("E010", "Ana", 20, 8.5, "Computacion"));
        lista.add(new Estudiante("E013", "Luis", 19, 7.8, "Electronica"));
        return lista;
    }
}
