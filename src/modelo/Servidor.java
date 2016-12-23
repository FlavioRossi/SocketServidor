/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.IOException;
import java.net.ServerSocket;
import javafx.concurrent.Task;
import util.BuscaNotificaciones;
import util.Status;

/**
 *
 * @author FLAVIO
 */
public final class Servidor {
    private final ServerSocket SERVER_SOCKET;
    
    private final Thread BUSCA_CLIENTES;
    private final Thread BUSCA_NOTIFICACIONES;
    
    public Servidor() throws IOException {
        SERVER_SOCKET = new ServerSocket(Status.PUERTO_SOCKET);
        
        BUSCA_CLIENTES = new Thread(new Task() {
            @Override
            protected Object call() throws Exception {
                int idCliente = 1;
                while(true){
                    if (isCancelled()) {
                        break;
                    }
                    Cliente cliente = new Cliente(idCliente++, SERVER_SOCKET.accept());
                    cliente.startCliente();
                }
                return null;
            }
        });
        BUSCA_NOTIFICACIONES = new Thread(new BuscaNotificaciones(this));
    }

    public void startServer(){
        BUSCA_CLIENTES.start();
        BUSCA_NOTIFICACIONES.start();
    }
    public void stopServer(){
        BUSCA_CLIENTES.interrupt();
        BUSCA_NOTIFICACIONES.interrupt();
    }
    public boolean isRunning(){
        return !BUSCA_CLIENTES.isInterrupted();
    }
}
