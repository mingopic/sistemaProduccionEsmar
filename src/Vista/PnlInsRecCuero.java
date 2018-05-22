/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.ConfiguracionMermaCommands;
import Controlador.ProveedorCommands;
import Controlador.RangoPesoCueroCommands;
import Controlador.RecepcionCueroCommands;
import Controlador.TipoCueroCommands;
import Modelo.RecepcionCuero;
import static Vista.FrmPrincipal.lblVentana;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Mingo
 */
public class PnlInsRecCuero extends javax.swing.JPanel {
    PnlRecepcionCuero pnlRecepcionCuero;
    ProveedorCommands pc;
    String[][] proveedoresAgregar = null;
    TipoCueroCommands tcc;
    String[][] tipoCueroAgregar = null;
    RangoPesoCueroCommands rpcc;
    String[][] rangoPesoCuero = null;
    RecepcionCuero rc;
    RecepcionCueroCommands rcc;
    ConfiguracionMermaCommands cmc;
    /**
     * Creates new form PnlInsRecCuero
     */
    public PnlInsRecCuero() throws Exception {
        initComponents();
        inicializar();
    }
    
    public void inicializar() throws Exception
    {
        rc = new RecepcionCuero();
        
        llenarComboProveedoresAgregar();
        llenarComboTipoCueroAgregar();
        llenarlblRangoPesoCuero();
        llenarNoCamion();
        calcularCostoTotalCamion();
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
    
    public void llenarlblRangoPesoCuero() throws Exception
    {
        rpcc = new RangoPesoCueroCommands();
        rangoPesoCuero = rpcc.llenarLabelRangoPesoCuero();
        
        lblRangoMin.setText(lblRangoMin.getText()+" "+rangoPesoCuero[0][1]);
        lblRangoMax.setText(lblRangoMax.getText()+" "+rangoPesoCuero[0][2]);
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
            
            noTotalPiezas = noPiezasLigeros+noPiezasPesados;

            if (txtRefMerma.getText().isEmpty())
            {
                refMerma = 0;
            }
            else
            {
                refMerma = Integer.parseInt(txtRefMerma.getText());
                if (refMerma > noTotalPiezas)
                {
                    JOptionPane.showMessageDialog(null, "La referencia para merma no puede \nser mayor al numero total de piezas", "Error", JOptionPane.WARNING_MESSAGE);
                    txtRefMerma.setText(String.valueOf(0));
                    return;
                }
            }
            
            costoCamion = kgTotales*precio;

            txtCostoCamion.setText(String.valueOf(costoCamion));

            txtNoTotalPiezas.setText(String.valueOf(noTotalPiezas));

            datosConfMerma = cmc.obtenerConfiguracionMerma(mermaSal,mermaHumedad,mermaCachete,tarimas,kgTotales,precio,noTotalPiezas,refMerma);
            
            double costoTotalCamion = (Double.parseDouble(datosConfMerma[0][0]));
            double salAcep = (Double.parseDouble(datosConfMerma[0][1]));
            double humedadAcep = (Double.parseDouble(datosConfMerma[0][2]));
            double cacheteAcep = (Double.parseDouble(datosConfMerma[0][3]));
            double tarimasAcep = (Double.parseDouble(datosConfMerma[0][4]));
            double salReal = (Double.parseDouble(datosConfMerma[0][5]));
            double humedadReal = (Double.parseDouble(datosConfMerma[0][6]));
            double cacheteReal = (Double.parseDouble(datosConfMerma[0][7]));
            double tarimasReal = (Double.parseDouble(datosConfMerma[0][8]));
            double salDiferencia = (Double.parseDouble(datosConfMerma[0][9]));
            double humedadDiferencia = (Double.parseDouble(datosConfMerma[0][10]));
            double cacheteDiferencia = (Double.parseDouble(datosConfMerma[0][11]));
            double tarimasDiferencia = (Double.parseDouble(datosConfMerma[0][12]));
            double salDescontar = (Double.parseDouble(datosConfMerma[0][13]));
            double humedadDescontar = (Double.parseDouble(datosConfMerma[0][14]));
            double cacheteDescontar = (Double.parseDouble(datosConfMerma[0][15]));
            double tarimasDescontar = (Double.parseDouble(datosConfMerma[0][16]));
            double totalDescontar = (Double.parseDouble(datosConfMerma[0][17]));
            double humedadAcepCalc = (Double.parseDouble(datosConfMerma[0][18]));
            
            txtMermaSalAceptada.setText(String.format("%.2f", ((salAcep))));
            txtMermaHumedadAceptada.setText(String.format("%.2f", ((humedadAcep))));
            txtMermaCacheteAceptada.setText(String.format("%.2f", ((cacheteAcep))));
            txtTarimasAceptada.setText(String.format("%.2f", ((tarimasAcep))));
            txtMermaSalReal.setText(String.format("%.2f", ((salReal))));
            txtMermaHumedadReal.setText(String.format("%.2f", ((humedadReal))));
            txtMermaCacheteReal.setText(String.format("%.2f", ((cacheteReal))));
            txtTarimasReal.setText(String.format("%.2f", ((tarimasReal))));
            txtMermaSalDiferencia.setText(String.format("%.2f", ((salDiferencia))));
            txtMermaHumedadDiferencia.setText(String.format("%.2f", ((humedadDiferencia))));
            txtMermaCacheteDiferencia.setText(String.format("%.2f", ((cacheteDiferencia))));
            txtTarimasDiferencia.setText(String.format("%.2f", ((tarimasDiferencia))));
            txtMermaSalDescontar.setText(String.format("%.2f", ((salDescontar))));
            txtMermaHumedadDescontar.setText(String.format("%.2f", ((humedadDescontar))));
            txtMermaCacheteDescontar.setText(String.format("%.2f", ((cacheteDescontar))));
            txtTarimasDescontar.setText(String.format("%.2f", ((tarimasDescontar))));
            txtTotalKgDescontar.setText(String.format("%.2f", ((totalDescontar))));
            txtPesoCamionKg.setText(String.format("%.2f", (Double.parseDouble(txtKgTotales.getText()))));
            lblDeKgTotal.setText("");
            lblDeKgTotal.setText((humedadAcepCalc*100)+"% de Kg Total");
            lblCostoTotalCamion.setText("");
            lblCostoTotalCamion.setText("$"+String.valueOf(costoTotalCamion));
            txtCostoTotalCamion.setText(String.valueOf(kgTotales-totalDescontar));
        }
        catch (Exception e)
        {
            txtMermaSalAceptada.setText(String.valueOf(0));
            txtMermaHumedadAceptada.setText(String.valueOf(0));
            txtMermaCacheteAceptada.setText(String.valueOf(0));
            txtTarimasAceptada.setText(String.valueOf(0));
            txtMermaSalReal.setText(String.valueOf(0));
            txtMermaHumedadReal.setText(String.valueOf(0));
            txtMermaCacheteReal.setText(String.valueOf(0));
            txtTarimasReal.setText(String.valueOf(0));
            txtMermaSalDiferencia.setText(String.valueOf(0));
            txtMermaHumedadDiferencia.setText(String.valueOf(0));
            txtMermaCacheteDiferencia.setText(String.valueOf(0));
            txtTarimasDiferencia.setText(String.valueOf(0));
            txtMermaSalDescontar.setText(String.valueOf(0));
            txtMermaHumedadDescontar.setText(String.valueOf(0));
            txtMermaCacheteDescontar.setText(String.valueOf(0));
            txtTarimasDescontar.setText(String.valueOf(0));
            txtTotalKgDescontar.setText(String.valueOf(0));
            txtPesoCamionKg.setText(String.valueOf(0));
            lblDeKgTotal.setText("% de Kg Total");
            lblCostoTotalCamion.setText("$"+String.valueOf(0));
            txtCostoTotalCamion.setText(String.valueOf(0));
            System.err.println(e);
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
    
    public void agregarEntradaRecepcionCuero()
    {
        try
        {
            if (JOptionPane.showConfirmDialog(null, "Realmente desea agregar la recepción", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
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
                
                JOptionPane.showMessageDialog(null, "Registro insertado correctamente");
                pnlRecepcionCuero = new PnlRecepcionCuero();
                FrmPrincipal.pnlPrincipalx.removeAll();
                FrmPrincipal.pnlPrincipalx.add(pnlRecepcionCuero, BorderLayout.CENTER);
                FrmPrincipal.pnlPrincipalx.paintAll(pnlRecepcionCuero.getGraphics());

                lblVentana.setText("Recepción de Cuero");
                ImageIcon ico=new ImageIcon(".\\src\\imagenes\\lorry.png");
                lblVentana.setIcon(ico);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al insertar la recepción", "Error", JOptionPane.ERROR_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbProveedorAgregar = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        lblRangoMin = new javax.swing.JLabel();
        txtNoPiezasLigeros = new javax.swing.JTextField();
        lblRangoMax = new javax.swing.JLabel();
        txtNoPiezasPesados = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtRefMerma = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtNoCamion = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtKgTotales = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtCostoCamion = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtMermaSal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtMermaSalAceptada = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtMermaSalReal = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtMermaSalDiferencia = new javax.swing.JTextField();
        txtMermaSalDescontar = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        cmbTipoCueroAgregar = new javax.swing.JComboBox<>();
        txtMermaHumedadAceptada = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtMermaHumedad = new javax.swing.JTextField();
        lblDeKgTotal = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtMermaHumedadDescontar = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtMermaHumedadReal = new javax.swing.JTextField();
        txtMermaHumedadDiferencia = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtMermaCacheteDescontar = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtMermaCachete = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtMermaCacheteAceptada = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        txtMermaCacheteDiferencia = new javax.swing.JTextField();
        txtMermaCacheteReal = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        txtTarimasReal = new javax.swing.JTextField();
        txtTarimasDiferencia = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txtTarimasAceptada = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txtTarimas = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        txtTotalKgDescontar = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        txtTarimasDescontar = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        txtPesoCamionKg = new javax.swing.JTextField();
        txtCostoTotalCamion = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        lblCostoTotalCamion = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel63 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        txtNoTotalPiezas = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Proveedor");

        cmbProveedorAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbProveedorAgregar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorAgregarItemStateChanged(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Rango (Kg).");

        lblRangoMin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRangoMin.setText("Menor a");

        txtNoPiezasLigeros.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasLigeros.setText("0");
        txtNoPiezasLigeros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoPiezasLigerosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasLigerosKeyTyped(evt);
            }
        });

        lblRangoMax.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRangoMax.setText("Mayor a");

        txtNoPiezasPesados.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasPesados.setText("0");
        txtNoPiezasPesados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoPiezasPesadosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasPesadosKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Total Piezas");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Ligeros");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Pesados");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Ref. P/Merma");

        txtRefMerma.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtRefMerma.setText("0");
        txtRefMerma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRefMermaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRefMermaKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Fecha de llegada");

        jTextField4.setEditable(false);
        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("No. Camión");

        txtNoCamion.setEditable(false);
        txtNoCamion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Kg. Total");

        txtKgTotales.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtKgTotales.setText("0");
        txtKgTotales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKgTotalesKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKgTotalesKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Precio");

        txtPrecio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecio.setText("0");
        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Costo del camión");

        txtCostoCamion.setEditable(false);
        txtCostoCamion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Merma de");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("sal (Kg)");

        txtMermaSal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaSal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMermaSal.setText("0");
        txtMermaSal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMermaSalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMermaSalKeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Merma");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Aceptada");

        txtMermaSalAceptada.setEditable(false);
        txtMermaSalAceptada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaSalAceptada.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Kg/Cuero");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Merma");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("real");

        txtMermaSalReal.setEditable(false);
        txtMermaSalReal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaSalReal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Kg/Cuero");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Diferencia");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Sal a");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("descontar");

        txtMermaSalDiferencia.setEditable(false);
        txtMermaSalDiferencia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaSalDiferencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtMermaSalDescontar.setEditable(false);
        txtMermaSalDescontar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaSalDescontar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Tipo cuero");

        cmbTipoCueroAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtMermaHumedadAceptada.setEditable(false);
        txtMermaHumedadAceptada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaHumedadAceptada.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Aceptada");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Merma");

        txtMermaHumedad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaHumedad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMermaHumedad.setText("0");
        txtMermaHumedad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMermaHumedadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMermaHumedadKeyTyped(evt);
            }
        });

        lblDeKgTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDeKgTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDeKgTotal.setText("de Kg Total");

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Diferencia");

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("descontar");

        txtMermaHumedadDescontar.setEditable(false);
        txtMermaHumedadDescontar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaHumedadDescontar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("real");

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Merma");

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Kg/Cuero");

        txtMermaHumedadReal.setEditable(false);
        txtMermaHumedadReal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaHumedadReal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtMermaHumedadDiferencia.setEditable(false);
        txtMermaHumedadDiferencia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaHumedadDiferencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Humedad a");

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel35.setText("Merma de");

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("humedad");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Merma");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("real");

        txtMermaCacheteDescontar.setEditable(false);
        txtMermaCacheteDescontar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaCacheteDescontar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("descontar");

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("Diferencia");

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Kg/Cuero");

        txtMermaCachete.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaCachete.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMermaCachete.setText("0");
        txtMermaCachete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMermaCacheteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMermaCacheteKeyTyped(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Merma");

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Aceptada");

        txtMermaCacheteAceptada.setEditable(false);
        txtMermaCacheteAceptada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaCacheteAceptada.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel44.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("cachete");

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel45.setText("Merma de");

        jLabel46.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("Cachete a");

        txtMermaCacheteDiferencia.setEditable(false);
        txtMermaCacheteDiferencia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaCacheteDiferencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtMermaCacheteReal.setEditable(false);
        txtMermaCacheteReal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaCacheteReal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel47.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("Kg/Cuero");

        jLabel48.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("Kg");

        txtTarimasReal.setEditable(false);
        txtTarimasReal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTarimasReal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtTarimasDiferencia.setEditable(false);
        txtTarimasDiferencia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTarimasDiferencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("Tarimas a");

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel50.setText("Merma de");

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("tarimas");

        txtTarimasAceptada.setEditable(false);
        txtTarimasAceptada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTarimasAceptada.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("Aceptada");

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("Merma");

        txtTarimas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTarimas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTarimas.setText("0");
        txtTarimas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTarimasKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTarimasKeyTyped(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("Kg");

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel55.setText("Diferencia");

        jLabel56.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("descontar");

        txtTotalKgDescontar.setEditable(false);
        txtTotalKgDescontar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTotalKgDescontar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setText("real");

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel58.setText("Merma");

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setText("Total a descontar en Kg");

        txtTarimasDescontar.setEditable(false);
        txtTarimasDescontar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTarimasDescontar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("Peso del camión en Kg");

        txtPesoCamionKg.setEditable(false);
        txtPesoCamionKg.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPesoCamionKg.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtCostoTotalCamion.setEditable(false);
        txtCostoTotalCamion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCostoTotalCamion.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel61.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel61.setText("Total a pagar");

        lblCostoTotalCamion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCostoTotalCamion.setForeground(new java.awt.Color(255, 51, 51));
        lblCostoTotalCamion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCostoTotalCamion.setText("Total ");

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel63.setText("Total Piezas");

        jPanel2.setBackground(new java.awt.Color(0, 204, 51));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jToolBar1.setBorder(null);
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        jButton1.setText("Registrar Recepción");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jLabel64.setText("     ");
        jToolBar1.add(jLabel64);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        txtNoTotalPiezas.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel25)
                    .addComponent(jLabel1)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbTipoCueroAgregar, 0, 111, Short.MAX_VALUE)
                    .addComponent(jTextField4)
                    .addComponent(cmbProveedorAgregar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNoCamion, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCostoCamion)
                    .addComponent(txtPrecio)
                    .addComponent(txtKgTotales, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtRefMerma, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblRangoMin)
                                    .addComponent(lblRangoMax)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtNoPiezasPesados, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                            .addComponent(txtNoPiezasLigeros))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel7)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNoTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel26)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtMermaHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(lblDeKgTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtMermaHumedadAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtMermaHumedadReal, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel29)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtMermaHumedadDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel34)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel43)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtMermaCachete, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtMermaCacheteAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtMermaCacheteReal, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                            .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel40)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtMermaCacheteDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel50)
                                            .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel52)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtTarimas, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTarimasAceptada))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtTarimasReal, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel55)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtTarimasDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMermaHumedadDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMermaCacheteDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTarimasDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel17)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtMermaSal, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMermaSalAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMermaSalReal, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtMermaSalDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(txtMermaSalDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel59)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotalKgDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesoCamionKg, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel61)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblCostoTotalCamion, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(txtCostoTotalCamion))))
                .addGap(0, 98, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRangoMin)
                            .addComponent(txtNoPiezasLigeros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtKgTotales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRangoMax)
                            .addComponent(txtNoPiezasPesados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel63)
                            .addComponent(txtNoTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbProveedorAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCostoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbTipoCueroAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel25))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13))))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtNoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtRefMerma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtMermaSalReal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel22)
                                    .addComponent(txtMermaSalDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23)
                                    .addComponent(txtMermaSalDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel20)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtMermaSalAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16)
                                .addComponent(txtMermaSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel18)
                                .addComponent(jLabel17))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel24)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel36))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtMermaHumedadReal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel32)
                                            .addComponent(jLabel29)
                                            .addComponent(txtMermaHumedadDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel34)
                                            .addComponent(txtMermaHumedadDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel33))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(jLabel31)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtMermaHumedadAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel27)
                                        .addComponent(txtMermaHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblDeKgTotal)
                                        .addComponent(jLabel26))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel30)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel45)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel44))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtMermaCacheteReal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel37)
                                            .addComponent(jLabel40)
                                            .addComponent(txtMermaCacheteDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel46)
                                            .addComponent(txtMermaCacheteDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel47))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(jLabel38)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtMermaCacheteAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel42)
                                        .addComponent(txtMermaCachete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel41)
                                        .addComponent(jLabel43))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel39)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel50)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel51))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtTarimasReal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel58)
                                            .addComponent(jLabel55)
                                            .addComponent(txtTarimasDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel49)
                                            .addComponent(txtTarimasDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel48))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(jLabel57)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtTarimasAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel53)
                                        .addComponent(txtTarimas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel54)
                                        .addComponent(jLabel52))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel56)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(txtTotalKgDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(txtPesoCamionKg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61)
                    .addComponent(txtCostoTotalCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCostoTotalCamion)
                .addContainerGap(20, Short.MAX_VALUE))
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

    private void txtNoPiezasLigerosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasLigerosKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtNoPiezasLigerosKeyReleased

    private void txtNoPiezasLigerosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasLigerosKeyTyped
        validarNumerosEnteros(evt, txtNoPiezasLigeros.getText());
    }//GEN-LAST:event_txtNoPiezasLigerosKeyTyped

    private void txtNoPiezasPesadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasPesadosKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtNoPiezasPesadosKeyReleased

    private void txtNoPiezasPesadosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasPesadosKeyTyped
        validarNumerosEnteros(evt, txtNoPiezasPesados.getText());
    }//GEN-LAST:event_txtNoPiezasPesadosKeyTyped

    private void txtKgTotalesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgTotalesKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtKgTotalesKeyReleased

    private void txtKgTotalesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgTotalesKeyTyped
        validarNumeros(evt, txtKgTotales.getText());
    }//GEN-LAST:event_txtKgTotalesKeyTyped

    private void txtPrecioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtPrecioKeyReleased

    private void txtPrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioKeyTyped
        validarNumeros(evt, txtPrecio.getText());
    }//GEN-LAST:event_txtPrecioKeyTyped

    private void txtMermaSalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaSalKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtMermaSalKeyReleased

    private void txtMermaHumedadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaHumedadKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtMermaHumedadKeyReleased

    private void txtMermaCacheteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaCacheteKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtMermaCacheteKeyReleased

    private void txtTarimasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarimasKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtTarimasKeyReleased

    private void txtTarimasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarimasKeyTyped
        validarNumeros(evt, txtTarimas.getText());
    }//GEN-LAST:event_txtTarimasKeyTyped

    private void txtMermaCacheteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaCacheteKeyTyped
        validarNumeros(evt, txtMermaCachete.getText());
    }//GEN-LAST:event_txtMermaCacheteKeyTyped

    private void txtMermaHumedadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaHumedadKeyTyped
        validarNumeros(evt, txtMermaHumedad.getText());
    }//GEN-LAST:event_txtMermaHumedadKeyTyped

    private void txtMermaSalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaSalKeyTyped
        validarNumeros(evt, txtMermaSal.getText());
    }//GEN-LAST:event_txtMermaSalKeyTyped

    private void txtRefMermaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRefMermaKeyReleased
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtRefMermaKeyReleased

    private void txtRefMermaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRefMermaKeyTyped
        validarNumerosEnteros(evt, txtRefMerma.getText());
    }//GEN-LAST:event_txtRefMermaKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        agregarEntradaRecepcionCuero();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbProveedorAgregar;
    private javax.swing.JComboBox<String> cmbTipoCueroAgregar;
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
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
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
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblCostoTotalCamion;
    private javax.swing.JLabel lblDeKgTotal;
    private javax.swing.JLabel lblRangoMax;
    private javax.swing.JLabel lblRangoMin;
    private javax.swing.JTextField txtCostoCamion;
    private javax.swing.JTextField txtCostoTotalCamion;
    private javax.swing.JTextField txtKgTotales;
    private javax.swing.JTextField txtMermaCachete;
    private javax.swing.JTextField txtMermaCacheteAceptada;
    private javax.swing.JTextField txtMermaCacheteDescontar;
    private javax.swing.JTextField txtMermaCacheteDiferencia;
    private javax.swing.JTextField txtMermaCacheteReal;
    private javax.swing.JTextField txtMermaHumedad;
    private javax.swing.JTextField txtMermaHumedadAceptada;
    private javax.swing.JTextField txtMermaHumedadDescontar;
    private javax.swing.JTextField txtMermaHumedadDiferencia;
    private javax.swing.JTextField txtMermaHumedadReal;
    private javax.swing.JTextField txtMermaSal;
    private javax.swing.JTextField txtMermaSalAceptada;
    private javax.swing.JTextField txtMermaSalDescontar;
    private javax.swing.JTextField txtMermaSalDiferencia;
    private javax.swing.JTextField txtMermaSalReal;
    private javax.swing.JTextField txtNoCamion;
    private javax.swing.JTextField txtNoPiezasLigeros;
    private javax.swing.JTextField txtNoPiezasPesados;
    private javax.swing.JTextField txtNoTotalPiezas;
    private javax.swing.JTextField txtPesoCamionKg;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtRefMerma;
    private javax.swing.JTextField txtTarimas;
    private javax.swing.JTextField txtTarimasAceptada;
    private javax.swing.JTextField txtTarimasDescontar;
    private javax.swing.JTextField txtTarimasDiferencia;
    private javax.swing.JTextField txtTarimasReal;
    private javax.swing.JTextField txtTotalKgDescontar;
    // End of variables declaration//GEN-END:variables
}
