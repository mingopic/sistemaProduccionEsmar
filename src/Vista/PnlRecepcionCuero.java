/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.ConexionBD;
import Controlador.ConfiguracionMermaCommands;
import Controlador.InventarioCrudoCommands;
import Controlador.PartidaDetalleCommands;
import Controlador.ProveedorCommands;
import Controlador.RangoPesoCueroCommands;
import Controlador.RecepcionCueroCommands;
import Controlador.TipoCueroCommands;
import Modelo.Entity.ConfiguracionMerma;
import Modelo.Entity.InventarioCrudo;
import Modelo.Entity.Proveedor;
import Modelo.Entity.RangoPesoCuero;
import Modelo.Entity.RecepcionCuero;
import Modelo.Entity.TipoCuero;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Mingo
 */
public class PnlRecepcionCuero extends javax.swing.JPanel {
    PnlInsRecCuero pnlInsRecCuero;
    ConexionBD conexion;
    RecepcionCuero rc;
    RecepcionCueroCommands rcc;
    Proveedor p;
    ProveedorCommands pc;
    TipoCuero tc;
    TipoCueroCommands tcc;
    RangoPesoCuero rpc;
    RangoPesoCueroCommands rpcc;
    ConfiguracionMerma cm;
    ConfiguracionMermaCommands cmc;
    PartidaDetalleCommands pdc;
    InventarioCrudo ic;
    InventarioCrudoCommands icc;
    String[][] proveedoresAgregar = null;
    String[][] tipoCueroAgregar = null;
    String[][] rangoPesoCuero = null;
    String[][] datosEntRecCuero = null;
    private final String imagen="/Imagenes/logo_esmar.png";
    
    DefaultTableModel dtms=new DefaultTableModel();
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "Provedor","Tipo Cuero","No. Camión","Total Piezas","Total Kg","Precio x Kg","Costo Camión","Origen","Fecha de Entrada"
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlRecepcionCuero() throws Exception {
        initComponents();
        inicializar();
    }
    
    
    //Método que se invica al inicializar el sistema, inicializa todas las clases a utilizar
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        rc = new RecepcionCuero();
        rcc = new RecepcionCueroCommands();
        tc = new TipoCuero();
        tcc = new TipoCueroCommands();
        
        jrFiltroFechasEntrada.setSelected(false);
        dcFecha1EntradaSemiterminado.setEnabled(false);
        dcFecha2EntradaSemiterminado.setEnabled(false);
        llenarComboProveedores();
        llenarComboTipoCuero();
        actualizarTablaRecepcionCuero();
        
        for (int i = 0; i < FrmPrincipal.roles.length; i++)
        {
            if (FrmPrincipal.roles[i].equals("Produccion") || FrmPrincipal.roles[i].equals("Sistemas") || FrmPrincipal.roles[i].equals("Rivera"))
            {
                btnAgregarEntrada.setEnabled(true);
                btnEliminarEntrada.setEnabled(true);
                break;
            }
        }
    }
    
    
    //método que llena los combobox de los proveedores en la base de datos
    public void llenarComboProveedores() throws Exception
    {
        pc = new ProveedorCommands();
        String[][] proveedores = pc.llenarComboboxProveedores();
        
        int i=0;
        while (i<proveedores.length)
        {
            cmbProveedor.addItem(proveedores[i][1]);
            i++;
        }
    }
    
    //método que llena los combobox del tipo de cuero en la base de datos
    public void llenarComboTipoCuero() throws Exception
    {
        tcc = new TipoCueroCommands();
        String[][] tipoCuero = tcc.llenarComboboxTipoCuero();
        
        int i=0;
        while (i<tipoCuero.length)
        {
            cmbTipoCuero.addItem(tipoCuero[i][1]);
            i++;
        }
    }
    
    //Método para actualizar la tabla de las entradas de cuero por trabajar, se inicializa al llamar la clase
    public void actualizarTablaRecepcionCuero() 
    {
//        //validamos si esta seleccionada lo opción de rango de fechas para tomar el valor seleccionado,
//        //si no esta seleccionado se ponen automáticamente los valores 1900-01-01 y 2040-01-01
        if (jrFiltroFechasEntrada.isSelected())
        {
            try {
                    String fechaAux="";
                    String fecha=dcFecha1EntradaSemiterminado.getText();
                    
                    if (fecha.length()<10)
                    {
                        fecha="0"+fecha;
                    }
                    
                    for (int i=6; i<fecha.length(); i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    for (int i=3; i<5; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    for (int i=0; i<2; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                            
                    rc.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    rc.setFecha("0");
                }
            
            try {
                    String fechaAux="";
                    String fecha=dcFecha2EntradaSemiterminado.getText();
                    
                    if (fecha.length()<10)
                    {
                        fecha="0"+fecha;
                    }
                    
                    //obtiene año
                    for (int i=6; i<fecha.length(); i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    //obtiene mes
                    for (int i=3; i<5; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
//                    
//                    //obtiene día
                    for (int i=0; i<2; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                            
                    rc.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    rc.setFecha1("0");
                }
        }
        else
        {
            rc.setFecha("1900-01-01");
            rc.setFecha1("2040-01-01");
        }
        
        
        //validamos si esta seleccionado algún proveedor para hacer filtro
        if (cmbProveedor.getSelectedItem().toString().equals("<Todos>"))
        {
            rc.setProveedor("%%");
        }
        else
        {
            rc.setProveedor(cmbProveedor.getSelectedItem().toString());
        }
        
        //validamos si esta seleccionado algún tipo de cuero para hacer filtro
        if (cmbTipoCuero.getSelectedItem().toString().equals("<Todos>"))
        {
            tc.setDescripcion("%%");
        }
        else
        {
            tc.setDescripcion(cmbTipoCuero.getSelectedItem().toString());
        }
        
        DefaultTableModel dtm = null;
        
        try {
            
            datosEntRecCuero = rcc.obtenerListaRecepcionCuero(rc,tc);
            
            dtm = new DefaultTableModel(datosEntRecCuero, cols){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblRecepcionCuero.setModel(dtm);
            tblRecepcionCuero.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
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

    public void generarReporteEntradaRecepcionCuero(RecepcionCuero rc, TipoCuero tp)
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteEntradas.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("proveedor",rc.getProveedor());
            parametros.put("descripcion",tp.getDescripcion());
            parametros.put("fecha",rc.getFecha());
            parametros.put("fecha1",rc.getFecha1());
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(path);
            
            conexion.conectar();
            
            JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, conexion.getConexion());
            
            JasperViewer view = new JasperViewer(jprint, false);
            
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            view.setVisible(true);
            conexion.desconectar();
        } catch (JRException ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se puede generar el reporte","Error",JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generarReporteInventarioCueroCrudo(RecepcionCuero rc, TipoCuero tp)
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteInvCueCrudo.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(path);
            
            conexion.conectar();
            
            JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, conexion.getConexion());
            
            JasperViewer view = new JasperViewer(jprint, false);
            
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            view.setVisible(true);
            conexion.desconectar();
        } catch (JRException ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se puede generar el reporte","Error",JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generarReporteEntradaRecepcionCueroDetalle(RecepcionCuero rc)
    {
        try
        {
            int idRecepcionCuero = 0;
            try 
            {
                idRecepcionCuero = Integer.parseInt(datosEntRecCuero[tblRecepcionCuero.getSelectedRow()][9]);
            } 
            catch (Exception e) 
            {
                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla","Mensaje",JOptionPane.WARNING_MESSAGE);
                return;
            }
            rc.setIdRecepcionCuero(idRecepcionCuero);
            
            URL path = this.getClass().getResource("/Reportes/ReporteEntradaDetalle.jasper");
            
            Map parametros = new HashMap();
            parametros.put("idRecepcionCuero",rc.getIdRecepcionCuero());
            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(path);
            
            conexion.conectar();
            
            JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, conexion.getConexion());
            
            JasperViewer view = new JasperViewer(jprint, false);
            
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            view.setVisible(true);
            conexion.desconectar();
        } catch (JRException ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se puede generar el  reporte","Error",JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarRecepcionCuero()
    {
        try
        {
            int fila = tblRecepcionCuero.getSelectedRow();
            
            if (fila != -1)
            {
                if (JOptionPane.showConfirmDialog(null, "¿Realmente desea eliminar la recepción de cuero seleccionada?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
                {
                    rc = new RecepcionCuero();
                    ic = new InventarioCrudo();

                    rc.setIdRecepcionCuero(Integer.parseInt(datosEntRecCuero[fila][9]));

                    int idRecepcionCuero = pdc.obtenerRecepcionCueroEliminar(rc);

                    if (idRecepcionCuero == 0)
                    {
                        icc.eliminarInventarioCrudo(rc);
                        rcc.eliminarRecepcionCuero(rc);
                        actualizarTablaRecepcionCuero();
                        JOptionPane.showMessageDialog(null, "Recepción de cuero eliminada");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "La recepción de cuero que desea eliminar ya sido utilizada en otros procesos","Advertencia",JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Recepción Cuero","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        }
        catch (Exception e)
        {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, e);
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
        dlgEliPzaRecCuero = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        txtNoPiezasActuales = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txtNoPiezasEliminar = new javax.swing.JTextField();
        btnRealizarEntradaEnvSemi = new javax.swing.JButton();
        btnCancelarAgregarEnvSemi = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtMotivo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNoCamion = new javax.swing.JTextField();
        txtProveedor = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRecepcionCuero = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbTipoCuero = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel27 = new javax.swing.JLabel();
        jrFiltroFechasEntrada = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        dcFecha1EntradaSemiterminado = new datechooser.beans.DateChooserCombo();
        lbl = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        dcFecha2EntradaSemiterminado = new datechooser.beans.DateChooserCombo();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnBuscarEntrada = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        btnReporteEntrada = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        btnReporteEntrada1 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        btnReporteEntrada2 = new javax.swing.JButton();
        jToolBar3 = new javax.swing.JToolBar();
        jLabel51 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        btnAgregarEntrada = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        btnEliminarEntrada = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel55.setText("No. Camión:");

        txtNoPiezasActuales.setEditable(false);
        txtNoPiezasActuales.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasActuales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoPiezasActualesActionPerformed(evt);
            }
        });
        txtNoPiezasActuales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasActualesKeyTyped(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel32.setText("Proveedor:");

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel53.setText("Motivo:");

        txtNoPiezasEliminar.setEditable(false);
        txtNoPiezasEliminar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasEliminar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasEliminarKeyTyped(evt);
            }
        });

        btnRealizarEntradaEnvSemi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarEntradaEnvSemi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/accept_1.png"))); // NOI18N
        btnRealizarEntradaEnvSemi.setText("Aceptar");
        btnRealizarEntradaEnvSemi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarEntradaEnvSemiActionPerformed(evt);
            }
        });

        btnCancelarAgregarEnvSemi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarAgregarEnvSemi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cross.png"))); // NOI18N
        btnCancelarAgregarEnvSemi.setText("Cancelar");
        btnCancelarAgregarEnvSemi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAgregarEnvSemiActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 204, 51));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
        jLabel14.setText("Eliminar Piezas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        txtMotivo.setEditable(false);
        txtMotivo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMotivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMotivoActionPerformed(evt);
            }
        });
        txtMotivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMotivoKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("No. piezas actuales:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("No. piezas a eliminar:");

        txtNoCamion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoCamionActionPerformed(evt);
            }
        });

        txtProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProveedorActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Tipo de cuero:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Tipo de recorte:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel32)
                                    .addComponent(jLabel55)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel53)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMotivo)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNoPiezasActuales, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                        .addComponent(txtNoCamion)
                                        .addComponent(jTextField1)
                                        .addComponent(jTextField2))
                                    .addComponent(txtNoPiezasEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 131, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRealizarEntradaEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelarAgregarEnvSemi)))
                .addGap(6, 6, 6))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txtNoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasActuales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntradaEnvSemi)
                    .addComponent(btnCancelarAgregarEnvSemi))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEliPzaRecCueroLayout = new javax.swing.GroupLayout(dlgEliPzaRecCuero.getContentPane());
        dlgEliPzaRecCuero.getContentPane().setLayout(dlgEliPzaRecCueroLayout);
        dlgEliPzaRecCueroLayout.setHorizontalGroup(
            dlgEliPzaRecCueroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEliPzaRecCueroLayout.setVerticalGroup(
            dlgEliPzaRecCueroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        tblRecepcionCuero.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblRecepcionCuero.setModel(new javax.swing.table.DefaultTableModel(
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
        tblRecepcionCuero.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblRecepcionCuero);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.add(jLabel1);

        jLabel10.setText("   ");
        jToolBar1.add(jLabel10);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Proveedor:");
        jToolBar1.add(jLabel5);

        jLabel34.setText("   ");
        jToolBar1.add(jLabel34);

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
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

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Tipo de Cuero:");
        jToolBar1.add(jLabel6);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(227, 222, 222));
        jLabel8.setText("  ");
        jToolBar1.add(jLabel8);

        cmbTipoCuero.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
        cmbTipoCuero.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbTipoCuero.setPreferredSize(new java.awt.Dimension(120, 25));
        cmbTipoCuero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoCueroActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbTipoCuero);

        jLabel29.setText("   ");
        jToolBar1.add(jLabel29);
        jToolBar1.add(jSeparator1);

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

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("De:");
        jToolBar1.add(jLabel4);

        dcFecha1EntradaSemiterminado.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dcFecha1EntradaSemiterminado.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
    dcFecha1EntradaSemiterminado.setFormat(2);
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
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
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

    jToolBar2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar2.setFloatable(false);
    jToolBar2.setRollover(true);

    btnReporteEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteEntrada.setText("Reporte de Entradas");
    btnReporteEntrada.setFocusable(false);
    btnReporteEntrada.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    btnReporteEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntrada.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntradaActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteEntrada);

    jLabel50.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel50.setForeground(new java.awt.Color(227, 222, 222));
    jLabel50.setText("     ");
    jToolBar2.add(jLabel50);

    btnReporteEntrada1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntrada1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteEntrada1.setText("Reporte Detalle Entrada");
    btnReporteEntrada1.setFocusable(false);
    btnReporteEntrada1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteEntrada1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteEntrada1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntrada1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntrada1ActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteEntrada1);

    jLabel48.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel48.setForeground(new java.awt.Color(227, 222, 222));
    jLabel48.setText("     ");
    jToolBar2.add(jLabel48);

    btnReporteEntrada2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntrada2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteEntrada2.setText("Reporte Inventario Crudo");
    btnReporteEntrada2.setFocusable(false);
    btnReporteEntrada2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteEntrada2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntrada2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntrada2ActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteEntrada2);

    jToolBar3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar3.setFloatable(false);
    jToolBar3.setRollover(true);

    jLabel51.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel51.setForeground(new java.awt.Color(227, 222, 222));
    jToolBar3.add(jLabel51);

    jLabel49.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel49.setForeground(new java.awt.Color(227, 222, 222));
    jToolBar3.add(jLabel49);

    btnAgregarEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnAgregarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
    btnAgregarEntrada.setText("Agregar Recepción");
    btnAgregarEntrada.setEnabled(false);
    btnAgregarEntrada.setFocusable(false);
    btnAgregarEntrada.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnAgregarEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnAgregarEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnAgregarEntrada.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAgregarEntradaActionPerformed(evt);
        }
    });
    jToolBar3.add(btnAgregarEntrada);

    jLabel30.setText("   ");
    jToolBar3.add(jLabel30);

    btnEliminarEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnEliminarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
    btnEliminarEntrada.setText("Eliminar Recepción");
    btnEliminarEntrada.setEnabled(false);
    btnEliminarEntrada.setFocusable(false);
    btnEliminarEntrada.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnEliminarEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnEliminarEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnEliminarEntrada.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnEliminarEntradaActionPerformed(evt);
        }
    });
    jToolBar3.add(btnEliminarEntrada);

    jLabel31.setText("   ");
    jToolBar3.add(jLabel31);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane1)
        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
        .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
        .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
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

    private void btnBuscarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEntradaActionPerformed
        actualizarTablaRecepcionCuero();
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
        actualizarTablaRecepcionCuero();
    }//GEN-LAST:event_cmbProveedorActionPerformed

    private void btnAgregarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEntradaActionPerformed
        try 
        {
            pnlInsRecCuero = new PnlInsRecCuero();
            FrmPrincipal.pnlPrincipalx.removeAll();
            FrmPrincipal.pnlPrincipalx.add(pnlInsRecCuero, BorderLayout.CENTER);
            FrmPrincipal.pnlPrincipalx.paintAll(pnlInsRecCuero.getGraphics());
            
            FrmPrincipal.lblVentana.setText("Agregar Recepción Cuero");
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\lorry.png");
            FrmPrincipal.lblVentana.setIcon(ico);
        } 
        catch (Exception ex)
        {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAgregarEntradaActionPerformed

    private void cmbTipoCueroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoCueroActionPerformed
        actualizarTablaRecepcionCuero();
    }//GEN-LAST:event_cmbTipoCueroActionPerformed

    private void btnReporteEntrada1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada1ActionPerformed
        generarReporteEntradaRecepcionCueroDetalle(rc);
    }//GEN-LAST:event_btnReporteEntrada1ActionPerformed

    private void btnReporteEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradaActionPerformed
        actualizarTablaRecepcionCuero();
        generarReporteEntradaRecepcionCuero(rc, tc);
    }//GEN-LAST:event_btnReporteEntradaActionPerformed

    private void btnReporteEntrada2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada2ActionPerformed
        generarReporteInventarioCueroCrudo(rc, tc);
    }//GEN-LAST:event_btnReporteEntrada2ActionPerformed

    private void btnEliminarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarEntradaActionPerformed
        eliminarRecepcionCuero();
    }//GEN-LAST:event_btnEliminarEntradaActionPerformed

    private void txtNoPiezasActualesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoPiezasActualesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPiezasActualesActionPerformed

    private void txtNoPiezasActualesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasActualesKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPiezasActualesKeyTyped

    private void txtNoPiezasEliminarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasEliminarKeyTyped
        char c;
        c=evt.getKeyChar();

        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtNoPiezasEliminarKeyTyped

    private void btnRealizarEntradaEnvSemiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaEnvSemiActionPerformed
        try
        {
//            realizarEntradaEnvSemi();
        }
        catch (Exception ex)
        {
            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRealizarEntradaEnvSemiActionPerformed

    private void btnCancelarAgregarEnvSemiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarEnvSemiActionPerformed
        dlgEliPzaRecCuero.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarEnvSemiActionPerformed

    private void txtMotivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMotivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMotivoActionPerformed

    private void txtMotivoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMotivoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMotivoKeyTyped

    private void txtNoCamionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoCamionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoCamionActionPerformed

    private void txtProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProveedorActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarEntrada;
    private javax.swing.JButton btnBuscarEntrada;
    private javax.swing.JButton btnCancelarAgregarEnvSemi;
    public javax.swing.JButton btnEliminarEntrada;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnRealizarEntradaEnvSemi;
    private javax.swing.JButton btnReporteEntrada;
    private javax.swing.JButton btnReporteEntrada1;
    private javax.swing.JButton btnReporteEntrada2;
    private javax.swing.JComboBox cmbProveedor;
    private javax.swing.JComboBox cmbTipoCuero;
    private datechooser.beans.DateChooserCombo dcFecha1EntradaSemiterminado;
    private datechooser.beans.DateChooserCombo dcFecha2EntradaSemiterminado;
    private javax.swing.JDialog dlgEliPzaRecCuero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JRadioButton jrFiltroFechasEntrada;
    private javax.swing.JLabel lbl;
    private javax.swing.JTable tblRecepcionCuero;
    private javax.swing.JTextField txtMotivo;
    private javax.swing.JTextField txtNoCamion;
    private javax.swing.JTextField txtNoPiezasActuales;
    private javax.swing.JTextField txtNoPiezasEliminar;
    private javax.swing.JTextField txtProveedor;
    // End of variables declaration//GEN-END:variables
}
