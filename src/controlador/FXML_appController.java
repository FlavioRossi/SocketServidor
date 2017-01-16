/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import modelo.Cliente;
import modelo.Servidor;
import util.ListaClientes;
import util.Status;

/**
 * FXML Controller class
 *
 * @author FLAVIO
 */
public class FXML_appController implements Initializable {

    /**
     * Instancia que abre el puerto del servidor
     */
    private Servidor servidor;

    @FXML
    private Label lbl_ip;
    @FXML
    private JFXTextField txt_puerto;
    @FXML
    private JFXTreeTableView<AdapterClientes> jfxTreeTableView_clientes;
    @FXML
    private JFXButton btn_ejecutar;

    /**
     * Devuelve instancia del servidor
     *
     * @return
     */
    public Servidor getServidor() {
        return servidor;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            servidor = new Servidor();

            JFXTreeTableColumn<AdapterClientes, Number> column_id = new JFXTreeTableColumn<>("ID");
            column_id.setPrefWidth(50);
            column_id.setCellValueFactory((TreeTableColumn.CellDataFeatures<AdapterClientes, Number> param) -> 
                param.getValue().getValue().id
            );
            
            JFXTreeTableColumn<AdapterClientes, String> column_nombre = new JFXTreeTableColumn<>("Cliente");
            column_nombre.setPrefWidth(150);
            column_nombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<AdapterClientes, String> param) ->
                param.getValue().getValue().nombre
            );

            JFXTreeTableColumn<AdapterClientes, String> column_clave = new JFXTreeTableColumn<>("Clave");
            column_clave.setPrefWidth(150);
            column_clave.setCellValueFactory((TreeTableColumn.CellDataFeatures<AdapterClientes, String> param) ->
                param.getValue().getValue().clave
            );            
            
            JFXTreeTableColumn<AdapterClientes, String> column_fechaHora = new JFXTreeTableColumn<>("Fecha y Hora de Ingreso");
            column_fechaHora.setPrefWidth(150);
            column_fechaHora.setCellValueFactory((TreeTableColumn.CellDataFeatures<AdapterClientes, String> param) ->
                param.getValue().getValue().fechaHoraIngreso
            );  

            JFXTreeTableColumn<AdapterClientes, Boolean> column_notifica = new JFXTreeTableColumn<>("Notificaciones");
            column_notifica.setPrefWidth(150);
            column_notifica.setCellValueFactory((TreeTableColumn.CellDataFeatures<AdapterClientes, Boolean> param) ->
                param.getValue().getValue().notificaciones
            );  
            
            ObservableList<AdapterClientes> adapterClientes = FXCollections.observableArrayList();
            
            ListaClientes clientes = ListaClientes.getInstancia();
            clientes.getClientes().addListener((ListChangeListener.Change<? extends Cliente> c) -> {
                while(c.next()){
                    if (c.wasAdded()) {
                        for (Cliente cliente : c.getAddedSubList()) {
                            AdapterClientes nuevo = new AdapterClientes(
                                    cliente.getId(),
                                    cliente.getNombre(),
                                    cliente.getClave(),
                                    cliente.getIngreso().format(Status.FORMATO_FECHA_HORA),
                                    false);
                            adapterClientes.add(nuevo);
                        }
                    }else if(c.wasRemoved()){
                        for (Cliente cliente : c.getRemoved()) {
                            for (AdapterClientes adapterCliente : adapterClientes) {
                                if (adapterCliente.getId().get() == cliente.getId()) {
                                    adapterClientes.remove(adapterCliente);
                                }
                            }
                        }
                    }
                }
            });
            
            final TreeItem<AdapterClientes> itemView = new RecursiveTreeItem<>(adapterClientes, RecursiveTreeObject::getChildren);
            jfxTreeTableView_clientes.setRoot(itemView);
            jfxTreeTableView_clientes.getColumns().setAll(column_id, column_nombre, column_clave, column_fechaHora, column_notifica);
            jfxTreeTableView_clientes.setShowRoot(false);
        } catch (IOException ex) {
            Logger.getLogger(FXML_appController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ejecutarServer(ActionEvent event) throws IOException {
        if (servidor.isRunning()) {
            servidor.startServer();
            btn_ejecutar.setText("Conectado");
        } else {
            servidor.stopServer();
            btn_ejecutar.setText("Conectar");
        }
    }

    /**
     * Clase adapter para tabla de usuarios conectados
     */
    private class AdapterClientes extends RecursiveTreeObject<AdapterClientes> {

        private final IntegerProperty id;
        private final StringProperty nombre;
        private final StringProperty clave;
        private final StringProperty fechaHoraIngreso;
        private final BooleanProperty notificaciones;

        public AdapterClientes(int id, String nombre, String clave, String fechaHora, boolean notificaciones) {
            this.id = new SimpleIntegerProperty(id);
            this.nombre = new SimpleStringProperty(nombre);
            this.clave = new SimpleStringProperty(clave);
            this.fechaHoraIngreso = new SimpleStringProperty(fechaHora);
            this.notificaciones = new SimpleBooleanProperty(notificaciones);
        }

        public IntegerProperty getId() {
            return id;
        }

        public StringProperty getNombre() {
            return nombre;
        }

        public StringProperty getClave() {
            return clave;
        }

        public StringProperty getFechaHoraIngreso() {
            return fechaHoraIngreso;
        }

        public BooleanProperty getNotificaciones() {
            return notificaciones;
        }
        
        
        
    }
}
