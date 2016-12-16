/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import modelo.Cliente;
import modelo.Servidor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author FLAVIO
 */
public class BuscaNotificaciones extends Task {
    private final Servidor SERVIDOR;
    private final JSONParser PARSE;
    private final File FILE = new File("Z:\\java\\SocketServidor\\src\\modelo\\notificaciones.json");
    private final ListaClientes CLIENTES;
    
    public BuscaNotificaciones(Servidor servidor) {
        SERVIDOR = servidor;
        PARSE = new JSONParser();
        CLIENTES = ListaClientes.getInstancia();
    }
    
    @Override
    protected Object call() throws Exception {
        while (true) {
            JSONObject json = (JSONObject) PARSE.parse(new FileReader(FILE));
            for (Object key : json.keySet()) {
                String usuario = (String) key;
                Cliente cliente = CLIENTES.getClienteByNombre(usuario);
                if (cliente != null && cliente.getId() > 0) {
                    JSONObject mensajes = (JSONObject) json.get(usuario);
                    for (Object obj2 : mensajes.keySet()) {
                        String idMsj = (String) obj2;
                        JSONObject msj = (JSONObject) mensajes.get(idMsj);
                        if (!cliente.enviar(2, msj)) {
                            Platform.runLater(() -> {
                                CLIENTES.removeCliente(cliente);
                            });
                            break;
                        }
                    }
                }
            }
            Thread.sleep(5000);
        }

    }
}
