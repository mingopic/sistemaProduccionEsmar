/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.ConexionBD;
import Controlador.ConfiguracionMermaCommands;
import Controlador.ProveedorCommands;
import Controlador.RangoPesoCueroCommands;
import Controlador.RecepcionCueroCommands;
import Controlador.TipoCueroCommands;
import Modelo.ConfiguracionMerma;
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
    String[][] proveedoresAgregar = null;
    String[][] tipoCueroAgregar = null;
    String[][] rangoPesoCuero = null;
    String[][] datosEntRecCuero = null;
    private final String imagen="/Imagenes/logo_esmar.png";
    
    DefaultTableModel dtms=new DefaultTableModel();
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "Provedor","Tipo Cuero","No. Camión","Total Piezas","Total Kg","Precio x Kg","Costo Camión","Fecha de Entrada"
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
        rc = new RecepcionCuero();
        rcc = new RecepcionCueroCommands();
        tc = new TipoCuero();
        tcc = new TipoCueroCommands();
        
        actualizarTablaRecepcionCuero();
        jrFiltroFechasEntrada.setSelected(false);
        dcFecha1EntradaSemiterminado.setEnabled(false);
        dcFecha2EntradaSemiterminado.setEnabled(false);
        llenarComboProveedores();
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
//        tcc = new TipoCueroCommands();
//        String[][] tipoCuero = tcc.llenarComboboxTipoCuero();
//        
//        int i=0;
//        while (i<tipoCuero.length)
//        {
//            cmbTipoCuero.addItem(tipoCuero[i][1]);
//            i++;
//        }
    }
    
    //Método para actualizar la tabla de las entradas de cuero por trabajar, se inicializa al llamar la clase
    public void actualizarTablaRecepcionCuero() 
    {
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
//                    rc.setFecha(fechaAux);
//                }
//            catch (Exception ex) 
//                {
//                    rc.setFecha("0");
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
////                    
////                    //obtiene día
//                    for (int i=0; i<2; i++)
//                    {
//                        fechaAux=fechaAux+fecha.charAt(i);
//                    }
//                            
//                    rc.setFecha1(fechaAux);
//                }
//            catch (Exception ex) 
//                {
//                    rc.setFecha1("0");
//                }
//        }
//        else
//        {
//            rc.setFecha("1900-01-01");
//            rc.setFecha1("2040-01-01");
//        }
//        
//        
//        //validamos si esta seleccionado algún proveedor para hacer filtro
//        if (cmbProveedor.getSelectedItem().toString().equals("<Todos>"))
//        {
//            rc.setProveedor("%%");
//        }
//        else
//        {
//            rc.setProveedor(cmbProveedor.getSelectedItem().toString());
//        }
//        
//        //validamos si esta seleccionado algún tipo de cuero para hacer filtro
//        if (cmbTipoCuero.getSelectedItem().toString().equals("<Todos>"))
//        {
//            tc.setDescripcion("%%");
//        }
//        else
//        {
//            tc.setDescripcion(cmbTipoCuero.getSelectedItem().toString());
//        }
//        
//        DefaultTableModel dtm = null;
//        
//        try {
//            
//            datosEntRecCuero = rcc.obtenerListaRecepcionCuero(rc,tc);
//            
//            dtm = new DefaultTableModel(datosEntRecCuero, cols){
//            public boolean isCellEditable(int row, int column) {
//            return false;
//            }
//            };
//            tblInvCross.setModel(dtm);
//            tblInvCross.getTableHeader().setReorderingAllowed(false);
//
//        } catch (Exception e) {
//           
//            e.printStackTrace();
//            
//            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
//        }
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
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEntradasCross = new javax.swing.JTable();
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
        jTextField1 = new javax.swing.JTextField();
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

        setBackground(new java.awt.Color(255, 255, 255));

        tblEntradasCross.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblEntradasCross.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEntradasCross.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblEntradasCross);

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

        jTextField1.setMinimumSize(new java.awt.Dimension(60, 25));
        jTextField1.setName(""); // NOI18N
        jTextField1.setPreferredSize(new java.awt.Dimension(50, 25));
        jToolBar1.add(jTextField1);

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

    private void cmbTipoCueroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoCueroActionPerformed
        actualizarTablaRecepcionCuero();
    }//GEN-LAST:event_cmbTipoCueroActionPerformed

    private void btnReporteEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradaActionPerformed
        actualizarTablaRecepcionCuero();
        generarReporteEntradaRecepcionCuero(rc, tc);
    }//GEN-LAST:event_btnReporteEntradaActionPerformed

    private void btnReporteEntrada2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReporteEntrada2ActionPerformed

    private void btnReporteEntrada3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReporteEntrada3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarEntrada;
    private javax.swing.JLabel btnEnviarSemiterminado;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnReporteEntrada;
    private javax.swing.JButton btnReporteEntrada2;
    private javax.swing.JButton btnReporteEntrada3;
    private javax.swing.JComboBox cmbTipoCuero;
    private datechooser.beans.DateChooserCombo dcFecha1EntradaSemiterminado;
    private datechooser.beans.DateChooserCombo dcFecha2EntradaSemiterminado;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JRadioButton jrFiltroFechasEntrada;
    private javax.swing.JLabel lbl;
    private javax.swing.JTable tblEntradasCross;
    // End of variables declaration//GEN-END:variables
}
