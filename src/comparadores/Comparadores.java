package comparadores;

import java.util.Comparator;

import modelo.Estudiante;

/**
 * Criterios externos de ordenamiento (C2 a C7).
 * C1 no esta aqui porque usa el orden natural (Comparable).
 */
public class Comparadores {

    /** C2: nombre ascendente y, si empatan, codigo ascendente. Lambda de dos parametros. */
    public static final Comparator<Estudiante> C2_NOMBRE_CODIGO = (a, b) -> {
        int resultado = a.getNombre().compareTo(b.getNombre());
        if (resultado != 0) {
            return resultado;
        }
        return a.getCodigo().compareTo(b.getCodigo());
    };

    /** C2 (version 2): mismo criterio con comparing(), thenComparing() y referencias a metodos. */
    public static final Comparator<Estudiante> C2_NOMBRE_CODIGO_REF =
            Comparator.comparing(Estudiante::getNombre)
                      .thenComparing(Estudiante::getCodigo);

    /** C3: nota ascendente, sin desempate. */
    public static final Comparator<Estudiante> C3_NOTA_ASC =
            Comparator.comparingDouble(Estudiante::getNota);

    /** C4: nota descendente y nombre ascendente. reversed() se aplica solo a la nota. */
    public static final Comparator<Estudiante> C4_NOTA_DESC_NOMBRE =
            Comparator.comparingDouble(Estudiante::getNota).reversed()
                      .thenComparing(Estudiante::getNombre);

    /** C5: edad ascendente y nombre descendente (cada criterio con su propio sentido). */
    public static final Comparator<Estudiante> C5_EDAD_NOMBRE_DESC =
            Comparator.comparingInt(Estudiante::getEdad)
                      .thenComparing(Comparator.comparing(Estudiante::getNombre).reversed());

    /** C6: carrera asc., nota desc., nombre asc., codigo asc. */
    public static final Comparator<Estudiante> C6_CARRERA_NOTA_NOMBRE_CODIGO =
            Comparator.comparing(Estudiante::getCarrera)
                      .thenComparing(Comparator.comparingDouble(Estudiante::getNota).reversed())
                      .thenComparing(Estudiante::getNombre)
                      .thenComparing(Estudiante::getCodigo);

    /** C7: nota ajustada desc., nombre asc., codigo asc. */
    public static final Comparator<Estudiante> C7_NOTA_AJUSTADA =
            Comparator.comparingDouble(Comparadores::notaAjustada).reversed()
                      .thenComparing(Estudiante::getNombre)
                      .thenComparing(Estudiante::getCodigo);

    /**
     * Ejemplo INCORRECTO de C5 (solo para el analisis): reversed() al final
     * invierte todo el comparador compuesto, tambien la edad.
     */
    public static final Comparator<Estudiante> C5_INCORRECTO =
            Comparator.comparingInt(Estudiante::getEdad)
                      .thenComparing(Estudiante::getNombre)
                      .reversed();

    /**
     * Nota ajustada para C7: +0.5 (maximo 10.0) solo para Computacion.
     * No modifica el objeto, solo calcula un valor.
     */
    public static double notaAjustada(Estudiante e) {
        if (e.getCarrera().equals("Computacion")) {
            return Math.min(10.0, e.getNota() + 0.5);
        }
        return e.getNota();
    }
}
