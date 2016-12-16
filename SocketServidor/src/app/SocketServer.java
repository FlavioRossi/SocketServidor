/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import controlador.FXML_appController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author FLAVIO
 */
public class SocketServer extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/FXML_app.fxml"));
        Parent fxml = fxmlLoader.load();
        FXML_appController servidor = (FXML_appController) fxmlLoader.getController();
        
        Scene scene = new Scene(fxml);
        stage.setScene(scene);
        stage.setTitle("Socket servidor - Organización Informática");
        stage.show();
        
        stage.setOnCloseRequest((WindowEvent event) -> {
            servidor.getServidor().stopServer();
            System.exit(0);
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
