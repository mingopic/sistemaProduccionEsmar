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
import Controlador.TipoCueroCommands;
import Controlador.TipoRecorteCommands;
import Modelo.ConfiguracionMerma;
import Modelo.InventarioCross;
import Modelo.InventarioCrossSemiterminado;
import Modelo.Partida;
import Modelo.Proveedor;
import Modelo.RangoPesoCuero;
import Modelo.RecepcionCuero;
import Modelo.TipoCuero;
import Modelo.TipoRecorte;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    ConexionBD conexion;
    TipoRecorte tr;
    TipoRecorteCommands trc;
    InventarioCross ic;
    InventarioCrossCommands icc;
    InventarioCrossSemiterminado ics;
    InventarioCrossSemiterminadoCommands icsc;
    Partida p;
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
        tr = new TipoRecorte();
        p = new Partida();
        
        jrFiltroFechasEntrada.setSelected(false);
        dcFecha1EntradaSemiterminado.setEnabled(false);
        dcFecha2EntradaSemiterminado.setEnabled(false);
        llenarComboTipoRecorte();
        actualizarTablaCross();
        
        for (int i = 0; i < FrmPrincipal.roles.length; i++)
        {
            if (FrmPrincipal.roles[i].equals("Cross") || FrmPrincipal.roles[i].equals("Sistemas"))
            {
                btnEnviarSemiterminado.setEnabled(true);
                break;
            }
        }
    }
    
    //método que llena los combobox del tipo de recorte en la base de datos
    public void llenarComboTipoRecorte() throws Exception
    {
        trc = new TipoRecorteCommands();
        String[][] tipoRecorte = trc.llenarComboboxTipoRecorte();
        
        int i=0;
        while (i<tipoRecorte.length)
        {
            cmbTipoRecorte.addItem(tipoRecorte[i][1]);
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
        if (cmbTipoRecorte.getSelectedItem().toString().equals("<Todos>"))
        {
            tr.setDescripcion("%%");
        }
        else
        {
            tr.setDescripcion(cmbTipoRecorte.getSelectedItem().toString());
        }
        
        if (txtNoPartida.getText().isEmpty())
        {
            p.setNoPartida(0);
        }
        else
        {
            int noPartida = Integer.parseInt(txtNoPartida.getText());
            p.setNoPartida(noPartida);
        }
        
        DefaultTableModel dtm = null;
        
        try {
            
            datosInvCross = icc.obtenerListaInvCross(ic,tr,p);
            
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
    
    //Metodo para inicializar los campos de dlgAgregar
    public void inicializarCamposEnvSemi() throws Exception
    {
        txtNoPiezasEnvSemi.setText("");
        
        int fila = tblInvCross.getSelectedRow();
        
        txtNoPartidaEnvSemi.setText(String.valueOf(tblInvCross.getValueAt(fila, 0)));
        txtTipoRecorteEnvSemi.setText(String.valueOf(tblInvCross.getValueAt(fila, 1)));
        txtNoPiezasActualesEnvSemi.setText(String.valueOf(tblInvCross.getValueAt(fila, 3)));
    }  
    
    //Método que abre el dialogo para enviar a semiterminado 
    public void abrirDialogoEnvSemi() throws Exception
    {
        inicializarCamposEnvSemi();
        
        dlgEnvSemi.setSize(340, 300);
        dlgEnvSemi.setPreferredSize(dlgEnvSemi.getSize());
        dlgEnvSemi.setLocationRelativeTo(null);
        dlgEnvSemi.setAlwaysOnTop(true);
        dlgEnvSemi.setVisible(true);
    }
    
    //Método para realizar entrada de material y actualizar inventarios
    public void realizarEntradaEnvSemi () throws Exception
    {
        if ( !txtNoPiezasEnvSemi.getText().isEmpty() && Integer.parseInt(txtNoPiezasEnvSemi.getText()) != 0)
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
                    ics.setNoPiezasActuales(Integer.parseInt(txtNoPiezasEnvSemi.getText()));

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
                JOptionPane.showMessageDialog(null, "Error de conexión", "Error",JOptionPane.ERROR_MESSAGE);
            }   
        }
        else
        {
            dlgEnvSemi.setVisible(false);
            JOptionPane.showMessageDialog(null, "Capture no. Piezas a enviar","Mensaje",JOptionPane.WARNING_MESSAGE);
            dlgEnvSemi.setVisible(true);
        }
    }
    
    public void generarReporteEntradaCross()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteEntCross.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoRecorte", tr.getDescripcion());
            parametros.put("fecha1", ic.getFecha());
            parametros.put("fecha2", ic.getFecha1());
            parametros.put("noPartida", p.getNoPartida());
            
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
            parametros.put("tipoRecorte", tr.getDescripcion());
            parametros.put("fecha", ic.getFecha());
            parametros.put("fecha1", ic.getFecha1());
            parametros.put("noPartida", p.getNoPartida());
            
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
        jPanel1 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        txtTipoRecorteEnvSemi = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txtNoPiezasActualesEnvSemi = new javax.swing.JTextField();
        txtNoPartidaEnvSemi = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        txtNoPiezasEnvSemi = new javax.swing.JTextField();
        btnRealizarEntradaEnvSemi = new javax.swing.JButton();
        btnCancelarAgregarEnvSemi = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvCross = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        EnviarSemiterminado = new javax.swing.JLabel();
        btnEnviarSemiterminado = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbTipoRecorte = new javax.swing.JComboBox();
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

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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
        jLabel14.setText("Enviar a Semiterminado");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel54)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNoPiezasEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(48, 48, 48)
                                            .addComponent(jLabel31))
                                        .addComponent(jLabel55, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtTipoRecorteEnvSemi)
                                        .addComponent(txtNoPartidaEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel53)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtNoPiezasActualesEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 57, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRealizarEntradaEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelarAgregarEnvSemi)))
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtNoPartidaEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTipoRecorteEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(txtNoPiezasActualesEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntradaEnvSemi)
                    .addComponent(btnCancelarAgregarEnvSemi))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEnvSemiLayout = new javax.swing.GroupLayout(dlgEnvSemi.getContentPane());
        dlgEnvSemi.getContentPane().setLayout(dlgEnvSemiLayout);
        dlgEnvSemiLayout.setHorizontalGroup(
            dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEnvSemiLayout.setVerticalGroup(
            dlgEnvSemiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        EnviarSemiterminado.setText("   ");
        jToolBar1.add(EnviarSemiterminado);

        btnEnviarSemiterminado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEnviarSemiterminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
        btnEnviarSemiterminado.setText("Enviar a Semiterminado");
        btnEnviarSemiterminado.setEnabled(false);
        btnEnviarSemiterminado.setFocusable(false);
        btnEnviarSemiterminado.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEnviarSemiterminado.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEnviarSemiterminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarSemiterminadoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEnviarSemiterminado);

        jLabel12.setText("   ");
        jToolBar1.add(jLabel12);

        jLabel13.setText("   ");
        jToolBar1.add(jLabel13);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Tipo Recorte:");
        jToolBar1.add(jLabel6);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(227, 222, 222));
        jLabel8.setText("  ");
        jToolBar1.add(jLabel8);

        cmbTipoRecorte.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
        cmbTipoRecorte.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbTipoRecorte.setPreferredSize(new java.awt.Dimension(120, 25));
        cmbTipoRecorte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoRecorteActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbTipoRecorte);

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
    btnReporteEntrada2.setText("Reporte Inventario Desvenado");
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

    private void cmbTipoRecorteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoRecorteActionPerformed
        actualizarTablaCross();
    }//GEN-LAST:event_cmbTipoRecorteActionPerformed

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

    private void btnEnviarSemiterminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarSemiterminadoActionPerformed
        try {
            int fila = tblInvCross.getSelectedRow();
            String piezas = (String.valueOf(tblInvCross.getValueAt(fila, 3)));
            int numPiezasActuales = Integer.parseInt(piezas);
            
            if (numPiezasActuales != 0)
            {
                abrirDialogoEnvSemi();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "El número de piezas actuales debe ser mayor a 0","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Inventario Cross","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEnviarSemiterminadoActionPerformed

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
    private javax.swing.JLabel EnviarSemiterminado;
    private javax.swing.JButton btnBuscarEntrada;
    private javax.swing.JButton btnCancelarAgregarEnvSemi;
    private javax.swing.JButton btnEnviarSemiterminado;
    private javax.swing.JButton btnRealizarEntradaEnvSemi;
    private javax.swing.JButton btnReporteEntrada;
    private javax.swing.JButton btnReporteEntrada2;
    private javax.swing.JButton btnReporteEntrada3;
    private javax.swing.JComboBox cmbTipoRecorte;
    private datechooser.beans.DateChooserCombo dcFecha1EntradaSemiterminado;
    private datechooser.beans.DateChooserCombo dcFecha2EntradaSemiterminado;
    private javax.swing.JDialog dlgEnvSemi;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
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
