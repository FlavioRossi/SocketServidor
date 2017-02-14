/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import modelo.Cliente;

/**
 *
 * @author FLAVIO
 */
public class RRecibeClientes implements Runnable {

    private static final Logger LOG = Logger.getLogger(RRecibeClientes.class.getName());
    
    private final ServerSocket SERVER_SOCKET;
    private final ObservableList<Cliente> CLIENTES;
    
    public RRecibeClientes(ObservableList<Cliente> clientes, int puerto) throws IOException {
        CLIENTES = clientes;
        SERVER_SOCKET = new ServerSocket(puerto);
        SERVER_SOCKET.setSoTimeout(1000);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Cliente cliente;
            try {
                cliente = new Cliente(CLIENTES.size() + 1, SERVER_SOCKET.accept());
                CLIENTES.add(cliente);
                cliente.startCliente();
            } catch (SocketTimeoutException ex) {
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Error de conexión", ex);
            }
        }
        try {
            SERVER_SOCKET.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error al cerrar servidor", ex);
        }
    }
    
}
