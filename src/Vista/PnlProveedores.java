/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.ConexionBD;
import Controlador.ProveedorCommands;
import Modelo.Proveedor;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mingo
 */
public class PnlProveedores extends javax.swing.JPanel {
    ConexionBD conexion;
    Proveedor p;
    ProveedorCommands pc;
    
    String[][] datos;
    DefaultTableModel dtm;
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols_tblProveedores = new String[]
    {
        "Provedor","Estatus"
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlProveedores() throws Exception {
        initComponents();
        inicializar();
    }
    
    
    //Método que se indica al inicializar el formulario
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        actualizarTablaProveedores();
    }
    
    //Método para actualizar la tabla de proveedores
    private void actualizarTablaProveedores() 
    {         
        try 
        {   
            pc = new ProveedorCommands();
            datos = pc.obtenerProveedores();
            
            dtm = new DefaultTableModel(datos, cols_tblProveedores){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblProveedores.setModel(dtm);
            tblProveedores.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Método para agregar un proveedor a BD
    private void agregarProveedor()
    {
        try 
        {
            p = new Proveedor();
            pc = new ProveedorCommands();
            
            //Elimina espacios, tabuladores y retornos delante.
            String nombre = txtNombreProvAgregar.getText().replaceAll("^\\s*","");
            p.setNombreProveedor(nombre);
            
            //Validar que el nombre del proveedor no este vacío
            if (p.getNombreProveedor().equals(""))
            {   
                dlgAgregarProveedor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Ingrese un nombre de proveedor","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarProveedor.setVisible(true);
                return;
            }
            
            //Validar que el nombre del proveedor no sea mayor a 100 caracteres
            if (p.getNombreProveedor().length() > 100)
            {   
                dlgAgregarProveedor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Solo se admiten 100 caracteres \npara el nombre del proveedor","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarProveedor.setVisible(true);
                return;
            }
            
            p = pc.obtenerProveedorXNombre(p);
            
            //Valida que no haya un proveedor resgistrado con los mismos datos en BD
            if (p.getIdProveedor() == 0) 
            {
                p.setNombreProveedor(nombre);
                if (cmbEstatusAgregar.getSelectedItem().toString().equals("Activo"))
                {
                    p.setEstatus(1);
                }
                else
                {
                    p.setEstatus(0);
                }
                pc.insertarProveedor(p);
                dlgAgregarProveedor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Proveedor registrado correctamente");
                actualizarTablaProveedores();
            }
            else
            {
                dlgAgregarProveedor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Ya existe un proveedor con el nombre capturado", "Mensaje", JOptionPane.WARNING_MESSAGE);
                dlgAgregarProveedor.setVisible(true);
            }
        } 
        catch (Exception e) 
        {   
            System.err.println(e);
            dlgAgregarProveedor.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al insertar Proveedor", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarProveedor.setVisible(true);
        }
    }
    
    //Método para editar un proveedor en BD
    private void editarProveedor()
    {
        dlgEditarProveedor.setVisible(false);
        if (JOptionPane.showConfirmDialog(null, "¿Realmente desea guardar las modificaciones?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
        {
            try
            {
                p = new Proveedor();
                pc = new ProveedorCommands();

                //Elimina espacios, tabuladores y retornos delante.
                String nombre = txtNombreProvEditar.getText().replaceAll("^\\s*","");
                p.setNombreProveedor(nombre);

                //Validar que el nombre del proveedor no este vacío
                if (p.getNombreProveedor().equals(""))
                {   
                    dlgEditarProveedor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ingrese un nombre de proveedor","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgEditarProveedor.setVisible(true);
                    return;
                }

                //Validar que el nombre del proveedor no sea mayor a 100 caracteres
                if (p.getNombreProveedor().length() > 100)
                {   
                    dlgEditarProveedor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Solo se admiten 100 caracteres \npara el nombre del proveedor","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgEditarProveedor.setVisible(true);
                    return;
                }

                p = pc.obtenerProveedorXNombre(p);

                //Valida que no haya un proveedor resgistrado con los mismos datos en BD
                if (p.getIdProveedor() == 0 || p.getIdProveedor() == Integer.parseInt(lblIdProveedor.getText()))
                {
                    p.setIdProveedor(Integer.parseInt(lblIdProveedor.getText()));
                    p.setNombreProveedor(nombre);
                    if (cmbEstatusEditar.getSelectedItem().toString().equals("Activo"))
                    {
                        p.setEstatus(1);
                    }
                    else
                    {
                        p.setEstatus(0);
                    }
                    pc.actualizarProveedor(p);
                    dlgEditarProveedor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Proveedor actualizado correctamente");
                    actualizarTablaProveedores();
                }
                else
                {
                    dlgEditarProveedor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ya existe un proveedor con el nombre capturado", "Mensaje", JOptionPane.WARNING_MESSAGE);
                    dlgEditarProveedor.setVisible(true);
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
                dlgEditarProveedor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Error al actualizar Proveedor", "Error", JOptionPane.ERROR_MESSAGE);
                dlgEditarProveedor.setVisible(true);
            }
        }
        else
            dlgEditarProveedor.setVisible(true);
    }
   
    //Método que abre el dialogo para agregar un proveedor
    public void abrirDialogoAgregar()
    {   try 
        {
            dlgAgregarProveedor.setSize(410, 210);
            dlgAgregarProveedor.setPreferredSize(dlgAgregarProveedor.getSize());
            dlgAgregarProveedor.setLocationRelativeTo(null);
            dlgAgregarProveedor.setModal(true);
            dlgAgregarProveedor.setVisible(true);

            txtNombreProvAgregar.setText("");
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgAgregarProveedor.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarProveedor.setVisible(true);
        }
    }
    
    //Método que abre el dialogo para editar un proveedor
    public void abrirDialogoEditar()
    {   try 
        {
            dlgEditarProveedor.setSize(410, 210);
            dlgEditarProveedor.setPreferredSize(dlgEditarProveedor.getSize());
            dlgEditarProveedor.setLocationRelativeTo(null);
            dlgEditarProveedor.setModal(true);
            dlgEditarProveedor.setVisible(true);
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgEditarProveedor.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgEditarProveedor.setVisible(true);
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
        dlgAgregarProveedor = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombreProvAgregar = new javax.swing.JTextField();
        cmbEstatusAgregar = new javax.swing.JComboBox<>();
        btnGuardarAgregar = new javax.swing.JButton();
        dlgEditarProveedor = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNombreProvEditar = new javax.swing.JTextField();
        cmbEstatusEditar = new javax.swing.JComboBox<>();
        btnGuardarEditar = new javax.swing.JButton();
        lblIdProveedor = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProveedores = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnAgregarProveedor = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();

        dlgAgregarProveedor.setBackground(new java.awt.Color(255, 255, 255));
        dlgAgregarProveedor.setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/user_add.png"))); // NOI18N
        jLabel2.setText("Agregar Proveedor");

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
        jLabel3.setText("Nombre Proveedor:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Estatus:");

        txtNombreProvAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreProvAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbEstatusAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardarAgregar)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNombreProvAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbEstatusAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(btnGuardarAgregar)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgAgregarProveedorLayout = new javax.swing.GroupLayout(dlgAgregarProveedor.getContentPane());
        dlgAgregarProveedor.getContentPane().setLayout(dlgAgregarProveedorLayout);
        dlgAgregarProveedorLayout.setHorizontalGroup(
            dlgAgregarProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgAgregarProveedorLayout.setVerticalGroup(
            dlgAgregarProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarProveedorLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dlgEditarProveedor.setBackground(new java.awt.Color(255, 255, 255));
        dlgEditarProveedor.setResizable(false);

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/user_edit.png"))); // NOI18N
        jLabel5.setText("Editar Proveedor");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Nombre Proveedor:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Estatus:");

        txtNombreProvEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cmbEstatusEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbEstatusEditar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btnGuardarEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarEditar.setText("Guardar cambios");
        btnGuardarEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEditarActionPerformed(evt);
            }
        });

        lblIdProveedor.setText("idProveedor");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreProvEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbEstatusEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 70, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(lblIdProveedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardarEditar)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtNombreProvEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbEstatusEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardarEditar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblIdProveedor, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEditarProveedorLayout = new javax.swing.GroupLayout(dlgEditarProveedor.getContentPane());
        dlgEditarProveedor.getContentPane().setLayout(dlgEditarProveedorLayout);
        dlgEditarProveedorLayout.setHorizontalGroup(
            dlgEditarProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEditarProveedorLayout.setVerticalGroup(
            dlgEditarProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEditarProveedorLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        tblProveedores.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblProveedores.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProveedores.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblProveedores);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnAgregarProveedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAgregarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/user_add.png"))); // NOI18N
        btnAgregarProveedor.setText("Agregar Proveedor");
        btnAgregarProveedor.setFocusable(false);
        btnAgregarProveedor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAgregarProveedor.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAgregarProveedor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProveedorActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAgregarProveedor);

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

    private void btnAgregarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProveedorActionPerformed
        abrirDialogoAgregar();
    }//GEN-LAST:event_btnAgregarProveedorActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        try
        {
            p = new Proveedor();
            p.setNombreProveedor(tblProveedores.getValueAt(tblProveedores.getSelectedRow(), 0).toString());
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de proveedores","Advertencia",JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try 
        {
            p = pc.obtenerProveedorXNombre(p);
            
            txtNombreProvEditar.setText(p.getNombreProveedor());
            lblIdProveedor.setText(String.valueOf(p.getIdProveedor()));
            lblIdProveedor.setVisible(false);
            if (p.getEstatus()== 1)
            {
                cmbEstatusEditar.setSelectedIndex(0);
            }
            else
            {
                cmbEstatusEditar.setSelectedIndex(1);
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
        agregarProveedor();
    }//GEN-LAST:event_btnGuardarAgregarActionPerformed

    private void btnGuardarEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEditarActionPerformed
        editarProveedor();
    }//GEN-LAST:event_btnGuardarEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarProveedor;
    public javax.swing.JButton btnEditar;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardarAgregar;
    private javax.swing.JButton btnGuardarEditar;
    private javax.swing.JComboBox<String> cmbEstatusAgregar;
    private javax.swing.JComboBox<String> cmbEstatusEditar;
    private javax.swing.JDialog dlgAgregarProveedor;
    private javax.swing.JDialog dlgEditarProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblIdProveedor;
    private javax.swing.JTable tblProveedores;
    private javax.swing.JTextField txtNombreProvAgregar;
    private javax.swing.JTextField txtNombreProvEditar;
    // End of variables declaration//GEN-END:variables
}
