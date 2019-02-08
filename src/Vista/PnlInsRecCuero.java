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
import java.text.DecimalFormat;
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
    String[][] datosConfMerma = null;
    DecimalFormat formatea = new DecimalFormat("###,###.##");
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
    
    public void calcularCostoTotalCamion()
    {
        try 
        {
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
            String tipoCamion;

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
            
            if (cmbTipoCamion.getSelectedItem().equals("Nacional"))
            {
                costoCamion = kgTotales*precio;
            }
            else
            {
                costoCamion = noTotalPiezas*precio;
            }

            txtCostoCamion.setText(String.valueOf(costoCamion));

            txtNoTotalPiezas.setText(String.valueOf(noTotalPiezas));
            
            tipoCamion = cmbTipoCamion.getSelectedItem().toString();

            datosConfMerma = cmc.obtenerConfiguracionMerma(mermaSal,mermaHumedad,mermaCachete,tarimas,kgTotales,precio,noTotalPiezas,refMerma,tipoCamion);
            
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
            double kgTotalesConTarimas = (Double.parseDouble(datosConfMerma[0][23]));
            
            txtMermaSalAceptada.setText(String.format("%.3f", ((salAcep))));
            txtMermaHumedadAceptada.setText(String.format("%.3f", ((humedadAcep))));
            txtMermaCacheteAceptada.setText(String.format("%.3f", ((cacheteAcep))));
            txtTarimasAceptada.setText(String.format("%.3f", ((tarimasAcep))));
            txtMermaSalReal.setText(String.format("%.3f", ((salReal))));
            txtMermaHumedadReal.setText(String.format("%.3f", ((humedadReal))));
            txtMermaCacheteReal.setText(String.format("%.3f", ((cacheteReal))));
            txtTarimasReal.setText(String.format("%.3f", ((tarimasReal))));
            txtMermaSalDiferencia.setText(String.format("%.3f", ((salDiferencia))));
            txtMermaHumedadDiferencia.setText(String.format("%.3f", ((humedadDiferencia))));
            txtMermaCacheteDiferencia.setText(String.format("%.3f", ((cacheteDiferencia))));
            txtMermaTarimasDiferencia.setText(String.format("%.3f", ((tarimasDiferencia))));
            txtMermaSalDescontar.setText(String.format("%.1f", ((salDescontar))));
            txtMermaHumedadDescontar.setText(String.format("%.1f", ((humedadDescontar))));
            txtMermaCacheteDescontar.setText(String.format("%.1f", ((cacheteDescontar))));
            txtTarimasDescontar.setText(String.format("%.1f", ((tarimasDescontar))));
            txtTotalKgDescontar.setText(String.format("%.1f", ((totalDescontar))));
            txtPesoCamionKg.setText(String.format("%.2f", (kgTotalesConTarimas)));
            lblDeKgTotal.setText("");
            lblDeKgTotal.setText((humedadAcepCalc*100)+"% de Kg Total");
            lblCostoTotalCamion.setText("");
            lblCostoTotalCamion.setText("$"+String.format("%.2f",costoTotalCamion));
            txtCostoTotalCamion.setText(String.format("%.2f",(kgTotalesConTarimas-totalDescontar)));
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
            txtMermaTarimasDiferencia.setText(String.valueOf(0));
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
                if (txtKgTotales.getText().equals("0") || txtPrecio.getText().equals("0") || txtNoTotalPiezas.getText().equals("0"))
                {
                    JOptionPane.showMessageDialog(null, "El numero de kg totales, precio y total de piezas debe ser mayor a 0", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                rcc = new RecepcionCueroCommands();
        
                String proveedor = cmbProveedorAgregar.getSelectedItem().toString();
                String tipoCuero = cmbTipoCueroAgregar.getSelectedItem().toString();
                String tipoCamion = cmbTipoCamion.getSelectedItem().toString();
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
                int refParaMerma;
                int idMerSal;
                int idMerHum;
                int idMerCac;
                int idMerTar;

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
                refParaMerma = Integer.parseInt(txtRefMerma.getText());
                idMerSal = Integer.parseInt(datosConfMerma[0][19]);
                idMerHum = Integer.parseInt(datosConfMerma[0][20]);
                idMerCac = Integer.parseInt(datosConfMerma[0][21]);
                idMerTar = Integer.parseInt(datosConfMerma[0][22]);
                tipoCamion = cmbTipoCamion.getSelectedItem().toString();

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
                rc.setRefParaMerma(refParaMerma);
                rc.setIdMerSal(idMerSal);
                rc.setIdMerHum(idMerHum);
                rc.setIdMerCac(idMerCac);
                rc.setIdMerTar(idMerTar);
                rc.setTipoCamion(tipoCamion);

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
            e.printStackTrace();
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
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
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
        jLabel10 = new javax.swing.JLabel();
        txtNoCamion = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtKgTotales = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtCostoCamion = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        cmbTipoCueroAgregar = new javax.swing.JComboBox<>();
        jLabel63 = new javax.swing.JLabel();
        txtNoTotalPiezas = new javax.swing.JTextField();
        txtRefMerma = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbTipoCamion = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
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
        jPanel6 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        txtMermaHumedad = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        txtMermaHumedadAceptada = new javax.swing.JTextField();
        lblDeKgTotal = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        txtMermaHumedadReal = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        txtMermaHumedadDiferencia = new javax.swing.JTextField();
        txtMermaHumedadDescontar = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        txtMermaCachete = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        txtMermaCacheteAceptada = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        txtMermaCacheteReal = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        txtMermaCacheteDiferencia = new javax.swing.JTextField();
        txtMermaCacheteDescontar = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel85 = new javax.swing.JLabel();
        txtTarimas = new javax.swing.JTextField();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        txtTarimasAceptada = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        txtTarimasReal = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        txtMermaTarimasDiferencia = new javax.swing.JTextField();
        txtTarimasDescontar = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        txtTotalKgDescontar = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        txtPesoCamionKg = new javax.swing.JTextField();
        txtCostoTotalCamion = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        lblCostoTotalCamion = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Camión", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

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

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Tipo cuero");

        cmbTipoCueroAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel63.setText("Total Piezas");

        txtNoTotalPiezas.setEditable(false);

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

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Ref. P/Merma");

        cmbTipoCamion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbTipoCamion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nacional", "Americano" }));
        cmbTipoCamion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoCamionItemStateChanged(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Tipo de camión");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel1)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbTipoCueroAgregar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbProveedorAgregar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNoCamion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCostoCamion)
                    .addComponent(txtPrecio)
                    .addComponent(txtKgTotales, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblRangoMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblRangoMin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtNoPiezasPesados, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNoPiezasLigeros, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtRefMerma))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbTipoCamion, 0, 95, Short.MAX_VALUE)
                            .addComponent(txtNoTotalPiezas))))
                .addContainerGap(231, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbProveedorAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel11)
                        .addComponent(txtKgTotales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(cmbTipoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRangoMin)
                            .addComponent(txtNoPiezasLigeros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel63)
                            .addComponent(txtNoTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCostoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRangoMax)
                            .addComponent(txtNoPiezasPesados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(txtRefMerma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbTipoCueroAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel10)
                            .addComponent(txtNoCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Mermas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Merma de");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Sal (Kg)");

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtMermaSal, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMermaSalAceptada, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMermaSalReal, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMermaSalDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(txtMermaSalDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMermaSalReal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel22)
                            .addComponent(txtMermaSalDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23)
                            .addComponent(txtMermaSalDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel20)))
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMermaSalAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16)
                        .addComponent(txtMermaSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel18)
                        .addComponent(jLabel17))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel24))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setText("Merma de");

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel62.setText("Humedad");

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

        jLabel65.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("Merma");

        jLabel66.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel66.setText("Aceptada");

        txtMermaHumedadAceptada.setEditable(false);
        txtMermaHumedadAceptada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaHumedadAceptada.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblDeKgTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDeKgTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDeKgTotal.setText("% de Kg Total");

        jLabel68.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setText("Merma");

        jLabel69.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("real");

        txtMermaHumedadReal.setEditable(false);
        txtMermaHumedadReal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaHumedadReal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel70.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setText("Kg/Cuero");

        jLabel71.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setText("Diferencia");

        jLabel72.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setText("Humedad a");

        jLabel73.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setText("descontar");

        txtMermaHumedadDiferencia.setEditable(false);
        txtMermaHumedadDiferencia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaHumedadDiferencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtMermaHumedadDescontar.setEditable(false);
        txtMermaHumedadDescontar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaHumedadDescontar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel66)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtMermaHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMermaHumedadAceptada, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                    .addComponent(lblDeKgTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMermaHumedadReal, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel71)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMermaHumedadDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMermaHumedadDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel62))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMermaHumedadReal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel68)
                            .addComponent(jLabel71)
                            .addComponent(txtMermaHumedadDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel72)
                            .addComponent(txtMermaHumedadDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel70))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel69)))
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMermaHumedadAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel65)
                        .addComponent(txtMermaHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblDeKgTotal)
                        .addComponent(jLabel66))))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel73))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel74.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel74.setText("Merma de");

        jLabel75.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel75.setText("Cachete");

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

        jLabel76.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel76.setText("Merma");

        jLabel77.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel77.setText("Aceptada");

        txtMermaCacheteAceptada.setEditable(false);
        txtMermaCacheteAceptada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaCacheteAceptada.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel78.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel78.setText("Kg/Cuero");

        jLabel79.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel79.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel79.setText("Merma");

        jLabel80.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel80.setText("real");

        txtMermaCacheteReal.setEditable(false);
        txtMermaCacheteReal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaCacheteReal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel81.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel81.setText("Kg/Cuero");

        jLabel82.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel82.setText("Diferencia");

        jLabel83.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel83.setText("Cachete a");

        jLabel84.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel84.setText("descontar");

        txtMermaCacheteDiferencia.setEditable(false);
        txtMermaCacheteDiferencia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaCacheteDiferencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtMermaCacheteDescontar.setEditable(false);
        txtMermaCacheteDescontar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaCacheteDescontar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel77)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtMermaCachete, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMermaCacheteAceptada, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                    .addComponent(jLabel78, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel80, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel81, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMermaCacheteReal, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel82)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMermaCacheteDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel84)
                    .addComponent(jLabel83))
                .addGap(18, 18, 18)
                .addComponent(txtMermaCacheteDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel74)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel75))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMermaCacheteReal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel79)
                            .addComponent(jLabel82)
                            .addComponent(txtMermaCacheteDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel83)
                            .addComponent(txtMermaCacheteDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel81))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel80)))
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMermaCacheteAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel76)
                        .addComponent(txtMermaCachete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel78)
                        .addComponent(jLabel77))))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel84))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel85.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel85.setText("Tarimas");

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

        jLabel87.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel87.setText("Merma");

        jLabel88.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel88.setText("Aceptada");

        txtTarimasAceptada.setEditable(false);
        txtTarimasAceptada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTarimasAceptada.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel89.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel89.setText("Kg/Cuero");

        jLabel90.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel90.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel90.setText("Merma");

        jLabel91.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setText("real");

        txtTarimasReal.setEditable(false);
        txtTarimasReal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTarimasReal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel92.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel92.setText("Kg/Cuero");

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel93.setText("Diferencia");

        jLabel94.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel94.setText("Tarimas a");

        jLabel95.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("descontar");

        txtMermaTarimasDiferencia.setEditable(false);
        txtMermaTarimasDiferencia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMermaTarimasDiferencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtTarimasDescontar.setEditable(false);
        txtTarimasDescontar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTarimasDescontar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel88)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel85)
                        .addGap(10, 10, 10)
                        .addComponent(txtTarimas, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTarimasAceptada)
                    .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel92, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTarimasReal, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel93)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMermaTarimasDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel95, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(txtTarimasDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTarimasReal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel90)
                            .addComponent(jLabel93)
                            .addComponent(txtMermaTarimasDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel94)
                            .addComponent(txtTarimasDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel92))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel91)))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTarimasAceptada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel87))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTarimas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel85)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel89)
                        .addComponent(jLabel88))))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel95))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Totales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        txtTotalKgDescontar.setEditable(false);
        txtTotalKgDescontar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTotalKgDescontar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setText("Total a descontar en Kg");

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

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
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
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblCostoTotalCamion, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                    .addComponent(txtCostoTotalCamion))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(txtTotalKgDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(txtPesoCamionKg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61)
                    .addComponent(txtCostoTotalCamion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCostoTotalCamion)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        calcularCostoTotalCamion();
    }//GEN-LAST:event_txtMermaSalKeyReleased

    private void txtMermaSalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaSalKeyTyped
        validarNumeros(evt, txtMermaSal.getText());
    }//GEN-LAST:event_txtMermaSalKeyTyped

    private void txtRefMermaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRefMermaKeyReleased
        calcularCostoTotalCamion();
    }//GEN-LAST:event_txtRefMermaKeyReleased

    private void txtRefMermaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRefMermaKeyTyped
        validarNumerosEnteros(evt, txtRefMerma.getText());
    }//GEN-LAST:event_txtRefMermaKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        agregarEntradaRecepcionCuero();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtMermaHumedadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaHumedadKeyReleased
        calcularCostoTotalCamion();
    }//GEN-LAST:event_txtMermaHumedadKeyReleased

    private void txtMermaHumedadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaHumedadKeyTyped
        validarNumeros(evt, txtMermaHumedad.getText());
    }//GEN-LAST:event_txtMermaHumedadKeyTyped

    private void txtMermaCacheteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaCacheteKeyReleased
        calcularCostoTotalCamion();
    }//GEN-LAST:event_txtMermaCacheteKeyReleased

    private void txtMermaCacheteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMermaCacheteKeyTyped
        validarNumeros(evt, txtMermaCachete.getText());
    }//GEN-LAST:event_txtMermaCacheteKeyTyped

    private void txtTarimasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarimasKeyReleased
        calcularCostoTotalCamion();
    }//GEN-LAST:event_txtTarimasKeyReleased

    private void txtTarimasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarimasKeyTyped
        validarNumeros(evt, txtTarimas.getText());
    }//GEN-LAST:event_txtTarimasKeyTyped

    private void cmbTipoCamionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoCamionItemStateChanged
        try {
            calcularCostoTotalCamion();
        } catch (Exception ex) {
            Logger.getLogger(PnlInsRecCuero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cmbTipoCamionItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbProveedorAgregar;
    private javax.swing.JComboBox<String> cmbTipoCamion;
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
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
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
    private javax.swing.JTextField txtMermaTarimasDiferencia;
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
    private javax.swing.JTextField txtTarimasReal;
    private javax.swing.JTextField txtTotalKgDescontar;
    // End of variables declaration//GEN-END:variables
}
