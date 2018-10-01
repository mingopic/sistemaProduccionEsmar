/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.BajasInventarioCrudoCommands;
import Controlador.ConexionBD;
import Controlador.InventarioCrudoCommands;
import Controlador.PartidaCommands;
import Controlador.PartidaDetalleCommands;
import Modelo.BajasInventarioCrudo;
import Modelo.InventarioCrudo;
import Modelo.Partida;
import Modelo.PartidaDetalle;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
public class PnlPartidas extends javax.swing.JPanel {
    ConexionBD conexion;
    InventarioCrudo ic;
    InventarioCrudoCommands icc;
    Partida p;
    PartidaCommands pc;
    PartidaDetalle pd;
    PartidaDetalleCommands pdc;
    BajasInventarioCrudo bic;
    BajasInventarioCrudoCommands bicc;
    DefaultTableModel dtms = null;
    String[][] datosIC = null;
    String[][] asignados = null;
    String datosPartidas[] = null;
    String recorteSeleccionado = null;
    private final String imagen="/Imagenes/logo_esmar.png";
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] columnas = new String[]
    {
        "Proveedor","No. Camión","Tipo Cuero","Recorte","No. Piezas","Kg. Totales","Prom. Kg/Pieza","Fecha Rec.","idRecCuero","idInvCrudo"
    };
    
    String[] colums = new String[]
    {
        "No. Camión","Proveedor","Recorte","No. Piezas","Pzas Inventario","Prom. Kg/Pza","Kg Totales","Fecha","idInventarioCrudo"
    };
    
    
    
    public PnlPartidas() throws Exception {
        initComponents();
        iniciar();
    }
    
    public void iniciar() throws Exception
    {
        conexion = new ConexionBD();
        actualizarTablaInvCrudo();
        inicializarTablaPartida();
        llenarNoPartida();
    }
    
    public void inicializarTablaPartida()
    {
        dtms=new DefaultTableModel()
        {

            public boolean isCellEditable (int row, int column)
            {
                // Aquí devolvemos true o false según queramos que una celda
                // identificada por fila,columna (row,column), sea o no editable
                if (column == 3)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };
        
        dtms.setColumnIdentifiers(colums);
        tblPartida.setModel(dtms);
        
        tblPartida.getColumnModel().getColumn(8).setMaxWidth(0);
        tblPartida.getColumnModel().getColumn(8).setMinWidth(0);
        tblPartida.getColumnModel().getColumn(8).setPreferredWidth(0);
        
        tblPartida.getTableHeader().setReorderingAllowed(false);
    }
    
    //Método para actualizar la tabla con las recepciones de cuero disponibles para crear partidad
    public void actualizarTablaInvCrudo() 
    {
        ic = new InventarioCrudo();
        
        DefaultTableModel dtm = null;
        
        try
        {
            datosIC = icc.obtenerListaInvCueroCrudo();
            
            if (datosIC != null)
            {
                asignados = new String[datosIC.length][1];
                
                for (int i = 0; i < asignados.length; i++)
                {
                    asignados[i][0] = "0";
                }
            }
            
            dtm = new DefaultTableModel(datosIC, columnas){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblInvCueCrudo.setModel(dtm);
            tblInvCueCrudo.getTableHeader().setReorderingAllowed(false);
            
            tblInvCueCrudo.getColumnModel().getColumn(9).setMaxWidth(0);
            tblInvCueCrudo.getColumnModel().getColumn(9).setMinWidth(0);
            tblInvCueCrudo.getColumnModel().getColumn(9).setPreferredWidth(0);
            
            tblInvCueCrudo.getColumnModel().getColumn(8).setMaxWidth(0);
            tblInvCueCrudo.getColumnModel().getColumn(8).setMinWidth(0);
            tblInvCueCrudo.getColumnModel().getColumn(8).setPreferredWidth(0);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    public void llenarNoPartida() throws Exception
    {
        String[] noPartida = null;
        
        noPartida = pc.llenarNoPartidas();
        
        txtNoPartida.setText(noPartida[0]);
    }
    
    public void totalPiezasPartida()
    {
        int totalPiezas = 0;
        double kgTotal = 0;
        String noRegistros[][] = new String[tblPartida.getRowCount()][2];
        
        if (tblPartida.getRowCount() > 0)
        {
            for (int i = 0; i < noRegistros.length; i++)
            {
                noRegistros[i][0] = tblPartida.getValueAt(i, 3).toString();
                noRegistros[i][1] = tblPartida.getValueAt(i, 6).toString();
                
                totalPiezas = totalPiezas + (Integer.parseInt(noRegistros[i][0]));
                kgTotal = kgTotal + (Double.parseDouble(noRegistros[i][1]));
            }
        }
        
        txtTotalPiezas.setText(String.valueOf(totalPiezas));
        txtKgTotal.setText(String.valueOf(kgTotal));
    }
    
    public void limpiarTabla(JTable tabla){
        try {
            DefaultTableModel modelo=(DefaultTableModel) tabla.getModel();
            int filas=tabla.getRowCount();
            for (int i = 0;filas>i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }
    
    private void validarNumerosEnteros(java.awt.event.KeyEvent evt)
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
    
    private void validarNumerosDecimales(java.awt.event.KeyEvent evt, String textoCaja)
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
    
    //Método que abre el dialogo para recortar un tipo de cuero
    public void abrirDialogoRecortar()
    {   
        dlgRecortar.setVisible(false);
        lblyRecortar.setVisible(false);
        txtNoPiezasRecortar2.setVisible(false);
        lblPiezasDe2.setVisible(false);
        lblTipoCuero2.setVisible(false);
        txtKgRecortar2.setVisible(false);
        lblKgRecortar2.setVisible(false);
        
        String[] tipoRecorte = null;
        String aRecortar = ic.getRecorte();
        
        if (aRecortar.equals("Entero"))
        {
            tipoRecorte = new String[] { "Delantero/Crupon", "Lados" };
        }
        else if (aRecortar.equals("Crupon"))
        {
            recorteSeleccionado = "Centro";
        }
        else if (aRecortar.equals("Delantero"))
        {
            recorteSeleccionado = "Delantero Suela";
        }
        else if (aRecortar.equals("Lados"))
        {
            recorteSeleccionado = "Centro/Delantero Suela";
        }
        else
        {
            JOptionPane.showMessageDialog(null, "No se puede recortar este \ntipo de cuero","Advertencia",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //Validar que se haya seleccionado un tipo de recorte
        if (tipoRecorte != null)
        {
            recorteSeleccionado = (String) JOptionPane.showInputDialog(null, "Seleccione tipo de Recorte","Recortar",JOptionPane.INFORMATION_MESSAGE, null, tipoRecorte, tipoRecorte[0]);
        }
        
        if (recorteSeleccionado == null)
        {
            return;
        }
        
        try 
        {
            lblTipoCueroRecortar.setText(aRecortar);
            txtNoPiezasRecortar.setText(String.valueOf(ic.getNoPiezasActual()));
            txtNoPiezasRecortar1.setText(String.valueOf(ic.getNoPiezasActual()*2));
            txtKgRecortar1.setText("0");
            
            if (recorteSeleccionado.equals("Delantero/Crupon"))
            {
                lblyRecortar.setVisible(true);
                txtNoPiezasRecortar2.setVisible(true);
                lblPiezasDe2.setVisible(true);
                lblTipoCuero2.setVisible(true);
                txtKgRecortar2.setVisible(true);
                lblKgRecortar2.setVisible(true);
                
                txtNoPiezasRecortar1.setText(txtNoPiezasRecortar.getText());
                lblTipoCuero1.setText("Delantero");
                txtKgRecortar1.setText("0");
                
                txtNoPiezasRecortar2.setText(txtNoPiezasRecortar.getText());
                lblTipoCuero2.setText("Crupon");
                txtKgRecortar2.setText("0");
            }
            else if(recorteSeleccionado.equals("Centro/Delantero Suela"))
            {
                lblyRecortar.setVisible(true);
                txtNoPiezasRecortar2.setVisible(true);
                lblPiezasDe2.setVisible(true);
                lblTipoCuero2.setVisible(true);
                txtKgRecortar2.setVisible(true);
                lblKgRecortar2.setVisible(true);
                
                txtNoPiezasRecortar1.setText(txtNoPiezasRecortar.getText());
                lblTipoCuero1.setText("Centro");
                txtKgRecortar1.setText("0");
                
                txtNoPiezasRecortar2.setText(txtNoPiezasRecortar.getText());
                lblTipoCuero2.setText("Delantero Suela");
                txtKgRecortar2.setText("0");
            }
            else
            {
                lblTipoCuero1.setText(recorteSeleccionado);
                txtKgRecortar1.setText("0");
            }
            
            dlgRecortar.setSize(430, 330);
            dlgRecortar.setPreferredSize(dlgRecortar.getSize());
            dlgRecortar.setLocationRelativeTo(null);
            dlgRecortar.setAlwaysOnTop(true);
            dlgRecortar.setVisible(true);
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dlgRecortar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgRecortar.setVisible(true);
        }
    }
    
    //Metodo para inicializar los campos de dlgEliInvCrudo
    public void inicializarCamposEliPzaInvCrudo() throws Exception
    {
        txtNoPiezasEliminar.setText("");
        txtrMotivo.setText("");
        
        int fila = tblInvCueCrudo.getSelectedRow();
        
        txtProveedor.setText(String.valueOf(tblInvCueCrudo.getValueAt(fila, 0)));
        txtNoCamion.setText(String.valueOf(tblInvCueCrudo.getValueAt(fila, 1)));
        txtTipoCuero.setText(String.valueOf(tblInvCueCrudo.getValueAt(fila, 2)));
        txtTipoRecorte.setText(String.valueOf(tblInvCueCrudo.getValueAt(fila, 3)));
        txtNoPiezasActuales.setText(String.valueOf(tblInvCueCrudo.getValueAt(fila, 4)));
    }
    
    //Método que abre el dialogo para eliminar piezas de una recepción de cuero 
    public void abrirDialogoEliPzaInvCrudo() throws Exception
    {
        inicializarCamposEliPzaInvCrudo();
        
        dlgEliPzaInvCrudo.setSize(430, 490);
        dlgEliPzaInvCrudo.setPreferredSize(dlgEliPzaInvCrudo.getSize());
        dlgEliPzaInvCrudo.setLocationRelativeTo(null);
        dlgEliPzaInvCrudo.setAlwaysOnTop(true);
        dlgEliPzaInvCrudo.setVisible(true);
    }
    
    //Método para realizar entrada de material y actualizar inventarios
    public void eliminarPiezasInvCrudo () throws Exception
    {
        if ( !txtNoPiezasEliminar.getText().isEmpty() && Integer.parseInt(txtNoPiezasEliminar.getText()) != 0)
        {
            try 
            {
                if (Integer.parseInt(txtNoPiezasEliminar.getText()) > Integer.parseInt(txtNoPiezasActuales.getText()))
                {
                    JOptionPane.showMessageDialog(dlgEliPzaInvCrudo, "El numero de piezas debe ser menor o igual al número de piezas actuales");
                }
                else
                {
                    int fila = tblInvCueCrudo.getSelectedRow();
                    bic = new BajasInventarioCrudo();
                    bicc = new BajasInventarioCrudoCommands();
                    
//                    double promKg = Double.parseDouble(tblInvCueCrudo.getValueAt(fila, 6).toString());
//                    double kg = promKg * Integer.parseInt(txtNoPiezasEliminar.getText());
//                    
//                    if (kg > Double.parseDouble(tblInvCueCrudo.getValueAt(fila, 5).toString()))
//                    {
//                        kg = Double.parseDouble(tblInvCueCrudo.getValueAt(fila, 5).toString());
//                    }

                    bic.setIdInventarioCrudo(Integer.parseInt(datosIC[fila][9]));
                    bic.setNoPiezas(Integer.parseInt(txtNoPiezasEliminar.getText()));
                    bic.setMotivo(txtrMotivo.getText());
//                    bic.setKgTotal(kg);

                    bicc.agregarBajaInvCrudo(bic);
                    icc.actualizarNoPiezasBaja(bic);
                    actualizarTablaInvCrudo();
                    dlgEliPzaInvCrudo.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Baja realizada correctamente");
                }
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                dlgEliPzaInvCrudo.setVisible(false);                
                JOptionPane.showMessageDialog(null, "Error de conexión", "Error",JOptionPane.ERROR_MESSAGE);
            }   
        }
        else
        {
            dlgEliPzaInvCrudo.setVisible(false);
            JOptionPane.showMessageDialog(null, "Capture no. Piezas a eliminar","Mensaje",JOptionPane.WARNING_MESSAGE);
            dlgEliPzaInvCrudo.setVisible(true);
        }
    }
    
    public void generarReporteBajasInvCrudo()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteBajasInvCrudo.jasper");
            
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

        dlgRecortar = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtNoPiezasRecortar = new javax.swing.JTextField();
        btnGuardarRecorte = new javax.swing.JButton();
        lblTipoCueroRecortar = new javax.swing.JLabel();
        txtNoPiezasRecortar1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblTipoCuero1 = new javax.swing.JLabel();
        txtNoPiezasRecortar2 = new javax.swing.JTextField();
        lblPiezasDe2 = new javax.swing.JLabel();
        lblyRecortar = new javax.swing.JLabel();
        lblTipoCuero2 = new javax.swing.JLabel();
        txtKgRecortar1 = new javax.swing.JTextField();
        txtKgRecortar2 = new javax.swing.JTextField();
        lblKgRecortar1 = new javax.swing.JLabel();
        lblKgRecortar2 = new javax.swing.JLabel();
        dlgEliPzaInvCrudo = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        txtNoPiezasActuales = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txtNoPiezasEliminar = new javax.swing.JTextField();
        btnRealizarEntradaEnvSemi = new javax.swing.JButton();
        btnCancelarAgregarEnvSemi = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNoCamion = new javax.swing.JTextField();
        txtProveedor = new javax.swing.JTextField();
        txtTipoCuero = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtTipoRecorte = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtrMotivo = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvCueCrudo = new javax.swing.JTable();
        btnAsignarEntrada = new javax.swing.JButton();
        btnRecortar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNoPartida = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPartida = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtTotalPiezas = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtKgTotal = new javax.swing.JTextField();

        jPanel7.setBackground(new java.awt.Color(0, 204, 204));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cut_red.png"))); // NOI18N
        jLabel13.setText("Recortar Cuero");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addGap(133, 133, 133))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Piezas de");

        txtNoPiezasRecortar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasRecortar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNoPiezasRecortar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoPiezasRecortarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasRecortarKeyTyped(evt);
            }
        });

        btnGuardarRecorte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarRecorte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarRecorte.setText("Guardar");
        btnGuardarRecorte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarRecorteActionPerformed(evt);
            }
        });

        lblTipoCueroRecortar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTipoCueroRecortar.setText("TipoCuero");

        txtNoPiezasRecortar1.setEditable(false);
        txtNoPiezasRecortar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasRecortar1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("=");
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Piezas de");

        lblTipoCuero1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTipoCuero1.setText("TipoCuero");

        txtNoPiezasRecortar2.setEditable(false);
        txtNoPiezasRecortar2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasRecortar2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblPiezasDe2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPiezasDe2.setText("Piezas de");

        lblyRecortar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblyRecortar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblyRecortar.setText("y");
        lblyRecortar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblTipoCuero2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTipoCuero2.setText("TipoCuero");

        txtKgRecortar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtKgRecortar1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtKgRecortar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKgRecortar1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKgRecortar1KeyTyped(evt);
            }
        });

        txtKgRecortar2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtKgRecortar2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtKgRecortar2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKgRecortar2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKgRecortar2KeyTyped(evt);
            }
        });

        lblKgRecortar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblKgRecortar1.setText("Kg");

        lblKgRecortar2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblKgRecortar2.setText("Kg");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtNoPiezasRecortar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTipoCueroRecortar))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblyRecortar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(txtNoPiezasRecortar2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblPiezasDe2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblTipoCuero2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                                            .addComponent(txtNoPiezasRecortar1, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblTipoCuero1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(txtKgRecortar2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblKgRecortar2))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(txtKgRecortar1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblKgRecortar1)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGuardarRecorte)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasRecortar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(lblTipoCueroRecortar))
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasRecortar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(lblTipoCuero1)
                    .addComponent(txtKgRecortar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKgRecortar1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblyRecortar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasRecortar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPiezasDe2)
                    .addComponent(lblTipoCuero2)
                    .addComponent(txtKgRecortar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKgRecortar2))
                .addGap(33, 33, 33)
                .addComponent(btnGuardarRecorte)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgRecortarLayout = new javax.swing.GroupLayout(dlgRecortar.getContentPane());
        dlgRecortar.getContentPane().setLayout(dlgRecortarLayout);
        dlgRecortarLayout.setHorizontalGroup(
            dlgRecortarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgRecortarLayout.setVerticalGroup(
            dlgRecortarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgRecortarLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel55.setText("No. Camión:");

        txtNoPiezasActuales.setEditable(false);
        txtNoPiezasActuales.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasActuales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasActualesKeyTyped(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel32.setText("Proveedor:");

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel53.setText("Motivo:");

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

        jPanel9.setBackground(new java.awt.Color(0, 204, 51));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
        jLabel17.setText("Eliminar Piezas");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("No. piezas actuales:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("No. piezas a eliminar:");

        txtNoCamion.setEditable(false);
        txtNoCamion.setBackground(new java.awt.Color(255, 255, 255));

        txtProveedor.setEditable(false);
        txtProveedor.setBackground(new java.awt.Color(255, 255, 255));

        txtTipoCuero.setEditable(false);
        txtTipoCuero.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Tipo de cuero:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Tipo de recorte:");

        txtTipoRecorte.setEditable(false);
        txtTipoRecorte.setBackground(new java.awt.Color(255, 255, 255));

        txtrMotivo.setColumns(20);
        txtrMotivo.setRows(5);
        jScrollPane3.setViewportView(txtrMotivo);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel32)
                                    .addComponent(jLabel55)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel6)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNoPiezasActuales, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                .addComponent(txtNoCamion)
                                .addComponent(txtTipoCuero)
                                .addComponent(txtTipoRecorte))
                            .addComponent(txtNoPiezasEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 30, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRealizarEntradaEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelarAgregarEnvSemi)))
                .addGap(6, 6, 6))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txtNoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTipoCuero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTipoRecorte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasActuales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntradaEnvSemi)
                    .addComponent(btnCancelarAgregarEnvSemi))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEliPzaInvCrudoLayout = new javax.swing.GroupLayout(dlgEliPzaInvCrudo.getContentPane());
        dlgEliPzaInvCrudo.getContentPane().setLayout(dlgEliPzaInvCrudoLayout);
        dlgEliPzaInvCrudoLayout.setHorizontalGroup(
            dlgEliPzaInvCrudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEliPzaInvCrudoLayout.setVerticalGroup(
            dlgEliPzaInvCrudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setPreferredSize(new java.awt.Dimension(1049, 508));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Inventario de Cuero Crudo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblInvCueCrudo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nombre Proveedor", "Número Camión", "Tipo Cuero", "Recorte", "No. Piezas Actual", "Kg. Total", "Prom. Kg/Pieza", "Fecha Recepción"
            }
        ));
        tblInvCueCrudo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblInvCueCrudo);

        btnAsignarEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAsignarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/arrow_turn_right.png"))); // NOI18N
        btnAsignarEntrada.setText("Asignar Entrada");
        btnAsignarEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarEntradaActionPerformed(evt);
            }
        });

        btnRecortar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnRecortar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cut_red.png"))); // NOI18N
        btnRecortar.setText("Recortar");
        btnRecortar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecortarActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        jButton1.setText("Eliminar Piezas");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
        jButton2.setText("Reporte Piezas Eliminadas");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRecortar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAsignarEntrada)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAsignarEntrada)
                        .addComponent(btnRecortar)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Partida");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("No. Partida");

        txtNoPartida.setEditable(false);
        txtNoPartida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnEliminar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setToolTipText("");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        tblPartida.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No. Camión", "Proveedor", "Recorte", "No. Piezas", "Pzas Inv", "Prom. Kg/Pza", "Kg Totales", "Fecha"
            }
        ));
        tblPartida.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPartida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPartidaMousePressed(evt);
            }
        });
        tblPartida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPartidaKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblPartida);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Total piezas");

        txtTotalPiezas.setEditable(false);
        txtTotalPiezas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Kg Total");

        txtKgTotal.setEditable(false);
        txtKgTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNoPartida, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKgTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnEliminar)
                    .addComponent(txtNoPartida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtKgTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void btnAsignarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarEntradaActionPerformed
        try
        {
            int fila = tblInvCueCrudo.getSelectedRow();
        
            if (asignados[fila][0].equals("1"))
            {
                JOptionPane.showMessageDialog(null, "Esta recepción de cuero ya se encuentra asignada");
            }
            else
            {
                datosPartidas = new String[9];

                datosPartidas[0]= tblInvCueCrudo.getValueAt(fila, 1).toString(); // No. Camión
                datosPartidas[1]= tblInvCueCrudo.getValueAt(fila, 0).toString(); // Proveedor
                datosPartidas[2]= tblInvCueCrudo.getValueAt(fila, 3).toString(); // Recorte
                datosPartidas[3]= tblInvCueCrudo.getValueAt(fila, 4).toString(); // No. Piezas
                datosPartidas[4]= tblInvCueCrudo.getValueAt(fila, 4).toString(); // Pzas Inv
                datosPartidas[5]= tblInvCueCrudo.getValueAt(fila, 6).toString(); // Prom. Kg/Pza
                datosPartidas[6]= String.valueOf((Integer.parseInt(datosPartidas[4])) * (Double.parseDouble(datosPartidas[5])));
                datosPartidas[6]= (String.format("%.2f",Double.parseDouble(datosPartidas[6])));
                datosPartidas[7]= tblInvCueCrudo.getValueAt(fila, 7).toString();
                datosPartidas[8]= tblInvCueCrudo.getValueAt(fila, 9).toString();

                dtms.addRow(datosPartidas);
                totalPiezasPartida();
                asignados[fila][0] = "1";
            }
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Inventario de Cuero Crudo","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnAsignarEntradaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila;
        try
        {
            fila=this.tblPartida.getSelectedRow();
            
            for (int i = 0; i < datosIC.length; i++)
            {
                if (datosIC[i][1].equals(tblPartida.getValueAt(fila, 0)) && datosIC[i][0].equals(tblPartida.getValueAt(fila, 1)) && datosIC[i][6].equals(tblPartida.getValueAt(fila, 6)))
                {
                    asignados[i][0] = "0";
                }
            }
            
            dtms.removeRow(fila);
            totalPiezasPartida();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de partidas","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (tblPartida.getRowCount() <= 0)
        {
            JOptionPane.showMessageDialog(null, "No hay registros para guardar","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            if (JOptionPane.showConfirmDialog(null, "Realmente desea guardar la partida", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
            {
                int filas = tblPartida.getRowCount();
                p = new Partida();
                pc = new PartidaCommands();
                PartidaDetalle[] datosPD = new PartidaDetalle[filas];
                pdc = new PartidaDetalleCommands();
                InventarioCrudo[] datosInC = new InventarioCrudo[filas];
                String[][] datosPar = new String[filas][6];

                try
                {   
                    p.setNoPartida(Integer.parseInt(txtNoPartida.getText()));
                    p.setNoTotalPiezas(Integer.parseInt(txtTotalPiezas.getText()));
                    p.setIdProceso(1);
                    
                    pc.agregarPartida(p);
                    
                    for (int i = 0; i < filas; i++)
                    {
                        datosPD[i] = new PartidaDetalle();
                        datosInC[i] = new InventarioCrudo();
                        
                        datosPD[i].setNoPiezas(Integer.parseInt(tblPartida.getValueAt(i, 3).toString()));
                        datosPD[i].setIdPartida(pc.obtenerUltIdPartida());
                        datosPD[i].setRecorte(tblPartida.getValueAt(i, 2).toString());
                        
                        datosPar[i][0] = tblPartida.getValueAt(i, 1).toString();
                        datosPar[i][1] = tblPartida.getValueAt(i, 0).toString();
                        datosPar[i][2] = tblPartida.getValueAt(i, 7).toString();
                        datosPar[i][3] = tblPartida.getValueAt(i, 3).toString();
                        datosPar[i][4] = tblPartida.getValueAt(i, 6).toString();
                        datosPar[i][5] = tblPartida.getValueAt(i, 8).toString();
                    }
                    
                    pdc.agregarPartidaDetalle(datosPD,datosPar);
                    icc.actualizarNoPiezasActual(datosPar);
                    JOptionPane.showMessageDialog(null, "Registro insertado correctamente");
                    limpiarTabla(tblPartida);
                    llenarNoPartida();
                    actualizarTablaInvCrudo();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ingrese datos validos","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tblPartidaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPartidaKeyReleased
        int fila = tblPartida.getSelectedRow();
        String piezasUtilizar = tblPartida.getValueAt(fila, 3).toString();
        String piezas = tblPartida.getValueAt(fila, 4).toString();
        String promKgPza = tblPartida.getValueAt(fila, 5).toString();
        
        try
        {
            if (Integer.parseInt(piezasUtilizar) < 0)
            {
                JOptionPane.showMessageDialog(null, "Solo puede insertar numeros enteros mayores o igual a 0");
                tblPartida.setValueAt("0", fila, 3);
            }
            else
            {
                if (Integer.parseInt(piezasUtilizar) > Integer.parseInt(piezas))
                {
                    JOptionPane.showMessageDialog(null, "El numero de piezas a utilizar no puede ser mayor al numero de piezas");
                    tblPartida.setValueAt("0", fila, 3);
                }
                else
                {
                    String kgTotales = String.valueOf((Integer.parseInt(piezasUtilizar)) * (Double.parseDouble(promKgPza)));
                    kgTotales = (String.format("%.2f",Double.parseDouble(kgTotales)));
                    tblPartida.setValueAt(kgTotales, fila, 6);
                }
            }
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Solo puede insertar numeros enteros mayores o igual a 0");
            tblPartida.setValueAt("0", fila, 3);
        }
        totalPiezasPartida();
    }//GEN-LAST:event_tblPartidaKeyReleased

    private void tblPartidaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartidaMousePressed
         if(evt.getClickCount()==1)
        {
            for (int fila = 0; fila < tblPartida.getRowCount(); fila++)
            {
                String piezasUtilizar = tblPartida.getValueAt(fila, 3).toString();
                String piezas = tblPartida.getValueAt(fila, 4).toString();
                String promKgPza = tblPartida.getValueAt(fila, 5).toString();

                try
                {
                    if (Integer.parseInt(piezasUtilizar) < 0)
                    {
                        JOptionPane.showMessageDialog(null, "Solo puede insertar numeros enteros mayores o igual a 0");
                        tblPartida.setValueAt("0", fila, 3);
                        tblPartida.setValueAt("0", fila, 6);
                    }
                    else
                    {
                        if (Integer.parseInt(piezasUtilizar) > Integer.parseInt(piezas))
                        {
                            JOptionPane.showMessageDialog(null, "El numero de piezas a utilizar no puede ser mayor al numero de piezas");
                            tblPartida.setValueAt("0", fila, 3);
                            tblPartida.setValueAt("0", fila, 6);
                        }
                        else
                        {
                            String kgTotales = String.valueOf((Integer.parseInt(piezasUtilizar)) * (Double.parseDouble(promKgPza)));
                            kgTotales = (String.format("%.2f",Double.parseDouble(kgTotales)));
                            tblPartida.setValueAt(kgTotales, fila, 6);
                        }
                    }
                } catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null, "Solo puede insertar numeros enteros mayores o igual a 0");
                    tblPartida.setValueAt("0", fila, 3);
                    tblPartida.setValueAt("0", fila, 6);
                }
            }
            totalPiezasPartida();
        }
    }//GEN-LAST:event_tblPartidaMousePressed

    private void btnRecortarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecortarActionPerformed
        try 
        {
            ic = new InventarioCrudo();
            int i = tblInvCueCrudo.getSelectedRow();
            
            ic.setIdInventarioCrudo(Integer.parseInt(datosIC[i][9]));
            ic.setRecorte(datosIC[i][3]);
            ic.setIdRecepcionCuero(Integer.parseInt(datosIC[i][8]));
            ic.setNoPiezasActual(Integer.parseInt(datosIC[i][4]));
            
            abrirDialogoRecortar();
        }
        catch (Exception e) 
        {
            dlgRecortar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la tabla de Inventario de Cuero Crudo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRecortarActionPerformed

    private void txtNoPiezasRecortarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasRecortarKeyReleased
        if (recorteSeleccionado.equals("Delantero/Crupon") || recorteSeleccionado.equals("Centro/Delantero Suela"))
        {
            if (txtNoPiezasRecortar.getText().equals(""))
            {
                txtNoPiezasRecortar.setText("0");
                txtNoPiezasRecortar1.setText("0");
                txtNoPiezasRecortar2.setText("0");
            }
            else
            {
                txtNoPiezasRecortar1.setText(txtNoPiezasRecortar.getText());
                txtNoPiezasRecortar2.setText(txtNoPiezasRecortar.getText());
            }
        }
        else
        {
            if (txtNoPiezasRecortar.getText().equals(""))
            {
                txtNoPiezasRecortar.setText("0");
                txtNoPiezasRecortar1.setText("0");
            }
            else
            txtNoPiezasRecortar1.setText(String.valueOf(Integer.parseInt(txtNoPiezasRecortar.getText())*2));
        }
    }//GEN-LAST:event_txtNoPiezasRecortarKeyReleased

    private void txtNoPiezasRecortarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasRecortarKeyTyped
        validarNumerosEnteros(evt);
    }//GEN-LAST:event_txtNoPiezasRecortarKeyTyped

    private void btnGuardarRecorteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarRecorteActionPerformed
        int noPiezasRecortar = 0;
        double kgRecortar1 = 0;
        double kgRecortar2 = 0;
        
        try
        {
            noPiezasRecortar = Integer.parseInt(txtNoPiezasRecortar.getText());
            kgRecortar1 = Double.parseDouble(txtKgRecortar1.getText());
            kgRecortar2 = Double.parseDouble(txtKgRecortar2.getText());
        }
        catch (Exception e)
        {

        }

        if (noPiezasRecortar > 0 && noPiezasRecortar <= ic.getNoPiezasActual())
        {
            try
            {
                int noPiezas1;
                int noPiezas2 = 0;
                Double kg1;
                Double kg2 = 0.0;
                
                noPiezas1 = Integer.parseInt(txtNoPiezasRecortar1.getText());
                kg1 = Double.parseDouble(txtKgRecortar1.getText());
                
                switch (recorteSeleccionado)
                {
                    case "Delantero/Crupon":
                    ic.setIdTipoRecorte(0);
                    noPiezas2 = Integer.parseInt(txtNoPiezasRecortar2.getText());
                    kg2 = Double.parseDouble(txtKgRecortar2.getText());
                    break;
                    
                    case "Centro/Delantero Suela":
                    ic.setIdTipoRecorte(1);
                    noPiezas2 = Integer.parseInt(txtNoPiezasRecortar2.getText());
                    kg2 = Double.parseDouble(txtKgRecortar2.getText());
                    break;
                    
                    case "Lados":
                    ic.setIdTipoRecorte(4);
                    noPiezas2 = 0;
                    kg2 = 0.0;
                    break;
                    
                    case "Centro":
                    ic.setIdTipoRecorte(8);
                    noPiezas2 = 0;
                    kg2 = 0.0;
                    break;
                    
                    case "Delantero Suela":
                    ic.setIdTipoRecorte(7);
                    noPiezas2 = 0;
                    kg2 = 0.0;
                    break;
                    
                    default:
                    break;
                }
                InventarioCrudoCommands icc = new InventarioCrudoCommands();
                icc.agregarRecorte(ic, noPiezasRecortar, noPiezas1, noPiezas2, kg1, kg2);
                dlgRecortar.setVisible(false);
                JOptionPane.showMessageDialog(null,"Recorte relizado correctamente");
                limpiarTabla(tblPartida);
                llenarNoPartida();
                actualizarTablaInvCrudo();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Error al realizar recorte","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            if (noPiezasRecortar == 0)
            {
                dlgRecortar.setVisible(false);
                JOptionPane.showMessageDialog(null,"Piezas a recortar debe ser mayor a 0","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgRecortar.setVisible(true);
            }
            else
            {
                dlgRecortar.setVisible(false);
                JOptionPane.showMessageDialog(null,"Piezas a recortar insuficientes para la partida seleccionada","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgRecortar.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnGuardarRecorteActionPerformed

    private void txtKgRecortar1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgRecortar1KeyTyped
        validarNumerosDecimales(evt, txtKgRecortar1.getText());
    }//GEN-LAST:event_txtKgRecortar1KeyTyped

    private void txtKgRecortar2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgRecortar2KeyTyped
        validarNumerosDecimales(evt, txtKgRecortar2.getText());
    }//GEN-LAST:event_txtKgRecortar2KeyTyped

    private void txtKgRecortar1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgRecortar1KeyReleased
        if (txtKgRecortar1.getText().equals(""))
        {
            txtKgRecortar1.setText("0");
        }
    }//GEN-LAST:event_txtKgRecortar1KeyReleased

    private void txtKgRecortar2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgRecortar2KeyReleased
        if (txtKgRecortar2.getText().equals(""))
        {
            txtKgRecortar2.setText("0");
        }
    }//GEN-LAST:event_txtKgRecortar2KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            int fila = tblInvCueCrudo.getSelectedRow();
            String piezas = (String.valueOf(tblInvCueCrudo.getValueAt(fila, 4)));
            int numPiezasActuales = Integer.parseInt(piezas);

            if (numPiezasActuales != 0)
            {
                abrirDialogoEliPzaInvCrudo();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "El número de piezas actuales debe ser mayor a 0","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de inventario crudo","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            eliminarPiezasInvCrudo();
        }
        catch (Exception ex)
        {
            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRealizarEntradaEnvSemiActionPerformed

    private void btnCancelarAgregarEnvSemiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarEnvSemiActionPerformed
        dlgEliPzaInvCrudo.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarEnvSemiActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        actualizarTablaInvCrudo();
        generarReporteBajasInvCrudo();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignarEntrada;
    private javax.swing.JButton btnCancelarAgregarEnvSemi;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardarRecorte;
    private javax.swing.JButton btnRealizarEntradaEnvSemi;
    private javax.swing.JButton btnRecortar;
    private javax.swing.JDialog dlgEliPzaInvCrudo;
    private javax.swing.JDialog dlgRecortar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JLabel lblKgRecortar1;
    private javax.swing.JLabel lblKgRecortar2;
    private javax.swing.JLabel lblPiezasDe2;
    private javax.swing.JLabel lblTipoCuero1;
    private javax.swing.JLabel lblTipoCuero2;
    private javax.swing.JLabel lblTipoCueroRecortar;
    private javax.swing.JLabel lblyRecortar;
    private javax.swing.JTable tblInvCueCrudo;
    private javax.swing.JTable tblPartida;
    private javax.swing.JTextField txtKgRecortar1;
    private javax.swing.JTextField txtKgRecortar2;
    private javax.swing.JTextField txtKgTotal;
    private javax.swing.JTextField txtNoCamion;
    private javax.swing.JTextField txtNoPartida;
    private javax.swing.JTextField txtNoPiezasActuales;
    private javax.swing.JTextField txtNoPiezasEliminar;
    private javax.swing.JTextField txtNoPiezasRecortar;
    private javax.swing.JTextField txtNoPiezasRecortar1;
    private javax.swing.JTextField txtNoPiezasRecortar2;
    private javax.swing.JTextField txtProveedor;
    private javax.swing.JTextField txtTipoCuero;
    private javax.swing.JTextField txtTipoRecorte;
    private javax.swing.JTextField txtTotalPiezas;
    private javax.swing.JTextArea txtrMotivo;
    // End of variables declaration//GEN-END:variables
}
