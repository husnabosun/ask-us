package com.example.karakamyonculars;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class ContactWindowController {

    public static void display(){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ASK HUSNA");
        window.setMinWidth(400);

        try{
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(ResultsController.class.getResource("/contactWindow.fxml")));
            Parent root = loader.load();
            ResultsController controller = loader.getController();
            window.setScene(new Scene(root));
            window.show();
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
