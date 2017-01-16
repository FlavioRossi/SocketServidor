
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.DataInputStream;
import java.time.LocalDateTime;
import javafx.concurrent.Task;
import modelo.Cliente;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author FLAVIO
 */
public class ClienteRespuestas extends Task{
    private final Cliente cliente;
    private final DataInputStream input;
    private final ListaClientes CLIENTES;
    
    public ClienteRespuestas(Cliente cliente, DataInputStream input) {
        this.cliente = cliente;
        this.input = input;
        CLIENTES = ListaClientes.getInstancia();
    }

    @Override
    protected Object call() throws Exception {
        while(true){
            String mensaje = input.readUTF();
            JSONObject respond = (JSONObject) new JSONParser().parse(mensaje);
            System.out.println("Llego del cliente " + mensaje);
            responder(respond);
        }
    }
    
    public void responder(JSONObject json){
        int valor = Integer.parseInt(json.get("parametro").toString());
        JSONObject resul = (JSONObject) json.get("resul");
        
        switch(valor){
            case 0:
                //pulso de vida (verifica conexion de socket)
                
            case 1:
                //logueo usuario
                registroCliente(resul);
                break;
            case 2:
                //envia notificaciones
                break;
        }
    }
    
    private void registroCliente(JSONObject resul){
        String nombre = (String) resul.get("usuario");
        String clave = (String) resul.get("clave");

        JSONObject respond = new JSONObject();
        if ((nombre.equals("FLAVIO") && clave.equals("FNR")) || (nombre.equals("FRANCOR") && clave.equals("JFR"))) {
            cliente.setNombre(nombre);
            cliente.setClave(clave);
            cliente.setIngreso(LocalDateTime.now());
            CLIENTES.addCliente(cliente);

            respond.put("logueo", "ok");
            respond.put("id", cliente.getId());
            respond.put("nombre", cliente.getNombre());
            respond.put("ingreso", cliente.getIngreso().format(Status.FORMATO_FECHA_HORA));
        }else{
            respond.put("logueo", "cancel");
        }
        cliente.enviar(1, respond);
    }
    
    
}
