/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.ConexionBD;
import Controlador.InventarioCrudoCommands;
import Controlador.PartidaCommands;
import Controlador.PartidaDetalleCommands;
import Modelo.InventarioCrudo;
import Modelo.Partida;
import Modelo.PartidaDetalle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mingo
 */
public class PnlPartidas extends javax.swing.JPanel {
    ConexionBD conexion;
    InventarioCrudo ic;
    InventarioCrudoCommands icc;
    Partida p;
    PartidaCommands pc;
    PartidaDetalle pd;
    PartidaDetalleCommands pdc;
    DefaultTableModel dtms = null;
    String[][] datosIC = null;
    String[][] asignados = null;
    String datosPartidas[] = null;
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] columnas = new String[]
    {
        "Proveedor","No. Camión","Tipo Cuero","No. Piezas","Kg. Totales","Prom. Kg/Pieza","Fecha Rec."
    };
    
    String[] colums = new String[]
        {
            "No. Camión","Proveedor","Pzas Inventario","No. Piezas","Prom. Kg/Pza","Kg Totales","Fecha"
        };
    
    
    
    public PnlPartidas() throws Exception {
        initComponents();
        iniciar();
    }
    
    public void iniciar() throws Exception
    {
        conexion = new ConexionBD();
        actualizarTablaInvCrudo();
        inicializarTablaPartida();
        llenarNoPartida();
    }
    
    public void inicializarTablaPartida()
    {
        dtms=new DefaultTableModel()
        {

            public boolean isCellEditable (int row, int column)
            {
                // Aquí devolvemos true o false según queramos que una celda
                // identificada por fila,columna (row,column), sea o no editable
                if (column == 2)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };
        
        dtms.setColumnIdentifiers(colums);
        tblPartida.setModel(dtms);
    }
    
    //Método para actualizar la tabla con las recepciones de cuero disponibles para crear partidad
    public void actualizarTablaInvCrudo() 
    {
        ic = new InventarioCrudo();
        
        DefaultTableModel dtm = null;
        
        try
        {
            datosIC = icc.obtenerListaInvCueroCrudo();
            
            if (datosIC != null)
            {
                asignados = new String[datosIC.length][1];
                
                for (int i = 0; i < asignados.length; i++)
                {
                    asignados[i][0] = "0";
                }
            }
            
            dtm = new DefaultTableModel(datosIC, columnas){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblInvCueCrudo.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    public void llenarNoPartida() throws Exception
    {
        String[] noPartida = null;
        
        noPartida = pc.llenarNoPartidas();
        
        txtNoPartida.setText(noPartida[0]);
    }
    
    public void totalPiezasPartida()
    {
        int totalPiezas = 0;
        double kgTotal = 0;
        String noRegistros[][] = new String[tblPartida.getRowCount()][2];
        
        if (tblPartida.getRowCount() > 0)
        {
            for (int i = 0; i < noRegistros.length; i++)
            {
                noRegistros[i][0] = tblPartida.getValueAt(i, 3).toString();
                noRegistros[i][1] = tblPartida.getValueAt(i, 5).toString();
                
                totalPiezas = totalPiezas + (Integer.parseInt(noRegistros[i][0]));
                kgTotal = kgTotal + (Double.parseDouble(noRegistros[i][1]));
            }
        }
        
        txtTotalPiezas.setText(String.valueOf(totalPiezas));
        txtKgTotal.setText(String.valueOf(kgTotal));
    }
    
    public void limpiarTabla(JTable tabla){
        try {
            DefaultTableModel modelo=(DefaultTableModel) tabla.getModel();
            int filas=tabla.getRowCount();
            for (int i = 0;filas>i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
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
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvCueCrudo = new javax.swing.JTable();
        btnAsignarEntrada = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNoPartida = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPartida = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtTotalPiezas = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtKgTotal = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(1049, 508));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Inventario de Cuero Crudo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblInvCueCrudo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nombre Proveedor", "Número Camión", "Tipo Cuero", "No. Piezas Actual", "Kg. Total", "Prom. Kg/Pieza", "Fecha Recepción"
            }
        ));
        tblInvCueCrudo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblInvCueCrudo);

        btnAsignarEntrada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAsignarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/arrow_turn_right.png"))); // NOI18N
        btnAsignarEntrada.setText("Asignar Entrada");
        btnAsignarEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarEntradaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAsignarEntrada)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAsignarEntrada)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Partida");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("No. Partida");

        txtNoPartida.setEditable(false);
        txtNoPartida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnEliminar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setToolTipText("");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
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

        tblPartida.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No. Camión", "Nombre Proveedor", "No. Piezas", "Kg Total"
            }
        ));
        tblPartida.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPartida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPartidaMousePressed(evt);
            }
        });
        tblPartida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPartidaKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblPartida);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Total piezas");

        txtTotalPiezas.setEditable(false);
        txtTotalPiezas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Kg Total");

        txtKgTotal.setEditable(false);
        txtKgTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNoPartida, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKgTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnEliminar)
                    .addComponent(txtNoPartida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtKgTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void btnAsignarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarEntradaActionPerformed
        try
        {
            int fila = tblInvCueCrudo.getSelectedRow();
        
            if (asignados[fila][0].equals("1"))
            {
                JOptionPane.showMessageDialog(null, "Esta recepción de cuero ya se encuentra asignada");
            }
            else
            {
                datosPartidas = new String[7];

                datosPartidas[0]= tblInvCueCrudo.getValueAt(fila, 1).toString();
                datosPartidas[1]= tblInvCueCrudo.getValueAt(fila, 0).toString();
                datosPartidas[2]= tblInvCueCrudo.getValueAt(fila, 3).toString();
                datosPartidas[3]= tblInvCueCrudo.getValueAt(fila, 3).toString();
                datosPartidas[4]= tblInvCueCrudo.getValueAt(fila, 5).toString();
                datosPartidas[5]= String.valueOf((Integer.parseInt(datosPartidas[2])) * (Double.parseDouble(datosPartidas[4])));
                datosPartidas[5]= (String.format("%.2f",Double.parseDouble(datosPartidas[5])));
                datosPartidas[6]= tblInvCueCrudo.getValueAt(fila, 6).toString();

                dtms.addRow(datosPartidas);
                totalPiezasPartida();
                asignados[fila][0] = "1";
            }
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de Inventario de Cuero Crudo","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnAsignarEntradaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila;
        try
        {
            fila=this.tblPartida.getSelectedRow();
            
            for (int i = 0; i < datosIC.length; i++)
            {
                if (datosIC[i][1].equals(tblPartida.getValueAt(fila, 0)) && datosIC[i][0].equals(tblPartida.getValueAt(fila, 1)) && datosIC[i][6].equals(tblPartida.getValueAt(fila, 6)))
                {
                    asignados[i][0] = "0";
                }
            }
            
            dtms.removeRow(fila);
            totalPiezasPartida();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla de partidas","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (tblPartida.getRowCount() <= 0)
        {
            JOptionPane.showMessageDialog(null, "No hay registros para guardar","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            if (JOptionPane.showConfirmDialog(null, "Realmente desea guardar la partida", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
            {
                int filas = tblPartida.getRowCount();
                p = new Partida();
                pc = new PartidaCommands();
                PartidaDetalle[] datosPD = new PartidaDetalle[filas];
                pdc = new PartidaDetalleCommands();
                InventarioCrudo[] datosInC = new InventarioCrudo[filas];
                String[][] datosPar = new String[filas][4];

                try
                {
                    for (int i = 0; i < filas; i++)
                    {
                        datosPD[i] = new PartidaDetalle();
                        datosInC[i] = new InventarioCrudo();
                        
                        datosPD[i].setNoPiezas(Integer.parseInt(tblPartida.getValueAt(i, 2).toString()));
                        datosPD[i].setIdPartida(Integer.parseInt(txtNoPartida.getText()));
                        datosPD[i].setIdTipoRecorte(1);
                        
                        
                        datosPar[i][0] = tblPartida.getValueAt(i, 1).toString();
                        datosPar[i][1] = tblPartida.getValueAt(i, 0).toString();
                        datosPar[i][2] = tblPartida.getValueAt(i, 6).toString();
                        datosPar[i][3] = tblPartida.getValueAt(i, 2).toString();
                    }
                    
                    p.setNoPartida(Integer.parseInt(txtNoPartida.getText()));
                    p.setNoTotalPiezas(Integer.parseInt(txtTotalPiezas.getText()));
                    p.setIdProceso(1);
                    
                    pc.agregarPartida(p);
                    pdc.agregarPartidaDetalle(datosPD,datosPar);
                    icc.actualizarNoPiezasActual(datosPar);
                    JOptionPane.showMessageDialog(null, "Registro insertado correctamente");
                    limpiarTabla(tblPartida);
                    llenarNoPartida();
                    actualizarTablaInvCrudo();
                } catch (Exception e)
                {
                    System.err.println(e);
                    JOptionPane.showMessageDialog(null, "Ingrese datos validos","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tblPartidaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPartidaKeyReleased
        int fila = tblPartida.getSelectedRow();
        String piezasUtilizar = tblPartida.getValueAt(fila, 2).toString();
        String piezas = tblPartida.getValueAt(fila, 3).toString();
        String promKgPza = tblPartida.getValueAt(fila, 4).toString();
        
        try
        {
            if (Integer.parseInt(piezasUtilizar) < 0)
            {
                JOptionPane.showMessageDialog(null, "Solo puede insertar numeros enteros mayores o igual a 0");
                tblPartida.setValueAt("0", fila, 2);
            }
            else
            {
                if (Integer.parseInt(piezasUtilizar) > Integer.parseInt(piezas))
                {
                    JOptionPane.showMessageDialog(null, "El numero de piezas a utilizar no puede ser mayor al numero de piezas");
                    tblPartida.setValueAt("0", fila, 2);
                }
                else
                {
                    String kgTotales = String.valueOf((Integer.parseInt(piezasUtilizar)) * (Double.parseDouble(promKgPza)));
                    kgTotales = (String.format("%.2f",Double.parseDouble(kgTotales)));
                    tblPartida.setValueAt(kgTotales, fila, 5);
                }
            }
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Solo puede insertar numeros enteros mayores o igual a 0");
            tblPartida.setValueAt("0", fila, 2);
        }
        totalPiezasPartida();
    }//GEN-LAST:event_tblPartidaKeyReleased

    private void tblPartidaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartidaMousePressed
         if(evt.getClickCount()==1)
        {
            for (int fila = 0; fila < tblPartida.getRowCount(); fila++)
            {
                String piezasUtilizar = tblPartida.getValueAt(fila, 2).toString();
                String piezas = tblPartida.getValueAt(fila, 3).toString();
                String promKgPza = tblPartida.getValueAt(fila, 4).toString();

                try
                {
                    if (Integer.parseInt(piezasUtilizar) < 0)
                    {
                        JOptionPane.showMessageDialog(null, "Solo puede insertar numeros enteros mayores o igual a 0");
                        tblPartida.setValueAt("0", fila, 2);
                        tblPartida.setValueAt("0", fila, 5);
                    }
                    else
                    {
                        if (Integer.parseInt(piezasUtilizar) > Integer.parseInt(piezas))
                        {
                            JOptionPane.showMessageDialog(null, "El numero de piezas a utilizar no puede ser mayor al numero de piezas");
                            tblPartida.setValueAt("0", fila, 2);
                            tblPartida.setValueAt("0", fila, 5);
                        }
                        else
                        {
                            String kgTotales = String.valueOf((Integer.parseInt(piezasUtilizar)) * (Double.parseDouble(promKgPza)));
                            kgTotales = (String.format("%.2f",Double.parseDouble(kgTotales)));
                            tblPartida.setValueAt(kgTotales, fila, 5);
                        }
                    }
                } catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null, "Solo puede insertar numeros enteros mayores o igual a 0");
                    tblPartida.setValueAt("0", fila, 2);
                    tblPartida.setValueAt("0", fila, 5);
                }
            }
            totalPiezasPartida();
        }
    }//GEN-LAST:event_tblPartidaMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignarEntrada;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblInvCueCrudo;
    private javax.swing.JTable tblPartida;
    private javax.swing.JTextField txtKgTotal;
    private javax.swing.JTextField txtNoPartida;
    private javax.swing.JTextField txtTotalPiezas;
    // End of variables declaration//GEN-END:variables
}
