/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author FLAVIO
 */
public class Notificacion {
    private int modulo;
    private int id;
    private int estado;

    public Notificacion(int modulo, int id, int estado) {
        this.modulo = modulo;
        this.id = id;
        this.estado = estado;
    }

    public int getModulo() {
        return modulo;
    }

    public void setModulo(int modulo) {
        this.modulo = modulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return getModulo() + "-" + getId() + "-" + getEstado() ;
    }
    
    
}
