/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.ConexionBD;
import Controlador.SalidaMaterialCommands;
import Modelo.Entity.FichaProduccion;
import Modelo.Entity.Material;
import Modelo.Entity.SalidaMaterial;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Yahiko Mikami
 */
public class PnlSalidaFichaMaterial extends javax.swing.JPanel {

    private ConexionBD conexion;
    private FichaProduccion fp;
    private Map ma;
    private String tmpFicha = "";
    private javax.swing.JDialog dlgSalidaMaterial;
    private TableModel tblMateriales;
    private List<Map> lstMaterial;
    
    /**
     * Creates new form pnlSalidaFichaMaterial
     */
    public PnlSalidaFichaMaterial() {
         initComponents();
        inicializar();
    }

    public void inicializar(){
        conexion = new ConexionBD();
        fp = new FichaProduccion();        
        lstMaterial = new ArrayList<>();
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
    
    private void validarNumerosEnteros(java.awt.event.KeyEvent evt, String textoCaja)
    {
        try {
            char c = evt.getKeyChar();
            
            if (c<'0' || c>'9') 
            {
                evt.consume();
            }
        } catch (Exception ex) {
            Logger.getLogger(PnlDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblMaterialesFicha = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        lblNoFicha = new javax.swing.JLabel();
        lblSolicitante = new javax.swing.JLabel();
        lblDepartamento = new javax.swing.JLabel();
        txtDepartamento = new javax.swing.JTextField();
        txtSolicitante = new javax.swing.JTextField();
        txtNoFicha = new javax.swing.JTextField();
        lblRequired1 = new javax.swing.JLabel();
        lblRequired2 = new javax.swing.JLabel();
        lblRequired3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblComentarios = new javax.swing.JLabel();
        txtaComentarios = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        lblFechaSalida = new javax.swing.JLabel();
        dcFechaSalida = new datechooser.beans.DateChooserCombo();
        btnGuardarSalida = new javax.swing.JButton();
        lblRequired4 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1223, 448));

        tblMaterialesFicha.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblMaterialesFicha.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Material", "Cantidad Solicitada", "Unidad Medida", "Existencia En Almacen", "Estatus"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMaterialesFicha.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblMaterialesFicha.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblMaterialesFicha);
        tblMaterialesFicha.getAccessibleContext().setAccessibleParent(this);

        jLabel4.setText("jLabel4");

        lblNoFicha.setText("No. Ficha:");

        lblSolicitante.setText("Solicitante:");

        lblDepartamento.setText("Departamento:");

        txtNoFicha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoFichaFocusLost(evt);
            }
        });
        txtNoFicha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoFichaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoFichaKeyTyped(evt);
            }
        });

        lblRequired1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblRequired1.setForeground(new java.awt.Color(255, 0, 0));
        lblRequired1.setText("*");

        lblRequired2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblRequired2.setForeground(new java.awt.Color(255, 0, 0));
        lblRequired2.setText("*");

        lblRequired3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblRequired3.setForeground(new java.awt.Color(255, 0, 0));
        lblRequired3.setText("*");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblDepartamento)
                        .addComponent(lblSolicitante, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(lblNoFicha))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtNoFicha, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRequired1))
                    .addComponent(txtSolicitante)
                    .addComponent(txtDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRequired2)
                    .addComponent(lblRequired3))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoFicha)
                    .addComponent(txtNoFicha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRequired1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSolicitante)
                    .addComponent(txtSolicitante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRequired2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDepartamento)
                    .addComponent(txtDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRequired3))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        lblComentarios.setText("Comentarios:");

        txtaComentarios.setColumns(20);
        txtaComentarios.setRows(5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblComentarios)
                .addContainerGap(322, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(100, 100, 100)
                    .addComponent(txtaComentarios, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblComentarios, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtaComentarios, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        lblFechaSalida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFechaSalida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/calendar.png"))); // NOI18N
        lblFechaSalida.setText("Fecha Salida:");

        dcFechaSalida.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(187, 187, 187),
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
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dcFechaSalida.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
    dcFechaSalida.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
    try {
        dcFechaSalida.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFechaSalida.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));

    btnGuardarSalida.setText("Generar Salida");
    btnGuardarSalida.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnGuardarSalidaActionPerformed(evt);
        }
    });

    lblRequired4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    lblRequired4.setForeground(new java.awt.Color(255, 0, 0));
    lblRequired4.setText("*");

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardarSalida))
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblFechaSalida)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dcFechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(lblRequired4)
                    .addGap(0, 130, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(dcFechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblFechaSalida)
                .addComponent(lblRequired4))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGuardarSalida)
            .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(354, 354, 354)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12, 12, 12)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(jScrollPane1))
            .addContainerGap())
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE)))
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGap(18, 18, 18)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
            .addGap(14, 14, 14))
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE)))
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)))
    );

    getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarSalidaActionPerformed
        llenarTabla();
        if (ValidarSalidaFicha()) {
            try {
                SalidaFichaMaterialCreate();
            } catch (Exception ex) {
                Logger.getLogger(PnlSalidaFichaMaterial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnGuardarSalidaActionPerformed

    private void txtNoFichaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoFichaFocusLost

        
    }//GEN-LAST:event_txtNoFichaFocusLost
    
    private void SalidaFichaMaterialCreate() throws ParseException, Exception
    {
        SalidaMaterial salida;
        SalidaMaterialCommands sc;
        List<SalidaMaterial> listaMateriales = new ArrayList<SalidaMaterial>();
        int respuesta = -1;
        ArrayList<SalidaMaterial> lstSalidas = new ArrayList<>();
        sc = new SalidaMaterialCommands();
        if (ValidarSalidaFicha())
        {
            for (int i = 0 ; i < lstMaterial.size(); i ++) {
            salida = new SalidaMaterial();
            salida.setMaterialId(Integer.parseInt(lstMaterial.get(i).get("idmaterial").toString()));
            salida.setCantidad(Double.parseDouble(lstMaterial.get(i).get("cantidad").toString()));
            salida.setSolicitante(txtSolicitante.getText());
            salida.setDepartamento(txtDepartamento.getText());
            salida.setComentarios(txtaComentarios.getText());
            salida.setIdInsumoFichaProd(0);
            salida.setIdUsuario(FrmPrincipal.u.getIdUsuario());
            salida.setFechaSalida(new SimpleDateFormat("dd/MM/yyyy").parse(dcFechaSalida.getText()));
            lstSalidas.add(salida);
            }
            respuesta = sc.SalidaFichaMaterialCreate(lstSalidas,Integer.parseInt(lstMaterial.get(1).get("idfichaprod").toString()));
            if (respuesta > 0)
            {
                //dlgSalidaMaterial.setVisible(false);
                JOptionPane.showMessageDialog(null, "Salida realizada con éxito");
                actualizarTablaMaterialesFicha();
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
    
    private void txtNoFichaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoFichaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            llenarTabla();
                ((Component) evt.getSource()).transferFocus();
            
        } else {
            limpiarComponentesSalidaMateriales();
        }
    }//GEN-LAST:event_txtNoFichaKeyPressed

    private void txtNoFichaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoFichaKeyTyped
        validarNumerosEnteros(evt, txtNoFicha.getText());
    }//GEN-LAST:event_txtNoFichaKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarSalida;
    private datechooser.beans.DateChooserCombo dcFechaSalida;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblComentarios;
    private javax.swing.JLabel lblDepartamento;
    private javax.swing.JLabel lblFechaSalida;
    private javax.swing.JLabel lblNoFicha;
    private javax.swing.JLabel lblRequired1;
    private javax.swing.JLabel lblRequired2;
    private javax.swing.JLabel lblRequired3;
    private javax.swing.JLabel lblRequired4;
    private javax.swing.JLabel lblSolicitante;
    private javax.swing.JTable tblMaterialesFicha;
    private javax.swing.JTextField txtDepartamento;
    private javax.swing.JTextField txtNoFicha;
    private javax.swing.JTextField txtSolicitante;
    private javax.swing.JTextArea txtaComentarios;
    // End of variables declaration//GEN-END:variables

    private void limpiarComponentesSalidaMateriales() {
        DefaultTableModel dtm = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] cols = new String[]{
            "idfichaprod",  "idmaterial", "Código", "Material", "Cantidad", "Unidad Medida", "Existencia", "idestatus", "Estatus"
        };
        dtm.setColumnIdentifiers(cols);
        tblMaterialesFicha.setModel(dtm);
        TableColumnModel columnModel = tblMaterialesFicha.getColumnModel();
        columnModel.getColumn(0).setMinWidth(0);
        columnModel.getColumn(0).setMaxWidth(0);
        columnModel.getColumn(1).setMinWidth(0);
        columnModel.getColumn(1).setMaxWidth(0);
        columnModel.getColumn(7).setMinWidth(0);
        columnModel.getColumn(7).setMaxWidth(0);
        tblMaterialesFicha.getTableHeader().setReorderingAllowed(false);
    }

    private boolean ValidarSalidaFicha() {
        boolean respuesta = true;
        String mensaje = "";

        if (respuesta) {
            if (txtNoFicha.getText().trim().equals("")) {
                mensaje = "Ingrese un No. de ficha";
                respuesta = false;
            }else
            if (txtSolicitante.getText().trim().equals("")) {
                mensaje = "Ingrese nombre del solicitante";
                respuesta = false;
            } else if (txtDepartamento.getText().trim().equals("")) {
                mensaje = "Ingrese nombre del departamento";
                respuesta = false;
            } else if (dcFechaSalida.getText().trim().equals("")) {
                mensaje = "Seleccione una fecha de sálida";
                respuesta = false;
            } else if (tblMaterialesFicha.getModel().getRowCount() > 0) {
                int c = 0;
                for (int i = 0; i < tblMaterialesFicha.getModel().getRowCount(); i++) {
                    if (tblMaterialesFicha.getModel().getValueAt(i, 8) == null) {
                        c++;
                    }else
                    if (!tblMaterialesFicha.getModel().getValueAt(i, 8).toString().equalsIgnoreCase("Disponible")) {
                        mensaje = "Uno o más materiales no se encuentran disponibles.";
                        respuesta = false;
                    } 
                }
                if (c == tblMaterialesFicha.getModel().getRowCount()) {
                    mensaje = "No hay material para aplicar sálida..";
                        respuesta = false;
                }
            }
        }
        if (!respuesta) {
            lblRequired1.setVisible(true);
            lblRequired2.setVisible(true);
            lblRequired3.setVisible(true);
            lblRequired4.setVisible(true);
            JOptionPane.showMessageDialog(this, mensaje, "", JOptionPane.WARNING_MESSAGE);
        }
        if (respuesta) {
            
            lblRequired1.setVisible(false);
            lblRequired2.setVisible(false);
            lblRequired3.setVisible(false);
            lblRequired4.setVisible(false);
            //JOptionPane.showMessageDialog(this, mensaje, "", JOptionPane.INFORMATION_MESSAGE);
        }

        return respuesta;
    }

    private void actualizarTablaMaterialesFicha() {
        limpiarComponentesSalidaMateriales();
    }

    private void llenarTabla() {
        lstMaterial = null;
            String noFicha = txtNoFicha.getText();
            lstMaterial = new ArrayList<Map>();
            if (noFicha != null && noFicha.toString().trim().equals("")) {
                limpiarComponentesSalidaMateriales();
            } else {
                limpiarComponentesSalidaMateriales();
                SalidaMaterialCommands mc = new SalidaMaterialCommands();
                lstMaterial = mc.MaterialGetCollectionByIdFichaProd(Integer.parseInt(noFicha));
                if (lstMaterial.size() == 0) {
                    JOptionPane.showMessageDialog(this, "La ficha no existe o ya fue surtida.", "", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    DefaultTableModel dtm = new DefaultTableModel() {
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    String[] cols = new String[]{
                        "idfichaprod", "idmaterial", "Código", "Material", "Cantidad", "Unidad Medida", "Existencia", "idestatus", "Estatus"
                    };
                    dtm.setColumnIdentifiers(cols);
                    dtm.setRowCount(lstMaterial.size());
                    String tmpIdMaterial = "";
                    String tmpMaterial = "";
                    int tmpFila = -1;
                    int fila = 0;
                    for (int i = 0; i < lstMaterial.size(); i++) {
                        if (i > 0 && lstMaterial.get(i).get("idmaterial").toString().equalsIgnoreCase(tmpIdMaterial) && lstMaterial.get(i).get("material").toString().equalsIgnoreCase(tmpMaterial)) {
                            float cantidad = Float.parseFloat(lstMaterial.get(i).get("cantidad").toString())
                                    + Float.parseFloat(dtm.getValueAt(tmpFila, 4).toString());
                            dtm.setValueAt(
                                    cantidad,
                                    tmpFila, 4);

                            continue;
                        }
                        dtm.setValueAt(lstMaterial.get(i).get("idfichaprod"), fila, 0);
                        //dtm.setValueAt(lstMaterial.get(i).get("clave"), fila, 1);
                        dtm.setValueAt(lstMaterial.get(i).get("idmaterial"), fila, 1);
                        dtm.setValueAt(lstMaterial.get(i).get("codigo"), fila, 2);
                        dtm.setValueAt(lstMaterial.get(i).get("material"), fila, 3);
                        dtm.setValueAt(lstMaterial.get(i).get("cantidad"), fila, 4);
                        dtm.setValueAt(lstMaterial.get(i).get("unidadmedidad"), fila, 5);
                        dtm.setValueAt(lstMaterial.get(i).get("existencia"), fila, 6);
                        dtm.setValueAt(lstMaterial.get(i).get("idestatus"), fila, 7);
                        if (lstMaterial.get(i).get("idestatus").toString().equals("25")
                                && Float.parseFloat(lstMaterial.get(i).get("cantidad").toString())
                                <= Float.parseFloat(lstMaterial.get(i).get("existencia").toString())) {
                            dtm.setValueAt("Disponible", fila, 8);
                        } else if (lstMaterial.get(i).get("idestatus").toString().equals("25")
                                && Float.parseFloat(lstMaterial.get(i).get("cantidad").toString())
                                > Float.parseFloat(lstMaterial.get(i).get("existencia").toString())) {
                            dtm.setValueAt("Cantidad Insuficiente", fila, 8);
                        } else {
                            dtm.setValueAt("No Existente En Almacen", fila, 8);
                        }
                        //dtm.setValueAt(lstMaterial.get(i).get("estatus"), fila, 9);
                        //dtm.setValueAt(lstMaterial.get(i).get("fechaultimaact"), fila, 10);
                        tmpIdMaterial = lstMaterial.get(i).get("idmaterial").toString();
                        tmpMaterial = lstMaterial.get(i).get("material").toString();
                        tmpFila = fila;
                        fila++;

                    }

                    tblMaterialesFicha.setModel(dtm);
                    TableColumnModel columnModel = tblMaterialesFicha.getColumnModel();
                    columnModel.getColumn(0).setMinWidth(0);
                    columnModel.getColumn(0).setMaxWidth(0);
                    columnModel.getColumn(1).setMinWidth(0);
                    columnModel.getColumn(1).setMaxWidth(0);
                    columnModel.getColumn(7).setMinWidth(0);
                    columnModel.getColumn(7).setMaxWidth(0);
                    tblMaterialesFicha.getTableHeader().setReorderingAllowed(false);
                }
            }
    }
}
