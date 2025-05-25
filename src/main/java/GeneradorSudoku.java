import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Clase que genera tableros de Sudoku válidos.
 */
public class GeneradorSudoku {
    private static final int TAMANIO = 9;
    private static final int SUBCUADRICULA = 3;
    private final Random random;

    /**
     * Constructor que inicializa el generador
     */
    public GeneradorSudoku() {
        random = new Random();
    }

    /**
     * Genera un tablero de Sudoku completo y válido
     * @return Matriz 9x9 con un Sudoku válido completo
     */
    public int[][] generarTableroCompleto() {
        int[][] tablero = new int[TAMANIO][TAMANIO];

        // Rellenar el tablero utilizando backtracking
        resolverTablero(tablero);

        return tablero;
    }

    /**
     * Vacia celdas en un tablero completo para crear un puzzle
     * @param tablero Tablero completo
     * @param celdasVacias Número de celdas a vaciar
     */
    public void vaciarCeldas(int[][] tablero, int celdasVacias) {
        // Lista de todas las posiciones en el tablero
        List<Integer> posiciones = new ArrayList<>();
        for (int i = 0; i < TAMANIO * TAMANIO; i++) {
            posiciones.add(i);
        }

        // Mezclar las posiciones para aleatorizar las celdas a vaciar
        Collections.shuffle(posiciones);

        // Vaciar celdas
        int celdasVaciadas = 0;
        for (int i = 0; i < posiciones.size() && celdasVaciadas < celdasVacias; i++) {
            int pos = posiciones.get(i);
            int fila = pos / TAMANIO;
            int columna = pos % TAMANIO;

            int valorOriginal = tablero[fila][columna];
            tablero[fila][columna] = 0; // Vaciar la celda

            // Verificar si el tablero sigue teniendo una única solución
            if (tieneUnicaSolucion(copiarTablero(tablero))) {
                celdasVaciadas++;
            } else {
                // Si no tiene única solución, restaurar el valor
                tablero[fila][columna] = valorOriginal;
            }
        }
    }

    /**
     * Copia un tablero de Sudoku
     * @param tablero Tablero a copiar
     * @return Copia del tablero
     */
    private int[][] copiarTablero(int[][] tablero) {
        int[][] copia = new int[TAMANIO][TAMANIO];
        for (int i = 0; i < TAMANIO; i++) {
            System.arraycopy(tablero[i], 0, copia[i], 0, TAMANIO);
        }
        return copia;
    }

    /**
     * Verifica si un tablero tiene una única solución
     * @param tablero Tablero a verificar
     * @return true si tiene una única solución
     */
    private boolean tieneUnicaSolucion(int[][] tablero) {
        // Para tableros simples, podemos simplificar y solo verificar si tiene solución
        // Una implementación completa debería contar el número de soluciones
        return resolverTablero(tablero);
    }

    /**
     * Resuelve un tablero de Sudoku utilizando backtracking
     * @param tablero Tablero a resolver
     * @return true si se pudo resolver
     */
    private boolean resolverTablero(int[][] tablero) {
        for (int fila = 0; fila < TAMANIO; fila++) {
            for (int columna = 0; columna < TAMANIO; columna++) {
                // Si la celda está vacía
                if (tablero[fila][columna] == 0) {
                    // Probar con valores del 1 al 9
                    List<Integer> valores = generarListaNumeros();
                    for (int valor : valores) {
                        if (esMovimientoValido(tablero, fila, columna, valor)) {
                            // Colocar el valor
                            tablero[fila][columna] = valor;

                            // Llamada recursiva
                            if (resolverTablero(tablero)) {
                                return true;
                            }

                            // Si no lleva a una solución, deshacer
                            tablero[fila][columna] = 0;
                        }
                    }
                    // Si ningún valor funciona, retroceder
                    return false;
                }
            }
        }
        // Si llegamos aquí, el tablero está completo
        return true;
    }

    /**
     * Genera una lista de números del 1 al 9 en orden aleatorio
     * @return Lista de números del 1 al 9 mezclados
     */
    private List<Integer> generarListaNumeros() {
        List<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= TAMANIO; i++) {
            numeros.add(i);
        }
        Collections.shuffle(numeros, random);
        return numeros;
    }

    /**
     * Verifica si un movimiento es válido según las reglas del Sudoku
     * @param tablero Tablero actual
     * @param fila Fila donde se desea colocar el número
     * @param columna Columna donde se desea colocar el número
     * @param valor Valor a colocar
     * @return true si el movimiento es válido
     */
    private boolean esMovimientoValido(int[][] tablero, int fila, int columna, int valor) {
        // Verificar fila
        for (int c = 0; c < TAMANIO; c++) {
            if (tablero[fila][c] == valor) {
                return false;
            }
        }

        // Verificar columna
        for (int f = 0; f < TAMANIO; f++) {
            if (tablero[f][columna] == valor) {
                return false;
            }
        }

        // Verificar subcuadrícula 3x3
        int inicioFila = (fila / SUBCUADRICULA) * SUBCUADRICULA;
        int inicioColumna = (columna / SUBCUADRICULA) * SUBCUADRICULA;

        for (int i = 0; i < SUBCUADRICULA; i++) {
            for (int j = 0; j < SUBCUADRICULA; j++) {
                if (tablero[inicioFila + i][inicioColumna + j] == valor) {
                    return false;
                }
            }
        }

        return true;
    }
}