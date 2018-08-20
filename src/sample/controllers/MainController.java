package sample.controllers;



import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import sample.model.DataSource;


import java.io.IOException;
import java.util.Optional;


public class MainController {


    @FXML
    private AnchorPane window;


    @FXML
    public void closeApp()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close App");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure? ");

        Optional<ButtonType> result =  alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            Platform.exit();
        }

    }

    @FXML
    public void startQuiz() throws IOException {

        int amountFlag = DataSource.getInstance().getAmountOfFlags();
        if(amountFlag<6)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not enough flags in database. !");
            alert.setHeaderText("ERROR");
            alert.setContentText("In database are "+amountFlag+". Program needs at lest 6 flags!");

            alert.showAndWait();


        }
        else {
            Parent newView = FXMLLoader.load(getClass().getResource("../Views/quizView.fxml"));
            window.getChildren().setAll(newView);
        }


    }

    @FXML
    public void addNewElement() throws IOException {

        Parent newView = FXMLLoader.load(getClass().getResource("../Views/addNewFlagView.fxml"));
        window.getChildren().setAll(newView);
    }


    @FXML
    public void deleteElement() throws IOException {

        Parent newView = FXMLLoader.load(getClass().getResource("../Views/deleteFlagView.fxml"));
        window.getChildren().setAll(newView);

    }

    @FXML
    public void updateElement() throws IOException{

        Parent newView = FXMLLoader.load(getClass().getResource("../Views/updateFlagView.fxml"));
        window.getChildren().setAll(newView);

    }



}
