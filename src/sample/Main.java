package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.model.DataSource;

import javax.xml.crypto.Data;


public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("Views/mainView.fxml"));
        primaryStage.setTitle("FLAG QUIZ");
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.setResizable(false);
        primaryStage.show();

    }



    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void init() throws Exception {
        if(!DataSource.getInstance().open())
            Platform.exit();

    }

    @Override
    public void stop() throws Exception {
        DataSource.getInstance().close();

    }
}
