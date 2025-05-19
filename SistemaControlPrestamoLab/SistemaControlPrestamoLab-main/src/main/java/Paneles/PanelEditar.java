/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

import DataBase.ConexionBD;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Panel para modificar o eliminar datos de usuarios
 * @author DOC
 */
public class PanelEditar extends JPanel {
    
    // Componentes para la búsqueda
    private JTextField txtBusquedaRU;
    private JButton btnBuscar;
    
    // Componentes para edición de datos
    private JTextField txtRU;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtCI;
    private JTextField txtEmail;
    private JPasswordField txtContraseña;
    private JComboBox<String> comboRol;
    
    // Componentes adicionales para administradores
    private JTextField txtSalario;
    private JTextField txtFechaInicio;
    private JTextField txtNumeroTitulo;
    private JPanel panelAdminCampos;
    
    // Botones de acción
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    
    // Usuario actual
    private int usuarioActualRU = -1;
    private String rolActual = "";
    
    public PanelEditar() {
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Configuración del panel principal
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titleLabel = new JLabel("GESTIÓN DE USUARIOS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Panel principal que contendrá todo
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        
        // Panel de búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();
        
        // Panel de datos de usuario
        JPanel panelDatos = crearPanelDatosUsuario();
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        
        // Agregar paneles al panel principal
        mainPanel.add(panelBusqueda);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(panelDatos);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(panelBotones);
        
        // Agregar componentes al panel principal
        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        
        // Inicialmente deshabilitar campos de edición
        habilitarCamposEdicion(false);
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(245, 245, 255));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 180), 1),
            "Búsqueda de Usuario",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 70, 150)
        ));
        
        // Label
        JLabel lblBusqueda = new JLabel("Ingresar RU:");
        lblBusqueda.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Campo de texto
        txtBusquedaRU = new JTextField(10);
        txtBusquedaRU.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Botón de búsqueda
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(70, 130, 180));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setFocusPainted(false);
        
        // Agregar acción al botón
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarUsuario();
            }
        });
        
        // Agregar componentes al panel
        panel.add(lblBusqueda);
        panel.add(txtBusquedaRU);
        panel.add(btnBuscar);
        
        return panel;
    }
    
    private JPanel crearPanelDatosUsuario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 180), 1),
            "Datos del Usuario",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 70, 150)
        ));
        
        // Panel para campos generales
        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Campos base del usuario
        String[] etiquetas = {"RU:", "Nombre:", "Apellido Paterno:", "Apellido Materno:", 
                          "CI:", "Email:", "Contraseña:", "Rol:"};
        
        JTextField[] campos = new JTextField[etiquetas.length];
        campos[0] = txtRU = new JTextField(20);
        campos[1] = txtNombre = new JTextField(20);
        campos[2] = txtApellidoPaterno = new JTextField(20);
        campos[3] = txtApellidoMaterno = new JTextField(20);
        campos[4] = txtCI = new JTextField(20);
        campos[5] = txtEmail = new JTextField(20);
        campos[6] = txtContraseña = new JPasswordField(20);
        comboRol = new JComboBox<>(new String[]{"Estudiante", "Docente", "Administrador"});
        
        // Añadir los campos al panel
        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.weightx = 0.1;
            JLabel label = new JLabel(etiquetas[i]);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            camposPanel.add(label, gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 0.9;
            if (i == 7) { // Para el rol (combobox)
                comboRol.setFont(new Font("Arial", Font.PLAIN, 14));
                comboRol.addActionListener(e -> actualizarCamposAdministrador());
                camposPanel.add(comboRol, gbc);
            } else {
                campos[i].setFont(new Font("Arial", Font.PLAIN, 14));
                camposPanel.add(campos[i], gbc);
            }
        }
        
        // Panel para campos específicos de administrador
        panelAdminCampos = new JPanel(new GridBagLayout());
        panelAdminCampos.setBackground(new Color(245, 245, 255));
        panelAdminCampos.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 180), 1),
            "Datos de Administrador",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 70, 150)
        ));
        
        // Campos de administrador
        String[] etiquetasAdmin = {"Salario:", "Fecha de Inicio:", "Número de Título:"};
        JTextField[] camposAdmin = new JTextField[etiquetasAdmin.length];
        camposAdmin[0] = txtSalario = new JTextField(20);
        camposAdmin[1] = txtFechaInicio = new JTextField(20);
        camposAdmin[2] = txtNumeroTitulo = new JTextField(20);
        
        // Añadir los campos de administrador
        for (int i = 0; i < etiquetasAdmin.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.weightx = 0.1;
            JLabel label = new JLabel(etiquetasAdmin[i]);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            panelAdminCampos.add(label, gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 0.9;
            camposAdmin[i].setFont(new Font("Arial", Font.PLAIN, 14));
            panelAdminCampos.add(camposAdmin[i], gbc);
        }
        
        // Inicialmente ocultar los campos de administrador
        panelAdminCampos.setVisible(false);
        
        // Agregar todo al panel principal
        panel.add(camposPanel, BorderLayout.CENTER);
        panel.add(panelAdminCampos, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Color.WHITE);
        
        // Botón Actualizar
        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(50, 150, 50));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setPreferredSize(new Dimension(120, 40));
        btnActualizar.addActionListener(e -> actualizarUsuario());
        
        // Botón Eliminar
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(200, 50, 50));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setPreferredSize(new Dimension(120, 40));
        btnEliminar.addActionListener(e -> eliminarUsuario());
        
        // Botón Limpiar
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(100, 100, 100));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 14));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setPreferredSize(new Dimension(120, 40));
        btnLimpiar.addActionListener(e -> limpiarCampos());
        
        // Agregar botones al panel
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        
        return panel;
    }
    
    // Método para habilitar/deshabilitar los campos de edición
    private void habilitarCamposEdicion(boolean habilitar) {
        for (JTextField campo : Arrays.asList(txtNombre, txtApellidoPaterno, txtApellidoMaterno, 
                                             txtCI, txtEmail, txtContraseña, txtSalario, 
                                             txtFechaInicio, txtNumeroTitulo)) {
            if (campo != null) {
                campo.setEnabled(habilitar);
            }
        }
        
        // El RU no debería ser editable
        if (txtRU != null) {
            txtRU.setEnabled(false);
        }
        
        if (comboRol != null) {
            comboRol.setEnabled(habilitar);
        }
        
        btnActualizar.setEnabled(habilitar);
        btnEliminar.setEnabled(habilitar);
    }
    
    // Método para actualizar la visibilidad de los campos de administrador
    private void actualizarCamposAdministrador() {
        boolean esAdmin = comboRol.getSelectedItem().equals("Administrador");
        panelAdminCampos.setVisible(esAdmin);
        revalidate();
        repaint();
    }
    
    // Método para buscar un usuario por RU
    private void buscarUsuario() {
        try {
            String ruText = txtBusquedaRU.getText().trim();
            if (ruText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un RU para buscar", 
                                             "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int ru = Integer.parseInt(ruText);
            String sql = "SELECT u.*, a.salario, a.fecha_inicio, a.nro_titulo " +
                         "FROM usuario u " +
                         "LEFT JOIN administrador a ON u.ru = a.ru " +
                         "WHERE u.ru = ?";
            
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, ru);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    // Guardar RU y rol actual para referencias futuras
                    usuarioActualRU = ru;
                    rolActual = rs.getString("rol");
                    
                    // Llenar los campos con los datos del usuario
                    txtRU.setText(String.valueOf(rs.getInt("ru")));
                    txtNombre.setText(rs.getString("nombre"));
                    txtApellidoPaterno.setText(rs.getString("apellido_paterno"));
                    txtApellidoMaterno.setText(rs.getString("apellido_materno"));
                    txtCI.setText(String.valueOf(rs.getInt("ci")));
                    txtEmail.setText(rs.getString("email"));
                    txtContraseña.setText(rs.getString("contra"));
                    
                    // Seleccionar el rol correcto
                    comboRol.setSelectedItem(rolActual.substring(0, 1).toUpperCase() + rolActual.substring(1).toLowerCase());
                    
                    // Si es administrador, llenar los campos adicionales
                    if (rolActual.equalsIgnoreCase("administrador")) {
                        txtSalario.setText(rs.getString("salario"));
                        txtFechaInicio.setText(rs.getString("fecha_inicio"));
                        txtNumeroTitulo.setText(rs.getString("nro_titulo"));
                        panelAdminCampos.setVisible(true);
                    } else {
                        txtSalario.setText("");
                        txtFechaInicio.setText("");
                        txtNumeroTitulo.setText("");
                        panelAdminCampos.setVisible(false);
                    }
                    
                    // Habilitar campos y botones
                    habilitarCamposEdicion(true);
                    
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con RU: " + ru, 
                                                 "Usuario no encontrado", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                }
                
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido para el RU", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar usuario: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    // Método para actualizar los datos de un usuario
    private void actualizarUsuario() {
        if (usuarioActualRU == -1) {
            JOptionPane.showMessageDialog(this, "No hay un usuario seleccionado para actualizar", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Validar datos
            String nombre = txtNombre.getText().trim();
            String apellidoPaterno = txtApellidoPaterno.getText().trim();
            String apellidoMaterno = txtApellidoMaterno.getText().trim();
            String ciText = txtCI.getText().trim();
            String email = txtEmail.getText().trim();
            String contra = new String(txtContraseña.getPassword());
            String nuevoRol = comboRol.getSelectedItem().toString().toLowerCase();
            
            if (nombre.isEmpty() || apellidoPaterno.isEmpty() || ciText.isEmpty() || email.isEmpty() || contra.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios", 
                                             "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int ci;
            try {
                ci = Integer.parseInt(ciText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El CI debe ser un número válido", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Comenzar transacción
            Connection conn = ConexionBD.conectar();
            conn.setAutoCommit(false);
            
            try {
                // Actualizar en la tabla usuario
                String sqlUsuario = "UPDATE usuario SET nombre=?, apellido_paterno=?, apellido_materno=?, " +
                                    "ci=?, email=?, contra=?, rol=? WHERE ru=?";
                
                PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario);
                stmtUsuario.setString(1, nombre);
                stmtUsuario.setString(2, apellidoPaterno);
                stmtUsuario.setString(3, apellidoMaterno);
                stmtUsuario.setInt(4, ci);
                stmtUsuario.setString(5, email);
                stmtUsuario.setString(6, contra);
                stmtUsuario.setString(7, nuevoRol);
                stmtUsuario.setInt(8, usuarioActualRU);
                
                stmtUsuario.executeUpdate();
                
                // Si el rol actual era administrador pero el nuevo no lo es, eliminar de la tabla administrador
                if (rolActual.equalsIgnoreCase("administrador") && !nuevoRol.equalsIgnoreCase("administrador")) {
                    String sqlEliminarAdmin = "DELETE FROM administrador WHERE ru=?";
                    PreparedStatement stmtEliminarAdmin = conn.prepareStatement(sqlEliminarAdmin);
                    stmtEliminarAdmin.setInt(1, usuarioActualRU);
                    stmtEliminarAdmin.executeUpdate();
                }
                
                // Si el nuevo rol es administrador
                if (nuevoRol.equalsIgnoreCase("administrador")) {
                    String salarioText = txtSalario.getText().trim();
                    String fechaInicio = txtFechaInicio.getText().trim();
                    String nroTitulo = txtNumeroTitulo.getText().trim();
                    
                    if (salarioText.isEmpty() || fechaInicio.isEmpty()) {
                        throw new Exception("Por favor, complete los campos de administrador");
                    }
                    
                    double salario;
                    try {
                        salario = Double.parseDouble(salarioText);
                    } catch (NumberFormatException ex) {
                        throw new Exception("El salario debe ser un número válido");
                    }
                    
                    // Si ya era administrador, actualizar
                    if (rolActual.equalsIgnoreCase("administrador")) {
                        String sqlUpdateAdmin = "UPDATE administrador SET salario=?, fecha_inicio=?, nro_titulo=? WHERE ru=?";
                        PreparedStatement stmtUpdateAdmin = conn.prepareStatement(sqlUpdateAdmin);
                        stmtUpdateAdmin.setDouble(1, salario);
                        stmtUpdateAdmin.setString(2, fechaInicio);
                        stmtUpdateAdmin.setString(3, nroTitulo);
                        stmtUpdateAdmin.setInt(4, usuarioActualRU);
                        stmtUpdateAdmin.executeUpdate();
                    } else {
                        // Si no era administrador, insertar
                        String sqlInsertAdmin = "INSERT INTO administrador (ru, salario, fecha_inicio, nro_titulo) VALUES (?, ?, ?, ?)";
                        PreparedStatement stmtInsertAdmin = conn.prepareStatement(sqlInsertAdmin);
                        stmtInsertAdmin.setInt(1, usuarioActualRU);
                        stmtInsertAdmin.setDouble(2, salario);
                        stmtInsertAdmin.setString(3, fechaInicio);
                        stmtInsertAdmin.setString(4, nroTitulo);
                        stmtInsertAdmin.executeUpdate();
                    }
                }
                
                // Confirmar transacción
                conn.commit();
                
                JOptionPane.showMessageDialog(this, "Usuario actualizado con éxito", 
                                             "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar el rol actual
                rolActual = nuevoRol;
                
            } catch (Exception ex) {
                // Revertir en caso de error
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Error al actualizar usuario: " + ex.getMessage(), 
                                             "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    // Método para eliminar un usuario
    private void eliminarUsuario() {
        if (usuarioActualRU == -1) {
            JOptionPane.showMessageDialog(this, "No hay un usuario seleccionado para eliminar", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirmar eliminación
        int confirmar = JOptionPane.showConfirmDialog(this, 
                                                     "¿Está seguro de eliminar este usuario? Esta acción no se puede deshacer.", 
                                                     "Confirmar eliminación", 
                                                     JOptionPane.YES_NO_OPTION, 
                                                     JOptionPane.WARNING_MESSAGE);
        
        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            Connection conn = ConexionBD.conectar();
            conn.setAutoCommit(false);
            
            try {
                // Si es administrador, primero eliminar de la tabla administrador
                if (rolActual.equalsIgnoreCase("administrador")) {
                    String sqlEliminarAdmin = "DELETE FROM administrador WHERE ru=?";
                    PreparedStatement stmtEliminarAdmin = conn.prepareStatement(sqlEliminarAdmin);
                    stmtEliminarAdmin.setInt(1, usuarioActualRU);
                    stmtEliminarAdmin.executeUpdate();
                }
                
                // Eliminar de la tabla usuario
                String sqlEliminarUsuario = "DELETE FROM usuario WHERE ru=?";
                PreparedStatement stmtEliminarUsuario = conn.prepareStatement(sqlEliminarUsuario);
                stmtEliminarUsuario.setInt(1, usuarioActualRU);
                stmtEliminarUsuario.executeUpdate();
                
                // Confirmar transacción
                conn.commit();
                
                JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito", 
                                             "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar campos y resetear variables
                limpiarCampos();
                usuarioActualRU = -1;
                rolActual = "";
                
            } catch (SQLException ex) {
                // Revertir en caso de error
                conn.rollback();
                
                if (ex.getMessage().contains("foreign key constraint")) {
                    JOptionPane.showMessageDialog(this, 
                                                 "No se puede eliminar este usuario porque está siendo usado en otras tablas.", 
                                                 "Error de eliminación", 
                                                 JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + ex.getMessage(), 
                                                 "Error", JOptionPane.ERROR_MESSAGE);
                }
                ex.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    // Método para limpiar todos los campos
    private void limpiarCampos() {
        for (JTextField campo : Arrays.asList(txtRU, txtNombre, txtApellidoPaterno, txtApellidoMaterno, 
                                            txtCI, txtEmail, txtContraseña, txtSalario, 
                                            txtFechaInicio, txtNumeroTitulo, txtBusquedaRU)) {
            if (campo != null) {
                campo.setText("");
            }
        }
        
        if (comboRol != null) {
            comboRol.setSelectedIndex(0);
        }
        
        panelAdminCampos.setVisible(false);
        habilitarCamposEdicion(false);
        usuarioActualRU = -1;
        rolActual = "";
    }
}