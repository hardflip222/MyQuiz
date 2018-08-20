package sample.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import sample.model.DataSource;
import sample.model.Flag;

import java.io.IOException;
import java.util.Optional;


public class DeleteFlagController
{

    @FXML
    TableView<Flag> flagsTable;

    @FXML
    AnchorPane window;

    @FXML
    Button exitButton;

    @FXML
    Button saveChangesButton;

    boolean changes = false;

    ObservableList<Flag> observableListFlag;

    public void initialize()
    {
        observableListFlag = FXCollections.observableArrayList(DataSource.getInstance().getAllFlags());
        flagsTable.itemsProperty().setValue(observableListFlag);
    }

    @FXML
    public void deleteFlag()
    {
        Flag f = flagsTable.getSelectionModel().getSelectedItem();
        if(f != null) {
            observableListFlag.removeAll(f);
            changes = true;
            exitButton.setVisible(false);
            saveChangesButton.setVisible(true);

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete from database");
            alert.setHeaderText("Information");
            alert.setContentText("You have to select some element!");

            alert.showAndWait();

        }
    }

    @FXML
    public void saveChanges() {

        if (changes) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save changes to database");
            alert.setHeaderText("Confirmation");
            alert.setContentText("Are you sure? ");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                //saving to database
                if (DataSource.getInstance().getAmountOfFlags() > 0)
                    DataSource.getInstance().saveToDatabase(this.observableListFlag);
            }


        }


        Parent newView = null;
        try {
            newView = FXMLLoader.load(getClass().getResource("../Views/mainView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        window.getChildren().setAll(newView);


    }



}


