/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.ConexionBD;
import Controlador.ConfiguracionMermaCommands;
import Controlador.InventarioCrossCommands;
import Controlador.InventarioCrossSemiterminadoCommands;
import Controlador.ProveedorCommands;
import Controlador.RangoPesoCueroCommands;
import Controlador.RecepcionCueroCommands;
import Controlador.TipoCueroCommands;
import Modelo.ConfiguracionMerma;
import Modelo.InventarioCross;
import Modelo.InventarioCrossSemiterminado;
import Modelo.Proveedor;
import Modelo.RangoPesoCuero;
import Modelo.RecepcionCuero;
import Modelo.TipoCuero;
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
public class PnlCross extends javax.swing.JPanel {
    PnlInsRecCuero pnlInsRecCuero;
    ConexionBD conexion;
    InventarioCross ic;
    InventarioCrossCommands icc;
    Proveedor p;
    ProveedorCommands pc;
    TipoCuero tc;
    TipoCueroCommands tcc;
    RangoPesoCuero rpc;
    RangoPesoCueroCommands rpcc;
    ConfiguracionMerma cm;
    ConfiguracionMermaCommands cmc;
    InventarioCrossSemiterminado ics;
    InventarioCrossSemiterminadoCommands icsc;
    String[][] proveedoresAgregar = null;
    String[][] tipoCueroAgregar = null;
    String[][] rangoPesoCuero = null;
    String[][] datosInvCross = null;
    private final String imagen="/Imagenes/logo_esmar.png";
    
    DefaultTableModel dtms=new DefaultTableModel();
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "No. Partida","Tipo Recorte","No. Piezas","No. Piezas Actuales","Fecha de Entrada"
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlCross() throws Exception {
        initComponents();
        inicializar();
    }
    
    
    //Método que se invica al inicializar el sistema, inicializa todas las clases a utilizar
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        ic = new InventarioCross();
        icc = new InventarioCrossCommands();
        tc = new TipoCuero();
        tcc = new TipoCueroCommands();
        
        actualizarTablaCross();
        jrFiltroFechasEntrada.setSelected(false);
        dcFecha1EntradaSemiterminado.setEnabled(false);
        dcFecha2EntradaSemiterminado.setEnabled(false);
        llenarComboTipoCuero();
    }
    
    
    //método que llena los combobox de los proveedores en la base de datos
    public void llenarComboProveedores() throws Exception
    {
//        pc = new ProveedorCommands();
//        String[][] proveedores = pc.llenarComboboxProveedores();
//        
//        int i=0;
//        while (i<proveedores.length)
//        {
//            cmbProveedor.addItem(proveedores[i][1]);
//            i++;
//        }
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
    
    private void validarNumerosEnteros(java.awt.event.KeyEvent evt, String textoCaja)
    {
        try {
            char c = evt.getKeyChar();
            
            if (c<'0' || c>'9') 
            {
                evt.consume();
            }
        } catch (Exception ex) {
            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Método para actualizar la tabla de las entradas de cuero por trabajar, se inicializa al llamar la clase
    public void actualizarTablaCross() 
    {
        //validamos si esta seleccionada lo opción de rango de fechas para tomar el valor seleccionado,
        //si no esta seleccionado se ponen automáticamente los valores 1900-01-01 y 2040-01-01
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
                            
                    ic.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    ic.setFecha("0");
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
                            
                    ic.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    ic.setFecha1("0");
                }
        }
        else
        {
            ic.setFecha("1900-01-01");
            ic.setFecha1("2040-01-01");
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
        
        if (txtNoPartida.getText().isEmpty())
        {
            ic.setIdPartida(0);
        }
        else
        {
            int noPartida = Integer.parseInt(txtNoPartida.getText());
            ic.setIdPartida(noPartida);
        }
        
        DefaultTableModel dtm = null;
        
        try {
            
            datosInvCross = icc.obtenerListaInvCross(ic,tc);
            
            dtm = new DefaultTableModel(datosInvCross, cols){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblInvCross.setModel(dtm);
            tblInvCross.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Inicializar la tabla donde se agregarán los nuevos productos
    public void inicializarTablaAgregarEntrada()
    {
//        String[] cols = new String[]
//        {
//            "Producto","Cantidad","Unidad"
//        };
//        
//        dtms=new DefaultTableModel();
//        dtms.setColumnIdentifiers(cols);
    }

    public void generarReporteEntradaRecepcionCuero(RecepcionCuero rc, TipoCuero tp)
    {
//        try
//        {
//            URL path = this.getClass().getResource("/Reportes/ReporteEntradas.jasper");
//            
//            Map parametros = new HashMap();
//            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
//            parametros.put("proveedor",rc.getProveedor());
//            parametros.put("descripcion",tp.getDescripcion());
//            parametros.put("fecha",rc.getFecha());
//            parametros.put("fecha1",rc.getFecha1());
//            
//            JasperReport reporte=(JasperReport) JRLoader.loadObject(path);
//            
//            conexion.conectar();
//            
//            JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, conexion.getConexion());
//            
//            JasperViewer view = new JasperViewer(jprint, false);
//            
//            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//            
//            view.setVisible(true);
//            conexion.desconectar();
//        } catch (JRException ex) {
//            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, "No se puede generar el reporte","Error",JOptionPane.ERROR_MESSAGE);
//        } catch (Exception ex) {
//            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    public void generarReporteEntradaRecepcionCueroDetalle(RecepcionCuero rc)
    {
//        try
//        {
//            int idRecepcionCuero = 0;
//            try 
//            {
//                idRecepcionCuero = Integer.parseInt(datosEntRecCuero[tblInvCross.getSelectedRow()][8]);
//            } 
//            catch (Exception e) 
//            {
//                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla","Mensaje",JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//            rc.setIdRecepcionCuero(idRecepcionCuero);
//            
//            URL path = this.getClass().getResource("/Reportes/ReporteEntradaDetalle.jasper");
//            
//            Map parametros = new HashMap();
//            parametros.put("idRecepcionCuero",rc.getIdRecepcionCuero());
//            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
//            
//            JasperReport reporte=(JasperReport) JRLoader.loadObject(path);
//            
//            conexion.conectar();
//            
//            JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, conexion.getConexion());
//            
//            JasperViewer view = new JasperViewer(jprint, false);
//            
//            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//            
//            view.setVisible(true);
//            conexion.desconectar();
//        } catch (JRException ex) {
//            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, "No se puede generar el  reporte","Error",JOptionPane.ERROR_MESSAGE);
//        } catch (Exception ex) {
//            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    //Metodo para inicializar los campos de dlgAgregar
    public void inicializarCamposEnvSemi() throws Exception
    {
        int fila = tblInvCross.getSelectedRow();
        
        txtNoPartidaEnvSemi.setText(String.valueOf(tblInvCross.getValueAt(fila, 0)));
        txtTipoRecorteEnvSemi.setText(String.valueOf(tblInvCross.getValueAt(fila, 1)));
        txtNoPiezasActualesEnvSemi.setText(String.valueOf(tblInvCross.getValueAt(fila, 3)));
    }  
    
    //Método que abre el dialogo para enviar a semiterminado 
    public void abrirDialogoEnvSemi() throws Exception
    {
        
        inicializarCamposEnvSemi();
        
        dlgEnvSemi.setSize(400, 380);
        dlgEnvSemi.setPreferredSize(dlgEnvSemi.getSize());
        dlgEnvSemi.setLocationRelativeTo(null);
        dlgEnvSemi.setAlwaysOnTop(true);
        dlgEnvSemi.setVisible(true);
    }
    
    //Método para realizar entrada de material y actualizar inventarios
    public void realizarEntradaEnvSemi () throws Exception
    {
        try 
        {
            if (Integer.parseInt(txtNoPiezasEnvSemi.getText()) > Integer.parseInt(txtNoPiezasActualesEnvSemi.getText()))
            {
                JOptionPane.showMessageDialog(dlgEnvSemi, "El numero de piezas debe ser menor o igual al número de piezas actuales");
            }
            else
            {
                int fila = tblInvCross.getSelectedRow();
                ics = new InventarioCrossSemiterminado();
                icsc = new InventarioCrossSemiterminadoCommands();

                ics.setIdInvPCross(Integer.parseInt(datosInvCross[fila][5]));
                ics.setNoPiezas(Integer.parseInt(txtNoPiezasEnvSemi.getText()));
                ics.setNoPiezasActuales(Integer.parseInt(txtNoPiezasActualesEnvSemi.getText()));

                icsc.agregarInvCrossSemi(ics);
                icc.actualizarNoPiezasActual(ics);
                actualizarTablaCross();
                dlgEnvSemi.setVisible(false);
                JOptionPane.showMessageDialog(null, "Entrada realizada correctamente");
            }
        } 
        catch (Exception e) 
        {
            dlgEnvSemi.setVisible(false);                
            JOptionPane.showMessageDialog(null, "Error de conexión");
        }
    }
    
    public void generarReporteEntradaCross()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteEntCross.jasper");
            
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
    
    public void generarReporteSalidaCross()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteSalCross.jasper");
            
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
    
    public void generarReporteInventarioCross()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteInvCross.jasper");
            
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
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dlgEnvSemi = new javax.swing.JDialog();
        jLabel14 = new javax.swing.JLabel();
        btnRealizarEntradaEnvSemi = new javax.swing.JButton();
        btnCancelarAgregarEnvSemi = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel53 = new javax.swing.JLabel();
        txtNoPiezasActualesEnvSemi = new javax.swing.JTextField();
        txtNoPartidaEnvSemi = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        txtNoPiezasEnvSemi = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txtTipoRecorteEnvSemi = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvCross = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnEnviarSemiterminado = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbTipoCuero = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNoPartida = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
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
        btnReporteEntrada3 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        btnReporteEntrada2 = new javax.swing.JButton();

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Agregar entrada semiterminado");

        btnRealizarEntradaEnvSemi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarEntradaEnvSemi.setText("Aceptar");
        btnRealizarEntradaEnvSemi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarEntradaEnvSemiActionPerformed(evt);
            }
        });

        btnCancelarAgregarEnvSemi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarAgregarEnvSemi.setText("Cancelar");
        btnCancelarAgregarEnvSemi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAgregarEnvSemiActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel31.setText("No. Partida:");

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel53.setText("No. Piezas Actuales:");

        txtNoPiezasActualesEnvSemi.setEditable(false);
        txtNoPiezasActualesEnvSemi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasActualesEnvSemi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoPiezasActualesEnvSemiActionPerformed(evt);
            }
        });
        txtNoPiezasActualesEnvSemi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasActualesEnvSemiKeyTyped(evt);
            }
        });

        txtNoPartidaEnvSemi.setEditable(false);
        txtNoPartidaEnvSemi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPartidaEnvSemi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaEnvSemiKeyTyped(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel54.setText("No. Piezas a enviar:");

        txtNoPiezasEnvSemi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasEnvSemi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasEnvSemiKeyTyped(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel55.setText("Tipo Recorte:");

        txtTipoRecorteEnvSemi.setEditable(false);
        txtTipoRecorteEnvSemi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTipoRecorteEnvSemi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTipoRecorteEnvSemiActionPerformed(evt);
            }
        });
        txtTipoRecorteEnvSemi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTipoRecorteEnvSemiKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout dlgEnvSemiLayout = new javax.swing.GroupLayout(dlgEnvSemi.getContentPane());
        dlgEnvSemi.getContentPane().setLayout(dlgEnvSemiLayout);
        dlgEnvSemiLayout.setHorizontalGroup(
            dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEnvSemiLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addComponent(jSeparator5)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgEnvSemiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRealizarEntradaEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelarAgregarEnvSemi)
                .addContainerGap())
            .addGroup(dlgEnvSemiLayout.createSequentialGroup()
                .addGroup(dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgEnvSemiLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dlgEnvSemiLayout.createSequentialGroup()
                                .addComponent(jLabel53)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNoPiezasActualesEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(dlgEnvSemiLayout.createSequentialGroup()
                                .addComponent(jLabel54)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNoPiezasEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgEnvSemiLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(dlgEnvSemiLayout.createSequentialGroup()
                                .addComponent(jLabel55)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTipoRecorteEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(dlgEnvSemiLayout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNoPartidaEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(2, 2, 2)))
                .addContainerGap(140, Short.MAX_VALUE))
        );
        dlgEnvSemiLayout.setVerticalGroup(
            dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEnvSemiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtNoPartidaEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txtTipoRecorteEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(txtNoPiezasActualesEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(txtNoPiezasEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntradaEnvSemi)
                    .addComponent(btnCancelarAgregarEnvSemi))
                .addContainerGap())
        );

        setBackground(new java.awt.Color(255, 255, 255));

        tblInvCross.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblInvCross.setModel(new javax.swing.table.DefaultTableModel(
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
        tblInvCross.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblInvCross);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnEnviarSemiterminado.setText("   ");
        jToolBar1.add(btnEnviarSemiterminado);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
        jButton1.setText("Enviar a Semiterminado");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jLabel12.setText("   ");
        jToolBar1.add(jLabel12);

        jLabel13.setText("   ");
        jToolBar1.add(jLabel13);

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

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("No. Partida");
        jToolBar1.add(jLabel7);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(227, 222, 222));
        jLabel9.setText("  ");
        jToolBar1.add(jLabel9);

        txtNoPartida.setMinimumSize(new java.awt.Dimension(60, 25));
        txtNoPartida.setName(""); // NOI18N
        txtNoPartida.setPreferredSize(new java.awt.Dimension(50, 25));
        txtNoPartida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoPartidaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaKeyTyped(evt);
            }
        });
        jToolBar1.add(txtNoPartida);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(227, 222, 222));
        jLabel11.setText("  ");
        jToolBar1.add(jLabel11);
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

    jToolBar2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar2.setFloatable(false);
    jToolBar2.setRollover(true);

    btnReporteEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteEntrada.setText("Reporte Entradas");
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

    btnReporteEntrada3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntrada3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteEntrada3.setText("Reporte Salidas");
    btnReporteEntrada3.setFocusable(false);
    btnReporteEntrada3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteEntrada3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteEntrada3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntrada3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntrada3ActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteEntrada3);

    jLabel51.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel51.setForeground(new java.awt.Color(227, 222, 222));
    jLabel51.setText("     ");
    jToolBar2.add(jLabel51);

    btnReporteEntrada2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntrada2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteEntrada2.setText("Reporte Inventario Cross");
    btnReporteEntrada2.setFocusable(false);
    btnReporteEntrada2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteEntrada2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntrada2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntrada2ActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteEntrada2);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane1)
        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
        .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
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
        actualizarTablaCross();
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

    private void cmbTipoCueroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoCueroActionPerformed
        actualizarTablaCross();
    }//GEN-LAST:event_cmbTipoCueroActionPerformed

    private void btnReporteEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradaActionPerformed
        actualizarTablaCross();
        generarReporteEntradaCross();
    }//GEN-LAST:event_btnReporteEntradaActionPerformed

    private void btnReporteEntrada2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada2ActionPerformed
        generarReporteInventarioCross();
    }//GEN-LAST:event_btnReporteEntrada2ActionPerformed

    private void btnReporteEntrada3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada3ActionPerformed
        generarReporteSalidaCross();
    }//GEN-LAST:event_btnReporteEntrada3ActionPerformed

    private void txtNoPartidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaKeyTyped
        validarNumerosEnteros(evt, txtNoPartida.getText());
    }//GEN-LAST:event_txtNoPartidaKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            abrirDialogoEnvSemi();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Inventario Cross","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnRealizarEntradaEnvSemiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaEnvSemiActionPerformed
        try
        {
            realizarEntradaEnvSemi();
        }
        catch (Exception ex)
        {
            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnRealizarEntradaEnvSemiActionPerformed

    private void btnCancelarAgregarEnvSemiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarEnvSemiActionPerformed
        dlgEnvSemi.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarEnvSemiActionPerformed

    private void txtNoPiezasActualesEnvSemiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasActualesEnvSemiKeyTyped
        char c;
        c=evt.getKeyChar();

        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtNoPiezasActualesEnvSemiKeyTyped

    private void txtNoPartidaEnvSemiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaEnvSemiKeyTyped
        char c;
        c=evt.getKeyChar();

        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtNoPartidaEnvSemiKeyTyped

    private void txtNoPiezasEnvSemiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasEnvSemiKeyTyped
        validarNumerosEnteros(evt, txtNoPiezasEnvSemi.getText());
    }//GEN-LAST:event_txtNoPiezasEnvSemiKeyTyped

    private void txtNoPiezasActualesEnvSemiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoPiezasActualesEnvSemiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPiezasActualesEnvSemiActionPerformed

    private void txtTipoRecorteEnvSemiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTipoRecorteEnvSemiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoRecorteEnvSemiActionPerformed

    private void txtTipoRecorteEnvSemiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTipoRecorteEnvSemiKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoRecorteEnvSemiKeyTyped

    private void txtNoPartidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            actualizarTablaCross();
        }
    }//GEN-LAST:event_txtNoPartidaKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarEntrada;
    private javax.swing.JButton btnCancelarAgregarEnvSemi;
    private javax.swing.JLabel btnEnviarSemiterminado;
    private javax.swing.JButton btnRealizarEntradaEnvSemi;
    private javax.swing.JButton btnReporteEntrada;
    private javax.swing.JButton btnReporteEntrada2;
    private javax.swing.JButton btnReporteEntrada3;
    private javax.swing.JComboBox cmbTipoCuero;
    private datechooser.beans.DateChooserCombo dcFecha1EntradaSemiterminado;
    private datechooser.beans.DateChooserCombo dcFecha2EntradaSemiterminado;
    private javax.swing.JDialog dlgEnvSemi;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JRadioButton jrFiltroFechasEntrada;
    private javax.swing.JLabel lbl;
    private javax.swing.JTable tblInvCross;
    private javax.swing.JTextField txtNoPartida;
    private javax.swing.JTextField txtNoPartidaEnvSemi;
    private javax.swing.JTextField txtNoPiezasActualesEnvSemi;
    private javax.swing.JTextField txtNoPiezasEnvSemi;
    private javax.swing.JTextField txtTipoRecorteEnvSemi;
    // End of variables declaration//GEN-END:variables
}
