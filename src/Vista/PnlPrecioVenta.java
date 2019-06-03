/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.CalibreCommands;
import Controlador.ConexionBD;
import Controlador.PrecioVentaCommands;
import Controlador.SeleccionCommands;
import Controlador.TipoMonedaCommands;
import Controlador.TipoRecorteCommands;
import Controlador.UnidadMedidaCommands;
import Modelo.Calibre;
import Modelo.PrecioVenta;
import Modelo.Seleccion;
import Modelo.TipoMoneda;
import Modelo.TipoRecorte;
import Modelo.UnidadMedida;
import java.awt.event.KeyEvent;
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
public class PnlPrecioVenta extends javax.swing.JPanel {
    ConexionBD conexion;
    PrecioVenta pv;
    PrecioVentaCommands pvc;
    TipoRecorte tr;
    TipoRecorteCommands trc;
    Calibre c;
    CalibreCommands cc;
    Seleccion s;
    SeleccionCommands sc;
    UnidadMedida um;
    UnidadMedidaCommands umc;
    List<UnidadMedida> lstUnidadMedida;
    List<TipoMoneda> lstTipoMoneda;
    TipoMonedaCommands tmc;
    
    String[][] tipoRecorte = null;
    String[][] calibres = null;
    String[][] seleccion = null;
    String[][] datos;
    DefaultTableModel dtm;
    private final String imagen="/Imagenes/logo_esmar.png";
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols_tblPrecioVenta = new String[]
    {
        "Tipo de Recorte" , "Calibre", "Selección", "Precio", "Moneda", "Unidad de medida", "Fecha Actualización" 
    };
   
    public PnlPrecioVenta() throws Exception {
        initComponents();
        inicializar();
    }
    
    
    //Método que se indica al inicializar el formulario
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        llenarComboCalibre();
        llenarComboTipoRecorte();
        llenarComboSeleccion();
        actualizarTablaPrecioVenta();
    }
    
    //método que llena los combobox del calibre en la base de datos
    public void llenarComboCalibre() throws Exception
    {
        cc = new CalibreCommands();
        calibres = cc.llenarComboboxCalibre();
        
        int i=0;
        while (i<calibres.length)
        {
            cmbCalibre.addItem(calibres[i][1]);
            i++;
        }
    }
    
    public void llenarComboCalibreAgregar() throws Exception
    {
        cc = new CalibreCommands();
        calibres = cc.llenarComboboxCalibre();
        
        int i=0;
        while (i<calibres.length)
        {
            cmbCalibreAgregar.addItem(calibres[i][1]);
            i++;
        }
    }
    
    public void llenarComboCalibreEditar() throws Exception
    {
        cc = new CalibreCommands();
        calibres = cc.llenarComboboxCalibre();
        
        int i=0;
        while (i<calibres.length)
        {
            cmbCalibreEditar.addItem(calibres[i][1]);
            i++;
        }
    }
    
    //método que llena los combobox del tipo de recorte en la base de datos
    public void llenarComboTipoRecorte() throws Exception
    {
        trc = new TipoRecorteCommands();
        tipoRecorte = trc.llenarComboboxTipoRecorte();
        
        int i=0;
        while (i<tipoRecorte.length)
        {
            cmbTipoRecorte.addItem(tipoRecorte[i][1]);
            i++;
        }
    }
    
    public void llenarComboTipoRecorteAgregar() throws Exception
    {
        trc = new TipoRecorteCommands();
        tipoRecorte = trc.llenarComboboxTipoRecorte();
        
        int i=0;
        while (i<tipoRecorte.length)
        {
            cmbTipoRecorteAgregar.addItem(tipoRecorte[i][1]);
            i++;
        }
    }
    
    public void llenarComboTipoRecorteEditar() throws Exception
    {
        trc = new TipoRecorteCommands();
        tipoRecorte = trc.llenarComboboxTipoRecorte();
        
        int i=0;
        while (i<tipoRecorte.length)
        {
            cmbTipoRecorteEditar.addItem(tipoRecorte[i][1]);
            i++;
        }
    }
    
    //método que llena los combobox de la selección en la base de datos
    public void llenarComboSeleccion() throws Exception
    {
        sc = new SeleccionCommands();
        seleccion = sc.llenarComboboxSeleccion();
        
        int i=0;
        while (i<seleccion.length)
        {
            cmbSeleccion.addItem(seleccion[i][1]);
            i++;
        }
    }
    
    public void llenarComboSeleccionAgregar() throws Exception
    {
        sc = new SeleccionCommands();
        seleccion = sc.llenarComboboxSeleccion();
        
        int i=0;
        while (i<seleccion.length)
        {
            cmbSeleccionAgregar.addItem(seleccion[i][1]);
            i++;
        }
    }
    
    public void llenarComboSeleccionEditar() throws Exception
    {
        sc = new SeleccionCommands();
        seleccion = sc.llenarComboboxSeleccion();
        
        int i=0;
        while (i<seleccion.length)
        {
            cmbSeleccionEditar.addItem(seleccion[i][1]);
            i++;
        }
    }
    
    public void llenarComboUnidadMedida() throws Exception
    {
        umc = new UnidadMedidaCommands();
        lstUnidadMedida = new ArrayList<>();
        cmbUnidadMedidaAgregar.removeAllItems();
        cmbUnidadMedidaEditar.removeAllItems();
        
        lstUnidadMedida = umc.obtenerUnidadMedida();
        
        int i=0;
        while (i < lstUnidadMedida.size())
        {
            cmbUnidadMedidaAgregar.addItem(lstUnidadMedida.get(i).getDescripcion());
            cmbUnidadMedidaEditar.addItem(lstUnidadMedida.get(i).getDescripcion());
            i++;
        }
    }
    
    public void llenarComboTipoMoneda() throws Exception
    {
        tmc = new TipoMonedaCommands();
        lstTipoMoneda = new ArrayList<>();
        cmbMonedaAgregar.addItem("");
        cmbMonedaAgregar.removeAllItems();
        cmbMonedaEditar.removeAllItems();
        
        lstTipoMoneda = tmc.obtenerMonedas();
        
        int i=0;
        while (i < lstTipoMoneda.size())
        {
            cmbMonedaAgregar.addItem(lstTipoMoneda.get(i).getDescripcion());
            cmbMonedaEditar.addItem(lstTipoMoneda.get(i).getDescripcion());
            i++;
        }
    }
    
    //Método para actualizar la tabla de Precio de venta
    private void actualizarTablaPrecioVenta() 
    {
        tr = new TipoRecorte();
        c = new Calibre();
        s = new Seleccion();
        
        //validamos si esta seleccionado algún tipo de cuero para hacer filtro
        if (cmbTipoRecorte.getSelectedItem().toString().equals("<Todos>"))
        {
            tr.setIdTipoRecorte(0);
        }
        else
        {
            tr.setIdTipoRecorte(Integer.parseInt(tipoRecorte[cmbTipoRecorte.getSelectedIndex()-1][0]));
        }
        
        //validamos si esta seleccionado algún calibre para hacer filtro
        if (cmbCalibre.getSelectedItem().toString().equals("<Todos>"))
        {
            c.setIdCalibre(0);
        }
        else
        {
            c.setIdCalibre(Integer.parseInt(calibres[cmbCalibre.getSelectedIndex()-1][0]));
        }
        
        //validamos si esta seleccionada alguna selección para hacer filtro
        if (cmbSeleccion.getSelectedItem().toString().equals("<Todos>"))
        {
            s.setIdSeleccion(0);
        }
        else
        {
            s.setIdSeleccion(Integer.parseInt(seleccion[cmbSeleccion.getSelectedIndex()-1][0]));
        }
        
        try 
        {   
            pvc = new PrecioVentaCommands();
            datos = pvc.obtenerTodosPrecioVenta(tr,c,s);
            
            dtm = new DefaultTableModel(datos, cols_tblPrecioVenta){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblPrecioVenta.setModel(dtm);
            tblPrecioVenta.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Método para agregar un precioVenta a BD
    private void agregarPrecioVenta()
    {
        try 
        {
            pv = new PrecioVenta();
            pvc = new PrecioVentaCommands();
            
            if (txtPrecioMXNAgregar.getText().isEmpty())
            {
                pv.setPrecio(0);
            }
            else
            {
                pv.setPrecio(Float.parseFloat(txtPrecioMXNAgregar.getText()));
            }
            
            if (txtPrecioAgregar.getText().isEmpty())
            {
                pv.setPrecio_original(0);
            }
            else
            {
                pv.setPrecio_original(Float.parseFloat(txtPrecioAgregar.getText()));
            }
            
            //Validar que el nombre del tambor no este vacío
            if (pv.getPrecio() <= 0)
            {   
                dlgAgregarPrecioVenta.setVisible(false);
                JOptionPane.showMessageDialog(null, "Ingrese un precio mayor a 0","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgAgregarPrecioVenta.setVisible(true);
                return;
            }
            
            pv.setIdSeleccion(Integer.parseInt(seleccion[cmbSeleccionAgregar.getSelectedIndex()][0]));
            pv.setIdCalibre(Integer.parseInt(calibres[cmbCalibreAgregar.getSelectedIndex()][0]));
            pv.setIdTipoRecorte(Integer.parseInt(tipoRecorte[cmbTipoRecorteAgregar.getSelectedIndex()][0]));
            pv.setUnidadMedida(lstUnidadMedida.get(cmbUnidadMedidaAgregar.getSelectedIndex()).getIdUnidadMedida());
            pv.setIdTipoMoneda(lstTipoMoneda.get(cmbMonedaAgregar.getSelectedIndex()).getIdTipoMoneda());
            
            //Valida que no haya un precio de venta registrado con los mismos datos en BD
            int valida = pvc.obtenerPrecioVentaDisp(pv);
            
            if (valida == 0) 
            {
                pvc.insertarPrecioVenta(pv);
                dlgAgregarPrecioVenta.setVisible(false);
                JOptionPane.showMessageDialog(null, "Precio de Venta registrado correctamente");
                actualizarTablaPrecioVenta();
            }
            else
            {
                dlgAgregarPrecioVenta.setVisible(false);
                JOptionPane.showMessageDialog(null, "Registro existente","Advertencia",JOptionPane.WARNING_MESSAGE);
                dlgAgregarPrecioVenta.setVisible(true);
            }
        } 
        catch (Exception e) 
        {   
            System.err.println(e);
            dlgAgregarPrecioVenta.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al insertar Precio de Venta", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarPrecioVenta.setVisible(true);
        }
    }
   
    //Método que abre el dialogo para agregar un tambor
    public void abrirDialogoAgregar()
    {   
        try 
        {
            llenarComboTipoRecorteAgregar();
            llenarComboCalibreAgregar();
            llenarComboSeleccionAgregar();
            llenarComboUnidadMedida();
            llenarComboTipoMoneda();
            txtPrecioAgregar.setText("");
            
            dlgAgregarPrecioVenta.setSize(435, 380);
            dlgAgregarPrecioVenta.setPreferredSize(dlgAgregarPrecioVenta.getSize());
            dlgAgregarPrecioVenta.setLocationRelativeTo(null);
            dlgAgregarPrecioVenta.setModal(true);
            dlgAgregarPrecioVenta.setVisible(true);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            dlgAgregarPrecioVenta.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregarPrecioVenta.setVisible(true);
        }
    }
    
    //Método que abre el dialogo para editar un tambor
    public void abrirDialogoEditar()
    {   
        try 
        {
            int fila = tblPrecioVenta.getSelectedRow();
            
            llenarComboTipoRecorteEditar();
            llenarComboSeleccionEditar();
            llenarComboCalibreEditar();
            llenarComboUnidadMedida();
            llenarComboTipoMoneda();
            
            cmbTipoRecorteEditar.setSelectedItem(String.valueOf(tblPrecioVenta.getValueAt(fila, 0)));
            cmbCalibreEditar.setSelectedItem(String.valueOf(tblPrecioVenta.getValueAt(fila, 1)));
            cmbSeleccionEditar.setSelectedItem(String.valueOf(tblPrecioVenta.getValueAt(fila, 2)));
            txtPrecioEditar.setText(String.valueOf(tblPrecioVenta.getValueAt(fila, 3)));
            cmbMonedaEditar.setSelectedItem(String.valueOf(tblPrecioVenta.getValueAt(fila, 4)));
            cmbUnidadMedidaEditar.setSelectedItem(String.valueOf(tblPrecioVenta.getValueAt(fila, 5)));
            
            if (!txtPrecioEditar.getText().equals(""))
                txtPrecioMXNEditar.setText(String.valueOf(Double.parseDouble(txtPrecioEditar.getText()) * lstTipoMoneda.get(cmbMonedaEditar.getSelectedIndex()).getTipoCambio()));
            else
                txtPrecioMXNEditar.setText("");
            
            dlgEditarPrecioVenta.setSize(475, 390);
            dlgEditarPrecioVenta.setPreferredSize(dlgEditarPrecioVenta.getSize());
            dlgEditarPrecioVenta.setLocationRelativeTo(null);
            dlgEditarPrecioVenta.setModal(true);
            dlgEditarPrecioVenta.setVisible(true);
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgEditarPrecioVenta.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgEditarPrecioVenta.setVisible(true);
        }
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
    
    //Método para editar un precio de venta en BD
    private void editarPrecioVenta()
    {
        dlgEditarPrecioVenta.setVisible(false);
        if (JOptionPane.showConfirmDialog(null, "¿Realmente desea guardar las modificaciones?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
        {
            int fila = tblPrecioVenta.getSelectedRow();
            
            try
            {
                pv = new PrecioVenta();
                pvc = new PrecioVentaCommands();
                
                //Elimina espacios, tabuladores y retornos delante.
                String precio = txtPrecioEditar.getText().replaceAll("^\\s*","");
                
                //Validar que el precio de venta no este vacío
                if (precio.equals(""))
                {   
                    dlgEditarPrecioVenta.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ingrese un precio de venta","Mensaje",JOptionPane.WARNING_MESSAGE);
                    dlgEditarPrecioVenta.setVisible(true);
                    return;
                }

                pv.setIdPrecioVenta(Integer.parseInt(datos[fila][7]));
                pv.setPrecio(Float.parseFloat(txtPrecioMXNEditar.getText()));
                pv.setPrecio_original(Float.parseFloat(txtPrecioEditar.getText()));
                pvc.actualizarPrecioVenta(pv);
                dlgEditarPrecioVenta.setVisible(false);
                JOptionPane.showMessageDialog(null, "Precio de venta actualizado correctamente");
                actualizarTablaPrecioVenta();
            }
            catch (Exception e)
            {
                System.err.println(e);
                dlgEditarPrecioVenta.setVisible(false);
                JOptionPane.showMessageDialog(null, "Error al actualizar precio de venta", "Error", JOptionPane.ERROR_MESSAGE);
                dlgEditarPrecioVenta.setVisible(true);
            }
        }
        else
            dlgEditarPrecioVenta.setVisible(true);
    }
    
    public void generarReportePrecioVenta()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReportePrecioVenta.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("idTipoRecorte", tr.getIdTipoRecorte());
            parametros.put("idCalibre", c.getIdCalibre());
            parametros.put("idSeleccion", s.getIdSeleccion());
            
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

        btnGroup = new javax.swing.ButtonGroup();
        dlgAgregarPrecioVenta = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtPrecioAgregar = new javax.swing.JTextField();
        btnGuardarAgregar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbTipoRecorteAgregar = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        cmbCalibreAgregar = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        cmbSeleccionAgregar = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cmbUnidadMedidaAgregar = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        txtPrecioMXNAgregar = new javax.swing.JTextField();
        cmbMonedaAgregar = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        dlgEditarPrecioVenta = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnGuardarEditar = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        cmbTipoRecorteEditar = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        cmbCalibreEditar = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        cmbSeleccionEditar = new javax.swing.JComboBox<>();
        txtPrecioEditar = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbUnidadMedidaEditar = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        cmbMonedaEditar = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        txtPrecioMXNEditar = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPrecioVenta = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbTipoRecorte = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cmbCalibre = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cmbSeleccion = new javax.swing.JComboBox<>();
        jToolBar3 = new javax.swing.JToolBar();
        btnAgregarProveedor = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();

        dlgAgregarPrecioVenta.setBackground(new java.awt.Color(255, 255, 255));
        dlgAgregarPrecioVenta.setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money_dollar.png"))); // NOI18N
        jLabel2.setText("Agregar Precio de Venta");

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
        jLabel3.setText("Precio:");

        txtPrecioAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioAgregarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioAgregarKeyTyped(evt);
            }
        });

        btnGuardarAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarAgregar.setText("Guardar");
        btnGuardarAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarAgregarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Tipo Recorte:");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Calibre:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setText("Selección:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Unidad de medida:");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Precio MXN:");

        txtPrecioMXNAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioMXNAgregar.setEnabled(false);
        txtPrecioMXNAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioMXNAgregarKeyTyped(evt);
            }
        });

        cmbMonedaAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonedaAgregarActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Moneda:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGuardarAgregar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbTipoRecorteAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCalibreAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbSeleccionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbMonedaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecioAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecioMXNAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbUnidadMedidaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(19, 19, 19)
                        .addComponent(jLabel17)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel18)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel23)
                        .addGap(19, 19, 19)
                        .addComponent(jLabel3)
                        .addGap(19, 19, 19)
                        .addComponent(jLabel22)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cmbTipoRecorteAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(cmbCalibreAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(cmbSeleccionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(cmbMonedaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(txtPrecioAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(txtPrecioMXNAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(cmbUnidadMedidaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardarAgregar)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgAgregarPrecioVentaLayout = new javax.swing.GroupLayout(dlgAgregarPrecioVenta.getContentPane());
        dlgAgregarPrecioVenta.getContentPane().setLayout(dlgAgregarPrecioVentaLayout);
        dlgAgregarPrecioVentaLayout.setHorizontalGroup(
            dlgAgregarPrecioVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgAgregarPrecioVentaLayout.setVerticalGroup(
            dlgAgregarPrecioVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarPrecioVentaLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dlgEditarPrecioVenta.setBackground(new java.awt.Color(255, 255, 255));
        dlgEditarPrecioVenta.setResizable(false);

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money_dollar.png"))); // NOI18N
        jLabel5.setText("Editar Precio de Venta");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(120, 120, 120))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Precio:");

        btnGuardarEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarEditar.setText("Guardar");
        btnGuardarEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEditarActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("Tipo Recorte:");

        cmbTipoRecorteEditar.setEnabled(false);

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Calibre:");

        cmbCalibreEditar.setEnabled(false);

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Selección:");

        cmbSeleccionEditar.setEnabled(false);

        txtPrecioEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioEditarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioEditarKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Unidad de medida:");

        cmbUnidadMedidaEditar.setEnabled(false);

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("Moneda:");

        cmbMonedaEditar.setEnabled(false);

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Precio MXN:");

        txtPrecioMXNEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioMXNEditar.setEnabled(false);
        txtPrecioMXNEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioMXNEditarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbSeleccionEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbUnidadMedidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMonedaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipoRecorteEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioMXNEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCalibreEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardarEditar)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(cmbTipoRecorteEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(cmbCalibreEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(cmbSeleccionEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(cmbMonedaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPrecioEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(txtPrecioMXNEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cmbUnidadMedidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnGuardarEditar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlgEditarPrecioVentaLayout = new javax.swing.GroupLayout(dlgEditarPrecioVenta.getContentPane());
        dlgEditarPrecioVenta.getContentPane().setLayout(dlgEditarPrecioVentaLayout);
        dlgEditarPrecioVentaLayout.setHorizontalGroup(
            dlgEditarPrecioVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEditarPrecioVentaLayout.setVerticalGroup(
            dlgEditarPrecioVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEditarPrecioVentaLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        tblPrecioVenta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblPrecioVenta.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPrecioVenta.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblPrecioVenta);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
        jButton1.setText("Reporte Precios de Venta");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jToolBar2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Tipo Recorte:");
        jToolBar2.add(jLabel4);

        jLabel11.setText("   ");
        jToolBar2.add(jLabel11);

        cmbTipoRecorte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Todos>" }));
        cmbTipoRecorte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoRecorteActionPerformed(evt);
            }
        });
        jToolBar2.add(cmbTipoRecorte);

        jLabel13.setText("   ");
        jToolBar2.add(jLabel13);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Calibre:");
        jToolBar2.add(jLabel9);

        jLabel12.setText("   ");
        jToolBar2.add(jLabel12);

        cmbCalibre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Todos>" }));
        cmbCalibre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCalibreActionPerformed(evt);
            }
        });
        jToolBar2.add(cmbCalibre);

        jLabel14.setText("   ");
        jToolBar2.add(jLabel14);

        jLabel15.setText("Selección:");
        jToolBar2.add(jLabel15);

        jLabel16.setText("   ");
        jToolBar2.add(jLabel16);

        cmbSeleccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Todos>" }));
        cmbSeleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSeleccionActionPerformed(evt);
            }
        });
        jToolBar2.add(cmbSeleccion);

        jToolBar3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnAgregarProveedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAgregarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
        btnAgregarProveedor.setText("Agregar Precio de Venta");
        btnAgregarProveedor.setFocusable(false);
        btnAgregarProveedor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAgregarProveedor.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAgregarProveedor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProveedorActionPerformed(evt);
            }
        });
        jToolBar3.add(btnAgregarProveedor);

        jLabel10.setText("   ");
        jToolBar3.add(jLabel10);

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
        jToolBar3.add(btnEditar);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
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

    private void btnAgregarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProveedorActionPerformed
        abrirDialogoAgregar();
    }//GEN-LAST:event_btnAgregarProveedorActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        try
        {
            int fila = tblPrecioVenta.getSelectedRow();
            if (fila >= 0)
            {
                abrirDialogoEditar();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de precios de venta","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
            
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de precios de venta","Advertencia",JOptionPane.WARNING_MESSAGE);
            return;
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnGuardarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAgregarActionPerformed
        agregarPrecioVenta();
    }//GEN-LAST:event_btnGuardarAgregarActionPerformed

    private void txtPrecioAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioAgregarKeyTyped
        validarNumeros(evt, txtPrecioAgregar.getText());
    }//GEN-LAST:event_txtPrecioAgregarKeyTyped

    private void btnGuardarEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEditarActionPerformed
        editarPrecioVenta();
    }//GEN-LAST:event_btnGuardarEditarActionPerformed

    private void cmbTipoRecorteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoRecorteActionPerformed
        actualizarTablaPrecioVenta();
    }//GEN-LAST:event_cmbTipoRecorteActionPerformed

    private void cmbCalibreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCalibreActionPerformed
        actualizarTablaPrecioVenta();
    }//GEN-LAST:event_cmbCalibreActionPerformed

    private void cmbSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSeleccionActionPerformed
        actualizarTablaPrecioVenta();
    }//GEN-LAST:event_cmbSeleccionActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        generarReportePrecioVenta();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPrecioEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioEditarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioEditarKeyTyped

    private void txtPrecioMXNAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioMXNAgregarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioMXNAgregarKeyTyped

    private void txtPrecioMXNEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioMXNEditarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioMXNEditarKeyTyped

    private void txtPrecioAgregarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioAgregarKeyReleased
        if (!txtPrecioAgregar.getText().equals(""))
            txtPrecioMXNAgregar.setText(String.valueOf(Double.parseDouble(txtPrecioAgregar.getText()) * lstTipoMoneda.get(cmbMonedaAgregar.getSelectedIndex()).getTipoCambio()));
        else
            txtPrecioMXNAgregar.setText("");
    }//GEN-LAST:event_txtPrecioAgregarKeyReleased

    private void cmbMonedaAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonedaAgregarActionPerformed
        if (cmbMonedaAgregar.getItemCount() > 0)
        {
            if (!txtPrecioAgregar.getText().equals(""))
                txtPrecioMXNAgregar.setText(String.valueOf(Double.parseDouble(txtPrecioAgregar.getText()) * lstTipoMoneda.get(cmbMonedaAgregar.getSelectedIndex()).getTipoCambio()));
            else
                txtPrecioMXNAgregar.setText("");
        }
        
    }//GEN-LAST:event_cmbMonedaAgregarActionPerformed

    private void txtPrecioEditarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioEditarKeyReleased
        if (!txtPrecioEditar.getText().equals(""))
            txtPrecioMXNEditar.setText(String.valueOf(Double.parseDouble(txtPrecioEditar.getText()) * lstTipoMoneda.get(cmbMonedaEditar.getSelectedIndex()).getTipoCambio()));
        else
            txtPrecioMXNEditar.setText("");
    }//GEN-LAST:event_txtPrecioEditarKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarProveedor;
    public javax.swing.JButton btnEditar;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardarAgregar;
    private javax.swing.JButton btnGuardarEditar;
    private javax.swing.JComboBox<String> cmbCalibre;
    private javax.swing.JComboBox<String> cmbCalibreAgregar;
    private javax.swing.JComboBox<String> cmbCalibreEditar;
    private javax.swing.JComboBox<String> cmbMonedaAgregar;
    private javax.swing.JComboBox<String> cmbMonedaEditar;
    private javax.swing.JComboBox<String> cmbSeleccion;
    private javax.swing.JComboBox<String> cmbSeleccionAgregar;
    private javax.swing.JComboBox<String> cmbSeleccionEditar;
    private javax.swing.JComboBox<String> cmbTipoRecorte;
    private javax.swing.JComboBox<String> cmbTipoRecorteAgregar;
    private javax.swing.JComboBox<String> cmbTipoRecorteEditar;
    private javax.swing.JComboBox<String> cmbUnidadMedidaAgregar;
    private javax.swing.JComboBox<String> cmbUnidadMedidaEditar;
    private javax.swing.JDialog dlgAgregarPrecioVenta;
    private javax.swing.JDialog dlgEditarPrecioVenta;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JTable tblPrecioVenta;
    private javax.swing.JTextField txtPrecioAgregar;
    private javax.swing.JTextField txtPrecioEditar;
    private javax.swing.JTextField txtPrecioMXNAgregar;
    private javax.swing.JTextField txtPrecioMXNEditar;
    // End of variables declaration//GEN-END:variables
}
