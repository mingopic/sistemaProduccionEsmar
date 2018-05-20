/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.ConexionBD;
import Controlador.ProcesoCommands;
import Controlador.SubProcesoCommands;
import Modelo.Proceso;
import Modelo.SubProceso;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mingo
 */
public class PnlSubProcesos extends javax.swing.JPanel {
    ConexionBD conexion;
    Proceso pr;
    SubProceso spr;
    ProcesoCommands prc;
    SubProcesoCommands sprc;
    
    String[][] datos;
    DefaultTableModel dtm;
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols_tblProveedores = new String[]
    {
        "SubProceso","Proceso"
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlSubProcesos() throws Exception {
        initComponents();
        inicializar();
    }
    
    
    //Método que se indica al inicializar el formulario
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        llenarComboProcesos(0);
        actualizarTablaSubProcesos();
    }
    
    public void llenarComboProcesos(int combo) throws Exception
    {
        prc = new ProcesoCommands();
        datos = prc.llenarComboboxProcesos();
        
        int i=0;
        
        if (combo == 0)
        {
            while (i<datos.length)
            {
                cmbProcesos.addItem(datos[i][1]);
                i++;
            }
        }
        else
        {
            cmbProcesoAgregar.removeAllItems();
            cmbProcesoEditar.removeAllItems();
            while (i<datos.length)
            {
                cmbProcesoAgregar.addItem(datos[i][1]);
                cmbProcesoEditar.addItem(datos[i][1]);
                i++;
            }
        }
    }
    
    //Método para actualizar la tabla de subProcesos
    private void actualizarTablaSubProcesos() 
    {         
        try 
        {   
            sprc = new SubProcesoCommands();
            String proceso;
            
            if (cmbProcesos.getSelectedItem().toString().equals("<Todos>"))
            {
                proceso = "%%";
            }
            else
            {
                proceso = cmbProcesos.getSelectedItem().toString();
            }
            datos = sprc.obtenerSubprocesos(proceso);
            
            dtm = new DefaultTableModel(datos, cols_tblProveedores){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblSubProcesos.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Método para agregar un proveedor a BD
    private void agregarSubProceso()
    {
        try 
        {
            spr = new SubProceso();
            sprc = new SubProcesoCommands();
            
            //Elimina espacios, tabuladores y retornos delante.
            String nombre = txtSubProcesoAgregar.getText().replaceAll("^\\s*","");
            spr.setDescripcion(nombre);
            
            //Validar que el nombre del subProceso no este vacío
            if (spr.getDescripcion().equals(""))
            {   
                dlgAgregarSubProceso.setVisible(false);
                JOptionPane.showMessageDialog(null, "Ingrese un nombre de subProceso","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarSubProceso.setVisible(true);
                return;
            }
            
            //Validar que el nombre del subProceso no sea mayor a 20 caracteres
            if (spr.getDescripcion().length() > 20)
            {   
                dlgAgregarSubProceso.setVisible(false);
                JOptionPane.showMessageDialog(null, "Solo se admiten 20 caracteres \npara el nombre del subProceso","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarSubProceso.setVisible(true);
                return;
            }
            
            spr = sprc.obtSubProcXdesc(nombre);
            
            //Valida que no haya un subProceso resgistrado con los mismos datos en BD
            if (spr.getIdSubProceso() == 0) 
            {
                String proceso;
                spr.setDescripcion(nombre);
                proceso = cmbProcesoAgregar.getSelectedItem().toString();

                sprc.insertarSubProceso(spr, proceso);
                dlgAgregarSubProceso.setVisible(false);
                JOptionPane.showMessageDialog(null, "SubProceso registrado correctamente");
                actualizarTablaSubProcesos();
            }
            else
            {
                dlgAgregarSubProceso.setVisible(false);
                JOptionPane.showMessageDialog(null, "Ya existe un subProceso con el nombre capturado", "Mensaje", JOptionPane.WARNING_MESSAGE);
                dlgAgregarSubProceso.setVisible(true);
            }
        } 
        catch (Exception e) 
        {   
            System.err.println(e);
            dlgAgregarSubProceso.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al insertar subProceso", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarSubProceso.setVisible(true);
        }
    }
    
    //Método para editar un subProceso en BD
    private void editarProveedor()
    {
        dlgEditarProveedor.setVisible(false);
        if (JOptionPane.showConfirmDialog(null, "¿Realmente desea guardar las modificaciones?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
        {
            try
            {
                spr = new SubProceso();
                sprc = new SubProcesoCommands();

                //Elimina espacios, tabuladores y retornos delante.
                String nombre = txtSubProcesoEditar.getText().replaceAll("^\\s*","");
                spr.setDescripcion(nombre);

                //Validar que el nombre del subProceso no este vacío
                if (spr.getDescripcion().equals(""))
                {   
                    dlgEditarProveedor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ingrese un nombre de subProceso","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgEditarProveedor.setVisible(true);
                    return;
                }

                //Validar que el nombre del subProceso no sea mayor a 20 caracteres
                if (spr.getDescripcion().length() > 20)
                {   
                    dlgEditarProveedor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Solo se admiten 20 caracteres \npara el nombre del subProceso","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgEditarProveedor.setVisible(true);
                    return;
                }

                spr = sprc.obtSubProcXdesc(nombre);

                //Valida que no haya un subProceso resgistrado con los mismos datos en BD
                if (spr.getIdSubProceso() == 0 || spr.getIdSubProceso() == Integer.parseInt(lblIdSubProceso.getText()))
                {
                    String proceso;
                    spr.setIdSubProceso(Integer.parseInt(lblIdSubProceso.getText()));
                    spr.setDescripcion(nombre);
                    proceso = cmbProcesoEditar.getSelectedItem().toString();
                    
                    sprc.actualizarSubProceso(spr, proceso);
                    dlgEditarProveedor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "SubProceso actualizado correctamente");
                    actualizarTablaSubProcesos();
                }
                else
                {
                    dlgEditarProveedor.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ya existe un subProceso con el nombre capturado", "Mensaje", JOptionPane.WARNING_MESSAGE);
                    dlgEditarProveedor.setVisible(true);
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
                dlgEditarProveedor.setVisible(false);
                JOptionPane.showMessageDialog(null, "Error al actualizar subProceso", "Error", JOptionPane.ERROR_MESSAGE);
                dlgEditarProveedor.setVisible(true);
            }
        }
        else
            dlgEditarProveedor.setVisible(true);
    }
   
    //Método que abre el dialogo para agregar un subProceso
    public void abrirDialogoAgregar()
    {   try 
        {
            dlgAgregarSubProceso.setSize(410, 210);
            dlgAgregarSubProceso.setPreferredSize(dlgAgregarSubProceso.getSize());
            dlgAgregarSubProceso.setLocationRelativeTo(null);
            dlgAgregarSubProceso.setAlwaysOnTop(true);
            dlgAgregarSubProceso.setVisible(true);

            txtSubProcesoAgregar.setText("");
            llenarComboProcesos(1);
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgAgregarSubProceso.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarSubProceso.setVisible(true);
        }
    }
    
    //Método que abre el dialogo para editar un proveedor
    public void abrirDialogoEditar()
    {   try 
        {
            dlgEditarProveedor.setSize(410, 210);
            dlgEditarProveedor.setPreferredSize(dlgEditarProveedor.getSize());
            dlgEditarProveedor.setLocationRelativeTo(null);
            dlgEditarProveedor.setAlwaysOnTop(true);
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
        dlgAgregarSubProceso = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSubProcesoAgregar = new javax.swing.JTextField();
        cmbProcesoAgregar = new javax.swing.JComboBox<>();
        btnGuardarAgregar = new javax.swing.JButton();
        dlgEditarProveedor = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSubProcesoEditar = new javax.swing.JTextField();
        cmbProcesoEditar = new javax.swing.JComboBox<>();
        btnGuardarEditar = new javax.swing.JButton();
        lblIdSubProceso = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSubProcesos = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnAgregarSubProceso = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel9 = new javax.swing.JLabel();
        btnEditar1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        cmbProcesos = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();

        dlgAgregarSubProceso.setBackground(new java.awt.Color(255, 255, 255));
        dlgAgregarSubProceso.setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cueroProceso.png"))); // NOI18N
        jLabel2.setText("Agregar SubProceso");

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
        jLabel3.setText("Nombre SubProceso:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Proceso:");

        txtSubProcesoAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cmbProcesoAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

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
                    .addComponent(txtSubProcesoAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbProcesoAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(txtSubProcesoAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbProcesoAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(btnGuardarAgregar)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgAgregarSubProcesoLayout = new javax.swing.GroupLayout(dlgAgregarSubProceso.getContentPane());
        dlgAgregarSubProceso.getContentPane().setLayout(dlgAgregarSubProcesoLayout);
        dlgAgregarSubProcesoLayout.setHorizontalGroup(
            dlgAgregarSubProcesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgAgregarSubProcesoLayout.setVerticalGroup(
            dlgAgregarSubProcesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarSubProcesoLayout.createSequentialGroup()
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
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cueroProceso.png"))); // NOI18N
        jLabel5.setText("Editar SubProceso");

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
        jLabel6.setText("Nombre SubProceso:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Proceso:");

        txtSubProcesoEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cmbProcesoEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbProcesoEditar.setEnabled(false);

        btnGuardarEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarEditar.setText("Guardar cambios");
        btnGuardarEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEditarActionPerformed(evt);
            }
        });

        lblIdSubProceso.setText("idSubProceso");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(cmbProcesoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtSubProcesoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblIdSubProceso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardarEditar)
                        .addGap(20, 20, 20))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtSubProcesoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbProcesoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdSubProceso)
                    .addComponent(btnGuardarEditar))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEditarProveedorLayout = new javax.swing.GroupLayout(dlgEditarProveedor.getContentPane());
        dlgEditarProveedor.getContentPane().setLayout(dlgEditarProveedorLayout);
        dlgEditarProveedorLayout.setHorizontalGroup(
            dlgEditarProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEditarProveedorLayout.setVerticalGroup(
            dlgEditarProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEditarProveedorLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        tblSubProcesos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblSubProcesos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblSubProcesos);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnAgregarSubProceso.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAgregarSubProceso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
        btnAgregarSubProceso.setText("Agregar SubProceso");
        btnAgregarSubProceso.setFocusable(false);
        btnAgregarSubProceso.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAgregarSubProceso.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAgregarSubProceso.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregarSubProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarSubProcesoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAgregarSubProceso);

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

        jLabel8.setText("   ");
        jToolBar1.add(jLabel8);
        jToolBar1.add(jSeparator1);

        jLabel9.setText("   ");
        jToolBar1.add(jLabel9);

        btnEditar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEditar1.setText("Proceso");
        btnEditar1.setFocusable(false);
        btnEditar1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditar1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditar1);

        jLabel11.setText("  ");
        jToolBar1.add(jLabel11);

        cmbProcesos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Todos>" }));
        cmbProcesos.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbProcesos.setPreferredSize(new java.awt.Dimension(135, 25));
        cmbProcesos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProcesosActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbProcesos);

        jLabel10.setText("                                                                                                                                                                                                                      ");
        jToolBar1.add(jLabel10);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1053, Short.MAX_VALUE)
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

    private void btnAgregarSubProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarSubProcesoActionPerformed
        abrirDialogoAgregar();
    }//GEN-LAST:event_btnAgregarSubProcesoActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        try
        {
            spr = new SubProceso();
            spr.setDescripcion(tblSubProcesos.getValueAt(tblSubProcesos.getSelectedRow(), 0).toString());
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de SubProcesos","Advertencia",JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try 
        {
            spr = sprc.obtSubProcXdesc(spr.getDescripcion());
            llenarComboProcesos(1);
            
            txtSubProcesoEditar.setText(spr.getDescripcion());
            lblIdSubProceso.setText(String.valueOf(spr.getIdSubProceso()));
            lblIdSubProceso.setVisible(false);
            if (spr.getIdProceso() == 1)
            {
                cmbProcesoEditar.setSelectedIndex(0);
            }
            else if (spr.getIdProceso() == 2)
            {
                cmbProcesoEditar.setSelectedIndex(1);
            }
            else if (spr.getIdProceso() == 3)
            {
                cmbProcesoEditar.setSelectedIndex(2);
            }
            else if (spr.getIdProceso() == 4)
            {
                cmbProcesoEditar.setSelectedIndex(3);
            }
            else if (spr.getIdProceso() == 5)
            {
                cmbProcesoEditar.setSelectedIndex(4);
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
        agregarSubProceso();
    }//GEN-LAST:event_btnGuardarAgregarActionPerformed

    private void btnGuardarEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEditarActionPerformed
        editarProveedor();
    }//GEN-LAST:event_btnGuardarEditarActionPerformed

    private void btnEditar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditar1ActionPerformed

    private void cmbProcesosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProcesosActionPerformed
        actualizarTablaSubProcesos();
    }//GEN-LAST:event_cmbProcesosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarSubProceso;
    public javax.swing.JButton btnEditar;
    public javax.swing.JButton btnEditar1;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardarAgregar;
    private javax.swing.JButton btnGuardarEditar;
    private javax.swing.JComboBox<String> cmbProcesoAgregar;
    private javax.swing.JComboBox<String> cmbProcesoEditar;
    private javax.swing.JComboBox<String> cmbProcesos;
    private javax.swing.JDialog dlgAgregarSubProceso;
    private javax.swing.JDialog dlgEditarProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblIdSubProceso;
    private javax.swing.JTable tblSubProcesos;
    private javax.swing.JTextField txtSubProcesoAgregar;
    private javax.swing.JTextField txtSubProcesoEditar;
    // End of variables declaration//GEN-END:variables
}
