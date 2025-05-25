import java.util.Scanner;

/**
 * Clase principal que sirve como punto de entrada para la aplicación.
 * Permite elegir entre el modo consola y el modo gráfico de forma interactiva.
 */
public class Main {

    /**
     * Metodo principal que inicia la aplicación
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        // Si se pasan argumentos, verificar si se especificó un modo de ejecución
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("consola")) {
                iniciarModoConsola();
                return;
            } else if (args[0].equalsIgnoreCase("grafico")) {
                iniciarModoGrafico();
                return;
            }
        }

        // Si no hay argumentos o no son válidos, preguntar al usuario
        Scanner scanner = new Scanner(System.in);
        System.out.println("¡Bienvenido al juego de Sudoku!");
        System.out.println("¿Cómo deseas jugar?");
        System.out.println("1. Modo Consola");
        System.out.println("2. Modo Gráfico");
        System.out.print("Selecciona una opción (1-2): ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());

            if (opcion == 1) {
                iniciarModoConsola();
            } else if (opcion == 2) {
                iniciarModoGrafico();
            } else {
                System.out.println("Opción no válida. Iniciando modo gráfico por defecto...");
                iniciarModoGrafico();
            }
        } catch (Exception e) {
            System.out.println("Entrada no válida. Iniciando modo gráfico por defecto...");
            iniciarModoGrafico();
        }
    }

    /**
     * Inicia el juego en modo consola
     */
    private static void iniciarModoConsola() {
        System.out.println("Iniciando Sudoku en modo consola...");
        JuegoSudoku juego = new JuegoSudoku();
        juego.iniciar();
    }

    /**
     * Inicia el juego en modo gráfico
     */
    private static void iniciarModoGrafico() {
        System.out.println("Iniciando Sudoku en modo gráfico...");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SudokuGUI gui = new SudokuGUI();
                gui.setVisible(true);
            }
        });
    }
}
