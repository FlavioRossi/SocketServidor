/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.io.IOException;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.Cliente;

/**
 *
 * @author FLAVIO
 */
public class Servidor {
    private static final Logger LOG = Logger.getLogger(Servidor.class.getName());
   
    private final BooleanProperty ESTADO_SERVER;
    private final ObservableList<Cliente> CLIENTES;
   
    private final Thread T_RECIBE_CLIENTES;
    
    private Thread tBuscaNotificaciones;
    
    public Servidor(int puerto) throws IOException {
        ESTADO_SERVER = new SimpleBooleanProperty(false);
        CLIENTES = FXCollections.observableArrayList();
        
        T_RECIBE_CLIENTES = new Thread(new RRecibeClientes(CLIENTES, puerto));
    }

    public ReadOnlyBooleanProperty getESTADO_SERVERProperty() {
        return ESTADO_SERVER;
    }
    
    public ObservableList<Cliente> getCLIENTES() {
        return FXCollections.unmodifiableObservableList(CLIENTES);
    }
    
    public void startServer() throws IOException{
        tBuscaNotificaciones = new Thread(new RBuscaNotificaciones(CLIENTES));
        
        T_RECIBE_CLIENTES.start();
        tBuscaNotificaciones.start();
        
        ESTADO_SERVER.set(true);
    }
    
    public void stopServer() throws InterruptedException{
        T_RECIBE_CLIENTES.interrupt();
        T_RECIBE_CLIENTES.join();
        tBuscaNotificaciones.interrupt();
        tBuscaNotificaciones.join();
        
        ESTADO_SERVER.set(false);
    }
}
