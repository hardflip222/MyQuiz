package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import sample.model.DataSource;
import sample.model.Flag;

import java.io.IOException;
import java.util.Optional;

public class UpdateFlagViewController
{

    @FXML
    private AnchorPane window;

    @FXML
    TableView<Flag> flagsTable;

    ObservableList<Flag> observableListFlag;

    public void initialize()
    {
        refresh();
    }

    @FXML
    public void goBack()
    {
        Parent newView = null;
        try {
            newView = FXMLLoader.load(getClass().getResource("../Views/mainView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        window.getChildren().setAll(newView);

    }


    public void update()
    {
        Flag updateFlag = flagsTable.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Update");
        dialog.setHeaderText("Enter update nation:");
        dialog.setContentText("Nation:");

        Optional<String> result = dialog.showAndWait();


        if(result.isPresent())
        {
            if(!result.get().trim().isEmpty())
            {
                result.ifPresent(nation->DataSource.getInstance().updateNation(updateFlag.getNation(),nation));
                refresh();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERROR");
                alert.setHeaderText("Empty field");
                alert.setContentText("The nation name field is empty!!!");

                alert.showAndWait();
            }
        }





    }

    private void refresh()
    {
        observableListFlag = FXCollections.observableArrayList(DataSource.getInstance().getAllFlags());
        flagsTable.itemsProperty().setValue(observableListFlag);
    }
}
