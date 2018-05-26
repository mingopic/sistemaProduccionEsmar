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
    String[][] proveedoresAgregar = null;
    String[][] tipoCueroAgregar = null;
    String[][] rangoPesoCuero = null;
    String[][] datosEntRecCuero = null;
    private final String imagen="/Imagenes/esmar.png";
    
    DefaultTableModel dtms=new DefaultTableModel();
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "Provedor","Tipo Cuero","No. Camión","Total Piezas","Total Kg","Precio x Kg","Costo Camión","Fecha de Entrada"
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
        pc = new ProveedorCommands();
        String[][] proveedores = pc.llenarComboboxProveedores();
        
        int i=0;
        while (i<proveedores.length)
        {
            cmbProveedor.addItem(proveedores[i][1]);
            i++;
        }
    }
    
    public void llenarComboProveedoresAgregar() throws Exception
    {
        pc = new ProveedorCommands();
        cmbProveedorAgregar.removeAllItems();
        proveedoresAgregar = pc.llenarComboboxProveedores();
        
        int i=0;
        while (i<proveedoresAgregar.length)
        {
            cmbProveedorAgregar.addItem(proveedoresAgregar[i][1]);
            i++;
        }
    }
    
    public void llenarNoCamion() throws Exception
    {
        String[] noCamion = null;
        
        String proveedor = cmbProveedorAgregar.getSelectedItem().toString();
        int idProveedor=0;
        
        for (int i = 0; i < proveedoresAgregar.length; i++)
        {
            if (proveedoresAgregar[i][1] == proveedor)
            {
                idProveedor=Integer.parseInt(proveedoresAgregar[i][0]);
            }
        }
        
        rc.setIdProveedor(idProveedor);
        
        noCamion = rcc.llenarNoCamiones(rc);
        
        txtNoCamion.setText(noCamion[0]);
    }
    
    public void llenarComboTipoCueroAgregar() throws Exception
    {
        tcc = new TipoCueroCommands();
        cmbTipoCueroAgregar.removeAllItems();
        tipoCueroAgregar= tcc.llenarComboboxTipoCuero();
        
        int i=0;
        while (i<tipoCueroAgregar.length)
        {
            cmbTipoCueroAgregar.addItem(tipoCueroAgregar[i][1]);
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
    
    public void llenarlblRangoPesoCuero() throws Exception
    {
        rpcc = new RangoPesoCueroCommands();
        rangoPesoCuero = rpcc.llenarLabelRangoPesoCuero();
        
        lblRangoMin.setText(rangoPesoCuero[0][1]);
        lblRangoMax.setText(rangoPesoCuero[0][2]);
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

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    

    //Método que abre el dialogo de agregar entrada de producto por trabajar 
    public void abrirDialogoAgregar() throws Exception
    {    
        dlgAgregarRecepcion.setSize(600, 550);
        dlgAgregarRecepcion.setPreferredSize(dlgAgregarRecepcion.getSize());
        dlgAgregarRecepcion.setLocationRelativeTo(null);
        dlgAgregarRecepcion.setAlwaysOnTop(true);
        dlgAgregarRecepcion.setVisible(true);
        
        llenarComboProveedoresAgregar();
        llenarComboTipoCueroAgregar();
        llenarlblRangoPesoCuero();
        llenarNoCamion();
    }
    
    
    
    public void calcularCostoCamion() throws Exception
    {
        try 
        {
            double kgTotales;
            double precio;

            if (txtKgTotales.getText().isEmpty())
            {
                kgTotales = 0;
            }
            else
            {
                kgTotales = Double.parseDouble(txtKgTotales.getText());
            }

            if (txtPrecio.getText().isEmpty())
            {
                precio = 0;
            }
            else
            {
                precio = Double.parseDouble(txtPrecio.getText());
            }

            double costoCamion = kgTotales*precio;

            txtCostoCamion.setText(String.valueOf(costoCamion));
        }
        catch (Exception e)
        {
            txtCostoCamion.setText(String.valueOf(0));
        }
    }
    
    public void calcularCostoTotalCamion() throws Exception
    {
        try 
        {
            String[][] datosConfMerma = null;
            cmc = new ConfiguracionMermaCommands();

            double costoCamion;
            double mermaSal;
            double mermaHumedad;
            double mermaCachete;
            double tarimas;
            double kgTotales;
            double precio;
            int noPiezasLigeros;
            int noPiezasPesados;
            int noTotalPiezas;
            int refMerma;

            if (txtKgTotales.getText().isEmpty())
            {
                kgTotales = 0;
            }
            else
            {
                kgTotales = Double.parseDouble(txtKgTotales.getText());
            }

            if (txtPrecio.getText().isEmpty())
            {
                precio = 0;
            }
            else
            {
                precio = Double.parseDouble(txtPrecio.getText());
            }

            if (txtCostoCamion.getText().isEmpty())
            {
                costoCamion = 0;
            }
            else
            {
                costoCamion = Double.parseDouble(txtCostoCamion.getText());
            }

            if (txtMermaSal.getText().isEmpty())
            {
                mermaSal = 0;
            }
            else
            {
                mermaSal = Double.parseDouble(txtMermaSal.getText());
            }

            if (txtMermaHumedad.getText().isEmpty())
            {
                mermaHumedad = 0;
            }
            else
            {
                mermaHumedad = Double.parseDouble(txtMermaHumedad.getText());
            }

            if (txtMermaCachete.getText().isEmpty())
            {
                mermaCachete = 0;
            }
            else
            {
                mermaCachete = Double.parseDouble(txtMermaCachete.getText());
            }

            if (txtTarimas.getText().isEmpty())
            {
                tarimas = 0;
            }
            else
            {
                tarimas = Double.parseDouble(txtTarimas.getText());
            }

            if (txtNoPiezasLigeros.getText().isEmpty())
            {
                noPiezasLigeros = 0;
            }
            else
            {
                noPiezasLigeros = Integer.parseInt(txtNoPiezasLigeros.getText());
            }

            if (txtNoPiezasPesados.getText().isEmpty())
            {
                noPiezasPesados = 0;
            }
            else
            {
                noPiezasPesados = Integer.parseInt(txtNoPiezasPesados.getText());
            }

            if (txtRefMerma.getText().isEmpty())
            {
                refMerma = 0;
            }
            else
            {
                refMerma = Integer.parseInt(txtRefMerma.getText());
            }

            noTotalPiezas = noPiezasLigeros+noPiezasPesados;

            txtNoTotalPiezas.setText(String.valueOf(noTotalPiezas));

            datosConfMerma = cmc.obtenerConfiguracionMerma(mermaSal,mermaHumedad,mermaCachete,tarimas,kgTotales,precio,noTotalPiezas,refMerma);
            
            double costoTotalCamion = Double.parseDouble(datosConfMerma[0][0]);
            double salAcep = Double.parseDouble(datosConfMerma[0][1]);
            double humedadAcep = Double.parseDouble(datosConfMerma[0][2]);
            double cacheteAcep = Double.parseDouble(datosConfMerma[0][3]);
            double tarimasAcep = Double.parseDouble(datosConfMerma[0][4]);
            
            txtMermaSalAceptada.setText(String.valueOf(salAcep));
            txtMermaHumedadAceptada.setText(String.valueOf(humedadAcep));
            txtMermaCacheteAceptada.setText(String.valueOf(cacheteAcep));
            txtTarimasAceptada.setText(String.valueOf(tarimasAcep));
            txtCostoTotalCamion.setText(String.valueOf(costoTotalCamion));
        }
        catch (Exception e)
        {
            txtCostoTotalCamion.setText(String.valueOf(0));
            txtMermaSalAceptada.setText(String.valueOf(0));
            txtMermaHumedadAceptada.setText(String.valueOf(0));
            txtMermaCacheteAceptada.setText(String.valueOf(0));
            txtTarimasAceptada.setText(String.valueOf(0));
        }
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
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarEntradaRecepcionCuero() throws Exception
    {
        try
        {
            rcc = new RecepcionCueroCommands();
        
            String proveedor = cmbProveedorAgregar.getSelectedItem().toString();
            String tipoCuero = cmbTipoCueroAgregar.getSelectedItem().toString();
            int idProveedor=0;
            int noCamion;
            int idTipoCuero=0;
            int idRangoPesoCuero;
            int noPiezasLigero;
            int noPiezasPesado;
            int noTotalPiezas;
            double kgTotal;
            double precioXKilo;
            double mermaSal;
            double mermaHumedad;
            double mermaCachete;
            double mermaTarimas;

            for (int i = 0; i < proveedoresAgregar.length; i++)
            {
                if (proveedoresAgregar[i][1] == proveedor)
                {
                    idProveedor = Integer.parseInt(proveedoresAgregar[i][0]);
                }
            }

            noCamion = Integer.parseInt(txtNoCamion.getText());

            for (int i = 0; i < tipoCueroAgregar.length; i++)
            {
                if (tipoCueroAgregar[i][1] == tipoCuero)
                {
                    idTipoCuero = Integer.parseInt(tipoCueroAgregar[i][0]);
                }
            }

            idRangoPesoCuero = Integer.parseInt(rangoPesoCuero[0][0]);
            noPiezasLigero = Integer.parseInt(txtNoPiezasLigeros.getText());
            noPiezasPesado = Integer.parseInt(txtNoPiezasPesados.getText());
            noTotalPiezas = Integer.parseInt(txtNoTotalPiezas.getText());
            kgTotal = Double.parseDouble(txtKgTotales.getText());
            precioXKilo = Double.parseDouble(txtPrecio.getText());
            mermaSal = Double.parseDouble(txtMermaSal.getText());
            mermaHumedad = Double.parseDouble(txtMermaHumedad.getText());
            mermaCachete = Double.parseDouble(txtMermaCachete.getText());
            mermaTarimas = Double.parseDouble(txtTarimas.getText());

            rc.setIdProveedor(idProveedor);
            rc.setNoCamion(noCamion);
            rc.setIdTipoCuero(idTipoCuero);
            rc.setIdRangoPesoCuero(idRangoPesoCuero);
            rc.setNoPiezasLigero(noPiezasLigero);
            rc.setNoPiezasPesado(noPiezasPesado);
            rc.setNoTotalPiezas(noTotalPiezas);
            rc.setKgTotal(kgTotal);
            rc.setPrecioXKilo(precioXKilo);
            rc.setMermaSal(mermaSal);
            rc.setMermaHumedad(mermaHumedad);
            rc.setMermaCachete(mermaCachete);
            rc.setMermaTarimas(mermaTarimas);

            rcc.InsertarEntradaRecepcionCuero(rc);
        } catch (Exception e) {
        }
    }
    
    public void generarReporteEntradaRecepcionCuero(RecepcionCuero rc, TipoCuero tp) throws Exception
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteEntrada.jasper");
            
            Map parametros = new HashMap();
            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
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
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generarReporteEntradaRecepcionCueroDetalle(RecepcionCuero rc) throws Exception
    {
        try
        {
            int idRecepcionCuero = Integer.parseInt(datosEntRecCuero[tblRecepcionCuero.getSelectedRow()][8]);
            rc.setIdRecepcionCuero(idRecepcionCuero);
            
            URL path = this.getClass().getResource("/Reportes/ReporteEntradaDetalle.jasper");
            
            Map parametros = new HashMap();
            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
            parametros.put("idRecepcionCuero",rc.getIdRecepcionCuero());
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(path);
            
            conexion.conectar();
            
            JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, conexion.getConexion());
            
            JasperViewer view = new JasperViewer(jprint, false);
            
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            view.setVisible(true);
            conexion.desconectar();
        } catch (JRException ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
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

        btnGroup = new javax.swing.ButtonGroup();
        dlgAgregarRecepcion = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbProveedorAgregar = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNoPiezasLigeros = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNoPiezasPesados = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtNoCamion = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtKgTotales = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtCostoCamion = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtMermaSal = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtMermaHumedad = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtMermaCachete = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtTarimas = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        lblRangoMin = new javax.swing.JLabel();
        lblRangoMax = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtCostoTotalCamion = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtNoTotalPiezas = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtRefMerma = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        cmbTipoCueroAgregar = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtMermaSalAceptada = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtMermaHumedadAceptada = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtMermaCacheteAceptada = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtTarimasAceptada = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRecepcionCuero = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnAgregarEntrada = new javax.swing.JButton();
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
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btnReporteEntrada = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        btnReporteEntrada1 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();

        dlgAgregarRecepcion.setBackground(new java.awt.Color(255, 255, 255));
        dlgAgregarRecepcion.setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lorry.png"))); // NOI18N
        jLabel2.setText("Agregar Recepción");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(400, 400, 400))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Proveedor");

        cmbProveedorAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbProveedorAgregar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorAgregarItemStateChanged(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Menor a ");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("No. Piezas Ligeros");

        txtNoPiezasLigeros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoPiezasLigerosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasLigerosKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Mayor a ");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("No. Piezas Pesados");

        txtNoPiezasPesados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoPiezasPesadosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasPesadosKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("No. Camión");

        txtNoCamion.setEditable(false);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("Kg. Totales");

        txtKgTotales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKgTotalesKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKgTotalesKeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Precio");

        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Costo Camión");

        txtCostoCamion.setEditable(false);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Mermas");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("Sal (Kg)");

        txtMermaSal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMermaSalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMermaSalKeyTyped(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Humedad");

        txtMermaHumedad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMermaHumedadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMermaHumedadKeyTyped(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Cachete");

        txtMermaCachete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMermaCacheteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMermaCacheteKeyTyped(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Tarimas");

        txtTarimas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTarimasKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTarimasKeyTyped(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cross.png"))); // NOI18N
        btnCancelar.setText("Cancelar");

        btnGuardar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Costo Final Camión");

        txtCostoTotalCamion.setEditable(false);

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("No. Total Piezas");

        txtNoTotalPiezas.setEditable(false);
        txtNoTotalPiezas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoTotalPiezasKeyTyped(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("RefMerma");

        txtRefMerma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRefMermaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRefMermaKeyTyped(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setText("Tipo de cuero");

        cmbTipoCueroAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setText("Mermas Aceptadas");

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel32.setText("Sal (Kg)");

        txtMermaSalAceptada.setEditable(false);

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel33.setText("Humedad");

        txtMermaHumedadAceptada.setEditable(false);

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel35.setText("Cachete");

        txtMermaCacheteAceptada.setEditable(false);

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel36.setText("Tarimas");

        txtTarimasAceptada.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel32)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMermaSalAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addComponent(jLabel33)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMermaHumedadAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(jLabel35)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMermaCacheteAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)
                                        .addComponent(jLabel36)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTarimasAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel19)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtMermaSal, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(28, 28, 28)
                                            .addComponent(jLabel20))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel26)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtRefMerma, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel23)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtCostoTotalCamion, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(txtMermaHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(34, 34, 34)
                                            .addComponent(jLabel21)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtMermaCachete, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(27, 27, 27)
                                            .addComponent(jLabel22)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtTarimas, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(0, 54, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(btnCancelar)
                                .addGap(18, 18, 18)
                                .addComponent(btnGuardar))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(386, 386, 386)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPrecio))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmbProveedorAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel30)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmbTipoCueroAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel9))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtNoPiezasLigeros, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                                    .addComponent(lblRangoMin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(28, 28, 28)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addComponent(jLabel14)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txtNoPiezasPesados, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lblRangoMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel24)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtNoTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCostoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37)
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtKgTotales, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 4, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(lblRangoMax))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(lblRangoMin)
                        .addComponent(jLabel3)
                        .addComponent(cmbProveedorAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(txtNoPiezasLigeros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel14)
                                .addComponent(txtNoPiezasPesados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtNoTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(cmbTipoCueroAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtNoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txtKgTotales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtCostoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel36)
                        .addComponent(txtTarimasAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMermaCacheteAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel35))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMermaHumedadAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel33))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32)
                        .addComponent(txtMermaSalAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22)
                        .addComponent(txtTarimas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMermaCachete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMermaHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(txtMermaSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtCostoTotalCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(txtRefMerma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnGuardar))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgAgregarRecepcionLayout = new javax.swing.GroupLayout(dlgAgregarRecepcion.getContentPane());
        dlgAgregarRecepcion.getContentPane().setLayout(dlgAgregarRecepcionLayout);
        dlgAgregarRecepcionLayout.setHorizontalGroup(
            dlgAgregarRecepcionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgAgregarRecepcionLayout.setVerticalGroup(
            dlgAgregarRecepcionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarRecepcionLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jScrollPane1.setViewportView(tblRecepcionCuero);

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

    jToolBar2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar2.setFloatable(false);
    jToolBar2.setRollover(true);

    btnReporteEntrada1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntrada1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteEntrada1.setText("Reporte Detalle");
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
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnReporteEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradaActionPerformed
        try {
            generarReporteEntradaRecepcionCuero(rc, tc);
            actualizarTablaRecepcionCuero();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnReporteEntradaActionPerformed

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
//        try {
//            abrirDialogoAgregar();
//        } catch (Exception ex) {
//            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
//        }

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

    private void txtPrecioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioKeyReleased
        try {
            calcularCostoCamion();
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtPrecioKeyReleased

    private void txtMermaSalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaSalKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtMermaSalKeyReleased

    private void txtMermaHumedadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaHumedadKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtMermaHumedadKeyReleased

    private void txtMermaCacheteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaCacheteKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtMermaCacheteKeyReleased

    private void txtTarimasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarimasKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtTarimasKeyReleased

    private void txtKgTotalesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgTotalesKeyTyped
        validarNumeros(evt, txtKgTotales.getText());
    }//GEN-LAST:event_txtKgTotalesKeyTyped

    private void txtKgTotalesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgTotalesKeyReleased
        try {
            calcularCostoCamion();
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtKgTotalesKeyReleased

    private void txtPrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioKeyTyped
        validarNumeros(evt, txtPrecio.getText());
    }//GEN-LAST:event_txtPrecioKeyTyped

    private void txtMermaSalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaSalKeyTyped
        validarNumeros(evt, txtMermaSal.getText());
    }//GEN-LAST:event_txtMermaSalKeyTyped

    private void txtMermaHumedadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaHumedadKeyTyped
        validarNumeros(evt, txtMermaHumedad.getText());
    }//GEN-LAST:event_txtMermaHumedadKeyTyped

    private void txtMermaCacheteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaCacheteKeyTyped
        validarNumeros(evt, txtMermaCachete.getText());
    }//GEN-LAST:event_txtMermaCacheteKeyTyped

    private void txtTarimasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarimasKeyTyped
        validarNumeros(evt, txtTarimas.getText());
    }//GEN-LAST:event_txtTarimasKeyTyped

    private void txtNoPiezasLigerosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasLigerosKeyTyped
        validarNumerosEnteros(evt, txtNoPiezasLigeros.getText());
    }//GEN-LAST:event_txtNoPiezasLigerosKeyTyped

    private void txtNoPiezasPesadosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasPesadosKeyTyped
        validarNumerosEnteros(evt, txtNoPiezasPesados.getText());
    }//GEN-LAST:event_txtNoPiezasPesadosKeyTyped

    private void txtNoTotalPiezasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoTotalPiezasKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoTotalPiezasKeyTyped

    private void txtRefMermaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRefMermaKeyTyped
        validarNumerosEnteros(evt, txtRefMerma.getText());
    }//GEN-LAST:event_txtRefMermaKeyTyped

    private void txtNoPiezasLigerosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasLigerosKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtNoPiezasLigerosKeyReleased

    private void txtNoPiezasPesadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasPesadosKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtNoPiezasPesadosKeyReleased

    private void txtRefMermaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRefMermaKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtRefMermaKeyReleased

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            agregarEntradaRecepcionCuero();
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void cmbProveedorAgregarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProveedorAgregarItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {
            try 
            {
                llenarNoCamion();
            } 
            catch (Exception ex) 
            {
                System.err.println(ex);;
            }
        }
    }//GEN-LAST:event_cmbProveedorAgregarItemStateChanged

    private void btnReporteEntrada1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada1ActionPerformed
         try {
            generarReporteEntradaRecepcionCueroDetalle(rc);
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnReporteEntrada1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarEntrada;
    private javax.swing.JButton btnBuscarEntrada;
    private javax.swing.JButton btnCancelar;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnReporteEntrada;
    private javax.swing.JButton btnReporteEntrada1;
    private javax.swing.JComboBox cmbProveedor;
    private javax.swing.JComboBox<String> cmbProveedorAgregar;
    private javax.swing.JComboBox cmbTipoCuero;
    private javax.swing.JComboBox<String> cmbTipoCueroAgregar;
    private datechooser.beans.DateChooserCombo dcFecha1EntradaSemiterminado;
    private datechooser.beans.DateChooserCombo dcFecha2EntradaSemiterminado;
    private javax.swing.JDialog dlgAgregarRecepcion;
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
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JRadioButton jrFiltroFechasEntrada;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblRangoMax;
    private javax.swing.JLabel lblRangoMin;
    private javax.swing.JTable tblRecepcionCuero;
    private javax.swing.JTextField txtCostoCamion;
    private javax.swing.JTextField txtCostoTotalCamion;
    private javax.swing.JTextField txtKgTotales;
    private javax.swing.JTextField txtMermaCachete;
    private javax.swing.JTextField txtMermaCacheteAceptada;
    private javax.swing.JTextField txtMermaHumedad;
    private javax.swing.JTextField txtMermaHumedadAceptada;
    private javax.swing.JTextField txtMermaSal;
    private javax.swing.JTextField txtMermaSalAceptada;
    private javax.swing.JTextField txtNoCamion;
    private javax.swing.JTextField txtNoPiezasLigeros;
    private javax.swing.JTextField txtNoPiezasPesados;
    private javax.swing.JTextField txtNoTotalPiezas;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtRefMerma;
    private javax.swing.JTextField txtTarimas;
    private javax.swing.JTextField txtTarimasAceptada;
    // End of variables declaration//GEN-END:variables
}
