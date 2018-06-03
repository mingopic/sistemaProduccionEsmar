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
        "Proveedor","No. Camión","Tipo de Cuero","No. Piezas Actuales","Kg. Totales","Prom. Kg/Pieza","Fecha de Recepción"
    };
    
    String[] colums = new String[]
        {
            "No. Camión","Proveedor","No. Piezas","Prom. Kg/Pieza","No. Piezas Utilizar","Kg Totales","Fecha"
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
                if (column == 4)
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
            asignados = new String[datosIC.length][1];
            
            for (int i = 0; i < asignados.length; i++)
            {
                asignados[i][0] = "0";
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
                noRegistros[i][0] = tblPartida.getValueAt(i, 4).toString();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvCueCrudo = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPartida = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txtNoPartida = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtTotalPiezas = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtKgTotal = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(1049, 508));

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
        jScrollPane1.setViewportView(tblInvCueCrudo);

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
        tblPartida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPartidaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblPartidaKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(tblPartida);

        jButton1.setText("Asignar Piezas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Eliminar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Guardar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        txtNoPartida.setEditable(false);

        jLabel1.setText("No. Partida");

        jLabel2.setText("Total piezas");

        txtTotalPiezas.setEditable(false);

        jLabel3.setText("Kg Total");

        txtKgTotal.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNoPartida))
                                    .addComponent(jButton1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtKgTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(62, 62, 62))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNoPartida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTotalPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtKgTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
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
            datosPartidas[3]= tblInvCueCrudo.getValueAt(fila, 5).toString();
            datosPartidas[4]= "0";
            datosPartidas[5]= String.valueOf((Integer.parseInt(datosPartidas[4])) * (Double.parseDouble(datosPartidas[3])));
            datosPartidas[5]= (String.format("%.2f",Double.parseDouble(datosPartidas[5])));
            datosPartidas[6]= tblInvCueCrudo.getValueAt(fila, 6).toString();

            dtms.addRow(datosPartidas);
            totalPiezasPartida();
            asignados[fila][0] = "1";
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
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
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
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
                        
                        datosPD[i].setNoPiezas(Integer.parseInt(tblPartida.getValueAt(i, 4).toString()));
                        datosPD[i].setIdPartida(Integer.parseInt(txtNoPartida.getText()));
                        datosPD[i].setIdTipoRecorte(1);
                        
                        
                        datosPar[i][0] = tblPartida.getValueAt(i, 1).toString();
                        datosPar[i][1] = tblPartida.getValueAt(i, 0).toString();
                        datosPar[i][2] = tblPartida.getValueAt(i, 6).toString();
                        datosPar[i][3] = tblPartida.getValueAt(i, 4).toString();
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
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tblPartidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPartidaKeyTyped
        
    }//GEN-LAST:event_tblPartidaKeyTyped

    private void tblPartidaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPartidaKeyReleased
        int fila = tblPartida.getSelectedRow();
        String piezasUtilizar = tblPartida.getValueAt(fila, 4).toString();
        String piezas = tblPartida.getValueAt(fila, 2).toString();
        String kgCalc = tblPartida.getValueAt(fila, 3).toString();
        
        try
        {
            if (Integer.parseInt(piezasUtilizar) < 0)
            {
                JOptionPane.showMessageDialog(null, "Solo puede insertar numeros enteros mayores o igual a 0");
                tblPartida.setValueAt("0", fila, 4);
            }
            else
            {
                if (Integer.parseInt(piezasUtilizar) > Integer.parseInt(piezas))
                {
                    JOptionPane.showMessageDialog(null, "El numero de piezas a utilizar no puede ser mayor al numero de piezas");
                    tblPartida.setValueAt("0", fila, 4);
                }
                else
                {
                    String kgTotales = String.valueOf((Integer.parseInt(piezasUtilizar)) * (Double.parseDouble(kgCalc)));
                    kgTotales = (String.format("%.2f",Double.parseDouble(kgTotales)));
                    tblPartida.setValueAt(kgTotales, fila, 5);
                    totalPiezasPartida();
                }
            }
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Solo puede insertar numeros enteros mayores o igual a 0");
            tblPartida.setValueAt("0", fila, 4);
        }
    }//GEN-LAST:event_tblPartidaKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblInvCueCrudo;
    private javax.swing.JTable tblPartida;
    private javax.swing.JTextField txtKgTotal;
    private javax.swing.JTextField txtNoPartida;
    private javax.swing.JTextField txtTotalPiezas;
    // End of variables declaration//GEN-END:variables
}
