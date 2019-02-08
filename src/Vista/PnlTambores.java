/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.ConexionBD;
import Controlador.TamborCommands;
import Modelo.Tambor;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mingo
 */
public class PnlTambores extends javax.swing.JPanel {
    ConexionBD conexion;
    Tambor t;
    TamborCommands tc;
    
    String[][] datos;
    DefaultTableModel dtm;
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols_tblTambores = new String[]
    {
        "Tambor","Estatus"
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlTambores() throws Exception {
        initComponents();
        inicializar();
    }
    
    
    //Método que se indica al inicializar el formulario
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        actualizarTablaTambores();
    }
    
    //Método para actualizar la tabla de proveedores
    private void actualizarTablaTambores() 
    {         
        try 
        {   
            tc = new TamborCommands();
            datos = tc.obtenerTambores();
            
            dtm = new DefaultTableModel(datos, cols_tblTambores){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblTambores.setModel(dtm);
            tblTambores.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Método para agregar un tambor a BD
    private void agregarTambor()
    {
        try 
        {
            t = new Tambor();
            tc = new TamborCommands();
            
            //Elimina espacios, tabuladores y retornos delante.
            String nombre = txtNombreTambAgregar.getText().replaceAll("^\\s*","");
            t.setNombreTambor(nombre);
            
            //Validar que el nombre del tambor no este vacío
            if (t.getNombreTambor().equals(""))
            {   
                dlgAgregarTambor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Ingrese un nombre de tambor","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarTambor.setVisible(true);
                return;
            }
            
            //Validar que el nombre del tambor no sea mayor a 100 caracteres
            if (t.getNombreTambor().length() > 100)
            {   
                dlgAgregarTambor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Solo se admiten 100 caracteres \npara el nombre del tambor","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarTambor.setVisible(true);
                return;
            }
            
            t = tc.obtenerTamborXNombre(t);
            
            //Valida que no haya un tambor registrado con los mismos datos en BD
            if (t.getIdTambor()== 0) 
            {
                t.setNombreTambor(nombre);
                if (cmbEstatusAgregar.getSelectedItem().toString().equals("Activo"))
                {
                    t.setEstatus(1);
                }
                else
                {
                    t.setEstatus(0);
                }
                tc.insertarTambor(t);
                dlgAgregarTambor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Tambor registrado correctamente");
                actualizarTablaTambores();
            }
            else
            {
                dlgAgregarTambor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Ya existe un tambor con el nombre capturado", "Mensaje", JOptionPane.WARNING_MESSAGE);
                dlgAgregarTambor.setVisible(true);
            }
        } 
        catch (Exception e) 
        {   
            System.err.println(e);
            dlgAgregarTambor.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al insertar Tambor", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarTambor.setVisible(true);
        }
    }
    
    //Método para editar un tambor en BD
    private void editarTambor()
    {
        dlgEditarTambor.setVisible(false);
        if (JOptionPane.showConfirmDialog(null, "¿Realmente desea guardar las modificaciones?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
        {
            try
            {
                t = new Tambor();
                tc = new TamborCommands();

                //Elimina espacios, tabuladores y retornos delante.
                String nombre = txtNombreTambEditar.getText().replaceAll("^\\s*","");
                t.setNombreTambor(nombre);

                //Validar que el nombre del tambor no este vacío
                if (t.getNombreTambor().equals(""))
                {   
                    dlgEditarTambor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ingrese un nombre de tambor","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgEditarTambor.setVisible(true);
                    return;
                }

                //Validar que el nombre del tambor no sea mayor a 100 caracteres
                if (t.getNombreTambor().length() > 100)
                {   
                    dlgEditarTambor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Solo se admiten 100 caracteres \npara el nombre del tambor","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgEditarTambor.setVisible(true);
                    return;
                }

                t = tc.obtenerTamborXNombre(t);

                //Valida que no haya un tambor resgistrado con los mismos datos en BD
                if (t.getIdTambor() == 0 || t.getIdTambor() == Integer.parseInt(lblIdTambor.getText()))
                {
                    t.setIdTambor(Integer.parseInt(lblIdTambor.getText()));
                    t.setNombreTambor(nombre);
                    if (cmbEstatusEditar.getSelectedItem().toString().equals("Activo"))
                    {
                        t.setEstatus(1);
                    }
                    else
                    {
                        t.setEstatus(0);
                    }
                    tc.actualizarTambor(t);
                    dlgEditarTambor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Tambor actualizado correctamente");
                    actualizarTablaTambores();
                }
                else
                {
                    dlgEditarTambor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ya existe un tambor con el nombre capturado", "Mensaje", JOptionPane.WARNING_MESSAGE);
                    dlgEditarTambor.setVisible(true);
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
                dlgEditarTambor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Error al actualizar tambor", "Error", JOptionPane.ERROR_MESSAGE);
                dlgEditarTambor.setVisible(true);
            }
        }
        else
            dlgEditarTambor.setVisible(true);
    }
   
    //Método que abre el dialogo para agregar un tambor
    public void abrirDialogoAgregar()
    {   try 
        {
            dlgAgregarTambor.setSize(410, 210);
            dlgAgregarTambor.setPreferredSize(dlgAgregarTambor.getSize());
            dlgAgregarTambor.setLocationRelativeTo(null);
            dlgAgregarTambor.setModal(true);
            dlgAgregarTambor.setVisible(true);

            txtNombreTambAgregar.setText("");
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgAgregarTambor.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarTambor.setVisible(true);
        }
    }
    
    //Método que abre el dialogo para editar un tambor
    public void abrirDialogoEditar()
    {   try 
        {
            dlgEditarTambor.setSize(410, 210);
            dlgEditarTambor.setPreferredSize(dlgEditarTambor.getSize());
            dlgEditarTambor.setLocationRelativeTo(null);
            dlgEditarTambor.setModal(true);
            dlgEditarTambor.setVisible(true);
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgEditarTambor.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgEditarTambor.setVisible(true);
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
        dlgAgregarTambor = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombreTambAgregar = new javax.swing.JTextField();
        cmbEstatusAgregar = new javax.swing.JComboBox<>();
        btnGuardarAgregar = new javax.swing.JButton();
        dlgEditarTambor = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNombreTambEditar = new javax.swing.JTextField();
        cmbEstatusEditar = new javax.swing.JComboBox<>();
        btnGuardarEditar = new javax.swing.JButton();
        lblIdTambor = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTambores = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnAgregarProveedor = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();

        dlgAgregarTambor.setBackground(new java.awt.Color(255, 255, 255));
        dlgAgregarTambor.setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/group.png"))); // NOI18N
        jLabel2.setText("Agregar Tambor");

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
        jLabel3.setText("Nombre Tambor:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Estatus:");

        txtNombreTambAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

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
                    .addComponent(txtNombreTambAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(txtNombreTambAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbEstatusAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(btnGuardarAgregar)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgAgregarTamborLayout = new javax.swing.GroupLayout(dlgAgregarTambor.getContentPane());
        dlgAgregarTambor.getContentPane().setLayout(dlgAgregarTamborLayout);
        dlgAgregarTamborLayout.setHorizontalGroup(
            dlgAgregarTamborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgAgregarTamborLayout.setVerticalGroup(
            dlgAgregarTamborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarTamborLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dlgEditarTambor.setBackground(new java.awt.Color(255, 255, 255));
        dlgEditarTambor.setResizable(false);

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/group.png"))); // NOI18N
        jLabel5.setText("Editar Tambor");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(208, 208, 208))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Nombre Tambor:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Estatus:");

        txtNombreTambEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombreTambEditar.setEnabled(false);

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

        lblIdTambor.setText("idTambor");

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
                            .addComponent(txtNombreTambEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbEstatusEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(lblIdTambor)
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
                    .addComponent(txtNombreTambEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(cmbEstatusEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(btnGuardarEditar))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblIdTambor)))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEditarTamborLayout = new javax.swing.GroupLayout(dlgEditarTambor.getContentPane());
        dlgEditarTambor.getContentPane().setLayout(dlgEditarTamborLayout);
        dlgEditarTamborLayout.setHorizontalGroup(
            dlgEditarTamborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEditarTamborLayout.setVerticalGroup(
            dlgEditarTamborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEditarTamborLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        tblTambores.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblTambores.setModel(new javax.swing.table.DefaultTableModel(
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
        tblTambores.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblTambores);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnAgregarProveedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAgregarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
        btnAgregarProveedor.setText("Agregar Tambor");
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
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pencil.png"))); // NOI18N
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
            t = new Tambor();
            t.setNombreTambor(tblTambores.getValueAt(tblTambores.getSelectedRow(), 0).toString());
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de tambores","Advertencia",JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try 
        {
            t = tc.obtenerTamborXNombre(t);
            
            txtNombreTambEditar.setText(t.getNombreTambor());
            lblIdTambor.setText(String.valueOf(t.getIdTambor()));
            lblIdTambor.setVisible(false);
            if (t.getEstatus()== 1)
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
        agregarTambor();
    }//GEN-LAST:event_btnGuardarAgregarActionPerformed

    private void btnGuardarEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEditarActionPerformed
        editarTambor();
    }//GEN-LAST:event_btnGuardarEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarProveedor;
    public javax.swing.JButton btnEditar;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardarAgregar;
    private javax.swing.JButton btnGuardarEditar;
    private javax.swing.JComboBox<String> cmbEstatusAgregar;
    private javax.swing.JComboBox<String> cmbEstatusEditar;
    private javax.swing.JDialog dlgAgregarTambor;
    private javax.swing.JDialog dlgEditarTambor;
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
    private javax.swing.JLabel lblIdTambor;
    private javax.swing.JTable tblTambores;
    private javax.swing.JTextField txtNombreTambAgregar;
    private javax.swing.JTextField txtNombreTambEditar;
    // End of variables declaration//GEN-END:variables
}
