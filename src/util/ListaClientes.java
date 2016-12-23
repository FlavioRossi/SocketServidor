/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.Cliente;

/**
 *
 * @author FLAVIO
 */
public class ListaClientes{
    private static ListaClientes listaClientes;
    
    private ObservableList<Cliente> CLIENTES = null;

    private ListaClientes() {
        CLIENTES = FXCollections.observableArrayList();
    }
    
    public static ListaClientes getInstancia(){
        if (listaClientes == null) {
            listaClientes = new ListaClientes();
        }
        return listaClientes;
    }
    
    public ObservableList<Cliente> getClientes(){ 
        return CLIENTES;
    }
    
    public boolean addCliente(Cliente cliente){
        return CLIENTES.add(cliente);
    }
    
    public boolean removeCliente(Cliente cliente){
        return CLIENTES.remove(cliente);
    }
    
    public Cliente getClienteByNombre(String nombre){
        for (Cliente cliente : CLIENTES) {
            if (nombre.equals(cliente.getNombre())) {
                return cliente;
            }
        }
        return null;
    }
    
    public Cliente getClienteById(int id){
        for (Cliente cliente : CLIENTES) {
            if (id == cliente.getId()) {
                return cliente;
            }
        }
        return null;
    }
}
