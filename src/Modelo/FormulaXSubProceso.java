/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Equipo
 */
public class FormulaXSubProceso {
    int idFormXSubproc;
    int idSubproceso;
    String fechaCreacion;

    public int getIdFormXSubproc() {
        return idFormXSubproc;
    }

    public void setIdFormXSubproc(int idFormXSubproc) {
        this.idFormXSubproc = idFormXSubproc;
    }

    public int getIdSubproceso() {
        return idSubproceso;
    }

    public void setIdSubproceso(int idSubproceso) {
        this.idSubproceso = idSubproceso;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
