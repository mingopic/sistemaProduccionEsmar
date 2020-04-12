/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.ConexionBD;
import Controlador.MaterialCommands;
import Controlador.PartidaCommands;
import Modelo.Entity.Material;
import java.util.ArrayList;
import java.util.List;
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
    private final String imagen="/Imagenes/logo_esmar.png";
    
    DefaultTableModel dtms=new DefaultTableModel();
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlAlmacen() throws Exception {
        initComponents();
        inicializar();
        llenarComboMateriales();
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
        
        actualizarTablaMateriales();
    }
    
    private void llenarComboMateriales() throws Exception
    {   
        cmbMaterialEntrada.removeAllItems();

        int i = 0;
        while (i < lstMaterial.size())
        {
            cmbMaterialEntrada.addItem(lstMaterial.get(i).Descripcion());
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
                dtm.setValueAt(lstMaterial.get(i).Codigo(), i, 0);
                dtm.setValueAt(lstMaterial.get(i).Descripcion(), i, 1);
                dtm.setValueAt(lstMaterial.get(i).Existencia(), i, 2);
                dtm.setValueAt(lstMaterial.get(i).UnidadMedida(), i, 3);
                dtm.setValueAt(lstMaterial.get(i).Precio(), i, 4);
                dtm.setValueAt(lstMaterial.get(i).TipoMoneda(), i, 5);
                dtm.setValueAt(lstMaterial.get(i).TipoMaterial(), i, 6);
                dtm.setValueAt(lstMaterial.get(i).Clasificacion(), i, 7);
                dtm.setValueAt(lstMaterial.get(i).Estatus(), i, 8);
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup = new javax.swing.ButtonGroup();
        dlgRealizarEntrada = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        lblRealizarEntradaDlg = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbMaterialEntrada = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        btnGuardarEntrada = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
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
        cmbCalibre = new javax.swing.JComboBox();
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

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(440, 33));

        lblRealizarEntradaDlg.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblRealizarEntradaDlg.setForeground(new java.awt.Color(255, 255, 255));
        lblRealizarEntradaDlg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblRealizarEntradaDlg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Flecha_arriba16x16.png"))); // NOI18N
        lblRealizarEntradaDlg.setText("Realizar Entrada");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRealizarEntradaDlg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblRealizarEntradaDlg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Material:");

        jLabel2.setText("Cantidad:");

        jLabel3.setText("Comentarios:");

        jLabel4.setText("Fecha Entrada:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        dateChooserCombo1.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
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
    dateChooserCombo1.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
    dateChooserCombo1.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
    try {
        dateChooserCombo1.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dateChooserCombo1.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));

    btnGuardarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
    btnGuardarEntrada.setText("Guardar");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel1)
                .addComponent(jLabel3)
                .addComponent(jLabel4)
                .addComponent(jLabel2))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbMaterialEntrada, 0, 94, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGuardarEntrada)
            .addContainerGap())
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
                .addComponent(jLabel2)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(jLabel3)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel4)
                .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
            .addComponent(btnGuardarEntrada)
            .addContainerGap())
    );

    javax.swing.GroupLayout dlgRealizarEntradaLayout = new javax.swing.GroupLayout(dlgRealizarEntrada.getContentPane());
    dlgRealizarEntrada.getContentPane().setLayout(dlgRealizarEntradaLayout);
    dlgRealizarEntradaLayout.setHorizontalGroup(
        dlgRealizarEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    dlgRealizarEntradaLayout.setVerticalGroup(
        dlgRealizarEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(dlgRealizarEntradaLayout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPasswordField1.setText("jPasswordField1");

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

    cmbCalibre.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Todos>" }));
    cmbCalibre.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbCalibre.setPreferredSize(new java.awt.Dimension(85, 25));
    cmbCalibre.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbCalibreActionPerformed(evt);
        }
    });
    jToolBar1.add(cmbCalibre);

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
    txtNoPartida.setPreferredSize(new java.awt.Dimension(40, 25));
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
    btnRealizarEntrada.setText("Realizar Entrada");
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
    btnRealizarSalida.setText("Realizar Salida");
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
        
    }//GEN-LAST:event_btnRealizarSalidaActionPerformed

    private void txtNoPartidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaKeyPressed
        
    }//GEN-LAST:event_txtNoPartidaKeyPressed

    private void cmbCalibreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCalibreActionPerformed
        
    }//GEN-LAST:event_cmbCalibreActionPerformed

    private void btnAgregarMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarMaterialActionPerformed
        abrirDialogo(dlgRealizarEntrada, 600, 310);
    }//GEN-LAST:event_btnAgregarMaterialActionPerformed

    private void btnRealizarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRealizarEntradaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarMaterial;
    private javax.swing.JButton btnBuscar;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardarEntrada;
    private javax.swing.JButton btnRealizarEntrada;
    private javax.swing.JButton btnRealizarSalida;
    private javax.swing.JButton btnReporteEntradas;
    private javax.swing.JButton btnReporteInventario;
    private javax.swing.JButton btnReporteSalidas;
    private javax.swing.JComboBox cmbCalibre;
    private javax.swing.JComboBox<String> cmbMaterialEntrada;
    private javax.swing.JComboBox cmbTipoMaterial;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserCombo dcFechaDe;
    private datechooser.beans.DateChooserCombo dcFechaHasta;
    private javax.swing.JDialog dlgRealizarEntrada;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JRadioButton jrFiltroFechas;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblCalendario;
    private javax.swing.JLabel lblClasificacion;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblDe;
    private javax.swing.JLabel lblEnviarTerminado;
    private javax.swing.JLabel lblHasta;
    private javax.swing.JLabel lblRealizarEntradaDlg;
    private javax.swing.JLabel lblTipoMaterial;
    private javax.swing.JTable tblMateriales;
    private javax.swing.JTextField txtNoPartida;
    // End of variables declaration//GEN-END:variables
}
