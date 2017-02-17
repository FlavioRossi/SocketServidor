/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.io.IOException;
import java.net.ServerSocket;
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
   
    private final ServerSocket SERVER_SOCKET;
    private final BooleanProperty ESTADO_SERVER;
    private final ObservableList<Cliente> CLIENTES;
   
    private Thread tRecibeClientes;
    private Thread tBuscaNotificaciones;
    
    public Servidor(int puerto) throws IOException {
        ESTADO_SERVER = new SimpleBooleanProperty(false);
        CLIENTES = FXCollections.observableArrayList();
        SERVER_SOCKET = new ServerSocket(puerto);
        SERVER_SOCKET.setSoTimeout(1000);
    }

    public ReadOnlyBooleanProperty getESTADO_SERVERProperty() {
        return ESTADO_SERVER;
    }
    
    public ObservableList<Cliente> getCLIENTES() {
        return CLIENTES;
    }
    
    public void startServer() throws IOException{
        tRecibeClientes = new Thread(new RRecibeClientes(CLIENTES, SERVER_SOCKET));
        tRecibeClientes.start();
        
        tBuscaNotificaciones = new Thread(new RBuscaNotificaciones(CLIENTES));
        tBuscaNotificaciones.start();
        
        ESTADO_SERVER.set(true);
    }
    
    public void stopServer() throws InterruptedException{
        tRecibeClientes.interrupt();
        tRecibeClientes.join();
        
        tBuscaNotificaciones.interrupt();
        tBuscaNotificaciones.join();
        
        ESTADO_SERVER.set(false);
    }
}
