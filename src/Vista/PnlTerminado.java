/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.BajasInventarioTerminadoCommands;
import Controlador.CalibreCommands;
import Controlador.ConexionBD;
import Controlador.InventarioCrossCommands;
import Controlador.InventarioCrossSemiterminadoCommands;
import Controlador.InventarioSalTerminadoCommands;
import Controlador.InventarioSemiterminadoCommands;
import Controlador.InventarioSemiterminadoTerminadoCommands;
import Controlador.InventarioTerminadoCommands;
import Controlador.SeleccionCommands;
import Controlador.TipoRecorteCommands;
import Modelo.BajasInventarioTerminado;
import Modelo.Calibre;
import Modelo.InventarioCross;
import Modelo.InventarioCrossSemiterminado;
import Modelo.InventarioSalTerminado;
import Modelo.InventarioSemiterminado;
import Modelo.InventarioSemiterminadoTerminado;
import Modelo.InventarioTerminado;
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
public class PnlTerminado extends javax.swing.JPanel {
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
    InventarioSemiterminadoTerminado ist;
    InventarioTerminado it;
    InventarioSemiterminadoTerminadoCommands istc;
    InventarioTerminadoCommands itc;
    InventarioSalTerminado isalt;
    InventarioSalTerminadoCommands isaltc;
    Partida p;
    BajasInventarioTerminado bit;
    BajasInventarioTerminadoCommands bitc;
    double promXPieza;
    String[][] datosTerminado = null;
    String[][] datos = null;
    String[][] calibres = null;
    String[][] selecciones = null;
    private final String imagen="/Imagenes/logo_esmar.png";
    
    DefaultTableModel dtms=new DefaultTableModel();
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "No. Partida","Tipo Recorte","No. Piezas","Peso","Peso prom. X pieza","Decimetros","Pies","Selección","Calibre","Fecha Entrada"
    };
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] colsInvTermi = new String[]
    {
        "No. Partida","Tipo Recorte","Calibre","Seleccion","No. Piezas","kgTotales","Fecha Entrada"
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlTerminado() throws Exception {
        initComponents();
        inicializar();
    }
    
    
//    //Método que se invica al inicializar el sistema, inicializa todas las clases a utilizar
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        it = new InventarioTerminado();
        tr = new TipoRecorte();
        trc = new TipoRecorteCommands();
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
        actualizarTablaTerminado();
        
        for (int i = 0; i < FrmPrincipal.roles.length; i++)
        {
            if (FrmPrincipal.roles[i].equals("Terminado") || FrmPrincipal.roles[i].equals("Sistemas"))
            {
                btnAgregarEntrada.setEnabled(true);
                btnEnviarTerminado.setEnabled(true);
                break;
            }
        }
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
            Logger.getLogger(PnlTerminado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    
//    //Método para actualizar la tabla de las entradas de cuero por trabajar, se inicializa al llamar la clase
    public void actualizarTablaTerminado() 
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
                            
                    it.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    it.setFecha("0");
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
                            
                    it.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    it.setFecha1("0");
                }
        }
        else
        {
            it.setFecha("1900-01-01");
            it.setFecha1("2040-01-01");
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
            
            datosTerminado = itc.obtenerListaInvTerminado(p,tr,c,s,it);
            
            dtm = new DefaultTableModel(datosTerminado, cols){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblTerminado.setModel(dtm);
            tblTerminado.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Metodo para inicializar los campos de dlgAgregar
    public void inicializarCamposAgregar() throws Exception
    {
        btnGroup.add(jrKg);
        btnGroup.add(jrArea);
        
        jrKg.setSelected(true);
        txtNoPartidaAgregar.setText("");
        txtTipoRecorteAgregar.setText("");
        txtNoPiezasAgregar.setText("");
        txtKgTotalesAgregar.setText("");
        txtDecimetrosAgregar.setText("");
        txtPiesCuadradosAgregar.setText("");
        
        txtDecimetrosAgregar.setEnabled(false);
        txtPiesCuadradosAgregar.setEnabled(false);
        
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
        
        dlgAgregar.setSize(370, 490);
        dlgAgregar.setPreferredSize(dlgAgregar.getSize());
        dlgAgregar.setLocationRelativeTo(null);
        dlgAgregar.setAlwaysOnTop(true);
        dlgAgregar.setVisible(true);
    }
    
    //Método para actualizar la tabla de inventario cross semiterminado
    public void actualizarTablaInvSemTer() 
    {  
        ist = new InventarioSemiterminadoTerminado();
        istc =new InventarioSemiterminadoTerminadoCommands();
        tr = new TipoRecorte();
        c = new Calibre();
        s = new Seleccion();
        p = new Partida();
        
        ist.setFecha("1900-01-01");
        ist.setFecha1("2040-01-01");     
        tr.setDescripcion("%%");
        c.setDescripcion("%%");
        s.setDescripcion("%%");
        p.setNoPartida(0);
       
        DefaultTableModel dtm = null;
        
        try {
            
            datos = istc.obtenerListaInvSemTer(ist,p,tr,c,s);
            
            dtm = new DefaultTableModel(datos, colsInvTermi)
            {
                public boolean isCellEditable(int row, int column) 
                {
                    return false;
                }
            };
            tblBuscarPartidaInvSemTer.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Método que abre el dialogo de buscar partida
    public void abrirDialogoBuscarPartida() throws Exception
    {   
        dlgAgregar.setVisible(false);
        actualizarTablaInvSemTer();
        
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
            Logger.getLogger(PnlTerminado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Método para validar que se selecciono un producto de la lista, se usa en dldBuscar
    public void SeleccionarEntrada() throws Exception
     {
        int renglonSeleccionado = tblBuscarPartidaInvSemTer.getSelectedRow();
        if (renglonSeleccionado!=-1) 
        {
            ist = new InventarioSemiterminadoTerminado();
            is =new InventarioSemiterminado();
            tr = new TipoRecorte();
            c = new Calibre();
            s = new Seleccion();
            
            is.setNoPartida(Integer.parseInt(tblBuscarPartidaInvSemTer.getValueAt(renglonSeleccionado, 0).toString()));
            tr.setDescripcion(tblBuscarPartidaInvSemTer.getValueAt(renglonSeleccionado, 1).toString());
            c.setDescripcion(tblBuscarPartidaInvSemTer.getValueAt(renglonSeleccionado, 2).toString());
            s.setDescripcion(tblBuscarPartidaInvSemTer.getValueAt(renglonSeleccionado, 3).toString());
            ist.setNoPiezasActuales(Integer.parseInt(tblBuscarPartidaInvSemTer.getValueAt(renglonSeleccionado, 4).toString()));
            ist.setIdInvSemTer(Integer.parseInt(datos[renglonSeleccionado][8]));
            
            promXPieza = (Double.parseDouble(tblBuscarPartidaInvSemTer.getValueAt(renglonSeleccionado, 5).toString())) / (Integer.parseInt(tblBuscarPartidaInvSemTer.getValueAt(renglonSeleccionado, 4).toString()));
            double kgTotales = promXPieza * ist.getNoPiezasActuales();
            
            dlgBuscar.setVisible(false);
            
            abrirDialogoAgregar();
            
            txtNoPartidaAgregar.setText(String.valueOf(is.getNoPartida()));
            txtTipoRecorteAgregar.setText(tr.getDescripcion());
            cmbCalibreAgregar.setSelectedItem(c.getDescripcion());
            cmbSeleccionAgregar.setSelectedItem(s.getDescripcion());
            txtNoPiezasAgregar.setText(String.valueOf(ist.getNoPiezasActuales()));      
            txtKgTotalesAgregar.setText(String.valueOf(kgTotales));
        }
        else 
        {
            dlgBuscar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Seleccione una entrada de la lista", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgBuscar.setVisible(true);
        }
     }
    
    public void realizarEntradaTerminado()
    {
        if (!txtNoPartidaAgregar.getText().equals("") && !txtTipoRecorteAgregar.getText().equals("") && !txtNoPiezasAgregar.getText().equals(""))
        {
            if (Integer.parseInt(txtNoPiezasAgregar.getText()) >= 1)
            {
                if (Integer.parseInt(txtNoPiezasAgregar.getText()) <= ist.getNoPiezasActuales())
                {
                    try 
                    {
                        it.setIdInvSemTer(ist.getIdInvSemTer());
                        it.setIdCalibre(Integer.parseInt(calibres[cmbCalibreAgregar.getSelectedIndex()][0]));
                        it.setIdSeleccion(Integer.parseInt(selecciones[cmbSeleccionAgregar.getSelectedIndex()][0]));
                        it.setNoPiezas(Integer.parseInt(txtNoPiezasAgregar.getText()));
                        
                        if (jrKg.isSelected())
                        {
                            it.setKgTotales(Double.parseDouble(txtKgTotalesAgregar.getText()));
                            
                            it.setDecimetros(0);
                            it.setPies(0);
                        }
                        else if (jrArea.isSelected())
                        {
                            it.setDecimetros(Double.parseDouble(txtDecimetrosAgregar.getText()));
                            it.setPies(Double.parseDouble(txtPiesCuadradosAgregar.getText()));
                            
                            it.setKgTotales(0);
                        }
                        
                        itc.agregarInvTerminado(it);
                        istc.actualizarNoPiezasActual(it);
                        dlgAgregar.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Entrada realizada con éxito");
                        actualizarTablaTerminado();
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
    
    public void generarReporteEntradaTerminado()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteEntTermi.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoRecorte", tr.getDescripcion());
            parametros.put("calibre", c.getDescripcion());
            parametros.put("seleccion", s.getDescripcion());
            parametros.put("fecha", it.getFecha());
            parametros.put("fecha1", it.getFecha1());
            parametros.put("noPartida", p.getNoPartida());
            parametros.put("accion", 1);
            
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
    
    public void generarReporteSalidaTerminado()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteSalTermi.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoRecorte", tr.getDescripcion());
            parametros.put("calibre", c.getDescripcion());
            parametros.put("seleccion", s.getDescripcion());
            parametros.put("fecha", it.getFecha());
            parametros.put("fecha1", it.getFecha1());
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
            URL path = this.getClass().getResource("/Reportes/ReporteInvSemTer.jasper");
            
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
    
    public void generarReporteInventarioTerminado()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/InvTerminado.jasper");
            
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
    
    //Metodo para inicializar los campos de dlgEnvTermi
    public void inicializarCamposEnvSal() throws Exception
    {
        btnGroup1.add(jrkgEnvSal);
        btnGroup1.add(jrAreaEnvSal);
        
        txtNoPiezasEnvSal.setText("");
        txtKgTotalesEnvSal.setText("");
        txtDecimetrosEnvSal.setText("");
        txtPiesEnvSal.setText("");
        jrkgEnvSal.setSelected(true);
        txtDecimetrosEnvSal.setEnabled(false);
        txtPiesEnvSal.setEnabled(false);
        
        int fila = tblTerminado.getSelectedRow();
        
        //Llenar comboBox con los tipos de calibre
        //----------------------------------------------------------------------
        cc=new CalibreCommands();
        cmbCalibreEnvSal.removeAllItems();
        calibres=cc.llenarComboboxCalibre();
        int i=0;
        while (i<calibres.length)
        {
            cmbCalibreEnvSal.addItem(calibres[i][1]);
            i++;
        }
        //----------------------------------------------------------------------
        
        //Llenar comboBox con los tipos de selección
        //----------------------------------------------------------------------
        sc = new SeleccionCommands();
        cmbSeleccionEnvSal.removeAllItems();
        selecciones=sc.llenarComboboxSeleccion();
        int j=0;
        while (j<selecciones.length)
        {
            cmbSeleccionEnvSal.addItem(selecciones[j][1]);
            j++;
        }
        
        txtNoPartidaEnvSal.setText(String.valueOf(tblTerminado.getValueAt(fila, 0)));
        txtTipoRecorteEnvSal.setText(String.valueOf(tblTerminado.getValueAt(fila, 1)));
        cmbCalibreEnvSal.setSelectedItem(String.valueOf(tblTerminado.getValueAt(fila, 8)));
        cmbSeleccionEnvSal.setSelectedItem(String.valueOf(tblTerminado.getValueAt(fila, 7)));
        txtNoPiezasActualesEnvSal.setText(String.valueOf(tblTerminado.getValueAt(fila, 2)));
    }
    
    //Método que abre el dialogo para enviar a terminado 
    public void abrirDialogoEnvSal() throws Exception
    {
        
        inicializarCamposEnvSal();
        
        dlgEnvSal.setSize(360, 540);
        dlgEnvSal.setPreferredSize(dlgEnvSal.getSize());
        dlgEnvSal.setLocationRelativeTo(null);
        dlgEnvSal.setAlwaysOnTop(true);
        dlgEnvSal.setVisible(true);
    }
    
    //Método para realizar salida de material y actualizar inventarios
    public void realizarEntradaEnvSal () throws Exception
    {
        if ( !txtNoPiezasEnvSal.getText().isEmpty() && Integer.parseInt(txtNoPiezasEnvSal.getText()) != 0)
        {
            try 
            {
                if (Integer.parseInt(txtNoPiezasEnvSal.getText()) > Integer.parseInt(txtNoPiezasActualesEnvSal.getText()))
                {
                    JOptionPane.showMessageDialog(dlgEnvSal, "El numero de piezas debe ser menor o igual al número de piezas actuales");
                }
                else
                {
                    int fila = tblTerminado.getSelectedRow();
                    isalt = new InventarioSalTerminado();
                    isaltc = new InventarioSalTerminadoCommands();

                    isalt.setIdInvTerminado(Integer.parseInt(datosTerminado[fila][14]));
                    isalt.setNoPiezas(Integer.parseInt(txtNoPiezasEnvSal.getText()));
                    
                    if (jrKg.isSelected())
                    {
                        isalt.setKg(Double.parseDouble(txtKgTotalesEnvSal.getText()));

                        isalt.setDecimetros(0);
                        isalt.setPies(0);
                    }
                    else if (jrArea.isSelected())
                    {
                        isalt.setDecimetros(Double.parseDouble(txtDecimetrosEnvSal.getText()));
                        isalt.setPies(Double.parseDouble(txtPiesEnvSal.getText()));

                        isalt.setKg(0);
                    }

                    isaltc.agregarInvSalTer(isalt);
                    itc.actualizarNoPiezasActual(isalt);
                    actualizarTablaTerminado();
                    dlgEnvSal.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Salida realizada correctamente");
                }
            } 
            catch (Exception e) 
            {
                dlgEnvSal.setVisible(false);                
                JOptionPane.showMessageDialog(null, "Error de conexión", "Error",JOptionPane.ERROR_MESSAGE);
            }   
        }
        else
        {
            dlgEnvSal.setVisible(false);
            JOptionPane.showMessageDialog(null, "Capture no. Piezas a enviar mayores a 0","Mensaje",JOptionPane.WARNING_MESSAGE);
            dlgEnvSal.setVisible(true);
        }
    }
    
    public void calcKgTotales()
    {
        try
        {
            double kgTotales = promXPieza * Integer.parseInt(txtNoPiezasAgregar.getText());
            txtKgTotalesAgregar.setText(String.valueOf(kgTotales));
        }
        catch (Exception e)
        {
            txtKgTotalesAgregar.setText("0");
        }
    }
    
    public void calcKgTotalesEnvSal()
    {
        try
        {
            int fila = tblTerminado.getSelectedRow();
            
            double promXPieza = Double.parseDouble(datosTerminado[fila][5]);
            double kgTotales = promXPieza * Integer.parseInt(txtNoPiezasEnvSal.getText());
            txtKgTotalesEnvSal.setText(String.valueOf(kgTotales));
        }
        catch (Exception e)
        {
            txtKgTotalesEnvSal.setText("0");
        }
    }
    
    public void calcPiesTotalesAgregar()
    {
        try
        {
            double decimetros = Double.parseDouble(txtDecimetrosAgregar.getText());
            double pies = decimetros * 0.328084;
            txtPiesCuadradosAgregar.setText(String.valueOf(pies));
        }
        catch (Exception e)
        {
            txtPiesCuadradosAgregar.setText("");
        }
    }
    
    public void calcDecimetrosTotalesAgregar()
    {
        try
        {
            double pies = Double.parseDouble(txtPiesCuadradosAgregar.getText());
            double decimetros = pies * 3.048;
            txtDecimetrosAgregar.setText(String.valueOf(decimetros));
        }
        catch (Exception e)
        {
            txtDecimetrosAgregar.setText("");
        }
    }
    
    public void calcPiesTotalesEnvSal()
    {
        try
        {
            double decimetros = Double.parseDouble(txtDecimetrosEnvSal.getText());
            double pies = decimetros * 0.328084;
            txtPiesEnvSal.setText(String.valueOf(pies));
        }
        catch (Exception e)
        {
            txtPiesEnvSal.setText("");
        }
    }
    
    public void calcDecimetrosTotalesEnvSal()
    {
        try
        {
            double pies = Double.parseDouble(txtPiesEnvSal.getText());
            double decimetros = pies * 3.048;
            txtDecimetrosEnvSal.setText(String.valueOf(decimetros));
        }
        catch (Exception e)
        {
            txtDecimetrosEnvSal.setText("");
        }
    }
    
    //Metodo para inicializar los campos de dlgEliPzaInvTerminado
    public void inicializarCamposEliPzaInvTerminado() throws Exception
    {
        txtNoPiezasEliminar.setText("");
        txtrMotivo.setText("");
        
        int fila = tblTerminado.getSelectedRow();
        
        txtNoPartida1.setText(String.valueOf(tblTerminado.getValueAt(fila, 0)));
        txtTipoRecorte.setText(String.valueOf(tblTerminado.getValueAt(fila, 1)));
        txtCalibre.setText(String.valueOf(tblTerminado.getValueAt(fila, 8)));
        txtSeleccion.setText(String.valueOf(tblTerminado.getValueAt(fila, 7)));
        txtNoPiezasActuales.setText(String.valueOf(tblTerminado.getValueAt(fila, 2)));
    }
    
    //Método que abre el dialogo para eliminar piezas del inventario terminado
    public void abrirDialogoEliPzaInvTerminado() throws Exception
    {
        inicializarCamposEliPzaInvTerminado();
        
        dlgEliPzaInvTerminado.setSize(430, 490);
        dlgEliPzaInvTerminado.setPreferredSize(dlgEliPzaInvTerminado.getSize());
        dlgEliPzaInvTerminado.setLocationRelativeTo(null);
        dlgEliPzaInvTerminado.setAlwaysOnTop(true);
        dlgEliPzaInvTerminado.setVisible(true);
    }
    
     //Método para realizar entrada de material y actualizar inventarios
    public void eliminarPiezasInvTerminado() throws Exception
    {
        if ( !txtNoPiezasEliminar.getText().isEmpty() && Integer.parseInt(txtNoPiezasEliminar.getText()) != 0)
        {
            try 
            {
                if (Integer.parseInt(txtNoPiezasEliminar.getText()) > Integer.parseInt(txtNoPiezasActuales.getText()))
                {
                    JOptionPane.showMessageDialog(dlgEliPzaInvTerminado, "El numero de piezas debe ser menor o igual al número de piezas actuales");
                }
                else
                {
                    int fila = tblTerminado.getSelectedRow();
                    bit = new BajasInventarioTerminado();
                    bitc = new BajasInventarioTerminadoCommands();
                    
                    if (Double.parseDouble(tblTerminado.getValueAt(fila, 3).toString()) > 0)
                    {
                        double promKg = Double.parseDouble(tblTerminado.getValueAt(fila, 4).toString());
                        double kg = promKg * Integer.parseInt(txtNoPiezasEliminar.getText());
                        
                        if (kg > Double.parseDouble(datosTerminado[fila][11]))
                        {
                            kg = Double.parseDouble(datosTerminado[fila][11]);
                        }
                        
                        bit.setKg(kg);
                        bit.setPies(0);
                        bit.setDecimetros(0);
                    }
                    else
                    {
                        double promPies = Double.parseDouble(datosTerminado[fila][13]) / Double.parseDouble(datosTerminado[fila][10]);
                        double pies = promPies * Integer.parseInt(txtNoPiezasEliminar.getText());
                        
                        double promDecimetros = Double.parseDouble(datosTerminado[fila][12]) / Double.parseDouble(datosTerminado[fila][10]);
                        double decimetros = promDecimetros * Integer.parseInt(txtNoPiezasEliminar.getText());
                        
                        if (pies > Double.parseDouble(datosTerminado[fila][13]))
                        {
                            pies = Double.parseDouble(datosTerminado[fila][13]);
                        }
                        
                        if (decimetros > Double.parseDouble(datosTerminado[fila][12]))
                        {
                            decimetros = Double.parseDouble(datosTerminado[fila][12]);
                        }
                        
                        bit.setKg(0);
                        bit.setPies(pies);
                        bit.setDecimetros(decimetros);
                    }

                    bit.setIdInvTerminado(Integer.parseInt(datosTerminado[fila][14]));
                    bit.setNoPiezas(Integer.parseInt(txtNoPiezasEliminar.getText()));
                    bit.setMotivo(txtrMotivo.getText());

                    bitc.agregarBajaInvTerminado(bit);
                    itc.actualizarNoPiezasBaja(bit);
                    actualizarTablaTerminado();
                    dlgEliPzaInvTerminado.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Baja realizada correctamente");
                }
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                dlgEliPzaInvTerminado.setVisible(false);                
                JOptionPane.showMessageDialog(null, "Error de conexión", "Error",JOptionPane.ERROR_MESSAGE);
            }   
        }
        else
        {
            dlgEliPzaInvTerminado.setVisible(false);
            JOptionPane.showMessageDialog(null, "Capture no. Piezas a eliminar","Mensaje",JOptionPane.WARNING_MESSAGE);
            dlgEliPzaInvTerminado.setVisible(true);
        }
    }
    
    public void generarReporteBajasInvTerminado()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteBajasInvTerminado.jasper");
            
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

        dlgAgregar = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        btnRealizarEntrada = new javax.swing.JButton();
        btnCancelarAgregar = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
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
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jrKg = new javax.swing.JRadioButton();
        jrArea = new javax.swing.JRadioButton();
        txtDecimetrosAgregar = new javax.swing.JTextField();
        txtPiesCuadradosAgregar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dlgBuscar = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBuscarPartidaInvSemTer = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        dlgEnvSal = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        btnRealizarEntrada1 = new javax.swing.JButton();
        btnCancelarAgregar1 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        txtNoPartidaEnvSal = new javax.swing.JTextField();
        txtTipoRecorteEnvSal = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        txtNoPiezasActualesEnvSal = new javax.swing.JTextField();
        cmbCalibreEnvSal = new javax.swing.JComboBox();
        jLabel69 = new javax.swing.JLabel();
        cmbSeleccionEnvSal = new javax.swing.JComboBox();
        jLabel70 = new javax.swing.JLabel();
        txtKgTotalesEnvSal = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        txtNoPiezasEnvSal = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtDecimetrosEnvSal = new javax.swing.JTextField();
        txtPiesEnvSal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jrkgEnvSal = new javax.swing.JRadioButton();
        jrAreaEnvSal = new javax.swing.JRadioButton();
        btnGroup = new javax.swing.ButtonGroup();
        btnGroup1 = new javax.swing.ButtonGroup();
        dlgEliPzaInvTerminado = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        txtCalibre = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        txtSeleccion = new javax.swing.JTextField();
        btnRealizarEntradaEnvSemi2 = new javax.swing.JButton();
        btnCancelarAgregarEnvSemi2 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtNoPartida1 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtTipoRecorte = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtrMotivo = new javax.swing.JTextArea();
        txtNoPiezasActuales = new javax.swing.JTextField();
        txtNoPiezasEliminar = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTerminado = new javax.swing.JTable();
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
        jLabel72 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jToolBar3 = new javax.swing.JToolBar();
        lblEnviarTerminado = new javax.swing.JLabel();
        btnAgregarEntrada = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        btnEnviarTerminado = new javax.swing.JButton();
        jLabel73 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/magnifier.png"))); // NOI18N
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoPiezasAgregarKeyReleased(evt);
            }
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

        jPanel2.setBackground(new java.awt.Color(0, 204, 51));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
        jLabel13.setText("Agregar entrada");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jrKg.setText("Kilogramos");
        jrKg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrKgActionPerformed(evt);
            }
        });

        jrArea.setText("Área");
        jrArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrAreaActionPerformed(evt);
            }
        });

        txtDecimetrosAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDecimetrosAgregarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDecimetrosAgregarKeyTyped(evt);
            }
        });

        txtPiesCuadradosAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPiesCuadradosAgregarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPiesCuadradosAgregarKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Decimetros:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Pies:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnRealizarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancelarAgregar)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jrKg)
                                .addGap(61, 61, 61)
                                .addComponent(jrArea)
                                .addGap(82, 82, 82))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel63)
                            .addComponent(jLabel52)
                            .addComponent(jLabel32)
                            .addComponent(jLabel62)
                            .addComponent(jLabel64)
                            .addComponent(jLabel65)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPiesCuadradosAgregar)
                            .addComponent(txtDecimetrosAgregar)
                            .addComponent(txtKgTotalesAgregar, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbSeleccionAgregar, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNoPiezasAgregar)
                            .addComponent(txtNoPartidaAgregar)
                            .addComponent(txtTipoRecorteAgregar)
                            .addComponent(cmbCalibreAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrKg)
                    .addComponent(jrArea))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(txtNoPartidaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTipoRecorteAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jButton3)))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCalibreAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSeleccionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKgTotalesAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDecimetrosAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPiesCuadradosAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntrada)
                    .addComponent(btnCancelarAgregar))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgAgregarLayout = new javax.swing.GroupLayout(dlgAgregar.getContentPane());
        dlgAgregar.getContentPane().setLayout(dlgAgregarLayout);
        dlgAgregarLayout.setHorizontalGroup(
            dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgAgregarLayout.setVerticalGroup(
            dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tblBuscarPartidaInvSemTer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblBuscarPartidaInvSemTer.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblBuscarPartidaInvSemTer);

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/accept_1.png"))); // NOI18N
        jButton4.setText("Seleccionar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cross.png"))); // NOI18N
        jButton5.setText("Cancelar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(0, 204, 204));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/magnifier.png"))); // NOI18N
        jLabel19.setText("Buscar partida");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgBuscarLayout = new javax.swing.GroupLayout(dlgBuscar.getContentPane());
        dlgBuscar.getContentPane().setLayout(dlgBuscarLayout);
        dlgBuscarLayout.setHorizontalGroup(
            dlgBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgBuscarLayout.setVerticalGroup(
            dlgBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        btnRealizarEntrada1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarEntrada1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/accept_1.png"))); // NOI18N
        btnRealizarEntrada1.setText("Aceptar");
        btnRealizarEntrada1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarEntrada1ActionPerformed(evt);
            }
        });

        btnCancelarAgregar1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarAgregar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cross.png"))); // NOI18N
        btnCancelarAgregar1.setText("Cancelar");
        btnCancelarAgregar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAgregar1ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel33.setText("No. Partida:");

        jLabel66.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel66.setText("Tipo de recorte:");

        jLabel67.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel67.setText("Calibre:");

        txtNoPartidaEnvSal.setEditable(false);
        txtNoPartidaEnvSal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPartidaEnvSal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaEnvSalKeyTyped(evt);
            }
        });

        txtTipoRecorteEnvSal.setEditable(false);
        txtTipoRecorteEnvSal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTipoRecorteEnvSal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTipoRecorteEnvSalKeyTyped(evt);
            }
        });

        jLabel68.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel68.setText("No. Piezas Actuales:");

        txtNoPiezasActualesEnvSal.setEditable(false);
        txtNoPiezasActualesEnvSal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasActualesEnvSal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoPiezasActualesEnvSalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasActualesEnvSalKeyTyped(evt);
            }
        });

        cmbCalibreEnvSal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel69.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel69.setText("Selección:");

        cmbSeleccionEnvSal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel70.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel70.setText("Kg Totales:");

        txtKgTotalesEnvSal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtKgTotalesEnvSal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKgTotalesEnvSalKeyTyped(evt);
            }
        });

        jLabel71.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel71.setText("No. Piezas:");

        txtNoPiezasEnvSal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasEnvSal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoPiezasEnvSalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasEnvSalKeyTyped(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(0, 204, 51));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
        jLabel20.setText("Realizar Salida");
        jLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtDecimetrosEnvSal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDecimetrosEnvSalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDecimetrosEnvSalKeyTyped(evt);
            }
        });

        txtPiesEnvSal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPiesEnvSalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPiesEnvSalKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Decimetros:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Pies:");

        jrkgEnvSal.setText("Kilogramos");
        jrkgEnvSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrkgEnvSalActionPerformed(evt);
            }
        });

        jrAreaEnvSal.setText("Área");
        jrAreaEnvSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrAreaEnvSalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel71)
                            .addComponent(jLabel68)
                            .addComponent(jLabel70)
                            .addComponent(jLabel33)
                            .addComponent(jLabel67)
                            .addComponent(jLabel69)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel66))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPiesEnvSal)
                            .addComponent(txtNoPartidaEnvSal, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(txtNoPiezasEnvSal, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(txtNoPiezasActualesEnvSal, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(cmbSeleccionEnvSal, 0, 122, Short.MAX_VALUE)
                            .addComponent(cmbCalibreEnvSal, 0, 122, Short.MAX_VALUE)
                            .addComponent(txtTipoRecorteEnvSal, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(txtKgTotalesEnvSal, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(txtDecimetrosEnvSal))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 74, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(btnRealizarEntrada1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancelarAgregar1)
                                .addGap(24, 24, 24))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jrkgEnvSal)
                                .addGap(63, 63, 63)
                                .addComponent(jrAreaEnvSal)
                                .addGap(57, 57, 57))))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrkgEnvSal)
                    .addComponent(jrAreaEnvSal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPartidaEnvSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTipoRecorteEnvSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCalibreEnvSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSeleccionEnvSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasActualesEnvSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel68))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasEnvSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel71))
                .addGap(13, 13, 13)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel70)
                    .addComponent(txtKgTotalesEnvSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDecimetrosEnvSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(13, 13, 13)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPiesEnvSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntrada1)
                    .addComponent(btnCancelarAgregar1))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEnvSalLayout = new javax.swing.GroupLayout(dlgEnvSal.getContentPane());
        dlgEnvSal.getContentPane().setLayout(dlgEnvSalLayout);
        dlgEnvSalLayout.setHorizontalGroup(
            dlgEnvSalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEnvSalLayout.setVerticalGroup(
            dlgEnvSalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        txtCalibre.setEditable(false);
        txtCalibre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCalibre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCalibreKeyTyped(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel34.setText("No. Partida:");

        jLabel74.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel74.setText("Motivo:");

        txtSeleccion.setEditable(false);
        txtSeleccion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSeleccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSeleccionKeyTyped(evt);
            }
        });

        btnRealizarEntradaEnvSemi2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarEntradaEnvSemi2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/accept_1.png"))); // NOI18N
        btnRealizarEntradaEnvSemi2.setText("Aceptar");
        btnRealizarEntradaEnvSemi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarEntradaEnvSemi2ActionPerformed(evt);
            }
        });

        btnCancelarAgregarEnvSemi2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarAgregarEnvSemi2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cross.png"))); // NOI18N
        btnCancelarAgregarEnvSemi2.setText("Cancelar");
        btnCancelarAgregarEnvSemi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAgregarEnvSemi2ActionPerformed(evt);
            }
        });

        jPanel9.setBackground(new java.awt.Color(0, 204, 51));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
        jLabel21.setText("Eliminar Piezas");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Calibre:");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setText("Selección:");

        txtNoPartida1.setEditable(false);
        txtNoPartida1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setText("Tipo de recorte:");

        txtTipoRecorte.setEditable(false);
        txtTipoRecorte.setBackground(new java.awt.Color(255, 255, 255));

        txtrMotivo.setColumns(20);
        txtrMotivo.setRows(5);
        jScrollPane3.setViewportView(txtrMotivo);

        txtNoPiezasActuales.setEditable(false);

        txtNoPiezasEliminar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasEliminarKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("No. Piezas Actuales:");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setText("No. Piezas a eliminar:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGap(32, 32, 32)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel34)
                                        .addComponent(jLabel23)
                                        .addComponent(jLabel10)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel22)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel74, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCalibre, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNoPartida1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                .addComponent(txtTipoRecorte))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtNoPiezasEliminar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNoPiezasActuales, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSeleccion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)))
                        .addGap(0, 57, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRealizarEntradaEnvSemi2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelarAgregarEnvSemi2)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txtNoPartida1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTipoRecorte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCalibre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(17, 17, 17)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSeleccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasActuales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel74))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntradaEnvSemi2)
                    .addComponent(btnCancelarAgregarEnvSemi2))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEliPzaInvTerminadoLayout = new javax.swing.GroupLayout(dlgEliPzaInvTerminado.getContentPane());
        dlgEliPzaInvTerminado.getContentPane().setLayout(dlgEliPzaInvTerminadoLayout);
        dlgEliPzaInvTerminadoLayout.setHorizontalGroup(
            dlgEliPzaInvTerminadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEliPzaInvTerminadoLayout.setVerticalGroup(
            dlgEliPzaInvTerminadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        tblTerminado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblTerminado.setModel(new javax.swing.table.DefaultTableModel(
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
        tblTerminado.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblTerminado);

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
    lbl.setText("   ");
    jToolBar1.add(lbl);

    jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel25.setText("Hasta:");
    jToolBar1.add(jLabel25);

    dcFecha2EntradaSemiterminado.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
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
    btnReporteEntrada6.setText("Reporte Inventario Terminado");
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

    jLabel72.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel72.setForeground(new java.awt.Color(227, 222, 222));
    jLabel72.setText("     ");
    jToolBar2.add(jLabel72);

    jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
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

    jToolBar3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar3.setFloatable(false);
    jToolBar3.setRollover(true);

    lblEnviarTerminado.setText("   ");
    jToolBar3.add(lblEnviarTerminado);

    btnAgregarEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnAgregarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
    btnAgregarEntrada.setText("Agregar Entrada");
    btnAgregarEntrada.setEnabled(false);
    btnAgregarEntrada.setFocusable(false);
    btnAgregarEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnAgregarEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnAgregarEntrada.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAgregarEntradaActionPerformed(evt);
        }
    });
    jToolBar3.add(btnAgregarEntrada);

    jLabel61.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel61.setForeground(new java.awt.Color(227, 222, 222));
    jLabel61.setText("   ");
    jToolBar3.add(jLabel61);

    btnEnviarTerminado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnEnviarTerminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
    btnEnviarTerminado.setText("Realizar Salida");
    btnEnviarTerminado.setEnabled(false);
    btnEnviarTerminado.setFocusable(false);
    btnEnviarTerminado.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnEnviarTerminado.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnEnviarTerminado.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnEnviarTerminadoActionPerformed(evt);
        }
    });
    jToolBar3.add(btnEnviarTerminado);

    jLabel73.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel73.setForeground(new java.awt.Color(227, 222, 222));
    jLabel73.setText("   ");
    jToolBar3.add(jLabel73);

    jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
    jButton2.setText("Eliminar Piezas");
    jButton2.setFocusable(false);
    jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });
    jToolBar3.add(jButton2);

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
        actualizarTablaTerminado();
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
        actualizarTablaTerminado();
    }//GEN-LAST:event_cmbTipoRecorteActionPerformed

    private void btnReporteEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradaActionPerformed
        actualizarTablaTerminado();
        generarReporteEntradaTerminado();
    }//GEN-LAST:event_btnReporteEntradaActionPerformed

    private void btnReporteEntrada2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada2ActionPerformed
        generarReporteInventarioXTrabajar();
    }//GEN-LAST:event_btnReporteEntrada2ActionPerformed

    private void btnReporteEntrada3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada3ActionPerformed
        actualizarTablaTerminado();
        generarReporteSalidaTerminado();
    }//GEN-LAST:event_btnReporteEntrada3ActionPerformed

    private void txtNoPartidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaKeyTyped
        validarNumerosEnteros(evt, txtNoPartida.getText());
    }//GEN-LAST:event_txtNoPartidaKeyTyped

    private void btnEnviarTerminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarTerminadoActionPerformed
        try 
        {
            int fila = tblTerminado.getSelectedRow();
            String piezas = (String.valueOf(tblTerminado.getValueAt(fila, 2)));
            int numPiezasActuales = Integer.parseInt(piezas);
            
            if (numPiezasActuales != 0)
            {
                abrirDialogoEnvSal();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "El número de piezas actuales debe ser mayor a 0","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Inventario Terminado","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEnviarTerminadoActionPerformed

    private void txtNoPartidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            actualizarTablaTerminado();
        }
    }//GEN-LAST:event_txtNoPartidaKeyPressed

    private void btnReporteEntrada6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada6ActionPerformed
        actualizarTablaTerminado();
        generarReporteInventarioTerminado();
    }//GEN-LAST:event_btnReporteEntrada6ActionPerformed

    private void cmbCalibreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCalibreActionPerformed
        actualizarTablaTerminado();
    }//GEN-LAST:event_cmbCalibreActionPerformed

    private void cmbSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSeleccionActionPerformed
        actualizarTablaTerminado();
    }//GEN-LAST:event_cmbSeleccionActionPerformed

    private void btnAgregarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEntradaActionPerformed
        try {
            abrirDialogoAgregar();
        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }//GEN-LAST:event_btnAgregarEntradaActionPerformed

    private void btnRealizarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaActionPerformed
        realizarEntradaTerminado();
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
            Logger.getLogger(PnlTerminado.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PnlTerminado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        dlgBuscar.setVisible(false);
        dlgAgregar.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtNoPiezasAgregarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasAgregarKeyReleased
//        calcKgTotales();
    }//GEN-LAST:event_txtNoPiezasAgregarKeyReleased

    private void btnRealizarEntrada1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntrada1ActionPerformed
        try
        {
            realizarEntradaEnvSal();
        }
        catch (Exception ex)
        {
            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRealizarEntrada1ActionPerformed

    private void btnCancelarAgregar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregar1ActionPerformed
        dlgEnvSal.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregar1ActionPerformed

    private void txtNoPartidaEnvSalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaEnvSalKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPartidaEnvSalKeyTyped

    private void txtTipoRecorteEnvSalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTipoRecorteEnvSalKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoRecorteEnvSalKeyTyped

    private void txtNoPiezasActualesEnvSalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasActualesEnvSalKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPiezasActualesEnvSalKeyReleased

    private void txtNoPiezasActualesEnvSalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasActualesEnvSalKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPiezasActualesEnvSalKeyTyped

    private void txtNoPiezasEnvSalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasEnvSalKeyReleased
//        calcKgTotalesEnvSal();
    }//GEN-LAST:event_txtNoPiezasEnvSalKeyReleased

    private void txtNoPiezasEnvSalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasEnvSalKeyTyped
        char c;
        c=evt.getKeyChar();

        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtNoPiezasEnvSalKeyTyped

    private void jrAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrAreaActionPerformed
        txtKgTotalesAgregar.setEnabled(false);
        txtKgTotalesAgregar.setText("");
        
        txtDecimetrosAgregar.setEnabled(true);
        txtDecimetrosAgregar.setText("");
        txtPiesCuadradosAgregar.setEnabled(true);
        txtPiesCuadradosAgregar.setText("");
    }//GEN-LAST:event_jrAreaActionPerformed

    private void jrKgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrKgActionPerformed
        txtKgTotalesAgregar.setEnabled(true);
        txtKgTotalesAgregar.setText("");
        
        txtDecimetrosAgregar.setEnabled(false);
        txtDecimetrosAgregar.setText("");
        txtPiesCuadradosAgregar.setEnabled(false);
        txtPiesCuadradosAgregar.setText("");
    }//GEN-LAST:event_jrKgActionPerformed

    private void txtDecimetrosAgregarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDecimetrosAgregarKeyReleased
        calcPiesTotalesAgregar();
    }//GEN-LAST:event_txtDecimetrosAgregarKeyReleased

    private void txtPiesCuadradosAgregarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPiesCuadradosAgregarKeyReleased
        calcDecimetrosTotalesAgregar();
    }//GEN-LAST:event_txtPiesCuadradosAgregarKeyReleased

    private void txtDecimetrosAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDecimetrosAgregarKeyTyped
        validarNumeros(evt, txtDecimetrosAgregar.getText());
    }//GEN-LAST:event_txtDecimetrosAgregarKeyTyped

    private void txtPiesCuadradosAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPiesCuadradosAgregarKeyTyped
        validarNumeros(evt, txtPiesCuadradosAgregar.getText());
    }//GEN-LAST:event_txtPiesCuadradosAgregarKeyTyped

    private void txtKgTotalesEnvSalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgTotalesEnvSalKeyTyped
        validarNumeros(evt, txtKgTotalesEnvSal.getText());
    }//GEN-LAST:event_txtKgTotalesEnvSalKeyTyped

    private void txtPiesEnvSalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPiesEnvSalKeyTyped
        validarNumeros(evt, txtPiesEnvSal.getText());
    }//GEN-LAST:event_txtPiesEnvSalKeyTyped

    private void txtDecimetrosEnvSalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDecimetrosEnvSalKeyTyped
        validarNumeros(evt, txtDecimetrosEnvSal.getText());
    }//GEN-LAST:event_txtDecimetrosEnvSalKeyTyped

    private void jrkgEnvSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrkgEnvSalActionPerformed
        txtKgTotalesEnvSal.setEnabled(true);
        txtKgTotalesEnvSal.setText("");
        
        txtDecimetrosEnvSal.setEnabled(false);
        txtDecimetrosEnvSal.setText("");
        txtPiesEnvSal.setEnabled(false);
        txtPiesEnvSal.setText("");
    }//GEN-LAST:event_jrkgEnvSalActionPerformed

    private void jrAreaEnvSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrAreaEnvSalActionPerformed
        txtKgTotalesEnvSal.setEnabled(false);
        txtKgTotalesEnvSal.setText("");
        
        txtDecimetrosEnvSal.setEnabled(true);
        txtDecimetrosEnvSal.setText("");
        txtPiesEnvSal.setEnabled(true);
        txtPiesEnvSal.setText("");
    }//GEN-LAST:event_jrAreaEnvSalActionPerformed

    private void txtDecimetrosEnvSalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDecimetrosEnvSalKeyReleased
        calcPiesTotalesEnvSal();
    }//GEN-LAST:event_txtDecimetrosEnvSalKeyReleased

    private void txtPiesEnvSalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPiesEnvSalKeyReleased
        calcDecimetrosTotalesEnvSal();
    }//GEN-LAST:event_txtPiesEnvSalKeyReleased

    private void txtCalibreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCalibreKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalibreKeyTyped

    private void txtSeleccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSeleccionKeyTyped
        char c;
        c=evt.getKeyChar();

        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtSeleccionKeyTyped

    private void btnRealizarEntradaEnvSemi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaEnvSemi2ActionPerformed
        try
        {
            eliminarPiezasInvTerminado();
        }
        catch (Exception ex)
        {
            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRealizarEntradaEnvSemi2ActionPerformed

    private void btnCancelarAgregarEnvSemi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarEnvSemi2ActionPerformed
        dlgEliPzaInvTerminado.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarEnvSemi2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            int fila = tblTerminado.getSelectedRow();
            String piezas = (String.valueOf(tblTerminado.getValueAt(fila, 2)));
            int numPiezasActuales = Integer.parseInt(piezas);

            if (numPiezasActuales != 0)
            {
                abrirDialogoEliPzaInvTerminado();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "El número de piezas actuales debe ser mayor a 0","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de inventario semiterminado","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        actualizarTablaTerminado();
        generarReporteBajasInvTerminado();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtNoPiezasEliminarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasEliminarKeyTyped
        char c;
        c=evt.getKeyChar();

        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtNoPiezasEliminarKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarEntrada;
    private javax.swing.JButton btnBuscarEntrada;
    private javax.swing.JButton btnCancelarAgregar;
    private javax.swing.JButton btnCancelarAgregar1;
    private javax.swing.JButton btnCancelarAgregarEnvSemi2;
    private javax.swing.JButton btnEnviarTerminado;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.ButtonGroup btnGroup1;
    private javax.swing.JButton btnRealizarEntrada;
    private javax.swing.JButton btnRealizarEntrada1;
    private javax.swing.JButton btnRealizarEntradaEnvSemi2;
    private javax.swing.JButton btnReporteEntrada;
    private javax.swing.JButton btnReporteEntrada2;
    private javax.swing.JButton btnReporteEntrada3;
    private javax.swing.JButton btnReporteEntrada6;
    private javax.swing.JComboBox cmbCalibre;
    private javax.swing.JComboBox cmbCalibreAgregar;
    private javax.swing.JComboBox cmbCalibreEnvSal;
    private javax.swing.JComboBox cmbSeleccion;
    private javax.swing.JComboBox cmbSeleccionAgregar;
    private javax.swing.JComboBox cmbSeleccionEnvSal;
    private javax.swing.JComboBox cmbTipoRecorte;
    private datechooser.beans.DateChooserCombo dcFecha1EntradaSemiterminado;
    private datechooser.beans.DateChooserCombo dcFecha2EntradaSemiterminado;
    private javax.swing.JDialog dlgAgregar;
    private javax.swing.JDialog dlgBuscar;
    private javax.swing.JDialog dlgEliPzaInvTerminado;
    private javax.swing.JDialog dlgEnvSal;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
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
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JRadioButton jrArea;
    private javax.swing.JRadioButton jrAreaEnvSal;
    private javax.swing.JRadioButton jrFiltroFechasEntrada;
    private javax.swing.JRadioButton jrKg;
    private javax.swing.JRadioButton jrkgEnvSal;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblEnviarTerminado;
    private javax.swing.JTable tblBuscarPartidaInvSemTer;
    private javax.swing.JTable tblTerminado;
    private javax.swing.JTextField txtCalibre;
    private javax.swing.JTextField txtDecimetrosAgregar;
    private javax.swing.JTextField txtDecimetrosEnvSal;
    private javax.swing.JTextField txtKgTotalesAgregar;
    private javax.swing.JTextField txtKgTotalesEnvSal;
    private javax.swing.JTextField txtNoPartida;
    private javax.swing.JTextField txtNoPartida1;
    private javax.swing.JTextField txtNoPartidaAgregar;
    private javax.swing.JTextField txtNoPartidaEnvSal;
    private javax.swing.JTextField txtNoPiezasActuales;
    private javax.swing.JTextField txtNoPiezasActualesEnvSal;
    private javax.swing.JTextField txtNoPiezasAgregar;
    private javax.swing.JTextField txtNoPiezasEliminar;
    private javax.swing.JTextField txtNoPiezasEnvSal;
    private javax.swing.JTextField txtPiesCuadradosAgregar;
    private javax.swing.JTextField txtPiesEnvSal;
    private javax.swing.JTextField txtSeleccion;
    private javax.swing.JTextField txtTipoRecorte;
    private javax.swing.JTextField txtTipoRecorteAgregar;
    private javax.swing.JTextField txtTipoRecorteEnvSal;
    private javax.swing.JTextArea txtrMotivo;
    // End of variables declaration//GEN-END:variables
}
