/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.CatalogoDetCommands;
import Controlador.ConexionBD;
import Controlador.EntradaMaterialCommands;
import Controlador.MaterialCommands;
import Controlador.SalidaMaterialCommands;
import Controlador.TipoMonedaCommands;
import Controlador.UnidadMedidaCommands;
import Modelo.Dto.RespuestaDto;
import Modelo.Entity.CatalogoDet;
import Modelo.Entity.EntradaMaterial;
import Modelo.Entity.Material;
import Modelo.Entity.SalidaMaterial;
import Modelo.Entity.TipoMoneda;
import Modelo.Entity.UnidadMedida;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Mingo
 */
public class PnlAlmacen extends javax.swing.JPanel {
    ConexionBD conexion;    
    List<Material> lstMaterial; 
    List<CatalogoDet> lstCatDetTipoMaterial; 
    List<CatalogoDet> lstCatDetClasMaterial; 
    List<UnidadMedida> lstUnidadMedida; 
    List<TipoMoneda> lstTipoMoneda; 
    private final String imagen="/Imagenes/logo_esmar.png";
    
    DefaultTableModel dtms=new DefaultTableModel();
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlAlmacen() throws Exception {
        initComponents();
        inicializar();
        llenarComboMateriales();
        llenarComboUnidadMedida();
        llenarComboTipoMoneda();
    }
    
    
//    //Método que se invoca al inicializar el panel, inicializa todas las clases a utilizar
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        
        jrFiltroFechas.setSelected(false);
        dcFechaDe.setEnabled(false);
        dcFechaHasta.setEnabled(false);
        
        for (int i = 0; i < FrmPrincipal.roles.length; i++)
        {
            if (FrmPrincipal.roles[i].equals("Sistemas") || FrmPrincipal.roles[i].equals("Produccion"))
            {
                btnAgregarMaterial.setEnabled(true);
                btnRealizarEntrada.setEnabled(true);
                btnRealizarSalida.setEnabled(true);
                break;
            }
        }
        
        llenarComboTipoMaterial();
        llenarComboClasificacionMaterial();
        actualizarTablaMateriales();
    }
    
    private void llenarComboMateriales() throws Exception
    {   
        cmbMaterialEntrada.removeAllItems();
        cmbMaterialSalida.removeAllItems();

        int i = 0;
        while (i < lstMaterial.size())
        {
            cmbMaterialEntrada.addItem(lstMaterial.get(i).getDescripcion());
            cmbMaterialSalida.addItem(lstMaterial.get(i).getDescripcion());
            i++;
        }
    }
    
    private void llenarComboTipoMaterial() throws Exception
    {   
        int CatIdTipoMaterial = 1;
        CatalogoDetCommands cd = new CatalogoDetCommands();
        lstCatDetTipoMaterial = cd.CatalogoDetGetByCatId(CatIdTipoMaterial);
        
        cmbTipoMaterial.removeAllItems();
        cmbTipoMaterialAgregar.removeAllItems();

        int i = 0;
        while (i < lstCatDetTipoMaterial.size())
        {
            cmbTipoMaterial.addItem(lstCatDetTipoMaterial.get(i).getNombre());
            cmbTipoMaterialAgregar.addItem(lstCatDetTipoMaterial.get(i).getNombre());
            i++;
        }
    }
    
    private void llenarComboClasificacionMaterial() throws Exception
    {   
        int CatIdClasMaterial = 2;
        CatalogoDetCommands cd = new CatalogoDetCommands();
        lstCatDetClasMaterial = cd.CatalogoDetGetByCatId(CatIdClasMaterial);
        
        cmbClasificacionMaterial.removeAllItems();
        cmbClasificacionAgregar.removeAllItems();

        int i = 0;
        while (i < lstCatDetClasMaterial.size())
        {
            cmbClasificacionMaterial.addItem(lstCatDetClasMaterial.get(i).getNombre());
            cmbClasificacionAgregar.addItem(lstCatDetClasMaterial.get(i).getNombre());
            i++;
        }
    }
    
    private void llenarComboUnidadMedida() throws Exception
    {   
        UnidadMedidaCommands umc = new UnidadMedidaCommands();
        lstUnidadMedida = umc.obtenerUnidadMedida();
        
        cmbUnidadMedidaAgregar.removeAllItems();

        int i = 0;
        while (i < lstUnidadMedida.size())
        {
            cmbUnidadMedidaAgregar.addItem(lstUnidadMedida.get(i).getDescripcion());
            i++;
        }
    }
    
    private void llenarComboTipoMoneda() throws Exception
    {   
        TipoMonedaCommands tmc = new TipoMonedaCommands();
        lstTipoMoneda = tmc.obtenerMonedas();
        
        cmbMonedaAgregar.removeAllItems();

        int i = 0;
        while (i < lstTipoMoneda.size())
        {
            cmbMonedaAgregar.addItem(lstTipoMoneda.get(i).getDescripcion());
            i++;
        }
    }
    
    private void actualizarTablaMateriales()
    {
        DefaultTableModel dtm = null;
        MaterialCommands mc;
        String[] cols = new String[]
        {
            "Código","Material","Existencia","Unidad Medida","Precio","Tipo Moneda","Tipo Material","Clasificación","Estatus"
        };
        
        try 
        {
            mc = new MaterialCommands();
            lstMaterial = mc.MaterialGetAll();
            
            dtm = new DefaultTableModel()
            {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            dtm.setColumnIdentifiers(cols);
            dtm.setRowCount(lstMaterial.size());
            
            for (int i = 0; i < lstMaterial.size(); i++)
            {
                dtm.setValueAt(lstMaterial.get(i).getCodigo(), i, 0);
                dtm.setValueAt(lstMaterial.get(i).getDescripcion(), i, 1);
                dtm.setValueAt(lstMaterial.get(i).getExistencia(), i, 2);
                dtm.setValueAt(lstMaterial.get(i).getUnidadMedida(), i, 3);
                dtm.setValueAt(lstMaterial.get(i).getPrecio(), i, 4);
                dtm.setValueAt(lstMaterial.get(i).getTipoMoneda(), i, 5);
                dtm.setValueAt(lstMaterial.get(i).getTipoMaterial(), i, 6);
                dtm.setValueAt(lstMaterial.get(i).getClasificacion(), i, 7);
                dtm.setValueAt(lstMaterial.get(i).getEstatus(), i, 8);
            }
            tblMateriales.setModel(dtm);
            
            TableColumnModel columnModel = tblMateriales.getColumnModel();

            columnModel.getColumn(0).setPreferredWidth(120);
            columnModel.getColumn(1).setPreferredWidth(210);
            columnModel.getColumn(2).setPreferredWidth(200);
            columnModel.getColumn(3).setPreferredWidth(120);
            
            tblMateriales.getTableHeader().setReorderingAllowed(false);
        } 
        catch (Exception e) 
        {   
            e.printStackTrace();   
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD" , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Método que abre un dialogo
    private void abrirDialogo(JDialog dialogo, int ancho, int alto)
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
    
    //Método para inicializar y abrir el JDialog para agregar un material 
    private void iniDialogoAgregarMaterial()
    {
        txtCodigoAgregar.setText("");
        txtDescripcionAgregar.setText("");
        txtExistenciaAgregar.setText("");
        cmbUnidadMedidaAgregar.setSelectedIndex(0);
        txtPrecioAgregar.setText("");
        cmbMonedaAgregar.setSelectedIndex(0);
        cmbTipoMaterialAgregar.setSelectedIndex(0);
        cmbClasificacionAgregar.setSelectedIndex(0);
    }
    
    //Método para inicializar y abrir el JDialog para Realizar Entrada 
    private void iniDialogoEntrada()
    {
        cmbMaterialEntrada.setSelectedIndex(0);
        txtCodigoEntrada.setText(lstMaterial.get(0).getCodigo());
        lblUnidadMedidaEntrada.setText(lstMaterial.get(0).getUnidadMedida());
        txtCantidadEntrada.setText("");
        txtaComentariosEntrada.setText("");
        dcEntrada.setCurrent(null);
    }
    
    //Método para inicializar y abrir el JDialog para Realizar una salida 
    private void iniDialogoSalida()
    {
        cmbMaterialSalida.setSelectedIndex(0);
        txtCodigoSalida.setText(lstMaterial.get(0).getCodigo());
        lblUnidadMedidaSalida.setText(lstMaterial.get(0).getUnidadMedida());
        txtCantidadSalida.setText("");
        txtSolicitanteSalida.setText("");
        txtDepartamentoSalida.setText("");
        txtaComentariosSalida.setText("");
        dcSalida.setCurrent(null);
    }
     
    private void onChangeCmbMaterialEntrada()
    {
        int index = cmbMaterialEntrada.getSelectedIndex();
        txtCodigoEntrada.setText(lstMaterial.get(index).getCodigo());
        lblUnidadMedidaEntrada.setText(lstMaterial.get(index).getUnidadMedida());
    }
    
    private void onChangeCmbMaterialSalida()
    {
        int index = cmbMaterialSalida.getSelectedIndex();
        txtCodigoSalida.setText(lstMaterial.get(index).getCodigo());
        lblUnidadMedidaSalida.setText(lstMaterial.get(index).getUnidadMedida());
    }
    
    private void EntradaMaterialCreate() throws ParseException, Exception
    {
        EntradaMaterial entrada;
        EntradaMaterialCommands emc;
        int respuesta;
        
        if (ValidarEntrada())
        {
            entrada = new EntradaMaterial();
            emc = new EntradaMaterialCommands();
            
            entrada.setMaterialId(lstMaterial.get(cmbMaterialEntrada.getSelectedIndex()).getMaterialId());
            entrada.setCantidad(Double.parseDouble(txtCantidadEntrada.getText().trim()));
            entrada.setComentarios(txtaComentariosEntrada.getText().trim());
            entrada.setIdUsuario(FrmPrincipal.u.getIdUsuario());
            entrada.setFechaEntrada(new SimpleDateFormat("dd/MM/yyyy").parse(dcEntrada.getText()));
            
            respuesta = emc.EntradaMaterialCreate(entrada);
            
            if (respuesta > 0)
            {
                dlgEntradaMaterial.setVisible(false);
                JOptionPane.showMessageDialog(null, "Entrada realizada con éxito");
                actualizarTablaMateriales();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Error al registrar entrada, intente de nuevo","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void SalidaMaterialCreate() throws ParseException, Exception
    {
        SalidaMaterial salida;
        SalidaMaterialCommands sc;
        int respuesta;
        
        if (ValidarSalida())
        {
            salida = new SalidaMaterial();
            sc = new SalidaMaterialCommands();
            
            salida.setMaterialId(lstMaterial.get(cmbMaterialSalida.getSelectedIndex()).getMaterialId());
            salida.setCantidad(Double.parseDouble(txtCantidadSalida.getText().trim()));
            salida.setSolicitante(txtSolicitanteSalida.getText().trim());
            salida.setDepartamento(txtDepartamentoSalida.getText().trim());
            salida.setComentarios(txtaComentariosSalida.getText().trim());
            salida.setIdInsumoFichaProd(0);
            salida.setIdUsuario(FrmPrincipal.u.getIdUsuario());
            salida.setFechaSalida(new SimpleDateFormat("dd/MM/yyyy").parse(dcSalida.getText()));
            
            respuesta = sc.SalidaMaterialCreate(salida);
            
            if (respuesta > 0)
            {
                dlgSalidaMaterial.setVisible(false);
                JOptionPane.showMessageDialog(null, "Salida realizada con éxito");
                actualizarTablaMateriales();
            }
            else if (respuesta == 0)
            {
                JOptionPane.showMessageDialog(null, "Cantidad insuficiente en inventario","Mensaje",JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Error al registrar salida, intente de nuevo","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void MaterialCreate() throws ParseException, Exception
    {
        Material material;
        MaterialCommands mc;
        RespuestaDto respuesta;
        
        if (ValidarAgregarMaterial())
        {
            material = new Material();
            mc = new MaterialCommands();
            
            material.setCodigo(txtCodigoAgregar.getText().trim());
            material.setDescripcion(txtDescripcionAgregar.getText().trim());
            material.setExistencia(Double.parseDouble(txtExistenciaAgregar.getText().trim()));
            material.setIdUnidadMedida(lstUnidadMedida.get(cmbUnidadMedidaAgregar.getSelectedIndex()).getIdUnidadMedida());
            material.setPrecio(Double.parseDouble(txtPrecioAgregar.getText().trim()));
            material.setIdTipoMoneda(lstTipoMoneda.get(cmbMonedaAgregar.getSelectedIndex()).getIdTipoMoneda());
            material.setCatDetTipoMaterialId(lstCatDetTipoMaterial.get(cmbTipoMaterialAgregar.getSelectedIndex()).getCatDetId());
            material.setCatDetClasificacionId(lstCatDetClasMaterial.get(cmbClasificacionAgregar.getSelectedIndex()).getCatDetId());
            
            respuesta = mc.MaterialCreate(material);
            
            if (respuesta.getRespuesta() > 0)
            {
                dlgAgregarMaterial.setVisible(false);
                JOptionPane.showMessageDialog(null, respuesta.getMensaje());
                actualizarTablaMateriales();
            }
            else if (respuesta.getRespuesta() == 0)
            {
                JOptionPane.showMessageDialog(null, respuesta.getMensaje(),"Mensaje",JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, respuesta.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean ValidarAgregarMaterial()
    {
        boolean respuesta = true;
        String mensaje = "";
        
        if (txtCodigoAgregar.getText().trim().equals(""))
        {
            mensaje = "Ingrese un código";
            respuesta = false;
        }
        
        if (respuesta)
        {
            if (txtDescripcionAgregar.getText().trim().equals(""))
            {
                mensaje = "Ingrese descripción del material";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (txtExistenciaAgregar.getText().trim().equals("") || txtExistenciaAgregar.getText().trim().equals("."))
            {
                mensaje = "Ingrese existencia";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (Double.parseDouble(txtExistenciaAgregar.getText().trim()) <= 0)
            {
                mensaje = "Existencia debe ser mayor a 0";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (txtPrecioAgregar.getText().trim().equals("") || txtPrecioAgregar.getText().trim().equals("."))
            {
                mensaje = "Ingrese un precio";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (Double.parseDouble(txtPrecioAgregar.getText().trim()) < 0)
            {
                mensaje = "Precio no puede ser menor a 0";
                respuesta = false;
            }
        }
        
        if (!respuesta)
        {
            dlgAgregarMaterial.setVisible(false);
            JOptionPane.showMessageDialog(dcEntrada, mensaje,"",JOptionPane.WARNING_MESSAGE);
            dlgAgregarMaterial.setVisible(true);
        }
        
        return respuesta;
    }
    
    private boolean ValidarEntrada()
    {
        boolean respuesta = true;
        String mensaje = "";
        
        if (cmbMaterialEntrada.getSelectedItem().toString().equals(""))
        {
            mensaje = "Seleccione un material";
            respuesta = false;
        }
        
        if (respuesta)
        {
            if (txtCantidadEntrada.getText().trim().equals("") || txtCantidadEntrada.getText().trim().equals("."))
            {
                mensaje = "Ingrese cantidad";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (Double.parseDouble(txtCantidadEntrada.getText().trim()) <= 0)
            {
                mensaje = "Cantidad debe ser mayor a 0";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (dcEntrada.getText().trim().equals(""))
            {
                mensaje = "Seleccione fecha de entrada";
                respuesta = false;
            }
        }
        
        if (!respuesta)
        {
            dlgEntradaMaterial.setVisible(false);
            JOptionPane.showMessageDialog(dcEntrada, mensaje,"",JOptionPane.WARNING_MESSAGE);
            dlgEntradaMaterial.setVisible(true);
        }
        
        return respuesta;
    }
    
    private boolean ValidarSalida()
    {
        boolean respuesta = true;
        String mensaje = "";
        
        if (cmbMaterialSalida.getSelectedItem().toString().equals(""))
        {
            mensaje = "Seleccione un material";
            respuesta = false;
        }
        
        if (respuesta)
        {
            if (txtCantidadSalida.getText().trim().equals("") || txtCantidadSalida.getText().trim().equals("."))
            {
                mensaje = "Ingrese cantidad";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (Double.parseDouble(txtCantidadSalida.getText().trim()) <= 0)
            {
                mensaje = "Cantidad debe ser mayor a 0";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (txtSolicitanteSalida.getText().trim().equals(""))
            {
                mensaje = "Ingrese solicitante";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (txtDepartamentoSalida.getText().trim().equals(""))
            {
                mensaje = "Ingrese departamento";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (dcSalida.getText().trim().equals(""))
            {
                mensaje = "Seleccione fecha de salida";
                respuesta = false;
            }
        }
        
        if (!respuesta)
        {
            dlgSalidaMaterial.setVisible(false);
            JOptionPane.showMessageDialog(dcEntrada, mensaje,"",JOptionPane.WARNING_MESSAGE);
            dlgSalidaMaterial.setVisible(true);
        }
        
        return respuesta;
    }
    
    private void ValidarNumeros(java.awt.event.KeyEvent evt, String textoCaja)
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
    
    private void ValidarLongitud(java.awt.event.KeyEvent evt, String textoCaja, int tamanioMaximo)
    {
        try {
            char c = evt.getKeyChar();    
            int longitud = textoCaja.trim().length();
            
            if (longitud == tamanioMaximo)
            {
                evt.consume();
            }
            
        } catch (Exception ex) {
            Logger.getLogger(PnlSemiterminado.class.getName()).log(Level.SEVERE, null, ex);
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
        dlgEntradaMaterial = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        lblRealizarEntradaDlg = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbMaterialEntrada = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCantidadEntrada = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtaComentariosEntrada = new javax.swing.JTextArea();
        dcEntrada = new datechooser.beans.DateChooserCombo();
        btnGuardarEntrada = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtCodigoEntrada = new javax.swing.JTextField();
        lblUnidadMedidaEntrada = new javax.swing.JLabel();
        dlgSalidaMaterial = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        lblSalidaMaterial = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbMaterialSalida = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCodigoSalida = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtaComentariosSalida = new javax.swing.JTextArea();
        dcSalida = new datechooser.beans.DateChooserCombo();
        btnGuardarSalida = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtCantidadSalida = new javax.swing.JTextField();
        lblUnidadMedidaSalida = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtSolicitanteSalida = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDepartamentoSalida = new javax.swing.JTextField();
        dlgAgregarMaterial = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        lblAgregarMaterial = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtCodigoAgregar = new javax.swing.JTextField();
        txtDescripcionAgregar = new javax.swing.JTextField();
        txtExistenciaAgregar = new javax.swing.JTextField();
        cmbUnidadMedidaAgregar = new javax.swing.JComboBox<>();
        txtPrecioAgregar = new javax.swing.JTextField();
        cmbMonedaAgregar = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        cmbTipoMaterialAgregar = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        cmbClasificacionAgregar = new javax.swing.JComboBox<>();
        btnGuardarAgregar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMateriales = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel12 = new javax.swing.JLabel();
        lblTipoMaterial = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbTipoMaterial = new javax.swing.JComboBox();
        jLabel58 = new javax.swing.JLabel();
        lblClasificacion = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cmbClasificacionMaterial = new javax.swing.JComboBox();
        jLabel60 = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNoPartida = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        lblCalendario = new javax.swing.JLabel();
        jrFiltroFechas = new javax.swing.JRadioButton();
        lblDe = new javax.swing.JLabel();
        dcFechaDe = new datechooser.beans.DateChooserCombo();
        lbl = new javax.swing.JLabel();
        lblHasta = new javax.swing.JLabel();
        dcFechaHasta = new datechooser.beans.DateChooserCombo();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnBuscar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        btnReporteEntradas = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        btnReporteSalidas = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        btnReporteInventario = new javax.swing.JButton();
        jLabel72 = new javax.swing.JLabel();
        jToolBar3 = new javax.swing.JToolBar();
        lblEnviarTerminado = new javax.swing.JLabel();
        btnAgregarMaterial = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        btnRealizarEntrada = new javax.swing.JButton();
        jLabel76 = new javax.swing.JLabel();
        btnRealizarSalida = new javax.swing.JButton();
        jLabel75 = new javax.swing.JLabel();

        dlgEntradaMaterial.setPreferredSize(new java.awt.Dimension(600, 335));

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(440, 33));

        lblRealizarEntradaDlg.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblRealizarEntradaDlg.setForeground(new java.awt.Color(255, 255, 255));
        lblRealizarEntradaDlg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblRealizarEntradaDlg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_arriba16x16.png"))); // NOI18N
        lblRealizarEntradaDlg.setText("Entrada Material");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRealizarEntradaDlg, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblRealizarEntradaDlg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Material:");

        cmbMaterialEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMaterialEntradaActionPerformed(evt);
            }
        });

        jLabel2.setText("Cantidad:");

        jLabel3.setText("Comentarios:");

        jLabel4.setText("Fecha Entrada:");

        txtCantidadEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadEntradaKeyTyped(evt);
            }
        });

        txtaComentariosEntrada.setColumns(20);
        txtaComentariosEntrada.setRows(5);
        txtaComentariosEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtaComentariosEntradaKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(txtaComentariosEntrada);

        dcEntrada.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dcEntrada.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
    dcEntrada.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
    try {
        dcEntrada.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcEntrada.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));

    btnGuardarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
    btnGuardarEntrada.setText("Guardar");
    btnGuardarEntrada.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnGuardarEntradaActionPerformed(evt);
        }
    });

    jLabel13.setText("Código:");

    txtCodigoEntrada.setEditable(false);

    lblUnidadMedidaEntrada.setText("Unidad Medida");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(btnGuardarEntrada)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addGap(12, 12, 12)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dcEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbMaterialEntrada, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtCodigoEntrada, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                    .addComponent(txtCantidadEntrada, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblUnidadMedidaEntrada))))))
            .addGap(528, 528, 528))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(cmbMaterialEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel13)
                .addComponent(txtCodigoEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(txtCantidadEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblUnidadMedidaEntrada))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(13, 13, 13)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel4)
                .addComponent(dcEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
            .addComponent(btnGuardarEntrada)
            .addContainerGap())
    );

    javax.swing.GroupLayout dlgEntradaMaterialLayout = new javax.swing.GroupLayout(dlgEntradaMaterial.getContentPane());
    dlgEntradaMaterial.getContentPane().setLayout(dlgEntradaMaterialLayout);
    dlgEntradaMaterialLayout.setHorizontalGroup(
        dlgEntradaMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
    );
    dlgEntradaMaterialLayout.setVerticalGroup(
        dlgEntradaMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(dlgEntradaMaterialLayout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel3.setBackground(new java.awt.Color(255, 51, 51));
    jPanel3.setPreferredSize(new java.awt.Dimension(440, 33));

    lblSalidaMaterial.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    lblSalidaMaterial.setForeground(new java.awt.Color(255, 255, 255));
    lblSalidaMaterial.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    lblSalidaMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
    lblSalidaMaterial.setText("Salida Material");

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(lblSalidaMaterial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(lblSalidaMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
    );

    jPanel5.setBackground(new java.awt.Color(255, 255, 255));

    jLabel5.setText("Material:");

    cmbMaterialSalida.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbMaterialSalidaActionPerformed(evt);
        }
    });

    jLabel6.setText("Código");

    jLabel7.setText("Comentarios:");

    jLabel10.setText("Fecha Salida:");

    txtCodigoSalida.setEditable(false);

    txtaComentariosSalida.setColumns(20);
    txtaComentariosSalida.setLineWrap(true);
    txtaComentariosSalida.setRows(5);
    txtaComentariosSalida.setWrapStyleWord(true);
    txtaComentariosSalida.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtaComentariosSalidaKeyTyped(evt);
        }
    });
    jScrollPane3.setViewportView(txtaComentariosSalida);

    dcSalida.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcSalida.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
dcSalida.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
try {
    dcSalida.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcSalida.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));

    btnGuardarSalida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
    btnGuardarSalida.setText("Guardar");
    btnGuardarSalida.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnGuardarSalidaActionPerformed(evt);
        }
    });

    jLabel14.setText("Cantidad");

    txtCantidadSalida.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtCantidadSalidaKeyTyped(evt);
        }
    });

    lblUnidadMedidaSalida.setText("Unidad de Medida");

    jLabel17.setText("Solicitante");

    txtSolicitanteSalida.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtSolicitanteSalidaKeyTyped(evt);
        }
    });

    jLabel18.setText("Departamento");

    txtDepartamentoSalida.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtDepartamentoSalidaKeyTyped(evt);
        }
    });

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardarSalida))
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6)
                                .addComponent(jLabel14)
                                .addComponent(jLabel17)
                                .addComponent(jLabel18))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtDepartamentoSalida, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSolicitanteSalida, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbMaterialSalida, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtCodigoSalida, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                                            .addComponent(txtCantidadSalida))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblUnidadMedidaSalida)))))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(25, 25, 25)
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(dcSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(cmbMaterialSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(txtCodigoSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel14)
                .addComponent(txtCantidadSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblUnidadMedidaSalida))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel17)
                .addComponent(txtSolicitanteSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel18)
                .addComponent(txtDepartamentoSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel10)
                .addComponent(dcSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGuardarSalida)
            .addContainerGap())
    );

    javax.swing.GroupLayout dlgSalidaMaterialLayout = new javax.swing.GroupLayout(dlgSalidaMaterial.getContentPane());
    dlgSalidaMaterial.getContentPane().setLayout(dlgSalidaMaterialLayout);
    dlgSalidaMaterialLayout.setHorizontalGroup(
        dlgSalidaMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    dlgSalidaMaterialLayout.setVerticalGroup(
        dlgSalidaMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(dlgSalidaMaterialLayout.createSequentialGroup()
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel6.setBackground(new java.awt.Color(0, 204, 51));
    jPanel6.setPreferredSize(new java.awt.Dimension(440, 33));

    lblAgregarMaterial.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    lblAgregarMaterial.setForeground(new java.awt.Color(255, 255, 255));
    lblAgregarMaterial.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    lblAgregarMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
    lblAgregarMaterial.setText("Agregar Material");

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(lblAgregarMaterial, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE))
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(lblAgregarMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
    );

    jPanel7.setBackground(new java.awt.Color(255, 255, 255));

    jLabel15.setText("Código");

    jLabel19.setText("Descripción");

    jLabel20.setText("Existencia");

    jLabel21.setText("Unidad de Medida");

    jLabel22.setText("Precio");

    jLabel25.setText("Clasificación");

    txtCodigoAgregar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txtCodigoAgregarActionPerformed(evt);
        }
    });
    txtCodigoAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtCodigoAgregarKeyTyped(evt);
        }
    });

    txtDescripcionAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtDescripcionAgregarKeyTyped(evt);
        }
    });

    txtExistenciaAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtExistenciaAgregarKeyTyped(evt);
        }
    });

    txtPrecioAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtPrecioAgregarKeyTyped(evt);
        }
    });

    jLabel26.setText("Moneda");

    jLabel23.setText("Tipo Material");

    btnGuardarAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
    btnGuardarAgregar.setText("Guardar");
    btnGuardarAgregar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnGuardarAgregarActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel15)
                        .addComponent(jLabel19)
                        .addComponent(jLabel20)
                        .addComponent(jLabel21)
                        .addComponent(jLabel22)
                        .addComponent(jLabel25)
                        .addComponent(jLabel26)
                        .addComponent(jLabel23))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtDescripcionAgregar)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cmbTipoMaterialAgregar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtCodigoAgregar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(txtExistenciaAgregar, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbUnidadMedidaAgregar, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtPrecioAgregar, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbMonedaAgregar, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(cmbClasificacionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(0, 0, Short.MAX_VALUE))))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnGuardarAgregar)))
            .addContainerGap())
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel15)
                .addComponent(txtCodigoAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel19)
                .addComponent(txtDescripcionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel20)
                .addComponent(txtExistenciaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel21)
                .addComponent(cmbUnidadMedidaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(10, 10, 10)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtPrecioAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel22))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(cmbMonedaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel26))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cmbTipoMaterialAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel23))
            .addGap(12, 12, 12)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel25)
                .addComponent(cmbClasificacionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
            .addComponent(btnGuardarAgregar)
            .addContainerGap())
    );

    javax.swing.GroupLayout dlgAgregarMaterialLayout = new javax.swing.GroupLayout(dlgAgregarMaterial.getContentPane());
    dlgAgregarMaterial.getContentPane().setLayout(dlgAgregarMaterialLayout);
    dlgAgregarMaterialLayout.setHorizontalGroup(
        dlgAgregarMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    dlgAgregarMaterialLayout.setVerticalGroup(
        dlgAgregarMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(dlgAgregarMaterialLayout.createSequentialGroup()
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    setBackground(new java.awt.Color(255, 255, 255));

    tblMateriales.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    tblMateriales.setModel(new javax.swing.table.DefaultTableModel(
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
    tblMateriales.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jScrollPane1.setViewportView(tblMateriales);

    jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);

    jLabel12.setText("   ");
    jToolBar1.add(jLabel12);

    lblTipoMaterial.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lblTipoMaterial.setText("Tipo Material:");
    jToolBar1.add(lblTipoMaterial);

    jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel8.setForeground(new java.awt.Color(227, 222, 222));
    jLabel8.setText("  ");
    jToolBar1.add(jLabel8);

    cmbTipoMaterial.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
    cmbTipoMaterial.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbTipoMaterial.setPreferredSize(new java.awt.Dimension(120, 25));
    cmbTipoMaterial.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbTipoMaterialActionPerformed(evt);
        }
    });
    jToolBar1.add(cmbTipoMaterial);

    jLabel58.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel58.setForeground(new java.awt.Color(227, 222, 222));
    jLabel58.setText("   ");
    jToolBar1.add(jLabel58);

    lblClasificacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lblClasificacion.setText("Clasificación:");
    jToolBar1.add(lblClasificacion);

    jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel16.setForeground(new java.awt.Color(227, 222, 222));
    jLabel16.setText("  ");
    jToolBar1.add(jLabel16);

    cmbClasificacionMaterial.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
    cmbClasificacionMaterial.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbClasificacionMaterial.setPreferredSize(new java.awt.Dimension(200, 25));
    cmbClasificacionMaterial.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbClasificacionMaterialActionPerformed(evt);
        }
    });
    jToolBar1.add(cmbClasificacionMaterial);

    jLabel60.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel60.setForeground(new java.awt.Color(227, 222, 222));
    jLabel60.setText("   ");
    jToolBar1.add(jLabel60);

    lblCodigo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lblCodigo.setText("Código");
    jToolBar1.add(lblCodigo);

    jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel9.setForeground(new java.awt.Color(227, 222, 222));
    jLabel9.setText("  ");
    jToolBar1.add(jLabel9);

    txtNoPartida.setMinimumSize(new java.awt.Dimension(60, 25));
    txtNoPartida.setName(""); // NOI18N
    txtNoPartida.setPreferredSize(new java.awt.Dimension(45, 25));
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

    lblCalendario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lblCalendario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/calendar.png"))); // NOI18N
    jToolBar1.add(lblCalendario);

    jrFiltroFechas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jrFiltroFechas.setFocusable(false);
    jrFiltroFechas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jrFiltroFechas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jrFiltroFechas.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
    jrFiltroFechas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jrFiltroFechas.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jrFiltroFechasActionPerformed(evt);
        }
    });
    jToolBar1.add(jrFiltroFechas);

    lblDe.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lblDe.setText("De:");
    jToolBar1.add(lblDe);

    dcFechaDe.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
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
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcFechaDe.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
dcFechaDe.setFormat(2);
dcFechaDe.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
try {
    dcFechaDe.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFechaDe.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar1.add(dcFechaDe);

    lbl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl.setForeground(new java.awt.Color(227, 222, 222));
    lbl.setText("   ");
    jToolBar1.add(lbl);

    lblHasta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lblHasta.setText("Hasta:");
    jToolBar1.add(lblHasta);

    dcFechaHasta.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
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
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcFechaHasta.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
try {
    dcFechaHasta.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFechaHasta.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar1.add(dcFechaHasta);
    jToolBar1.add(jSeparator2);

    btnBuscar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/magnifier.png"))); // NOI18N
    btnBuscar.setText("Buscar");
    btnBuscar.setFocusable(false);
    btnBuscar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnBuscar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnBuscar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnBuscar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnBuscarActionPerformed(evt);
        }
    });
    jToolBar1.add(btnBuscar);

    jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel11.setForeground(new java.awt.Color(227, 222, 222));
    jLabel11.setText("  ");
    jToolBar1.add(jLabel11);

    jToolBar2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar2.setFloatable(false);
    jToolBar2.setRollover(true);

    btnReporteEntradas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntradas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteEntradas.setText("Reporte Entradas");
    btnReporteEntradas.setFocusable(false);
    btnReporteEntradas.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    btnReporteEntradas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteEntradas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntradas.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntradasActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteEntradas);

    jLabel50.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel50.setForeground(new java.awt.Color(227, 222, 222));
    jLabel50.setText("     ");
    jToolBar2.add(jLabel50);

    btnReporteSalidas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteSalidas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteSalidas.setText("Reporte Salidas");
    btnReporteSalidas.setFocusable(false);
    btnReporteSalidas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteSalidas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteSalidas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteSalidas.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteSalidasActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteSalidas);

    jLabel51.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel51.setForeground(new java.awt.Color(227, 222, 222));
    jLabel51.setText("     ");
    jToolBar2.add(jLabel51);

    btnReporteInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/printer.png"))); // NOI18N
    btnReporteInventario.setText("Reporte Inventario Almacén");
    btnReporteInventario.setFocusable(false);
    btnReporteInventario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteInventario.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteInventario.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteInventarioActionPerformed(evt);
        }
    });
    jToolBar2.add(btnReporteInventario);

    jLabel72.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel72.setForeground(new java.awt.Color(227, 222, 222));
    jLabel72.setText("     ");
    jToolBar2.add(jLabel72);

    jToolBar3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar3.setFloatable(false);
    jToolBar3.setRollover(true);

    lblEnviarTerminado.setText("   ");
    jToolBar3.add(lblEnviarTerminado);

    btnAgregarMaterial.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnAgregarMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
    btnAgregarMaterial.setText("Agregar Material");
    btnAgregarMaterial.setEnabled(false);
    btnAgregarMaterial.setFocusable(false);
    btnAgregarMaterial.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnAgregarMaterial.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnAgregarMaterial.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAgregarMaterialActionPerformed(evt);
        }
    });
    jToolBar3.add(btnAgregarMaterial);

    jLabel61.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel61.setForeground(new java.awt.Color(227, 222, 222));
    jLabel61.setText("   ");
    jToolBar3.add(jLabel61);

    btnRealizarEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnRealizarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_arriba16x16.png"))); // NOI18N
    btnRealizarEntrada.setText("Entrada Material");
    btnRealizarEntrada.setEnabled(false);
    btnRealizarEntrada.setFocusable(false);
    btnRealizarEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnRealizarEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnRealizarEntrada.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnRealizarEntradaActionPerformed(evt);
        }
    });
    jToolBar3.add(btnRealizarEntrada);

    jLabel76.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel76.setForeground(new java.awt.Color(227, 222, 222));
    jLabel76.setText("   ");
    jToolBar3.add(jLabel76);

    btnRealizarSalida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnRealizarSalida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
    btnRealizarSalida.setText("Salida Material");
    btnRealizarSalida.setEnabled(false);
    btnRealizarSalida.setFocusable(false);
    btnRealizarSalida.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnRealizarSalida.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnRealizarSalida.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnRealizarSalidaActionPerformed(evt);
        }
    });
    jToolBar3.add(btnRealizarSalida);

    jLabel75.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel75.setForeground(new java.awt.Color(227, 222, 222));
    jLabel75.setText("   ");
    jToolBar3.add(jLabel75);

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

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jrFiltroFechasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrFiltroFechasActionPerformed
        if (dcFechaDe.isEnabled() && dcFechaHasta.isEnabled())
        {
            dcFechaDe.setEnabled(false);
            dcFechaDe.setCurrent(null);
            dcFechaHasta.setEnabled(false);
            dcFechaHasta.setCurrent(null);
        }
        else
        {
            dcFechaDe.setEnabled(true);
            dcFechaHasta.setEnabled(true);
        }
    }//GEN-LAST:event_jrFiltroFechasActionPerformed

    private void cmbTipoMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoMaterialActionPerformed
        
    }//GEN-LAST:event_cmbTipoMaterialActionPerformed

    private void btnReporteEntradasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradasActionPerformed
        
        
    }//GEN-LAST:event_btnReporteEntradasActionPerformed

    private void btnReporteInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteInventarioActionPerformed
        
    }//GEN-LAST:event_btnReporteInventarioActionPerformed

    private void btnReporteSalidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteSalidasActionPerformed
        
    }//GEN-LAST:event_btnReporteSalidasActionPerformed

    private void txtNoPartidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaKeyTyped
        
    }//GEN-LAST:event_txtNoPartidaKeyTyped

    private void btnRealizarSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarSalidaActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Realizar surtido de ficha de producción?", "Realizar Salida", JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            
        } else {
            iniDialogoSalida();
            abrirDialogo(dlgSalidaMaterial, 600, 450);
        }
    }//GEN-LAST:event_btnRealizarSalidaActionPerformed

    private void txtNoPartidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaKeyPressed
        
    }//GEN-LAST:event_txtNoPartidaKeyPressed

    private void cmbClasificacionMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClasificacionMaterialActionPerformed
        
    }//GEN-LAST:event_cmbClasificacionMaterialActionPerformed

    private void btnAgregarMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarMaterialActionPerformed
        iniDialogoAgregarMaterial();
        abrirDialogo(dlgAgregarMaterial, 600, 450);
    }//GEN-LAST:event_btnAgregarMaterialActionPerformed

    private void btnRealizarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaActionPerformed
        iniDialogoEntrada();
        abrirDialogo(dlgEntradaMaterial, 600, 355);
    }//GEN-LAST:event_btnRealizarEntradaActionPerformed

    private void cmbMaterialEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMaterialEntradaActionPerformed
        onChangeCmbMaterialEntrada();
    }//GEN-LAST:event_cmbMaterialEntradaActionPerformed

    private void btnGuardarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEntradaActionPerformed
        try {
            EntradaMaterialCreate();
        } catch (Exception ex) {
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarEntradaActionPerformed

    private void txtCantidadEntradaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadEntradaKeyTyped
        ValidarNumeros(evt, txtCantidadEntrada.getText());
    }//GEN-LAST:event_txtCantidadEntradaKeyTyped

    private void txtaComentariosEntradaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtaComentariosEntradaKeyTyped
        ValidarLongitud(evt,txtaComentariosEntrada.getText(),300);
    }//GEN-LAST:event_txtaComentariosEntradaKeyTyped

    private void btnGuardarSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarSalidaActionPerformed
        try {
            SalidaMaterialCreate();
        } catch (Exception ex) {
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarSalidaActionPerformed

    private void txtaComentariosSalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtaComentariosSalidaKeyTyped
        ValidarLongitud(evt,txtaComentariosSalida.getText(),300);
    }//GEN-LAST:event_txtaComentariosSalidaKeyTyped

    private void txtCantidadSalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadSalidaKeyTyped
        ValidarNumeros(evt, txtCantidadSalida.getText());
    }//GEN-LAST:event_txtCantidadSalidaKeyTyped

    private void txtSolicitanteSalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSolicitanteSalidaKeyTyped
        ValidarLongitud(evt,txtaComentariosSalida.getText(),100);
    }//GEN-LAST:event_txtSolicitanteSalidaKeyTyped

    private void txtDepartamentoSalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDepartamentoSalidaKeyTyped
        ValidarLongitud(evt,txtaComentariosSalida.getText(),50);
    }//GEN-LAST:event_txtDepartamentoSalidaKeyTyped

    private void cmbMaterialSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMaterialSalidaActionPerformed
        onChangeCmbMaterialSalida();
    }//GEN-LAST:event_cmbMaterialSalidaActionPerformed

    private void txtCodigoAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoAgregarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoAgregarActionPerformed

    private void btnGuardarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAgregarActionPerformed
        try {
            MaterialCreate();
        } catch (Exception ex) {
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarAgregarActionPerformed

    private void txtCodigoAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoAgregarKeyTyped
        ValidarLongitud(evt,txtCodigoAgregar.getText(),10);
    }//GEN-LAST:event_txtCodigoAgregarKeyTyped

    private void txtDescripcionAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionAgregarKeyTyped
        ValidarLongitud(evt,txtDescripcionAgregar.getText(),100);
    }//GEN-LAST:event_txtDescripcionAgregarKeyTyped

    private void txtExistenciaAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExistenciaAgregarKeyTyped
        ValidarNumeros(evt, txtExistenciaAgregar.getText());
    }//GEN-LAST:event_txtExistenciaAgregarKeyTyped

    private void txtPrecioAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioAgregarKeyTyped
        ValidarNumeros(evt, txtCantidadEntrada.getText());
    }//GEN-LAST:event_txtPrecioAgregarKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarMaterial;
    private javax.swing.JButton btnBuscar;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardarAgregar;
    private javax.swing.JButton btnGuardarEntrada;
    private javax.swing.JButton btnGuardarSalida;
    private javax.swing.JButton btnRealizarEntrada;
    private javax.swing.JButton btnRealizarSalida;
    private javax.swing.JButton btnReporteEntradas;
    private javax.swing.JButton btnReporteInventario;
    private javax.swing.JButton btnReporteSalidas;
    private javax.swing.JComboBox<String> cmbClasificacionAgregar;
    private javax.swing.JComboBox cmbClasificacionMaterial;
    private javax.swing.JComboBox<String> cmbMaterialEntrada;
    private javax.swing.JComboBox<String> cmbMaterialSalida;
    private javax.swing.JComboBox<String> cmbMonedaAgregar;
    private javax.swing.JComboBox cmbTipoMaterial;
    private javax.swing.JComboBox<String> cmbTipoMaterialAgregar;
    private javax.swing.JComboBox<String> cmbUnidadMedidaAgregar;
    private datechooser.beans.DateChooserCombo dcEntrada;
    private datechooser.beans.DateChooserCombo dcFechaDe;
    private datechooser.beans.DateChooserCombo dcFechaHasta;
    private datechooser.beans.DateChooserCombo dcSalida;
    private javax.swing.JDialog dlgAgregarMaterial;
    private javax.swing.JDialog dlgEntradaMaterial;
    private javax.swing.JDialog dlgSalidaMaterial;
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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JRadioButton jrFiltroFechas;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblAgregarMaterial;
    private javax.swing.JLabel lblCalendario;
    private javax.swing.JLabel lblClasificacion;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblDe;
    private javax.swing.JLabel lblEnviarTerminado;
    private javax.swing.JLabel lblHasta;
    private javax.swing.JLabel lblRealizarEntradaDlg;
    private javax.swing.JLabel lblSalidaMaterial;
    private javax.swing.JLabel lblTipoMaterial;
    private javax.swing.JLabel lblUnidadMedidaEntrada;
    private javax.swing.JLabel lblUnidadMedidaSalida;
    private javax.swing.JTable tblMateriales;
    private javax.swing.JTextField txtCantidadEntrada;
    private javax.swing.JTextField txtCantidadSalida;
    private javax.swing.JTextField txtCodigoAgregar;
    private javax.swing.JTextField txtCodigoEntrada;
    private javax.swing.JTextField txtCodigoSalida;
    private javax.swing.JTextField txtDepartamentoSalida;
    private javax.swing.JTextField txtDescripcionAgregar;
    private javax.swing.JTextField txtExistenciaAgregar;
    private javax.swing.JTextField txtNoPartida;
    private javax.swing.JTextField txtPrecioAgregar;
    private javax.swing.JTextField txtSolicitanteSalida;
    private javax.swing.JTextArea txtaComentariosEntrada;
    private javax.swing.JTextArea txtaComentariosSalida;
    // End of variables declaration//GEN-END:variables
}
