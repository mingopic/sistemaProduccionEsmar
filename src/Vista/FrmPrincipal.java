/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.ConexionBD;
import Controlador.ConfiguracionMermaCommands;
import Controlador.ControladorUsuario;
import Controlador.RangoPesoCueroCommands;
import Controlador.RolesXUsuarioCommands;
import Modelo.ConfiguracionMerma;
import Modelo.RangoPesoCuero;
import Modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

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
    PnlCross pnlCross;
    PnlSemiterminado pnlSemiterminado;
    PnlFichaProduccion pnlFichaProduccion;
    PnlUsuarios pnlUsuarios;
    PnlTerminado pnlTerminado;
    ConexionBD conexionBD;
    ConfiguracionMerma cm;
    ConfiguracionMermaCommands cmc;
    RangoPesoCuero rpc;
    RangoPesoCueroCommands rpcc;
    public static String[] roles;
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
//                DateLabel();
                lblNombreUsuario.setText(u.getNombre());
//                lblIdUsuario.setText(u.getIdUsuario());
//                lblTipoUsuario.setText(u.getTipo());
//                lblTipoUsuario.setVisible(false);
                
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].equals("Semiterminado"))
                    {
                        btnSemiterminado.setVisible(true);
                        break;
                    }
                }
                
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].equals("Terminado"))
                    {
                        btnTerminado.setVisible(true);
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
                        jmpConfiguraciones.setVisible(true);
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
                        jmpConfiguraciones.setVisible(true);
                        jmpBaseDeDatos.setVisible(true);
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
            dialogo.setAlwaysOnTop(true);
            dialogo.setVisible(true);
            
            cargarConfActMerma();
            cargarConfActRango();
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
        jmProveedores = new javax.swing.JMenuItem();
        jmSubProcesos = new javax.swing.JMenuItem();
        jmTambores = new javax.swing.JMenuItem();
        Usuarios = new javax.swing.JMenuItem();
        jmpConfiguraciones = new javax.swing.JMenu();
        jmMermas = new javax.swing.JMenuItem();
        jmRangosPeso = new javax.swing.JMenuItem();
        jmInsumosXproceso = new javax.swing.JMenuItem();
        jmpBaseDeDatos = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jmpAcercaDe = new javax.swing.JMenu();

        dlgLogin.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        dlgLogin.setTitle("Login");
        dlgLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Sistema de Producción");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Contraseña:");

        txtUsuario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtUsuario.setText("Mario");
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
        ptxtContrasenia.setText("MarioDL96");
        ptxtContrasenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ptxtContraseniaActionPerformed(evt);
            }
        });

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logoEsmar.png"))); // NOI18N

        lblUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/status_online.png"))); // NOI18N

        lblContrasenia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lock.png"))); // NOI18N

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
        btnCross.setText("Cross");
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
        btnProdEnProc.setText("Producción en proceso");
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
                .addContainerGap(425, Short.MAX_VALUE))
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
                        .addGap(0, 0, Short.MAX_VALUE)
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

        jmProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/group.png"))); // NOI18N
        jmProveedores.setText("Proveedores");
        jmProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmProveedoresActionPerformed(evt);
            }
        });
        jmpCatalogos.add(jmProveedores);

        jmSubProcesos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cueroProceso.png"))); // NOI18N
        jmSubProcesos.setText("SubProcesos");
        jmSubProcesos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmSubProcesosActionPerformed(evt);
            }
        });
        jmpCatalogos.add(jmSubProcesos);

        jmTambores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/barril.png"))); // NOI18N
        jmTambores.setText("Tambores");
        jmTambores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmTamboresActionPerformed(evt);
            }
        });
        jmpCatalogos.add(jmTambores);

        Usuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/status_online.png"))); // NOI18N
        Usuarios.setText("Usuarios");
        Usuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsuariosActionPerformed(evt);
            }
        });
        jmpCatalogos.add(Usuarios);

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

        jMenuBar1.add(jmpConfiguraciones);

        jmpBaseDeDatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/database.png"))); // NOI18N
        jmpBaseDeDatos.setText("Base de datos");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/database_save.png"))); // NOI18N
        jMenuItem1.setText("Exportar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jmpBaseDeDatos.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/database_cleanup.png"))); // NOI18N
        jMenuItem2.setText("Importar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jmpBaseDeDatos.add(jMenuItem2);

        jMenuBar1.add(jmpBaseDeDatos);

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
        JOptionPane.showMessageDialog(null, "'Sistema de Producción ESMAR V.1.0' \n"
            + "Julio 2018\n\nDesarrollado por: \nIng. César Domingo Luna Gutiérrez \n\n"
            + "Dudas, comentarios o reportes de errores: \n"
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
        // TODO add your handling code here:
    }//GEN-LAST:event_btnProdEnProcActionPerformed

    private void btnTerminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerminadoActionPerformed
        try {
            pnlTerminado = new PnlTerminado();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlTerminado, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlTerminado.getGraphics());
            
            lblVentana.setText("Terminado");
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\cueroProceso.png");
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
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\cueroProceso.png");
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
            
            lblVentana.setText("Cross");
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\cueroProceso.png");
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
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\cueroProceso.png");
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
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\lorry.png");
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
        ImageIcon ico=new ImageIcon(".\\src\\imagenes\\house.png");
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
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\group.png");
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
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\cueroProceso.png");
            lblVentana.setIcon(ico);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jmSubProcesosActionPerformed

    private void jmMermasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmMermasActionPerformed
        abrirDialogo(dlgMermas, 310, 280);
    }//GEN-LAST:event_jmMermasActionPerformed

    private void jmRangosPesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmRangosPesoActionPerformed
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
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\flask.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jmInsumosXprocesoActionPerformed

    private void btnFichasProduccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFichasProduccionActionPerformed
        try {
            pnlFichaProduccion = new PnlFichaProduccion();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlFichaProduccion, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlFichaProduccion.getGraphics());
            
            lblVentana.setText("Fichas de Producción");
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\document_index.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnFichasProduccionActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            pnlExportar = new PnlExportar();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlExportar, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlExportar.getGraphics());
            
            lblVentana.setText("Exportar base de datos");
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\flask.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            pnlImportar = new PnlImportar();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlImportar, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlImportar.getGraphics());
            
            lblVentana.setText("Importar Base de datos");
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\flask.png");
            lblVentana.setIcon(ico);
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jmTamboresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmTamboresActionPerformed
        try 
        {
            pnlTambores = new PnlTambores();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlTambores, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlTambores.getGraphics());
            
            lblVentana.setText("Catálogo de Tambores");
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\barril.png");
            lblVentana.setIcon(ico);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jmTamboresActionPerformed

    private void UsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsuariosActionPerformed
        try 
        {
            pnlUsuarios = new PnlUsuarios();
            pnlPrincipalx.removeAll();
            pnlPrincipalx.add(pnlUsuarios, BorderLayout.CENTER);
            pnlPrincipalx.paintAll(pnlUsuarios.getGraphics());
            
            lblVentana.setText("Catálogo de Usuarios");
            ImageIcon ico=new ImageIcon(".\\src\\imagenes\\status_online.png");
            lblVentana.setIcon(ico);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error al abrir JDialog","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_UsuariosActionPerformed

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
    private javax.swing.JMenuItem Usuarios;
    private javax.swing.JButton btnCross;
    private javax.swing.JButton btnEntrar;
    private javax.swing.JButton btnFichasProduccion;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnPartidas;
    private javax.swing.JButton btnProdEnProc;
    private javax.swing.JButton btnRecepcionCuero;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSemiterminado;
    private javax.swing.JButton btnTerminado;
    private javax.swing.JDialog dlgLogin;
    private javax.swing.JDialog dlgMermas;
    private javax.swing.JDialog dlgRangos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JMenuItem jmInsumosXproceso;
    private javax.swing.JMenuItem jmMermas;
    private javax.swing.JMenuItem jmProveedores;
    private javax.swing.JMenuItem jmRangosPeso;
    private javax.swing.JMenuItem jmSalir;
    private javax.swing.JMenuItem jmSubProcesos;
    private javax.swing.JMenuItem jmTambores;
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
    private javax.swing.JTextField txtCachAcep;
    private javax.swing.JTextField txtHumAcep;
    private javax.swing.JTextField txtRangoMax;
    private javax.swing.JTextField txtRangoMin;
    private javax.swing.JTextField txtSalAcep;
    private javax.swing.JTextField txtTarAcep;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
