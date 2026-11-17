# PL2.1 – Ordenamiento de estudiantes con `Comparable` y `Comparator`

**Autor:** Daniel Flores
**Asignatura:** Estructura de Datos – Ing. Pablo Torres, Mgtr.

## Objetivo

Ordenar un mismo grupo de 20 estudiantes con distintos criterios: el orden natural (código) con `Comparable<Estudiante>` y los demás con `Comparator` (lambdas, referencias a métodos, `comparing`, `thenComparing`, `reversed`), verificando que se conservan los registros, que el orden es correcto, que `List.sort()` es estable y que la lista original no cambia.

## Versión de JDK

```
openjdk version "21.0.12.1" 2026-08-18
```
(Java puro, sin Maven, Gradle ni dependencias externas.)

## Estructura

```
PL2.1/
├── src/
│   ├── modelo/Estudiante.java          # Modelo inmutable + Comparable (orden natural: código)
│   ├── comparadores/Comparadores.java  # Comparadores C2–C7 y nota ajustada de C7
│   ├── datos/DatosPrueba.java          # Los 20 registros de trabajo
│   └── app/
│       ├── Main.java                   # Ejecuta C1–C7 y el análisis
│       └── Pruebas.java                # Pruebas y tabla de resultados
├── evidencias/
│   ├── salida_consultas.txt            # Salida real de Main
│   ├── tabla_pruebas.md                # Salida real de Pruebas (escenario/esperado/obtenido/estado)
│   └── version_jdk.txt
└── README.md
```

## Compilación y ejecución

**Terminal** (desde la carpeta `PL2.1`):

```bash
javac -d out src/modelo/*.java src/datos/*.java src/comparadores/*.java src/app/*.java
java -cp out app.Main       # consultas C1–C7
java -cp out app.Pruebas    # pruebas
```

Para regenerar las evidencias:

```bash
java -cp out app.Main > evidencias/salida_consultas.txt
java -cp out app.Pruebas > evidencias/tabla_pruebas.md
```

**IDE** (IntelliJ / NetBeans / VS Code): abrir la carpeta `PL2.1`, marcar `src` como carpeta de fuentes (Sources Root) y ejecutar `app.Main` o `app.Pruebas` con el botón *Run*.

## Consultas C1–C7

| Consulta | Criterios | Método empleado |
|---|---|---|
| C1 | código asc. | `Comparable` (`compareTo`) + `Collections.sort()` |
| C2 | nombre asc., código asc. | Lambda de dos parámetros `(a, b) -> {...}` |
| C3 | nota asc., sin desempate | `Comparator.comparingDouble(Estudiante::getNota)` |
| C4 | nota desc., nombre asc. | `comparingDouble(...).reversed().thenComparing(Estudiante::getNombre)` |
| C5 | edad asc., nombre desc. | `comparingInt(Estudiante::getEdad).thenComparing(comparing(Estudiante::getNombre).reversed())` |
| C6 | carrera asc., nota desc., nombre asc., código asc. | `comparing` + `thenComparing(comparingDouble(...).reversed())` + `thenComparing` x2 |
| C7 | nota ajustada desc., nombre asc., código asc. | `comparingDouble(Comparadores::notaAjustada).reversed()` + `thenComparing` x2 |

Cada consulta se hace sobre `new ArrayList<>(original)`, nunca sobre la lista original. Los textos se comparan con `String.compareTo()` y los números con `comparingDouble`/`comparingInt` (sin restas).

## Resultados

| Consulta | Primeros registros obtenidos | Esperado |
|---|---|---|
| C1 | E001, E002, …, E020 | E001 a E020 |
| C2 | E005, E010, E014, E017 | E005, E010, E014, E017 |
| C3 (nota 8.5) | E014, E007, E005, E008, E010 | mismo orden relativo original |
| C4 | E017, E011, E001 | E017, E011, E001 |
| C5 | E001, E016, E004 | E001, E016, E004 |
| C6 | E001, E018, E005, E010, E014 | E001, E018, E005, E010, E014 |
| C7 | E001 (9.5 → 10.0), E018 (9.2 → 9.7) | E001 10.0, E018 9.7 |

Pruebas: **29/29 aprobadas** (ver `evidencias/tabla_pruebas.md`). La salida completa está en `evidencias/salida_consultas.txt`.

### Casos propios

1. **Tope de la nota ajustada (C7):** un estudiante de Computación con 9.8 debe quedar con 10.0 ajustada (no 10.3) y su nota original debe seguir siendo 9.8. Comprueba que `Math.min(10.0, ...)` funciona y que el objeto no se modifica.
2. **Validación de notas:** crear un estudiante con nota 10.5 o `NaN` debe lanzar `IllegalArgumentException`. Comprueba la regla del modelo de que las notas sean finitas y estén entre 0 y 10.

## Análisis

**Signo de `compareTo()`:** `E014.compareTo(E003)` devuelve un valor positivo (E014 va después), `E003.compareTo(E014)` uno negativo (va antes) y comparar un código consigo mismo devuelve 0.

**C2 con lambda vs. `comparing()`/`thenComparing()`:** ambas versiones producen exactamente la misma secuencia de 20 códigos. La versión con referencias a métodos es más corta, pero hace lo mismo que la lambda: compara nombre y, si da 0, compara código.

**Invertir un comparador compuesto:** `reversed()` invierte todo el comparador sobre el que se llama. Si se escribe `comparingInt(edad).thenComparing(nombre).reversed()`, se invierte también la edad y la lista empieza por los de 22 años (E002, E012, E015) en lugar de los de 18 (ver “C5 INCORRECTO” en la salida). Por eso en C4 el `reversed()` va justo después de `comparingDouble(nota)`, antes de `thenComparing(nombre)`, y en C5 el `reversed()` se aplica solo al comparador del nombre.

**Nota igual con códigos distintos:** `C3.compare(E014, E007)` devuelve 0 porque ambos tienen 8.5, aunque sus códigos sean distintos. El comparador solo mira la nota, así que para él son “iguales”; como `List.sort()` es estable, mantiene el orden que tenían en la lista original (E014, E007, E005, E008, E010).

## Conclusiones

1. El proyecto mostró que ordenar datos no es solo acomodarlos, sino definir con claridad qué criterio importa y en qué orden.
2. Separar el código en partes claras y probarlo paso a paso hizo que fuera más fácil encontrar errores y confiar en los resultados.
3. Trabajar con un mismo grupo de datos y consultarlo de muchas formas sin alterarlo demuestra lo útil que es planificar antes de programar.

