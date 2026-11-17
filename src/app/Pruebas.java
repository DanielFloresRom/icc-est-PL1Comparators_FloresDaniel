package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import comparadores.Comparadores;
import datos.DatosPrueba;
import modelo.Estudiante;

/**
 * Pruebas manuales (sin librerias externas). Imprime una tabla Markdown:
 * Escenario | Esperado | Obtenido | Estado.
 */
public class Pruebas {

    private static int aprobadas = 0;
    private static int total = 0;

    public static void main(String[] args) {
        List<Estudiante> original = DatosPrueba.crearLista();
        String secuenciaInicial = codigos(original);

        System.out.println("| # | Escenario | Esperado | Obtenido | Estado |");
        System.out.println("|---|---|---|---|---|");

        // ---- C1 a C7: conservacion de registros y orden entre consecutivos ----
        List<Estudiante> c1 = new ArrayList<>(original);
        Collections.sort(c1);
        verificarConsulta("C1", c1, null, original);

        List<Estudiante> c2 = ordenar(original, Comparadores.C2_NOMBRE_CODIGO);
        verificarConsulta("C2", c2, Comparadores.C2_NOMBRE_CODIGO, original);
        List<Estudiante> c3 = ordenar(original, Comparadores.C3_NOTA_ASC);
        verificarConsulta("C3", c3, Comparadores.C3_NOTA_ASC, original);
        List<Estudiante> c4 = ordenar(original, Comparadores.C4_NOTA_DESC_NOMBRE);
        verificarConsulta("C4", c4, Comparadores.C4_NOTA_DESC_NOMBRE, original);
        List<Estudiante> c5 = ordenar(original, Comparadores.C5_EDAD_NOMBRE_DESC);
        verificarConsulta("C5", c5, Comparadores.C5_EDAD_NOMBRE_DESC, original);
        List<Estudiante> c6 = ordenar(original, Comparadores.C6_CARRERA_NOTA_NOMBRE_CODIGO);
        verificarConsulta("C6", c6, Comparadores.C6_CARRERA_NOTA_NOMBRE_CODIGO, original);
        List<Estudiante> c7 = ordenar(original, Comparadores.C7_NOTA_AJUSTADA);
        verificarConsulta("C7", c7, Comparadores.C7_NOTA_AJUSTADA, original);

        // ---- Casos de aceptacion ----
        registrar("C1: codigos E001 a E020 en orden",
                "E001, E002, ..., E020", codigos(c1), codigos(c1).equals(secuenciaE001aE020()));
        registrar("C2: primeros registros", "E005, E010, E014, E017",
                primeros(c2, 4), primeros(c2, 4).equals("E005, E010, E014, E017"));
        String notas85 = conNota(c3, 8.5);
        registrar("C3: estabilidad con nota 8.5 (orden relativo original)", "E014, E007, E005, E008, E010",
                notas85, notas85.equals("E014, E007, E005, E008, E010"));
        registrar("C4: primeros registros", "E017, E011, E001",
                primeros(c4, 3), primeros(c4, 3).equals("E017, E011, E001"));
        registrar("C5: primeros registros", "E001, E016, E004",
                primeros(c5, 3), primeros(c5, 3).equals("E001, E016, E004"));
        registrar("C6: primeros registros", "E001, E018, E005, E010, E014",
                primeros(c6, 5), primeros(c6, 5).equals("E001, E018, E005, E010, E014"));

        String c7obtenido = c7.get(0).getCodigo() + " " + Comparadores.notaAjustada(c7.get(0))
                + " (orig " + c7.get(0).getNota() + "), "
                + c7.get(1).getCodigo() + " " + Comparadores.notaAjustada(c7.get(1))
                + " (orig " + c7.get(1).getNota() + ")";
        String c7esperado = "E001 10.0 (orig 9.5), E018 9.7 (orig 9.2)";
        registrar("C7: nota ajustada y original", c7esperado, c7obtenido, c7obtenido.equals(c7esperado));

        // ---- Lista vacia y de un elemento con todos los criterios ----
        List<Comparator<Estudiante>> todos = new ArrayList<>();
        todos.add(Comparadores.C2_NOMBRE_CODIGO);
        todos.add(Comparadores.C3_NOTA_ASC);
        todos.add(Comparadores.C4_NOTA_DESC_NOMBRE);
        todos.add(Comparadores.C5_EDAD_NOMBRE_DESC);
        todos.add(Comparadores.C6_CARRERA_NOTA_NOMBRE_CODIGO);
        todos.add(Comparadores.C7_NOTA_AJUSTADA);

        boolean vaciaOk = true;
        boolean unoOk = true;
        try {
            List<Estudiante> vacia = new ArrayList<>();
            Collections.sort(vacia);
            vaciaOk = vacia.isEmpty();
            List<Estudiante> uno = new ArrayList<>();
            uno.add(original.get(0));
            Collections.sort(uno);
            unoOk = uno.size() == 1 && uno.get(0).getCodigo().equals("E014");
            for (Comparator<Estudiante> c : todos) {
                vacia.sort(c);
                uno.sort(c);
                vaciaOk = vaciaOk && vacia.isEmpty();
                unoOk = unoOk && uno.size() == 1 && uno.get(0).getCodigo().equals("E014");
            }
        } catch (Exception ex) {
            vaciaOk = false;
            unoOk = false;
        }
        registrar("Lista vacia con C1-C7", "sin errores, sigue vacia",
                vaciaOk ? "sin errores, sigue vacia" : "error", vaciaOk);
        registrar("Lista de un elemento (E014) con C1-C7", "sin errores, [E014]",
                unoOk ? "sin errores, [E014]" : "error", unoOk);

        // ---- Empate completo: C4 sobre E014, E005, E010 (Ana, 8.5) ----
        List<Estudiante> empate = new ArrayList<>();
        empate.add(buscar(original, "E014"));
        empate.add(buscar(original, "E005"));
        empate.add(buscar(original, "E010"));
        empate.sort(Comparadores.C4_NOTA_DESC_NOMBRE);
        registrar("Empate completo: C4 sobre E014, E005, E010", "E014, E005, E010",
                codigos(empate), codigos(empate).equals("E014, E005, E010"));

        // ---- C2 con lambda vs C2 con referencias ----
        List<Estudiante> c2ref = ordenar(original, Comparadores.C2_NOMBRE_CODIGO_REF);
        registrar("C2 lambda vs C2 comparing/thenComparing", "misma secuencia",
                codigos(c2).equals(codigos(c2ref)) ? "misma secuencia" : "distinta",
                codigos(c2).equals(codigos(c2ref)));

        // ---- Codigos unicos en los datos ----
        boolean unicos = true;
        for (int i = 0; i < original.size(); i++) {
            for (int j = i + 1; j < original.size(); j++) {
                if (original.get(i).getCodigo().equals(original.get(j).getCodigo())) {
                    unicos = false;
                }
            }
        }
        registrar("Datos: codigos unicos", "20 codigos distintos",
                unicos ? "20 codigos distintos" : "hay repetidos", unicos);

        // ---- Caso propio 1: tope de Math.min en C7 ----
        Estudiante alta = new Estudiante("E099", "Prueba", 20, 9.8, "Computacion");
        double ajustada = Comparadores.notaAjustada(alta);
        registrar("Propio 1: C7 no pasa de 10 (Computacion, nota 9.8)", "ajustada 10.0, original 9.8",
                "ajustada " + ajustada + ", original " + alta.getNota(),
                ajustada == 10.0 && alta.getNota() == 9.8);

        // ---- Caso propio 2: el constructor rechaza notas invalidas ----
        String obtenido2 = "";
        boolean rechaza105 = false;
        boolean rechazaNaN = false;
        try {
            new Estudiante("E098", "Prueba", 20, 10.5, "Computacion");
        } catch (IllegalArgumentException ex) {
            rechaza105 = true;
        }
        try {
            new Estudiante("E097", "Prueba", 20, Double.NaN, "Computacion");
        } catch (IllegalArgumentException ex) {
            rechazaNaN = true;
        }
        obtenido2 = "10.5 " + (rechaza105 ? "rechazada" : "aceptada")
                + ", NaN " + (rechazaNaN ? "rechazada" : "aceptada");
        registrar("Propio 2: notas fuera de rango o no finitas", "10.5 rechazada, NaN rechazada",
                obtenido2, rechaza105 && rechazaNaN);

        // ---- Lista original intacta ----
        registrar("Lista original tras todas las consultas", "misma secuencia inicial (E014 ... E013)",
                codigos(original).equals(secuenciaInicial) ? "misma secuencia inicial (E014 ... E013)" : codigos(original),
                codigos(original).equals(secuenciaInicial));

        System.out.println();
        System.out.println("Resultado: " + aprobadas + "/" + total + " pruebas aprobadas");
    }

    // ---------------- utilidades ----------------

    private static List<Estudiante> ordenar(List<Estudiante> original, Comparator<Estudiante> c) {
        List<Estudiante> copia = new ArrayList<>(original);
        copia.sort(c);
        return copia;
    }

    /** Verifica 20 codigos conservados y orden correcto entre consecutivos. */
    private static void verificarConsulta(String nombre, List<Estudiante> resultado,
                                          Comparator<Estudiante> c, List<Estudiante> original) {
        boolean conserva = resultado.size() == original.size();
        for (Estudiante e : original) {
            if (buscar(resultado, e.getCodigo()) == null) {
                conserva = false;
            }
        }
        registrar(nombre + ": conserva los 20 codigos", "20 codigos",
                conserva ? resultado.size() + " codigos" : "faltan codigos", conserva);

        boolean ordenado = true;
        for (int i = 0; i < resultado.size() - 1; i++) {
            int cmp;
            if (c == null) {
                cmp = resultado.get(i).compareTo(resultado.get(i + 1));
            } else {
                cmp = c.compare(resultado.get(i), resultado.get(i + 1));
            }
            if (cmp > 0) {
                ordenado = false;
            }
        }
        registrar(nombre + ": orden entre consecutivos", "compare(i, i+1) <= 0",
                ordenado ? "compare(i, i+1) <= 0" : "hay pares desordenados", ordenado);
    }

    private static Estudiante buscar(List<Estudiante> lista, String codigo) {
        for (Estudiante e : lista) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }

    private static String codigos(List<Estudiante> lista) {
        String texto = "";
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) {
                texto += ", ";
            }
            texto += lista.get(i).getCodigo();
        }
        return texto;
    }

    private static String primeros(List<Estudiante> lista, int n) {
        return codigos(lista.subList(0, n));
    }

    private static String conNota(List<Estudiante> lista, double nota) {
        List<Estudiante> filtro = new ArrayList<>();
        for (Estudiante e : lista) {
            if (Double.compare(e.getNota(), nota) == 0) {
                filtro.add(e);
            }
        }
        return codigos(filtro);
    }

    private static String secuenciaE001aE020() {
        String texto = "";
        for (int i = 1; i <= 20; i++) {
            if (i > 1) {
                texto += ", ";
            }
            texto += (i < 10 ? "E00" : "E0") + i;
        }
        return texto;
    }

    private static void registrar(String escenario, String esperado, String obtenido, boolean ok) {
        total++;
        if (ok) {
            aprobadas++;
        }
        System.out.println("| " + total + " | " + escenario + " | " + esperado + " | "
                + obtenido + " | " + (ok ? "OK" : "FALLA") + " |");
    }
}
