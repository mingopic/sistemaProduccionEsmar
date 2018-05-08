/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.ConexionBD;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mingo
 */
public class PnlInsumoXproceso extends javax.swing.JPanel {
    ConexionBD conexion;
    
    DefaultTableModel dtms=new DefaultTableModel();
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "Provedor","Tipo Cuero","No. Camión","Total Piezas","Total Kg","Costo Camión","Fecha de Entrada"
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlInsumoXproceso() throws Exception {
        initComponents();
        inicializar();
    }
    
    
    //Método que se invica al inicializar el sistema, inicializa todas las clases a utilizar
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        
        actualizarTablaCueroTrabajar();
        jrFiltroFechasEntrada.setSelected(false);
        dcFecha1EntradaSemiterminado.setEnabled(false);
        dcFecha2EntradaSemiterminado.setEnabled(false);
        llenarComboProveedores();
    }
    
    
    //método que llena los combobox de los tipos de producto en la base de datos
    public void llenarComboProveedores() throws Exception
    {
        String[] proveedores = new String[3];
        proveedores[0] = "Prov 1";
        proveedores[1] = "Prov 2";
        proveedores[2] = "Prov 3";
        
        int i=0;
        while (i<proveedores.length)
        {
            cmbProveedor.addItem(proveedores[i]);
            i++;
        }
    }
    
    
    //Método para actualizar la tabla de las entradas de cuero por trabajar, se inicializa al llamar la clase
    public void actualizarTablaCueroTrabajar() 
    {
//        boolean invIniciales;
//        
//        //validamos si esta seleccionada lo opción de rango de fechas para tomar el valor seleccionado,
//        //si no esta seleccionado se ponen automáticamente los valores 1900-01-01 y 2040-01-01
//        if (jrFiltroFechasEntrada.isSelected())
//        {
//            try {
//                    String fechaAux="";
//                    String fecha=dcFecha1EntradaSemiterminado.getText();
//                    
//                    if (fecha.length()<10)
//                    {
//                        fecha="0"+fecha;
//                    }
//                    
//                    for (int i=6; i<fecha.length(); i++)
//                    {
//                        fechaAux=fechaAux+fecha.charAt(i);
//                    }
//                    fechaAux=fechaAux+"-";
//                    
//                    for (int i=3; i<5; i++)
//                    {
//                        fechaAux=fechaAux+fecha.charAt(i);
//                    }
//                    fechaAux=fechaAux+"-";
//                    
//                    for (int i=0; i<2; i++)
//                    {
//                        fechaAux=fechaAux+fecha.charAt(i);
//                    }
//                            
//                    ct.setFecha(fechaAux);
//                }
//            catch (Exception ex) 
//                {
//                    ct.setFecha("0");
//                }
//            
//            try {
//                    String fechaAux="";
//                    String fecha=dcFecha2EntradaSemiterminado.getText();
//                    
//                    if (fecha.length()<10)
//                    {
//                        fecha="0"+fecha;
//                    }
//                    
//                    //obtiene año
//                    for (int i=6; i<fecha.length(); i++)
//                    {
//                        fechaAux=fechaAux+fecha.charAt(i);
//                    }
//                    fechaAux=fechaAux+"-";
//                    
//                    //obtiene mes
//                    for (int i=3; i<5; i++)
//                    {
//                        fechaAux=fechaAux+fecha.charAt(i);
//                    }
//                    fechaAux=fechaAux+"-";
//                    
//                    //obtiene día
//                    for (int i=0; i<2; i++)
//                    {
//                        fechaAux=fechaAux+fecha.charAt(i);
//                    }
//                            
//                    ct.setFecha1(fechaAux);
//                }
//            catch (Exception ex) 
//                {
//                    ct.setFecha1("0");
//                }
//        }
//        else
//        {
//            ct.setFecha("1900-01-01");
//            ct.setFecha1("2040-01-01");
//        }
//        
//        
//        //validamos si esta seleccionado algún producto para hacer filtro
//        if (cmbProveedor.getSelectedItem().toString().equals("..."))
//        {
//            ct.setTipoProducto("'%%'");
//        }
//        else
//        {
//            ct.setTipoProducto("'"+cmbProveedor.getSelectedItem().toString()+"'");
//        }
//        
//        ct.setDescripcion("");
//        
//        String[][] datos = null;
//       
//        DefaultTableModel dtm = null;
//        
//        try {
//            
//            datos = ctc.obtenerListaCueroTrabajar(ct);
//            
//            dtm = new DefaultTableModel(datos, cols){
//            public boolean isCellEditable(int row, int column) {
//            return false;
//            }
//            };
//            tblCueroTrabajar.setModel(dtm);
//
//        } catch (Exception e) {
//           
//            e.printStackTrace();
//            
//            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
//        }
    }
    

    //Método que abre el dialogo de agregar entrada de producto por trabajar 
    public void abrirDialogoAgregar() throws Exception
    {    
        dlgAgregar.setSize(400, 350);
        dlgAgregar.setPreferredSize(dlgAgregar.getSize());
        dlgAgregar.setLocationRelativeTo(null);
        dlgAgregar.setAlwaysOnTop(true);
        dlgAgregar.setVisible(true);
    }
    
    
    //Método que abre el dialogo de Detalle producto
    public void abrirDialogoDetalle() throws Exception
    {
        
    }
    
    
    //Poner unidad de acuerdo al producto seleccionado en dlgAgregar
    public void ponerUnidadProducto() throws Exception
    {
        
    }
    
    
    //Inicializar la tabla donde se agregarán los nuevos productos
    public void inicializarTablaAgregarEntrada()
    {
        String[] cols = new String[]
        {
            "Producto","Cantidad","Unidad"
        };
        
        dtms=new DefaultTableModel();
        dtms.setColumnIdentifiers(cols);
    }
    
    
    //Metodo para inicializar los campos de dlgAgregar
    public void inizializarCamposAgregar() throws Exception
    {
//        btnGroup.add(jrNoPartida);
//        btnGroup.add(jrDescripcion);
//        
//        jrNoPartida.setSelected(true);
//        txtNoPartidaCueroTrabajar.setEnabled(true);
//        txtNoPartidaCueroTrabajar.setText("");
//        txtDescripcion.setText("");
//        txtDescripcion.setEnabled(false);
//        txtNoPiezasCueroTrabajar.setText("");
//        lblErrorAgregar.setText("");
    }   
    
    
    //Método para realizar entrada de material y actualizar inventarios
    public void realizarEntrada () throws Exception
    {
//        if (jrNoPartida.isSelected())
//        {
//            if (txtNoPiezasCueroTrabajar.getText().equals("") || txtNoPartidaCueroTrabajar.getText().equals(""))
//            {
//                dlgAgregar.setVisible(false);
//                JOptionPane.showMessageDialog(null,"Es necesario capturar 'No. Partida' y 'No. Piezas'","Mensaje de advertencia",JOptionPane.WARNING_MESSAGE);
//                dlgAgregar.setVisible(true);
//                return;
//            }
//            else
//            {
//                ct.setNoPartida(Integer.parseInt(txtNoPartidaCueroTrabajar.getText()));
//                ct.setDescripcion("");
//            }
//        }
//        else if (jrDescripcion.isSelected())
//        {
//            if (txtDescripcion.getText().equals("") || txtNoPiezasCueroTrabajar.getText().equals(""))
//            {
//                dlgAgregar.setVisible(false);
//                JOptionPane.showMessageDialog(null,"Es necesario capturar 'Descripción' y 'No. Piezas'","Mensaje de advertencia",JOptionPane.WARNING_MESSAGE);
//                dlgAgregar.setVisible(true);
//                return;
//            }
//            else
//            {
//                ct.setNoPartida(0);
//                ct.setDescripcion(txtDescripcion.getText());
//            }
//        }
//        try 
//        {
//            ct.setTipoProducto(cmbProdutoAgregar.getSelectedItem().toString());
//            ct.setNoPiezas(Integer.parseInt(txtNoPiezasCueroTrabajar.getText()));
//            ct.setNoPiezasActuales(Integer.parseInt(txtNoPiezasCueroTrabajar.getText()));
//            ct.setFecha(FrmPrincipal.lblFecha.getText());
//            ctc.agregarEntradaProductoTrabajar(ct);
//            actualizarTablaCueroTrabajar();               
//            dlgAgregar.setVisible(false);
//            JOptionPane.showMessageDialog(null, "Entrada realizada correctamente");
//        } 
//        catch (Exception e) 
//        {
//            dlgAgregar.setVisible(false);                
//            JOptionPane.showMessageDialog(null, "Error de conexión");
//        }
    }
    
    
    //Método para editar la entrada de almacén
    public void editarEntrada() throws Exception
    {
        
    }
            
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dlgAgregar = new javax.swing.JDialog();
        jLabel13 = new javax.swing.JLabel();
        btnRealizarEntrada = new javax.swing.JButton();
        btnCancelarAgregar = new javax.swing.JButton();
        lblErrorAgregar = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        cmbProveedorAgregar = new javax.swing.JComboBox();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel51 = new javax.swing.JLabel();
        txtNoPiezasCueroTrabajar = new javax.swing.JTextField();
        txtNoPartidaCueroTrabajar = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtNoPartidaCueroTrabajar1 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        dcFecha1EntradaSemiterminado1 = new datechooser.beans.DateChooserCombo();
        jLabel38 = new javax.swing.JLabel();
        txtNoPartidaCueroTrabajar2 = new javax.swing.JTextField();
        btnGroup = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCueroTrabajar = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnAgregarEntrada = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jrFiltroFechasEntrada = new javax.swing.JRadioButton();
        jLabel29 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel4 = new javax.swing.JLabel();
        dcFecha1EntradaSemiterminado = new datechooser.beans.DateChooserCombo();
        lbl = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        dcFecha2EntradaSemiterminado = new datechooser.beans.DateChooserCombo();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnBuscarEntrada = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btnReporteEntrada = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Agregar entrada");

        btnRealizarEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/accept_1.png"))); // NOI18N
        btnRealizarEntrada.setText("Aceptar");
        btnRealizarEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarEntradaActionPerformed(evt);
            }
        });

        btnCancelarAgregar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cross.png"))); // NOI18N
        btnCancelarAgregar.setText("Cancelar");
        btnCancelarAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAgregarActionPerformed(evt);
            }
        });

        lblErrorAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblErrorAgregar.setForeground(new java.awt.Color(204, 0, 0));
        lblErrorAgregar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorAgregar.setText("error");

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel31.setText("Proveedor");

        cmbProveedorAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbProveedorAgregar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorAgregarItemStateChanged(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel51.setText("Kg. Totales:");

        txtNoPiezasCueroTrabajar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasCueroTrabajar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasCueroTrabajarKeyTyped(evt);
            }
        });

        txtNoPartidaCueroTrabajar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPartidaCueroTrabajar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaCueroTrabajarKeyTyped(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel32.setText("No. Piezas Ligeros");

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel33.setText("No. Piezas Pesados");

        txtNoPartidaCueroTrabajar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPartidaCueroTrabajar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaCueroTrabajar1KeyTyped(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel35.setText("Mayor a xx");

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel36.setText("Menor a xx");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel37.setText("Fecha llegada");

        dcFecha1EntradaSemiterminado1.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dcFecha1EntradaSemiterminado1.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
    dcFecha1EntradaSemiterminado1.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
    try {
        dcFecha1EntradaSemiterminado1.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha1EntradaSemiterminado1.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));

    jLabel38.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    jLabel38.setText("No. Camión");

    txtNoPartidaCueroTrabajar2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    txtNoPartidaCueroTrabajar2.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtNoPartidaCueroTrabajar2KeyTyped(evt);
        }
    });

    javax.swing.GroupLayout dlgAgregarLayout = new javax.swing.GroupLayout(dlgAgregar.getContentPane());
    dlgAgregar.getContentPane().setLayout(dlgAgregarLayout);
    dlgAgregarLayout.setHorizontalGroup(
        dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(dlgAgregarLayout.createSequentialGroup()
            .addGap(10, 10, 10)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGap(10, 10, 10))
        .addComponent(jSeparator5)
        .addComponent(lblErrorAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarLayout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnRealizarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnCancelarAgregar)
            .addContainerGap())
        .addGroup(dlgAgregarLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(dlgAgregarLayout.createSequentialGroup()
                    .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel36)
                        .addGroup(dlgAgregarLayout.createSequentialGroup()
                            .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(dlgAgregarLayout.createSequentialGroup()
                                    .addComponent(jLabel31)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbProveedorAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(dlgAgregarLayout.createSequentialGroup()
                                    .addComponent(jLabel37)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dcFecha1EntradaSemiterminado1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(18, 18, 18)
                            .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel32)
                                .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(dlgAgregarLayout.createSequentialGroup()
                            .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarLayout.createSequentialGroup()
                                    .addComponent(txtNoPartidaCueroTrabajar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel33))
                                .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtNoPartidaCueroTrabajar1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtNoPartidaCueroTrabajar2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(dlgAgregarLayout.createSequentialGroup()
                    .addComponent(jLabel51)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtNoPiezasCueroTrabajar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    dlgAgregarLayout.setVerticalGroup(
        dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(dlgAgregarLayout.createSequentialGroup()
            .addComponent(jLabel13)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel31)
                .addComponent(cmbProveedorAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel32)
                .addComponent(txtNoPartidaCueroTrabajar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel33)
                .addComponent(txtNoPartidaCueroTrabajar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(dcFecha1EntradaSemiterminado1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(dlgAgregarLayout.createSequentialGroup()
                    .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel35)
                        .addComponent(jLabel36))
                    .addGap(18, 18, 18)
                    .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel38)
                        .addComponent(jLabel37)
                        .addComponent(txtNoPartidaCueroTrabajar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGap(36, 36, 36)
            .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel51)
                .addComponent(txtNoPiezasCueroTrabajar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
            .addComponent(lblErrorAgregar)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnRealizarEntrada)
                .addComponent(btnCancelarAgregar))
            .addContainerGap())
    );

    setBackground(new java.awt.Color(255, 255, 255));

    tblCueroTrabajar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    tblCueroTrabajar.setModel(new javax.swing.table.DefaultTableModel(
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
    jScrollPane1.setViewportView(tblCueroTrabajar);

    jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);

    btnAgregarEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnAgregarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
    btnAgregarEntrada.setText("Agregar Recepción");
    btnAgregarEntrada.setFocusable(false);
    btnAgregarEntrada.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnAgregarEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnAgregarEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnAgregarEntrada.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAgregarEntradaActionPerformed(evt);
        }
    });
    jToolBar1.add(btnAgregarEntrada);

    jLabel1.setText("   ");
    jToolBar1.add(jLabel1);

    jLabel10.setText("   ");
    jToolBar1.add(jLabel10);

    jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel5.setText("Proveedor:");
    jToolBar1.add(jLabel5);

    jLabel34.setText("   ");
    jToolBar1.add(jLabel34);

    cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
    cmbProveedor.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbProveedor.setPreferredSize(new java.awt.Dimension(120, 25));
    cmbProveedor.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbProveedorActionPerformed(evt);
        }
    });
    jToolBar1.add(cmbProveedor);

    jLabel28.setText("   ");
    jToolBar1.add(jLabel28);

    jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel8.setForeground(new java.awt.Color(227, 222, 222));
    jLabel8.setText("     ");
    jToolBar1.add(jLabel8);

    jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/calendar.png"))); // NOI18N
    jToolBar1.add(jLabel27);

    jrFiltroFechasEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jrFiltroFechasEntrada.setFocusable(false);
    jrFiltroFechasEntrada.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jrFiltroFechasEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jrFiltroFechasEntrada.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
    jrFiltroFechasEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jrFiltroFechasEntrada.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jrFiltroFechasEntradaActionPerformed(evt);
        }
    });
    jToolBar1.add(jrFiltroFechasEntrada);

    jLabel29.setText("   ");
    jToolBar1.add(jLabel29);
    jToolBar1.add(jSeparator1);

    jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel4.setText("De:");
    jToolBar1.add(jLabel4);

    dcFecha1EntradaSemiterminado.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcFecha1EntradaSemiterminado.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
dcFecha1EntradaSemiterminado.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
try {
    dcFecha1EntradaSemiterminado.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha1EntradaSemiterminado.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar1.add(dcFecha1EntradaSemiterminado);

    lbl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl.setForeground(new java.awt.Color(227, 222, 222));
    lbl.setText("     ");
    jToolBar1.add(lbl);

    jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel25.setText("Hasta:");
    jToolBar1.add(jLabel25);

    dcFecha2EntradaSemiterminado.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcFecha2EntradaSemiterminado.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
try {
    dcFecha2EntradaSemiterminado.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha2EntradaSemiterminado.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar1.add(dcFecha2EntradaSemiterminado);
    jToolBar1.add(jSeparator2);

    btnBuscarEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnBuscarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/magnifier.png"))); // NOI18N
    btnBuscarEntrada.setText("Buscar");
    btnBuscarEntrada.setFocusable(false);
    btnBuscarEntrada.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnBuscarEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnBuscarEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnBuscarEntrada.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnBuscarEntradaActionPerformed(evt);
        }
    });
    jToolBar1.add(btnBuscarEntrada);
    jToolBar1.add(jSeparator6);

    btnReporteEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteEntrada.setText("Reporte");
    btnReporteEntrada.setFocusable(false);
    btnReporteEntrada.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntrada.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntradaActionPerformed(evt);
        }
    });
    jToolBar1.add(btnReporteEntrada);

    jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel11.setForeground(new java.awt.Color(227, 222, 222));
    jLabel11.setText("     ");
    jToolBar1.add(jLabel11);

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

    private void btnRealizarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaActionPerformed
        try
        {
            realizarEntrada();
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(PnlInsumoXproceso.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }//GEN-LAST:event_btnRealizarEntradaActionPerformed

    private void btnCancelarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarActionPerformed
        dlgAgregar.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarActionPerformed

    private void cmbProveedorAgregarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProveedorAgregarItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {
            try {
                ponerUnidadProducto();
            } catch (Exception ex) {
                Logger.getLogger(PnlInsumoXproceso.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_cmbProveedorAgregarItemStateChanged

    private void txtNoPartidaCueroTrabajarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaCueroTrabajarKeyTyped
       char c;
        c=evt.getKeyChar();    
        
        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();           
            evt.consume(); 
        }
    }//GEN-LAST:event_txtNoPartidaCueroTrabajarKeyTyped

    private void txtNoPiezasCueroTrabajarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasCueroTrabajarKeyTyped
        char c;
        c=evt.getKeyChar();    
        
        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();           
            evt.consume(); 
        }
    }//GEN-LAST:event_txtNoPiezasCueroTrabajarKeyTyped

    private void btnReporteEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradaActionPerformed
//        try {
//            actualizarTablaCueroTrabajar();
//            ctc.mostrarReporteInventarioTrabajar(ct);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, "Error de conexión a BD","Mensaje de error",JOptionPane.ERROR_MESSAGE);
//            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_btnReporteEntradaActionPerformed

    private void btnBuscarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEntradaActionPerformed
        actualizarTablaCueroTrabajar();
    }//GEN-LAST:event_btnBuscarEntradaActionPerformed

    private void jrFiltroFechasEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrFiltroFechasEntradaActionPerformed
        if (dcFecha1EntradaSemiterminado.isEnabled() && dcFecha2EntradaSemiterminado.isEnabled())
        {
            dcFecha1EntradaSemiterminado.setEnabled(false);
            dcFecha1EntradaSemiterminado.setCurrent(null);
            dcFecha2EntradaSemiterminado.setEnabled(false);
            dcFecha2EntradaSemiterminado.setCurrent(null);
        }
        else
        {
            dcFecha1EntradaSemiterminado.setEnabled(true);
            dcFecha2EntradaSemiterminado.setEnabled(true);
        }
    }//GEN-LAST:event_jrFiltroFechasEntradaActionPerformed

    private void cmbProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProveedorActionPerformed
        actualizarTablaCueroTrabajar();
    }//GEN-LAST:event_cmbProveedorActionPerformed

    private void btnAgregarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEntradaActionPerformed
        try {
            abrirDialogoAgregar();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsumoXproceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAgregarEntradaActionPerformed

    private void txtNoPartidaCueroTrabajar1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaCueroTrabajar1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPartidaCueroTrabajar1KeyTyped

    private void txtNoPartidaCueroTrabajar2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaCueroTrabajar2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPartidaCueroTrabajar2KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarEntrada;
    private javax.swing.JButton btnBuscarEntrada;
    private javax.swing.JButton btnCancelarAgregar;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnRealizarEntrada;
    private javax.swing.JButton btnReporteEntrada;
    private javax.swing.JComboBox cmbProveedor;
    private javax.swing.JComboBox cmbProveedorAgregar;
    private datechooser.beans.DateChooserCombo dcFecha1EntradaSemiterminado;
    private datechooser.beans.DateChooserCombo dcFecha1EntradaSemiterminado1;
    private datechooser.beans.DateChooserCombo dcFecha2EntradaSemiterminado;
    private javax.swing.JDialog dlgAgregar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JRadioButton jrFiltroFechasEntrada;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblErrorAgregar;
    private javax.swing.JTable tblCueroTrabajar;
    private javax.swing.JTextField txtNoPartidaCueroTrabajar;
    private javax.swing.JTextField txtNoPartidaCueroTrabajar1;
    private javax.swing.JTextField txtNoPartidaCueroTrabajar2;
    private javax.swing.JTextField txtNoPiezasCueroTrabajar;
    // End of variables declaration//GEN-END:variables
}
