/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.PartidaCommands;
import Controlador.PartidaDetalleCommands;
import Controlador.ProcesoCommands;
import Controlador.SubProcesoCommands;
import Modelo.Partida;
import Modelo.PartidaDetalle;
import Modelo.Proceso;
import Modelo.SubProceso;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mingo
 */
public class PnlFichaProduccion extends javax.swing.JPanel {
    ProcesoCommands prc;
    Proceso pr;
    Partida p;
    PartidaCommands pc;
    PartidaDetalle pd;
    PartidaDetalleCommands pdc;
    SubProceso subP;
    SubProcesoCommands subPc;
    String[][] proceso = null;
    String[][] subProceso = null;
    String[][] partida = null;
    
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
            actualizarTablaSubProc();
        } 
        catch (Exception e)
        {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error de BD","Error",JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void llenarComboProcesos() throws Exception
    {
        prc = new ProcesoCommands();
        proceso = prc.llenarComboboxProcesos();
        
        int i=0;
        while (i<proceso.length)
        {
            cmbProceso.addItem(proceso[i][1]);
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
            
            tblSubproceso.setRowSelectionInterval(0, 0);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD" , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Método para actualizar la tabla de las partidas disponibles por proceso
    public void actualizarTablaPartidas() 
    {
        p = new Partida();
        p.setIdProceso(Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0]));

        DefaultTableModel dtm = null;
        
        try 
        {
            
            pc = new PartidaCommands();
            partida = pc.obtenerPartidasDisponibles(p);
            
            String[] cols = new String[]
            {
                "No. Partida", "Recorte", "No. Piezas",
            };
            
            dtm = new DefaultTableModel(partida, cols){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblPartidasDisponibles.setModel(dtm);
            tblPartidasDisponibles.getTableHeader().setReorderingAllowed(false);
        } 
        catch (Exception e) 
        {   
            e.printStackTrace();   
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD" , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Método que abre el dialogo para recortar un tipo de cuero
    public void abrirDialogoRecortar()
    {   
        lblyRecortar.setVisible(false);
        txtNoPiezasRecortar2.setVisible(false);
        lblPiezasDe2.setVisible(false);
        lblTipoCuero2.setVisible(false);
        
        String[] tipoRecorte = null;
        String recorteSeleccionado = null;
        String aRecortar = partida[tblPartidasDisponibles.getSelectedRow()][1];
        
        if (aRecortar.equals("Entero"))
        {
            tipoRecorte = new String[] { "Delantero/Crupon", "Lados" };
        }
        else if (aRecortar.equals("Crupon Sillero"))
        {
            tipoRecorte = new String[] { "Centro Castaño", "Centro Quebracho" };
        }
        else if (aRecortar.equals("Delantero Sillero"))
        {
            recorteSeleccionado = "Delantero Suela";
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
            txtNoPiezasRecortar.setText(partida[tblPartidasDisponibles.getSelectedRow()][2]);
            txtNoPiezasRecortar1.setText(String.valueOf(Integer.parseInt(txtNoPiezasRecortar.getText())*2));
            
            if (recorteSeleccionado.equals("Delantero/Crupon"))
            {
                lblyRecortar.setVisible(true);
                txtNoPiezasRecortar2.setVisible(true);
                lblPiezasDe2.setVisible(true);
                lblTipoCuero2.setVisible(true);
                
                lblTipoCuero1.setText("Delantero");
                
                txtNoPiezasRecortar2.setText(String.valueOf(Integer.parseInt(txtNoPiezasRecortar.getText())*2));
                lblTipoCuero2.setText("Crupon");
            }
            
            dlgRecortar.setSize(300, 290);
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
        btnGuardarEditar = new javax.swing.JButton();
        lblTipoCueroRecortar = new javax.swing.JLabel();
        txtNoPiezasRecortar1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblTipoCuero1 = new javax.swing.JLabel();
        txtNoPiezasRecortar2 = new javax.swing.JTextField();
        lblPiezasDe2 = new javax.swing.JLabel();
        lblyRecortar = new javax.swing.JLabel();
        lblTipoCuero2 = new javax.swing.JLabel();
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
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbProceso2 = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbProceso = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbProceso1 = new javax.swing.JComboBox<>();

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

        btnGuardarEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarEditar.setText("Guardar cambios");
        btnGuardarEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEditarActionPerformed(evt);
            }
        });

        lblTipoCueroRecortar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTipoCueroRecortar.setText("TipoCuero");

        txtNoPiezasRecortar1.setEditable(false);
        txtNoPiezasRecortar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

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
                        .addComponent(txtNoPiezasRecortar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTipoCueroRecortar))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(btnGuardarEditar))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                .addComponent(lblTipoCuero1)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(btnGuardarEditar)
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
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 190, Short.MAX_VALUE))
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
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPartidasDisponibles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPartidasDisponiblesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblPartidasDisponibles);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRecortar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnRecortar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
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

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Proceso");

        cmbProceso2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProceso2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbProceso2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(312, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbProceso2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel13.setPreferredSize(new java.awt.Dimension(98, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cueroProceso.png"))); // NOI18N
        jLabel4.setText("Insumos por Proceso");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(257, Short.MAX_VALUE))
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/document_next.png"))); // NOI18N
        jButton2.setText("Generar Ficha");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

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

        cmbProceso1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProceso1ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbProceso1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProcesoActionPerformed
        actualizarTablaSubProc();
        actualizarTablaPartidas();
    }//GEN-LAST:event_cmbProcesoActionPerformed

    private void cmbProceso2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProceso2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbProceso2ActionPerformed

    private void tblSubprocesoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubprocesoMouseClicked
        
    }//GEN-LAST:event_tblSubprocesoMouseClicked

    private void tblPartidasDisponiblesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartidasDisponiblesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPartidasDisponiblesMouseClicked

    private void btnRecortarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecortarActionPerformed
        try 
        {
            pd =  new  PartidaDetalle();
            pdc = new PartidaDetalleCommands();

            pd.setIdTipoRecorte(Integer.parseInt(partida[tblPartidasDisponibles.getSelectedRow()][5]));
            pd.setIdPartidaDet(Integer.parseInt(partida[tblPartidasDisponibles.getSelectedRow()][3]));
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla","Mensaje",JOptionPane.WARNING_MESSAGE);
            return;
        }
        abrirDialogoRecortar();
    }//GEN-LAST:event_btnRecortarActionPerformed

    private void btnGuardarEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEditarActionPerformed
//        editarProveedor();
    }//GEN-LAST:event_btnGuardarEditarActionPerformed

    private void cmbProceso1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProceso1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbProceso1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarEditar;
    private javax.swing.JButton btnRecortar;
    private javax.swing.JComboBox<String> cmbProceso;
    private javax.swing.JComboBox<String> cmbProceso1;
    private javax.swing.JComboBox<String> cmbProceso2;
    private javax.swing.JDialog dlgRecortar;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblPiezasDe2;
    private javax.swing.JLabel lblTipoCuero1;
    private javax.swing.JLabel lblTipoCuero2;
    private javax.swing.JLabel lblTipoCueroRecortar;
    private javax.swing.JLabel lblyRecortar;
    private javax.swing.JTable tblPartidasDisponibles;
    private javax.swing.JTable tblSubproceso;
    private javax.swing.JTextField txtNoPiezasRecortar;
    private javax.swing.JTextField txtNoPiezasRecortar1;
    private javax.swing.JTextField txtNoPiezasRecortar2;
    // End of variables declaration//GEN-END:variables
}
