/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.CalibreCommands;
import Controlador.ConexionBD;
import Modelo.Calibre;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mingo
 */
public class PnlCalibres extends javax.swing.JPanel {
    ConexionBD conexion;
    Calibre c;
    CalibreCommands cc;
    
    String[][] datos;
    DefaultTableModel dtm;
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols_tblCalibres = new String[]
    {
        "Calibre" , "Estatus"
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlCalibres() throws Exception {
        initComponents();
        inicializar();
    }
    
    
    //Método que se indica al inicializar el formulario
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        actualizarTablaCalibres();
    }
    
    //Método para actualizar la tabla de calibres
    private void actualizarTablaCalibres() 
    {         
        try 
        {   
            cc = new CalibreCommands();
            datos = cc.obtenerTodosCalibres();
            
            dtm = new DefaultTableModel(datos, cols_tblCalibres){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblCalibres.setModel(dtm);
            tblCalibres.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Método para agregar un calibre a BD
    private void agregarCalibre()
    {
        try 
        {
            c = new Calibre();
            cc = new CalibreCommands();
            
            //Elimina espacios, tabuladores y retornos delante.
            String nombre = txtNombreCalAgregar.getText().replaceAll("^\\s*","");
            c.setDescripcion(nombre);
            
            //Validar que el nombre del tambor no este vacío
            if (c.getDescripcion().equals(""))
            {   
                dlgAgregarCalibre.setVisible(false);
                JOptionPane.showMessageDialog(null, "Ingrese un calibre","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarCalibre.setVisible(true);
                return;
            }
            
            //Validar que el nombre del calibre no sea mayor a 15 caracteres
            if (c.getDescripcion().length() > 15)
            {   
                dlgAgregarCalibre.setVisible(false);
                JOptionPane.showMessageDialog(null, "Solo se admiten 15 caracteres \npara el nombre del calibre","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarCalibre.setVisible(true);
                return;
            }
            
            c = cc.obtenerCalibreXNombre(c);
            
            //Valida que no haya un tambor registrado con los mismos datos en BD
            if (c.getIdCalibre()== 0) 
            {
                c.setDescripcion(nombre);
                if (cmbEstatusAgregar.getSelectedItem().toString().equals("Activo"))
                {
                    c.setEstatus(1);
                }
                else
                {
                    c.setEstatus(0);
                }
                cc.insertarCalibre(c);
                dlgAgregarCalibre.setVisible(false);
                JOptionPane.showMessageDialog(null, "Calibre registrado correctamente");
                actualizarTablaCalibres();
            }
            else
            {
                dlgAgregarCalibre.setVisible(false);
                JOptionPane.showMessageDialog(null, "Ya existe un calibre con el nombre capturado", "Mensaje", JOptionPane.WARNING_MESSAGE);
                dlgAgregarCalibre.setVisible(true);
            }
        } 
        catch (Exception e) 
        {   
            System.err.println(e);
            dlgAgregarCalibre.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al insertar Calibre", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarCalibre.setVisible(true);
        }
    }
    
    //Método para editar un calibre en BD
    private void editarCalibre()
    {
        dlgEditarCalibre.setVisible(false);
        if (JOptionPane.showConfirmDialog(null, "¿Realmente desea guardar las modificaciones?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
        {
            try
            {
                c = new Calibre();
                cc = new CalibreCommands();

                //Elimina espacios, tabuladores y retornos delante.
                String nombre = txtNombreCalEditar.getText().replaceAll("^\\s*","");
                c.setDescripcion(nombre);

                //Validar que el nombre del calibre no este vacío
                if (c.getDescripcion().equals(""))
                {   
                    dlgEditarCalibre.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ingrese un nombre de calibre","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgEditarCalibre.setVisible(true);
                    return;
                }

                //Validar que el nombre del calibre no sea mayor a 15 caracteres
                if (c.getDescripcion().length() > 15)
                {   
                    dlgEditarCalibre.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Solo se admiten 15 caracteres \npara el nombre del calibre","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgEditarCalibre.setVisible(true);
                    return;
                }

                c = cc.obtenerCalibreXNombre(c);

                //Valida que no haya un tambor resgistrado con los mismos datos en BD
                if (c.getIdCalibre()== 0 || c.getIdCalibre()== Integer.parseInt(lblIdCalibre.getText()))
                {
                    c.setIdCalibre(Integer.parseInt(lblIdCalibre.getText()));
                    c.setDescripcion(nombre);
                    if (cmbEstatusEditar.getSelectedItem().toString().equals("Activo"))
                    {
                        c.setEstatus(1);
                    }
                    else
                    {
                        c.setEstatus(0);
                    }
                    cc.actualizarCalibre(c);
                    dlgEditarCalibre.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Calibre actualizado correctamente");
                    actualizarTablaCalibres();
                }
                else
                {
                    dlgEditarCalibre.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ya existe un calibre con el nombre capturado", "Mensaje", JOptionPane.WARNING_MESSAGE);
                    dlgEditarCalibre.setVisible(true);
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
                dlgEditarCalibre.setVisible(false);
                JOptionPane.showMessageDialog(null, "Error al actualizar calibre", "Error", JOptionPane.ERROR_MESSAGE);
                dlgEditarCalibre.setVisible(true);
            }
        }
        else
            dlgEditarCalibre.setVisible(true);
    }
   
    //Método que abre el dialogo para agregar un tambor
    public void abrirDialogoAgregar()
    {   try 
        {
            dlgAgregarCalibre.setSize(410, 210);
            dlgAgregarCalibre.setPreferredSize(dlgAgregarCalibre.getSize());
            dlgAgregarCalibre.setLocationRelativeTo(null);
            dlgAgregarCalibre.setModal(true);
            dlgAgregarCalibre.setVisible(true);

            txtNombreCalAgregar.setText("");
            cmbEstatusAgregar.setSelectedIndex(0);
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgAgregarCalibre.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarCalibre.setVisible(true);
        }
    }
    
    //Método que abre el dialogo para editar un tambor
    public void abrirDialogoEditar()
    {   try 
        {
            dlgEditarCalibre.setSize(410, 210);
            dlgEditarCalibre.setPreferredSize(dlgEditarCalibre.getSize());
            dlgEditarCalibre.setLocationRelativeTo(null);
            dlgEditarCalibre.setModal(true);
            dlgEditarCalibre.setVisible(true);
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgEditarCalibre.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgEditarCalibre.setVisible(true);
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
        dlgAgregarCalibre = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNombreCalAgregar = new javax.swing.JTextField();
        btnGuardarAgregar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        cmbEstatusAgregar = new javax.swing.JComboBox<>();
        dlgEditarCalibre = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtNombreCalEditar = new javax.swing.JTextField();
        btnGuardarEditar = new javax.swing.JButton();
        lblIdCalibre = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbEstatusEditar = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCalibres = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnAgregarProveedor = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();

        dlgAgregarCalibre.setBackground(new java.awt.Color(255, 255, 255));
        dlgAgregarCalibre.setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ruler.png"))); // NOI18N
        jLabel2.setText("Agregar Calibre");

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
        jLabel3.setText("Nombre Calibre:");

        txtNombreCalAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnGuardarAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarAgregar.setText("Guardar");
        btnGuardarAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarAgregarActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Estatus:");

        cmbEstatusAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbEstatusAgregar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGuardarAgregar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbEstatusAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreCalAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNombreCalAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEstatusAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(btnGuardarAgregar)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgAgregarCalibreLayout = new javax.swing.GroupLayout(dlgAgregarCalibre.getContentPane());
        dlgAgregarCalibre.getContentPane().setLayout(dlgAgregarCalibreLayout);
        dlgAgregarCalibreLayout.setHorizontalGroup(
            dlgAgregarCalibreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgAgregarCalibreLayout.setVerticalGroup(
            dlgAgregarCalibreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarCalibreLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dlgEditarCalibre.setBackground(new java.awt.Color(255, 255, 255));
        dlgEditarCalibre.setResizable(false);

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ruler.png"))); // NOI18N
        jLabel5.setText("Editar Calibre");

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
        jLabel6.setText("Nombre Calibre:");

        txtNombreCalEditar.setEditable(false);
        txtNombreCalEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnGuardarEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarEditar.setText("Guardar cambios");
        btnGuardarEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEditarActionPerformed(evt);
            }
        });

        lblIdCalibre.setText("idCalibre");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Estatus:");

        cmbEstatusEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbEstatusEditar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNombreCalEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(lblIdCalibre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardarEditar)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbEstatusEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtNombreCalEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEstatusEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardarEditar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblIdCalibre, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEditarCalibreLayout = new javax.swing.GroupLayout(dlgEditarCalibre.getContentPane());
        dlgEditarCalibre.getContentPane().setLayout(dlgEditarCalibreLayout);
        dlgEditarCalibreLayout.setHorizontalGroup(
            dlgEditarCalibreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEditarCalibreLayout.setVerticalGroup(
            dlgEditarCalibreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEditarCalibreLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        tblCalibres.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblCalibres.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCalibres.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblCalibres);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnAgregarProveedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAgregarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
        btnAgregarProveedor.setText("Agregar Calibre");
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
            c = new Calibre();
            c.setDescripcion(tblCalibres.getValueAt(tblCalibres.getSelectedRow(), 0).toString());
            if (tblCalibres.getValueAt(tblCalibres.getSelectedRow(), 1).toString().equals("Activo"))
            {
                c.setEstatus(1);
            }
            else
            {
                c.setEstatus(0);
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de calibres","Advertencia",JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try 
        {
            c = cc.obtenerCalibreXNombre(c);
            
            txtNombreCalEditar.setText(c.getDescripcion());
            lblIdCalibre.setText(String.valueOf(c.getIdCalibre()));
            lblIdCalibre.setVisible(false);
            abrirDialogoEditar();
        }  
        catch (Exception e) 
        {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnGuardarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAgregarActionPerformed
        agregarCalibre();
    }//GEN-LAST:event_btnGuardarAgregarActionPerformed

    private void btnGuardarEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEditarActionPerformed
        editarCalibre();
    }//GEN-LAST:event_btnGuardarEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarProveedor;
    public javax.swing.JButton btnEditar;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardarAgregar;
    private javax.swing.JButton btnGuardarEditar;
    private javax.swing.JComboBox<String> cmbEstatusAgregar;
    private javax.swing.JComboBox<String> cmbEstatusEditar;
    private javax.swing.JDialog dlgAgregarCalibre;
    private javax.swing.JDialog dlgEditarCalibre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblIdCalibre;
    private javax.swing.JTable tblCalibres;
    private javax.swing.JTextField txtNombreCalAgregar;
    private javax.swing.JTextField txtNombreCalEditar;
    // End of variables declaration//GEN-END:variables
}
