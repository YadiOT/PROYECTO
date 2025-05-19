package Ventanas;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import Paneles.PanelDocentes;
import Paneles.PanelAdministradores;
import Paneles.PanelEstudiantes;

/**
 * Ventana principal del Sistema de Control y Préstamo de Laboratorios para docentes.
 * Ofrece una interfaz moderna, elegante y fácil de usar para gestionar laboratorios, usuarios, equipos y préstamos.
 *
 * @author DOC
 */
public class Principal2 extends JFrame {

    private JPanel contentPanel;
    private JLabel usuarioLabel;
    private static final Color PRIMARY_COLOR = new Color(30, 60, 120); // Azul oscuro para el fondo
    private static final Color SECONDARY_COLOR = new Color(235, 245, 255); // Azul claro para fondos secundarios
    private static final Color ACCENT_COLOR = new Color(52, 152, 219); // Azul vibrante para botones
    private static final Color TEXT_COLOR = Color.WHITE; // Blanco para texto en botones
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 80); // Sombra sutil
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public Principal2() {
        setTitle("Sistema de Control y Préstamo de Laboratorios - Docente");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 500));
        setLayout(new BorderLayout());

        // Panel de fondo azul sólido
        JPanel backgroundPanel = createBackgroundPanel();
        setContentPane(backgroundPanel);

        // Panel de encabezado
        JPanel headerPanel = createHeaderPanel();
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel de menú principal
        JPanel menuPanel = createMenuPanel();
        backgroundPanel.add(menuPanel, BorderLayout.CENTER);

        // Centrar la ventana
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Crea un panel de fondo azul sólido sin animaciones.
     */
    private JPanel createBackgroundPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(PRIMARY_COLOR); // Fondo azul sólido
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        panel.setLayout(new BorderLayout());
        return panel;
    }

    /**
     * Crea el panel de encabezado con la marca de la universidad y controles de usuario.
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 97, 140)); // Color del encabezado como en Principal
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Título de la universidad
        JLabel titleLabel = new JLabel("Universidad Salesiana de Bolivia");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(HEADER_FONT);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Controles de usuario
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        userPanel.setOpaque(false);

        usuarioLabel = new JLabel("Docente ▼");
        usuarioLabel.setForeground(Color.WHITE);
        usuarioLabel.setFont(BUTTON_FONT);
        usuarioLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        userPanel.add(usuarioLabel);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(BUTTON_FONT);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(new RoundedBorder(12));
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(250, 100, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(231, 76, 60));
            }
        });
        logoutButton.addActionListener(e -> dispose());
        userPanel.add(logoutButton);

        headerPanel.add(userPanel, BorderLayout.EAST);
        return headerPanel;
    }

    /**
     * Crea el panel de menú principal con botones y área de contenido.
     */
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel de botones de menú
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setOpaque(false);

        // Panel de contenido
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new RoundedBorder(10));

        // Mensaje de bienvenida
        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema de Control y Préstamo de Laboratorios", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setForeground(new Color(44, 62, 80)); // Color más formal
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Añadir botones de menú
        buttonPanel.add(createMenuButton("Laboratorios", new String[]{"Horarios"}));
        buttonPanel.add(createMenuButton("Usuarios", new String[]{"Docentes", "Estudiantes"}));
        buttonPanel.add(createMenuButton("Equipos", new String[]{"Máquinas"}));
        buttonPanel.add(createMenuButton("Préstamos", new String[]{"Solicitar Préstamo", "Notificaciones"}));

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea un botón de menú estilizado con un menú desplegable.
     */
    private JButton createMenuButton(String title, String[] subOptions) {
        JButton button = new JButton(title) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2d.setColor(SHADOW_COLOR);
                g2d.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, 20, 20);
                // Fondo del botón
                GradientPaint gradient = new GradientPaint(0, 0, ACCENT_COLOR, 0, getHeight(), new Color(41, 128, 185));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();

                // Asegurar que el texto se dibuje correctamente
                g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight) / 2 - 2;
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No dibujar borde adicional para mantener el estilo limpio
            }
        };
        button.setForeground(TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(20));
        button.setPreferredSize(new Dimension(200, 45));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(52, 170, 220));
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
                button.repaint();
            }
        });

        // Menú desplegable
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(SECONDARY_COLOR);
        popupMenu.setBorder(new RoundedBorder(10));

        for (String subOption : subOptions) {
            JMenuItem menuItem = new JMenuItem(subOption);
            menuItem.setFont(LABEL_FONT);
            menuItem.setBackground(SECONDARY_COLOR);
            menuItem.setForeground(new Color(44, 62, 80));
            menuItem.setBorder(new EmptyBorder(8, 15, 8, 15));
            menuItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    menuItem.setBackground(new Color(200, 230, 255));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    menuItem.setBackground(SECONDARY_COLOR);
                }
            });
            menuItem.addActionListener(e -> mostrarContenido(title, subOption));
            popupMenu.add(menuItem);
        }

        button.addActionListener(e -> popupMenu.show(button, 0, button.getHeight()));
        return button;
    }

    /**
     * Muestra el contenido seleccionado en el panel principal.
     */
    private void mostrarContenido(String categoria, String subOpcion) {
        contentPanel.removeAll();

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título del contenido
        JLabel titleLabel = new JLabel(categoria.toUpperCase() + " > " + subOpcion);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80)); // Color más formal
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentWrapper.add(titleLabel, BorderLayout.NORTH);

        // Panel de contenido específico
        JPanel contenidoEspecifico = new JPanel();
        contenidoEspecifico.setOpaque(false);
        contenidoEspecifico.setLayout(new GridLayout(0, 5, 15, 15));

        switch (categoria) {
            case "Laboratorios":
                if (subOpcion.equals("Horarios")) {
                    contenidoEspecifico = crearPanelHorarios();
                }
                break;
            case "Usuarios":
                if (subOpcion.equals("Docentes")) {
                    contenidoEspecifico = crearPanelDocentes();
                } else if (subOpcion.equals("Estudiantes")) {
                    contenidoEspecifico = crearPanelEstudiantes();
                }
                break;
            case "Equipos":
                if (subOpcion.equals("Máquinas")) {
                    contenidoEspecifico = crearPanelMaquinas();
                }
                break;
            case "Préstamos":
                if (subOpcion.equals("Solicitar Préstamo")) {
                    contenidoEspecifico = crearPanelSolicitarPrestamo();
                } else if (subOpcion.equals("Notificaciones")) {
                    contenidoEspecifico = crearPanelNotificaciones();
                }
                break;
        }

        contentWrapper.add(contenidoEspecifico, BorderLayout.CENTER);
        contentPanel.add(contentWrapper, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel crearPanelHorarios() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(new JLabel("Panel de Horarios - Implementación Pendiente"));
        return panel;
    }

    private JPanel crearPanelDocentes() {
        return new PanelDocentes("Docente");
    }

    private JPanel crearPanelEstudiantes() {
        return new PanelEstudiantes("Docente");
    }

    private JPanel crearPanelMaquinas() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(new JLabel("Panel de Máquinas - Implementación Pendiente"));
        return panel;
    }

    private JPanel crearPanelSolicitarPrestamo() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(new JLabel("Panel de Solicitud de Préstamo - Implementación Pendiente"));
        return panel;
    }

    private JPanel crearPanelNotificaciones() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(new JLabel("Panel de Notificaciones - Implementación Pendiente"));
        return panel;
    }

    /**
     * Borde personalizado con esquinas redondeadas.
     */
    private static class RoundedBorder implements Border {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(150, 150, 150));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}