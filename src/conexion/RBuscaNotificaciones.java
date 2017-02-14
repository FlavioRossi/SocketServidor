/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Cliente;
import modelo.Notificacion;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.Cobol;

/**
 *
 * @author FLAVIO
 */
public class RBuscaNotificaciones implements Runnable {
    private static final Logger LOG = Logger.getLogger(RBuscaNotificaciones.class.getName());
    
    private final JSONParser PARSE;
    private final ObservableList<Cliente> CLIENTES;
    
    //tiempo de espera en segundos para volver a realizar la consulta 
    private final int TIEMPO_DE_CONSULTA = 3;
    
    private List<Notificacion> marcar;
    
    public RBuscaNotificaciones(ObservableList<Cliente> clientes) {
        PARSE = new JSONParser();
        CLIENTES = clientes;
    }
    
    @Override
    public void run() {
        marcar = new ArrayList<>();
        
        while (!Thread.interrupted()) {
            try {
                marcar.clear();
                File file = Cobol.buscaNotificaciones();
                if (file != null) {
                    try (FileReader fileRead = new FileReader(file)) {
                        JSONObject json = (JSONObject) PARSE.parse(fileRead);
                        for (Object key : json.keySet()) {
                            String usuario = (String) key;
                            JSONObject mensajes = (JSONObject) json.get(usuario);
                            try {
                                if (CLIENTES.size() < 1) {
                                    break;
                                }
                                Cliente cliente = CLIENTES
                                        .stream()
                                        .filter(e -> e.getNombre().equals(usuario) && e.getEstado().get())
                                        .findFirst()
                                        .orElse(null);
                                
                                if (cliente != null) {
                                    if (!cliente.enviar(2, mensajes)) {
                                        cliente.setEstado(false);
                                        break;
                                    }else{
                                    agregaNotificacion(mensajes, 2); //marca como recibido
                                    }
                                }else{
                                    agregaNotificacion(mensajes, 1); //marca como enviado
                                }
                            } catch (IndexOutOfBoundsException | NullPointerException ex) {
                                LOG.log(Level.INFO, "Lista de clientes vacia", ex);
                                agregaNotificacion(mensajes, 1); //marca como enviado
                            }      
                        }
                    }catch(ParseException exParseJson){
                        LOG.log(Level.INFO, "Formato json erroneo", exParseJson);
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, "Error al leer el archivo", ex);
                    }
                }else{
                    System.out.println("Error al ejecutar consulta: buscar notificación");
                }

                if (!marcar.isEmpty()) Cobol.marcaNotificacion(marcar); //envio las notificaciones a APOLO
                Thread.sleep(TIEMPO_DE_CONSULTA * 1000);
            } catch (InterruptedException e) {
                System.out.println("El hilo de notificaciones fue interrumpido");
            } catch (IOException ex) {
                Logger.getLogger(RBuscaNotificaciones.class.getName()).log(Level.SEVERE, null, ex);
            }
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
