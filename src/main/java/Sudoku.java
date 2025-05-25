
/**
 * Clase principal que representa un tablero de Sudoku y sus operaciones.
 */
public class Sudoku {
    // Tamaño del tablero de Sudoku (9x9)
    public static final int TAMANIO = 9;
    // Tamaño de cada subcuadrícula (3x3)
    public static final int SUBCUADRICULA = 3;

    // Matriz que representa el tablero del Sudoku
    private int[][] tablero;
    // Matriz que indica las celdas que no se pueden modificar (valores iniciales)
    private boolean[][] celdasFijas;

    /**
     * Constructor que inicializa un tablero vacío
     */
    public Sudoku() {
        tablero = new int[TAMANIO][TAMANIO];
        celdasFijas = new boolean[TAMANIO][TAMANIO];
    }

    /**
     * Genera un tablero válido según el nivel de dificultad
     * @param dificultad Nivel de dificultad ("facil", "medio", "dificil")
     */
    public void generarTablero(String dificultad) {
        // Utiliza GeneradorSudoku para crear un nuevo tablero
        GeneradorSudoku generador = new GeneradorSudoku();
        int[][] nuevoTablero = generador.generarTableroCompleto();

        // Determina cuántas celdas vaciar según la dificultad
        int celdasVacias;
        switch (dificultad.toLowerCase()) {
            case "facil":
                celdasVacias = 30;
                break;
            case "medio":
                celdasVacias = 40;
                break;
            case "dificil":
                celdasVacias = 50;
                break;
            default:
                celdasVacias = 30; // Por defecto, nivel fácil
        }

        // Aplica el patrón según la dificultad
        generador.vaciarCeldas(nuevoTablero, celdasVacias);

        // Actualiza el tablero y marca las celdas fijas
        this.tablero = nuevoTablero;
        actualizarCeldasFijas();
    }

    /**
     * Marca las celdas no vacías como fijas (no modificables)
     */
    private void actualizarCeldasFijas() {
        celdasFijas = new boolean[TAMANIO][TAMANIO];
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                celdasFijas[i][j] = tablero[i][j] != 0;
            }
        }
    }

    /**
     * Verifica si un movimiento es válido según las reglas del Sudoku
     * @param fila Fila donde se desea colocar el número (0-8)
     * @param columna Columna donde se desea colocar el número (0-8)
     * @param valor Valor a colocar (1-9)
     * @return Mensaje de error si el movimiento no es válido, o null si es válido
     */
    public String esMovimientoValido(int fila, int columna, int valor) {
        // Validar límites de fila y columna
        if (fila < 0 || fila >= TAMANIO || columna < 0 || columna >= TAMANIO) {
            return "Posición fuera del tablero. Las filas y columnas deben estar entre 0 y 8.";
        }

        // Validar rango del valor
        if (valor < 1 || valor > 9) {
            return "El valor debe estar entre 1 y 9.";
        }

        // Verificar si la celda es fija (no modificable)
        if (celdasFijas[fila][columna]) {
            return "Esta celda es parte del tablero inicial y no puede ser modificada.";
        }

        // Verificar si el valor ya existe en la misma fila
        for (int c = 0; c < TAMANIO; c++) {
            if (tablero[fila][c] == valor) {
                return "El valor " + valor + " ya existe en la fila " + fila + ".";
            }
        }

        // Verificar si el valor ya existe en la misma columna
        for (int f = 0; f < TAMANIO; f++) {
            if (tablero[f][columna] == valor) {
                return "El valor " + valor + " ya existe en la columna " + columna + ".";
            }
        }

        // Verificar si el valor ya existe en la misma subcuadrícula 3x3
        int inicioFila = (fila / SUBCUADRICULA) * SUBCUADRICULA;
        int inicioColumna = (columna / SUBCUADRICULA) * SUBCUADRICULA;

        for (int i = 0; i < SUBCUADRICULA; i++) {
            for (int j = 0; j < SUBCUADRICULA; j++) {
                if (tablero[inicioFila + i][inicioColumna + j] == valor) {
                    return "El valor " + valor + " ya existe en la subcuadrícula.";
                }
            }
        }

        // Si pasa todas las validaciones, el movimiento es válido
        return null;
    }

    /**
     * Coloca un número en la posición especificada si el movimiento es válido
     * @param fila Fila donde colocar el número
     * @param columna Columna donde colocar el número
     * @param valor Valor a colocar
     * @return true si se pudo colocar, false en caso contrario
     */
    public boolean colocarNumero(int fila, int columna, int valor) {
        String error = esMovimientoValido(fila, columna, valor);
        if (error == null) {
            tablero[fila][columna] = valor;
            return true;
        } else {
            System.out.println("Error: " + error);
            return false;
        }
    }

    /**
     * Verifica si el tablero está completamente resuelto y es válido
     * @return true si el tablero está resuelto correctamente
     */
    public boolean estaResuelto() {
        // Verificar que no haya celdas vacías
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (tablero[i][j] == 0) {
                    return false;
                }
            }
        }

        // Verificar filas
        for (int fila = 0; fila < TAMANIO; fila++) {
            boolean[] numeros = new boolean[TAMANIO + 1];
            for (int col = 0; col < TAMANIO; col++) {
                int valor = tablero[fila][col];
                if (valor == 0 || numeros[valor]) {
                    return false;
                }
                numeros[valor] = true;
            }
        }

        // Verificar columnas
        for (int col = 0; col < TAMANIO; col++) {
            boolean[] numeros = new boolean[TAMANIO + 1];
            for (int fila = 0; fila < TAMANIO; fila++) {
                int valor = tablero[fila][col];
                if (valor == 0 || numeros[valor]) {
                    return false;
                }
                numeros[valor] = true;
            }
        }

        // Verificar subcuadrículas
        for (int bloqueFila = 0; bloqueFila < TAMANIO; bloqueFila += SUBCUADRICULA) {
            for (int bloqueCol = 0; bloqueCol < TAMANIO; bloqueCol += SUBCUADRICULA) {
                boolean[] numeros = new boolean[TAMANIO + 1];
                for (int i = 0; i < SUBCUADRICULA; i++) {
                    for (int j = 0; j < SUBCUADRICULA; j++) {
                        int valor = tablero[bloqueFila + i][bloqueCol + j];
                        if (valor == 0 || numeros[valor]) {
                            return false;
                        }
                        numeros[valor] = true;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Imprime el tablero en la consola
     */
    public void mostrarTablero() {
        System.out.println("┌───────┬───────┬───────┐");
        for (int i = 0; i < TAMANIO; i++) {
            System.out.print("│ ");
            for (int j = 0; j < TAMANIO; j++) {
                if (tablero[i][j] == 0) {
                    System.out.print("· ");
                } else {
                    System.out.print(tablero[i][j] + " ");
                }

                if ((j + 1) % SUBCUADRICULA == 0 && j < TAMANIO - 1) {
                    System.out.print("│ ");
                }
            }
            System.out.println("│");

            if ((i + 1) % SUBCUADRICULA == 0 && i < TAMANIO - 1) {
                System.out.println("├───────┼───────┼───────┤");
            }
        }
        System.out.println("└───────┴───────┴───────┘");
    }

    /**
     * Devuelve el valor de una celda
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return Valor en la celda
     */
    public int getValor(int fila, int columna) {
        return tablero[fila][columna];
    }

    /**
     * Verifica si una celda es fija (no modificable)
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return true si la celda es fija
     */
    public boolean esCeldaFija(int fila, int columna) {
        return celdasFijas[fila][columna];
    }

    /**
     * Establece un valor directamente (usado para cargar tableros)
     * @param fila Fila donde colocar el valor
     * @param columna Columna donde colocar el valor
     * @param valor Valor a colocar
     */
    public void setValor(int fila, int columna, int valor) {
        tablero[fila][columna] = valor;
    }
}
