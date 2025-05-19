package Paneles;

import DataBase.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Panel para mostrar y gestionar docentes del sistema
 * @author DOC
 */
public class PanelDocentes extends JPanel {
    
    // Componentes de la interfaz
    private JTable tablaDocentes;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    
    // Constantes de estilo
    private static final Color COLOR_FONDO = Color.WHITE;
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 20);
    private static final Font FUENTE_TABLA = new Font("Arial", Font.PLAIN, 14);
    private static final Font FUENTE_CABECERA = new Font("Arial", Font.BOLD, 14);

    /**
     * Constructor del panel de docentes
     * @param rol Rol del usuario actual (Estudiante, Docente, Administrador)
     */
    public PanelDocentes(String rol) {
        inicializarPanel();
        crearTablaDocentes();
        configurarComponentes(rol);
        cargarDatosDocentes();
    }
    
    private void inicializarPanel() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private void configurarComponentes(String rol) {
        JLabel titleLabel = crearTitulo();
        JPanel panelBotones = crearPanelBotones(rol);
        
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private JLabel crearTitulo() {
        JLabel titleLabel = new JLabel("LISTA DOCENTES", SwingConstants.CENTER);
        titleLabel.setFont(FUENTE_TITULO);
        titleLabel.setForeground(new Color(50, 50, 150));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        return titleLabel;
    }
    
    private void crearTablaDocentes() {
        String[] columnas = {"RU", "Nombre", "Apellido Paterno", "Apellido Materno", "CI", "Correo"};
        
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.setColumnIdentifiers(columnas);
        
        tablaDocentes = new JTable(modeloTabla);
        personalizarTabla();
        
        scrollPane = new JScrollPane(tablaDocentes);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(200, 200, 200))
        ));
    }
    
    private void personalizarTabla() {
        tablaDocentes.setRowHeight(25);
        tablaDocentes.setFont(FUENTE_TABLA);
        tablaDocentes.getTableHeader().setFont(FUENTE_CABECERA);
        tablaDocentes.getTableHeader().setBackground(new Color(210, 210, 255));
        tablaDocentes.setSelectionBackground(new Color(200, 220, 255));
        tablaDocentes.setGridColor(new Color(180, 180, 180));
        tablaDocentes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    private JPanel crearPanelBotones(String rol) {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        btnAgregar = crearBoton("Agregar", new Color(50, 150, 50), 
            e -> abrirFormularioNuevoDocente());
        btnEditar = crearBoton("Editar", new Color(70, 130, 180), 
            e -> editarDocenteSeleccionado());
        btnEliminar = crearBoton("Eliminar", new Color(180, 70, 70), 
            e -> eliminarDocenteSeleccionado());
        btnActualizar = crearBoton("Actualizar", new Color(100, 100, 100), 
            e -> cargarDatosDocentes());
        
        // Deshabilitar botones de edición si el rol es "Estudiante"
        if (rol.equals("Estudiante")) {
            btnAgregar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
        }
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        
        return panelBotones;
    }
    
    private JButton crearBoton(String texto, Color color, java.awt.event.ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(120, 35));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(FUENTE_CABECERA);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.addActionListener(action);
        return boton;
    }
    
    /**
     * Carga los datos de docentes desde la base de datos
     */
    public void cargarDatosDocentes() {
        modeloTabla.setRowCount(0);
        String sql = "SELECT ru, nombre, apellido_paterno, apellido_materno, ci, email " +
                    "FROM usuario " +
                    "WHERE rol = 'docente' ORDER BY ru";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("ru"),
                    rs.getString("nombre"),
                    rs.getString("apellido_paterno"),
                    rs.getString("apellido_materno"),
                    rs.getInt("ci"),
                    rs.getString("email")
                };
                modeloTabla.addRow(fila);
            }
            
            mostrarMensajeSiVacio("No se encontraron docentes en la base de datos");
            
        } catch (SQLException e) {
            mostrarMensajeError("Error al cargar los datos de docentes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void abrirFormularioNuevoDocente() {
        // Crear y mostrar la ventana de agregar docente
        FormularioAgregarDocente formulario = new FormularioAgregarDocente(this);
        formulario.setVisible(true);
    }
    
    // Clase interna para el formulario de agregar docente
    private class FormularioAgregarDocente extends JDialog {
        private JTextField txtRu;
        private JPasswordField txtContra;
        private JTextField txtNombre;
        private JTextField txtApellidoPaterno;
        private JTextField txtApellidoMaterno;
        private JTextField txtCi;
        private JTextField txtEmail;
        
        public FormularioAgregarDocente(Component parent) {
            super(JOptionPane.getFrameForComponent(parent), "Agregar Nuevo Docente", true); // Modal
            inicializarFormulario();
        }
        
        private void inicializarFormulario() {
            setLayout(new BorderLayout(10, 10));
            setBackground(COLOR_FONDO);
            setSize(400, 400);
            setLocationRelativeTo(PanelDocentes.this);
            
            // Panel del formulario
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(COLOR_FONDO);
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Campos del formulario
            txtRu = new JTextField(15);
            txtContra = new JPasswordField(15);
            txtNombre = new JTextField(15);
            txtApellidoPaterno = new JTextField(15);
            txtApellidoMaterno = new JTextField(15);
            txtCi = new JTextField(15);
            txtEmail = new JTextField(15);
            
            // Agregar etiquetas y campos
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(new JLabel("RU:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtRu, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanel.add(new JLabel("Contraseña:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtContra, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 2;
            formPanel.add(new JLabel("Nombre:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtNombre, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 3;
            formPanel.add(new JLabel("Apellido Paterno:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtApellidoPaterno, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 4;
            formPanel.add(new JLabel("Apellido Materno:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtApellidoMaterno, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 5;
            formPanel.add(new JLabel("CI:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtCi, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 6;
            formPanel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtEmail, gbc);
            
            // Panel de botones
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setBackground(COLOR_FONDO);
            
            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.setBackground(new Color(50, 150, 50));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(FUENTE_CABECERA);
            btnGuardar.setFocusPainted(false);
            btnGuardar.setBorderPainted(false);
            btnGuardar.addActionListener(e -> guardarDocente());
            
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setBackground(new Color(180, 70, 70));
            btnCancelar.setForeground(Color.WHITE);
            btnCancelar.setFont(FUENTE_CABECERA);
            btnCancelar.setFocusPainted(false);
            btnCancelar.setBorderPainted(false);
            btnCancelar.addActionListener(e -> dispose());
            
            buttonPanel.add(btnGuardar);
            buttonPanel.add(btnCancelar);
            
            // Agregar paneles al diálogo
            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        private void guardarDocente() {
            try {
                // Validar y obtener los datos
                int ru = Integer.parseInt(txtRu.getText().trim());
                String contra = new String(txtContra.getPassword());
                String nombre = txtNombre.getText().trim();
                String apellidoPaterno = txtApellidoPaterno.getText().trim();
                String apellidoMaterno = txtApellidoMaterno.getText().trim();
                int ci = Integer.parseInt(txtCi.getText().trim());
                String email = txtEmail.getText().trim();
                
                // Validar que los campos obligatorios no estén vacíos
                if (contra.isEmpty() || nombre.isEmpty() || apellidoPaterno.isEmpty() || email.isEmpty()) {
                    mostrarMensajeAdvertencia("Todos los campos son obligatorios, excepto Apellido Materno.");
                    return;
                }
                
                // Agregar el docente a la base de datos
                agregarDocente(ru, contra, nombre, apellidoPaterno, apellidoMaterno, ci, email);
                
                // Cerrar el formulario y recargar los datos
                dispose();
                cargarDatosDocentes();
                
            } catch (NumberFormatException e) {
                mostrarMensajeError("RU y CI deben ser valores numéricos.");
            } catch (SQLException e) {
                mostrarMensajeError("Error al registrar el docente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void agregarDocente(int ru, String contra, String nombre, String apellidoPaterno, 
                               String apellidoMaterno, int ci, String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Insertar en la tabla usuario
            String sql = "INSERT INTO usuario (ru, contra, nombre, apellido_paterno, apellido_materno, ci, rol, email) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 'docente', ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ru);
            stmt.setString(2, contra);
            stmt.setString(3, nombre);
            stmt.setString(4, apellidoPaterno);
            stmt.setString(5, apellidoMaterno != null && !apellidoMaterno.isEmpty() ? apellidoMaterno : null);
            stmt.setInt(6, ci);
            stmt.setString(7, email);
            stmt.executeUpdate();
            
            conn.commit(); // Confirmar transacción
            
            JOptionPane.showMessageDialog(this, "Docente registrado exitosamente", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir transacción en caso de error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e; // Propagar la excepción
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
    
    private void editarDocenteSeleccionado() {
        int filaSeleccionada = tablaDocentes.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarMensajeAdvertencia("Por favor, seleccione un docente para editar");
            return;
        }
        
        // Obtener los datos actuales del docente seleccionado
        int ru = (int) tablaDocentes.getValueAt(filaSeleccionada, 0);
        String nombre = (String) tablaDocentes.getValueAt(filaSeleccionada, 1);
        String apellidoPaterno = (String) tablaDocentes.getValueAt(filaSeleccionada, 2);
        String apellidoMaterno = (String) tablaDocentes.getValueAt(filaSeleccionada, 3);
        int ci = (int) tablaDocentes.getValueAt(filaSeleccionada, 4);
        String email = (String) tablaDocentes.getValueAt(filaSeleccionada, 5);
        
        // Crear un panel para el formulario de edición
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        
        // Campos del formulario con los valores actuales
        JTextField txtRu = new JTextField(String.valueOf(ru), 10);
        txtRu.setEditable(false); // RU no editable
        JTextField txtNombre = new JTextField(nombre, 10);
        JTextField txtApellidoPaterno = new JTextField(apellidoPaterno, 10);
        JTextField txtApellidoMaterno = new JTextField(apellidoMaterno, 10);
        JTextField txtCi = new JTextField(String.valueOf(ci), 10);
        JTextField txtEmail = new JTextField(email, 10);
        
        // Agregar etiquetas y campos al panel
        formPanel.add(new JLabel("RU:"));
        formPanel.add(txtRu);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Apellido Paterno:"));
        formPanel.add(txtApellidoPaterno);
        formPanel.add(new JLabel("Apellido Materno:"));
        formPanel.add(txtApellidoMaterno);
        formPanel.add(new JLabel("CI:"));
        formPanel.add(txtCi);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        
        // Mostrar el formulario en un JOptionPane
        int result = JOptionPane.showConfirmDialog(this, formPanel, 
            "Editar Docente", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Obtener los datos editados
                String nuevoNombre = txtNombre.getText().trim();
                String nuevoApellidoPaterno = txtApellidoPaterno.getText().trim();
                String nuevoApellidoMaterno = txtApellidoMaterno.getText().trim();
                int nuevoCi = Integer.parseInt(txtCi.getText().trim());
                String nuevoEmail = txtEmail.getText().trim();
                
                // Validar que los campos obligatorios no estén vacíos
                if (nuevoNombre.isEmpty() || nuevoApellidoPaterno.isEmpty() || nuevoEmail.isEmpty()) {
                    mostrarMensajeAdvertencia("Todos los campos son obligatorios, excepto Apellido Materno.");
                    return;
                }
                
                // Actualizar el docente en la base de datos
                actualizarDocente(ru, nuevoNombre, nuevoApellidoPaterno, nuevoApellidoMaterno, nuevoCi, nuevoEmail);
                
                // Recargar los datos para reflejar los cambios
                cargarDatosDocentes();
                
            } catch (NumberFormatException e) {
                mostrarMensajeError("CI debe ser un valor numérico.");
            } catch (SQLException e) {
                mostrarMensajeError("Error al actualizar el docente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void actualizarDocente(int ru, String nombre, String apellidoPaterno, String apellidoMaterno, 
                                  int ci, String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Actualizar en la tabla usuario
            String sql = "UPDATE usuario SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, " +
                        "ci = ?, email = ? WHERE ru = ? AND rol = 'docente'";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, apellidoPaterno);
            stmt.setString(3, apellidoMaterno != null && !apellidoMaterno.isEmpty() ? apellidoMaterno : null);
            stmt.setInt(4, ci);
            stmt.setString(5, email);
            stmt.setInt(6, ru);
            stmt.executeUpdate();
            
            conn.commit(); // Confirmar transacción
            
            JOptionPane.showMessageDialog(this, "Docente actualizado exitosamente", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir transacción en caso de error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e; // Propagar la excepción
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
    
    private void eliminarDocenteSeleccionado() {
        int filaSeleccionada = tablaDocentes.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarMensajeAdvertencia("Por favor, seleccione un docente para eliminar");
            return;
        }
        
        int ru = (int) tablaDocentes.getValueAt(filaSeleccionada, 0);
        String nombre = tablaDocentes.getValueAt(filaSeleccionada, 1) + " " +
                       tablaDocentes.getValueAt(filaSeleccionada, 2);
        
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar al docente " + nombre + "?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                eliminarDocente(ru);
                cargarDatosDocentes(); // Recargar datos después de eliminar
            } catch (SQLException e) {
                mostrarMensajeError("Error al eliminar el docente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void eliminarDocente(int ru) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Eliminar de la tabla usuario
            String sql = "DELETE FROM usuario WHERE ru = ? AND rol = 'docente'";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ru);
            stmt.executeUpdate();
            
            conn.commit(); // Confirmar transacción
            
            JOptionPane.showMessageDialog(this, "Docente eliminado exitosamente", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir transacción en caso de error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e; // Propagar la excepción
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
    
    // Métodos de utilidad para mensajes
    private void mostrarMensajeSiVacio(String mensaje) {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, mensaje, "Información", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarMensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}