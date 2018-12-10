/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.BajasPartidaDetCommands;
import Controlador.FichaProdCommands;
import Controlador.FichaProdDetCommands;
import Controlador.InsumosFichaProdCommands;
import Controlador.InsumosFichaProdDetCommands;
import Controlador.PartidaCommands;
import Controlador.PartidaDetalleCommands;
import Controlador.ProcesoCommands;
import Controlador.SubProcesoCommands;
import Controlador.TamborCommands;
import Modelo.BajasPartidaDet;
import Modelo.FichaProd;
import Modelo.FichaProdDet;
import Modelo.InsumosFichaProd;
import Modelo.InsumosFichaProdDet;
import Modelo.InsumosXFichaProd;
import Modelo.Partida;
import Modelo.PartidaDetalle;
import Modelo.PartidaDisp;
import Modelo.Proceso;
import Modelo.SubProceso;
import Modelo.Tambor;
import static Vista.FrmPrincipal.pnlPrincipalx;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Mingo
 */
public class PnlFichaProduccion extends javax.swing.JPanel {
    ProcesoCommands prc;
    Proceso pr;
    Partida p;
    PartidaDisp pad;
    PartidaCommands pc;
    PartidaDetalle pd;
    PartidaDetalleCommands pdc;
    SubProceso subP;
    SubProcesoCommands subPc;
    Tambor t;
    TamborCommands tc;
    BajasPartidaDet bpd;
    BajasPartidaDetCommands bpdc;
    String[][] proceso = null;
    String[][] subProceso = null;
    List<Tambor> lstTambor;
    List<PartidaDisp> lstPartidas = null;
    String recorteSeleccionado = null;
    String[][] asignados;
    DefaultTableModel dtms;
    DefaultTableModel dtmInsumos;
    int idSubproceso = 0;
    List<InsumosXFichaProd> lstInsumos;
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "Subproceso"
    };
    /**
     * Creates new form PnlFichaProduccion
     */
    public PnlFichaProduccion() {
        initComponents();
        inicializar();
    }
    
    private void inicializar()
    {
        try 
        {
            llenarComboProcesos();
            llenarComboTambores();
            actualizarTablaSubProc();
            inicializarTablaPartidasAgregadas();
            actualizarTablaInsumos();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de BD","Error",JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void llenarComboProcesos() throws Exception
    {
        prc = new ProcesoCommands();
        cmbProceso.removeAllItems();
        proceso = prc.llenarComboboxProcesos();
        
        int i=0;
        while (i<proceso.length)
        {
            cmbProceso.addItem(proceso[i][1]);
            i++;
        }
    }
    
    public void llenarComboTambores() throws Exception
    {
        tc = new TamborCommands();
        lstTambor = new ArrayList<>();
        cmbTambores.removeAllItems();
        
        lstTambor = tc.llenarComboboxTambores();
        
        int i=0;
        while (i < lstTambor.size())
        {
            cmbTambores.addItem(lstTambor.get(i).getNombreTambor());
            i++;
        }
    }
   
    //Método para actualizar la tabla de los subprocesos, se inicializa al llamar la clase
    public void actualizarTablaSubProc() 
    {
        pr = new Proceso();
        pr.setIdProceso(Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0]));

        DefaultTableModel dtm = null;
        
        try {
            
            subProceso = subPc.obtenerListaSubprocesosXid(pr);
            
            dtm = new DefaultTableModel(subProceso, cols){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblSubproceso.setModel(dtm);
            tblSubproceso.getTableHeader().setReorderingAllowed(false);
            if (dtm.getRowCount() > 0)
            {
                tblSubproceso.setRowSelectionInterval(0, 0);
            }

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD" , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Método para actualizar la tabla de las partidas disponibles por proceso
    public void actualizarTablaPartidasDisponibles() 
    {
        p = new Partida();
        p.setIdProceso(Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0]));
        
        if (p.getIdProceso() == 2)
        {
            btnEliminarPartida.setEnabled(true);
            btnEliminarRecorte.setEnabled(true);
        }
        else
        {
            btnEliminarPartida.setEnabled(false);
            btnEliminarRecorte.setEnabled(false);
        }
        
        btnSelAcabado.setEnabled(false);

        DefaultTableModel dtm = null;
        
        try 
        {
            pc = new PartidaCommands();
            lstPartidas = pc.obtenerPartidasDisponibles(p);
            
            if (lstPartidas != null)
            {
                asignados = new String[lstPartidas.size()][1];
                
                for (int i = 0; i < asignados.length; i++)
                {
                    asignados[i][0] = "0";
                }
            }
            else
            {
                asignados = new String[0][0];
            }
            
            String[] cols = new String[]
            {
                "No. Partida", "Prov - Camión", "Recorte", "No. Piezas"
            };
            
            dtm = new DefaultTableModel(){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            dtm.setColumnIdentifiers(cols);
            dtm.setRowCount(lstPartidas.size());
            for (int i = 0; i < lstPartidas.size(); i++)
            {
                dtm.setValueAt(lstPartidas.get(i).getNoPartida(), i, 0);
                dtm.setValueAt(lstPartidas.get(i).getProveedor(), i, 1);
                dtm.setValueAt(lstPartidas.get(i).getTipoRecorte(), i, 2);
                dtm.setValueAt(lstPartidas.get(i).getNoPiezasAct(), i, 3);
            }
            tblPartidasDisponibles.setModel(dtm);
            
            TableColumnModel columnModel = tblPartidasDisponibles.getColumnModel();

            columnModel.getColumn(0).setPreferredWidth(120);
            columnModel.getColumn(1).setPreferredWidth(210);
            columnModel.getColumn(2).setPreferredWidth(200);
            columnModel.getColumn(3).setPreferredWidth(120);
            
            tblPartidasDisponibles.getTableHeader().setReorderingAllowed(false);
        } 
        catch (Exception e) 
        {   
            e.printStackTrace();   
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD" , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Método para actualizar la tabla de las partidas disponibles por proceso
    public void actualizarPartidasDisponibles() 
    {
        p = new Partida();
        p.setIdProceso(Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0]));

        DefaultTableModel dtm = null;
        
        try 
        {
            pc = new PartidaCommands();
            lstPartidas = pc.obtenerPartidasDisponibles(p);
            
            if (lstPartidas != null)
            {
                asignados = new String[lstPartidas.size()][1];
                
                for (int i = 0; i < asignados.length; i++)
                {
                    asignados[i][0] = "0";
                }
            }
            
            dtm = new DefaultTableModel(){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            dtm.setRowCount(lstPartidas.size());
            for (int i = 0; i < lstPartidas.size(); i++)
            {
                dtm.setValueAt(lstPartidas.get(i).getNoPartida(), i, 0);
                dtm.setValueAt(lstPartidas.get(i).getTipoRecorte(), i, 1);
                dtm.setValueAt(lstPartidas.get(i).getNoPiezasAct(), i, 2);
            }
            tblPartidasDisponibles.setModel(dtm);
            tblPartidasDisponibles.getTableHeader().setReorderingAllowed(false);
        } 
        catch (Exception e) 
        {   
            e.printStackTrace();   
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD" , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void inicializarTablaPartidasAgregadas()
    {
        String[] columnas = new String[]
        {
            "No. Partida","Recorte","No. Piezas","Peso (Kg)","IdPartidaDet"
        };
        dtms = new DefaultTableModel()
        {
            public boolean isCellEditable (int row, int column)
            {
                // Aquí devolvemos true o false según queramos que una celda
                // identificada por fila,columna (row,column), sea o no editable
                if (column == 2 || column == 3)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };
        dtms.setColumnIdentifiers(columnas);
        tblPartidasAgregadas.removeAll();
        tblPartidasAgregadas.setModel(dtms);
        tblPartidasAgregadas.getColumnModel().getColumn(4).setMaxWidth(0);
        tblPartidasAgregadas.getColumnModel().getColumn(4).setMinWidth(0);
        tblPartidasAgregadas.getColumnModel().getColumn(4).setPreferredWidth(0);
        tblPartidasAgregadas.getTableHeader().setReorderingAllowed(false);
    }
    
    //Método que abre el dialogo para recortar un tipo de cuero
    public void abrirDialogoRecortar()
    {   
        dlgRecortar.setVisible(false);
        lblyRecortar.setVisible(false);
        txtNoPiezasRecortar2.setVisible(false);
        lblPiezasDe2.setVisible(false);
        lblTipoCuero2.setVisible(false);
        
        String[] tipoRecorte = null;
        String aRecortar = pad.getTipoRecorte();
        
        if (aRecortar.equals("Entero"))
        {
            tipoRecorte = new String[] { "Delantero/Crupon", "Lados" };
        }
        else if (aRecortar.equals("Crupon Sillero") || aRecortar.equals("Crupon"))
        {
            if (pad.getIdProceso() > 4)
            {
                tipoRecorte = new String[] { "Centro Castaño", "Centro Quebracho" };
            }
            else
            {
                recorteSeleccionado = "Centro";
            }
        }
        else if (aRecortar.equals("Delantero Sillero") || aRecortar.equals("Delantero"))
        {
            recorteSeleccionado = "Delantero Suela";
        }
        else if (aRecortar.equals("Lados"))
        {
            if (pad.getIdProceso() > 4)
            {
                tipoRecorte = new String[] { "Centro Castaño/Delantero Suela", "Centro Quebracho/Delantero Suela" };
            }
            else
            {
                recorteSeleccionado = "Centro/Delantero Suela";
            }
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
            txtNoPiezasRecortar.setText(String.valueOf(pad.getNoPiezasAct()));
            txtNoPiezasRecortar1.setText(String.valueOf(pad.getNoPiezasAct()*2));
            
            if (recorteSeleccionado.equals("Delantero/Crupon"))
            {
                lblyRecortar.setVisible(true);
                txtNoPiezasRecortar2.setVisible(true);
                lblPiezasDe2.setVisible(true);
                lblTipoCuero2.setVisible(true);
                
                txtNoPiezasRecortar1.setText(txtNoPiezasRecortar.getText());
                if (pr.getIdProceso() == 2 || pr.getIdProceso() == 3)
                {
                    lblTipoCuero1.setText("Delantero");
                }
                else
                {
                    lblTipoCuero1.setText("Delantero Sillero");
                }
                
                txtNoPiezasRecortar2.setText(txtNoPiezasRecortar.getText());
                if (pr.getIdProceso() == 2 || pr.getIdProceso() == 3)
                {
                    lblTipoCuero2.setText("Crupon");
                }
                else
                {
                    lblTipoCuero2.setText("Crupon Sillero");
                }
            }
            else if(recorteSeleccionado.equals("Centro Castaño/Delantero Suela"))
            {
                lblyRecortar.setVisible(true);
                txtNoPiezasRecortar2.setVisible(true);
                lblPiezasDe2.setVisible(true);
                lblTipoCuero2.setVisible(true);
                
                txtNoPiezasRecortar1.setText(txtNoPiezasRecortar.getText());
                lblTipoCuero1.setText("Centro Castaño");
                
                txtNoPiezasRecortar2.setText(txtNoPiezasRecortar.getText());
                lblTipoCuero2.setText("Delantero Suela");
            }
            else if(recorteSeleccionado.equals("Centro Quebracho/Delantero Suela"))
            {
                lblyRecortar.setVisible(true);
                txtNoPiezasRecortar2.setVisible(true);
                lblPiezasDe2.setVisible(true);
                lblTipoCuero2.setVisible(true);
                
                txtNoPiezasRecortar1.setText(txtNoPiezasRecortar.getText());
                lblTipoCuero1.setText("Centro Quebracho");
                
                txtNoPiezasRecortar2.setText(txtNoPiezasRecortar.getText());
                lblTipoCuero2.setText("Delantero Suela");
            }
            else if(recorteSeleccionado.equals("Centro/Delantero Suela"))
            {
                lblyRecortar.setVisible(true);
                txtNoPiezasRecortar2.setVisible(true);
                lblPiezasDe2.setVisible(true);
                lblTipoCuero2.setVisible(true);
                
                txtNoPiezasRecortar1.setText(txtNoPiezasRecortar.getText());
                lblTipoCuero1.setText("Centro");
                
                txtNoPiezasRecortar2.setText(txtNoPiezasRecortar.getText());
                lblTipoCuero2.setText("Delantero Suela");
            }
            else
            {
                lblTipoCuero1.setText(recorteSeleccionado);
            }
            
            switch (recorteSeleccionado) 
            {
                case "Delantero/Crupon":
                    pad.setIdTipoRecorte(0);
                    break;
                case "Centro Castaño/Delantero Suela":
                    pad.setIdTipoRecorte(1);
                    break;
                case "Centro Quebracho/Delantero Suela":
                    pad.setIdTipoRecorte(2);
                    break;
                case "Centro/Delantero Suela":
                    pad.setIdTipoRecorte(3);
                    break;
                case "Lados":
                    pad.setIdTipoRecorte(4);
                    break;
                case "Centro Castaño":
                    pad.setIdTipoRecorte(5);
                    break;
                case "Centro Quebracho":
                    pad.setIdTipoRecorte(6);
                    break;
                case "Delantero Suela":
                    pad.setIdTipoRecorte(7);
                    break;
                case "Centro":
                    pad.setIdTipoRecorte(8);
                    break;
                default:
                    break;
            }
            
            PartidaDetalleCommands pdc = new PartidaDetalleCommands();
            
            int banderaRecortar = pdc.validarCrearRecorte(pad);
            
            if (banderaRecortar == 1)
            {
                dlgRecortar.setSize(300, 280);
                dlgRecortar.setPreferredSize(dlgRecortar.getSize());
                dlgRecortar.setLocationRelativeTo(null);
                dlgRecortar.setAlwaysOnTop(true);
                dlgRecortar.setVisible(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No se puede recortar. \nYa se tienen recortes del mismos tipo para esta partida y camión","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            dlgRecortar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgRecortar.setVisible(true);
        }
    }
    
    //Método que abre el dialogo para seleccionar el acabado del tipo recorte centro
    public void abrirDialogoSeleccionarAcabado()
    {   
        dlgSelAcabado.setVisible(false);
        
        String[] tipoRecorte = null;
        
        tipoRecorte = new String[] { "Centro Castaño", "Centro Quebracho" };
        
        //Validar que se haya seleccionado un tipo de recorte
        if (tipoRecorte != null)
        {
            recorteSeleccionado = (String) JOptionPane.showInputDialog(null, "Seleccione acabado","Acabar",JOptionPane.INFORMATION_MESSAGE, null, tipoRecorte, tipoRecorte[0]);
        }
        
        if (recorteSeleccionado == null)
        {
            return;
        }
        
        try 
        {
            lblTipoCueroAcabar.setText(pad.getTipoRecorte());
            txtNoPiezasSelAcabado.setText(String.valueOf(pad.getNoPiezasAct()));
            txtNoPiezasSelAcabado1.setText(String.valueOf(pad.getNoPiezasAct()));
            
            lblTipoCueroAcabar1.setText(recorteSeleccionado);
            
            switch (recorteSeleccionado) 
            {
                case "Centro Castaño":
                    pad.setIdTipoRecorte(5);
                    break;
                case "Centro Quebracho":
                    pad.setIdTipoRecorte(6);
                    break;
                default:
                    break;
            }
            
            PartidaDetalleCommands pdc = new PartidaDetalleCommands();
            
            int banderaRecortar = pdc.validarCrearRecorte(pad);
            
            if (banderaRecortar == 1)
            {
                dlgSelAcabado.setSize(300, 280);
                dlgSelAcabado.setPreferredSize(dlgRecortar.getSize());
                dlgSelAcabado.setLocationRelativeTo(null);
                dlgSelAcabado.setAlwaysOnTop(true);
                dlgSelAcabado.setVisible(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No se puede recortar. \nYa se tienen recortes del mismos tipo para esta partida y camión","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            dlgSelAcabado.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog", "Error", JOptionPane.ERROR_MESSAGE);
            dlgSelAcabado.setVisible(true);
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
    
    private void actualizarTablaInsumos()
    {
        subP = new SubProceso();
        String idAux = "";
        String descripcion = tblSubproceso.getValueAt(tblSubproceso.getSelectedRow(), 0).toString();
        if (subProceso[tblSubproceso.getSelectedRow()][0].equals(descripcion))
        {
            idAux = subProceso[tblSubproceso.getSelectedRow()][1];
            idSubproceso = Integer.parseInt(idAux);
        }

        try {
            lstInsumos = new ArrayList<>();
            lstInsumos = subPc.obtInsXSubProcList(idSubproceso);
            for (int i = 0; i < lstInsumos.size(); i++)
            {
                lstInsumos.get(i).setPrecioUnitario(subPc.obtPrecioProducto(lstInsumos.get(i).getIdProducto()));
            }
            
            String[] cols = new String[]
            {
                "Clave", "%", "Material", "Temp", "Rodar", "Cantidad", "Observaciones", "P/U", "Total"
            };

            dtmInsumos = new DefaultTableModel()
            {
                public boolean isCellEditable (int row, int column)
                {
                    // Aquí devolvemos true o false según queramos que una celda
                    // identificada por fila,columna (row,column), sea o no editable
                    if (column == 3 || column == 4 || column == 6 || column == 7)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            };
            dtmInsumos.setColumnIdentifiers(cols);
            dtmInsumos.setRowCount(lstInsumos.size());
            for (int i = 0; i < lstInsumos.size(); i++)
            {
                if (lstInsumos.get(i).getIdProducto() == 0)
                {
                    dtmInsumos.setValueAt("", i, 0);
                    dtmInsumos.setValueAt("", i, 1);
                    dtmInsumos.setValueAt("", i, 7);
                }
                else
                {
                    dtmInsumos.setValueAt(lstInsumos.get(i).getClave(), i, 0);
                    dtmInsumos.setValueAt(lstInsumos.get(i).getPorcentaje(), i, 1);
                    dtmInsumos.setValueAt(lstInsumos.get(i).getPrecioUnitario(), i, 7);
                }
                dtmInsumos.setValueAt(lstInsumos.get(i).getMaterial(), i, 2);
                dtmInsumos.setValueAt("", i, 3);
                dtmInsumos.setValueAt("", i, 4);
                dtmInsumos.setValueAt("", i, 6);
                
            }
            tblInsXproc.getTableHeader().setReorderingAllowed(false);
            tblInsXproc.setModel(dtmInsumos);
            actualizarTotalInsumos();
            lblSubProceso.setText(subProceso[tblSubproceso.getSelectedRow()][0]);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    private void actualizarTotalInsumos()
    {
        try 
        {
            //Validar que se hayan agregado elementos a la tabla tblPartidasAgregadas
            if (tblPartidasAgregadas.getRowCount() > 0)
            {
                //Se suma el total de kg de los elementos en la tabla tblPartidasAgregadas
                Double totalKg = 0.0;
                for (int i = 0; i < tblPartidasAgregadas.getRowCount(); i++)
                {
                    totalKg += Double.parseDouble(String.valueOf(tblPartidasAgregadas.getValueAt(i, 3)));
                }
                //Se calcula la cantidad y total de cada elemento de la tabla tblInsXproc
                for (int i = 0; i < lstInsumos.size(); i++)
                {
                    if (lstInsumos.get(i).getIdProducto() != 0)
                    {
                        String precioUnitario = (String.format("%.2f",Double.parseDouble(String.valueOf(tblInsXproc.getValueAt(i, 7)))));
                        lstInsumos.get(i).setPrecioUnitario(Double.parseDouble(precioUnitario));

                        String cantidad = (String.format("%.2f",Double.parseDouble(String.valueOf((lstInsumos.get(i).getPorcentaje()*totalKg)/100))));
                        lstInsumos.get(i).setCantidad(Double.parseDouble(cantidad));

                        String total = (String.format("%.2f",Double.parseDouble(String.valueOf(lstInsumos.get(i).getCantidad()*lstInsumos.get(i).getPrecioUnitario()))));
                        lstInsumos.get(i).setTotal(Double.parseDouble(total));

                        dtmInsumos.setValueAt(lstInsumos.get(i).getCantidad(), i, 5);
                        dtmInsumos.setValueAt(lstInsumos.get(i).getTotal(), i, 8);
                    }
                    else
                    {
                        lstInsumos.get(i).setPrecioUnitario(0.0);
                        lstInsumos.get(i).setCantidad(0.0);
                        lstInsumos.get(i).setTotal(0.0);
                        
                        dtmInsumos.setValueAt("", i, 5);
                        dtmInsumos.setValueAt("", i, 8);
                    }
                }
            }
            else
            {
                /*En caso de no tener elementos en la tabla tblPartidasAgregadas,
                se pone en 0 la cantidad y total de cada elemento de la tabla tblInsXproc */ 
                for (int i = 0; i < lstInsumos.size(); i++)
                {
                    lstInsumos.get(i).setCantidad(0.0);
                    lstInsumos.get(i).setTotal(0.0);
                    
                    if (lstInsumos.get(i).getIdProducto() == 0)
                    {
                        dtmInsumos.setValueAt("", i, 5);
                        dtmInsumos.setValueAt("", i, 8);
                    }
                    else
                    {
                        dtmInsumos.setValueAt(lstInsumos.get(i).getCantidad(), i, 5);
                        dtmInsumos.setValueAt(lstInsumos.get(i).getTotal(), i, 8);
                    }
                }
            }
            //obtener costo total de insumos
            Double total = 0.0;
            for (int i = 0; i < lstInsumos.size(); i++)
            {
                total += lstInsumos.get(i).getTotal();
            }
            String costoTotal = (String.format("%.2f",total));
            txtTotal.setText(costoTotal);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al calcular costos de ficha de producción","Error",JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void validarPiezasPartidaAgregar()
    {
        for (int fila = 0; fila < tblPartidasAgregadas.getRowCount(); fila++)
        {
            try
            {
                int piezasUtilizar = Integer.parseInt(tblPartidasAgregadas.getValueAt(fila, 2).toString());
                Double kgUtilizar = Double.parseDouble(tblPartidasAgregadas.getValueAt(fila, 3).toString());

                if (piezasUtilizar < 0)
                {
                    tblPartidasAgregadas.setValueAt("0", fila, 2);
                }
                else
                {
                    //obtener número de piezas originales de la tabla tblPartidasDisponibles
                    int piezasOriginal = 0;
                    for (int i = 0; i < lstPartidas.size(); i++)
                    {
                        if (lstPartidas.get(i).getIdPartidaDet() == Integer.parseInt(tblPartidasAgregadas.getValueAt(fila, 4).toString()))
                        {
                            piezasOriginal = lstPartidas.get(i).getNoPiezasAct();
                        }
                    }

                    if (piezasUtilizar > piezasOriginal)
                    {
                        JOptionPane.showMessageDialog(null, "El número de piezas ingresado no puede ser mayor al número de piezas original");
                        tblPartidasAgregadas.setValueAt("0", fila, 2);
                    }
                    actualizarTotalInsumos();
                }
            } catch (Exception e)
            {
                JOptionPane.showMessageDialog(null, "Ingrese números validos en los campos \nNo. Piezas y Peso (Kg)","Error",JOptionPane.ERROR_MESSAGE);
                tblPartidasAgregadas.setValueAt("0", fila, 2);
                tblPartidasAgregadas.setValueAt("0", fila, 3);
            }
        }
    }
    
    private void validarPrecioInsumos(int accion)
    {
        int idProducto = 0;
        Double precio = 0.0;
        
        if (accion == 1)
        {
            try
            {
                idProducto = lstInsumos.get(tblInsXproc.getSelectedRow()).getIdProducto();
                precio = Double.parseDouble(tblInsXproc.getValueAt(tblInsXproc.getSelectedRow(), 7).toString());
                
                for (int fila = 0; fila < tblInsXproc.getRowCount(); fila++)
                {
                    if (lstInsumos.get(fila).getIdProducto() == idProducto)
                    {
                        tblInsXproc.setValueAt(precio, fila, 7);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ingrese un precio válido", "Advertencia", JOptionPane.ERROR_MESSAGE);
                tblInsXproc.setValueAt("0.0", tblInsXproc.getSelectedRow(), 7);
                return;
            }
        }
        
        for (int fila = 0; fila < tblInsXproc.getRowCount(); fila++)
        {
            try 
            {
                if (lstInsumos.get(fila).getIdProducto() != 0)
                {
                    Double pu = Double.parseDouble(tblInsXproc.getValueAt(fila, 7).toString());
                    lstInsumos.get(fila).setPrecioUnitario(pu);
                }
                else
                {
                    tblInsXproc.setValueAt("", fila, 7);
                }
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ingrese un precio válido", "Advertencia", JOptionPane.ERROR_MESSAGE);
                tblInsXproc.setValueAt("0.0", fila, 7);
                lstInsumos.get(fila).setPrecioUnitario(0.0);
            }
        }
        actualizarTotalInsumos();
    }
    
    public void eliminarPartida()
    {
        try
        {
            int fila = tblPartidasDisponibles.getSelectedRow();
            
            if (fila != -1)
            {
                if (JOptionPane.showConfirmDialog(null, "¿Realmente desea eliminar la partida seleccionada?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
                {
                    pd = new PartidaDetalle();
                    pdc = new PartidaDetalleCommands();

                    pd.setIdPartidaDet(lstPartidas.get(fila).getIdPartidaDet());
                    pd.setIdPartida(lstPartidas.get(fila).getIdPartida());
                    pd.setIdRecortePartidaDet(lstPartidas.get(fila).getIdRecortePartidaDet());
                    pd.setIdInventarioCrudo(lstPartidas.get(fila).getIdInventarioCrudo());
                    
                    if (pd.getIdRecortePartidaDet() == 0)
                    {
                        for (int i = 0; i < lstPartidas.size(); i++)
                        {
                            if(lstPartidas.get(i).getIdRecortePartidaDet() == pd.getIdPartidaDet())
                            {
                                JOptionPane.showMessageDialog(null, "No se puede eliminar partida \nEsta partida ya tiene recortes de cuero","Advertencia",JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                    }
                    
                    else
                    {
                        for (int i = 0; i < lstPartidas.size(); i++)
                        {
                            if(lstPartidas.get(i).getIdPartida() == pd.getIdPartida() && lstPartidas.get(i).getIdTipoRecorte() != 1)
                            {
                                JOptionPane.showMessageDialog(null, "No se puede eliminar partida \nEsta partida ya tiene recortes de cuero","Advertencia",JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                    }                    

                    int eliminar = pdc.obtenerPartidaDetalleEliminar(pd);

                    if (eliminar == 1)
                    {
                        pdc.eliminarPartidaDet(pd);
                        JOptionPane.showMessageDialog(null, "Detalle de partida eliminada");
                        
                        PnlFichaProduccion pnlFichaProduccion = new PnlFichaProduccion();
                        pnlPrincipalx.removeAll();
                        pnlPrincipalx.add(pnlFichaProduccion, BorderLayout.CENTER);
                        pnlPrincipalx.paintAll(pnlFichaProduccion.getGraphics());
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "La partida que desea eliminar ya esta siendo utilizada en otros procesos","Advertencia",JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Partidas","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        }
        catch (Exception e)
        {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void eliminarRecorte()
    {
        try
        {
            int fila = tblPartidasDisponibles.getSelectedRow();
            
            if (fila != -1)
            {
                if (JOptionPane.showConfirmDialog(null, "¿Desea eliminar el recorte seleccionado?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
                {
                    pd = new PartidaDetalle();
                    pdc = new PartidaDetalleCommands();

                    pd.setIdPartidaDet(lstPartidas.get(fila).getIdPartidaDet());
                    pd.setNoPiezas(lstPartidas.get(fila).getNoPiezasAct());
                    pd.setIdPartida(lstPartidas.get(fila).getIdPartida());
                    pd.setIdRecepcionCuero(lstPartidas.get(fila).getIdRecepcionCuero());
                    pd.setIdTipoRecorte(lstPartidas.get(fila).getIdTipoRecorte());
                    pd.setIdRecortePartidaDet(lstPartidas.get(fila).getIdRecortePartidaDet());
                    
                    if (pd.getIdTipoRecorte() == 1)
                    {
                        JOptionPane.showMessageDialog(null, "No se puede eliminar este tipo de recorte","Advertencia",JOptionPane.WARNING_MESSAGE);
                    }
                    else
                    {
                        int eliminar = pdc.validarBorrarRecorte(pd);

                        if (eliminar == 1)
                        {
                            pdc.eliminarRecorte(pd);
                            JOptionPane.showMessageDialog(null, "Recorte eliminado correctamente");

                            PnlFichaProduccion pnlFichaProduccion = new PnlFichaProduccion();
                            pnlPrincipalx.removeAll();
                            pnlPrincipalx.add(pnlFichaProduccion, BorderLayout.CENTER);
                            pnlPrincipalx.paintAll(pnlFichaProduccion.getGraphics());
                        }
                        else if (eliminar == 1)
                        {
                            if (pd.getIdTipoRecorte() == 2 || pd.getIdTipoRecorte() == 3)
                            {
                                JOptionPane.showMessageDialog(null, "El recorte Delantero Sillero o Crupon Sillero ya esta siendo utilizado en otros procesos","Advertencia",JOptionPane.WARNING_MESSAGE);
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "El recorte que desea eliminar ya esta siendo utilizado en otros procesos","Advertencia",JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar recorte creado desde la creación de la partida","Advertencia",JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Partidas","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        }
        catch (Exception e)
        {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    //Metodo para inicializar los campos de dlgEliPzaFichaProd
    public void inicializarCamposEliPzaFichaProd() throws Exception
    {
        txtNoPiezasEliminar.setText("");
        txtrMotivo.setText("");
        
        int fila = tblPartidasDisponibles.getSelectedRow();
        
        txtNoPartida.setText(String.valueOf(tblPartidasDisponibles.getValueAt(fila, 0)));
        txtProveedorNoCamion.setText(String.valueOf(tblPartidasDisponibles.getValueAt(fila, 1)));
        txtTipoRecorte.setText(String.valueOf(tblPartidasDisponibles.getValueAt(fila, 2)));
        txtNoPiezasActuales.setText(String.valueOf(tblPartidasDisponibles.getValueAt(fila, 3)));
    }
    
    //Método que abre el dialogo para eliminar piezas del inventario en proceso 
    public void abrirDialogoEliPzaFichaProd() throws Exception
    {
        inicializarCamposEliPzaFichaProd();
        
        dlgEliPzaFichaProd.setSize(460, 490);
        dlgEliPzaFichaProd.setPreferredSize(dlgEliPzaFichaProd.getSize());
        dlgEliPzaFichaProd.setLocationRelativeTo(null);
        dlgEliPzaFichaProd.setAlwaysOnTop(true);
        dlgEliPzaFichaProd.setVisible(true);
    }
    
    //Método para eliminar piezas del inventario en proceso
    public void eliminarPiezasFichaProd () throws Exception
    {
        if ( !txtNoPiezasEliminar.getText().isEmpty() && Integer.parseInt(txtNoPiezasEliminar.getText()) != 0)
        {
            try 
            {
                if (Integer.parseInt(txtNoPiezasEliminar.getText()) > Integer.parseInt(txtNoPiezasActuales.getText()))
                {
                    JOptionPane.showMessageDialog(dlgEliPzaFichaProd, "El numero de piezas debe ser menor o igual al número de piezas actuales");
                }
                else
                {
                    int fila = tblPartidasDisponibles.getSelectedRow();
                    bpd = new BajasPartidaDet();
                    bpdc = new BajasPartidaDetCommands();
                    
//                    double promKg = Double.parseDouble(tblPartidasDisponibles.getValueAt(fila, 6).toString());
//                    double kg = promKg * Integer.parseInt(txtNoPiezasEliminar.getText());
//                    
//                    if (kg > Double.parseDouble(tblPartidasDisponibles.getValueAt(fila, 5).toString()))
//                    {
//                        kg = Double.parseDouble(tblPartidasDisponibles.getValueAt(fila, 5).toString());
//                    }

                    bpd.setIdPartidaDet(lstPartidas.get(fila).getIdPartidaDet());
                    bpd.setNoPiezas(Integer.parseInt(txtNoPiezasEliminar.getText()));
                    bpd.setMotivo(txtrMotivo.getText());
//                    bic.setKgTotal(kg);

                    bpdc.agregarBajaPartidaDet(bpd);
                    pdc.actualizarNoPiezasBaja(bpd);
                    actualizarTablaPartidasDisponibles();
                    dlgEliPzaFichaProd.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Baja realizada correctamente");
                }
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                dlgEliPzaFichaProd.setVisible(false);                
                JOptionPane.showMessageDialog(null, "Error de conexión", "Error",JOptionPane.ERROR_MESSAGE);
            }   
        }
        else
        {
            dlgEliPzaFichaProd.setVisible(false);
            JOptionPane.showMessageDialog(null, "Capture no. Piezas a eliminar","Mensaje",JOptionPane.WARNING_MESSAGE);
            dlgEliPzaFichaProd.setVisible(true);
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
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtNoPiezasRecortar = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        lblTipoCueroRecortar = new javax.swing.JLabel();
        txtNoPiezasRecortar1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblTipoCuero1 = new javax.swing.JLabel();
        txtNoPiezasRecortar2 = new javax.swing.JTextField();
        lblPiezasDe2 = new javax.swing.JLabel();
        lblyRecortar = new javax.swing.JLabel();
        lblTipoCuero2 = new javax.swing.JLabel();
        dlgSelAcabado = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtNoPiezasSelAcabado = new javax.swing.JTextField();
        btnGuardar1 = new javax.swing.JButton();
        lblTipoCueroAcabar = new javax.swing.JLabel();
        txtNoPiezasSelAcabado1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblTipoCueroAcabar1 = new javax.swing.JLabel();
        dlgEliPzaFichaProd = new javax.swing.JDialog();
        jPanel15 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        txtNoPiezasActuales = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txtNoPiezasEliminar = new javax.swing.JTextField();
        btnRealizarEntradaEnvSemi = new javax.swing.JButton();
        btnCancelarAgregarEnvSemi = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtProveedorNoCamion = new javax.swing.JTextField();
        txtNoPartida = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtTipoRecorte = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtrMotivo = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSubproceso = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnRecortar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPartidasDisponibles = new javax.swing.JTable();
        btnAsignar = new javax.swing.JButton();
        btnEliminarPartida = new javax.swing.JButton();
        btnEliminarRecorte = new javax.swing.JButton();
        btnSelAcabado = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPartidasAgregadas = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblSubProceso = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblInsXproc = new javax.swing.JTable();
        txtTotal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnGenerarFicha = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbProceso = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbTambores = new javax.swing.JComboBox<>();

        dlgRecortar.setResizable(false);

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cut_red.png"))); // NOI18N
        jLabel12.setText("Recortar Cuero");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(133, 133, 133))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Piezas de");

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

        btnGuardar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        lblTipoCueroRecortar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTipoCueroRecortar.setText("TipoCuero");

        txtNoPiezasRecortar1.setEditable(false);
        txtNoPiezasRecortar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasRecortar1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("=");
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtNoPiezasRecortar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTipoCueroRecortar))
                            .addComponent(lblyRecortar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtNoPiezasRecortar2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblPiezasDe2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTipoCuero2))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                                    .addComponent(txtNoPiezasRecortar1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTipoCuero1)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGuardar)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasRecortar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(lblTipoCueroRecortar))
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasRecortar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(lblTipoCuero1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblyRecortar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasRecortar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPiezasDe2)
                    .addComponent(lblTipoCuero2))
                .addGap(18, 18, 18)
                .addComponent(btnGuardar)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout dlgRecortarLayout = new javax.swing.GroupLayout(dlgRecortar.getContentPane());
        dlgRecortar.getContentPane().setLayout(dlgRecortarLayout);
        dlgRecortarLayout.setHorizontalGroup(
            dlgRecortarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgRecortarLayout.setVerticalGroup(
            dlgRecortarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgRecortarLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
        );

        dlgSelAcabado.setResizable(false);

        jPanel4.setBackground(new java.awt.Color(0, 204, 204));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Seleccionar Curtido");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(108, 108, 108))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Piezas de");

        txtNoPiezasSelAcabado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasSelAcabado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNoPiezasSelAcabado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoPiezasSelAcabadoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasSelAcabadoKeyTyped(evt);
            }
        });

        btnGuardar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardar1.setText("Guardar");
        btnGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardar1ActionPerformed(evt);
            }
        });

        lblTipoCueroAcabar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTipoCueroAcabar.setText("TipoCuero");

        txtNoPiezasSelAcabado1.setEditable(false);
        txtNoPiezasSelAcabado1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasSelAcabado1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("=");
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("Piezas de");

        lblTipoCueroAcabar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTipoCueroAcabar1.setText("TipoCuero");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(txtNoPiezasSelAcabado, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTipoCueroAcabar))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNoPiezasSelAcabado1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTipoCueroAcabar1)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGuardar1)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasSelAcabado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(lblTipoCueroAcabar))
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasSelAcabado1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(lblTipoCueroAcabar1))
                .addGap(60, 60, 60)
                .addComponent(btnGuardar1)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout dlgSelAcabadoLayout = new javax.swing.GroupLayout(dlgSelAcabado.getContentPane());
        dlgSelAcabado.getContentPane().setLayout(dlgSelAcabadoLayout);
        dlgSelAcabadoLayout.setHorizontalGroup(
            dlgSelAcabadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgSelAcabadoLayout.setVerticalGroup(
            dlgSelAcabadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgSelAcabadoLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel55.setText("Proveedor y No. Camión:");

        txtNoPiezasActuales.setEditable(false);
        txtNoPiezasActuales.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasActuales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasActualesKeyTyped(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel32.setText("No. Partida:");

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

        jPanel16.setBackground(new java.awt.Color(0, 204, 51));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_abajo16x16.png"))); // NOI18N
        jLabel20.setText("Eliminar Piezas");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel21.setText("No. piezas actuales:");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setText("No. piezas a eliminar:");

        txtProveedorNoCamion.setEditable(false);
        txtProveedorNoCamion.setBackground(new java.awt.Color(255, 255, 255));

        txtNoPartida.setEditable(false);
        txtNoPartida.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setText("Tipo de recorte:");

        txtTipoRecorte.setEditable(false);
        txtTipoRecorte.setBackground(new java.awt.Color(255, 255, 255));

        txtrMotivo.setColumns(20);
        txtrMotivo.setRows(5);
        jScrollPane5.setViewportView(txtrMotivo);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel15Layout.createSequentialGroup()
                                    .addGap(32, 32, 32)
                                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel32)
                                        .addComponent(jLabel55)
                                        .addComponent(jLabel24)
                                        .addComponent(jLabel21)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                                    .addGap(86, 86, 86)
                                    .addComponent(jLabel53)))
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNoPiezasActuales, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNoPartida, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                .addComponent(txtProveedorNoCamion)
                                .addComponent(txtTipoRecorte))
                            .addComponent(txtNoPiezasEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRealizarEntradaEnvSemi, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelarAgregarEnvSemi)))
                .addGap(6, 6, 6))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtNoPartida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txtProveedorNoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTipoRecorte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoPiezasActuales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNoPiezasEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntradaEnvSemi)
                    .addComponent(btnCancelarAgregarEnvSemi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlgEliPzaFichaProdLayout = new javax.swing.GroupLayout(dlgEliPzaFichaProd.getContentPane());
        dlgEliPzaFichaProd.getContentPane().setLayout(dlgEliPzaFichaProdLayout);
        dlgEliPzaFichaProdLayout.setHorizontalGroup(
            dlgEliPzaFichaProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgEliPzaFichaProdLayout.setVerticalGroup(
            dlgEliPzaFichaProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setPreferredSize(new java.awt.Dimension(1000, 450));

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setPreferredSize(new java.awt.Dimension(98, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cog.png"))); // NOI18N
        jLabel1.setText("Procesos");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        tblSubproceso.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSubproceso.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSubproceso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSubprocesoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSubproceso);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setPreferredSize(new java.awt.Dimension(98, 30));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cueroProceso.png"))); // NOI18N
        jLabel2.setText("Partidas Disponibles");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        btnRecortar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnRecortar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cut_red.png"))); // NOI18N
        btnRecortar.setText("Recortar");
        btnRecortar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecortarActionPerformed(evt);
            }
        });

        tblPartidasDisponibles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "No. Partida", "Prov - Camión", "Recorte", "No. Piezas"
            }
        ));
        tblPartidasDisponibles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPartidasDisponibles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPartidasDisponiblesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblPartidasDisponibles);

        btnAsignar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAsignar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/arrow_turn_right.png"))); // NOI18N
        btnAsignar.setText("Asignar");
        btnAsignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarActionPerformed(evt);
            }
        });

        btnEliminarPartida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEliminarPartida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        btnEliminarPartida.setText("Eliminar Partida");
        btnEliminarPartida.setEnabled(false);
        btnEliminarPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPartidaActionPerformed(evt);
            }
        });

        btnEliminarRecorte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEliminarRecorte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        btnEliminarRecorte.setText("Eliminar Recorte");
        btnEliminarRecorte.setEnabled(false);
        btnEliminarRecorte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarRecorteActionPerformed(evt);
            }
        });

        btnSelAcabado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSelAcabado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/color_picker.png"))); // NOI18N
        btnSelAcabado.setText("Seleccionar Curtido");
        btnSelAcabado.setEnabled(false);
        btnSelAcabado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelAcabadoActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        jButton1.setText("Eliminar Piezas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(btnRecortar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAsignar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarRecorte))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(btnEliminarPartida)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelAcabado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRecortar)
                    .addComponent(btnAsignar)
                    .addComponent(btnEliminarRecorte))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarPartida)
                    .addComponent(btnSelAcabado)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel11.setPreferredSize(new java.awt.Dimension(98, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cueroProceso.png"))); // NOI18N
        jLabel3.setText("Partidas Agregadas");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        tblPartidasAgregadas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. Partida", "Recorte", "No. Piezas", "Peso (Kg)"
            }
        ));
        tblPartidasAgregadas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPartidasAgregadas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPartidasAgregadasMousePressed(evt);
            }
        });
        tblPartidasAgregadas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPartidasAgregadasKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblPartidasAgregadas);

        btnEliminar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEliminar)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel13.setPreferredSize(new java.awt.Dimension(98, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/flask.png"))); // NOI18N
        jLabel4.setText("Insumos por Proceso:");

        lblSubProceso.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSubProceso.setText("Insumo");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubProceso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addComponent(lblSubProceso))
        );

        tblInsXproc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave", "%", "Material", "Temp", "Rodar", "Cantidad", "Observaciones", "P/U", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInsXproc.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblInsXproc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInsXprocMousePressed(evt);
            }
        });
        tblInsXproc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblInsXprocKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tblInsXproc);

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("TOTAL $");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
            .addComponent(jScrollPane4)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnGenerarFicha.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGenerarFicha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/document_next.png"))); // NOI18N
        btnGenerarFicha.setText("Generar Ficha");
        btnGenerarFicha.setEnabled(false);
        btnGenerarFicha.setFocusable(false);
        btnGenerarFicha.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnGenerarFicha.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGenerarFicha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarFichaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGenerarFicha);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("   ");
        jToolBar1.add(jLabel8);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Proceso");
        jToolBar1.add(jLabel5);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText(" ");
        jToolBar1.add(jLabel10);

        cmbProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProcesoActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbProceso);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("   ");
        jToolBar1.add(jLabel9);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Tambor");
        jToolBar1.add(jLabel6);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText(" ");
        jToolBar1.add(jLabel11);

        cmbTambores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTamboresActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbTambores);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProcesoActionPerformed
        actualizarTablaSubProc();
        actualizarTablaPartidasDisponibles();
        inicializarTablaPartidasAgregadas();
        actualizarTablaInsumos();
    }//GEN-LAST:event_cmbProcesoActionPerformed

    private void tblSubprocesoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubprocesoMouseClicked
        actualizarTablaInsumos();
    }//GEN-LAST:event_tblSubprocesoMouseClicked

    private void btnRecortarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecortarActionPerformed
        try 
        {
            pad = new PartidaDisp();
            int i = tblPartidasDisponibles.getSelectedRow();
            
            pad.setNoPartida(lstPartidas.get(i).getNoPartida());
            pad.setTipoRecorte(lstPartidas.get(i).getTipoRecorte());
            pad.setNoPiezasAct(lstPartidas.get(i).getNoPiezasAct());
            pad.setIdPartidaDet(lstPartidas.get(i).getIdPartidaDet());
            pad.setIdPartida(lstPartidas.get(i).getIdPartida());
            pad.setIdTipoRecorte(lstPartidas.get(i).getIdTipoRecorte());
            pad.setIdProceso(Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0]));
            
            abrirDialogoRecortar();
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla \nde partidas disponibles","Mensaje",JOptionPane.WARNING_MESSAGE);
            return;
        }
    }//GEN-LAST:event_btnRecortarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        int noPiezasRecortar = 0;
        int noPiezas = 0;
        try 
        {
            noPiezasRecortar = Integer.parseInt(txtNoPiezasRecortar.getText());
        } 
        catch (Exception e) 
        {
            
        }
        
        if (noPiezasRecortar > 0 && noPiezasRecortar <= pad.getNoPiezasAct())
        {
            try 
            {
                noPiezas = Integer.parseInt(txtNoPiezasRecortar1.getText());
                
                pdc = new PartidaDetalleCommands();
                pdc.agregarRecorte(pad, noPiezasRecortar, noPiezas, Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0])-1);
                dlgRecortar.setVisible(false);
                JOptionPane.showMessageDialog(null,"Recorte relizado correctamente");
                actualizarTablaPartidasDisponibles();
                inicializarTablaPartidasAgregadas();
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
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void cmbTamboresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTamboresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTamboresActionPerformed

    private void txtNoPiezasRecortarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasRecortarKeyTyped
        validarNumerosEnteros(evt);
    }//GEN-LAST:event_txtNoPiezasRecortarKeyTyped

    private void txtNoPiezasRecortarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasRecortarKeyReleased
        if (recorteSeleccionado.equals("Delantero/Crupon") || recorteSeleccionado.equals("Centro Castaño/Delantero Suela") || recorteSeleccionado.equals("Centro Quebracho/Delantero Suela"))
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

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarActionPerformed
        try
        {
            int fila = tblPartidasDisponibles.getSelectedRow();
        
            if (asignados[fila][0].equals("1"))
            {
                JOptionPane.showMessageDialog(null, "Esta partida ya se encuentra asignada");
            }
            else
            {
                String datosPartidas[];
                datosPartidas = new String[5];

                datosPartidas[0]= String.valueOf(lstPartidas.get(fila).getNoPartida());
                datosPartidas[1]= lstPartidas.get(fila).getTipoRecorte();
                datosPartidas[2]= String.valueOf(lstPartidas.get(fila).getNoPiezasAct());
                datosPartidas[3]= "0.0";
                datosPartidas[4]= String.valueOf(lstPartidas.get(fila).getIdPartidaDet());

                dtms.addRow(datosPartidas);
                asignados[fila][0] = "1";
                actualizarTotalInsumos();
                btnGenerarFicha.setEnabled(true);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Inventario de Partidas Disponibles","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnAsignarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila;
        try
        {
            fila = this.tblPartidasAgregadas.getSelectedRow();
            
            for (int i = 0; i < lstPartidas.size(); i++)
            {
                if (String.valueOf(lstPartidas.get(i).getIdPartidaDet()).equals(tblPartidasAgregadas.getValueAt(fila, 4)))
                {
                    asignados[i][0] = "0";
                }
            }
            
            dtms.removeRow(fila);
            actualizarTotalInsumos();
            if (tblPartidasAgregadas.getRowCount() == 0)
            {
                btnGenerarFicha.setEnabled(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Partidas Agregadas","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tblPartidasAgregadasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPartidasAgregadasKeyReleased
        validarPiezasPartidaAgregar();
    }//GEN-LAST:event_tblPartidasAgregadasKeyReleased

    private void tblPartidasAgregadasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartidasAgregadasMousePressed
        if(evt.getClickCount()==1)
        {
            validarPiezasPartidaAgregar();
        }
    }//GEN-LAST:event_tblPartidasAgregadasMousePressed

    private void tblInsXprocKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblInsXprocKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            validarPrecioInsumos(1);
        }
        else
        {
            validarPrecioInsumos(0);
        }
    }//GEN-LAST:event_tblInsXprocKeyReleased

    private void tblInsXprocMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInsXprocMousePressed
        validarPrecioInsumos(0);
    }//GEN-LAST:event_tblInsXprocMousePressed

    private void btnGenerarFichaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarFichaActionPerformed
        try 
        {
            if (tblInsXproc.getRowCount() > 0)
            {
                for (int i = 0; i < tblPartidasAgregadas.getRowCount(); i++) 
                {
                    Double noPiezas = Double.parseDouble(tblPartidasAgregadas.getValueAt(i, 2).toString());
                    Double peso = Double.parseDouble(tblPartidasAgregadas.getValueAt(i, 3).toString());
                    String validaCentro = String.valueOf(tblPartidasAgregadas.getValueAt(i, 1).toString());
                    if (noPiezas <= 0 || peso <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Todos los campos No. Piezas y Peso (Kg), \nde la tabla Partidas agregadas deben ser mayor a 0","Advertencia",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    pad = new PartidaDisp();
                    pad.setIdProceso(Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0]));
                    
                    if (validaCentro.equals("Centro") && pad.getIdProceso() == 4)
                    {
                        JOptionPane.showMessageDialog(null, "Debe darle acabado a las piezas de Centro \nde las que se creará la ficha de producción","Advertencia",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                
                FichaProd fp = new FichaProd();
                FichaProdCommands fpc = new FichaProdCommands();

                fp.setIdTambor(lstTambor.get(cmbTambores.getSelectedIndex()).getIdTambor());
                fp.setNoPiezasTotal(0);
                fp.setKgTotal(0.0);
                for (int i = 0; i < tblPartidasAgregadas.getRowCount(); i++)
                {
                    fp.setNoPiezasTotal(fp.getNoPiezasTotal() + Integer.parseInt(tblPartidasAgregadas.getValueAt(i, 2).toString()));
                    fp.setKgTotal(fp.getKgTotal() + Double.parseDouble(tblPartidasAgregadas.getValueAt(i, 3).toString()));
                }
                fp.setCostoInsumos(Double.parseDouble(txtTotal.getText()));

                fpc.agregarFichaProd(fp);

                PartidaDetalle pd = new PartidaDetalle();
                PartidaDetalleCommands pdc = new PartidaDetalleCommands();

                FichaProdDet fpd = new FichaProdDet();
                FichaProdDetCommands fpdc = new FichaProdDetCommands();

                fpd.setIdFichaProd(fpc.obtenerUltFichaProduccion());
                Double costoInsumosFicha = Double.parseDouble(txtTotal.getText());
                for (int i = 0; i < tblPartidasAgregadas.getRowCount(); i++)
                {
                    int noPiezasPartida = Integer.parseInt(tblPartidasAgregadas.getValueAt(i, 2).toString());

                    pd.setNoPiezas(noPiezasPartida);
                    for (int j = 0; j < lstPartidas.size(); j++)
                    {
                        if (Integer.parseInt(tblPartidasAgregadas.getValueAt(i, 4).toString()) == lstPartidas.get(j).getIdPartidaDet())
                        {
                            pd.setIdPartida(lstPartidas.get(j).getIdPartida());
                            pd.setIdTipoRecorte(lstPartidas.get(j).getIdTipoRecorte());
                        }
                    }
                    pd.setIdPartidaDet(Integer.parseInt(tblPartidasAgregadas.getValueAt(i, 4).toString()));

                    pdc.insPartidaDetFicha(pd);


                    fpd.setIdPartidaDet(pdc.obtenerUltPartidaDet());
                    fpd.setNoPiezasTotal(fp.getNoPiezasTotal());
                    Double kgPartida = Double.parseDouble(tblPartidasAgregadas.getValueAt(i, 3).toString());
                    fpd.setKgTotal(fp.getKgTotal());

                    fpdc.agregarFichaProdDet(fpd, noPiezasPartida, kgPartida, costoInsumosFicha);
                }

                InsumosFichaProd ifp = new InsumosFichaProd();
                InsumosFichaProdCommands ifpc = new InsumosFichaProdCommands();

                ifp.setIdFichaProd(fpd.getIdFichaProd());
                ifp.setIdProceso(Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0]));
                ifp.setIdSubproceso(Integer.parseInt(subProceso[tblSubproceso.getSelectedRow()][1]));
                ifp.setIdFormXSubProc(lstInsumos.get(0).getIdFormXSubProc());
                ifp.setTotalInsumos(costoInsumosFicha);

                ifpc.agregarInsumosFichaProd(ifp);

                InsumosFichaProdDet ifpd = new InsumosFichaProdDet();
                InsumosFichaProdDetCommands ifpdc = new InsumosFichaProdDetCommands();

                ifpd.setIdInsumoFichaProd(ifpc.obtenerUltIdInsumoFichaProd());
                for (int i = 0; i < tblInsXproc.getRowCount(); i++)
                {
                    if (lstInsumos.get(i).getIdProducto() != 0)
                    {
                        ifpd.setClave(tblInsXproc.getValueAt(i, 0).toString());
                        ifpd.setPorcentaje(Double.parseDouble(tblInsXproc.getValueAt(i, 1).toString()));
                        ifpd.setMaterial(tblInsXproc.getValueAt(i, 2).toString());
                        ifpd.setTemperatura(tblInsXproc.getValueAt(i, 3).toString());
                        ifpd.setRodar(tblInsXproc.getValueAt(i, 4).toString());
                        ifpd.setCantidad(Double.parseDouble(tblInsXproc.getValueAt(i, 5).toString()));
                        ifpd.setObservaciones(tblInsXproc.getValueAt(i, 6).toString());
                        ifpd.setPrecioUnitario(Double.parseDouble(tblInsXproc.getValueAt(i, 7).toString()));
                        ifpd.setTotal(Double.parseDouble(tblInsXproc.getValueAt(i, 8).toString()));
                    }
                    else
                    {
                        ifpd.setClave("");
                        ifpd.setPorcentaje(0.0);
                        ifpd.setMaterial("");
                        ifpd.setTemperatura(tblInsXproc.getValueAt(i, 3).toString());
                        ifpd.setRodar(tblInsXproc.getValueAt(i, 4).toString());
                        ifpd.setCantidad(0.0);
                        ifpd.setObservaciones(tblInsXproc.getValueAt(i, 6).toString());
                        ifpd.setPrecioUnitario(0.0);
                        ifpd.setTotal(0.0);
                    }

                    ifpdc.insertarInsumosFichaProdDet(ifpd);
                }

                JOptionPane.showMessageDialog(null, "Se generó correctamente la ficha no. "+ fpd.getIdFichaProd());

                PnlFichaProduccion pnlFichaProduccion =  new PnlFichaProduccion();
                pnlPrincipalx.removeAll();
                pnlPrincipalx.add(pnlFichaProduccion, BorderLayout.CENTER);
                pnlPrincipalx.paintAll(pnlFichaProduccion.getGraphics());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Es necesario configurar insumos \npara generar fichas de este subProceso","Error",JOptionPane.ERROR_MESSAGE);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar ficha de producción \nVerifique la información capturada","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGenerarFichaActionPerformed

    private void btnEliminarPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPartidaActionPerformed
        eliminarPartida();
    }//GEN-LAST:event_btnEliminarPartidaActionPerformed

    private void btnEliminarRecorteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarRecorteActionPerformed
        eliminarRecorte();
    }//GEN-LAST:event_btnEliminarRecorteActionPerformed

    private void btnSelAcabadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelAcabadoActionPerformed
        try 
        {
            pad = new PartidaDisp();
            int i = tblPartidasDisponibles.getSelectedRow();
            
            pad.setNoPartida(lstPartidas.get(i).getNoPartida());
            pad.setTipoRecorte(lstPartidas.get(i).getTipoRecorte());
            pad.setNoPiezasAct(lstPartidas.get(i).getNoPiezasAct());
            pad.setIdPartidaDet(lstPartidas.get(i).getIdPartidaDet());
            pad.setIdPartida(lstPartidas.get(i).getIdPartida());
            pad.setIdTipoRecorte(lstPartidas.get(i).getIdTipoRecorte());
            pad.setIdProceso(Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0]));
            
            abrirDialogoSeleccionarAcabado();
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla \nde partidas disponibles","Mensaje",JOptionPane.WARNING_MESSAGE);
            return;
        }
    }//GEN-LAST:event_btnSelAcabadoActionPerformed

    private void tblPartidasDisponiblesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartidasDisponiblesMouseClicked
        if (evt.getClickCount() == 1)
        {
            if (p.getIdProceso() == 4)
            {
                int fila = tblPartidasDisponibles.getSelectedRow();
                if (tblPartidasDisponibles.getValueAt(fila, 2).equals("Centro"))
                {
                    btnSelAcabado.setEnabled(true);
                }
                else
                {
                    btnSelAcabado.setEnabled(false);
                }
            }
            else
            {
                btnSelAcabado.setEnabled(false);
            }
        }
        else
        {
            btnSelAcabado.setEnabled(false);
        }
    }//GEN-LAST:event_tblPartidasDisponiblesMouseClicked

    private void txtNoPiezasSelAcabadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasSelAcabadoKeyReleased
        if (txtNoPiezasSelAcabado.getText().equals(""))
        {
            txtNoPiezasSelAcabado.setText("0");
            txtNoPiezasSelAcabado1.setText("0");
        }
        else
        {
            txtNoPiezasSelAcabado1.setText(txtNoPiezasSelAcabado.getText());
        }
    }//GEN-LAST:event_txtNoPiezasSelAcabadoKeyReleased

    private void txtNoPiezasSelAcabadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasSelAcabadoKeyTyped
        validarNumerosEnteros(evt);
    }//GEN-LAST:event_txtNoPiezasSelAcabadoKeyTyped

    private void btnGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardar1ActionPerformed
        int noPiezasAcabar = 0;
        int noPiezas = 0;
        try 
        {
            noPiezasAcabar = Integer.parseInt(txtNoPiezasSelAcabado.getText());
        } 
        catch (Exception e) 
        {
            
        }
        
        if (noPiezasAcabar > 0 && noPiezasAcabar <= pad.getNoPiezasAct())
        {
            try 
            {
                noPiezas = Integer.parseInt(txtNoPiezasSelAcabado1.getText());
                
                pdc = new PartidaDetalleCommands();
                pdc.agregarRecorte(pad, noPiezasAcabar, noPiezas, Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0])-1);
                dlgSelAcabado.setVisible(false);
                JOptionPane.showMessageDialog(null,"Acabado realizado correctamente");
                actualizarTablaPartidasDisponibles();
                inicializarTablaPartidasAgregadas();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Error al realizar acabado","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            if (noPiezasAcabar == 0)
            {
                dlgSelAcabado.setVisible(false);
                JOptionPane.showMessageDialog(null,"Piezas a acabar deben ser mayor a 0","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgSelAcabado.setVisible(true);
            }
            else
            {
                dlgSelAcabado.setVisible(false);
                JOptionPane.showMessageDialog(null,"Piezas a acabar insuficientes para la partida seleccionada","Mensaje",JOptionPane.WARNING_MESSAGE);
                dlgSelAcabado.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnGuardar1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            int fila = tblPartidasDisponibles.getSelectedRow();
            String piezas = (String.valueOf(tblPartidasDisponibles.getValueAt(fila, 3)));
            int numPiezasActuales = Integer.parseInt(piezas);

            if (numPiezasActuales != 0)
            {
                abrirDialogoEliPzaFichaProd();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "El número de piezas actuales debe ser mayor a 0","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de partidas disponibles","Advertencia",JOptionPane.WARNING_MESSAGE);
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
            eliminarPiezasFichaProd();
        }
        catch (Exception ex)
        {
            Logger.getLogger(PnlCross.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRealizarEntradaEnvSemiActionPerformed

    private void btnCancelarAgregarEnvSemiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarEnvSemiActionPerformed
        dlgEliPzaFichaProd.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarEnvSemiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnCancelarAgregarEnvSemi;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminarPartida;
    private javax.swing.JButton btnEliminarRecorte;
    private javax.swing.JButton btnGenerarFicha;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardar1;
    private javax.swing.JButton btnRealizarEntradaEnvSemi;
    private javax.swing.JButton btnRecortar;
    private javax.swing.JButton btnSelAcabado;
    private javax.swing.JComboBox<String> cmbProceso;
    private javax.swing.JComboBox<String> cmbTambores;
    private javax.swing.JDialog dlgEliPzaFichaProd;
    private javax.swing.JDialog dlgRecortar;
    private javax.swing.JDialog dlgSelAcabado;
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
    private javax.swing.JLabel jLabel24;
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
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
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
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblPiezasDe2;
    private javax.swing.JLabel lblSubProceso;
    private javax.swing.JLabel lblTipoCuero1;
    private javax.swing.JLabel lblTipoCuero2;
    private javax.swing.JLabel lblTipoCueroAcabar;
    private javax.swing.JLabel lblTipoCueroAcabar1;
    private javax.swing.JLabel lblTipoCueroRecortar;
    private javax.swing.JLabel lblyRecortar;
    private javax.swing.JTable tblInsXproc;
    private javax.swing.JTable tblPartidasAgregadas;
    private javax.swing.JTable tblPartidasDisponibles;
    private javax.swing.JTable tblSubproceso;
    private javax.swing.JTextField txtNoPartida;
    private javax.swing.JTextField txtNoPiezasActuales;
    private javax.swing.JTextField txtNoPiezasEliminar;
    private javax.swing.JTextField txtNoPiezasRecortar;
    private javax.swing.JTextField txtNoPiezasRecortar1;
    private javax.swing.JTextField txtNoPiezasRecortar2;
    private javax.swing.JTextField txtNoPiezasSelAcabado;
    private javax.swing.JTextField txtNoPiezasSelAcabado1;
    private javax.swing.JTextField txtProveedorNoCamion;
    private javax.swing.JTextField txtTipoRecorte;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextArea txtrMotivo;
    // End of variables declaration//GEN-END:variables
}
