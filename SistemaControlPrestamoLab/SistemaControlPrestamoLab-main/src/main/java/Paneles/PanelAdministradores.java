package Paneles;

import DataBase.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Panel para mostrar y gestionar administradores del sistema
 * @author DOC
 */
public class PanelAdministradores extends JPanel {
    
    // Componentes de la interfaz
    private JTable tablaAdministradores;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;
    
    // Constantes de estilo
    private static final Color COLOR_FONDO = Color.WHITE;
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 20);
    private static final Font FUENTE_TABLA = new Font("Arial", Font.PLAIN, 14);
    private static final Font FUENTE_CABECERA = new Font("Arial", Font.BOLD, 14);

    /**
     * Constructor del panel de administradores
     */
    public PanelAdministradores() {
        inicializarPanel();
        crearTablaAdministradores();
        configurarComponentes();
        cargarDatosAdministradores();
    }
    
    private void inicializarPanel() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private void configurarComponentes() {
        JLabel titleLabel = crearTitulo();
        JPanel panelBotones = crearPanelBotones();
        
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private JLabel crearTitulo() {
        JLabel titleLabel = new JLabel("LISTA ADMINISTRADORES", SwingConstants.CENTER);
        titleLabel.setFont(FUENTE_TITULO);
        titleLabel.setForeground(new Color(50, 50, 150));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        return titleLabel;
    }
    
    private void crearTablaAdministradores() {
        String[] columnas = {
            "RU", "Nombre", "Apellido Paterno", "Apellido Materno", 
            "CI", "Email", "Salario", "Fecha Inicio", "Nro. Título"
        };
        
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.setColumnIdentifiers(columnas);
        
        tablaAdministradores = new JTable(modeloTabla);
        personalizarTabla();
        
        scrollPane = new JScrollPane(tablaAdministradores);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(200, 200, 200))
        ));
    }
    
    private void personalizarTabla() {
        tablaAdministradores.setRowHeight(25);
        tablaAdministradores.setFont(FUENTE_TABLA);
        tablaAdministradores.getTableHeader().setFont(FUENTE_CABECERA);
        tablaAdministradores.getTableHeader().setBackground(new Color(210, 210, 255));
        tablaAdministradores.setSelectionBackground(new Color(200, 220, 255));
        tablaAdministradores.setGridColor(new Color(180, 180, 180));
        tablaAdministradores.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JButton btnAgregar = crearBoton("Agregar", new Color(50, 150, 50), 
            e -> abrirFormularioNuevoAdmin());
        JButton btnEditar = crearBoton("Editar", new Color(70, 130, 180), 
            e -> editarAdminSeleccionado());
        JButton btnEliminar = crearBoton("Eliminar", new Color(180, 70, 70), 
            e -> eliminarAdminSeleccionado());
        JButton btnActualizar = crearBoton("Actualizar", new Color(100, 100, 100), 
            e -> cargarDatosAdministradores());
        
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
     * Carga los datos de administradores desde la base de datos
     */
    public void cargarDatosAdministradores() {
        modeloTabla.setRowCount(0);
        
        String sql = "SELECT u.ru, u.nombre, u.apellido_paterno, u.apellido_materno, u.ci, u.email, " + 
                     "a.salario, a.fecha_inicio, a.nro_titulo " +
                     "FROM usuario u " +
                     "JOIN administrador a ON u.ru = a.ru " +
                     "WHERE u.rol = 'administrador' ORDER BY u.ru";
        
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
                    rs.getString("email"),
                    rs.getDouble("salario"),
                    rs.getDate("fecha_inicio"),
                    rs.getString("nro_titulo")
                };
                modeloTabla.addRow(fila);
            }
            
            mostrarMensajeSiVacio("No se encontraron administradores en la base de datos");
            
        } catch (SQLException e) {
            mostrarMensajeError("Error al cargar los datos de administradores: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void abrirFormularioNuevoAdmin() {
        // Crear y mostrar la ventana de agregar administrador
        FormularioAgregarAdmin formulario = new FormularioAgregarAdmin(this);
        formulario.setVisible(true);
    }
    
    // Clase interna para el formulario de agregar administrador
    private class FormularioAgregarAdmin extends JDialog {
        private JTextField txtRu;
        private JPasswordField txtContra;
        private JTextField txtNombre;
        private JTextField txtApellidoPaterno;
        private JTextField txtApellidoMaterno;
        private JTextField txtCi;
        private JTextField txtEmail;
        private JTextField txtSalario;
        private JTextField txtFechaInicio;
        private JTextField txtNroTitulo;
        
        public FormularioAgregarAdmin(Component parent) {
            super(JOptionPane.getFrameForComponent(parent), "Agregar Nuevo Administrador", true); // Modal
            inicializarFormulario();
        }
        
        private void inicializarFormulario() {
            setLayout(new BorderLayout(10, 10));
            setBackground(COLOR_FONDO);
            setSize(400, 500);
            setLocationRelativeTo(PanelAdministradores.this);
            
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
            txtSalario = new JTextField(15);
            txtFechaInicio = new JTextField("YYYY-MM-DD", 15);
            txtNroTitulo = new JTextField(15);
            
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
            
            gbc.gridx = 0;
            gbc.gridy = 7;
            formPanel.add(new JLabel("Salario:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtSalario, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 8;
            formPanel.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtFechaInicio, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 9;
            formPanel.add(new JLabel("Nro. Título:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtNroTitulo, gbc);
            
            // Panel de botones
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setBackground(COLOR_FONDO);
            
            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.setBackground(new Color(50, 150, 50));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(FUENTE_CABECERA);
            btnGuardar.setFocusPainted(false);
            btnGuardar.setBorderPainted(false);
            btnGuardar.addActionListener(e -> guardarAdministrador());
            
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
        
        private void guardarAdministrador() {
            try {
                // Validar y obtener los datos
                int ru = Integer.parseInt(txtRu.getText().trim());
                String contra = new String(txtContra.getPassword());
                String nombre = txtNombre.getText().trim();
                String apellidoPaterno = txtApellidoPaterno.getText().trim();
                String apellidoMaterno = txtApellidoMaterno.getText().trim();
                int ci = Integer.parseInt(txtCi.getText().trim());
                String email = txtEmail.getText().trim();
                double salario = Double.parseDouble(txtSalario.getText().trim());
                String fechaInicio = txtFechaInicio.getText().trim();
                String nroTitulo = txtNroTitulo.getText().trim();
                
                // Validar que los campos obligatorios no estén vacíos
                if (contra.isEmpty() || nombre.isEmpty() || apellidoPaterno.isEmpty() || email.isEmpty() || 
                    fechaInicio.isEmpty() || nroTitulo.isEmpty()) {
                    mostrarMensajeAdvertencia("Todos los campos son obligatorios, excepto Apellido Materno.");
                    return;
                }
                
                // Validar formato de la fecha
                if (!validarFecha(fechaInicio)) {
                    mostrarMensajeAdvertencia("La fecha debe tener el formato YYYY-MM-DD y ser válida.");
                    return;
                }
                
                // Agregar el administrador a la base de datos
                agregarAdministrador(ru, contra, nombre, apellidoPaterno, apellidoMaterno, ci, email, 
                                    salario, fechaInicio, nroTitulo);
                
                // Cerrar el formulario y recargar los datos
                dispose();
                cargarDatosAdministradores();
                
            } catch (NumberFormatException e) {
                mostrarMensajeError("RU, CI y Salario deben ser valores numéricos.");
            } catch (SQLException e) {
                mostrarMensajeError("Error al registrar el administrador: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        private boolean validarFecha(String fecha) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false); // No permitir fechas inválidas (ej. 2023-13-01)
                sdf.parse(fecha);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
    }
    
    private void agregarAdministrador(int ru, String contra, String nombre, String apellidoPaterno, 
                                     String apellidoMaterno, int ci, String email, double salario, 
                                     String fechaInicio, String nroTitulo) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtUsuario = null;
        PreparedStatement stmtAdmin = null;
        
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Insertar en la tabla usuario
            String sqlUsuario = "INSERT INTO usuario (ru, contra, nombre, apellido_paterno, apellido_materno, ci, rol, email) " +
                               "VALUES (?, ?, ?, ?, ?, ?, 'administrador', ?)";
            stmtUsuario = conn.prepareStatement(sqlUsuario);
            stmtUsuario.setInt(1, ru);
            stmtUsuario.setString(2, contra);
            stmtUsuario.setString(3, nombre);
            stmtUsuario.setString(4, apellidoPaterno);
            stmtUsuario.setString(5, apellidoMaterno != null && !apellidoMaterno.isEmpty() ? apellidoMaterno : null);
            stmtUsuario.setInt(6, ci);
            stmtUsuario.setString(7, email);
            stmtUsuario.executeUpdate();
            
            // Insertar en la tabla administrador
            String sqlAdmin = "INSERT INTO administrador (ru, salario, fecha_inicio, nro_titulo) " +
                             "VALUES (?, ?, ?, ?)";
            stmtAdmin = conn.prepareStatement(sqlAdmin);
            stmtAdmin.setInt(1, ru);
            stmtAdmin.setDouble(2, salario);
            stmtAdmin.setDate(3, java.sql.Date.valueOf(fechaInicio)); // Usar java.sql.Date
            stmtAdmin.setString(4, nroTitulo);
            stmtAdmin.executeUpdate();
            
            conn.commit(); // Confirmar transacción
            
            JOptionPane.showMessageDialog(this, "Administrador registrado exitosamente", 
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
            if (stmtUsuario != null) stmtUsuario.close();
            if (stmtAdmin != null) stmtAdmin.close();
            if (conn != null) conn.close();
        }
    }
    
    private void editarAdminSeleccionado() {
        int filaSeleccionada = tablaAdministradores.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarMensajeAdvertencia("Por favor, seleccione un administrador para editar");
            return;
        }
        
        int ru = (int) tablaAdministradores.getValueAt(filaSeleccionada, 0);
        String nombre = (String) tablaAdministradores.getValueAt(filaSeleccionada, 1);
        String apellidoPaterno = (String) tablaAdministradores.getValueAt(filaSeleccionada, 2);
        String apellidoMaterno = (String) tablaAdministradores.getValueAt(filaSeleccionada, 3);
        int ci = (int) tablaAdministradores.getValueAt(filaSeleccionada, 4);
        String email = (String) tablaAdministradores.getValueAt(filaSeleccionada, 5);
        double salario = (double) tablaAdministradores.getValueAt(filaSeleccionada, 6);
        String fechaInicio = tablaAdministradores.getValueAt(filaSeleccionada, 7).toString();
        String nroTitulo = (String) tablaAdministradores.getValueAt(filaSeleccionada, 8);
        
        // Crear un panel para el formulario de edición
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 5, 5));
        
        // Campos del formulario con los valores actuales
        JTextField txtRu = new JTextField(String.valueOf(ru), 10);
        txtRu.setEditable(false); // RU no editable
        JTextField txtNombre = new JTextField(nombre, 10);
        JTextField txtApellidoPaterno = new JTextField(apellidoPaterno, 10);
        JTextField txtApellidoMaterno = new JTextField(apellidoMaterno, 10);
        JTextField txtCi = new JTextField(String.valueOf(ci), 10);
        JTextField txtEmail = new JTextField(email, 10);
        JTextField txtSalario = new JTextField(String.valueOf(salario), 10);
        JTextField txtFechaInicio = new JTextField(fechaInicio, 10);
        JTextField txtNroTitulo = new JTextField(nroTitulo, 10);
        
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
        formPanel.add(new JLabel("Salario:"));
        formPanel.add(txtSalario);
        formPanel.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"));
        formPanel.add(txtFechaInicio);
        formPanel.add(new JLabel("Nro. Título:"));
        formPanel.add(txtNroTitulo);
        
        // Mostrar el formulario en un JOptionPane
        int result = JOptionPane.showConfirmDialog(this, formPanel, 
            "Editar Administrador", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Obtener los datos editados
                String nuevoNombre = txtNombre.getText().trim();
                String nuevoApellidoPaterno = txtApellidoPaterno.getText().trim();
                String nuevoApellidoMaterno = txtApellidoMaterno.getText().trim();
                int nuevoCi = Integer.parseInt(txtCi.getText().trim());
                String nuevoEmail = txtEmail.getText().trim();
                double nuevoSalario = Double.parseDouble(txtSalario.getText().trim());
                String nuevaFechaInicio = txtFechaInicio.getText().trim();
                String nuevoNroTitulo = txtNroTitulo.getText().trim();
                
                // Validar que los campos obligatorios no estén vacíos
                if (nuevoNombre.isEmpty() || nuevoApellidoPaterno.isEmpty() || nuevoEmail.isEmpty() || 
                    nuevaFechaInicio.isEmpty() || nuevoNroTitulo.isEmpty()) {
                    mostrarMensajeAdvertencia("Todos los campos son obligatorios, excepto Apellido Materno.");
                    return;
                }
                
                // Validar formato de la fecha
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                sdf.parse(nuevaFechaInicio); // Validar la fecha
                
                // Actualizar el administrador en la base de datos
                actualizarAdministrador(ru, nuevoNombre, nuevoApellidoPaterno, nuevoApellidoMaterno, 
                                       nuevoCi, nuevoEmail, nuevoSalario, nuevaFechaInicio, nuevoNroTitulo);
                
                // Recargar los datos para reflejar los cambios
                cargarDatosAdministradores();
                
            } catch (NumberFormatException e) {
                mostrarMensajeError("CI y Salario deben ser valores numéricos.");
            } catch (ParseException e) {
                mostrarMensajeError("La fecha debe tener el formato YYYY-MM-DD y ser válida.");
            } catch (SQLException e) {
                mostrarMensajeError("Error al actualizar el administrador: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void actualizarAdministrador(int ru, String nombre, String apellidoPaterno, String apellidoMaterno, 
                                        int ci, String email, double salario, String fechaInicio, 
                                        String nroTitulo) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtUsuario = null;
        PreparedStatement stmtAdmin = null;
        
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Actualizar en la tabla usuario
            String sqlUsuario = "UPDATE usuario SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, " +
                               "ci = ?, email = ? WHERE ru = ? AND rol = 'administrador'";
            stmtUsuario = conn.prepareStatement(sqlUsuario);
            stmtUsuario.setString(1, nombre);
            stmtUsuario.setString(2, apellidoPaterno);
            stmtUsuario.setString(3, apellidoMaterno != null && !apellidoMaterno.isEmpty() ? apellidoMaterno : null);
            stmtUsuario.setInt(4, ci);
            stmtUsuario.setString(5, email);
            stmtUsuario.setInt(6, ru);
            stmtUsuario.executeUpdate();
            
            // Actualizar en la tabla administrador
            String sqlAdmin = "UPDATE administrador SET salario = ?, fecha_inicio = ?, nro_titulo = ? WHERE ru = ?";
            stmtAdmin = conn.prepareStatement(sqlAdmin);
            stmtAdmin.setDouble(1, salario);
            stmtAdmin.setDate(2, java.sql.Date.valueOf(fechaInicio)); // Usar java.sql.Date
            stmtAdmin.setString(3, nroTitulo);
            stmtAdmin.setInt(4, ru);
            stmtAdmin.executeUpdate();
            
            conn.commit(); // Confirmar transacción
            
            JOptionPane.showMessageDialog(this, "Administrador actualizado exitosamente", 
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
            if (stmtUsuario != null) stmtUsuario.close();
            if (stmtAdmin != null) stmtAdmin.close();
            if (conn != null) conn.close();
        }
    }
    
    private void eliminarAdminSeleccionado() {
        int filaSeleccionada = tablaAdministradores.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarMensajeAdvertencia("Por favor, seleccione un administrador para eliminar");
            return;
        }
        
        int ru = (int) tablaAdministradores.getValueAt(filaSeleccionada, 0);
        String nombre = tablaAdministradores.getValueAt(filaSeleccionada, 1) + " " +
                       tablaAdministradores.getValueAt(filaSeleccionada, 2);
        
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar al administrador " + nombre + "?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                eliminarAdministrador(ru);
                cargarDatosAdministradores(); // Recargar datos después de eliminar
            } catch (SQLException e) {
                mostrarMensajeError("Error al eliminar el administrador: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void eliminarAdministrador(int ru) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtAdmin = null;
        PreparedStatement stmtUsuario = null;
        
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Eliminar de la tabla administrador
            String sqlAdmin = "DELETE FROM administrador WHERE ru = ?";
            stmtAdmin = conn.prepareStatement(sqlAdmin);
            stmtAdmin.setInt(1, ru);
            stmtAdmin.executeUpdate();
            
            // Eliminar de la tabla usuario
            String sqlUsuario = "DELETE FROM usuario WHERE ru = ? AND rol = 'administrador'";
            stmtUsuario = conn.prepareStatement(sqlUsuario);
            stmtUsuario.setInt(1, ru);
            stmtUsuario.executeUpdate();
            
            conn.commit(); // Confirmar transacción
            
            JOptionPane.showMessageDialog(this, "Administrador eliminado exitosamente", 
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
            if (stmtAdmin != null) stmtAdmin.close();
            if (stmtUsuario != null) stmtUsuario.close();
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