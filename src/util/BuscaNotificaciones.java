/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import modelo.Cliente;
import modelo.Notificacion;
import modelo.Servidor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author FLAVIO
 */
public class BuscaNotificaciones extends Task {
    private final JSONParser PARSE;
    private final ListaClientes CLIENTES;
    
    //tiempo de espera en segundos para volver a realizar la consulta 
    private final int TIEMPO_DE_CONSULTA = 3;
    
    /**
     * estado de las notifiaciones que se envian a APOLO(cobol) para marcar su estado
     */
    private List<Notificacion> marcar;
    
    public BuscaNotificaciones(Servidor servidor) {
        PARSE = new JSONParser();
        CLIENTES = ListaClientes.getInstancia();
    }
    
    @Override
    protected Object call() throws Exception {
        int cont = 0;
        while (true) {
            marcar = new ArrayList<>();
            
            cont++;
            System.out.println(cont);
            
            File file = Cobol.buscaNotificaciones();
            if (file != null) {
                try (FileReader fileRead = new FileReader(file)) {
                    JSONObject json = (JSONObject) PARSE.parse(fileRead);
                    for (Object key : json.keySet()) {
                        String usuario = (String) key;
                        
                        Cliente cliente = CLIENTES.getClienteByNombre(usuario);
                        JSONObject mensajes = (JSONObject) json.get(usuario);
                        
                        if (cliente != null && cliente.getId() > 0) {
                            if (!cliente.enviar(2, mensajes)) {
                                Platform.runLater(() -> {
                                    CLIENTES.removeCliente(cliente);
                                });
                                break;
                            }else{
                                //marca como recibido
                                agregaNotificacion(mensajes, 2);
                            }
                        }else{
                            //marca como enviados
                            agregaNotificacion(mensajes, 1);
                        }
                    }
                }catch(ParseException exParseJson){
                    System.out.println("Formato json erroneo");
                }
            }else{
                System.out.println("Error al ejecutar consulta: buscar notificación");
            }
            //envio las notificaciones a APOLO
            if (!marcar.isEmpty()) Cobol.marcaNotificacion(marcar);
            Thread.sleep(TIEMPO_DE_CONSULTA * 1000);
        }
    }
    
    private void agregaNotificacion(JSONObject mensajes, int estado){
        for (Object modulos : mensajes.keySet()) {
            String strModulo = (String) modulos;
            JSONObject jsonItems = (JSONObject) mensajes.get(strModulo);
            for (Object ids : jsonItems.keySet()) {
                int modulo = Integer.valueOf(strModulo);
                int id = Integer.valueOf(((String) ids));
                
                JSONObject jsonItem = (JSONObject) jsonItems.get("" + id);
                int notEstado = Integer.valueOf(((String) jsonItem.get("estado")));
                
                if (notEstado < estado) marcar.add(new Notificacion(modulo, id, estado));
            }
        }
    }
}
