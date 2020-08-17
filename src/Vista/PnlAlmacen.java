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
import Utils.ExportarExcel;
import static Vista.FrmPrincipal.lblVentana;
import static Vista.FrmPrincipal.pnlPrincipalx;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
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
public class PnlAlmacen extends javax.swing.JPanel {
    ConexionBD conexion;    
    List<Material> lstMaterial; 
    List<EntradaMaterial> lstEntradaMaterial; 
    List<SalidaMaterial> lstSalidaMaterial;
    List<CatalogoDet> lstCatDetTipoMaterial; 
    List<CatalogoDet> lstCatDetClasMaterial; 
    List<CatalogoDet> lstCatDetEstatus; 
    List<UnidadMedida> lstUnidadMedida; 
    List<TipoMoneda> lstTipoMoneda;
    PnlSalidaFichaMaterial pnlSalidaFichaMaterial;
    Material materialEditar = null;
    private final String imagen="/Imagenes/logo_esmar.png";
    int panelActual = 0;
    EntradaMaterial entradaEdicion;
    SalidaMaterial salidaEdicion;
    JTextFieldDateEditor dtFechaEntradaEditar;
    JTextFieldDateEditor dtFechaDeEntrada;
    JTextFieldDateEditor dtFechaHastaEntrada;
    JTextFieldDateEditor dtFechaSalidaEditar;
    JTextFieldDateEditor dtFechaDeSalida;
    JTextFieldDateEditor dtFechaHastaSalida;
    JTextFieldDateEditor dtFechaEntrada;
    JTextFieldDateEditor dtFechaSalida;
    
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
        llenarComboEstatus();
    }
    
    
    //Método que se invoca al inicializar el panel, inicializa todas las clases a utilizar
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        
        jrFiltroFechasEntradas.setSelected(false);
        dcFechaDeEntradas.setEnabled(false);
        dcFechaHastaEntradas.setEnabled(false);
        
        jrFiltroFechasSalidas.setSelected(false);
        dcFechaDeSalidas.setEnabled(false);
        dcFechaHastaSalidas.setEnabled(false);
        
        dtFechaEntradaEditar = (JTextFieldDateEditor) dcEntradaEditar.getDateEditor();
        dtFechaEntradaEditar.setEditable(false);
        dtFechaDeEntrada = (JTextFieldDateEditor) dcFechaDeEntradas.getDateEditor();
        dtFechaDeEntrada.setEditable(false);
        dtFechaHastaEntrada = (JTextFieldDateEditor) dcFechaHastaEntradas.getDateEditor();
        dtFechaHastaEntrada.setEditable(false);
        
        dtFechaSalidaEditar = (JTextFieldDateEditor) dcSalidaEditar.getDateEditor();
        dtFechaSalidaEditar.setEditable(false);
        dtFechaDeSalida = (JTextFieldDateEditor) dcFechaDeSalidas.getDateEditor();
        dtFechaDeSalida.setEditable(false);
        dtFechaHastaSalida = (JTextFieldDateEditor) dcFechaHastaSalidas.getDateEditor();
        dtFechaHastaSalida.setEditable(false);
        
        dtFechaEntrada = (JTextFieldDateEditor) dcEntrada.getDateEditor();
        dtFechaEntrada.setEditable(false);
        
        dtFechaSalida = (JTextFieldDateEditor) dcSalida.getDateEditor();
        dtFechaSalida.setEditable(false);
        
        for (int i = 0; i < FrmPrincipal.roles.length; i++)
        {
            if (FrmPrincipal.roles[i].contains("Sistemas") || FrmPrincipal.roles[i].contains("Almacen"))
            {
                btnAgregarMaterial.setEnabled(true);
                btnEditarMaterial.setEnabled(true);
                btnRealizarEntrada.setEnabled(true);
                btnRealizarSalida.setEnabled(true);
            }
            else
            {
                btnEditarEntrada.setVisible(false);
                btnEliminarEntrada.setVisible(false);
                
                btnEditarSalida.setVisible(false);
                btnEliminarSalida.setVisible(false);
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
        
        cmbTipoMaterialAgregar.removeAllItems();
        cmbTipoMaterialEditar.removeAllItems();

        int i = 0;
        while (i < lstCatDetTipoMaterial.size())
        {
            cmbTipoMaterial.addItem(lstCatDetTipoMaterial.get(i).getNombre());
            cmbTipoMaterialEntradas.addItem(lstCatDetTipoMaterial.get(i).getNombre());
            cmbTipoMaterialSalidas.addItem(lstCatDetTipoMaterial.get(i).getNombre());
            
            cmbTipoMaterialAgregar.addItem(lstCatDetTipoMaterial.get(i).getNombre());
            cmbTipoMaterialEditar.addItem(lstCatDetTipoMaterial.get(i).getNombre());
            i++;
        }
    }
    
    private void llenarComboClasificacionMaterial() throws Exception
    {   
        int CatIdClasMaterial = 2;
        CatalogoDetCommands cd = new CatalogoDetCommands();
        lstCatDetClasMaterial = cd.CatalogoDetGetByCatId(CatIdClasMaterial);
        
        cmbClasificacionAgregar.removeAllItems();
        cmbClasificacionEditar.removeAllItems();

        int i = 0;
        while (i < lstCatDetClasMaterial.size())
        {
            cmbClasificacionMaterial.addItem(lstCatDetClasMaterial.get(i).getNombre());
            cmbClasificacionMaterialEntradas.addItem(lstCatDetClasMaterial.get(i).getNombre());
            cmbClasificacionMaterialSalidas.addItem(lstCatDetClasMaterial.get(i).getNombre());
            
            cmbClasificacionAgregar.addItem(lstCatDetClasMaterial.get(i).getNombre());
            cmbClasificacionEditar.addItem(lstCatDetClasMaterial.get(i).getNombre());
            i++;
        }
    }
    
    private void llenarComboUnidadMedida() throws Exception
    {   
        UnidadMedidaCommands umc = new UnidadMedidaCommands();
        lstUnidadMedida = umc.obtenerUnidadMedida();
        
        cmbUnidadMedidaAgregar.removeAllItems();
        cmbUnidadMedidaEditar.removeAllItems();

        int i = 0;
        while (i < lstUnidadMedida.size())
        {
            cmbUnidadMedidaAgregar.addItem(lstUnidadMedida.get(i).getDescripcion());
            cmbUnidadMedidaEditar.addItem(lstUnidadMedida.get(i).getDescripcion());
            i++;
        }
    }
    
    private void llenarComboTipoMoneda() throws Exception
    {   
        TipoMonedaCommands tmc = new TipoMonedaCommands();
        lstTipoMoneda = tmc.obtenerMonedas();
        
        cmbMonedaAgregar.removeAllItems();
        cmbMonedaEditar.removeAllItems();

        int i = 0;
        while (i < lstTipoMoneda.size())
        {
            cmbMonedaAgregar.addItem(lstTipoMoneda.get(i).getDescripcion());
            cmbMonedaEditar.addItem(lstTipoMoneda.get(i).getDescripcion());
            i++;
        }
    }
    
    private void llenarComboEstatus() throws Exception
    {   
        int CatIdEstatus = 3;
        CatalogoDetCommands cd = new CatalogoDetCommands();
        lstCatDetEstatus = cd.CatalogoDetGetByCatId(CatIdEstatus);
        
        cmbEstatusEditar.removeAllItems();

        int i = 0;
        while (i < lstCatDetEstatus.size())
        {
            cmbEstatusEditar.addItem(lstCatDetEstatus.get(i).getNombre());
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
            Material m = new Material();
            mc = new MaterialCommands();
            
            m.setCodigo(txtCodigo.getText().trim());
            
            if (cmbTipoMaterial.getSelectedIndex() > 0)
            {
                m.setCatDetTipoMaterialId(lstCatDetTipoMaterial.get(cmbTipoMaterial.getSelectedIndex()-1).getCatDetId());
            }
            else
            {
                m.setCatDetTipoMaterialId(0);
            }
            
            if (cmbClasificacionMaterial.getSelectedIndex() > 0)
            {
                m.setCatDetClasificacionId(lstCatDetClasMaterial.get(cmbClasificacionMaterial.getSelectedIndex()-1).getCatDetId());
            }
            else
            {
                m.setCatDetClasificacionId(0);
            }

            lstMaterial = mc.MaterialGetAll(m);
            
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

            columnModel.getColumn(0).setPreferredWidth(30);
            columnModel.getColumn(1).setPreferredWidth(250);
            
            tblMateriales.getTableHeader().setReorderingAllowed(false);
        } 
        catch (Exception e) 
        {   
            e.printStackTrace();   
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD" , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarTablaEntradas()
    {
        DefaultTableModel dtm = null;
        String[] cols = new String[]
        {
            "Código","Descripción","Cantidad","Unidad de Medida","Precio","Tipo Material","Clasificación","Comentarios","Fecha Entrada"
        };
        
        try 
        {
            lstEntradaMaterial = entradaMaterialGetList();
            
            dtm = new DefaultTableModel()
            {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            dtm.setColumnIdentifiers(cols);
            dtm.setRowCount(lstEntradaMaterial.size());
            
            for (int i = 0; i < lstEntradaMaterial.size(); i++)
            {
                dtm.setValueAt(lstEntradaMaterial.get(i).getCodigo(), i, 0);
                dtm.setValueAt(lstEntradaMaterial.get(i).getDescripcion(), i, 1);
                dtm.setValueAt(lstEntradaMaterial.get(i).getCantidad(), i, 2);
                dtm.setValueAt(lstEntradaMaterial.get(i).getUnidadMedida(), i, 3);
                dtm.setValueAt(lstEntradaMaterial.get(i).getPrecio(), i, 4);
                dtm.setValueAt(lstEntradaMaterial.get(i).getTipoMaterial(), i, 5);
                dtm.setValueAt(lstEntradaMaterial.get(i).getClasificacion(), i, 6);
                dtm.setValueAt(lstEntradaMaterial.get(i).getComentarios(), i, 7);
                dtm.setValueAt(lstEntradaMaterial.get(i).getFechaEntrada(), i, 8);
            }
            tblEntradas.setModel(dtm);
            
            TableColumnModel columnModel = tblEntradas.getColumnModel();

            columnModel.getColumn(0).setPreferredWidth(30);
            columnModel.getColumn(1).setPreferredWidth(250);
            
            tblEntradas.getTableHeader().setReorderingAllowed(false);
        } 
        catch (Exception e) 
        {   
            e.printStackTrace();   
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD" , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarTablaSalidas()
    {
        DefaultTableModel dtm = null;
        String[] cols = new String[]
        {
            "Código","Descripción","Cantidad","Unidad de Medida","Tipo Material","Clasificación","Ficha","Solicitante","Departamento","Comentarios","Fecha Salida"
        };
        
        try 
        {
            lstSalidaMaterial = salidaMaterialGetList();
            
            dtm = new DefaultTableModel()
            {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            dtm.setColumnIdentifiers(cols);
            dtm.setRowCount(lstSalidaMaterial.size());
            
            for (int i = 0; i < lstSalidaMaterial.size(); i++)
            {
                dtm.setValueAt(lstSalidaMaterial.get(i).getCodigo(), i, 0);
                dtm.setValueAt(lstSalidaMaterial.get(i).getDescripcion(), i, 1);
                dtm.setValueAt(lstSalidaMaterial.get(i).getCantidad(), i, 2);
                dtm.setValueAt(lstSalidaMaterial.get(i).getUnidadMedida(), i, 3);
                dtm.setValueAt(lstSalidaMaterial.get(i).getTipoMaterial(), i, 4);
                dtm.setValueAt(lstSalidaMaterial.get(i).getClasificacion(), i, 5);
                dtm.setValueAt(lstSalidaMaterial.get(i).getFicha() == 0 ? "" : lstSalidaMaterial.get(i).getFicha(), i, 6);
                dtm.setValueAt(lstSalidaMaterial.get(i).getSolicitante(), i, 7);
                dtm.setValueAt(lstSalidaMaterial.get(i).getDepartamento(), i, 8);
                dtm.setValueAt(lstSalidaMaterial.get(i).getComentarios(), i, 9);
                dtm.setValueAt(lstSalidaMaterial.get(i).getFechaSalida(), i, 10);
            }
            tblSalidas.setModel(dtm);
            
            TableColumnModel columnModel = tblSalidas.getColumnModel();

            columnModel.getColumn(0).setPreferredWidth(30);
            columnModel.getColumn(1).setPreferredWidth(250);
            
            tblSalidas.getTableHeader().setReorderingAllowed(false);
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
    
    //Método para inicializar y abrir el JDialog para editar un material 
    private void iniDialogoEditarMaterial()
    {
        txtCodigoEditar.setText(materialEditar.getCodigo());
        txtDescripcionEditar.setText(materialEditar.getDescripcion());
        
        for (int i = 0; i < cmbUnidadMedidaEditar.getItemCount(); i++)
        {
            if (cmbUnidadMedidaEditar.getItemAt(i).equals(materialEditar.getUnidadMedida()))
            {
                cmbUnidadMedidaEditar.setSelectedIndex(i);
                break;
            }
        }
        
        txtPrecioEditar.setText(materialEditar.getPrecio().toString());
        
        for (int i = 0; i < cmbMonedaEditar.getItemCount(); i++)
        {
            if (cmbMonedaEditar.getItemAt(i).equals(materialEditar.getTipoMoneda()))
            {
                cmbMonedaEditar.setSelectedIndex(i);
                break;
            }
        }
        
        for (int i = 0; i < cmbTipoMaterialEditar.getItemCount(); i++)
        {
            if (cmbTipoMaterialEditar.getItemAt(i).equals(materialEditar.getTipoMaterial()))
            {
                cmbTipoMaterialEditar.setSelectedIndex(i);
                break;
            }
        }
        
        for (int i = 0; i < cmbClasificacionEditar.getItemCount(); i++)
        {
            if (cmbClasificacionEditar.getItemAt(i).equals(materialEditar.getClasificacion()))
            {
                cmbClasificacionEditar.setSelectedIndex(i);
                break;
            }
        }
        
        for (int i = 0; i < cmbEstatusEditar.getItemCount(); i++)
        {
            if (cmbEstatusEditar.getItemAt(i).equals(materialEditar.getEstatus()))
            {
                cmbEstatusEditar.setSelectedIndex(i);
                break;
            }
        }
    }
    
    //Método para inicializar y abrir el JDialog para Realizar Entrada 
    private void iniDialogoEntrada()
    {
        cmbMaterialEntrada.setSelectedIndex(0);
        txtCodigoEntrada.setText(lstMaterial.get(0).getCodigo());
        lblUnidadMedidaEntrada.setText(lstMaterial.get(0).getUnidadMedida());
        txtCantidadEntrada.setText("");
        txtaComentariosEntrada.setText("");
        dcEntrada.setDate(null);
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
        dcSalida.setDate(null);
    }
     
    private void onChangeCmbMaterialEntrada()
    {
        int index = cmbMaterialEntrada.getSelectedIndex();
        if (index >= 0)
        {
            txtCodigoEntrada.setText(lstMaterial.get(index).getCodigo());
            txtPrecioEntrada.setText(lstMaterial.get(index).getPrecio().toString());
            lblUnidadMedidaEntrada.setText(lstMaterial.get(index).getUnidadMedida());
        }
    }
    
    private void onChangeCmbMaterialSalida()
    {
        int index = cmbMaterialSalida.getSelectedIndex();
        if (index >= 0)
        {
            txtCodigoSalida.setText(lstMaterial.get(index).getCodigo());
            lblUnidadMedidaSalida.setText(lstMaterial.get(index).getUnidadMedida());
        }
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
            entrada.setPrecio(Double.parseDouble(txtPrecioEntrada.getText().trim()));
            entrada.setComentarios(txtaComentariosEntrada.getText().trim());
            entrada.setIdUsuario(FrmPrincipal.u.getIdUsuario());
            entrada.setFechaEntrada(dcEntrada.getDate());
            
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
            salida.setFechaSalida(dcSalida.getDate());
            
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
                llenarComboMateriales();
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
    
    private void MaterialUpdate() throws ParseException, Exception
    {
        MaterialCommands mc;
        RespuestaDto respuesta;
        
        if (ValidarEditarMaterial())
        {
            mc = new MaterialCommands();
            
            materialEditar.setCodigo(txtCodigoEditar.getText().trim());
            materialEditar.setDescripcion(txtDescripcionEditar.getText().trim());
            materialEditar.setIdUnidadMedida(lstUnidadMedida.get(cmbUnidadMedidaEditar.getSelectedIndex()).getIdUnidadMedida());
            materialEditar.setPrecio(Double.parseDouble(txtPrecioEditar.getText().trim()));
            materialEditar.setIdTipoMoneda(lstTipoMoneda.get(cmbMonedaEditar.getSelectedIndex()).getIdTipoMoneda());
            materialEditar.setCatDetTipoMaterialId(lstCatDetTipoMaterial.get(cmbTipoMaterialEditar.getSelectedIndex()).getCatDetId());
            materialEditar.setCatDetClasificacionId(lstCatDetClasMaterial.get(cmbClasificacionEditar.getSelectedIndex()).getCatDetId());
            materialEditar.setCatDetEstatusId(lstCatDetEstatus.get(cmbEstatusEditar.getSelectedIndex()).getCatDetId());
            
            respuesta = mc.MaterialUpdate(materialEditar);
            
            if (respuesta.getRespuesta() > 0)
            {
                dlgEditarMaterial.setVisible(false);
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
    
    private void EntradaMaterialUpdate() throws ParseException, Exception
    {
        EntradaMaterial entrada;
        EntradaMaterialCommands emc;
        int respuesta;
        
        if (ValidarEditarEntradaMaterial())
        {
            entrada = entradaEdicion;
            emc = new EntradaMaterialCommands();
            
            entrada.setCantidad(Double.parseDouble(txtCantidadEntradaEditar.getText().trim()));
            entrada.setPrecio(Double.parseDouble(txtPrecioEntradaEditar.getText().trim()));
            entrada.setComentarios(txtaComentariosEntradaEditar.getText().trim());
            entrada.setIdUsuario(FrmPrincipal.u.getIdUsuario());
            entrada.setFechaEntrada(dcEntradaEditar.getDate());
            
            respuesta = emc.EntradaMaterialUpdate(entrada);
            
            if (respuesta > 0)
            {
                dlgEditarEntrada.setVisible(false);
                JOptionPane.showMessageDialog(null, "Edición realizada con éxito");
                actualizarTablaEntradas();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Error al editar entrada, intente de nuevo","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void SalidaMaterialUpdate() throws ParseException, Exception
    {
        SalidaMaterial salida;
        SalidaMaterialCommands smc;
        int respuesta;
        
        if (ValidarEditarSalida())
        {
            salida = salidaEdicion;
            smc = new SalidaMaterialCommands();
            
            salida.setCantidad(Double.parseDouble(txtCantidadSalidaEditar.getText().trim()));
            salida.setSolicitante(txtSolicitanteSalidaEditar.getText().trim());
            salida.setDepartamento(txtDepartamentoSalidaEditar.getText().trim());
            salida.setComentarios(txtaComentariosSalidaEditar.getText().trim());
            salida.setIdUsuario(FrmPrincipal.u.getIdUsuario());
            salida.setFechaSalida(dcSalidaEditar.getDate());
            
            respuesta = smc.SalidaMaterialUpdate(salida);
            
            if (respuesta > 0)
            {
                dlgEditarSalida.setVisible(false);
                JOptionPane.showMessageDialog(null, "Edición realizada con éxito");
                actualizarTablaSalidas();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Error al editar salida, intente de nuevo","Error",JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(dlgAgregarMaterial, mensaje,"",JOptionPane.WARNING_MESSAGE);
            dlgAgregarMaterial.setVisible(true);
        }
        
        return respuesta;
    }
    
    private boolean ValidarEditarMaterial()
    {
        boolean respuesta = true;
        String mensaje = "";
        
        if (txtCodigoEditar.getText().trim().equals(""))
        {
            mensaje = "Ingrese un código";
            respuesta = false;
        }
        
        if (respuesta)
        {
            if (txtPrecioEditar.getText().trim().equals("") || txtPrecioEditar.getText().trim().equals("."))
            {
                mensaje = "Ingrese un precio";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (Double.parseDouble(txtPrecioEditar.getText().trim()) < 0)
            {
                mensaje = "Precio no puede ser menor a 0";
                respuesta = false;
            }
        }
        
        if (!respuesta)
        {
            dlgEditarMaterial.setVisible(false);
            JOptionPane.showMessageDialog(dlgEditarMaterial, mensaje,"",JOptionPane.WARNING_MESSAGE);
            dlgEditarMaterial.setVisible(true);
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
            if (txtPrecioEntrada.getText().trim().equals("") || txtPrecioEntrada.getText().trim().equals("."))
            {
                mensaje = "Ingrese precio";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (Double.parseDouble(txtPrecioEntrada.getText().trim()) <= 0)
            {
                mensaje = "Precio debe ser mayor a 0";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (dcEntrada.getDate() == null)
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
    
    private boolean ValidarEditarEntradaMaterial()
    {
        boolean respuesta = true;
        String mensaje = "";
        
        if (respuesta)
        {
            if (txtCantidadEntradaEditar.getText().trim().equals("") || txtCantidadEntradaEditar.getText().trim().equals("."))
            {
                mensaje = "Ingrese cantidad";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (Double.parseDouble(txtCantidadEntradaEditar.getText().trim()) <= 0)
            {
                mensaje = "Cantidad debe ser mayor a 0";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (txtPrecioEntradaEditar.getText().trim().equals("") || txtPrecioEntradaEditar.getText().trim().equals("."))
            {
                mensaje = "Ingrese precio";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (Double.parseDouble(txtPrecioEntradaEditar.getText().trim()) <= 0)
            {
                mensaje = "Precio debe ser mayor a 0";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (dcEntradaEditar.getDate() == null)
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
            if (dcSalida.getDate() == null)
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
    
     private boolean ValidarEditarSalida()
    {
        boolean respuesta = true;
        String mensaje = "";
        
        if (txtCantidadSalidaEditar.getText().trim().equals("") || txtCantidadSalidaEditar.getText().trim().equals("."))
        {
            mensaje = "Ingrese cantidad";
            respuesta = false;
        }
        if (respuesta)
        {
            if (Double.parseDouble(txtCantidadSalidaEditar.getText().trim()) <= 0)
            {
                mensaje = "Cantidad debe ser mayor a 0";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (txtSolicitanteSalidaEditar.getText().trim().equals(""))
            {
                mensaje = "Ingrese solicitante";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (txtDepartamentoSalidaEditar.getText().trim().equals(""))
            {
                mensaje = "Ingrese departamento";
                respuesta = false;
            }
        }
        
        if (respuesta)
        {
            if (dcSalidaEditar.getDate() == null)
            {
                mensaje = "Seleccione fecha de salida";
                respuesta = false;
            }
        }
        
        if (!respuesta)
        {
            dlgEditarSalida.setVisible(false);
            JOptionPane.showMessageDialog(null, mensaje,"",JOptionPane.WARNING_MESSAGE);
            dlgEditarSalida.setVisible(true);
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
        btnGuardarEntrada = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtCodigoEntrada = new javax.swing.JTextField();
        lblUnidadMedidaEntrada = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtPrecioEntrada = new javax.swing.JTextField();
        dcEntrada = new com.toedter.calendar.JDateChooser();
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
        btnGuardarSalida = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtCantidadSalida = new javax.swing.JTextField();
        lblUnidadMedidaSalida = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtSolicitanteSalida = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDepartamentoSalida = new javax.swing.JTextField();
        dcSalida = new com.toedter.calendar.JDateChooser();
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
        dlgEditarMaterial = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        lblAgregarMaterial1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtCodigoEditar = new javax.swing.JTextField();
        txtDescripcionEditar = new javax.swing.JTextField();
        cmbUnidadMedidaEditar = new javax.swing.JComboBox<>();
        txtPrecioEditar = new javax.swing.JTextField();
        cmbMonedaEditar = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        cmbTipoMaterialEditar = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        cmbClasificacionEditar = new javax.swing.JComboBox<>();
        btnGuardarEditar = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        cmbEstatusEditar = new javax.swing.JComboBox<>();
        dlgEditarEntrada = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        lblEditarEntradaDlg = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        txtCantidadEntradaEditar = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtaComentariosEntradaEditar = new javax.swing.JTextArea();
        btnEditarEntradaGuardar = new javax.swing.JButton();
        jLabel49 = new javax.swing.JLabel();
        txtCodigoEntradaEditar = new javax.swing.JTextField();
        lblUnidadMedidaEntradaEditar = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        txtPrecioEntradaEditar = new javax.swing.JTextField();
        txtMaterialEntradaEditar = new javax.swing.JTextField();
        dcEntradaEditar = new com.toedter.calendar.JDateChooser();
        dlgEditarSalida = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        lblEditarEntradaDlg1 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        txtCantidadSalidaEditar = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtaComentariosSalidaEditar = new javax.swing.JTextArea();
        btnEditarSalidaGuardar = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        txtCodigoSalidaEditar = new javax.swing.JTextField();
        lblUnidadMedidaSalidaEditar = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        txtSolicitanteSalidaEditar = new javax.swing.JTextField();
        txtMaterialSalidaEditar = new javax.swing.JTextField();
        dcSalidaEditar = new com.toedter.calendar.JDateChooser();
        jLabel57 = new javax.swing.JLabel();
        txtDepartamentoSalidaEditar = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        PnlInventario = new javax.swing.JPanel();
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
        txtCodigo = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnBuscar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        btnReporteInventarioExcel = new javax.swing.JButton();
        jToolBar3 = new javax.swing.JToolBar();
        lblEnviarTerminado = new javax.swing.JLabel();
        btnAgregarMaterial = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        btnEditarMaterial = new javax.swing.JButton();
        jLabel62 = new javax.swing.JLabel();
        btnRealizarEntrada = new javax.swing.JButton();
        jLabel76 = new javax.swing.JLabel();
        btnRealizarSalida = new javax.swing.JButton();
        jLabel75 = new javax.swing.JLabel();
        PnlEntradas = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblEntradas = new javax.swing.JTable();
        jToolBar4 = new javax.swing.JToolBar();
        jLabel28 = new javax.swing.JLabel();
        lblTipoMaterial1 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        cmbTipoMaterialEntradas = new javax.swing.JComboBox();
        jLabel63 = new javax.swing.JLabel();
        lblClasificacion1 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        cmbClasificacionMaterialEntradas = new javax.swing.JComboBox();
        jLabel64 = new javax.swing.JLabel();
        lblCodigo1 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtCodigoEntradasMaterial = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        lblCalendario1 = new javax.swing.JLabel();
        jrFiltroFechasEntradas = new javax.swing.JRadioButton();
        lblDe1 = new javax.swing.JLabel();
        dcFechaDeEntradas = new com.toedter.calendar.JDateChooser();
        lbl1 = new javax.swing.JLabel();
        lblHasta1 = new javax.swing.JLabel();
        dcFechaHastaEntradas = new com.toedter.calendar.JDateChooser();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnBuscarEntradas = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jToolBar5 = new javax.swing.JToolBar();
        btnReporteEntradasExcel = new javax.swing.JButton();
        jToolBar6 = new javax.swing.JToolBar();
        jLabel66 = new javax.swing.JLabel();
        btnEditarEntrada = new javax.swing.JButton();
        jLabel67 = new javax.swing.JLabel();
        btnEliminarEntrada = new javax.swing.JButton();
        jLabel78 = new javax.swing.JLabel();
        PnlSalidas = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblSalidas = new javax.swing.JTable();
        jToolBar7 = new javax.swing.JToolBar();
        jLabel39 = new javax.swing.JLabel();
        lblTipoMaterial2 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        cmbTipoMaterialSalidas = new javax.swing.JComboBox();
        jLabel68 = new javax.swing.JLabel();
        lblClasificacion2 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        cmbClasificacionMaterialSalidas = new javax.swing.JComboBox();
        jLabel69 = new javax.swing.JLabel();
        lblCodigo2 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtCodigoSalidasMaterial = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        lblCalendario2 = new javax.swing.JLabel();
        jrFiltroFechasSalidas = new javax.swing.JRadioButton();
        lblDe2 = new javax.swing.JLabel();
        dcFechaDeSalidas = new com.toedter.calendar.JDateChooser();
        lbl2 = new javax.swing.JLabel();
        lblHasta2 = new javax.swing.JLabel();
        dcFechaHastaSalidas = new com.toedter.calendar.JDateChooser();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btnBuscarSalidas = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        jToolBar8 = new javax.swing.JToolBar();
        btnReporteSalidasExcel = new javax.swing.JButton();
        jToolBar9 = new javax.swing.JToolBar();
        jLabel71 = new javax.swing.JLabel();
        btnEditarSalida = new javax.swing.JButton();
        jLabel79 = new javax.swing.JLabel();
        btnEliminarSalida = new javax.swing.JButton();
        jLabel81 = new javax.swing.JLabel();

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

        jLabel44.setText("Precio:");

        txtPrecioEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioEntradaKeyTyped(evt);
            }
        });

        dcEntrada.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGuardarEntrada)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(12, 12, 12))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cmbMaterialEntrada, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtCodigoEntrada, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                        .addComponent(txtCantidadEntrada, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtPrecioEntrada, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblUnidadMedidaEntrada)))
                            .addComponent(dcEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addGap(10, 10, 10)
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

        dcSalida.setDateFormatString("dd/MM/yyyy");

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
                                .addComponent(dcSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        jPanel8.setBackground(new java.awt.Color(0, 204, 204));
        jPanel8.setPreferredSize(new java.awt.Dimension(440, 33));

        lblAgregarMaterial1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblAgregarMaterial1.setForeground(new java.awt.Color(255, 255, 255));
        lblAgregarMaterial1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAgregarMaterial1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pencil.png"))); // NOI18N
        lblAgregarMaterial1.setText("Editar Material");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAgregarMaterial1, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAgregarMaterial1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setText("Código");

        jLabel27.setText("Descripción");

        jLabel29.setText("Unidad de Medida");

        jLabel30.setText("Precio");

        jLabel31.setText("Clasificación");

        txtCodigoEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoEditarKeyTyped(evt);
            }
        });

        txtPrecioEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioEditarKeyTyped(evt);
            }
        });

        jLabel32.setText("Moneda");

        jLabel33.setText("Tipo Material");

        btnGuardarEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarEditar.setText("Guardar");
        btnGuardarEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEditarActionPerformed(evt);
            }
        });

        jLabel34.setText("Estatus");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDescripcionEditar)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(txtCodigoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardarEditar)
                .addGap(12, 12, 12))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbClasificacionEditar, 0, 210, Short.MAX_VALUE)
                    .addComponent(cmbTipoMaterialEditar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbMonedaEditar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPrecioEditar)
                    .addComponent(cmbUnidadMedidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbEstatusEditar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtCodigoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtDescripcionEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(cmbUnidadMedidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbMonedaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTipoMaterialEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(cmbClasificacionEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(cmbEstatusEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(btnGuardarEditar)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEditarMaterialLayout = new javax.swing.GroupLayout(dlgEditarMaterial.getContentPane());
        dlgEditarMaterial.getContentPane().setLayout(dlgEditarMaterialLayout);
        dlgEditarMaterialLayout.setHorizontalGroup(
            dlgEditarMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEditarMaterialLayout.setVerticalGroup(
            dlgEditarMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEditarMaterialLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 204, 204));
        jPanel4.setPreferredSize(new java.awt.Dimension(440, 33));

        lblEditarEntradaDlg.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblEditarEntradaDlg.setForeground(new java.awt.Color(255, 255, 255));
        lblEditarEntradaDlg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditarEntradaDlg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pencil.png"))); // NOI18N
        lblEditarEntradaDlg.setText("Editar Entrada Material");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEditarEntradaDlg, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblEditarEntradaDlg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel45.setText("Material:");

        jLabel46.setText("Cantidad:");

        jLabel47.setText("Comentarios:");

        jLabel48.setText("Fecha Entrada:");

        txtCantidadEntradaEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadEntradaEditarKeyTyped(evt);
            }
        });

        txtaComentariosEntradaEditar.setColumns(20);
        txtaComentariosEntradaEditar.setRows(5);
        txtaComentariosEntradaEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtaComentariosEntradaEditarKeyTyped(evt);
            }
        });
        jScrollPane6.setViewportView(txtaComentariosEntradaEditar);

        btnEditarEntradaGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnEditarEntradaGuardar.setText("Guardar");
        btnEditarEntradaGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarEntradaGuardarActionPerformed(evt);
            }
        });

        jLabel49.setText("Código:");

        txtCodigoEntradaEditar.setEditable(false);

        lblUnidadMedidaEntradaEditar.setText("Unidad Medida");

        jLabel50.setText("Precio:");

        txtPrecioEntradaEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioEntradaEditarKeyTyped(evt);
            }
        });

        txtMaterialEntradaEditar.setEditable(false);

        dcEntradaEditar.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEditarEntradaGuardar)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(12, 12, 12))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtCodigoEntradaEditar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                    .addComponent(txtCantidadEntradaEditar, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrecioEntradaEditar, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblUnidadMedidaEntradaEditar))
                            .addComponent(txtMaterialEntradaEditar)
                            .addComponent(dcEntradaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(528, 528, 528))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(txtMaterialEntradaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(txtCodigoEntradaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(txtCantidadEntradaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUnidadMedidaEntradaEditar))
                .addGap(13, 13, 13)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioEntradaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addGap(10, 10, 10)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48)
                    .addComponent(dcEntradaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(btnEditarEntradaGuardar)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEditarEntradaLayout = new javax.swing.GroupLayout(dlgEditarEntrada.getContentPane());
        dlgEditarEntrada.getContentPane().setLayout(dlgEditarEntradaLayout);
        dlgEditarEntradaLayout.setHorizontalGroup(
            dlgEditarEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
        dlgEditarEntradaLayout.setVerticalGroup(
            dlgEditarEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEditarEntradaLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(0, 204, 204));
        jPanel11.setPreferredSize(new java.awt.Dimension(440, 33));

        lblEditarEntradaDlg1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblEditarEntradaDlg1.setForeground(new java.awt.Color(255, 255, 255));
        lblEditarEntradaDlg1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditarEntradaDlg1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pencil.png"))); // NOI18N
        lblEditarEntradaDlg1.setText("Editar Salida Material");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEditarEntradaDlg1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblEditarEntradaDlg1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel51.setText("Material:");

        jLabel52.setText("Cantidad:");

        jLabel53.setText("Comentarios:");

        jLabel54.setText("Fecha Salida:");

        txtCantidadSalidaEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadSalidaEditarKeyTyped(evt);
            }
        });

        txtaComentariosSalidaEditar.setColumns(20);
        txtaComentariosSalidaEditar.setRows(5);
        txtaComentariosSalidaEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtaComentariosSalidaEditarKeyTyped(evt);
            }
        });
        jScrollPane7.setViewportView(txtaComentariosSalidaEditar);

        btnEditarSalidaGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnEditarSalidaGuardar.setText("Guardar");
        btnEditarSalidaGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarSalidaGuardarActionPerformed(evt);
            }
        });

        jLabel55.setText("Código:");

        txtCodigoSalidaEditar.setEditable(false);

        lblUnidadMedidaSalidaEditar.setText("Unidad Medida");

        jLabel56.setText("Solicitante:");

        txtSolicitanteSalidaEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSolicitanteSalidaEditarKeyTyped(evt);
            }
        });

        txtMaterialSalidaEditar.setEditable(false);

        dcSalidaEditar.setDateFormatString("dd/MM/yyyy");

        jLabel57.setText("Departamento:");

        txtDepartamentoSalidaEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDepartamentoSalidaEditarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEditarSalidaGuardar)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel54)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dcSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel57)
                                    .addComponent(jLabel56)
                                    .addComponent(jLabel55)
                                    .addComponent(jLabel52)
                                    .addComponent(jLabel51))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSolicitanteSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCodigoSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaterialSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtDepartamentoSalidaEditar, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                            .addComponent(txtCantidadSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(lblUnidadMedidaSalidaEditar)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel53)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(txtMaterialSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txtCodigoSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(txtCantidadSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUnidadMedidaSalidaEditar))
                .addGap(13, 13, 13)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSolicitanteSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDepartamentoSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dcSalidaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEditarSalidaGuardar)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgEditarSalidaLayout = new javax.swing.GroupLayout(dlgEditarSalida.getContentPane());
        dlgEditarSalida.getContentPane().setLayout(dlgEditarSalidaLayout);
        dlgEditarSalidaLayout.setHorizontalGroup(
            dlgEditarSalidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEditarSalidaLayout.setVerticalGroup(
            dlgEditarSalidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgEditarSalidaLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

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

        txtCodigo.setMinimumSize(new java.awt.Dimension(60, 25));
        txtCodigo.setName(""); // NOI18N
        txtCodigo.setPreferredSize(new java.awt.Dimension(45, 25));
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });
        jToolBar1.add(txtCodigo);
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

        btnReporteInventarioExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReporteInventarioExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/save_as_excel.png"))); // NOI18N
        btnReporteInventarioExcel.setText("Reporte Inventario");
        btnReporteInventarioExcel.setFocusable(false);
        btnReporteInventarioExcel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReporteInventarioExcel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnReporteInventarioExcel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReporteInventarioExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteInventarioExcelActionPerformed(evt);
            }
        });
        jToolBar2.add(btnReporteInventarioExcel);

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

        btnEditarMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pencil.png"))); // NOI18N
        btnEditarMaterial.setText("Editar Material");
        btnEditarMaterial.setEnabled(false);
        btnEditarMaterial.setFocusable(false);
        btnEditarMaterial.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEditarMaterial.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditarMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarMaterialActionPerformed(evt);
            }
        });
        jToolBar3.add(btnEditarMaterial);

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(227, 222, 222));
        jLabel62.setText("   ");
        jToolBar3.add(jLabel62);

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

        javax.swing.GroupLayout PnlInventarioLayout = new javax.swing.GroupLayout(PnlInventario);
        PnlInventario.setLayout(PnlInventarioLayout);
        PnlInventarioLayout.setHorizontalGroup(
            PnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 958, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PnlInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        PnlInventarioLayout.setVerticalGroup(
            PnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlInventarioLayout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Inventario", PnlInventario);

        tblEntradas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblEntradas.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEntradas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(tblEntradas);

        jToolBar4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        jLabel28.setText("   ");
        jToolBar4.add(jLabel28);

        lblTipoMaterial1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTipoMaterial1.setText("Tipo Material:");
        jToolBar4.add(lblTipoMaterial1);

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(227, 222, 222));
        jLabel35.setText("  ");
        jToolBar4.add(jLabel35);

        cmbTipoMaterialEntradas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
        cmbTipoMaterialEntradas.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbTipoMaterialEntradas.setPreferredSize(new java.awt.Dimension(120, 25));
        cmbTipoMaterialEntradas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoMaterialEntradasActionPerformed(evt);
            }
        });
        jToolBar4.add(cmbTipoMaterialEntradas);

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(227, 222, 222));
        jLabel63.setText("   ");
        jToolBar4.add(jLabel63);

        lblClasificacion1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblClasificacion1.setText("Clasificación:");
        jToolBar4.add(lblClasificacion1);

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(227, 222, 222));
        jLabel36.setText("  ");
        jToolBar4.add(jLabel36);

        cmbClasificacionMaterialEntradas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
        cmbClasificacionMaterialEntradas.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbClasificacionMaterialEntradas.setPreferredSize(new java.awt.Dimension(200, 25));
        cmbClasificacionMaterialEntradas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClasificacionMaterialEntradasActionPerformed(evt);
            }
        });
        jToolBar4.add(cmbClasificacionMaterialEntradas);

        jLabel64.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(227, 222, 222));
        jLabel64.setText("   ");
        jToolBar4.add(jLabel64);

        lblCodigo1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCodigo1.setText("Código");
        jToolBar4.add(lblCodigo1);

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(227, 222, 222));
        jLabel37.setText("  ");
        jToolBar4.add(jLabel37);

        txtCodigoEntradasMaterial.setMinimumSize(new java.awt.Dimension(60, 25));
        txtCodigoEntradasMaterial.setName(""); // NOI18N
        txtCodigoEntradasMaterial.setPreferredSize(new java.awt.Dimension(45, 25));
        txtCodigoEntradasMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoEntradasMaterialActionPerformed(evt);
            }
        });
        jToolBar4.add(txtCodigoEntradasMaterial);

        jLabel65.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(227, 222, 222));
        jLabel65.setText("  ");
        jToolBar4.add(jLabel65);
        jToolBar4.add(jSeparator3);

        lblCalendario1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCalendario1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/calendar.png"))); // NOI18N
        jToolBar4.add(lblCalendario1);

        jrFiltroFechasEntradas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrFiltroFechasEntradas.setFocusable(false);
        jrFiltroFechasEntradas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jrFiltroFechasEntradas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jrFiltroFechasEntradas.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jrFiltroFechasEntradas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jrFiltroFechasEntradas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrFiltroFechasEntradasActionPerformed(evt);
            }
        });
        jToolBar4.add(jrFiltroFechasEntradas);

        lblDe1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDe1.setText("De:");
        jToolBar4.add(lblDe1);

        dcFechaDeEntradas.setDateFormatString("dd/MM/yyyy");
        dcFechaDeEntradas.setPreferredSize(new java.awt.Dimension(130, 25));
        jToolBar4.add(dcFechaDeEntradas);

        lbl1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl1.setForeground(new java.awt.Color(227, 222, 222));
        lbl1.setText("   ");
        jToolBar4.add(lbl1);

        lblHasta1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHasta1.setText("Hasta:");
        jToolBar4.add(lblHasta1);

        dcFechaHastaEntradas.setDateFormatString("dd/MM/yyyy");
        dcFechaHastaEntradas.setPreferredSize(new java.awt.Dimension(130, 25));
        jToolBar4.add(dcFechaHastaEntradas);
        jToolBar4.add(jSeparator4);

        btnBuscarEntradas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBuscarEntradas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/magnifier.png"))); // NOI18N
        btnBuscarEntradas.setText("Buscar");
        btnBuscarEntradas.setFocusable(false);
        btnBuscarEntradas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBuscarEntradas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBuscarEntradas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBuscarEntradas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarEntradasActionPerformed(evt);
            }
        });
        jToolBar4.add(btnBuscarEntradas);

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(227, 222, 222));
        jLabel38.setText("  ");
        jToolBar4.add(jLabel38);

        jToolBar5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);

        btnReporteEntradasExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReporteEntradasExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/save_as_excel.png"))); // NOI18N
        btnReporteEntradasExcel.setText("Reporte Entradas");
        btnReporteEntradasExcel.setFocusable(false);
        btnReporteEntradasExcel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnReporteEntradasExcel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnReporteEntradasExcel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReporteEntradasExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteEntradasExcelActionPerformed(evt);
            }
        });
        jToolBar5.add(btnReporteEntradasExcel);

        jToolBar6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar6.setFloatable(false);
        jToolBar6.setRollover(true);

        jLabel66.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(227, 222, 222));
        jLabel66.setText("   ");
        jToolBar6.add(jLabel66);

        btnEditarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pencil.png"))); // NOI18N
        btnEditarEntrada.setText("Editar Entrada");
        btnEditarEntrada.setFocusable(false);
        btnEditarEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEditarEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditarEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarEntradaActionPerformed(evt);
            }
        });
        jToolBar6.add(btnEditarEntrada);

        jLabel67.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(227, 222, 222));
        jLabel67.setText("   ");
        jToolBar6.add(jLabel67);

        btnEliminarEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEliminarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        btnEliminarEntrada.setText("Eliminar Entrada");
        btnEliminarEntrada.setFocusable(false);
        btnEliminarEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEliminarEntrada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEliminarEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEntradaActionPerformed(evt);
            }
        });
        jToolBar6.add(btnEliminarEntrada);

        jLabel78.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(227, 222, 222));
        jLabel78.setText("   ");
        jToolBar6.add(jLabel78);

        javax.swing.GroupLayout PnlEntradasLayout = new javax.swing.GroupLayout(PnlEntradas);
        PnlEntradas.setLayout(PnlEntradasLayout);
        PnlEntradasLayout.setHorizontalGroup(
            PnlEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, 958, Short.MAX_VALUE)
            .addComponent(jToolBar5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PnlEntradasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        PnlEntradasLayout.setVerticalGroup(
            PnlEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlEntradasLayout.createSequentialGroup()
                .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Entradas", PnlEntradas);

        tblSalidas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblSalidas.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSalidas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane5.setViewportView(tblSalidas);

        jToolBar7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar7.setFloatable(false);
        jToolBar7.setRollover(true);

        jLabel39.setText("   ");
        jToolBar7.add(jLabel39);

        lblTipoMaterial2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTipoMaterial2.setText("Tipo Material:");
        jToolBar7.add(lblTipoMaterial2);

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(227, 222, 222));
        jLabel40.setText("  ");
        jToolBar7.add(jLabel40);

        cmbTipoMaterialSalidas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
        cmbTipoMaterialSalidas.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbTipoMaterialSalidas.setPreferredSize(new java.awt.Dimension(120, 25));
        cmbTipoMaterialSalidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoMaterialSalidasActionPerformed(evt);
            }
        });
        jToolBar7.add(cmbTipoMaterialSalidas);

        jLabel68.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(227, 222, 222));
        jLabel68.setText("   ");
        jToolBar7.add(jLabel68);

        lblClasificacion2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblClasificacion2.setText("Clasificación:");
        jToolBar7.add(lblClasificacion2);

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(227, 222, 222));
        jLabel41.setText("  ");
        jToolBar7.add(jLabel41);

        cmbClasificacionMaterialSalidas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
        cmbClasificacionMaterialSalidas.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbClasificacionMaterialSalidas.setPreferredSize(new java.awt.Dimension(200, 25));
        cmbClasificacionMaterialSalidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClasificacionMaterialSalidasActionPerformed(evt);
            }
        });
        jToolBar7.add(cmbClasificacionMaterialSalidas);

        jLabel69.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(227, 222, 222));
        jLabel69.setText("   ");
        jToolBar7.add(jLabel69);

        lblCodigo2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCodigo2.setText("Código");
        jToolBar7.add(lblCodigo2);

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(227, 222, 222));
        jLabel42.setText("  ");
        jToolBar7.add(jLabel42);

        txtCodigoSalidasMaterial.setMinimumSize(new java.awt.Dimension(60, 25));
        txtCodigoSalidasMaterial.setName(""); // NOI18N
        txtCodigoSalidasMaterial.setPreferredSize(new java.awt.Dimension(45, 25));
        txtCodigoSalidasMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoSalidasMaterialActionPerformed(evt);
            }
        });
        txtCodigoSalidasMaterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoSalidasMaterialKeyPressed(evt);
            }
        });
        jToolBar7.add(txtCodigoSalidasMaterial);

        jLabel70.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(227, 222, 222));
        jLabel70.setText("  ");
        jToolBar7.add(jLabel70);
        jToolBar7.add(jSeparator5);

        lblCalendario2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCalendario2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/calendar.png"))); // NOI18N
        jToolBar7.add(lblCalendario2);

        jrFiltroFechasSalidas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrFiltroFechasSalidas.setFocusable(false);
        jrFiltroFechasSalidas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jrFiltroFechasSalidas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jrFiltroFechasSalidas.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jrFiltroFechasSalidas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jrFiltroFechasSalidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrFiltroFechasSalidasActionPerformed(evt);
            }
        });
        jToolBar7.add(jrFiltroFechasSalidas);

        lblDe2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDe2.setText("De:");
        jToolBar7.add(lblDe2);

        dcFechaDeSalidas.setDateFormatString("dd/MM/yyyy");
        dcFechaDeSalidas.setPreferredSize(new java.awt.Dimension(130, 25));
        jToolBar7.add(dcFechaDeSalidas);

        lbl2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl2.setForeground(new java.awt.Color(227, 222, 222));
        lbl2.setText("   ");
        jToolBar7.add(lbl2);

        lblHasta2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHasta2.setText("Hasta:");
        jToolBar7.add(lblHasta2);

        dcFechaHastaSalidas.setDateFormatString("dd/MM/yyyy");
        dcFechaHastaSalidas.setPreferredSize(new java.awt.Dimension(130, 25));
        jToolBar7.add(dcFechaHastaSalidas);
        jToolBar7.add(jSeparator6);

        btnBuscarSalidas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBuscarSalidas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/magnifier.png"))); // NOI18N
        btnBuscarSalidas.setText("Buscar");
        btnBuscarSalidas.setFocusable(false);
        btnBuscarSalidas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBuscarSalidas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBuscarSalidas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBuscarSalidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarSalidasActionPerformed(evt);
            }
        });
        jToolBar7.add(btnBuscarSalidas);

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(227, 222, 222));
        jLabel43.setText("  ");
        jToolBar7.add(jLabel43);

        jToolBar8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar8.setFloatable(false);
        jToolBar8.setRollover(true);

        btnReporteSalidasExcel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReporteSalidasExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/save_as_excel.png"))); // NOI18N
        btnReporteSalidasExcel.setText("Reporte Salidas");
        btnReporteSalidasExcel.setFocusable(false);
        btnReporteSalidasExcel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReporteSalidasExcel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnReporteSalidasExcel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReporteSalidasExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteSalidasExcelActionPerformed(evt);
            }
        });
        jToolBar8.add(btnReporteSalidasExcel);

        jToolBar9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar9.setFloatable(false);
        jToolBar9.setRollover(true);

        jLabel71.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(227, 222, 222));
        jLabel71.setText("   ");
        jToolBar9.add(jLabel71);

        btnEditarSalida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pencil.png"))); // NOI18N
        btnEditarSalida.setText("Editar Salida");
        btnEditarSalida.setFocusable(false);
        btnEditarSalida.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEditarSalida.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditarSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarSalidaActionPerformed(evt);
            }
        });
        jToolBar9.add(btnEditarSalida);

        jLabel79.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(227, 222, 222));
        jLabel79.setText("   ");
        jToolBar9.add(jLabel79);

        btnEliminarSalida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEliminarSalida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        btnEliminarSalida.setText("Eliminar Salida");
        btnEliminarSalida.setFocusable(false);
        btnEliminarSalida.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEliminarSalida.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEliminarSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarSalidaActionPerformed(evt);
            }
        });
        jToolBar9.add(btnEliminarSalida);

        jLabel81.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(227, 222, 222));
        jLabel81.setText("   ");
        jToolBar9.add(jLabel81);

        javax.swing.GroupLayout PnlSalidasLayout = new javax.swing.GroupLayout(PnlSalidas);
        PnlSalidas.setLayout(PnlSalidasLayout);
        PnlSalidasLayout.setHorizontalGroup(
            PnlSalidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar7, javax.swing.GroupLayout.DEFAULT_SIZE, 958, Short.MAX_VALUE)
            .addComponent(jToolBar8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PnlSalidasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5)
                .addContainerGap())
        );
        PnlSalidasLayout.setVerticalGroup(
            PnlSalidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlSalidasLayout.createSequentialGroup()
                .addComponent(jToolBar8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Salidas", PnlSalidas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

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

    private void txtCodigoEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoEditarKeyTyped
        ValidarLongitud(evt,txtCodigoAgregar.getText(),10);
    }//GEN-LAST:event_txtCodigoEditarKeyTyped

    private void txtPrecioEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioEditarKeyTyped
        ValidarNumeros(evt, txtCantidadEntrada.getText());
    }//GEN-LAST:event_txtPrecioEditarKeyTyped

    private void btnGuardarEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEditarActionPerformed
        try {
            MaterialUpdate();
        } catch (Exception ex) {
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarEditarActionPerformed

    private void btnRealizarSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarSalidaActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Realizar surtido de ficha de producción?", "Realizar Salida", JOptionPane.YES_NO_OPTION);

        try {
            if (respuesta == JOptionPane.YES_OPTION) {
                pnlSalidaFichaMaterial = new PnlSalidaFichaMaterial();
                pnlPrincipalx.removeAll();
                pnlPrincipalx.add(pnlSalidaFichaMaterial, BorderLayout.CENTER);
                pnlPrincipalx.paintAll(pnlSalidaFichaMaterial.getGraphics());

                lblVentana.setText("Salida Ficha Material");
                ImageIcon ico=new ImageIcon("src/Imagenes/Flecha_abajo16x16.png");
                lblVentana.setIcon(ico);
            } else {
                iniDialogoSalida();
                abrirDialogo(dlgSalidaMaterial, 760, 450);
            }
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRealizarSalidaActionPerformed

    private void btnRealizarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaActionPerformed
        iniDialogoEntrada();
        abrirDialogo(dlgEntradaMaterial, 750, 400);
    }//GEN-LAST:event_btnRealizarEntradaActionPerformed

    private void btnEditarMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarMaterialActionPerformed
        if (tblMateriales.getSelectedRow() >= 0)
        {
            materialEditar = new Material();
            materialEditar = lstMaterial.get(tblMateriales.getSelectedRow());
            iniDialogoEditarMaterial();
            abrirDialogo(dlgEditarMaterial, 700, 450);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Seleccione un material para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarMaterialActionPerformed

    private void btnAgregarMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarMaterialActionPerformed
        iniDialogoAgregarMaterial();
        abrirDialogo(dlgAgregarMaterial, 600, 450);
    }//GEN-LAST:event_btnAgregarMaterialActionPerformed

    private void btnReporteInventarioExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteInventarioExcelActionPerformed
        ExportarExcel obj = new ExportarExcel();
        try {
            obj.exportarExcelJTable(tblMateriales);
        } catch (IOException ex) {
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnReporteInventarioExcelActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            actualizarTablaMateriales();
        }
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void cmbClasificacionMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClasificacionMaterialActionPerformed
        actualizarTablaMateriales();
    }//GEN-LAST:event_cmbClasificacionMaterialActionPerformed

    private void cmbTipoMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoMaterialActionPerformed
        actualizarTablaMateriales();
    }//GEN-LAST:event_cmbTipoMaterialActionPerformed

    private void cmbTipoMaterialEntradasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoMaterialEntradasActionPerformed
        actualizarTablaEntradas();
    }//GEN-LAST:event_cmbTipoMaterialEntradasActionPerformed

    private void cmbClasificacionMaterialEntradasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClasificacionMaterialEntradasActionPerformed
        actualizarTablaEntradas();
    }//GEN-LAST:event_cmbClasificacionMaterialEntradasActionPerformed

    private void jrFiltroFechasEntradasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrFiltroFechasEntradasActionPerformed
        if (dcFechaDeEntradas.isEnabled())
        {
            dcFechaDeEntradas.setEnabled(false);
            dtFechaDeEntrada.setDate(null);
            
            dcFechaHastaEntradas.setEnabled(false);
            dcFechaHastaEntradas.setDate(null);
        }
        else
        {
            dcFechaDeEntradas.setEnabled(true);
            dtFechaDeEntrada.setDate(null);
            
            dcFechaHastaEntradas.setEnabled(true);
            dcFechaHastaEntradas.setDate(null);
        }
    }//GEN-LAST:event_jrFiltroFechasEntradasActionPerformed

    private void btnBuscarEntradasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEntradasActionPerformed
        actualizarTablaEntradas();
    }//GEN-LAST:event_btnBuscarEntradasActionPerformed

    private void btnReporteEntradasExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradasExcelActionPerformed
        generarReporteEntradas();
    }//GEN-LAST:event_btnReporteEntradasExcelActionPerformed

    private void btnEditarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarEntradaActionPerformed
        try 
        {
            int fila = tblEntradas.getSelectedRow();
            if (fila >= 0)
            {
                entradaEdicion = lstEntradaMaterial.get(fila);

                txtMaterialEntradaEditar.setText(entradaEdicion.getDescripcion());
                txtCodigoEntradaEditar.setText(entradaEdicion.getCodigo());
                txtCantidadEntradaEditar.setText(entradaEdicion.getCantidad().toString());
                lblUnidadMedidaEntradaEditar.setText(entradaEdicion.getUnidadMedida());
                txtPrecioEntradaEditar.setText(entradaEdicion.getPrecio().toString());
                txtaComentariosEntradaEditar.setText(entradaEdicion.getComentarios());
                dtFechaEntradaEditar.setDate(entradaEdicion.getFechaEntrada());
                
                abrirDialogo(dlgEditarEntrada, 600, 400);
            }
            else
                JOptionPane.showMessageDialog(null, "Seleccione registro a editar","Mensaje",JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarEntradaActionPerformed

    private void btnEliminarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarEntradaActionPerformed
        try
        {
            int fila = tblEntradas.getSelectedRow();
            
            if (fila != -1)
            {
                if (JOptionPane.showConfirmDialog(null, "¿Realmente desea eliminar la entrada de material?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
                {
                    int entradaMaterialId = lstEntradaMaterial.get(fila).getEntradaMaterialId();
                    EntradaMaterialCommands emc = new EntradaMaterialCommands();
                    int respuesta = emc.EntradaMaterialDelete(entradaMaterialId);
                    
                    if (respuesta > 0)
                    {
                        JOptionPane.showMessageDialog(null, "Entrada de material eliminada");
                        actualizarTablaEntradas();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Error al eliminar entrada, intente de nuevo","Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla a eliminar","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        }
        catch (Exception e)
        {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarEntradaActionPerformed

    private void cmbTipoMaterialSalidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoMaterialSalidasActionPerformed
        actualizarTablaSalidas();
    }//GEN-LAST:event_cmbTipoMaterialSalidasActionPerformed

    private void cmbClasificacionMaterialSalidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClasificacionMaterialSalidasActionPerformed
        actualizarTablaSalidas();
    }//GEN-LAST:event_cmbClasificacionMaterialSalidasActionPerformed

    private void txtCodigoSalidasMaterialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoSalidasMaterialKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoSalidasMaterialKeyPressed

    private void jrFiltroFechasSalidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrFiltroFechasSalidasActionPerformed
        if (dcFechaDeSalidas.isEnabled())
        {
            dcFechaDeSalidas.setEnabled(false);
            dtFechaDeSalida.setDate(null);
            
            dcFechaHastaSalidas.setEnabled(false);
            dcFechaHastaSalidas.setDate(null);
        }
        else
        {
            dcFechaDeSalidas.setEnabled(true);
            dtFechaDeSalida.setDate(null);
            
            dcFechaHastaSalidas.setEnabled(true);
            dcFechaHastaSalidas.setDate(null);
        }
    }//GEN-LAST:event_jrFiltroFechasSalidasActionPerformed

    private void btnBuscarSalidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarSalidasActionPerformed
        actualizarTablaSalidas();
    }//GEN-LAST:event_btnBuscarSalidasActionPerformed

    private void btnReporteSalidasExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteSalidasExcelActionPerformed
        generarReporteSalidas();
    }//GEN-LAST:event_btnReporteSalidasExcelActionPerformed

    private void btnEditarSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarSalidaActionPerformed
        try 
        {
            int fila = tblSalidas.getSelectedRow();
            if (fila >= 0)
            {
                salidaEdicion = lstSalidaMaterial.get(fila);

                txtMaterialSalidaEditar.setText(salidaEdicion.getDescripcion());
                txtCodigoSalidaEditar.setText(salidaEdicion.getCodigo());
                txtCantidadSalidaEditar.setText(salidaEdicion.getCantidad().toString());  
                lblUnidadMedidaSalidaEditar.setText(salidaEdicion.getUnidadMedida());
                txtSolicitanteSalidaEditar.setText(salidaEdicion.getSolicitante());
                txtDepartamentoSalidaEditar.setText(salidaEdicion.getDepartamento());
                txtaComentariosSalidaEditar.setText(salidaEdicion.getComentarios());
                dtFechaSalidaEditar.setDate(salidaEdicion.getFechaSalida());
                
                abrirDialogo(dlgEditarSalida, 600, 450);
            }
            else
                JOptionPane.showMessageDialog(null, "Seleccione registro a editar","Mensaje",JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarSalidaActionPerformed

    private void btnEliminarSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarSalidaActionPerformed
        try
        {
            int fila = tblSalidas.getSelectedRow();
            
            if (fila != -1)
            {
                if (JOptionPane.showConfirmDialog(null, "¿Realmente desea eliminar la salida de material?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
                {
                    int salidaMaterialId = lstSalidaMaterial.get(fila).getSalidaMaterialId();
                    SalidaMaterialCommands smc = new SalidaMaterialCommands();
                    int respuesta = smc.SalidaMaterialDelete(salidaMaterialId);
                    
                    if (respuesta > 0)
                    {
                        JOptionPane.showMessageDialog(null, "Salida de material eliminada");
                        actualizarTablaSalidas();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Error al eliminar salida, intente de nuevo","Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla a eliminar","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        }
        catch (Exception e)
        {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_btnEliminarSalidaActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        actualizarTablaMateriales();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtCodigoEntradasMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoEntradasMaterialActionPerformed
        actualizarTablaEntradas();
    }//GEN-LAST:event_txtCodigoEntradasMaterialActionPerformed

    private void txtCodigoSalidasMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoSalidasMaterialActionPerformed
        actualizarTablaSalidas();
    }//GEN-LAST:event_txtCodigoSalidasMaterialActionPerformed

    private void txtPrecioEntradaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioEntradaKeyTyped
        ValidarNumeros(evt, txtPrecioEntrada.getText());
    }//GEN-LAST:event_txtPrecioEntradaKeyTyped

    private void txtCantidadEntradaEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadEntradaEditarKeyTyped
        ValidarNumeros(evt, txtCantidadEntradaEditar.getText());
    }//GEN-LAST:event_txtCantidadEntradaEditarKeyTyped

    private void txtaComentariosEntradaEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtaComentariosEntradaEditarKeyTyped
        ValidarLongitud(evt,txtaComentariosEntradaEditar.getText(),300);
    }//GEN-LAST:event_txtaComentariosEntradaEditarKeyTyped

    private void btnEditarEntradaGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarEntradaGuardarActionPerformed
        try {
            EntradaMaterialUpdate();
        } catch (Exception ex) {
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarEntradaGuardarActionPerformed

    private void txtPrecioEntradaEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioEntradaEditarKeyTyped
        ValidarNumeros(evt, txtPrecioEntradaEditar.getText());
    }//GEN-LAST:event_txtPrecioEntradaEditarKeyTyped

    private void txtCantidadSalidaEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadSalidaEditarKeyTyped
        ValidarNumeros(evt, txtCantidadSalidaEditar.getText());
    }//GEN-LAST:event_txtCantidadSalidaEditarKeyTyped

    private void txtaComentariosSalidaEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtaComentariosSalidaEditarKeyTyped
        ValidarLongitud(evt,txtaComentariosSalidaEditar.getText(),300);
    }//GEN-LAST:event_txtaComentariosSalidaEditarKeyTyped

    private void btnEditarSalidaGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarSalidaGuardarActionPerformed
        try {
            SalidaMaterialUpdate();
        } catch (Exception ex) {
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarSalidaGuardarActionPerformed

    private void txtSolicitanteSalidaEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSolicitanteSalidaEditarKeyTyped
        ValidarLongitud(evt,txtSolicitanteSalidaEditar.getText(),100);
    }//GEN-LAST:event_txtSolicitanteSalidaEditarKeyTyped

    private void txtDepartamentoSalidaEditarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDepartamentoSalidaEditarKeyTyped
        ValidarLongitud(evt,txtDepartamentoSalidaEditar.getText(),100);
    }//GEN-LAST:event_txtDepartamentoSalidaEditarKeyTyped

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if (panelActual != jTabbedPane1.getSelectedIndex())
        {
            panelActual = jTabbedPane1.getSelectedIndex();
            
            switch(jTabbedPane1.getSelectedIndex())
            {
                case 0:
                    actualizarTablaMateriales();
                    break;

                case 1:
                    actualizarTablaEntradas();
                    break;

                case 2:
                    actualizarTablaSalidas();
                    break;
            }
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlEntradas;
    private javax.swing.JPanel PnlInventario;
    private javax.swing.JPanel PnlSalidas;
    private javax.swing.JButton btnAgregarMaterial;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarEntradas;
    private javax.swing.JButton btnBuscarSalidas;
    private javax.swing.JButton btnEditarEntrada;
    private javax.swing.JButton btnEditarEntradaGuardar;
    private javax.swing.JButton btnEditarMaterial;
    private javax.swing.JButton btnEditarSalida;
    private javax.swing.JButton btnEditarSalidaGuardar;
    private javax.swing.JButton btnEliminarEntrada;
    private javax.swing.JButton btnEliminarSalida;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardarAgregar;
    private javax.swing.JButton btnGuardarEditar;
    private javax.swing.JButton btnGuardarEntrada;
    private javax.swing.JButton btnGuardarSalida;
    private javax.swing.JButton btnRealizarEntrada;
    private javax.swing.JButton btnRealizarSalida;
    private javax.swing.JButton btnReporteEntradasExcel;
    private javax.swing.JButton btnReporteInventarioExcel;
    private javax.swing.JButton btnReporteSalidasExcel;
    private javax.swing.JComboBox<String> cmbClasificacionAgregar;
    private javax.swing.JComboBox<String> cmbClasificacionEditar;
    private javax.swing.JComboBox cmbClasificacionMaterial;
    private javax.swing.JComboBox cmbClasificacionMaterialEntradas;
    private javax.swing.JComboBox cmbClasificacionMaterialSalidas;
    private javax.swing.JComboBox<String> cmbEstatusEditar;
    private javax.swing.JComboBox<String> cmbMaterialEntrada;
    private javax.swing.JComboBox<String> cmbMaterialSalida;
    private javax.swing.JComboBox<String> cmbMonedaAgregar;
    private javax.swing.JComboBox<String> cmbMonedaEditar;
    private javax.swing.JComboBox cmbTipoMaterial;
    private javax.swing.JComboBox<String> cmbTipoMaterialAgregar;
    private javax.swing.JComboBox<String> cmbTipoMaterialEditar;
    private javax.swing.JComboBox cmbTipoMaterialEntradas;
    private javax.swing.JComboBox cmbTipoMaterialSalidas;
    private javax.swing.JComboBox<String> cmbUnidadMedidaAgregar;
    private javax.swing.JComboBox<String> cmbUnidadMedidaEditar;
    private com.toedter.calendar.JDateChooser dcEntrada;
    private com.toedter.calendar.JDateChooser dcEntradaEditar;
    private com.toedter.calendar.JDateChooser dcFechaDeEntradas;
    private com.toedter.calendar.JDateChooser dcFechaDeSalidas;
    private com.toedter.calendar.JDateChooser dcFechaHastaEntradas;
    private com.toedter.calendar.JDateChooser dcFechaHastaSalidas;
    private com.toedter.calendar.JDateChooser dcSalida;
    private com.toedter.calendar.JDateChooser dcSalidaEditar;
    private javax.swing.JDialog dlgAgregarMaterial;
    private javax.swing.JDialog dlgEditarEntrada;
    private javax.swing.JDialog dlgEditarMaterial;
    private javax.swing.JDialog dlgEditarSalida;
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
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
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
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
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
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    private javax.swing.JToolBar jToolBar9;
    private javax.swing.JRadioButton jrFiltroFechasEntradas;
    private javax.swing.JRadioButton jrFiltroFechasSalidas;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lblAgregarMaterial;
    private javax.swing.JLabel lblAgregarMaterial1;
    private javax.swing.JLabel lblCalendario1;
    private javax.swing.JLabel lblCalendario2;
    private javax.swing.JLabel lblClasificacion;
    private javax.swing.JLabel lblClasificacion1;
    private javax.swing.JLabel lblClasificacion2;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblCodigo1;
    private javax.swing.JLabel lblCodigo2;
    private javax.swing.JLabel lblDe1;
    private javax.swing.JLabel lblDe2;
    private javax.swing.JLabel lblEditarEntradaDlg;
    private javax.swing.JLabel lblEditarEntradaDlg1;
    private javax.swing.JLabel lblEnviarTerminado;
    private javax.swing.JLabel lblHasta1;
    private javax.swing.JLabel lblHasta2;
    private javax.swing.JLabel lblRealizarEntradaDlg;
    private javax.swing.JLabel lblSalidaMaterial;
    private javax.swing.JLabel lblTipoMaterial;
    private javax.swing.JLabel lblTipoMaterial1;
    private javax.swing.JLabel lblTipoMaterial2;
    private javax.swing.JLabel lblUnidadMedidaEntrada;
    private javax.swing.JLabel lblUnidadMedidaEntradaEditar;
    private javax.swing.JLabel lblUnidadMedidaSalida;
    private javax.swing.JLabel lblUnidadMedidaSalidaEditar;
    private javax.swing.JTable tblEntradas;
    private javax.swing.JTable tblMateriales;
    private javax.swing.JTable tblSalidas;
    private javax.swing.JTextField txtCantidadEntrada;
    private javax.swing.JTextField txtCantidadEntradaEditar;
    private javax.swing.JTextField txtCantidadSalida;
    private javax.swing.JTextField txtCantidadSalidaEditar;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCodigoAgregar;
    private javax.swing.JTextField txtCodigoEditar;
    private javax.swing.JTextField txtCodigoEntrada;
    private javax.swing.JTextField txtCodigoEntradaEditar;
    private javax.swing.JTextField txtCodigoEntradasMaterial;
    private javax.swing.JTextField txtCodigoSalida;
    private javax.swing.JTextField txtCodigoSalidaEditar;
    private javax.swing.JTextField txtCodigoSalidasMaterial;
    private javax.swing.JTextField txtDepartamentoSalida;
    private javax.swing.JTextField txtDepartamentoSalidaEditar;
    private javax.swing.JTextField txtDescripcionAgregar;
    private javax.swing.JTextField txtDescripcionEditar;
    private javax.swing.JTextField txtExistenciaAgregar;
    private javax.swing.JTextField txtMaterialEntradaEditar;
    private javax.swing.JTextField txtMaterialSalidaEditar;
    private javax.swing.JTextField txtPrecioAgregar;
    private javax.swing.JTextField txtPrecioEditar;
    private javax.swing.JTextField txtPrecioEntrada;
    private javax.swing.JTextField txtPrecioEntradaEditar;
    private javax.swing.JTextField txtSolicitanteSalida;
    private javax.swing.JTextField txtSolicitanteSalidaEditar;
    private javax.swing.JTextArea txtaComentariosEntrada;
    private javax.swing.JTextArea txtaComentariosEntradaEditar;
    private javax.swing.JTextArea txtaComentariosSalida;
    private javax.swing.JTextArea txtaComentariosSalidaEditar;
    // End of variables declaration//GEN-END:variables

    private void generarReporteInventario() {
        try
        {
            actualizarTablaMateriales();
            URL path = this.getClass().getResource("/Reportes/ReporteInventarioAlmacen.jasper");
            
            Map parametros = new HashMap();
            parametros.put("imagen", this.getClass().getResourceAsStream(imagen));
            parametros.put("codigo", txtCodigo.getText().trim());
            
            if (cmbTipoMaterial.getSelectedIndex() == 0)
                parametros.put("catDetTipoMaterialId", 0);
            else
                parametros.put("catDetTipoMaterialId", lstCatDetTipoMaterial.get(cmbTipoMaterial.getSelectedIndex() - 1).getCatDetId());
            
            if (cmbClasificacionMaterial.getSelectedIndex() == 0)
                parametros.put("catDetClasificacionId", 0);
            else
                parametros.put("catDetClasificacionId", lstCatDetClasMaterial.get(cmbClasificacionMaterial.getSelectedIndex() - 1).getCatDetId());
            
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
            JOptionPane.showMessageDialog(null, "No se puede generar el reporte","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlRecepcionCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void generarReporteEntradas() {
        try
        {
            List<EntradaMaterial> entrada = entradaMaterialGetList();
            
            if (entrada.size() > 0)
            {
                String[][] report = new String[entrada.size()+1][9];
                
                // Encabezados
                report[0][0] = "Código";
                report[0][1] = "Descripción";
                report[0][2] = "Cantidad";
                report[0][3] = "Unidad de Medida";
                report[0][4] = "Precio";
                report[0][5] = "Tipo Material";
                report[0][6] = "Clasificación";
                report[0][7] = "Comentarios";
                report[0][8] = "Fecha Entrada";
                
                for(int i = 0; i < entrada.size(); i++)
                {
                    int position = i + 1;
                    report[position][0] = entrada.get(i).getCodigo();
                    report[position][1] = entrada.get(i).getDescripcion();
                    report[position][2] = entrada.get(i).getCantidad().toString();
                    report[position][3] = entrada.get(i).getUnidadMedida();
                    report[position][4] = entrada.get(i).getPrecio().toString();
                    report[position][5] = entrada.get(i).getTipoMaterial();
                    report[position][6] = entrada.get(i).getClasificacion();
                    report[position][7] = entrada.get(i).getComentarios();
                    report[position][8] = entrada.get(i).getFechaEntrada().toString();
                }
                ExportarExcel excel = new ExportarExcel();
                excel.exportarExcelArray(report);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No hay información","Mensaje",JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se puede generar el reporte","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void generarReporteSalidas() {
        try
        {
            List<SalidaMaterial> salida = salidaMaterialGetList();
            
            if (salida.size() > 0)
            {
                String[][] report = new String[salida.size()+1][11];
                
                // Encabezados
                report[0][0] = "Código";
                report[0][1] = "Descripción";
                report[0][2] = "Unidad de Medida";
                report[0][3] = "Cantidad";
                report[0][4] = "Tipo Material";
                report[0][5] = "Clasificación";
                report[0][6] = "Ficha";
                report[0][7] = "Solicitante";
                report[0][8] = "Departamento";
                report[0][9] = "Comentarios";
                report[0][10] = "Fecha Salida";
                
                for(int i = 0; i < salida.size(); i++)
                {
                    int position = i + 1;
                    report[position][0] = salida.get(i).getCodigo();
                    report[position][1] = salida.get(i).getDescripcion();
                    report[position][2] = salida.get(i).getUnidadMedida();
                    report[position][3] = salida.get(i).getCantidad().toString();
                    report[position][4] = salida.get(i).getTipoMaterial();
                    report[position][5] = salida.get(i).getClasificacion();
                    
                    if (salida.get(i).getFicha() > 0)
                        report[position][6] = String.valueOf(salida.get(i).getFicha());
                    else
                        report[position][6] = "";
                    
                    report[position][7] = salida.get(i).getSolicitante();
                    report[position][8] = salida.get(i).getDepartamento();
                    report[position][9] = salida.get(i).getComentarios();
                    report[position][10] = salida.get(i).getFechaSalida().toString();
                }
                ExportarExcel excel = new ExportarExcel();
                excel.exportarExcelArray(report);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No hay información","Mensaje",JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se puede generar el reporte","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private List<EntradaMaterial> entradaMaterialGetList()
    {
        List<EntradaMaterial> entradas = new ArrayList<EntradaMaterial>();
        try 
        {
            EntradaMaterialCommands emc = new EntradaMaterialCommands();
            String codigo = txtCodigoEntradasMaterial.getText().trim();
            int catDetTipoMaterialId = 0;
            int catDetClasificacionId = 0;
            String fechainicio = "";
            String fechafin = "";

            if (cmbTipoMaterialEntradas.getSelectedIndex() != 0)
                catDetTipoMaterialId = lstCatDetTipoMaterial.get(cmbTipoMaterialEntradas.getSelectedIndex() - 1).getCatDetId();

            if (cmbClasificacionMaterialEntradas.getSelectedIndex() != 0)
                catDetClasificacionId = lstCatDetClasMaterial.get(cmbClasificacionMaterialEntradas.getSelectedIndex() - 1).getCatDetId();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
            Date dinicio = dcFechaDeEntradas.getDate() == null ? null : dcFechaDeEntradas.getDate();
            String strDate = dinicio == null ? null : dateFormat.format(dinicio);
            fechainicio = strDate;

            Date dfin = dcFechaHastaEntradas.getDate() == null ? null : dcFechaHastaEntradas.getDate();
            String strDate2 = dfin == null ? null : dateFormat.format(dfin);   
            fechafin = strDate2;

            entradas = emc.EntradaMaterialGetAll(codigo,catDetTipoMaterialId,catDetClasificacionId,fechainicio,fechafin);
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, "Error al obtener entradas \n"+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, e);
        }
        finally
        {
            return entradas;
        }
    }
    
    private List<SalidaMaterial> salidaMaterialGetList()
    {
        List<SalidaMaterial> salidas = new ArrayList<SalidaMaterial>();
        try 
        {
            SalidaMaterialCommands smc = new SalidaMaterialCommands();
            String codigo = txtCodigoSalidasMaterial.getText().trim();
            int catDetTipoMaterialId = 0;
            int catDetClasificacionId = 0;
            String fechainicio = "";
            String fechafin = "";
            
            if (cmbTipoMaterialSalidas.getSelectedIndex() != 0)
                catDetTipoMaterialId = lstCatDetTipoMaterial.get(cmbTipoMaterialSalidas.getSelectedIndex() - 1).getCatDetId();
            
            if (cmbClasificacionMaterialSalidas.getSelectedIndex() != 0)
                catDetClasificacionId = lstCatDetClasMaterial.get(cmbClasificacionMaterialSalidas.getSelectedIndex() - 1).getCatDetId();
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
            Date dinicio = dcFechaDeSalidas.getDate() == null ? null : dcFechaDeSalidas.getDate();
            String strDate = dinicio == null ? null : dateFormat.format(dinicio);
            fechainicio = strDate;

            Date dfin = dcFechaHastaSalidas.getDate() == null ? null : dcFechaHastaSalidas.getDate();
            String strDate2 = dfin == null ? null : dateFormat.format(dfin);   
            fechafin = strDate2;

            salidas = smc.SalidaMaterialGetAll(codigo,catDetTipoMaterialId,catDetClasificacionId,fechainicio,fechafin);
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, "Error al obtener salidas \n"+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlAlmacen.class.getName()).log(Level.SEVERE, null, e);
        }
        finally
        {
            return salidas;
        }
    }
}
