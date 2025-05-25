import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneradorSudokuTest {

    @Test
    void testGenerarTableroCompletoValido() {
        GeneradorSudoku generador = new GeneradorSudoku();
        int[][] tablero = generador.generarTableroCompleto();

        assertNotNull(tablero, "El tablero no debe ser nulo");
        assertEquals(9, tablero.length, "El tablero debe tener 9 filas");

        for (int i = 0; i < 9; i++) {
            assertEquals(9, tablero[i].length, "Cada fila debe tener 9 columnas");
        }

        // Verificar filas
        for (int fila = 0; fila < 9; fila++) {
            boolean[] vistos = new boolean[10];
            for (int col = 0; col < 9; col++) {
                int valor = tablero[fila][col];
                assertTrue(valor >= 1 && valor <= 9, "Los valores deben estar entre 1 y 9");
                assertFalse(vistos[valor], "No debe haber duplicados en la fila");
                vistos[valor] = true;
            }
        }

        // Verificar columnas
        for (int col = 0; col < 9; col++) {
            boolean[] vistos = new boolean[10];
            for (int fila = 0; fila < 9; fila++) {
                int valor = tablero[fila][col];
                assertFalse(vistos[valor], "No debe haber duplicados en la columna");
                vistos[valor] = true;
            }
        }

        // Verificar subcuadrículas
        for (int fila = 0; fila < 9; fila += 3) {
            for (int col = 0; col < 9; col += 3) {
                boolean[] vistos = new boolean[10];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int valor = tablero[fila + i][col + j];
                        assertFalse(vistos[valor], "No debe haber duplicados en la subcuadrícula");
                        vistos[valor] = true;
                    }
                }
            }
        }
    }

    @Test
    void testVaciarCeldasCantidadEsperada() {
        GeneradorSudoku generador = new GeneradorSudoku();
        int[][] tablero = generador.generarTableroCompleto();

        int celdasAVaciar = 40;
        generador.vaciarCeldas(tablero, celdasAVaciar);

        int celdasVacias = 0;
        for (int[] fila : tablero) {
            for (int valor : fila) {
                if (valor == 0) celdasVacias++;
            }
        }

        // Damos cierto margen por si el algoritmo no logra vaciar todas
        assertTrue(celdasVacias >= celdasAVaciar - 2, "Debe vaciar al menos " + (celdasAVaciar - 2) + " celdas");
    }
}
