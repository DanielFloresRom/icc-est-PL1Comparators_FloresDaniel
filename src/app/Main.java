package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comparadores.Comparadores;
import datos.DatosPrueba;
import modelo.Estudiante;

public class Main {

    public static void main(String[] args) {
        List<Estudiante> original = DatosPrueba.crearLista();

        imprimir("LISTA ORIGINAL", original);

        // ---------- Actividad 1: orden natural ----------
        System.out.println("=== Signo de compareTo() ===");
        Estudiante e014 = original.get(0);
        Estudiante e003 = original.get(1);
        System.out.println("E014.compareTo(E003) = " + e014.compareTo(e003) + "  (positivo: E014 va despues)");
        System.out.println("E003.compareTo(E014) = " + e003.compareTo(e014) + "  (negativo: E003 va antes)");
        System.out.println("E014.compareTo(E014) = " + e014.compareTo(e014) + "  (cero: mismo codigo)");
        System.out.println();

        // C1: orden natural (Comparable)
        List<Estudiante> c1 = new ArrayList<>(original);
        Collections.sort(c1);
        imprimir("C1: codigo asc. (Comparable)", c1);

        // ---------- Actividad 2: consultas con Comparator ----------
        List<Estudiante> c2 = new ArrayList<>(original);
        c2.sort(Comparadores.C2_NOMBRE_CODIGO);
        imprimir("C2: nombre asc., codigo asc. (lambda)", c2);

        List<Estudiante> c3 = new ArrayList<>(original);
        c3.sort(Comparadores.C3_NOTA_ASC);
        imprimir("C3: nota asc. sin desempate (comparingDouble)", c3);

        List<Estudiante> c4 = new ArrayList<>(original);
        c4.sort(Comparadores.C4_NOTA_DESC_NOMBRE);
        imprimir("C4: nota desc., nombre asc.", c4);

        List<Estudiante> c5 = new ArrayList<>(original);
        c5.sort(Comparadores.C5_EDAD_NOMBRE_DESC);
        imprimir("C5: edad asc., nombre desc.", c5);

        List<Estudiante> c6 = new ArrayList<>(original);
        c6.sort(Comparadores.C6_CARRERA_NOTA_NOMBRE_CODIGO);
        imprimir("C6: carrera asc., nota desc., nombre asc., codigo asc.", c6);

        List<Estudiante> c7 = new ArrayList<>(original);
        c7.sort(Comparadores.C7_NOTA_AJUSTADA);
        System.out.println("=== C7: nota ajustada desc., nombre asc., codigo asc. ===");
        for (Estudiante e : c7) {
            System.out.println(e.getCodigo() + " | " + e.getNombre() + " | " + e.getCarrera()
                    + " | original: " + e.getNota()
                    + " | ajustada: " + Comparadores.notaAjustada(e));
        }
        System.out.println();

        // ---------- Actividad 3: comparacion de implementaciones ----------
        List<Estudiante> c2ref = new ArrayList<>(original);
        c2ref.sort(Comparadores.C2_NOMBRE_CODIGO_REF);
        imprimir("C2 (version comparing + thenComparing + referencias)", c2ref);
        System.out.println("C2 lambda y C2 referencias dan la misma secuencia: " + mismaSecuencia(c2, c2ref));
        System.out.println();

        List<Estudiante> c5mal = new ArrayList<>(original);
        c5mal.sort(Comparadores.C5_INCORRECTO);
        imprimir("C5 INCORRECTO: (edad, nombre).reversed() -> invierte tambien la edad", c5mal);

        System.out.println("=== Nota igual con codigos distintos ===");
        Estudiante e007 = original.get(3);
        System.out.println("C3.compare(E014, E007) = " + Comparadores.C3_NOTA_ASC.compare(e014, e007)
                + "  (ambos 8.5, el comparador los considera empatados)");
        System.out.println("E014.compareTo(E007)   = " + e014.compareTo(e007)
                + "  (por codigo si son distintos)");
        System.out.println();

        imprimir("LISTA ORIGINAL DESPUES DE TODAS LAS CONSULTAS", original);
    }

    private static void imprimir(String titulo, List<Estudiante> lista) {
        System.out.println("=== " + titulo + " ===");
        for (Estudiante e : lista) {
            System.out.println(e);
        }
        System.out.println();
    }

    private static boolean mismaSecuencia(List<Estudiante> a, List<Estudiante> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).getCodigo().equals(b.get(i).getCodigo())) {
                return false;
            }
        }
        return true;
    }
}
