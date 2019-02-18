/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.ConexionBD;
import Controlador.ConfGastosFabricacionCommands;
import Controlador.ConfPrecioManoDeObraCommands;
import Controlador.ConfiPrecioCueroCommands;
import Controlador.ConfiguracionMermaCommands;
import Controlador.ControladorUsuario;
import Controlador.CostoGarraCommands;
import Controlador.RangoPesoCueroCommands;
import Controlador.RolesXUsuarioCommands;
import Modelo.ConfGastosFabricacion;
import Modelo.ConfPrecioCuero;
import Modelo.ConfPrecioManoDeObra;
import Modelo.ConfiguracionMerma;
import Modelo.CostoGarra;
import Modelo.RangoPesoCuero;
import Modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
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
public class FrmPrincipal extends javax.swing.JFrame {
    PnlPrincipal pnlPrincipal;
    PnlRecepcionCuero pnlRecepcionCuero;
    PnlInsXproc pnlInsXproc;
    PnlProveedores pnlProveedores;
    PnlSubProcesos pnlSubProcesos;
    PnlPartidas pnlPartidas;
    PnlExportar pnlExportar; 
    PnlImportar pnlImportar;
    PnlTambores pnlTambores;
    PnlCalibres pnlCalibres;
    PnlPrecioVenta pnlPrecioVenta;
    PnlCross pnlCross;
    PnlSemiterminado pnlSemiterminado;
    PnlFichaProduccion pnlFichaProduccion;
    PnlUsuarios pnlUsuarios;
    PnlTerminado pnlTerminado;
    PnlProduccionEnProceso pnlProduccionEnProceso;
    ConexionBD conexionBD;
    ConfiguracionMerma cm;
    ConfiguracionMermaCommands cmc;
    RangoPesoCuero rpc;
    RangoPesoCueroCommands rpcc;
    CostoGarra cg;
    CostoGarraCommands cgc;
    public static String[] roles;
    List<ConfPrecioCuero> lstConfPrecioCuero;
    List<ConfPrecioManoDeObra> lstConfPrecioManoDeObra;
    List<ConfGastosFabricacion> lstConfGastosFabricacion;
    private final String imagen="/Imagenes/logo_esmar.png";
    /**
     * Creates new form FrmPrincipal
     */
    public FrmPrincipal() throws IOException, IOException {
        initComponents();
        inicializar();
    }
    
    private void inicializar() throws IOException
    {
        conexionBD = new ConexionBD();
        //Colocar icono de la aplicación
        Image ico = new ImageIcon(getClass().getResource("/Imagenes/esmar.png")).getImage();
        setIconImage(ico);        
        //Titulo de la aplicación
        this.setTitle("Sistema de producción V.1.0");
        
        ImageIcon fot = new ImageIcon("src/Imagenes/logoEsmar_CH.png");
        Icon icono = new ImageIcon(fot.getImage().getScaledInstance(lblLogoSistema.getWidth(), lblLogoSistema.getHeight(), Image.SCALE_DEFAULT));
        lblLogoSistema.setIcon(icono);
        this.repaint();
        
        //Inicializamos las propiedades del dialogo:
        dlgLogin.setSize(560, 322);
        dlgLogin.setPreferredSize(dlgLogin.getSize());
        dlgLogin.setLocationRelativeTo(null);
        dlgLogin.setAlwaysOnTop(true);
        dlgLogin.setVisible(true);
        
        btnRecepcionCuero.setVisible(false);
        btnPartidas.setVisible(false);
        btnFichasProduccion.setVisible(false);
        btnProdEnProc.setVisible(false);
        btnCross.setVisible(false);
        btnSemiterminado.setVisible(false);
        btnTerminado.setVisible(false);
        jmpCatalogos.setVisible(false);
        jmpConfiguraciones.setVisible(false);
        jmpBaseDeDatos.setVisible(false);
        
        jmCalibres.setVisible(false);
        jmTambores.setVisible(false);
        jmSubProcesos.setVisible(false);
        jmProveedores.setVisible(false);
        jmUsuarios.setVisible(false);
        jmPrecioVenta.setVisible(false);
    }
    
    
    private void validarUsuario()
    {
        ControladorUsuario controladorUsuario = new ControladorUsuario(conexionBD);
        Usuario u = new Usuario();
        u.setUserName(txtUsuario.getText());
        u.setPassword(new String(ptxtContrasenia.getPassword()));
        
        try
        {
            if (controladorUsuario.validarUsuario(u))
            {   
                roles = RolesXUsuarioCommands.obtenerRolXUsuario(u);
                
                dlgLogin.setVisible(false);
                this.setVisible(true);
                this.setExtendedState(MAXIMIZED_BOTH);
                pnlPrincipal=new PnlPrincipal();
                pnlPrincipalx.removeAll();        
                pnlPrincipalx.add(pnlPrincipal, BorderLayout.CENTER);
                pnlPrincipalx.paintAll(pnlPrincipal.getGraphics());
                ImageIcon ico=new ImageIcon(".\\src\\Imagenes\\house.png");
                lblVentana.setIcon(ico);
                lblNombreUsuario.setText(u.getNombre());
                
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].equals("Contabilidad"))
                    {
                        btnRecepcionCuero.setVisible(true);
                        btnPartidas.setVisible(true);
                        btnProdEnProc.setVisible(true);
                        btnCross.setVisible(true);
                        btnSemiterminado.setVisible(true);
                        btnTerminado.setVisible(true);
                        
                        jmpCatalogos.setVisible(true);
                        jmPrecioVenta.setVisible(true);
                        break;
                    }
                }
                
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].equals("Cross"))
                    {
                        btnCross.setVisible(true);
                        break;
                    }
                }
                
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].equals("Produccion"))
                    {
                        btnRecepcionCuero.setVisible(true);
                        btnPartidas.setVisible(true);
                        btnFichasProduccion.setVisible(true);
                        btnProdEnProc.setVisible(true);
                        btnCross.setVisible(true);
                        btnSemiterminado.setVisible(true);
                        btnTerminado.setVisible(true);
                        jmpCatalogos.setVisible(true);
                        jmCalibres.setVisible(true);
                        jmTambores.setVisible(true);
                        jmSubProcesos.setVisible(true);
                        jmProveedores.setVisible(true);
                        jmpConfiguraciones.setVisible(true);
                        break;
                    }
                }
                
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].equals("Semiterminado"))
                    {
                        btnSemiterminado.setVisible(true);
                        jmpCatalogos.setVisible(true);
                        jmCalibres.setVisible(true);
                        break;
                    }
                }
                
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].equals("Sistemas"))
                    {
                        btnRecepcionCuero.setVisible(true);
                        btnPartidas.setVisible(true);
                        btnFichasProduccion.setVisible(true);
                        btnProdEnProc.setVisible(true);
                        btnCross.setVisible(true);
                        btnSemiterminado.setVisible(true);
                        btnTerminado.setVisible(true);
                        
                        jmpCatalogos.setVisible(true);
                        jmCalibres.setVisible(true);
                        jmTambores.setVisible(true);
                        jmSubProcesos.setVisible(true);
                        jmProveedores.setVisible(true);
                        jmUsuarios.setVisible(true);
                        jmPrecioVenta.setVisible(true);
                        
                        jmpConfiguraciones.setVisible(true);
//                        jmpBaseDeDatos.setVisible(true);
                        break;
                    }
                }
                
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].equals("Terminado"))
                    {
                        btnTerminado.setVisible(true);
                        jmpCatalogos.setVisible(true);
                        jmCalibres.setVisible(true);
                        break;
                    }
                }
                
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].equals("Rivera"))
                    {
                        btnRecepcionCuero.setVisible(true);
                        btnPartidas.setVisible(true);
                        btnFichasProduccion.setVisible(true);
                        jmpCatalogos.setVisible(true);
                        jmTambores.setVisible(true);
                        jmSubProcesos.setVisible(true);
                        jmProveedores.setVisible(true);
                        jmpConfiguraciones.setVisible(true);
                        break;
                    }
                }
                
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].equals("Engrase"))
                    {
                        btnFichasProduccion.setVisible(true);
                        jmpCatalogos.setVisible(true);
                        jmTambores.setVisible(true);
                        jmSubProcesos.setVisible(true);
                        jmpConfiguraciones.setVisible(true);
                        break;
                    }
                }
            }
            else
                JOptionPane.showMessageDialog(dlgLogin, "Datos incorrectos o usuario inactivo", "Error de Login", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dlgLogin, "Error de conexion de datos", "Error de conexion", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    //Método que abre un dialogo
    public void abrirDialogo(JDialog dialogo, int ancho, int alto)
    {   try 
        {
            dialogo.setSize(ancho, alto);
            dialogo.setPreferredSize(dialogo.getSize());
            dialogo.setLocationRelativeTo(null);
            dialogo.setModal(true);
            dialogo.setVisible(true);
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            dialogo.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dialogo.setVisible(true);
        }
    }
    
    public void cargarConfActMerma()
    {
        try
        {
            String datos[][] = null;
            cmc = new ConfiguracionMermaCommands();
            
            datos = cmc.obtenerConfiguracionesMerma();
            
            double humAcep = (Double.parseDouble(datos[0][1]))*100;
            
            txtSalAcep.setText(datos[0][0]);
            txtHumAcep.setText(String.valueOf(humAcep));
            txtCachAcep.setText(datos[0][2]);
            txtTarAcep.setText(datos[0][3]);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar datos de la BD", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void cargarConfActRango()
    {
        try
        {
            String datos[][] = null;
            rpcc = new RangoPesoCueroCommands();
            
            datos = rpcc.llenarLabelRangoPesoCuero();
            
            txtRangoMin.setText(datos[0][1]);
            txtRangoMax.setText(datos[0][2]);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar datos de la BD", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void guardarConfMerma()
    {
        try
        {
            if (JOptionPane.showConfirmDialog(dlgMermas, "Realmente desea guardar la configuracion", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
            {
                if (txtSalAcep.getText().isEmpty() || txtHumAcep.getText().isEmpty() || txtCachAcep.getText().isEmpty() || txtTarAcep.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(dlgMermas, "Debe señalar un valor a todas las mermas", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                cmc = new ConfiguracionMermaCommands();
                ConfiguracionMerma[] datosCM = new ConfiguracionMerma[4];
                
                try
                {
                    double salAcep = Double.parseDouble(txtSalAcep.getText());
                    double humedadAcep = (Double.parseDouble(txtHumAcep.getText()))/100;
                    double cacheteAcep = Double.parseDouble(txtCachAcep.getText());
                    double tarimasAcep = Double.parseDouble(txtTarAcep.getText());
                    
                    for (int i = 0; i < 4; i++)
                    {
                        datosCM[i] = new ConfiguracionMerma();
                        
                        switch(i)
                        {
                            case 0:
                               datosCM[i].setIdTipoMerma(i+1);
                               datosCM[i].setPorcMermaAcep(salAcep);
                            break;
                            
                            case 1:
                                datosCM[i].setIdTipoMerma(i+1);
                                datosCM[i].setPorcMermaAcep(humedadAcep);
                            break;
                            
                            case 2:
                                datosCM[i].setIdTipoMerma(i+1);
                                datosCM[i].setPorcMermaAcep(cacheteAcep);
                            break;
                            
                            case 3:
                                datosCM[i].setIdTipoMerma(i+1);
                                datosCM[i].setPorcMermaAcep(tarimasAcep);
                            break;
                        }
                    }
                    
                    cmc.agregarConfigMerma(datosCM);
                    
                    JOptionPane.showMessageDialog(dlgMermas, "Registro insertado correctamente");
                    
                    dlgMermas.setVisible(false);
                    this.setVisible(true);
                    this.setExtendedState(MAXIMIZED_BOTH);
                    pnlPrincipal=new PnlPrincipal();
                    pnlPrincipalx.removeAll();        
                    pnlPrincipalx.add(pnlPrincipal, BorderLayout.CENTER);
                    pnlPrincipalx.paintAll(pnlPrincipal.getGraphics());
                    ImageIcon ico=new ImageIcon(".\\src\\imagenes\\house.png");
                    lblVentana.setIcon(ico);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(dlgMermas, "Error al guardar la configiracion de mermas", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch (Exception e)
        {
            
        }
    }
    
    public void guardarConfRangoPesoCuero()
    {
        try
        {
            if (JOptionPane.showConfirmDialog(dlgRangos, "Realmente desea guardar la configuracion", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
            {
                if (txtRangoMin.getText().isEmpty() || txtRangoMax.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(dlgRangos, "Debe señalar un valor a todas los rangos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                rpcc = new RangoPesoCueroCommands();
                rpc = new RangoPesoCuero();
                
                try
                {
                    float rangoMin = Float.parseFloat(txtRangoMin.getText());
                    float rangoMax = Float.parseFloat(txtRangoMax.getText());
                    
                    rpc.setRangoMin(rangoMin);
                    rpc.setRangoMax(rangoMax);
                    
                    rpcc.agregarConfigRangoPesoCuero(rpc);
                    
                    JOptionPane.showMessageDialog(dlgRangos, "Registro insertado correctamente");
                    
                    dlgRangos.setVisible(false);
                    this.setVisible(true);
                    this.setExtendedState(MAXIMIZED_BOTH);
                    pnlPrincipal=new PnlPrincipal();
                    pnlPrincipalx.removeAll();        
                    pnlPrincipalx.add(pnlPrincipal, BorderLayout.CENTER);
                    pnlPrincipalx.paintAll(pnlPrincipal.getGraphics());
                    ImageIcon ico=new ImageIcon(".\\src\\imagenes\\house.png");
                    lblVentana.setIcon(ico);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(dlgRangos, "Error al guardar la configiracion de mermas", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch (Exception e)
        {
            
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
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cargarConfPrecioCuero()
    {
        try
        {
            ConfiPrecioCueroCommands cpcc = new ConfiPrecioCueroCommands();
            lstConfPrecioCuero = cpcc.obtenerConfPrecioCuero();
            
            String[] cols = new String[]
            {
                "Tipo Recorte", "Porcentaje del Costo"
            };
            
            DefaultTableModel dtm = new DefaultTableModel()
            {
                public boolean isCellEditable (int row, int column)
                {
                    // Aquí devolvemos true o false según queramos que una celda
                    // identificada por fila,columna (row,column), sea o no editable
                    if (column == 1)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            };
            dtm.setColumnIdentifiers(cols);
            dtm.setRowCount(lstConfPrecioCuero.size());
            for (int i = 0; i < lstConfPrecioCuero.size(); i++)
            {
                dtm.setValueAt(lstConfPrecioCuero.get(i).getDescTipoRecorte(), i, 0);
                dtm.setValueAt(lstConfPrecioCuero.get(i).getPorcentaje(), i, 1);
            }
            tblConfCostoCuero.setModel(dtm);
            tblConfCostoCuero.getTableHeader().setReorderingAllowed(false);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar datos de la BD", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarConfGastosFabricacion()
    {
        try
        {
            ConfGastosFabricacionCommands cgfc = new ConfGastosFabricacionCommands();
            lstConfGastosFabricacion = cgfc.obtenerConfGastosFabricacion();
            
            txtCostoEntero1.setText(String.valueOf(lstConfGastosFabricacion.get(0).getCosto()));
            txtCostoDelSillero1.setText(String.valueOf(lstConfGastosFabricacion.get(1).getCosto()));
            txtCostoCrupSillero1.setText(String.valueOf(lstConfGastosFabricacion.get(2).getCosto()));
            txtCostoLados1.setText(String.valueOf(lstConfGastosFabricacion.get(3).getCosto()));
            txtCostoCentroCas1.setText(String.valueOf(lstConfGastosFabricacion.get(4).getCosto()));
            txtCostoCentroQue1.setText(String.valueOf(lstConfGastosFabricacion.get(5).getCosto()));
            txtCostoDelSuela1.setText(String.valueOf(lstConfGastosFabricacion.get(6).getCosto()));
            txtCostoCentro1.setText(String.valueOf(lstConfGastosFabricacion.get(7).getCosto()));
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar datos de la BD", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void validarPorcentaje()
    {
        for (int fila = 0; fila < tblConfCostoCuero.getRowCount(); fila++)
        {
            try
            {
                Double porcentaje = Double.parseDouble(tblConfCostoCuero.getValueAt(fila, 1).toString());

                if (porcentaje < 0)
                {
                    tblConfCostoCuero.setValueAt("0.0", fila, 1);
                }
            } catch (Exception e)
            {
                tblConfCostoCuero.setValueAt("0.0", fila, 1);
            }
        }
    }
    
    public void cargarConfActPrecGarra()
    {
        try
        {
            String datos[][] = null;
            cg = new CostoGarra();
            
            datos = cgc.obtenerConfiguracionesGarra();
            
            txtCostoGarra.setText(datos[0][0]);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar datos de la BD", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void guardarConfCostoGarra()
    {
        try
        {
            if (JOptionPane.showConfirmDialog(dlgCostoGarra, "Realmente desea guardar la configuracion", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
            {
                if (txtCostoGarra.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(dlgCostoGarra, "Debe señalar un valor al costo de la garra", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                cgc = new CostoGarraCommands();
                cg = new CostoGarra();
                
                try
                {
                    double costoGarra = Double.parseDouble(txtCostoGarra.getText());
                    
                    if (costoGarra <= 0)
                    {
                        JOptionPane.showMessageDialog(dlgCostoGarra, "Costo de la garra debe ser mayor a 0", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    cg.setCosto(costoGarra);
                    
                    cgc.agregarConfigGarra(cg);
                    
                    JOptionPane.showMessageDialog(dlgCostoGarra, "Registro insertado correctamente");
                    
                    dlgCostoGarra.setVisible(false);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(dlgMermas, "Error al guardar el costo de la garra", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dlgCostoGarra.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al guardar costo de la garra","Error",JOptionPane.ERROR_MESSAGE);
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
    
    public void generarReporteInventarioInsumos()
    {
        try
        {
            URL path = this.getClass().getResource("/Reportes/ReporteInvInsumos.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(path);
            
            conexionBD.conectarCompaq();
            
            JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, conexionBD.getConexion());
            
            JasperViewer view = new JasperViewer(jprint, false);
            
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            view.setVisible(true);
            conexionBD.desconectar();
        } catch (JRException ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se puede generar el reporte","Error",JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cargarConfPrecioManoDeObra1()
    {
        try
        {
            ConfPrecioManoDeObraCommands cpmc = new ConfPrecioManoDeObraCommands();
            lstConfPrecioManoDeObra = cpmc.obtenerConfPrecioManoDeObra();
            
            txtCostoEntero.setText(String.valueOf(lstConfPrecioManoDeObra.get(0).getCosto()));
            txtCostoDelSillero.setText(String.valueOf(lstConfPrecioManoDeObra.get(1).getCosto()));
            txtCostoCrupSillero.setText(String.valueOf(lstConfPrecioManoDeObra.get(2).getCosto()));
            txtCostoLados.setText(String.valueOf(lstConfPrecioManoDeObra.get(3).getCosto()));
            txtCostoCentroCas.setText(String.valueOf(lstConfPrecioManoDeObra.get(4).getCosto()));
            txtCostoCentroQue.setText(String.valueOf(lstConfPrecioManoDeObra.get(5).getCosto()));
            txtCostoDelSuela.setText(String.valueOf(lstConfPrecioManoDeObra.get(6).getCosto()));
            txtCostoCentro.setText(String.valueOf(lstConfPrecioManoDeObra.get(7).getCosto()));
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error al recuperar datos de la BD", "Error", JOptionPane.ERROR_MESSAGE);
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

        dlgLogin = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnEntrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        ptxtContrasenia = new javax.swing.JPasswordField();
        lblLogo = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblContrasenia = new javax.swing.JLabel();
        dlgMermas = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSalAcep = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtHumAcep = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCachAcep = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtTarAcep = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        dlgRangos = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtRangoMin = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtRangoMax = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        dlgPrecioCuero = new javax.swing.JDialog();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        btnGuardar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblConfCostoCuero = new javax.swing.JTable();
        dlgCostoGarra = new javax.swing.JDialog();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtCostoGarra = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        jButton3 = new javax.swing.JButton();
        dlgPrecioManoDeObra1 = new javax.swing.JDialog();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        txtCostoEntero = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jToolBar7 = new javax.swing.JToolBar();
        jButton4 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        txtCostoDelSillero = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtCostoCrupSillero = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtCostoLados = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtCostoCentroCas = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtCostoCentroQue = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtCostoDelSuela = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtCostoCentro = new javax.swing.JTextField();
        dlgGastosDeFabricacion = new javax.swing.JDialog();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        txtCostoEntero1 = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jToolBar8 = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        txtCostoDelSillero1 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtCostoCrupSillero1 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtCostoLados1 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtCostoCentroCas1 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtCostoCentroQue1 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtCostoDelSuela1 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtCostoCentro1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        pnlMenu = new javax.swing.JPanel();
        btnInicio = new javax.swing.JButton();
        lblMenui = new javax.swing.JLabel();
        btnRecepcionCuero = new javax.swing.JButton();
        btnPartidas = new javax.swing.JButton();
        btnCross = new javax.swing.JButton();
        btnSemiterminado = new javax.swing.JButton();
        btnTerminado = new javax.swing.JButton();
        btnProdEnProc = new javax.swing.JButton();
        btnFichasProduccion = new javax.swing.JButton();
        pnlPrincipalx = new javax.swing.JPanel();
        pnlFooter = new javax.swing.JPanel();
        lblNombreUsuario = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lblVentana = new javax.swing.JLabel();
        lblLogoSistema = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmpArchivo = new javax.swing.JMenu();
        jmSalir = new javax.swing.JMenuItem();
        jmpCatalogos = new javax.swing.JMenu();
        jmCalibres = new javax.swing.JMenuItem();
        jmTambores = new javax.swing.JMenuItem();
        jmSubProcesos = new javax.swing.JMenuItem();
        jmProveedores = new javax.swing.JMenuItem();
        jmUsuarios = new javax.swing.JMenuItem();
        jmPrecioVenta = new javax.swing.JMenuItem();
        jmpConfiguraciones = new javax.swing.JMenu();
        jmMermas = new javax.swing.JMenuItem();
        jmRangosPeso = new javax.swing.JMenuItem();
        jmInsumosXproceso = new javax.swing.JMenuItem();
        jmCostoCuero = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jmCostoManoDeObra = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jmpBaseDeDatos = new javax.swing.JMenu();
        jmiExportar = new javax.swing.JMenuItem();
        jmiImportar = new javax.swing.JMenuItem();
        jmAlmacen = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jmpAcercaDe = new javax.swing.JMenu();

        dlgLogin.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        dlgLogin.setTitle("Login");
        dlgLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Sistema de Producción");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Contraseña:");

        txtUsuario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Usuario:");

        btnEntrar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEntrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/door.png"))); // NOI18N
        btnEntrar.setText("Entrar");
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarActionPerformed(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cross.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        ptxtContrasenia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ptxtContrasenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ptxtContraseniaActionPerformed(evt);
            }
        });

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logoEsmar.png"))); // NOI18N

        lblUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/users_men_women.png"))); // NOI18N

        lblContrasenia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/textfield_password.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnEntrar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSalir))
                            .addComponent(txtUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                            .addComponent(ptxtContrasenia))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUsuario)
                            .addComponent(lblContrasenia)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(lblLogo)))
                .addContainerGap(145, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(34, 34, 34)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsuario, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblContrasenia, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(ptxtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSalir)
                    .addComponent(btnEntrar))
                .addContainerGap())
        );

        dlgLogin.getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(0, 204, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
        jLabel2.setText("Configuración Mermas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(66, 66, 66))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Sal Aceptada");

        txtSalAcep.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSalAcep.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSalAcep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSalAcepKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Kg/Cuero");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Humedad aceptada");

        txtHumAcep.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtHumAcep.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHumAcep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHumAcepKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("% de Kg total");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Cachete aceptado");

        txtCachAcep.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCachAcep.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCachAcep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCachAcepKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Kg/Cuero");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Tarima aceptada");

        txtTarAcep.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTarAcep.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTarAcep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTarAcepKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Kg");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        jButton1.setText("Guardar");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTarAcep, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCachAcep, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSalAcep, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtHumAcep, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSalAcep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtHumAcep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtCachAcep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtTarAcep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlgMermasLayout = new javax.swing.GroupLayout(dlgMermas.getContentPane());
        dlgMermas.getContentPane().setLayout(dlgMermasLayout);
        dlgMermasLayout.setHorizontalGroup(
            dlgMermasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgMermasLayout.setVerticalGroup(
            dlgMermasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(0, 204, 51));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/arrow_down_up.png"))); // NOI18N
        jLabel13.setText("Configuración Rangos Peso Cuero");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Menor a");

        txtRangoMin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtRangoMin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRangoMin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRangoMinKeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("Ligeros");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Mayor a");

        txtRangoMax.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtRangoMax.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRangoMax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRangoMaxKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Pesados");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Kg");

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        jButton2.setText("Guardar");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton2);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtRangoMax, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtRangoMin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel15))
                .addGap(73, 73, 73))
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtRangoMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtRangoMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlgRangosLayout = new javax.swing.GroupLayout(dlgRangos.getContentPane());
        dlgRangos.getContentPane().setLayout(dlgRangosLayout);
        dlgRangosLayout.setHorizontalGroup(
            dlgRangosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        dlgRangosLayout.setVerticalGroup(
            dlgRangosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel13.setBackground(new java.awt.Color(0, 204, 51));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/coins.png"))); // NOI18N
        jLabel18.setText("Configuración Costo Cuero");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnGuardar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setFocusable(false);
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jToolBar3.add(btnGuardar);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tblConfCostoCuero.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Tipo Recorte", "Porcentaje del Costo"
            }
        ));
        tblConfCostoCuero.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblConfCostoCuero.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblConfCostoCueroMouseClicked(evt);
            }
        });
        tblConfCostoCuero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblConfCostoCueroKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblConfCostoCuero);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlgPrecioCueroLayout = new javax.swing.GroupLayout(dlgPrecioCuero.getContentPane());
        dlgPrecioCuero.getContentPane().setLayout(dlgPrecioCueroLayout);
        dlgPrecioCueroLayout.setHorizontalGroup(
            dlgPrecioCueroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgPrecioCueroLayout.setVerticalGroup(
            dlgPrecioCueroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel17.setBackground(new java.awt.Color(0, 204, 51));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/coins.png"))); // NOI18N
        jLabel19.setText("Configuración Costo Garra");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(66, 66, 66))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Costo Garra $");

        txtCostoGarra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoGarra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoGarra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoGarraKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoGarraKeyTyped(evt);
            }
        });

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        jButton3.setText("Guardar");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton3);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCostoGarra, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtCostoGarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlgCostoGarraLayout = new javax.swing.GroupLayout(dlgCostoGarra.getContentPane());
        dlgCostoGarra.getContentPane().setLayout(dlgCostoGarraLayout);
        dlgCostoGarraLayout.setHorizontalGroup(
            dlgCostoGarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgCostoGarraLayout.setVerticalGroup(
            dlgCostoGarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel25.setBackground(new java.awt.Color(0, 204, 51));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/coins.png"))); // NOI18N
        jLabel23.setText("Configuración Costo Mano De Obra");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("Entero $");

        txtCostoEntero.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoEntero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoEntero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoEnteroKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoEnteroKeyTyped(evt);
            }
        });

        jToolBar7.setFloatable(false);
        jToolBar7.setRollover(true);

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        jButton4.setText("Guardar");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar7.add(jButton4);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jToolBar7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Delantero Sillero $");

        txtCostoDelSillero.setEditable(false);
        txtCostoDelSillero.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoDelSillero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoDelSillero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoDelSilleroKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoDelSilleroKeyTyped(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Crupon Sillero $");

        txtCostoCrupSillero.setEditable(false);
        txtCostoCrupSillero.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoCrupSillero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoCrupSillero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoCrupSilleroKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoCrupSilleroKeyTyped(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText("Lados $");

        txtCostoLados.setEditable(false);
        txtCostoLados.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoLados.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoLados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoLadosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoLadosKeyTyped(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setText("Centro Castaño $");

        txtCostoCentroCas.setEditable(false);
        txtCostoCentroCas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoCentroCas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoCentroCas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoCentroCasKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoCentroCasKeyTyped(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setText("Centro Quebracho $");

        txtCostoCentroQue.setEditable(false);
        txtCostoCentroQue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoCentroQue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoCentroQue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoCentroQueKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoCentroQueKeyTyped(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setText("Delantero Suela $");

        txtCostoDelSuela.setEditable(false);
        txtCostoDelSuela.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoDelSuela.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoDelSuela.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoDelSuelaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoDelSuelaKeyTyped(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel31.setText("Centro $");

        txtCostoCentro.setEditable(false);
        txtCostoCentro.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoCentro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoCentro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoCentroKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoCentroKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCostoCentro))
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCostoDelSuela))))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCostoCentroQue))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel28Layout.createSequentialGroup()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel24)))
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCostoEntero)
                            .addComponent(txtCostoDelSillero)
                            .addComponent(txtCostoCrupSillero)
                            .addComponent(txtCostoLados)
                            .addComponent(txtCostoCentroCas))))
                .addGap(68, 68, 68))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtCostoEntero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtCostoDelSillero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtCostoCrupSillero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtCostoLados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtCostoCentroCas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtCostoCentroQue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtCostoDelSuela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtCostoCentro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlgPrecioManoDeObra1Layout = new javax.swing.GroupLayout(dlgPrecioManoDeObra1.getContentPane());
        dlgPrecioManoDeObra1.getContentPane().setLayout(dlgPrecioManoDeObra1Layout);
        dlgPrecioManoDeObra1Layout.setHorizontalGroup(
            dlgPrecioManoDeObra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgPrecioManoDeObra1Layout.setVerticalGroup(
            dlgPrecioManoDeObra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel27.setBackground(new java.awt.Color(0, 204, 51));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/coins.png"))); // NOI18N
        jLabel32.setText("Configuración Gastos De Fabricación");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel33.setText("Entero $");

        txtCostoEntero1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoEntero1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoEntero1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoEntero1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoEntero1KeyTyped(evt);
            }
        });

        jToolBar8.setFloatable(false);
        jToolBar8.setRollover(true);

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        jButton5.setText("Guardar");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar8.add(jButton5);

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jToolBar8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel34.setText("Delantero Sillero $");

        txtCostoDelSillero1.setEditable(false);
        txtCostoDelSillero1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoDelSillero1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoDelSillero1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoDelSillero1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoDelSillero1KeyTyped(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel35.setText("Crupon Sillero $");

        txtCostoCrupSillero1.setEditable(false);
        txtCostoCrupSillero1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoCrupSillero1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoCrupSillero1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoCrupSillero1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoCrupSillero1KeyTyped(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel36.setText("Lados $");

        txtCostoLados1.setEditable(false);
        txtCostoLados1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoLados1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoLados1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoLados1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoLados1KeyTyped(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel37.setText("Centro Castaño $");

        txtCostoCentroCas1.setEditable(false);
        txtCostoCentroCas1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoCentroCas1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoCentroCas1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoCentroCas1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoCentroCas1KeyTyped(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel38.setText("Centro Quebracho $");

        txtCostoCentroQue1.setEditable(false);
        txtCostoCentroQue1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoCentroQue1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoCentroQue1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoCentroQue1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoCentroQue1KeyTyped(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel39.setText("Delantero Suela $");

        txtCostoDelSuela1.setEditable(false);
        txtCostoDelSuela1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoDelSuela1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoDelSuela1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoDelSuela1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoDelSuela1KeyTyped(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel40.setText("Centro $");

        txtCostoCentro1.setEditable(false);
        txtCostoCentro1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoCentro1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoCentro1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoCentro1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoCentro1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jLabel40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCostoCentro1))
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCostoDelSuela1))))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCostoCentroQue1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel30Layout.createSequentialGroup()
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel34)
                                    .addComponent(jLabel33)))
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCostoEntero1)
                            .addComponent(txtCostoDelSillero1)
                            .addComponent(txtCostoCrupSillero1)
                            .addComponent(txtCostoLados1)
                            .addComponent(txtCostoCentroCas1))))
                .addGap(68, 68, 68))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txtCostoEntero1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txtCostoDelSillero1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(txtCostoCrupSillero1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(txtCostoLados1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(txtCostoCentroCas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(txtCostoCentroQue1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(txtCostoDelSuela1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(txtCostoCentro1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlgGastosDeFabricacionLayout = new javax.swing.GroupLayout(dlgGastosDeFabricacion.getContentPane());
        dlgGastosDeFabricacion.getContentPane().setLayout(dlgGastosDeFabricacionLayout);
        dlgGastosDeFabricacionLayout.setHorizontalGroup(
            dlgGastosDeFabricacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgGastosDeFabricacionLayout.setVerticalGroup(
            dlgGastosDeFabricacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        pnlMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnInicio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/house.png"))); // NOI18N
        btnInicio.setText("Inicio");
        btnInicio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });

        lblMenui.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblMenui.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMenui.setText("Menu");

        btnRecepcionCuero.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRecepcionCuero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lorry.png"))); // NOI18N
        btnRecepcionCuero.setText("Recepción de cuero ");
        btnRecepcionCuero.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRecepcionCuero.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnRecepcionCuero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecepcionCueroActionPerformed(evt);
            }
        });

        btnPartidas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPartidas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cueroProceso.png"))); // NOI18N
        btnPartidas.setText("Partidas");
        btnPartidas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPartidas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPartidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartidasActionPerformed(evt);
            }
        });

        btnCross.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCross.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cuero.png"))); // NOI18N
        btnCross.setText("Desvenado");
        btnCross.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCross.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCross.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrossActionPerformed(evt);
            }
        });

        btnSemiterminado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSemiterminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cuero.png"))); // NOI18N
        btnSemiterminado.setText("Semiterminado");
        btnSemiterminado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSemiterminado.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSemiterminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSemiterminadoActionPerformed(evt);
            }
        });

        btnTerminado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnTerminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cuero.png"))); // NOI18N
        btnTerminado.setText("Terminado");
        btnTerminado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnTerminado.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnTerminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTerminadoActionPerformed(evt);
            }
        });

        btnProdEnProc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnProdEnProc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cog.png"))); // NOI18N
        btnProdEnProc.setText("Fichas Generadas");
        btnProdEnProc.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnProdEnProc.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnProdEnProc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdEnProcActionPerformed(evt);
            }
        });

        btnFichasProduccion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnFichasProduccion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/document_index.png"))); // NOI18N
        btnFichasProduccion.setText("Fichas de Producción");
        btnFichasProduccion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnFichasProduccion.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnFichasProduccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFichasProduccionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMenuLayout = new javax.swing.GroupLayout(pnlMenu);
        pnlMenu.setLayout(pnlMenuLayout);
        pnlMenuLayout.setHorizontalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMenui, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMenuLayout.createSequentialGroup()
                .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTerminado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlMenuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCross, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnPartidas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRecepcionCuero, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSemiterminado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnProdEnProc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFichasProduccion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlMenuLayout.setVerticalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblMenui, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRecepcionCuero)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPartidas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFichasProduccion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProdEnProc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCross)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSemiterminado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTerminado)
                .addContainerGap(244, Short.MAX_VALUE))
        );

        pnlPrincipalx.setBackground(new java.awt.Color(255, 255, 255));
        pnlPrincipalx.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPrincipalx.setLayout(new java.awt.BorderLayout());

        lblNombreUsuario.setText("Nombre usuario");

        javax.swing.GroupLayout pnlFooterLayout = new javax.swing.GroupLayout(pnlFooter);
        pnlFooter.setLayout(pnlFooterLayout);
        pnlFooterLayout.setHorizontalGroup(
            pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFooterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblNombreUsuario)
                .addGap(18, 18, 18))
        );
        pnlFooterLayout.setVerticalGroup(
            pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFooterLayout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addComponent(lblNombreUsuario))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblVentana.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblVentana.setText("Inicio");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVentana, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(449, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblVentana, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 867, Short.MAX_VALUE)
                        .addComponent(lblLogoSistema, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlPrincipalx, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogoSistema, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlPrincipalx, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
                    .addComponent(pnlMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jmpArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/page.png"))); // NOI18N
        jmpArchivo.setText("Archivo");

        jmSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cross.png"))); // NOI18N
        jmSalir.setText("Salir");
        jmSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmSalirActionPerformed(evt);
            }
        });
        jmpArchivo.add(jmSalir);

        jMenuBar1.add(jmpArchivo);

        jmpCatalogos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/book.png"))); // NOI18N
        jmpCatalogos.setText("Catálogos");
        jmpCatalogos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jmpCatalogosMouseClicked(evt);
            }
        });

        jmCalibres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ruler.png"))); // NOI18N
        jmCalibres.setText("Calibres");
        jmCalibres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmCalibresActionPerformed(evt);
            }
        });
        jmpCatalogos.add(jmCalibres);

        jmTambores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/barril.png"))); // NOI18N
        jmTambores.setText("Tambores");
        jmTambores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmTamboresActionPerformed(evt);
            }
        });
        jmpCatalogos.add(jmTambores);

        jmSubProcesos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cueroProceso.png"))); // NOI18N
        jmSubProcesos.setText("SubProcesos");
        jmSubProcesos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmSubProcesosActionPerformed(evt);
            }
        });
        jmpCatalogos.add(jmSubProcesos);

        jmProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/group.png"))); // NOI18N
        jmProveedores.setText("Proveedores");
        jmProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmProveedoresActionPerformed(evt);
            }
        });
        jmpCatalogos.add(jmProveedores);

        jmUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/users_4.png"))); // NOI18N
        jmUsuarios.setText("Usuarios");
        jmUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmUsuariosActionPerformed(evt);
            }
        });
        jmpCatalogos.add(jmUsuarios);

        jmPrecioVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money_dollar.png"))); // NOI18N
        jmPrecioVenta.setText("Precios de venta");
        jmPrecioVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmPrecioVentaActionPerformed(evt);
            }
        });
        jmpCatalogos.add(jmPrecioVenta);

        jMenuBar1.add(jmpCatalogos);

        jmpConfiguraciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cog.png"))); // NOI18N
        jmpConfiguraciones.setText("Configuraciones");
        jmpConfiguraciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jmpConfiguracionesMouseClicked(evt);
            }
        });

        jmMermas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
        jmMermas.setText("Mermas");
        jmMermas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmMermasActionPerformed(evt);
            }
        });
        jmpConfiguraciones.add(jmMermas);

        jmRangosPeso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/arrow_down_up.png"))); // NOI18N
        jmRangosPeso.setText("Rangos Peso");
        jmRangosPeso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmRangosPesoActionPerformed(evt);
            }
        });
        jmpConfiguraciones.add(jmRangosPeso);

        jmInsumosXproceso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/flask.png"))); // NOI18N
        jmInsumosXproceso.setText("Insumos por Proceso");
        jmInsumosXproceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmInsumosXprocesoActionPerformed(evt);
            }
        });
        jmpConfiguraciones.add(jmInsumosXproceso);

        jmCostoCuero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/coins.png"))); // NOI18N
        jmCostoCuero.setText("Costo Cuero");
        jmCostoCuero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmCostoCueroActionPerformed(evt);
            }
        });
        jmpConfiguraciones.add(jmCostoCuero);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/coins.png"))); // NOI18N
        jMenuItem2.setText("Costo Garra");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jmpConfiguraciones.add(jMenuItem2);

        jmCostoManoDeObra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/coins.png"))); // NOI18N
        jmCostoManoDeObra.setText("Costo Mano De Obra");
        jmCostoManoDeObra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmCostoManoDeObraActionPerformed(evt);
            }
        });
        jmpConfiguraciones.add(jmCostoManoDeObra);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/coins.png"))); // NOI18N
        jMenuItem4.setText("Gastos De Fabricación");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jmpConfiguraciones.add(jMenuItem4);

        jMenuBar1.add(jmpConfiguraciones);

        jmpBaseDeDatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/database.png"))); // NOI18N
        jmpBaseDeDatos.setText("Base de datos");

        jmiExportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/database_save.png"))); // NOI18N
        jmiExportar.setText("Exportar");
        jmiExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiExportarActionPerformed(evt);
            }
        });
        jmpBaseDeDatos.add(jmiExportar);

        jmiImportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/database_cleanup.png"))); // NOI18N
        jmiImportar.setText("Importar");
        jmiImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiImportarActionPerformed(evt);
            }
        });
        jmpBaseDeDatos.add(jmiImportar);

        jMenuBar1.add(jmpBaseDeDatos);

        jmAlmacen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/report_key.png"))); // NOI18N
        jmAlmacen.setText("Almacen");

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
        jMenuItem3.setText("Reporte de Inventario de Almacen");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jmAlmacen.add(jMenuItem3);

        jMenuBar1.add(jmAlmacen);

        jmpAcercaDe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/information.png"))); // NOI18N
        jmpAcercaDe.setText("Acerca de");
        jmpAcercaDe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jmpAcercaDeMouseClicked(evt);
            }
        });
        jMenuBar1.add(jmpAcercaDe);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmSalirActionPerformed
        
        System.exit(0);
    }//GEN-LAST:event_jmSalirActionPerformed

    private void jmpCatalogosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jmpCatalogosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jmpCatalogosMouseClicked

    private void jmpAcercaDeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jmpAcercaDeMouseClicked
        JOptionPane.showMessageDialog(null, "'Sistema de Producción V.1.0' \n"
            + "Diciembre 2018\n\nDesarrollado por: \nIng. César Domingo Luna Gutiérrez"
            + "\nIng. Mario Daniel Luna Gutiérrez \n\n"
            + "Dudas o comentarios: \n"
            + "mingo.utl@gmail.com", "Acerca de...",
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jmpAcercaDeMouseClicked

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        validarUsuario();
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void btnEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarActionPerformed
        validarUsuario();
    }//GEN-LAST:event_btnEntrarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void ptxtContraseniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ptxtContraseniaActionPerformed
        validarUsuario();
    }//GEN-LAST:event_ptxtContraseniaActionPerformed

    private void btnProdEnProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdEnProcActionPerformed
        try {
            pnlProduccionEnProceso = new PnlProduccionEnProceso();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlProduccionEnProceso, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlProduccionEnProceso.getGraphics());
            
            lblVentana.setText("Fichas Generadas");
            ImageIcon ico=new ImageIcon("src/Imagenes/cog.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnProdEnProcActionPerformed

    private void btnTerminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerminadoActionPerformed
        try {
            pnlTerminado = new PnlTerminado();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlTerminado, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlTerminado.getGraphics());
            
            lblVentana.setText("Terminado");
            ImageIcon ico=new ImageIcon("src/Imagenes/cuero.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTerminadoActionPerformed

    private void btnSemiterminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSemiterminadoActionPerformed
        try {
            pnlSemiterminado = new PnlSemiterminado();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlSemiterminado, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlSemiterminado.getGraphics());
            
            lblVentana.setText("Semiterminado");
            ImageIcon ico=new ImageIcon("src/Imagenes/cuero.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSemiterminadoActionPerformed

    private void btnCrossActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrossActionPerformed
        try {
            pnlCross = new PnlCross();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlCross, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlCross.getGraphics());
            
            lblVentana.setText("Desvenado");
            ImageIcon ico=new ImageIcon("src/Imagenes/cuero.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCrossActionPerformed

    private void btnPartidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartidasActionPerformed
        try {
            pnlPartidas = new PnlPartidas();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlPartidas, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlPartidas.getGraphics());
            
            lblVentana.setText("Partidas");
            ImageIcon ico=new ImageIcon("src/Imagenes/cueroProceso.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPartidasActionPerformed

    private void btnRecepcionCueroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecepcionCueroActionPerformed
        try {
            pnlRecepcionCuero = new PnlRecepcionCuero();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlRecepcionCuero, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlRecepcionCuero.getGraphics());
            
            lblVentana.setText("Recepción de Cuero");
            ImageIcon ico=new ImageIcon("src/Imagenes/lorry.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRecepcionCueroActionPerformed

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        pnlPrincipal=new PnlPrincipal();
        pnlPrincipalx.removeAll();
        pnlPrincipalx.add(pnlPrincipal, BorderLayout.CENTER);
        pnlPrincipalx.paintAll(pnlPrincipal.getGraphics());

        lblVentana.setText("Inicio");
        ImageIcon ico=new ImageIcon("src/Imagenes/house.png");
        lblVentana.setIcon(ico);
    }//GEN-LAST:event_btnInicioActionPerformed

    private void jmpConfiguracionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jmpConfiguracionesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jmpConfiguracionesMouseClicked

    private void jmProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmProveedoresActionPerformed
        try 
        {
            pnlProveedores = new PnlProveedores();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlProveedores, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlProveedores.getGraphics());
            
            lblVentana.setText("Catálogo de Proveedores");
            ImageIcon ico=new ImageIcon("src/Imagenes/group.png");
            lblVentana.setIcon(ico);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jmProveedoresActionPerformed

    private void jmSubProcesosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmSubProcesosActionPerformed
        try 
        {
            pnlSubProcesos = new PnlSubProcesos();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlSubProcesos, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlSubProcesos.getGraphics());
            
            lblVentana.setText("Catálogo de SubProcesos");
            ImageIcon ico=new ImageIcon("src/Imagenes/cueroProceso.png");
            lblVentana.setIcon(ico);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jmSubProcesosActionPerformed

    private void jmMermasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmMermasActionPerformed
        cargarConfActMerma();
        abrirDialogo(dlgMermas, 310, 280);
    }//GEN-LAST:event_jmMermasActionPerformed

    private void jmRangosPesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmRangosPesoActionPerformed
        cargarConfActRango();
        abrirDialogo(dlgRangos, 325, 210);
    }//GEN-LAST:event_jmRangosPesoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        guardarConfMerma();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        guardarConfRangoPesoCuero();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtSalAcepKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalAcepKeyTyped
        validarNumeros(evt, txtSalAcep.getText());
    }//GEN-LAST:event_txtSalAcepKeyTyped

    private void txtHumAcepKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHumAcepKeyTyped
        validarNumeros(evt, txtHumAcep.getText());
    }//GEN-LAST:event_txtHumAcepKeyTyped

    private void txtCachAcepKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCachAcepKeyTyped
        validarNumeros(evt, txtCachAcep.getText());
    }//GEN-LAST:event_txtCachAcepKeyTyped

    private void txtTarAcepKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarAcepKeyTyped
        validarNumeros(evt, txtTarAcep.getText());
    }//GEN-LAST:event_txtTarAcepKeyTyped

    private void txtRangoMinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRangoMinKeyTyped
        validarNumeros(evt, txtRangoMin.getText());
    }//GEN-LAST:event_txtRangoMinKeyTyped

    private void txtRangoMaxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRangoMaxKeyTyped
        validarNumeros(evt, txtRangoMax.getText());
    }//GEN-LAST:event_txtRangoMaxKeyTyped

    private void jmInsumosXprocesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmInsumosXprocesoActionPerformed
        try {
            pnlInsXproc = new PnlInsXproc();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlInsXproc, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlInsXproc.getGraphics());
            
            lblVentana.setText("Insumos Por Proceso");
            ImageIcon ico=new ImageIcon("src/Imagenes/flask.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            ex.printStackTrace();
//            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jmInsumosXprocesoActionPerformed

    private void btnFichasProduccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFichasProduccionActionPerformed
        try {
            pnlFichaProduccion = new PnlFichaProduccion();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlFichaProduccion, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlFichaProduccion.getGraphics());
            
            lblVentana.setText("Fichas de Producción");
            ImageIcon ico=new ImageIcon("src/Imagenes/document_index.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnFichasProduccionActionPerformed

    private void jmiExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiExportarActionPerformed
        try {
            pnlExportar = new PnlExportar();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlExportar, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlExportar.getGraphics());
            
            lblVentana.setText("Exportar base de datos");
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\database_save.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jmiExportarActionPerformed

    private void jmiImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiImportarActionPerformed
        try {
            pnlImportar = new PnlImportar();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlImportar, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlImportar.getGraphics());
            
            lblVentana.setText("Importar Base de datos");
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\database_cleanup.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jmiImportarActionPerformed

    private void jmTamboresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmTamboresActionPerformed
        try 
        {
            pnlTambores = new PnlTambores();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlTambores, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlTambores.getGraphics());
            
            lblVentana.setText("Catálogo de Tambores");
            ImageIcon ico=new ImageIcon("src/Imagenes/barril.png");
            lblVentana.setIcon(ico);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jmTamboresActionPerformed

    private void jmUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmUsuariosActionPerformed
        try 
        {
            pnlUsuarios = new PnlUsuarios();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlUsuarios, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlUsuarios.getGraphics());
            
            lblVentana.setText("Catálogo de Usuarios");
            ImageIcon ico=new ImageIcon("src/Imagenes/users_4.png");
            lblVentana.setIcon(ico);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jmUsuariosActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try 
        {
            for (int i = 0; i < lstConfPrecioCuero.size(); i++) 
            {
                ConfPrecioCuero cpc = new ConfPrecioCuero();
                ConfiPrecioCueroCommands cpcc = new ConfiPrecioCueroCommands();
                
                cpc.setIdConfPrecioCuero(lstConfPrecioCuero.get(i).getIdConfPrecioCuero());
                cpc.setPorcentaje(Double.parseDouble(tblConfCostoCuero.getValueAt(i, 1).toString()) / 100);
                cpcc.actualizarPorcentajePrecio(cpc);
                
            }
            cargarConfPrecioCuero();
            dlgPrecioCuero.setVisible(false);
            JOptionPane.showMessageDialog(null, "Configuraciones guardadas correctamente","Mensaje",JOptionPane.INFORMATION_MESSAGE);
            dlgPrecioCuero.setVisible(true);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            dlgPrecioCuero.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al guardar las configuraciones de precios","Error",JOptionPane.WARNING_MESSAGE);
            dlgPrecioCuero.setVisible(true);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void jmCostoCueroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmCostoCueroActionPerformed
        cargarConfPrecioCuero();
        abrirDialogo(dlgPrecioCuero, 450, 300);
    }//GEN-LAST:event_jmCostoCueroActionPerformed

    private void jmCalibresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmCalibresActionPerformed
        try 
        {
            pnlCalibres = new PnlCalibres();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlCalibres, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlCalibres.getGraphics());
            
            lblVentana.setText("Catálogo de Calibres");
            ImageIcon ico=new ImageIcon("src/Imagenes/ruler.png");
            lblVentana.setIcon(ico);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jmCalibresActionPerformed

    private void txtCostoGarraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoGarraKeyTyped
        validarNumerosDecimales(evt, txtCostoGarra.getText());
    }//GEN-LAST:event_txtCostoGarraKeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        guardarConfCostoGarra();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        cargarConfActPrecGarra();
        abrirDialogo(dlgCostoGarra, 310, 180);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void txtCostoGarraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoGarraKeyReleased
        if (txtCostoGarra.getText().equals(""))
        {
            txtCostoGarra.setText("0");
        }
    }//GEN-LAST:event_txtCostoGarraKeyReleased

    private void jmCostoManoDeObraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmCostoManoDeObraActionPerformed
        cargarConfPrecioManoDeObra1();
        abrirDialogo(dlgPrecioManoDeObra1, 450, 450);
    }//GEN-LAST:event_jmCostoManoDeObraActionPerformed

    private void tblConfCostoCueroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblConfCostoCueroKeyReleased
        validarPorcentaje();
    }//GEN-LAST:event_tblConfCostoCueroKeyReleased

    private void tblConfCostoCueroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblConfCostoCueroMouseClicked
        validarPorcentaje();
    }//GEN-LAST:event_tblConfCostoCueroMouseClicked

    private void txtCostoEnteroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoEnteroKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoEnteroKeyReleased

    private void txtCostoEnteroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoEnteroKeyTyped
        validarNumerosEnteros(evt, txtCostoEntero.getText());
    }//GEN-LAST:event_txtCostoEnteroKeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try 
        {
            ConfPrecioManoDeObra cpm = new ConfPrecioManoDeObra();
            ConfPrecioManoDeObraCommands cpmc = new ConfPrecioManoDeObraCommands();

            cpm.setIdConfPrecioManoDeObra(lstConfPrecioManoDeObra.get(0).getIdConfPrecioManoDeObra());
            cpm.setCosto(Double.parseDouble(txtCostoEntero.getText()));
            cpmc.actualizarCosto(cpm);
            dlgPrecioManoDeObra1.setVisible(false);
            JOptionPane.showMessageDialog(null, "Cambio realizado correctamente","Mensaje",JOptionPane.WARNING_MESSAGE);
            cargarConfPrecioManoDeObra1();
            dlgPrecioManoDeObra1.setVisible(true);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            dlgPrecioManoDeObra1.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al guardar las configuraciones de precios","Error",JOptionPane.WARNING_MESSAGE);
            dlgPrecioManoDeObra1.setVisible(true);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtCostoDelSilleroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoDelSilleroKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoDelSilleroKeyReleased

    private void txtCostoDelSilleroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoDelSilleroKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoDelSilleroKeyTyped

    private void txtCostoCrupSilleroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCrupSilleroKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCrupSilleroKeyReleased

    private void txtCostoCrupSilleroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCrupSilleroKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCrupSilleroKeyTyped

    private void txtCostoLadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoLadosKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoLadosKeyReleased

    private void txtCostoLadosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoLadosKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoLadosKeyTyped

    private void txtCostoCentroCasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentroCasKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentroCasKeyReleased

    private void txtCostoCentroCasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentroCasKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentroCasKeyTyped

    private void txtCostoCentroQueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentroQueKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentroQueKeyReleased

    private void txtCostoCentroQueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentroQueKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentroQueKeyTyped

    private void txtCostoDelSuelaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoDelSuelaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoDelSuelaKeyReleased

    private void txtCostoDelSuelaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoDelSuelaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoDelSuelaKeyTyped

    private void txtCostoCentroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentroKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentroKeyReleased

    private void txtCostoCentroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentroKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentroKeyTyped

    private void jmPrecioVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmPrecioVentaActionPerformed
        try 
        {
            lblVentana.setText("Catálogo de Precios de venta");
            ImageIcon ico=new ImageIcon("src/Imagenes/money_dollar.png");
            lblVentana.setIcon(ico);
            
            pnlPrecioVenta = new PnlPrecioVenta();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlPrecioVenta, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlPrecioVenta.getGraphics());
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error al abrir el Panel","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jmPrecioVentaActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        generarReporteInventarioInsumos();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void txtCostoEntero1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoEntero1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoEntero1KeyReleased

    private void txtCostoEntero1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoEntero1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoEntero1KeyTyped

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try 
        {
            ConfGastosFabricacion cgf = new ConfGastosFabricacion();
            ConfGastosFabricacionCommands cgfc = new ConfGastosFabricacionCommands();

            cgf.setIdConfGastosFabricacion(lstConfGastosFabricacion.get(0).getIdConfGastosFabricacion());
            cgf.setCosto(Double.parseDouble(txtCostoEntero1.getText()));
            cgfc.actualizarCosto(cgf);
            dlgGastosDeFabricacion.setVisible(false);
            JOptionPane.showMessageDialog(null, "Cambio realizado correctamente","Mensaje",JOptionPane.WARNING_MESSAGE);
            cargarConfGastosFabricacion();
            dlgGastosDeFabricacion.setVisible(true);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            dlgGastosDeFabricacion.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al guardar las configuraciones de gastos de fabricación","Error",JOptionPane.WARNING_MESSAGE);
            dlgGastosDeFabricacion.setVisible(true);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtCostoDelSillero1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoDelSillero1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoDelSillero1KeyReleased

    private void txtCostoDelSillero1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoDelSillero1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoDelSillero1KeyTyped

    private void txtCostoCrupSillero1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCrupSillero1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCrupSillero1KeyReleased

    private void txtCostoCrupSillero1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCrupSillero1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCrupSillero1KeyTyped

    private void txtCostoLados1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoLados1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoLados1KeyReleased

    private void txtCostoLados1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoLados1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoLados1KeyTyped

    private void txtCostoCentroCas1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentroCas1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentroCas1KeyReleased

    private void txtCostoCentroCas1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentroCas1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentroCas1KeyTyped

    private void txtCostoCentroQue1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentroQue1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentroQue1KeyReleased

    private void txtCostoCentroQue1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentroQue1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentroQue1KeyTyped

    private void txtCostoDelSuela1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoDelSuela1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoDelSuela1KeyReleased

    private void txtCostoDelSuela1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoDelSuela1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoDelSuela1KeyTyped

    private void txtCostoCentro1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentro1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentro1KeyReleased

    private void txtCostoCentro1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCentro1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoCentro1KeyTyped

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        cargarConfGastosFabricacion();
        abrirDialogo(dlgGastosDeFabricacion, 450, 450);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new FrmPrincipal().setVisible(true);
                try {
                    new FrmPrincipal().setLocationRelativeTo(null);
                } catch (Exception ex) {
                    Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCross;
    private javax.swing.JButton btnEntrar;
    private javax.swing.JButton btnFichasProduccion;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnPartidas;
    private javax.swing.JButton btnProdEnProc;
    private javax.swing.JButton btnRecepcionCuero;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSemiterminado;
    private javax.swing.JButton btnTerminado;
    private javax.swing.JDialog dlgCostoGarra;
    private javax.swing.JDialog dlgGastosDeFabricacion;
    private javax.swing.JDialog dlgLogin;
    private javax.swing.JDialog dlgMermas;
    private javax.swing.JDialog dlgPrecioCuero;
    private javax.swing.JDialog dlgPrecioManoDeObra1;
    private javax.swing.JDialog dlgRangos;
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
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    private javax.swing.JMenu jmAlmacen;
    private javax.swing.JMenuItem jmCalibres;
    private javax.swing.JMenuItem jmCostoCuero;
    private javax.swing.JMenuItem jmCostoManoDeObra;
    private javax.swing.JMenuItem jmInsumosXproceso;
    private javax.swing.JMenuItem jmMermas;
    private javax.swing.JMenuItem jmPrecioVenta;
    private javax.swing.JMenuItem jmProveedores;
    private javax.swing.JMenuItem jmRangosPeso;
    private javax.swing.JMenuItem jmSalir;
    private javax.swing.JMenuItem jmSubProcesos;
    private javax.swing.JMenuItem jmTambores;
    private javax.swing.JMenuItem jmUsuarios;
    private javax.swing.JMenuItem jmiExportar;
    private javax.swing.JMenuItem jmiImportar;
    private javax.swing.JMenu jmpAcercaDe;
    private javax.swing.JMenu jmpArchivo;
    private javax.swing.JMenu jmpBaseDeDatos;
    private javax.swing.JMenu jmpCatalogos;
    private javax.swing.JMenu jmpConfiguraciones;
    private javax.swing.JLabel lblContrasenia;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblLogoSistema;
    private javax.swing.JLabel lblMenui;
    private javax.swing.JLabel lblNombreUsuario;
    private javax.swing.JLabel lblUsuario;
    public static javax.swing.JLabel lblVentana;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlMenu;
    public static javax.swing.JPanel pnlPrincipalx;
    private javax.swing.JPasswordField ptxtContrasenia;
    private javax.swing.JTable tblConfCostoCuero;
    private javax.swing.JTextField txtCachAcep;
    private javax.swing.JTextField txtCostoCentro;
    private javax.swing.JTextField txtCostoCentro1;
    private javax.swing.JTextField txtCostoCentroCas;
    private javax.swing.JTextField txtCostoCentroCas1;
    private javax.swing.JTextField txtCostoCentroQue;
    private javax.swing.JTextField txtCostoCentroQue1;
    private javax.swing.JTextField txtCostoCrupSillero;
    private javax.swing.JTextField txtCostoCrupSillero1;
    private javax.swing.JTextField txtCostoDelSillero;
    private javax.swing.JTextField txtCostoDelSillero1;
    private javax.swing.JTextField txtCostoDelSuela;
    private javax.swing.JTextField txtCostoDelSuela1;
    private javax.swing.JTextField txtCostoEntero;
    private javax.swing.JTextField txtCostoEntero1;
    private javax.swing.JTextField txtCostoGarra;
    private javax.swing.JTextField txtCostoLados;
    private javax.swing.JTextField txtCostoLados1;
    private javax.swing.JTextField txtHumAcep;
    private javax.swing.JTextField txtRangoMax;
    private javax.swing.JTextField txtRangoMin;
    private javax.swing.JTextField txtSalAcep;
    private javax.swing.JTextField txtTarAcep;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
