import java.util.Scanner;

/**
 * Clase que maneja la lógica del juego de Sudoku en modo consola.
 */
public class JuegoSudoku {
    private Sudoku sudoku;
    private Scanner scanner;

    /**
     * Constructor que inicializa el juego
     */
    public JuegoSudoku() {
        sudoku = new Sudoku();
        scanner = new Scanner(System.in);
    }

    /**
     * Inicia el juego de Sudoku desde la consola
     */
    public void iniciar() {
        System.out.println("¡Bienvenido al juego de Sudoku!");

        // Seleccionar dificultad
        String dificultad = seleccionarDificultad();

        // Generar tablero según dificultad
        sudoku.generarTablero(dificultad);

        // Bucle principal del juego
        boolean jugando = true;
        while (jugando) {
            // Mostrar el tablero actual
            System.out.println("\nTablero actual:");
            sudoku.mostrarTablero();

            // Mostrar menú de opciones
            System.out.println("\nOpciones:");
            System.out.println("1. Colocar un número");
            System.out.println("2. Verificar si está resuelto");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    colocarNumero();
                    break;
                case 2:
                    verificarSolucion();
                    break;
                case 3:
                    jugando = false;
                    System.out.println("¡Gracias por jugar!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }

            // Verificar si el tablero está resuelto después de cada movimiento
            if (sudoku.estaResuelto()) {
                System.out.println("\n¡Felicidades! Has resuelto el Sudoku correctamente.");
                sudoku.mostrarTablero();

                System.out.print("¿Desea jugar otra partida? (s/n): ");
                String respuesta = scanner.next().toLowerCase();
                if (respuesta.equals("s")) {
                    // Reiniciar el juego
                    dificultad = seleccionarDificultad();
                    sudoku.generarTablero(dificultad);
                } else {
                    jugando = false;
                    System.out.println("¡Gracias por jugar!");
                }
            }
        }

        scanner.close();
    }

    /**
     * Permite al usuario seleccionar una dificultad
     * @return Dificultad seleccionada
     */
    private String seleccionarDificultad() {
        System.out.println("\nSeleccione la dificultad:");
        System.out.println("1. Fácil");
        System.out.println("2. Medio");
        System.out.println("3. Difícil");
        System.out.print("Opción: ");

        int opcion = leerEntero();

        switch (opcion) {
            case 1:
                return "facil";
            case 2:
                return "medio";
            case 3:
                return "dificil";
            default:
                System.out.println("Opción no válida. Se seleccionará dificultad fácil por defecto.");
                return "facil";
        }
    }

    /**
     * Permite al usuario colocar un número en el tablero
     */
    private void colocarNumero() {
        System.out.println("\nIntroduzca la posición y el valor:");

        System.out.print("Fila (0-8): ");
        int fila = leerEntero();

        System.out.print("Columna (0-8): ");
        int columna = leerEntero();

        System.out.print("Valor (1-9): ");
        int valor = leerEntero();

        // Intentar colocar el número
        boolean exito = sudoku.colocarNumero(fila, columna, valor);

        if (exito) {
            System.out.println("Número colocado correctamente.");
        }
    }

    /**
     * Verifica si el tablero está resuelto
     */
    private void verificarSolucion() {
        if (sudoku.estaResuelto()) {
            System.out.println("¡Felicidades! El Sudoku está resuelto correctamente.");
        } else {
            System.out.println("El Sudoku aún no está resuelto completamente o contiene errores.");
        }
    }

    /**
     * Lee un entero desde la consola, con validación
     * @return Entero leído
     */
    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.print("Por favor, ingrese un número válido: ");
            }
        }
    }

    /**
     * Punto de entrada principal para el modo consola
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        JuegoSudoku juego = new JuegoSudoku();
        juego.iniciar();
    }
}
