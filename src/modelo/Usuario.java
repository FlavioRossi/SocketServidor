/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.time.LocalDateTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author FLAVIO
 */
public class Usuario{
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty clave;
    private LocalDateTime ingreso;
    private LocalDateTime salida;
    private final BooleanProperty notificaciones;
    
    public Usuario(int id) {
        this.id = new SimpleIntegerProperty(id);
        nombre = new SimpleStringProperty();
        clave = new SimpleStringProperty();
        notificaciones = new SimpleBooleanProperty(true);
    }

    public int getId() {
        return id.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getClave() {
        return clave.get();
    }

    public void setClave(String clave) {
        this.clave.set(clave);
    }
    
    public LocalDateTime getIngreso() {
        return ingreso;
    }

    public void setIngreso(LocalDateTime ingreso) {
        this.ingreso = ingreso;
    }

    public LocalDateTime getSalida() {
        return salida;
    }

    public void setSalida(LocalDateTime salida) {
        this.salida = salida;
    }

    public boolean isNotificaciones() {
        return notificaciones.get();
    }

    public void setNotificaciones(boolean notificaciones) {
        this.notificaciones.set(notificaciones);
    }
    
    
}
