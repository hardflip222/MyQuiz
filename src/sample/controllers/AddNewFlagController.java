package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.model.DataSource;

import java.io.IOException;

public class AddNewFlagController
{

    @FXML
    private AnchorPane window;

    @FXML
    private TextField nationTextField;

    public void initialize()
    {

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

    @FXML
    public void addNewFlag()
    {
        if(nationTextField.getText().trim().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Adding to database");
            alert.setHeaderText("Error");
            alert.setContentText("You have not entered name");

            alert.showAndWait();
        }
        else {
            String nation = nationTextField.getText();
            boolean flagExists = DataSource.getInstance().isFlagExists(nation);

            if(flagExists)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Adding to database");
                alert.setHeaderText("Error");
                alert.setContentText("You have entered name of the existing flag");

                alert.showAndWait();
            }
            else
            {
                DataSource.getInstance().addNewFlag(nation);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Adding to database");
                alert.setHeaderText("Flag Added");
                alert.setContentText("The flag was added!!!");

                alert.showAndWait();
                nationTextField.setText("");

            }
        }

    }

}
