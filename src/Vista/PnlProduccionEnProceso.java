/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.ConexionBD;
import Controlador.FichaProdDetCommands;
import Controlador.FichaProduccionCommands;
import Controlador.PartidaCommands;
import Controlador.ProcesoCommands;
import Controlador.SubProcesoCommands;
import Controlador.TipoRecorteCommands;
import Modelo.FichaProduccion;
import Modelo.Partida;
import Modelo.Proceso;
import Modelo.TipoRecorte;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class PnlProduccionEnProceso extends javax.swing.JPanel {
    ConexionBD conexion;
    Proceso pr;
    ProcesoCommands prc;
    FichaProduccion fp;
    FichaProduccionCommands fpc;
    TipoRecorte tr;
    TipoRecorteCommands trc;
    Partida p;
    PartidaCommands pc;
    String[][] proceso = null;
    String[][] recorte = null;
    List<Partida> lstPartida;
    String[][] datosProduccionProceso = null;
    private final String imagen="/Imagenes/logo_esmar.png";
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "No. Ficha","No. Partida","Proceso","Tipo Recorte","No. Piezas","Kg","Costo Cuero","$ Cuero/Pza","Costo Insumos","$ Insumo/Kg","Tambor","Fecha"
    };
    
    
    public PnlProduccionEnProceso() throws Exception {
        initComponents();
        inicializar();
    }
    
    //Método que se realiza al inicializar el sistema, inicializa todas las clases a utilizar
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        fp = new FichaProduccion();
        pr = new Proceso();
        tr = new TipoRecorte();
        
        jrFiltroFechasEntrada.setSelected(false);
        dcFecha1EntradaProduccionProceso.setEnabled(false);
        dcFecha2EntradaProduccionProceso.setEnabled(false);
        
        llenarComboProcesos();
        llenarComboTipoRecorte();
        llenarComboAnio();
        actualizarTablaProduccionProceso();
    }
    
    public void llenarComboProcesos() throws Exception
    {
        prc = new ProcesoCommands();
        proceso = prc.llenarComboboxProcesos();
        
        int i=0;
        while (i<proceso.length)
        {
            cmbProceso.addItem(proceso[i][1]);
            i++;
        }
    }
    
    public void llenarComboTipoRecorte() throws Exception
    {
        trc = new TipoRecorteCommands();
        recorte = trc.llenarComboboxTipoRecorte();
        
        int i=0;
        while (i<recorte.length)
        {
            cmbTipoRecorte.addItem(recorte[i][1]);
            i++;
        }
    }
    
    public void llenarComboAnio() throws Exception
    {
        pc = new PartidaCommands();
        lstPartida = new ArrayList<>();
        
        lstPartida = pc.llenarComboboxAnio();
        
        int i=0;
        while (i < lstPartida.size())
        {
            cmbAnioPartida.addItem(lstPartida.get(i).getAnio());
            i++;
        }
    }
    
    //Método para actualizar la tabla de las entradas de producción en proceso, se inicializa al llamar la clase
    public void actualizarTablaProduccionProceso() 
    {
        //validamos si esta seleccionada lo opción de rango de fechas para tomar el valor seleccionado,
        //si no esta seleccionado se ponen automáticamente los valores 1900-01-01 y 2040-01-01
        if (jrFiltroFechasEntrada.isSelected())
        {
            try {
                    String fechaAux="";
                    String fecha=dcFecha1EntradaProduccionProceso.getText();
                    
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
                            
                    fp.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    fp.setFecha("0");
                }
            
            try {
                    String fechaAux="";
                    String fecha=dcFecha2EntradaProduccionProceso.getText();
                    
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
                            
                    fp.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    fp.setFecha1("0");
                }
        }
        else
        {
            fp.setFecha("1900-01-01");
            fp.setFecha1("2040-01-01");
        }
        
        if (cmbProceso.getSelectedItem().toString().equals("<Todos>"))
        {
            pr.setDescripcion("%%");
        }
        else
        {
            pr.setDescripcion(cmbProceso.getSelectedItem().toString());
        }
        
        if (cmbTipoRecorte.getSelectedItem().toString().equals("<Todos>"))
        {
            tr.setDescripcion("%%");
        }
        else
        {
            tr.setDescripcion(cmbTipoRecorte.getSelectedItem().toString());
        }
        
        DefaultTableModel dtm = null;
        
        try {
            
            datosProduccionProceso = fpc.obtenerListaProduccionProceso(fp, pr,tr);
            
            dtm = new DefaultTableModel(datosProduccionProceso, cols){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblProduccionProceso.setModel(dtm);
            tblProduccionProceso.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    public void generarReporteListaPartidasProdProc()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteListaParProdProc.jasper");

            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("proceso", pr.getDescripcion());
            parametros.put("recorte", tr.getDescripcion());
            parametros.put("fecha", fp.getFecha());
            parametros.put("fecha1", fp.getFecha1());
            
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
    
    public void generarReporteFichaProd(String idFicha, String noPartida, String subproceso, String tambor, String fecha)
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteFichaProd.jasper");
            InputStream subInputStream = this.getClass().getResourceAsStream("ReporteFichaProd_sub.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("Fecha", fecha);
            parametros.put("idFicha",idFicha);
            parametros.put("noPartida", noPartida);
            parametros.put("SubProceso", subproceso);
            parametros.put("Tambor", tambor);
            parametros.put("ruta_subreporte",  getClass().getResource("/Reportes/ReporteFichaProd_sub.jasper").openStream());
            
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
    
    public void generarReporteCostoPartida(int idProceso, int idTipoRecorte, int noPartida, String anio)
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/CostoPartida.jasper");

            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("idProceso", idProceso);
            parametros.put("idTipoRecorte", idTipoRecorte);
            parametros.put("noPartida", noPartida);
            parametros.put("anio", anio);
            
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
    
    private void validarNumerosEnteros(java.awt.event.KeyEvent evt, String textoCaja)
    {
        try {
            char c = evt.getKeyChar();
            
            if (c<'0' || c>'9' )
            {
                evt.consume();
            }
        } catch (Exception ex) {
            Logger.getLogger(PnlSemiterminado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generarReporteInventario()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/InvEnProceso.jasper");

            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("idProceso", 0);
            
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
    
    public void generarReporteBajasPartidaDet()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteBajasPartidaDet.jasper");
            
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

        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbProceso = new javax.swing.JComboBox<>();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jLabel11 = new javax.swing.JLabel();
        cmbTipoRecorte = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel27 = new javax.swing.JLabel();
        jrFiltroFechasEntrada = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        dcFecha1EntradaProduccionProceso = new datechooser.beans.DateChooserCombo();
        lbl = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        dcFecha2EntradaProduccionProceso = new datechooser.beans.DateChooserCombo();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnBuscarEntrada = new javax.swing.JButton();
        lbl1 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNoPartida = new javax.swing.JTextField();
        lbl2 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbAnioPartida = new javax.swing.JComboBox<>();
        jToolBar2 = new javax.swing.JToolBar();
        btnReporteFichaProd = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnReporteListaPartProdProc = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnReporteListaPartProdProc1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        btnReporteCostoPartida = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduccionProceso = new javax.swing.JTable();

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel4.setText("   ");
        jToolBar1.add(jLabel4);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Proceso");
        jToolBar1.add(jLabel2);

        jLabel3.setText("  ");
        jToolBar1.add(jLabel3);

        cmbProceso.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbProceso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Todos>" }));
        cmbProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProcesoActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbProceso);
        jToolBar1.add(jSeparator3);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Tipo Recorte ");
        jToolBar1.add(jLabel11);

        cmbTipoRecorte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbTipoRecorte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Todos>" }));
        cmbTipoRecorte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoRecorteActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbTipoRecorte);

        jLabel5.setText("   ");
        jToolBar1.add(jLabel5);
        jToolBar1.add(jSeparator2);

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/calendar.png"))); // NOI18N
        jToolBar1.add(jLabel27);

        jrFiltroFechasEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrFiltroFechasEntrada.setFocusable(false);
        jrFiltroFechasEntrada.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jrFiltroFechasEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jrFiltroFechasEntrada.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jrFiltroFechasEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jrFiltroFechasEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrFiltroFechasEntradaActionPerformed(evt);
            }
        });
        jToolBar1.add(jrFiltroFechasEntrada);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("De:");
        jToolBar1.add(jLabel6);

        dcFecha1EntradaProduccionProceso.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(187, 187, 187),
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
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dcFecha1EntradaProduccionProceso.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
    dcFecha1EntradaProduccionProceso.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
    try {
        dcFecha1EntradaProduccionProceso.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha1EntradaProduccionProceso.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar1.add(dcFecha1EntradaProduccionProceso);

    lbl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl.setForeground(new java.awt.Color(227, 222, 222));
    lbl.setText("   ");
    jToolBar1.add(lbl);

    jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel25.setText("Hasta:");
    jToolBar1.add(jLabel25);

    dcFecha2EntradaProduccionProceso.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(187, 187, 187),
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
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(187, 187, 187),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcFecha2EntradaProduccionProceso.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
try {
    dcFecha2EntradaProduccionProceso.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha2EntradaProduccionProceso.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar1.add(dcFecha2EntradaProduccionProceso);
    jToolBar1.add(jSeparator1);

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

    lbl1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl1.setForeground(new java.awt.Color(227, 222, 222));
    lbl1.setText("   ");
    jToolBar1.add(lbl1);

    jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel26.setText("No. Partida");
    jToolBar1.add(jLabel26);

    jLabel8.setText("  ");
    jToolBar1.add(jLabel8);

    txtNoPartida.setMinimumSize(new java.awt.Dimension(60, 25));
    txtNoPartida.setPreferredSize(new java.awt.Dimension(50, 25));
    txtNoPartida.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtNoPartidaKeyTyped(evt);
        }
    });
    jToolBar1.add(txtNoPartida);

    lbl2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl2.setForeground(new java.awt.Color(227, 222, 222));
    lbl2.setText("   ");
    jToolBar1.add(lbl2);

    jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel28.setText("Año");
    jToolBar1.add(jLabel28);

    jLabel9.setText("  ");
    jToolBar1.add(jLabel9);

    cmbAnioPartida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    cmbAnioPartida.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbAnioPartidaActionPerformed(evt);
        }
    });
    jToolBar1.add(cmbAnioPartida);

    jToolBar2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar2.setFloatable(false);
    jToolBar2.setRollover(true);

    btnReporteFichaProd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteFichaProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/report.png"))); // NOI18N
    btnReporteFichaProd.setText("Reporte de Ficha de Producción");
    btnReporteFichaProd.setFocusable(false);
    btnReporteFichaProd.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteFichaProd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteFichaProd.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteFichaProdActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteFichaProd);

    jLabel1.setText("   ");
    jToolBar2.add(jLabel1);

    btnReporteListaPartProdProc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteListaPartProdProc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/report.png"))); // NOI18N
    btnReporteListaPartProdProc.setText("Reporte de Listado de Fichas");
    btnReporteListaPartProdProc.setFocusable(false);
    btnReporteListaPartProdProc.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteListaPartProdProc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteListaPartProdProc.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteListaPartProdProcActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteListaPartProdProc);

    jLabel7.setText("   ");
    jToolBar2.add(jLabel7);

    btnReporteListaPartProdProc1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteListaPartProdProc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/report.png"))); // NOI18N
    btnReporteListaPartProdProc1.setText("Reporte de Inventario");
    btnReporteListaPartProdProc1.setFocusable(false);
    btnReporteListaPartProdProc1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteListaPartProdProc1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteListaPartProdProc1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteListaPartProdProc1ActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteListaPartProdProc1);

    jLabel10.setText("   ");
    jToolBar2.add(jLabel10);

    btnReporteCostoPartida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteCostoPartida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/report.png"))); // NOI18N
    btnReporteCostoPartida.setText("Reporte Costo Partida");
    btnReporteCostoPartida.setFocusable(false);
    btnReporteCostoPartida.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteCostoPartida.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteCostoPartida.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteCostoPartidaActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteCostoPartida);

    jLabel12.setText("   ");
    jToolBar2.add(jLabel12);

    jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/report.png"))); // NOI18N
    jButton1.setText("Reporte Piezas Eliminadas");
    jButton1.setFocusable(false);
    jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });
    jToolBar2.add(jButton1);

    tblProduccionProceso.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "No. Ficha", "No. Partida", "Proceso", "Tipo Recorte", "No. Piezas", "Kg", "Costo Cuero", "$ Cuero/Pza", "Costo Insumos", "$ Insumo/Kg", "Tambor", "Fecha"
        }
    ));
    tblProduccionProceso.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jScrollPane1.setViewportView(tblProduccionProceso);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jScrollPane1)
        .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1048, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(4, 4, 4)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    }// </editor-fold>//GEN-END:initComponents

    private void jrFiltroFechasEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrFiltroFechasEntradaActionPerformed
        if (dcFecha1EntradaProduccionProceso.isEnabled() && dcFecha2EntradaProduccionProceso.isEnabled())
        {
            dcFecha1EntradaProduccionProceso.setEnabled(false);
            dcFecha1EntradaProduccionProceso.setCurrent(null);
            dcFecha2EntradaProduccionProceso.setEnabled(false);
            dcFecha2EntradaProduccionProceso.setCurrent(null);
        }
        else
        {
            dcFecha1EntradaProduccionProceso.setEnabled(true);
            dcFecha2EntradaProduccionProceso.setEnabled(true);
        }
    }//GEN-LAST:event_jrFiltroFechasEntradaActionPerformed

    private void btnBuscarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEntradaActionPerformed
        actualizarTablaProduccionProceso();
    }//GEN-LAST:event_btnBuscarEntradaActionPerformed

    private void cmbProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProcesoActionPerformed
        actualizarTablaProduccionProceso();
    }//GEN-LAST:event_cmbProcesoActionPerformed

    private void btnReporteFichaProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteFichaProdActionPerformed
        if (tblProduccionProceso.getSelectedRow() != -1)
        {
            try 
            {
                int fila = tblProduccionProceso.getSelectedRow();
                FichaProdDetCommands fpdc = new FichaProdDetCommands();
                SubProcesoCommands spc= new SubProcesoCommands();
                List<String> lstPartidas = new ArrayList<>();
                String noPartida = "";
                
                String idFicha = tblProduccionProceso.getValueAt(fila, 0).toString();
                for (int i = 0; i < tblProduccionProceso.getRowCount(); i++)
                {
                    if (tblProduccionProceso.getValueAt(i, 0).toString().equals(idFicha))
                    {
                        if (lstPartidas.size() > 0)
                        {
                            if (!lstPartidas.contains(tblProduccionProceso.getValueAt(i, 1).toString()))
                            {
                                noPartida = noPartida + ", No. " + tblProduccionProceso.getValueAt(i, 1).toString();
                                lstPartidas.add(tblProduccionProceso.getValueAt(i, 1).toString());
                            }
                        }
                        else
                        {
                            noPartida = "No. " + tblProduccionProceso.getValueAt(i, 1).toString();
                            lstPartidas.add(tblProduccionProceso.getValueAt(i, 1).toString());
                        }
                    }
                }
                String subproceso = spc.obtenerSubProcesoXidFichaProd(Integer.parseInt(idFicha));
                String tambor = tblProduccionProceso.getValueAt(fila, 10).toString();
                String fecha = tblProduccionProceso.getValueAt(fila, 11).toString();
                        
                generarReporteFichaProd(idFicha, noPartida, subproceso, tambor, fecha);
            } catch (Exception ex) 
            {
                Logger.getLogger(PnlProduccionEnProceso.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnReporteFichaProdActionPerformed

    private void btnReporteListaPartProdProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteListaPartProdProcActionPerformed
        actualizarTablaProduccionProceso();
        generarReporteListaPartidasProdProc();
    }//GEN-LAST:event_btnReporteListaPartProdProcActionPerformed

    private void btnReporteListaPartProdProc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteListaPartProdProc1ActionPerformed
        generarReporteInventario();
    }//GEN-LAST:event_btnReporteListaPartProdProc1ActionPerformed

    private void txtNoPartidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaKeyTyped
         validarNumerosEnteros(evt, txtNoPartida.getText());
    }//GEN-LAST:event_txtNoPartidaKeyTyped

    private void btnReporteCostoPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteCostoPartidaActionPerformed
        if (txtNoPartida.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "Ingrese un numero de partida","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            int idProceso;
            int idTipoRecorte;
            int noPartida;
            String anio;

            if (cmbProceso.getSelectedItem().toString().equals("<Todos>"))
            {
                idProceso = 0;
            }
            else
            {
                idProceso = Integer.parseInt(proceso[cmbProceso.getSelectedIndex() - 1][0]);
            }

            if (cmbTipoRecorte.getSelectedItem().toString().equals("<Todos>"))
            {
                idTipoRecorte = 0;
            }
            else
            {
                idTipoRecorte = Integer.parseInt(recorte[cmbTipoRecorte.getSelectedIndex() - 1][0]);
            }

            if (txtNoPartida.getText().equals(""))
            {
                noPartida = 0;
            }
            else
            {
                noPartida = Integer.parseInt(txtNoPartida.getText());
            }


            anio = cmbAnioPartida.getSelectedItem().toString();

            generarReporteCostoPartida(idProceso, idTipoRecorte, noPartida, anio);
        }
    }//GEN-LAST:event_btnReporteCostoPartidaActionPerformed

    private void cmbTipoRecorteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoRecorteActionPerformed
        actualizarTablaProduccionProceso();
    }//GEN-LAST:event_cmbTipoRecorteActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        generarReporteBajasPartidaDet();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmbAnioPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAnioPartidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAnioPartidaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarEntrada;
    private javax.swing.JButton btnReporteCostoPartida;
    private javax.swing.JButton btnReporteFichaProd;
    private javax.swing.JButton btnReporteListaPartProdProc;
    private javax.swing.JButton btnReporteListaPartProdProc1;
    private javax.swing.JComboBox<String> cmbAnioPartida;
    private javax.swing.JComboBox<String> cmbProceso;
    private javax.swing.JComboBox<String> cmbTipoRecorte;
    private datechooser.beans.DateChooserCombo dcFecha1EntradaProduccionProceso;
    private datechooser.beans.DateChooserCombo dcFecha2EntradaProduccionProceso;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JRadioButton jrFiltroFechasEntrada;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl2;
    private javax.swing.JTable tblProduccionProceso;
    private javax.swing.JTextField txtNoPartida;
    // End of variables declaration//GEN-END:variables
}
