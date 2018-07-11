/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.CalibreCommands;
import Controlador.ConexionBD;
import Controlador.InventarioCrossCommands;
import Controlador.InventarioCrossSemiterminadoCommands;
import Controlador.InventarioSemiterminadoCommands;
import Controlador.SeleccionCommands;
import Controlador.TipoRecorteCommands;
import Modelo.Calibre;
import Modelo.InventarioCross;
import Modelo.InventarioCrossSemiterminado;
import Modelo.InventarioSemiterminado;
import Modelo.Partida;
import Modelo.Seleccion;
import Modelo.TipoRecorte;
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
public class PnlSemiterminado extends javax.swing.JPanel {
    ConexionBD conexion;
    TipoRecorte tr;
    TipoRecorteCommands trc;
    InventarioSemiterminado is;
    InventarioSemiterminadoCommands isc;
    Calibre c;
    CalibreCommands cc;
    Seleccion s;
    SeleccionCommands sc;
    InventarioCross ic;
    InventarioCrossCommands icc;
    InventarioCrossSemiterminado ics;
    InventarioCrossSemiterminadoCommands icsc;
    Partida p;
    String[][] datosSemiterminado = null;
    String[][] datos = null;
    String[][] calibres = null;
    String[][] selecciones = null;
    private final String imagen="/Imagenes/logo_esmar.png";
    
    DefaultTableModel dtms=new DefaultTableModel();
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "No. Partida","Tipo Recorte","No. Piezas Iniciales","No. Piezas Actuales","Peso","Peso prom. X pieza","Selección","Calibre","Fecha Entrada"
    };
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] colsInvSemi = new String[]
    {
        "No. Partida","Tipo Recorte","No. Piezas Iniciales","No. Piezas Actuales","Fecha Entrada"
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlSemiterminado() throws Exception {
        initComponents();
        inicializar();
    }
    
    
//    //Método que se invica al inicializar el sistema, inicializa todas las clases a utilizar
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        is = new InventarioSemiterminado();
        tr = new TipoRecorte();
        trc = new TipoRecorteCommands();
        ic = new InventarioCross();
        c = new Calibre();
        cc = new CalibreCommands();
        s = new Seleccion();
        sc = new SeleccionCommands();
        p = new Partida();
        
        jrFiltroFechasEntrada.setSelected(false);
        dcFecha1EntradaSemiterminado.setEnabled(false);
        dcFecha2EntradaSemiterminado.setEnabled(false);
        llenarComboTipoRecorte();
        llenarComboCalibre();
        llenarComboSeleccion();
        actualizarTablaSemiterminado();
    }
//    
//    
    //método que llena los combobox del calibre en la base de datos
    public void llenarComboCalibre() throws Exception
    {
        cc = new CalibreCommands();
        String[][] calibres = cc.llenarComboboxCalibre();
        
        int i=0;
        while (i<calibres.length)
        {
            cmbCalibre.addItem(calibres[i][1]);
            i++;
        }
    }
//    
//    //método que llena los combobox del tipo de recorte en la base de datos
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
    
    //método que llena los combobox de la selección en la base de datos
    public void llenarComboSeleccion() throws Exception
    {
        sc = new SeleccionCommands();
        String[][] seleccion = sc.llenarComboboxSeleccion();
        
        int i=0;
        while (i<seleccion.length)
        {
            cmbSeleccion.addItem(seleccion[i][1]);
            i++;
        }
    }
//    
    private void validarNumerosEnteros(java.awt.event.KeyEvent evt, String textoCaja)
    {
        try {
            char c = evt.getKeyChar();
            
            if (c<'0' || c>'9') 
            {
                evt.consume();
            }
        } catch (Exception ex) {
            Logger.getLogger(PnlSemiterminado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    
//    //Método para actualizar la tabla de las entradas de cuero por trabajar, se inicializa al llamar la clase
    public void actualizarTablaSemiterminado() 
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
                            
                    is.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    is.setFecha("0");
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
                            
                    is.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    is.setFecha1("0");
                }
        }
        else
        {
            is.setFecha("1900-01-01");
            is.setFecha1("2040-01-01");
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
        
        if (cmbCalibre.getSelectedItem().toString().equals("<Todos>"))
        {
            c.setDescripcion("%%");
        }
        else
        {
            c.setDescripcion(cmbCalibre.getSelectedItem().toString());
        }
        
        if (cmbSeleccion.getSelectedItem().toString().equals("<Todos>"))
        {
            s.setDescripcion("%%");
        }
        else
        {
            s.setDescripcion(cmbSeleccion.getSelectedItem().toString());
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
            
            datosSemiterminado = isc.obtenerListaInvSemiterminado(p,tr,c,s,is);
            
            dtm = new DefaultTableModel(datosSemiterminado, cols){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblSemiterminado.setModel(dtm);
            tblSemiterminado.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Metodo para inicializar los campos de dlgAgregar
    public void inicializarCamposAgregar() throws Exception
    {
        
        c = new Calibre();
        s = new Seleccion();
        txtNoPartidaAgregar.setText("");
        txtTipoRecorteAgregar.setText("");
        txtNoPiezasAgregar.setText("");
        txtKgTotalesAgregar.setText("");
        
        //Llenar comboBox con los tipos de calibre
        //----------------------------------------------------------------------
        cc=new CalibreCommands();
        cmbCalibreAgregar.removeAllItems();
        calibres=cc.llenarComboboxCalibre();
        int i=0;
        while (i<calibres.length)
        {
            cmbCalibreAgregar.addItem(calibres[i][1]);
            i++;
        }
        //----------------------------------------------------------------------
        
        //Llenar comboBox con los tipos de selección
        //----------------------------------------------------------------------
        sc = new SeleccionCommands();
        cmbSeleccionAgregar.removeAllItems();
        selecciones=sc.llenarComboboxSeleccion();
        int j=0;
        while (j<selecciones.length)
        {
            cmbSeleccionAgregar.addItem(selecciones[j][1]);
            j++;
        }
        //----------------------------------------------------------------------
    }
    
    //Método que abre el dialogo de agregar entrada de Semiterminad
    public void abrirDialogoAgregar() throws Exception
    {
        inicializarCamposAgregar();
        
        dlgAgregar.setSize(400, 400);
        dlgAgregar.setPreferredSize(dlgAgregar.getSize());
        dlgAgregar.setLocationRelativeTo(null);
        dlgAgregar.setAlwaysOnTop(true);
        dlgAgregar.setVisible(true);
    }
    
    //Método para actualizar la tabla de inventario cross semiterminado
    public void actualizarTablaInvCrossSemi() 
    {  
        ics = new InventarioCrossSemiterminado();
        icsc =new InventarioCrossSemiterminadoCommands();
        tr = new TipoRecorte();
        p = new Partida();
        
        ics.setFecha("1900-01-01");
        ics.setFecha1("2040-01-01");     
        tr.setDescripcion("%%");
        p.setNoPartida(0);
       
        DefaultTableModel dtm = null;
        
        try {
            
            datos = icsc.obtenerListaInvCrossSemi(ics,p,tr);
            
            dtm = new DefaultTableModel(datos, colsInvSemi)
            {
                public boolean isCellEditable(int row, int column) 
                {
                    return false;
                }
            };
            tblBuscarPartidaInvCrossSemi.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Método que abre el dialogo de buscar partida
    public void abrirDialogoBuscarPartida() throws Exception
    {   
        dlgAgregar.setVisible(false);
        actualizarTablaInvCrossSemi();
        
        dlgBuscar.setSize(600,320);
        dlgBuscar.setPreferredSize(dlgBuscar.getSize());
        dlgBuscar.setLocationRelativeTo(null);
        dlgBuscar.setAlwaysOnTop(true);
        dlgBuscar.setVisible(true);
    }
    
    private void validarNumeros(java.awt.event.KeyEvent evt, String textoCaja)
    {
        try {
            char c;
            c=evt.getKeyChar();    
            int punto=textoCaja.indexOf(".")+1;

            if (punto==0)
            {
                if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE && c!=KeyEvent.VK_PERIOD)
                {
                    getToolkit().beep();           
                    evt.consume();
                }
            }

            else
            {
                if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
                {
                    getToolkit().beep();           
                    evt.consume();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PnlSemiterminado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Método para validar que se selecciono un producto de la lista, se usa en dldBuscar
    public void SeleccionarEntrada() throws Exception
     {
        int renglonSeleccionado = tblBuscarPartidaInvCrossSemi.getSelectedRow();
        if (renglonSeleccionado!=-1) 
        {
            ics = new InventarioCrossSemiterminado();
            ic =new InventarioCross();
            tr = new TipoRecorte();
            
            ic.setIdPartida(Integer.parseInt(tblBuscarPartidaInvCrossSemi.getValueAt(renglonSeleccionado, 0).toString()));
            tr.setDescripcion(tblBuscarPartidaInvCrossSemi.getValueAt(renglonSeleccionado, 1).toString());
            ics.setIdInvCrossSemi(Integer.parseInt(datos[renglonSeleccionado][5]));
            ics.setNoPiezasActuales(Integer.parseInt(tblBuscarPartidaInvCrossSemi.getValueAt(renglonSeleccionado, 3).toString()));
            
            dlgBuscar.setVisible(false);
            
            abrirDialogoAgregar();
            
            txtNoPartidaAgregar.setText(String.valueOf(ic.getIdPartida()));
            txtTipoRecorteAgregar.setText(tr.getDescripcion());
            txtNoPiezasAgregar.setText(String.valueOf(ics.getNoPiezasActuales()));
        }
        else 
        {
            dlgBuscar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Seleccione una entrada de la lista", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgBuscar.setVisible(true);
        }
     }
    
    public void realizarEntradaSemiterminado()
    {
        if (!txtNoPartidaAgregar.getText().equals("") && !txtTipoRecorteAgregar.getText().equals("") && !txtNoPiezasAgregar.getText().equals("") && !txtKgTotalesAgregar.getText().equals(""))
        {
            if (Integer.parseInt(txtNoPiezasAgregar.getText()) >= 1)
            {
                if (Integer.parseInt(txtNoPiezasAgregar.getText()) <= ics.getNoPiezasActuales())
                {
                    try 
                    {
                        is.setIdInvCrossSemi(ics.getIdInvCrossSemi());
                        is.setIdCalibre(Integer.parseInt(calibres[cmbCalibreAgregar.getSelectedIndex()][0]));
                        is.setIdSeleccion(Integer.parseInt(selecciones[cmbSeleccionAgregar.getSelectedIndex()][0]));
                        is.setKgTotales(Double.parseDouble(txtKgTotalesAgregar.getText()));
                        is.setNoPiezas(ics.getNoPiezasActuales());
                        
                        isc.agregarInvSemiterminado(is);
                        icsc.actualizarNoPiezasActual(is);
                        dlgAgregar.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Entrada realizada con éxito");
                        actualizarTablaSemiterminado();
                    } 
                    catch (Exception e) 
                    {
                        System.out.println(e);
                        dlgAgregar.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Error de conexión en la base de datos", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
                        dlgAgregar.setVisible(true);
                    }
                }
                else
                {
                    dlgAgregar.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Cantidad en inventario insuficiente", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
                    dlgAgregar.setVisible(true);
                }
            }   
            else
            {
                dlgAgregar.setVisible(false);
                JOptionPane.showMessageDialog(null, "El número de piezas debe de ser mayor a 0", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
                dlgAgregar.setVisible(true);
            }
        }
        else
        {
            dlgAgregar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Llene todos los campos", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgAgregar.setVisible(true);
        }
    }
    
    public void generarReporteEntradaSemiterminado()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteEntSemi.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoRecorte", tr.getDescripcion());
            parametros.put("calibre", c.getDescripcion());
            parametros.put("seleccion", s.getDescripcion());
            parametros.put("fecha", is.getFecha());
            parametros.put("fecha1", is.getFecha1());
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
    
    public void generarReporteInventarioXTrabajar()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteInvCrossSemi.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoRecorte", tr.getDescripcion());
            
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
    
    public void generarReporteInventarioSemiterminado()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteInvSemi.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoRecorte", tr.getDescripcion());
            parametros.put("calibre", c.getDescripcion());
            parametros.put("seleccion", s.getDescripcion());
            
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
        dlgAgregar = new javax.swing.JDialog();
        jLabel13 = new javax.swing.JLabel();
        btnRealizarEntrada = new javax.swing.JButton();
        btnCancelarAgregar = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel52 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        txtNoPartidaAgregar = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        txtTipoRecorteAgregar = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        txtNoPiezasAgregar = new javax.swing.JTextField();
        cmbCalibreAgregar = new javax.swing.JComboBox();
        jLabel64 = new javax.swing.JLabel();
        cmbSeleccionAgregar = new javax.swing.JComboBox();
        jLabel65 = new javax.swing.JLabel();
        txtKgTotalesAgregar = new javax.swing.JTextField();
        dlgBuscar = new javax.swing.JDialog();
        jLabel19 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBuscarPartidaInvCrossSemi = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSemiterminado = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel12 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbTipoRecorte = new javax.swing.JComboBox();
        jLabel58 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cmbCalibre = new javax.swing.JComboBox();
        jLabel56 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        cmbSeleccion = new javax.swing.JComboBox();
        jLabel60 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNoPartida = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
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
        jLabel11 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        btnReporteEntrada = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        btnReporteEntrada3 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        btnReporteEntrada2 = new javax.swing.JButton();
        jLabel57 = new javax.swing.JLabel();
        btnReporteEntrada6 = new javax.swing.JButton();
        jToolBar3 = new javax.swing.JToolBar();
        btnEnviarSemiterminado1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

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

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Agregar entrada");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btnRealizarEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarEntrada.setText("Aceptar");
        btnRealizarEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarEntradaActionPerformed(evt);
            }
        });

        btnCancelarAgregar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarAgregar.setText("Cancelar");
        btnCancelarAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAgregarActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel32.setText("No. Partida:");

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel52.setText("Tipo de recorte:");

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel62.setText("Calibre:");

        txtNoPartidaAgregar.setEditable(false);
        txtNoPartidaAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPartidaAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaAgregarKeyTyped(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setText("Buscar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        txtTipoRecorteAgregar.setEditable(false);
        txtTipoRecorteAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTipoRecorteAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTipoRecorteAgregarKeyTyped(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel63.setText("No. Piezas:");

        txtNoPiezasAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasAgregarKeyTyped(evt);
            }
        });

        cmbCalibreAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel64.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel64.setText("Selección");

        cmbSeleccionAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel65.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel65.setText("Kg Totales:");

        txtKgTotalesAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtKgTotalesAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKgTotalesAgregarKeyTyped(evt);
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
            .addComponent(jSeparator6)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRealizarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelarAgregar)
                .addGap(12, 12, 12))
            .addGroup(dlgAgregarLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel63)
                    .addComponent(jLabel52)
                    .addComponent(jLabel32)
                    .addComponent(jLabel62)
                    .addComponent(jLabel64)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtKgTotalesAgregar)
                    .addComponent(cmbSeleccionAgregar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNoPiezasAgregar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNoPartidaAgregar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTipoRecorteAgregar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbCalibreAgregar, javax.swing.GroupLayout.Alignment.LEADING, 0, 122, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        dlgAgregarLayout.setVerticalGroup(
            dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgAgregarLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(txtNoPartidaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTipoRecorteAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52)))
                    .addGroup(dlgAgregarLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jButton3)))
                .addGap(11, 11, 11)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCalibreAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSeleccionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKgTotalesAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntrada)
                    .addComponent(btnCancelarAgregar))
                .addContainerGap())
        );

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Buscar partida");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        tblBuscarPartidaInvCrossSemi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblBuscarPartidaInvCrossSemi.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblBuscarPartidaInvCrossSemi);

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton4.setText("Seleccionar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton5.setText("Cancelar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dlgBuscarLayout = new javax.swing.GroupLayout(dlgBuscar.getContentPane());
        dlgBuscar.getContentPane().setLayout(dlgBuscarLayout);
        dlgBuscarLayout.setHorizontalGroup(
            dlgBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgBuscarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                .addGap(20, 20, 20))
            .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(dlgBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgBuscarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addContainerGap())
        );
        dlgBuscarLayout.setVerticalGroup(
            dlgBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(dlgBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        setBackground(new java.awt.Color(255, 255, 255));

        tblSemiterminado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblSemiterminado.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSemiterminado.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblSemiterminado);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel12.setText("   ");
        jToolBar1.add(jLabel12);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Tipo de Recorte:");
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

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(227, 222, 222));
        jLabel58.setText("   ");
        jToolBar1.add(jLabel58);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("Calibre:");
        jToolBar1.add(jLabel15);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(227, 222, 222));
        jLabel16.setText("  ");
        jToolBar1.add(jLabel16);

        cmbCalibre.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
        cmbCalibre.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbCalibre.setPreferredSize(new java.awt.Dimension(85, 25));
        cmbCalibre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCalibreActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbCalibre);

        jLabel56.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(227, 222, 222));
        jLabel56.setText("   ");
        jToolBar1.add(jLabel56);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Selección:");
        jToolBar1.add(jLabel17);

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(227, 222, 222));
        jLabel18.setText("  ");
        jToolBar1.add(jLabel18);

        cmbSeleccion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
        cmbSeleccion.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbSeleccion.setPreferredSize(new java.awt.Dimension(85, 25));
        cmbSeleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSeleccionActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbSeleccion);

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(227, 222, 222));
        jLabel60.setText("   ");
        jToolBar1.add(jLabel60);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("No. Partida");
        jToolBar1.add(jLabel7);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(227, 222, 222));
        jLabel9.setText("  ");
        jToolBar1.add(jLabel9);

        txtNoPartida.setMinimumSize(new java.awt.Dimension(60, 25));
        txtNoPartida.setName(""); // NOI18N
        txtNoPartida.setPreferredSize(new java.awt.Dimension(40, 25));
        txtNoPartida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoPartidaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaKeyTyped(evt);
            }
        });
        jToolBar1.add(txtNoPartida);

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(227, 222, 222));
        jLabel59.setText("  ");
        jToolBar1.add(jLabel59);
        jToolBar1.add(jSeparator1);

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
    lbl.setText("   ");
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

    jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel11.setForeground(new java.awt.Color(227, 222, 222));
    jLabel11.setText("  ");
    jToolBar1.add(jLabel11);

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
    btnReporteEntrada2.setText("Reporte Inventario x Trabajar");
    btnReporteEntrada2.setFocusable(false);
    btnReporteEntrada2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteEntrada2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntrada2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntrada2ActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteEntrada2);

    jLabel57.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel57.setForeground(new java.awt.Color(227, 222, 222));
    jLabel57.setText("     ");
    jToolBar2.add(jLabel57);

    btnReporteEntrada6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntrada6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteEntrada6.setText("Reporte Inventario Semiterminado");
    btnReporteEntrada6.setFocusable(false);
    btnReporteEntrada6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteEntrada6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteEntrada6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntrada6.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntrada6ActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteEntrada6);

    jToolBar3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar3.setFloatable(false);
    jToolBar3.setRollover(true);

    btnEnviarSemiterminado1.setText("   ");
    jToolBar3.add(btnEnviarSemiterminado1);

    jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
    jButton2.setText("Agregar Entrada");
    jButton2.setFocusable(false);
    jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });
    jToolBar3.add(jButton2);

    jLabel61.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel61.setForeground(new java.awt.Color(227, 222, 222));
    jLabel61.setText("   ");
    jToolBar3.add(jLabel61);

    jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
    jButton1.setText("Enviar a Terminado");
    jButton1.setFocusable(false);
    jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });
    jToolBar3.add(jButton1);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane1)
        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1223, Short.MAX_VALUE)
        .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 1223, Short.MAX_VALUE)
        .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 1223, Short.MAX_VALUE)
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
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
        actualizarTablaSemiterminado();
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
        actualizarTablaSemiterminado();
    }//GEN-LAST:event_cmbTipoRecorteActionPerformed

    private void btnReporteEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradaActionPerformed
        actualizarTablaSemiterminado();
        generarReporteEntradaSemiterminado();
    }//GEN-LAST:event_btnReporteEntradaActionPerformed

    private void btnReporteEntrada2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada2ActionPerformed
        generarReporteInventarioXTrabajar();
    }//GEN-LAST:event_btnReporteEntrada2ActionPerformed

    private void btnReporteEntrada3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada3ActionPerformed
//        generarReporteSalidaCross();
    }//GEN-LAST:event_btnReporteEntrada3ActionPerformed

    private void txtNoPartidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaKeyTyped
        validarNumerosEnteros(evt, txtNoPartida.getText());
    }//GEN-LAST:event_txtNoPartidaKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//        try {
//            abrirDialogoEnvSemi();
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Inventario Cross","Advertencia",JOptionPane.WARNING_MESSAGE);
//        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnRealizarEntradaEnvSemiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaEnvSemiActionPerformed
//        try
//        {
//            realizarEntradaEnvSemi();
//        }
//        catch (Exception ex)
//        {
//            Logger.getLogger(PnlSemiterminado.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }//GEN-LAST:event_btnRealizarEntradaEnvSemiActionPerformed

    private void btnCancelarAgregarEnvSemiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarEnvSemiActionPerformed
//        dlgEnvSemi.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarEnvSemiActionPerformed

    private void txtNoPiezasActualesEnvSemiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasActualesEnvSemiKeyTyped
//        char c;
//        c=evt.getKeyChar();
//
//        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
//        {
//            getToolkit().beep();
//            evt.consume();
//        }
    }//GEN-LAST:event_txtNoPiezasActualesEnvSemiKeyTyped

    private void txtNoPartidaEnvSemiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaEnvSemiKeyTyped
//        char c;
//        c=evt.getKeyChar();
//
//        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
//        {
//            getToolkit().beep();
//            evt.consume();
//        }
    }//GEN-LAST:event_txtNoPartidaEnvSemiKeyTyped

    private void txtNoPiezasEnvSemiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasEnvSemiKeyTyped
//        validarNumerosEnteros(evt, txtNoPiezasEnvSemi.getText());
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
            actualizarTablaSemiterminado();
        }
    }//GEN-LAST:event_txtNoPartidaKeyPressed

    private void btnReporteEntrada6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada6ActionPerformed
        generarReporteInventarioSemiterminado();
    }//GEN-LAST:event_btnReporteEntrada6ActionPerformed

    private void cmbCalibreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCalibreActionPerformed
        actualizarTablaSemiterminado();
    }//GEN-LAST:event_cmbCalibreActionPerformed

    private void cmbSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSeleccionActionPerformed
        actualizarTablaSemiterminado();
    }//GEN-LAST:event_cmbSeleccionActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            abrirDialogoAgregar();
        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnRealizarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaActionPerformed
        realizarEntradaSemiterminado();
    }//GEN-LAST:event_btnRealizarEntradaActionPerformed

    private void btnCancelarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarActionPerformed
        dlgAgregar.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarActionPerformed

    private void txtNoPartidaAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaAgregarKeyTyped

    }//GEN-LAST:event_txtNoPartidaAgregarKeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            abrirDialogoBuscarPartida();
        } catch (Exception ex) {
            Logger.getLogger(PnlSemiterminado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtTipoRecorteAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTipoRecorteAgregarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoRecorteAgregarKeyTyped

    private void txtNoPiezasAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasAgregarKeyTyped
        char c;
        c=evt.getKeyChar();

        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtNoPiezasAgregarKeyTyped

    private void txtKgTotalesAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgTotalesAgregarKeyTyped
        validarNumeros(evt, txtKgTotalesAgregar.getText());
    }//GEN-LAST:event_txtKgTotalesAgregarKeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            SeleccionarEntrada();
        } catch (Exception ex) {
            Logger.getLogger(PnlSemiterminado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        dlgBuscar.setVisible(false);
        dlgAgregar.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarEntrada;
    private javax.swing.JButton btnCancelarAgregar;
    private javax.swing.JButton btnCancelarAgregarEnvSemi;
    private javax.swing.JLabel btnEnviarSemiterminado1;
    private javax.swing.JButton btnRealizarEntrada;
    private javax.swing.JButton btnRealizarEntradaEnvSemi;
    private javax.swing.JButton btnReporteEntrada;
    private javax.swing.JButton btnReporteEntrada2;
    private javax.swing.JButton btnReporteEntrada3;
    private javax.swing.JButton btnReporteEntrada6;
    private javax.swing.JComboBox cmbCalibre;
    private javax.swing.JComboBox cmbCalibreAgregar;
    private javax.swing.JComboBox cmbSeleccion;
    private javax.swing.JComboBox cmbSeleccionAgregar;
    private javax.swing.JComboBox cmbTipoRecorte;
    private datechooser.beans.DateChooserCombo dcFecha1EntradaSemiterminado;
    private datechooser.beans.DateChooserCombo dcFecha2EntradaSemiterminado;
    private javax.swing.JDialog dlgAgregar;
    private javax.swing.JDialog dlgBuscar;
    private javax.swing.JDialog dlgEnvSemi;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JRadioButton jrFiltroFechasEntrada;
    private javax.swing.JLabel lbl;
    private javax.swing.JTable tblBuscarPartidaInvCrossSemi;
    private javax.swing.JTable tblSemiterminado;
    private javax.swing.JTextField txtKgTotalesAgregar;
    private javax.swing.JTextField txtNoPartida;
    private javax.swing.JTextField txtNoPartidaAgregar;
    private javax.swing.JTextField txtNoPartidaEnvSemi;
    private javax.swing.JTextField txtNoPiezasActualesEnvSemi;
    private javax.swing.JTextField txtNoPiezasAgregar;
    private javax.swing.JTextField txtNoPiezasEnvSemi;
    private javax.swing.JTextField txtTipoRecorteAgregar;
    private javax.swing.JTextField txtTipoRecorteEnvSemi;
    // End of variables declaration//GEN-END:variables
}
