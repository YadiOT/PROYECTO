package Ventanas;

import exceptions.CredencialesInvalidas;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import Clases.Administrador;
import Controles.ControlAdministrador;

/**
 * Ventana para registrar un nuevo administrador en el sistema
 * @author DOC
 */
public class NuevoAdministrador extends JFrame {
    private JTextField cajaNombre, cajaAPP, cajaAPM, cajaCorreo, cajaContra, cajaRU, cajaCI, cajaFI, cajaNIT, cajaSalario;
    private JComboBox<String> cajarol;

    public NuevoAdministrador() {
        inicializarComponentes();
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setVisible(true);
    }

    private void inicializarComponentes() {
        setTitle("Sistema de Control de Préstamos - Nuevo Administrador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 600)); // Tamaño más compacto
        setResizable(true); // Permitimos redimensionar la ventana

        // Fondo con degradado
        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 60, 120), 0, getHeight(), new Color(60, 120, 180));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fondoPanel.setLayout(new GridBagLayout());
        setContentPane(fondoPanel);

        // Panel principal del formulario
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
            }
        };
        formPanel.setBackground(Color.WHITE);
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setLayout(null);
        formPanel.setPreferredSize(new Dimension(500, 500));

        // Añadir el panel al fondoPanel usando GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        fondoPanel.add(formPanel, gbc);

        // Título
        JLabel titulo = new JLabel("Registrar Nuevo Administrador", SwingConstants.CENTER);
        titulo.setBounds(0, 10, 500, 30);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(30, 60, 120));
        formPanel.add(titulo);

        // Subtítulo
        JLabel subtitulo = new JLabel("Complete los datos para crear un nuevo administrador", SwingConstants.CENTER);
        subtitulo.setBounds(0, 40, 500, 20);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitulo.setForeground(Color.GRAY);
        formPanel.add(subtitulo);

        // Campos del formulario (dos columnas)
        int padding = 30;
        int labelWidth = 120;
        int fieldWidth = 150;
        int fieldHeight = 30;
        int yStart = 70;
        int yGap = 40;

        // Columna izquierda
        // Nombre
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(padding, yStart, labelWidth, 20);
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nombreLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(nombreLabel);

        cajaNombre = new JTextField();
        cajaNombre.setBounds(padding + labelWidth, yStart, fieldWidth, fieldHeight);
        cajaNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaNombre.setForeground(Color.BLACK);
        cajaNombre.setBackground(new Color(245, 245, 245));
        cajaNombre.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaNombre.setText("Ingrese el nombre");
        cajaNombre.setForeground(Color.GRAY);
        cajaNombre.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaNombre.getText().equals("Ingrese el nombre")) {
                    cajaNombre.setText("");
                    cajaNombre.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaNombre.getText().isEmpty()) {
                    cajaNombre.setText("Ingrese el nombre");
                    cajaNombre.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaNombre);

        // Apellido Paterno
        JLabel appLabel = new JLabel("Apellido Paterno:");
        appLabel.setBounds(padding, yStart + yGap, labelWidth, 20);
        appLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        appLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(appLabel);

        cajaAPP = new JTextField();
        cajaAPP.setBounds(padding + labelWidth, yStart + yGap, fieldWidth, fieldHeight);
        cajaAPP.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaAPP.setForeground(Color.BLACK);
        cajaAPP.setBackground(new Color(245, 245, 245));
        cajaAPP.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaAPP.setText("Ingrese el apellido paterno");
        cajaAPP.setForeground(Color.GRAY);
        cajaAPP.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaAPP.getText().equals("Ingrese el apellido paterno")) {
                    cajaAPP.setText("");
                    cajaAPP.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaAPP.getText().isEmpty()) {
                    cajaAPP.setText("Ingrese el apellido paterno");
                    cajaAPP.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaAPP);

        // Apellido Materno
        JLabel apmLabel = new JLabel("Apellido Materno:");
        apmLabel.setBounds(padding, yStart + 2 * yGap, labelWidth, 20);
        apmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        apmLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(apmLabel);

        cajaAPM = new JTextField();
        cajaAPM.setBounds(padding + labelWidth, yStart + 2 * yGap, fieldWidth, fieldHeight);
        cajaAPM.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaAPM.setForeground(Color.BLACK);
        cajaAPM.setBackground(new Color(245, 245, 245));
        cajaAPM.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaAPM.setText("Ingrese el apellido materno");
        cajaAPM.setForeground(Color.GRAY);
        cajaAPM.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaAPM.getText().equals("Ingrese el apellido materno")) {
                    cajaAPM.setText("");
                    cajaAPM.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaAPM.getText().isEmpty()) {
                    cajaAPM.setText("Ingrese el apellido materno");
                    cajaAPM.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaAPM);

        // Correo Electrónico
        JLabel correoLabel = new JLabel("Correo Electrónico:");
        correoLabel.setBounds(padding, yStart + 3 * yGap, labelWidth, 20);
        correoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        correoLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(correoLabel);

        cajaCorreo = new JTextField();
        cajaCorreo.setBounds(padding + labelWidth, yStart + 3 * yGap, fieldWidth, fieldHeight);
        cajaCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaCorreo.setForeground(Color.BLACK);
        cajaCorreo.setBackground(new Color(245, 245, 245));
        cajaCorreo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaCorreo.setText("ejemplo@correo.com");
        cajaCorreo.setForeground(Color.GRAY);
        cajaCorreo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaCorreo.getText().equals("ejemplo@correo.com")) {
                    cajaCorreo.setText("");
                    cajaCorreo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaCorreo.getText().isEmpty()) {
                    cajaCorreo.setText("ejemplo@correo.com");
                    cajaCorreo.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaCorreo);

        // Fecha de Inicio
        JLabel fechaLabel = new JLabel("Fecha de Inicio:");
        fechaLabel.setBounds(padding, yStart + 4 * yGap, labelWidth, 20);
        fechaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fechaLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(fechaLabel);

        cajaFI = new JTextField();
        cajaFI.setBounds(padding + labelWidth, yStart + 4 * yGap, fieldWidth, fieldHeight);
        cajaFI.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaFI.setForeground(Color.BLACK);
        cajaFI.setBackground(new Color(245, 245, 245));
        cajaFI.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaFI.setText("YYYY-MM-DD");
        cajaFI.setForeground(Color.GRAY);
        cajaFI.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaFI.getText().equals("YYYY-MM-DD")) {
                    cajaFI.setText("");
                    cajaFI.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaFI.getText().isEmpty()) {
                    cajaFI.setText("YYYY-MM-DD");
                    cajaFI.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaFI);

        // NIT
        JLabel nitLabel = new JLabel("Nro. Título:");
        nitLabel.setBounds(padding, yStart + 5 * yGap, labelWidth, 20);
        nitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nitLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(nitLabel);

        cajaNIT = new JTextField();
        cajaNIT.setBounds(padding + labelWidth, yStart + 5 * yGap, fieldWidth, fieldHeight);
        cajaNIT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaNIT.setForeground(Color.BLACK);
        cajaNIT.setBackground(new Color(245, 245, 245));
        cajaNIT.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaNIT.setText("Ingrese el Nro. Título");
        cajaNIT.setForeground(Color.GRAY);
        cajaNIT.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaNIT.getText().equals("Ingrese el Nro. Título")) {
                    cajaNIT.setText("");
                    cajaNIT.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaNIT.getText().isEmpty()) {
                    cajaNIT.setText("Ingrese el Nro. Título");
                    cajaNIT.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaNIT);

        // Columna derecha
        int rightColumnX = padding + labelWidth + fieldWidth + 20; // Espacio entre columnas

        // Contraseña
        JLabel contraLabel = new JLabel("Contraseña:");
        contraLabel.setBounds(rightColumnX, yStart, labelWidth, 20);
        contraLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contraLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(contraLabel);

        cajaContra = new JTextField();
        cajaContra.setBounds(rightColumnX + labelWidth, yStart, fieldWidth, fieldHeight);
        cajaContra.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaContra.setForeground(Color.BLACK);
        cajaContra.setBackground(new Color(245, 245, 245));
        cajaContra.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaContra.setText("Ingrese su contraseña");
        cajaContra.setForeground(Color.GRAY);
        cajaContra.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaContra.getText().equals("Ingrese su contraseña")) {
                    cajaContra.setText("");
                    cajaContra.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaContra.getText().isEmpty()) {
                    cajaContra.setText("Ingrese su contraseña");
                    cajaContra.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaContra);

        // RU
        JLabel ruLabel = new JLabel("R.U.:");
        ruLabel.setBounds(rightColumnX, yStart + yGap, labelWidth, 20);
        ruLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ruLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(ruLabel);

        cajaRU = new JTextField();
        cajaRU.setBounds(rightColumnX + labelWidth, yStart + yGap, fieldWidth, fieldHeight);
        cajaRU.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaRU.setForeground(Color.BLACK);
        cajaRU.setBackground(new Color(245, 245, 245));
        cajaRU.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaRU.setText("Ingrese el R.U.");
        cajaRU.setForeground(Color.GRAY);
        cajaRU.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaRU.getText().equals("Ingrese el R.U.")) {
                    cajaRU.setText("");
                    cajaRU.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaRU.getText().isEmpty()) {
                    cajaRU.setText("Ingrese el R.U.");
                    cajaRU.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaRU);

        // Rol
        JLabel rolLabel = new JLabel("Rol:");
        rolLabel.setBounds(rightColumnX, yStart + 2 * yGap, labelWidth, 20);
        rolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rolLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(rolLabel);

        cajarol = new JComboBox<>(new String[]{"Administrador"});
        cajarol.setBounds(rightColumnX + labelWidth, yStart + 2 * yGap, fieldWidth, fieldHeight);
        cajarol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajarol.setBackground(new Color(245, 245, 245));
        cajarol.setForeground(Color.BLACK);
        formPanel.add(cajarol);

        // CI
        JLabel ciLabel = new JLabel("C.I.:");
        ciLabel.setBounds(rightColumnX, yStart + 3 * yGap, labelWidth, 20);
        ciLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ciLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(ciLabel);

        cajaCI = new JTextField();
        cajaCI.setBounds(rightColumnX + labelWidth, yStart + 3 * yGap, fieldWidth, fieldHeight);
        cajaCI.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaCI.setForeground(Color.BLACK);
        cajaCI.setBackground(new Color(245, 245, 245));
        cajaCI.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaCI.setText("Ingrese el C.I.");
        cajaCI.setForeground(Color.GRAY);
        cajaCI.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaCI.getText().equals("Ingrese el C.I.")) {
                    cajaCI.setText("");
                    cajaCI.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaCI.getText().isEmpty()) {
                    cajaCI.setText("Ingrese el C.I.");
                    cajaCI.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaCI);

        // Salario
        JLabel salarioLabel = new JLabel("Salario:");
        salarioLabel.setBounds(rightColumnX, yStart + 4 * yGap, labelWidth, 20);
        salarioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        salarioLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(salarioLabel);

        cajaSalario = new JTextField();
        cajaSalario.setBounds(rightColumnX + labelWidth, yStart + 4 * yGap, fieldWidth, fieldHeight);
        cajaSalario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaSalario.setForeground(Color.BLACK);
        cajaSalario.setBackground(new Color(245, 245, 245));
        cajaSalario.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaSalario.setText("Ingrese el salario");
        cajaSalario.setForeground(Color.GRAY);
        cajaSalario.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaSalario.getText().equals("Ingrese el salario")) {
                    cajaSalario.setText("");
                    cajaSalario.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaSalario.getText().isEmpty()) {
                    cajaSalario.setText("Ingrese el salario");
                    cajaSalario.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaSalario);

        // Botones
        int buttonWidth = 120;
        int buttonHeight = 30;
        int buttonY = yStart + 6 * yGap + 10; // Ajustamos la posición

        // Botón "Guardar"
        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.setBounds(padding, buttonY, buttonWidth, buttonHeight);
        botonGuardar.setBackground(new Color(30, 120, 60));
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonGuardar.setFocusPainted(false);
        botonGuardar.setBorderPainted(false);
        botonGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonGuardar.setBorder(new LineBorder(new Color(30, 120, 60), 1, true));
        // Efecto hover
        botonGuardar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonGuardar.setBackground(new Color(50, 140, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botonGuardar.setBackground(new Color(30, 120, 60));
            }
        });
        // Acción del botón "Guardar"
        botonGuardar.addActionListener(e -> guardarAdministrador());
        formPanel.add(botonGuardar);

        // Botón "Cancelar"
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBounds(padding + labelWidth + fieldWidth + 20 + labelWidth + fieldWidth - buttonWidth, buttonY, buttonWidth, buttonHeight);
        botonCancelar.setBackground(new Color(180, 70, 70));
        botonCancelar.setForeground(Color.WHITE);
        botonCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonCancelar.setFocusPainted(false);
        botonCancelar.setBorderPainted(false);
        botonCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonCancelar.setBorder(new LineBorder(new Color(180, 70, 70), 1, true));
        botonCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonCancelar.setBackground(new Color(200, 90, 90));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botonCancelar.setBackground(new Color(180, 70, 70));
            }
        });
        botonCancelar.addActionListener(e -> dispose());
        formPanel.add(botonCancelar);
    }

    private void guardarAdministrador() {
        try {
            // Recoger los datos de los campos
            String nombre = cajaNombre.getText().trim();
            String app = cajaAPP.getText().trim();
            String apm = cajaAPM.getText().trim();
            String correo = cajaCorreo.getText().trim();
            String password = cajaContra.getText().trim();
            String ruText = cajaRU.getText().trim();
            String ciText = cajaCI.getText().trim();
            String fechaInicio = cajaFI.getText().trim();
            String nit = cajaNIT.getText().trim();
            String salarioText = cajaSalario.getText().trim();
            String rol = (String) cajarol.getSelectedItem();

            // Validar campos vacíos y placeholders
            if (nombre.isEmpty() || nombre.equals("Ingrese el nombre") ||
                app.isEmpty() || app.equals("Ingrese el apellido paterno") ||
                apm.isEmpty() || apm.equals("Ingrese el apellido materno") ||
                correo.isEmpty() || correo.equals("ejemplo@correo.com") ||
                password.isEmpty() || password.equals("Ingrese su contraseña") ||
                ruText.isEmpty() || ruText.equals("Ingrese el R.U.") ||
                ciText.isEmpty() || ciText.equals("Ingrese el C.I.") ||
                fechaInicio.isEmpty() || fechaInicio.equals("YYYY-MM-DD") ||
                nit.isEmpty() || nit.equals("Ingrese el Nro. Título") ||
                salarioText.isEmpty() || salarioText.equals("Ingrese el salario")) {
                throw new CredencialesInvalidas("Por favor, complete todos los campos");
            }

            // Convertir RU, CI y salario a números, y fecha a Date
            int ru = Integer.parseInt(ruText);
            int ci = Integer.parseInt(ciText);
            Date fechaInicioDate = Date.valueOf(fechaInicio);
            BigDecimal salarioDecimal = new BigDecimal(salarioText);

            // Crear una nueva instancia de Administrador
            Administrador admin = new Administrador(
                ru, nombre, app, apm, password, ci, rol, correo,
                salarioDecimal.doubleValue(), fechaInicioDate, nit
            );

            // Insertar en la base de datos
            ControlAdministrador controlador = new ControlAdministrador();
            controlador.insertarAdministrador(admin);

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this, "Administrador guardado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException nfe) {
            mostrarMensajeError("RU, CI o salario deben ser valores numéricos válidos");
        } catch (IllegalArgumentException iae) {
            mostrarMensajeError("Formato de fecha inválido. Use YYYY-MM-DD");
        } catch (CredencialesInvalidas ex) {
            mostrarMensajeError(ex.getMessage());
        } catch (SQLException sqle) {
            String msg = sqle.getMessage();
            if (msg.contains("Duplicate entry")) {
                mostrarMensajeError("El RU, CI o correo ya está registrado");
            } else {
                mostrarMensajeError("Error al guardar el administrador:\n" + msg);
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error del sistema");
        }
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane optionPane = new JOptionPane(
            mensaje,
            JOptionPane.ERROR_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[] {"Aceptar"},
            "Aceptar"
        );
        JDialog dialog = optionPane.createDialog(this, "Error");
        dialog.setVisible(true);
    }
}