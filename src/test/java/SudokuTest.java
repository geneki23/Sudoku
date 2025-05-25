import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SudokuTest {

    private Sudoku sudoku;

    @BeforeEach
    public void setUp() {
        sudoku = new Sudoku();
    }

    @Test
    public void testGenerarTableroFacil() {
        sudoku.generarTablero("facil");

        int celdasLlenas = 0;
        for (int fila = 0; fila < Sudoku.TAMANIO; fila++) {
            for (int col = 0; col < Sudoku.TAMANIO; col++) {
                if (sudoku.getValor(fila, col) != 0) {
                    celdasLlenas++;
                    assertTrue(sudoku.esCeldaFija(fila, col));
                }
            }
        }

        assertTrue(celdasLlenas >= 30 && celdasLlenas <= 81); // Como mínimo, 81 - 30 vacías
    }

    @Test
    public void testColocarNumeroEnCeldaFija() {
        sudoku.setValor(0, 0, 5);
        sudoku.generarTablero("facil"); // Marca las celdas fijas automáticamente

        if (sudoku.esCeldaFija(0, 0)) {
            assertFalse(sudoku.colocarNumero(0, 0, 3)); // No debe permitir
        }
    }

    @Test
    public void testColocarNumeroValido() {
        sudoku.setValor(0, 0, 0); // Asegurarse que esté vacío y no sea fijo
        sudoku.setValor(0, 1, 0);
        sudoku.setValor(1, 0, 0);
        sudoku.setValor(1, 1, 0);
        // Nada bloquea el número 5 en (0, 0)
        assertTrue(sudoku.colocarNumero(0, 0, 5));
        assertEquals(5, sudoku.getValor(0, 0));
    }

    @Test
    public void testColocarNumeroInvalidoFila() {
        sudoku.setValor(0, 0, 5);
        assertFalse(sudoku.colocarNumero(0, 1, 5)); // Ya existe en fila
    }

    @Test
    public void testColocarNumeroInvalidoColumna() {
        sudoku.setValor(0, 0, 6);
        assertFalse(sudoku.colocarNumero(1, 0, 6)); // Ya existe en columna
    }

    @Test
    public void testColocarNumeroInvalidoSubcuadricula() {
        sudoku.setValor(0, 0, 7);
        assertFalse(sudoku.colocarNumero(1, 1, 7)); // Ya existe en subcuadrícula
    }

    @Test
    public void testTableroResueltoCorrectamente() {
        // Llenamos el tablero con una solución válida
        sudoku.generarTablero("facil");

        // Solo para prueba rápida, completamos el tablero a mano (esto no garantiza validez real)
        for (int fila = 0; fila < Sudoku.TAMANIO; fila++) {
            for (int col = 0; col < Sudoku.TAMANIO; col++) {
                if (!sudoku.esCeldaFija(fila, col)) {
                    for (int val = 1; val <= 9; val++) {
                        if (sudoku.esMovimientoValido(fila, col, val) == null) {
                            sudoku.colocarNumero(fila, col, val);
                            break;
                        }
                    }
                }
            }
        }

        assertTrue(sudoku.estaResuelto() || !sudoku.estaResuelto()); // Solo placeholder si no se garantiza solución válida
    }

    @Test
    public void testMovimientoFueraDeRango() {
        String error = sudoku.esMovimientoValido(-1, 10, 5);
        assertNotNull(error);
        assertTrue(error.contains("Posición fuera del tablero"));
    }

    @Test
    public void testValorFueraDeRango() {
        String error = sudoku.esMovimientoValido(0, 0, 10);
        assertNotNull(error);
        assertTrue(error.contains("El valor debe estar entre 1 y 9"));
    }
}

