/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Controlador.ConexionBD;
import Controlador.FormulaXSubProcesoCommands;
import Controlador.InsumoPorProcesoCommands;
import Controlador.ProcesoCommands;
import Controlador.SubProcesoCommands;
import Modelo.FormulaXSubProceso;
import Modelo.Insumo;
import Modelo.InsumoPorProceso;
import Modelo.Proceso;
import Modelo.SubProceso;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mingo
 */
public class PnlInsXproc extends javax.swing.JPanel {
    ConexionBD conexion;
    Proceso pr;
    ProcesoCommands prc;
    SubProceso subP;
    SubProcesoCommands subPc;
    InsumoPorProcesoCommands ippc;
    String[][] proceso = null;
    String[][] subProceso = null;
    String[][] datosInsumXProc = null;
    int idSubproceso = 0;
    DefaultTableModel dtms=new DefaultTableModel();
    DefaultTableModel dtmInsumos=new DefaultTableModel();
    List<Insumo> lstInsumo;
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "Subproceso"
    };
    
    String[] cols2 = new String[]
    {
        "Clave","Porcentaje","Insumo","Rodar","idInsumo"
    };
    
    boolean[] editables = new boolean[]
    {
        true,true,false,true
    };
   
    /**
     * Creates new form PnlEntradas
     */
    public PnlInsXproc() throws Exception {
        initComponents();
        inicializar();
    }
    
    
    //Método que se invica al inicializar el sistema, inicializa todas las clases a utilizar
    public void inicializar() throws Exception
    {
        conexion = new ConexionBD();
        llenarComboProcesos();
        llenarComboInsumos();
        actualizarTablaSubProc();
        btnAgregarInsumo.setEnabled(false);
        btnAgregarEspacio.setEnabled(false);
        btnQuitarInsumo.setEnabled(false);
        btnGuardarInsumo.setEnabled(false);
        btnCopiarFormula.setEnabled(false);
        cmbInsumo.setEnabled(false);
    }
    
    //método que llena los combobox de los insumos en la base de datos
    public void llenarComboInsumos() throws Exception
    {
        ippc = new InsumoPorProcesoCommands();
        lstInsumo = new ArrayList<>();
        
        lstInsumo = ippc.ObtenerInsumos();
        
        int i=0;
        while (i < lstInsumo.size())
        {
            cmbInsumo.addItem(lstInsumo.get(i).getCNOMBREPRODUCTO());
            i++;
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

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    private void guardarFormula(int idSubProceso)
    {
        if (tblInsXSubProc.getRowCount() <= 0)
        {
            JOptionPane.showMessageDialog(null, "No hay registros para guardar","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            if (JOptionPane.showConfirmDialog(null, "Realmente desea guardar las modificaciones", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
            {
                int filas = tblInsXSubProc.getRowCount();
                FormulaXSubProceso fxs = new FormulaXSubProceso();
                FormulaXSubProcesoCommands fxsc = new FormulaXSubProcesoCommands();
                InsumoPorProceso[] datosIXP = new InsumoPorProceso[filas];
                ippc = new InsumoPorProcesoCommands();

                try
                {
                    for (int i = 0; i < filas; i++)
                    {
                        datosIXP[i] = new InsumoPorProceso();
                        try 
                        {
                            if (tblInsXSubProc.getValueAt(i, 0).toString().length() > 50) {
                                JOptionPane.showMessageDialog(null, "La clave no puede superar los 50 caracteres","Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }
//                            if (tblInsXSubProc.getValueAt(i, 3).toString().equals("0"))
//                            {
//                                datosIXP[i].setClave("");
//                            }
                            else
                            {
                                datosIXP[i].setClave(tblInsXSubProc.getValueAt(i, 0).toString());
                            }
                        } catch (Exception e)
                        {
                            datosIXP[i].setClave("");
                        }
                        //validar si el renglon se agrego por presionar btnAgregarEspacio
                        if (tblInsXSubProc.getValueAt(i, 4).toString().equals("0"))
                        {
                            datosIXP[i].setPorcentaje((float) 0.0);
                            datosIXP[i].setNombreProducto("");
                            datosIXP[i].setComentario(tblInsXSubProc.getValueAt(i, 3).toString());
                            datosIXP[i].setIdInsumo(Integer.parseInt(tblInsXSubProc.getValueAt(i, 4).toString()));
                        }
                        else
                        {
                            datosIXP[i].setPorcentaje(Float.parseFloat(tblInsXSubProc.getValueAt(i, 1).toString()));
                            datosIXP[i].setNombreProducto(tblInsXSubProc.getValueAt(i, 2).toString());
                            datosIXP[i].setComentario(tblInsXSubProc.getValueAt(i, 3).toString());
                            datosIXP[i].setIdInsumo(Integer.parseInt(tblInsXSubProc.getValueAt(i, 4).toString()));
                        } 
                    }
                    fxs.setIdSubproceso(idSubProceso);
                    fxsc.agregarFormXSubProc(fxs);
                    ippc.agregarInsumoXProc(datosIXP, idSubProceso);
                    JOptionPane.showMessageDialog(null, "Fórmula guardada correctamente");
                } catch (Exception e)
                {
//                    System.err.println(e);
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ingrese datos validos","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
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
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnGuardarInsumo = new javax.swing.JButton();
        lblSubProceso = new javax.swing.JLabel();
        btnCopiarFormula = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cmbInsumo = new javax.swing.JComboBox<>();
        btnAgregarInsumo = new javax.swing.JButton();
        btnQuitarInsumo = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblInsXSubProc = new javax.swing.JTable();
        btnAgregarEspacio = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbProceso = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSubproceso = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/flask.png"))); // NOI18N
        jLabel4.setText("Insumos Asignados:");

        btnGuardarInsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disk.png"))); // NOI18N
        btnGuardarInsumo.setText("Guardar");
        btnGuardarInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarInsumoActionPerformed(evt);
            }
        });

        lblSubProceso.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnCopiarFormula.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/calendar_copy.png"))); // NOI18N
        btnCopiarFormula.setText("Copiar Fórmula");
        btnCopiarFormula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopiarFormulaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(btnGuardarInsumo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCopiarFormula)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardarInsumo)
                .addComponent(btnCopiarFormula))
            .addComponent(lblSubProceso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Insumo");

        cmbInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbInsumoActionPerformed(evt);
            }
        });

        btnAgregarInsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
        btnAgregarInsumo.setText("Agregar Insumo");
        btnAgregarInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarInsumoActionPerformed(evt);
            }
        });

        btnQuitarInsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cross.png"))); // NOI18N
        btnQuitarInsumo.setText("Quitar");
        btnQuitarInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarInsumoActionPerformed(evt);
            }
        });

        tblInsXSubProc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave", "Porcentaje", "Insumo", "Rodar"
            }
        ));
        tblInsXSubProc.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblInsXSubProc.getTableHeader().setReorderingAllowed(false);
        tblInsXSubProc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInsXSubProcMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblInsXSubProc);

        btnAgregarEspacio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/note_add.png"))); // NOI18N
        btnAgregarEspacio.setText("Agregar Espacio");
        btnAgregarEspacio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEspacioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbInsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAgregarInsumo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAgregarEspacio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnQuitarInsumo)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbInsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarInsumo)
                    .addComponent(btnQuitarInsumo)
                    .addComponent(btnAgregarEspacio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setPreferredSize(new java.awt.Dimension(98, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cueroProceso.png"))); // NOI18N
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

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Proceso");

        cmbProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProcesoActionPerformed(evt);
            }
        });

        tblSubproceso.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbProceso, 0, 168, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbProceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void cmbProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProcesoActionPerformed
        actualizarTablaSubProc();
    }//GEN-LAST:event_cmbProcesoActionPerformed

    private void tblSubprocesoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubprocesoMouseClicked
      
      subP = new SubProceso();
      if(evt.getClickCount() == 2){
          btnAgregarInsumo.setEnabled(true);
          btnAgregarEspacio.setEnabled(true);
          btnQuitarInsumo.setEnabled(true);
          btnGuardarInsumo.setEnabled(true);
          btnCopiarFormula.setEnabled(true);
          cmbInsumo.setEnabled(true);
          lblSubProceso.setText(tblSubproceso.getValueAt(tblSubproceso.getSelectedRow(), 0).toString());
          
          String idAux = "";
          String descripcion = tblSubproceso.getValueAt(tblSubproceso.getSelectedRow(), 0).toString();
            if (subProceso[tblSubproceso.getSelectedRow()][0].equals(descripcion))
            {
                idAux = subProceso[tblSubproceso.getSelectedRow()][1];
                idSubproceso = Integer.parseInt(idAux);
                subP.setIdSubProceso(idSubproceso);
            }
        
            try {

                datosInsumXProc = subPc.obtenerListaInsXSubProc(subP);

                dtmInsumos = new DefaultTableModel(datosInsumXProc, cols2){ 
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                return editables[columnIndex];
                }
                };
                tblInsXSubProc.setModel(dtmInsumos);
                tblInsXSubProc.getTableHeader().setReorderingAllowed(false);
                tblInsXSubProc.getColumnModel().getColumn(4).setMaxWidth(0);
                tblInsXSubProc.getColumnModel().getColumn(4).setMinWidth(0);
                tblInsXSubProc.getColumnModel().getColumn(4).setPreferredWidth(0);

            } catch (Exception e) {

                e.printStackTrace();

                JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
            }
       }
    }//GEN-LAST:event_tblSubprocesoMouseClicked

    private void btnQuitarInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarInsumoActionPerformed
       int fila;
        try
        {
            fila=this.tblInsXSubProc.getSelectedRow();
            dtmInsumos.removeRow(fila);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de insumos","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnQuitarInsumoActionPerformed

    private void tblInsXSubProcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInsXSubProcMouseClicked
        if(evt.getClickCount()==1)
        {
            btnAgregarInsumo.setEnabled(true);
            btnQuitarInsumo.setEnabled(true);
        }
    }//GEN-LAST:event_tblInsXSubProcMouseClicked

    private void btnAgregarInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarInsumoActionPerformed
        int n;
        String datos[]=new String[5];
        datos[0]= "";
        datos[1]= "";
        datos[2]= cmbInsumo.getSelectedItem().toString();
        datos[3]= "";
        datos[4]= String.valueOf(lstInsumo.get(cmbInsumo.getSelectedIndex()).getCIDPRODUCTO());
        dtmInsumos.insertRow(tblInsXSubProc.getSelectedRow() + 1, datos);
    }//GEN-LAST:event_btnAgregarInsumoActionPerformed

    private void cmbInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbInsumoActionPerformed
        btnAgregarInsumo.setEnabled(true);
    }//GEN-LAST:event_cmbInsumoActionPerformed

    private void btnGuardarInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarInsumoActionPerformed
        guardarFormula(idSubproceso);
    }//GEN-LAST:event_btnGuardarInsumoActionPerformed

    private void btnAgregarEspacioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEspacioActionPerformed
        String datos[]=new String[5];
        datos[0]= "";
        datos[1]= "";
        datos[2]= "";
        datos[3]= "";
        datos[4]= "0";
        dtmInsumos.insertRow(tblInsXSubProc.getSelectedRow() + 1, datos);
    }//GEN-LAST:event_btnAgregarEspacioActionPerformed

    private void btnCopiarFormulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopiarFormulaActionPerformed
        try 
        {
            int idSubProceso = 0;
            String subProceso;
            pr = new Proceso();
            pr.setIdProceso(Integer.parseInt(proceso[cmbProceso.getSelectedIndex()][0]));
            
            String[][] subprocesosAux = subPc.obtenerListaSubprocesosXid(pr);
            
            String[] subprocesos = new String[subprocesosAux.length];
            for (int i = 0; i < subprocesosAux.length; i++)
            {
                subprocesos[i] = subprocesosAux[i][0];
            }
            
            subProceso = (String) JOptionPane.showInputDialog(null, "Seleccione el SubProceso al que será copiada la fórmula","Copiar Fórmula",JOptionPane.INFORMATION_MESSAGE, null, subprocesos, subprocesos[0]);
            
            if (subProceso != null)
            {
                for (int i = 0; i < subprocesosAux.length; i++) 
                {
                    if (subprocesosAux[i][0].equals(subProceso)) 
                    {
                        idSubProceso = Integer.parseInt(subprocesosAux[i][1]);
                    }
                }
                guardarFormula(idSubProceso);   
            }
            else
            {
                return;
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_btnCopiarFormulaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarEspacio;
    private javax.swing.JButton btnAgregarInsumo;
    private javax.swing.JButton btnCopiarFormula;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnGuardarInsumo;
    private javax.swing.JButton btnQuitarInsumo;
    private javax.swing.JComboBox<String> cmbInsumo;
    private javax.swing.JComboBox<String> cmbProceso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblSubProceso;
    private javax.swing.JTable tblInsXSubProc;
    private javax.swing.JTable tblSubproceso;
    // End of variables declaration//GEN-END:variables
}
