import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que implementa la interfaz gráfica del juego de Sudoku.
 */
public class SudokuGUI extends JFrame {
    private static final int TAMANIO = 9;
    private static final int SUBCUADRICULA = 3;

    private Sudoku sudoku;
    private JTextField[][] casillas;
    private JPanel panelTablero;
    private JLabel statusLabel;
    private JButton botonVerificar;
    private JButton botonNuevoJuego;

    /**
     * Constructor que inicializa la interfaz gráfica
     */
    public SudokuGUI() {
        sudoku = new Sudoku();
        inicializarInterfaz();
    }

    /**
     * Inicializa los componentes de la interfaz gráfica
     */
    private void inicializarInterfaz() {
        setTitle("Juego de Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel principal con layout BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de control (superior)
        JPanel panelControl = crearPanelControl();
        panelPrincipal.add(panelControl, BorderLayout.NORTH);

        // Panel del tablero (centro)
        panelTablero = crearPanelTablero();
        panelPrincipal.add(panelTablero, BorderLayout.CENTER);

        // Panel de estado (inferior)
        JPanel panelEstado = crearPanelEstado();
        panelPrincipal.add(panelEstado, BorderLayout.SOUTH);

        // Añadir el panel principal al frame
        add(panelPrincipal);

        // Ajustar tamaño y centrar
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Crea el panel de control con los botones
     * @return Panel de control
     */
    private JPanel crearPanelControl() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Botón para nuevo juego
        botonNuevoJuego = new JButton("Nuevo Juego");
        botonNuevoJuego.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoNuevoJuego();
            }
        });
        panel.add(botonNuevoJuego);

        // Botón para verificar solución
        botonVerificar = new JButton("Verificar Solución");
        botonVerificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarSolucion();
            }
        });
        panel.add(botonVerificar);

        return panel;
    }

    /**
     * Crea el panel del tablero de Sudoku
     * @return Panel del tablero
     */
    private JPanel crearPanelTablero() {
        JPanel panel = new JPanel(new GridLayout(SUBCUADRICULA, SUBCUADRICULA, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        casillas = new JTextField[TAMANIO][TAMANIO];

        // Crear las subcuadrículas 3x3
        for (int bloqueI = 0; bloqueI < SUBCUADRICULA; bloqueI++) {
            for (int bloqueJ = 0; bloqueJ < SUBCUADRICULA; bloqueJ++) {
                JPanel subPanel = new JPanel(new GridLayout(SUBCUADRICULA, SUBCUADRICULA));
                subPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                // Crear las casillas dentro de cada subcuadrícula
                for (int i = 0; i < SUBCUADRICULA; i++) {
                    for (int j = 0; j < SUBCUADRICULA; j++) {
                        int fila = bloqueI * SUBCUADRICULA + i;
                        int columna = bloqueJ * SUBCUADRICULA + j;

                        JTextField casilla = new JTextField();
                        casilla.setHorizontalAlignment(JTextField.CENTER);
                        casilla.setFont(new Font("Arial", Font.BOLD, 20));
                        casilla.setDocument(new LimitadorCampoNumerico(1)); // Limitar a 1 dígito

                        // Guardar referencia a la casilla
                        casillas[fila][columna] = casilla;

                        // Añadir la casilla al subpanel
                        subPanel.add(casilla);
                    }
                }

                // Añadir el subpanel al panel principal
                panel.add(subPanel);
            }
        }

        return panel;
    }

    /**
     * Crea el panel de estado
     * @return Panel de estado
     */
    private JPanel crearPanelEstado() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Bienvenido al Sudoku. Seleccione 'Nuevo Juego' para comenzar.");
        panel.add(statusLabel);
        return panel;
    }

    /**
     * Muestra el diálogo para seleccionar dificultad y comenzar un nuevo juego
     */
    private void mostrarDialogoNuevoJuego() {
        String[] opciones = {"Fácil", "Medio", "Difícil"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "Seleccione el nivel de dificultad:",
                "Nuevo Juego",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (opcion >= 0) {
            String dificultad;
            switch (opcion) {
                case 0:
                    dificultad = "facil";
                    break;
                case 1:
                    dificultad = "medio";
                    break;
                case 2:
                    dificultad = "dificil";
                    break;
                default:
                    dificultad = "facil";
            }

            iniciarNuevoJuego(dificultad);
        }
    }

    /**
     * Inicia un nuevo juego con la dificultad especificada
     * @param dificultad Nivel de dificultad
     */
    private void iniciarNuevoJuego(String dificultad) {
        // Generar nuevo tablero
        sudoku.generarTablero(dificultad);

        // Actualizar la interfaz gráfica
        actualizarTablero();

        // Actualizar mensaje de estado
        statusLabel.setText("Nuevo juego iniciado con dificultad: " + dificultad);
    }

    /**
     * Actualiza la interfaz gráfica con el estado actual del tablero
     */
    private void actualizarTablero() {
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                int valor = sudoku.getValor(i, j);
                JTextField casilla = casillas[i][j];

                // Limpiar casilla
                casilla.setText(valor == 0 ? "" : String.valueOf(valor));

                // Establecer editable o no según si es una celda fija
                boolean esFija = sudoku.esCeldaFija(i, j);
                casilla.setEditable(!esFija);
                casilla.setBackground(esFija ? new Color(240, 240, 240) : Color.WHITE);
                casilla.setForeground(esFija ? Color.BLUE : Color.BLACK);
            }
        }
    }

    /**
     * Verifica si la solución actual es correcta
     */
    private void verificarSolucion() {
        // Actualizar el tablero con los valores de la interfaz
        actualizarTableroDesdeInterfaz();

        // Verificar si está resuelto
        if (sudoku.estaResuelto()) {
            JOptionPane.showMessageDialog(
                    this,
                    "¡Felicidades!🎉🎉 Has resuelto el Sudoku correctamente.✔✔",
                    "Sudoku Resuelto",
                    JOptionPane.INFORMATION_MESSAGE
            );
            statusLabel.setText("¡Sudoku resuelto correctamente!");
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "El Sudoku aún no está resuelto completamente o contiene errores.❌❌",
                    "Verificación",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Actualiza el modelo del tablero con los valores de la interfaz
     */
    private void actualizarTableroDesdeInterfaz() {
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                // Solo actualizar celdas editables (no fijas)
                if (!sudoku.esCeldaFija(i, j)) {
                    String texto = casillas[i][j].getText().trim();
                    int valor = texto.isEmpty() ? 0 : Integer.parseInt(texto);

                    // Validar y colocar el número
                    if (valor > 0) {
                        String error = sudoku.esMovimientoValido(i, j, valor);
                        if (error == null) {
                            sudoku.setValor(i, j, valor);
                        } else {
                            // Marcar error en la interfaz
                            casillas[i][j].setBackground(new Color(255, 200, 200));
                        }
                    } else {
                        sudoku.setValor(i, j, 0);
                    }
                }
            }
        }
    }

    /**
     * Punto de entrada principal para el modo gráfico
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        // Ejecutar la interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SudokuGUI gui = new SudokuGUI();
                gui.setVisible(true);
            }
        });
    }

    /**
     * Clase interna para limitar los campos de texto a solo números
     */
    private class LimitadorCampoNumerico extends javax.swing.text.PlainDocument {
        private int limite;

        public LimitadorCampoNumerico(int limite) {
            this.limite = limite;
        }

        @Override
        public void insertString(int offset, String str, javax.swing.text.AttributeSet attr)
                throws javax.swing.text.BadLocationException {
            if (str == null) return;

            // Permitir solo dígitos del 1 al 9
            if ((getLength() + str.length()) <= limite) {
                String nuevaValor = getText(0, getLength()) + str;
                try {
                    int valor = Integer.parseInt(nuevaValor);
                    if (valor >= 1 && valor <= 9) {
                        super.insertString(offset, str, attr);
                    }
                } catch (NumberFormatException e) {
                    // No es un número, no hacer nada
                }
            }
        }
    }
}

