/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.ConexionBD;
import Controlador.RolesXUsuarioCommands;
import Controlador.TamborCommands;
import Controlador.UsuarioCommands;
import Modelo.RolesXUsuario;
import Modelo.Tambor;
import Modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mingo
 */
public class PnlUsuarios extends javax.swing.JPanel {
    ConexionBD conexion;
    Usuario u;
    UsuarioCommands uc;
    RolesXUsuario rxu;
    RolesXUsuarioCommands rxuc;
    List<String> listaPermisos;
    int idUsuario;
    String[][] datos;
    DefaultTableModel dtm;
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols_tblUsuarios = new String[]
    {
        "Nombre","Usuario","Estatus"
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlUsuarios() throws Exception {
        initComponents();
        inicializar();
    }
    
    
    //Método que se indica al inicializar el formulario
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        actualizarTablaUsuarios();
    }
    
    //Método para actualizar la tabla de proveedores
    private void actualizarTablaUsuarios() 
    {         
        try 
        {   
            uc = new UsuarioCommands();
            datos = uc.obtenerUsuarios();
            
            dtm = new DefaultTableModel(datos, cols_tblUsuarios){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblUsuarios.setModel(dtm);
            tblUsuarios.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Método para agregar un usuario a BD
    private void agregarUsuario()
    {
        try 
        {
            u = new Usuario();
            uc = new UsuarioCommands();
            
            u.setUserName(txtNombreUsuAgregar.getText().trim());
            u.setPassword(txtPasswordUsuAgregar.getText().trim());
            u.setNombre(txtNombreEmpAgregar.getText().trim());
            
            //Validar que los campos del usuario no esten vacíos
            if (u.getUserName().equals("") || u.getPassword().equals("") || u.getNombre().equals(""))
            {   
                dlgAgregarUsuario.setVisible(false);
                JOptionPane.showMessageDialog(null, "Llene todos los campos","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarUsuario.setVisible(true);
                return;
            }
            
            //Validar que el nombre del usuario no sea mayor a 100 caracteres
            if (u.getUserName().length() > 100 || u.getPassword().length() > 100 || u.getNombre().length() > 100)
            {   
                dlgAgregarUsuario.setVisible(false);
                JOptionPane.showMessageDialog(null, "Solo se admiten 100 caracteres \npara cada uno de los campos","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarUsuario.setVisible(true);
                return;
            }
            
            u = uc.obtenerUsuarioXNombre(u);
            
            //Valida que no haya un usuario registrado con los mismos datos en BD
            if (u.getIdUsuario() == 0) 
            {
                u.setUserName(txtNombreUsuAgregar.getText().trim());
                u.setPassword(txtPasswordUsuAgregar.getText().trim());
                u.setNombre(txtNombreEmpAgregar.getText().trim());
                
                if (cmbEstatusAgregar.getSelectedItem().toString().equals("Activo"))
                {
                    u.setEstatus(1);
                }
                else
                {
                    u.setEstatus(0);
                }
                if (agregarRolesXUsuario() == true)
                {
                    dlgAgregarUsuario.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Usuario registrado correctamente");
                    actualizarTablaUsuarios();
                }
            }
            else
            {
                dlgAgregarUsuario.setVisible(false);
                JOptionPane.showMessageDialog(null, "Ya existe un Usuario con el nombre capturado", "Mensaje", JOptionPane.WARNING_MESSAGE);
                dlgAgregarUsuario.setVisible(true);
            }
        } 
        catch (Exception e) 
        {   
            System.err.println(e);
            dlgAgregarUsuario.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al insertar Usuario", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarUsuario.setVisible(true);
        }
    }
    
    private boolean agregarRolesXUsuario()
    {
        try
        {
            rxuc = new RolesXUsuarioCommands();
            listaPermisos = new ArrayList<>();

            if (chkContabilidadAgregar.isSelected())
            {
                listaPermisos.add("Contabilidad");
            }

            if (chkCrossAgregar.isSelected())
            {
                listaPermisos.add("Cross");
            }

            if (chkProduccionAgregar.isSelected())
            {
                listaPermisos.add("Produccion");
            }

            if (chkSemiterminadoAgregar.isSelected())
            {
                listaPermisos.add("Semiterminado");
            }

            if (chkSistemasAgregar.isSelected())
            {
                listaPermisos.add("Sistemas");
            }

            if (chkTerminadoAgregar.isSelected())
            {
                listaPermisos.add("Terminado");
            }
            
            if (chkRiveraAgregar.isSelected())
            {
                listaPermisos.add("Rivera");
            }

            if (chkEngraseAgregar.isSelected())
            {
                listaPermisos.add("Engrase");
            }

            if (listaPermisos.size() > 0)
            {
                uc.insertarUsuario(u);
                idUsuario = uc.obtenerUltimoId();
                
                for (int i = 0; i < listaPermisos.size(); i++)
                {
                    rxuc.insertarRolXUsuario(idUsuario, listaPermisos.get(i));
                }
                return true;
            }
            else
            {
                dlgAgregarUsuario.setVisible(false);
                JOptionPane.showMessageDialog(null, "Debe dejar al menos un rol activo para cada usuario","Advertencia",JOptionPane.WARNING_MESSAGE);
                dlgAgregarUsuario.setVisible(true);
                return false;
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar Roles del Usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private boolean eliminarRolesXUsuario()
    {
        try
        {
            rxuc = new RolesXUsuarioCommands();
            listaPermisos = new ArrayList<>();
            
            idUsuario = Integer.parseInt(lblIdUsuario.getText());

            if (chkContabilidadEditar.isSelected())
            {
                listaPermisos.add("Contabilidad");
            }

            if (chkCrossEditar.isSelected())
            {
                listaPermisos.add("Cross");
            }

            if (chkProduccionEditar.isSelected())
            {
                listaPermisos.add("Produccion");
            }

            if (chkSemiterminadoEditar.isSelected())
            {
                listaPermisos.add("Semiterminado");
            }

            if (chkSistemasEditar.isSelected())
            {
                listaPermisos.add("Sistemas");
            }

            if (chkTerminadoEditar.isSelected())
            {
                listaPermisos.add("Terminado");
            }
            
            if (chkRiveraEditar.isSelected())
            {
                listaPermisos.add("Rivera");
            }

            if (chkEngraseEditar.isSelected())
            {
                listaPermisos.add("Engrase");
            }
            
            if (listaPermisos.size() > 0)
            {
                rxuc.eliminarRolXUsuario(u);
                for (int i = 0; i < listaPermisos.size(); i++)
                {
                    rxuc.insertarRolXUsuario(idUsuario, listaPermisos.get(i));
                }
                return true;
            }
            else
            {
                dlgEditarUsuario.setVisible(false);
                JOptionPane.showMessageDialog(null, "Debe dejar al menos un rol activo para cada usuario","Advertencia",JOptionPane.WARNING_MESSAGE);
                dlgEditarUsuario.setVisible(true); 
                return false;
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error al insertar Roles del Usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    //Método para editar un usuario en BD
    private void editarUsuario()
    {
        dlgEditarUsuario.setVisible(false);
        if (JOptionPane.showConfirmDialog(null, "¿Realmente desea guardar las modificaciones?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
        {
            try
            {
                u = new Usuario();
                uc = new UsuarioCommands();
                rxuc = new RolesXUsuarioCommands();

                u.setUserName(txtNombreUsuEditar.getText().trim());
                u.setPassword(txtPasswordUsuEditar.getText().trim());
                u.setNombre(txtNombreEmpEditar.getText().trim());

                //Validar que los campos del usuario no esten vacíos
                if (u.getUserName().equals("") || u.getPassword().equals("") || u.getNombre().equals(""))
                {   
                    dlgEditarUsuario.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Llene todos los campos","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgEditarUsuario.setVisible(true);
                    return;
                }

                //Validar que el nombre del usuario no sea mayor a 100 caracteres
                if (u.getUserName().length() > 100 || u.getPassword().length() > 100 || u.getNombre().length() > 100)
                {   
                    dlgAgregarUsuario.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Solo se admiten 100 caracteres \npara cada uno de los campos","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgAgregarUsuario.setVisible(true);
                    return;
                }

                u = uc.obtenerUsuarioXNombre(u);

                //Valida que no haya un usuario resgistrado con los mismos datos en BD
                if (u.getIdUsuario()== 0 || u.getIdUsuario() == Integer.parseInt(lblIdUsuario.getText()))
                {
                    u.setIdUsuario(Integer.parseInt(lblIdUsuario.getText()));
                    u.setUserName(txtNombreUsuEditar.getText().trim());
                    u.setPassword(txtPasswordUsuEditar.getText().trim());
                    u.setNombre(txtNombreEmpEditar.getText().trim());
                    
                    if (cmbEstatusEditar.getSelectedItem().toString().equals("Activo"))
                    {
                        u.setEstatus(1);
                    }
                    else
                    {
                        u.setEstatus(0);
                    }
                    if (eliminarRolesXUsuario() == true)
                    {
                        uc.actualizarUsuario(u);
                        dlgEditarUsuario.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente");
                        actualizarTablaUsuarios();
                    }
                }
                else
                {
                    dlgEditarUsuario.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ya existe un usuario con el nombre capturado", "Mensaje", JOptionPane.WARNING_MESSAGE);
                    dlgEditarUsuario.setVisible(true);
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
                dlgEditarUsuario.setVisible(false);
                JOptionPane.showMessageDialog(null, "Error al actualizar Usuario", "Error", JOptionPane.ERROR_MESSAGE);
                dlgEditarUsuario.setVisible(true);
            }
        }
        else
            dlgEditarUsuario.setVisible(true);
    }
   
    //Método que abre el dialogo para agregar un tambor
    public void abrirDialogoAgregar()
    {   try 
        {
            dlgAgregarUsuario.setSize(580, 340);
            dlgAgregarUsuario.setPreferredSize(dlgAgregarUsuario.getSize());
            dlgAgregarUsuario.setLocationRelativeTo(null);
            dlgAgregarUsuario.setModal(true);
            dlgAgregarUsuario.setVisible(true);
            
            txtNombreUsuAgregar.setText("");
            txtPasswordUsuAgregar.setText("");
            txtNombreEmpAgregar.setText("");
            cmbEstatusAgregar.setSelectedIndex(0);

            chkContabilidadAgregar.setSelected(false);
            chkCrossAgregar.setSelected(false);
            chkProduccionAgregar.setSelected(false);
            chkSemiterminadoAgregar.setSelected(false);
            chkSistemasAgregar.setSelected(false);
            chkTerminadoAgregar.setSelected(false);
            
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgAgregarUsuario.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarUsuario.setVisible(true);
        }
    }
    
    //Método que abre el dialogo para editar un tambor
    public void abrirDialogoEditar()
    {   try 
        {
            dlgEditarUsuario.setSize(580, 340);
            dlgEditarUsuario.setPreferredSize(dlgEditarUsuario.getSize());
            dlgEditarUsuario.setLocationRelativeTo(null);
            dlgEditarUsuario.setModal(true);
            dlgEditarUsuario.setVisible(true);
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgEditarUsuario.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgEditarUsuario.setVisible(true);
        }
    }
            
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup = new javax.swing.ButtonGroup();
        dlgAgregarUsuario = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombreUsuAgregar = new javax.swing.JTextField();
        cmbEstatusAgregar = new javax.swing.JComboBox<>();
        btnGuardarAgregar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNombreEmpAgregar = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        chkContabilidadAgregar = new javax.swing.JCheckBox();
        chkCrossAgregar = new javax.swing.JCheckBox();
        chkProduccionAgregar = new javax.swing.JCheckBox();
        chkSemiterminadoAgregar = new javax.swing.JCheckBox();
        chkSistemasAgregar = new javax.swing.JCheckBox();
        chkTerminadoAgregar = new javax.swing.JCheckBox();
        chkRiveraAgregar = new javax.swing.JCheckBox();
        chkEngraseAgregar = new javax.swing.JCheckBox();
        txtPasswordUsuAgregar = new javax.swing.JPasswordField();
        dlgEditarUsuario = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lblIdUsuario = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtNombreUsuEditar = new javax.swing.JTextField();
        cmbEstatusEditar = new javax.swing.JComboBox<>();
        btnGuardarEditar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNombreEmpEditar = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        chkContabilidadEditar = new javax.swing.JCheckBox();
        chkCrossEditar = new javax.swing.JCheckBox();
        chkProduccionEditar = new javax.swing.JCheckBox();
        chkSemiterminadoEditar = new javax.swing.JCheckBox();
        chkSistemasEditar = new javax.swing.JCheckBox();
        chkTerminadoEditar = new javax.swing.JCheckBox();
        chkRiveraEditar = new javax.swing.JCheckBox();
        chkEngraseEditar = new javax.swing.JCheckBox();
        txtPasswordUsuEditar = new javax.swing.JPasswordField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnAgregarUsuario = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();

        dlgAgregarUsuario.setBackground(new java.awt.Color(255, 255, 255));
        dlgAgregarUsuario.setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/user_add.png"))); // NOI18N
        jLabel2.setText("Agregar Usuario");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(208, 208, 208))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Usuario:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Estatus:");

        txtNombreUsuAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cmbEstatusAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbEstatusAgregar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btnGuardarAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarAgregar.setText("Guardar");
        btnGuardarAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarAgregarActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Contraseña:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Nombre Empleado:");

        txtNombreEmpAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Roles"));

        chkContabilidadAgregar.setText("Contabilidad");

        chkCrossAgregar.setText("Desvenado");

        chkProduccionAgregar.setText("Producción");

        chkSemiterminadoAgregar.setText("Semiterminado");

        chkSistemasAgregar.setText("Sistemas");

        chkTerminadoAgregar.setText("Terminado");

        chkRiveraAgregar.setText("Rivera");

        chkEngraseAgregar.setText("Engrase");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(chkContabilidadAgregar)
                        .addGap(10, 10, 10)
                        .addComponent(chkCrossAgregar))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkProduccionAgregar)
                            .addComponent(chkSistemasAgregar)
                            .addComponent(chkRiveraAgregar))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkEngraseAgregar)
                            .addComponent(chkSemiterminadoAgregar)
                            .addComponent(chkTerminadoAgregar))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkContabilidadAgregar)
                    .addComponent(chkCrossAgregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkProduccionAgregar)
                    .addComponent(chkSemiterminadoAgregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkSistemasAgregar)
                    .addComponent(chkTerminadoAgregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkRiveraAgregar)
                    .addComponent(chkEngraseAgregar))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGuardarAgregar)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPasswordUsuAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtNombreUsuAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtNombreEmpAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbEstatusAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNombreUsuAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtPasswordUsuAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtNombreEmpAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmbEstatusAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnGuardarAgregar)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgAgregarUsuarioLayout = new javax.swing.GroupLayout(dlgAgregarUsuario.getContentPane());
        dlgAgregarUsuario.getContentPane().setLayout(dlgAgregarUsuarioLayout);
        dlgAgregarUsuarioLayout.setHorizontalGroup(
            dlgAgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgAgregarUsuarioLayout.setVerticalGroup(
            dlgAgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarUsuarioLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dlgEditarUsuario.setBackground(new java.awt.Color(255, 255, 255));
        dlgEditarUsuario.setResizable(false);

        jPanel7.setBackground(new java.awt.Color(0, 204, 204));

        jLabel10.setBackground(new java.awt.Color(0, 204, 204));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/user_edit.png"))); // NOI18N
        jLabel10.setText("Editar Usuario");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(208, 208, 208))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        lblIdUsuario.setText("idUsuario");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Usuario:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Estatus:");

        txtNombreUsuEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombreUsuEditar.setEnabled(false);

        cmbEstatusEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbEstatusEditar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btnGuardarEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarEditar.setText("Guardar Cambios");
        btnGuardarEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEditarActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Contraseña:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Nombre Empleado:");

        txtNombreEmpEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Roles"));

        chkContabilidadEditar.setText("Contabilidad");

        chkCrossEditar.setText("Desvenado");

        chkProduccionEditar.setText("Producción");

        chkSemiterminadoEditar.setText("Semiterminado");

        chkSistemasEditar.setText("Sistemas");

        chkTerminadoEditar.setText("Terminado");

        chkRiveraEditar.setText("Rivera");

        chkEngraseEditar.setText("Engrase");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(chkContabilidadEditar)
                        .addGap(10, 10, 10)
                        .addComponent(chkCrossEditar))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkProduccionEditar)
                            .addComponent(chkSistemasEditar)
                            .addComponent(chkRiveraEditar))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkEngraseEditar)
                            .addComponent(chkSemiterminadoEditar)
                            .addComponent(chkTerminadoEditar))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkContabilidadEditar)
                    .addComponent(chkCrossEditar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkProduccionEditar)
                    .addComponent(chkSemiterminadoEditar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkSistemasEditar)
                    .addComponent(chkTerminadoEditar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkRiveraEditar)
                    .addComponent(chkEngraseEditar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(lblIdUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardarEditar))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPasswordUsuEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombreUsuEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNombreEmpEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbEstatusEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtNombreUsuEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtPasswordUsuEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtNombreEmpEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbEstatusEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)))
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarEditar)
                    .addComponent(lblIdUsuario))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlgEditarUsuarioLayout = new javax.swing.GroupLayout(dlgEditarUsuario.getContentPane());
        dlgEditarUsuario.getContentPane().setLayout(dlgEditarUsuarioLayout);
        dlgEditarUsuarioLayout.setHorizontalGroup(
            dlgEditarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEditarUsuarioLayout.setVerticalGroup(
            dlgEditarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEditarUsuarioLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        tblUsuarios.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblUsuarios.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblUsuarios);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnAgregarUsuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAgregarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/user_add.png"))); // NOI18N
        btnAgregarUsuario.setText("Agregar Usuario");
        btnAgregarUsuario.setFocusable(false);
        btnAgregarUsuario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAgregarUsuario.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAgregarUsuario.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarUsuarioActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAgregarUsuario);

        jLabel1.setText("   ");
        jToolBar1.add(jLabel1);

        btnEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/user_edit.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.setFocusable(false);
        btnEditar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditar);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarUsuarioActionPerformed
        abrirDialogoAgregar();
    }//GEN-LAST:event_btnAgregarUsuarioActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        try
        {
            u = new Usuario();
            rxuc = new RolesXUsuarioCommands();
            
            chkContabilidadEditar.setSelected(false);
            chkCrossEditar.setSelected(false);
            chkProduccionEditar.setSelected(false);
            chkSemiterminadoEditar.setSelected(false);
            chkSistemasEditar.setSelected(false);
            chkTerminadoEditar.setSelected(false);
            
            u.setUserName(tblUsuarios.getValueAt(tblUsuarios.getSelectedRow(), 0).toString());
            u.setNombre(tblUsuarios.getValueAt(tblUsuarios.getSelectedRow(), 1).toString());
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de usuarios","Advertencia",JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try 
        {
            u = uc.obtenerUsuarioXNombre(u);
            
            txtNombreUsuEditar.setText(u.getUserName());
            txtPasswordUsuEditar.setText(u.getPassword());
            txtNombreEmpEditar.setText(u.getNombre());
            lblIdUsuario.setText(String.valueOf(u.getIdUsuario()));
            lblIdUsuario.setVisible(false);
            if (u.getEstatus()== 1)
            {
                cmbEstatusEditar.setSelectedIndex(0);
            }
            else
            {
                cmbEstatusEditar.setSelectedIndex(1);
            }
            
            String[] listaRoles = rxuc.obtenerRolXUsuario(u);
            
            if (listaRoles != null)
            {
                for (int i = 0; i < listaRoles.length; i++)
                {
                    if (listaRoles[i].equals("Contabilidad"))
                    {
                        chkContabilidadEditar.setSelected(true);
                        break;
                    }
                }

                for (int i = 0; i < listaRoles.length; i++)
                {
                    if (listaRoles[i].equals("Cross"))
                    {
                        chkCrossEditar.setSelected(true);
                        break;
                    }
                }

                for (int i = 0; i < listaRoles.length; i++)
                {
                    if (listaRoles[i].equals("Produccion"))
                    {
                        chkProduccionEditar.setSelected(true);
                        break;
                    }
                }

                for (int i = 0; i < listaRoles.length; i++)
                {
                    if (listaRoles[i].equals("Semiterminado"))
                    {
                        chkSemiterminadoEditar.setSelected(true);
                        break;
                    }
                }

                for (int i = 0; i < listaRoles.length; i++)
                {
                    if (listaRoles[i].equals("Sistemas"))
                    {
                        chkSistemasEditar.setSelected(true);
                        break;
                    }
                }

                for (int i = 0; i < listaRoles.length; i++)
                {
                    if (listaRoles[i].equals("Terminado"))
                    {
                        chkTerminadoEditar.setSelected(true);
                        break;
                    }
                }
            }
            
            abrirDialogoEditar();
        }  
        catch (Exception e) 
        {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnGuardarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAgregarActionPerformed
        agregarUsuario();
    }//GEN-LAST:event_btnGuardarAgregarActionPerformed

    private void btnGuardarEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEditarActionPerformed
       editarUsuario();
    }//GEN-LAST:event_btnGuardarEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarUsuario;
    public javax.swing.JButton btnEditar;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardarAgregar;
    private javax.swing.JButton btnGuardarEditar;
    private javax.swing.JCheckBox chkContabilidadAgregar;
    private javax.swing.JCheckBox chkContabilidadEditar;
    private javax.swing.JCheckBox chkCrossAgregar;
    private javax.swing.JCheckBox chkCrossEditar;
    private javax.swing.JCheckBox chkEngraseAgregar;
    private javax.swing.JCheckBox chkEngraseEditar;
    private javax.swing.JCheckBox chkProduccionAgregar;
    private javax.swing.JCheckBox chkProduccionEditar;
    private javax.swing.JCheckBox chkRiveraAgregar;
    private javax.swing.JCheckBox chkRiveraEditar;
    private javax.swing.JCheckBox chkSemiterminadoAgregar;
    private javax.swing.JCheckBox chkSemiterminadoEditar;
    private javax.swing.JCheckBox chkSistemasAgregar;
    private javax.swing.JCheckBox chkSistemasEditar;
    private javax.swing.JCheckBox chkTerminadoAgregar;
    private javax.swing.JCheckBox chkTerminadoEditar;
    private javax.swing.JComboBox<String> cmbEstatusAgregar;
    private javax.swing.JComboBox<String> cmbEstatusEditar;
    private javax.swing.JDialog dlgAgregarUsuario;
    private javax.swing.JDialog dlgEditarUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblIdUsuario;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTextField txtNombreEmpAgregar;
    private javax.swing.JTextField txtNombreEmpEditar;
    private javax.swing.JTextField txtNombreUsuAgregar;
    private javax.swing.JTextField txtNombreUsuEditar;
    private javax.swing.JPasswordField txtPasswordUsuAgregar;
    private javax.swing.JPasswordField txtPasswordUsuEditar;
    // End of variables declaration//GEN-END:variables
}
