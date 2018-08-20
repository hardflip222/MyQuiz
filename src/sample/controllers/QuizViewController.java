package sample.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import sample.model.DataSource;
import sample.model.Question;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class QuizViewController
{

    @FXML
    private Button answer1;
    @FXML
    private Button answer2;
    @FXML
    private Button answer3;
    @FXML
    private Button answer4;
    @FXML
    private Button answer5;
    @FXML
    private Button answer6;

    @FXML
    private ImageView flagImage;

    @FXML
    private AnchorPane window;

    @FXML
    private Label correctLabel;

    @FXML
    private Label highScoreLabel;

    private Question question;

    private int coorectAnswers;

    private int highScore;

    public void initialize()
    {

        highScore = DataSource.getInstance().getHighScore();
        highScoreLabel.setText("High score: "+highScore);
        coorectAnswers = 0;
        correctLabel.setText(correctLabel.getText()+" "+coorectAnswers);
        loadQuestion();
    }

    public void loadQuestion()
    {
        Image image;
        try {
            question =  DataSource.getInstance().getRandomQuestion();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try(InputStream input = new FileInputStream("src/images/flags/"+question.getCorrectFlag().getNation().toLowerCase()+".png"))
        {


            image = new Image(input);
            flagImage.setImage(image);

        } catch (FileNotFoundException e) {
            try {
                image = new Image(new FileInputStream("src/images/flags/temp.png"));
                flagImage.setImage(image);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        int randCorrectButton = (int)(Math.random()*6.0) +1;
        int index = 0;



        for(int i =0;i<6;i++)
        {
            if(i+1==randCorrectButton) {
                if(randCorrectButton == 1)
                    answer1.setText(question.getCorrectFlag().getNation());
                else if(randCorrectButton ==2)
                    answer2.setText(question.getCorrectFlag().getNation());
                else if(randCorrectButton ==3)
                    answer3.setText(question.getCorrectFlag().getNation());
                else if(randCorrectButton ==4)
                    answer4.setText(question.getCorrectFlag().getNation());
                else if(randCorrectButton ==5)
                    answer5.setText(question.getCorrectFlag().getNation());
                else
                    answer6.setText(question.getCorrectFlag().getNation());


            }
            else
            {
                switch(i)
                {
                    case 0: answer1.setText(question.getFlags().get(index).getNation()); break;
                    case 1: answer2.setText(question.getFlags().get(index).getNation()); break;
                    case 2: answer3.setText(question.getFlags().get(index).getNation()); break;
                    case 3: answer4.setText(question.getFlags().get(index).getNation()); break;
                    case 4: answer5.setText(question.getFlags().get(index).getNation()); break;
                    case 5: answer6.setText(question.getFlags().get(index).getNation()); break;
                }
                index++;

            }

        }


    }


    @FXML
    public void clickAnswerButton(ActionEvent event)
    {


         if( ((Button)event.getSource()).getText().equals(question.getCorrectFlag().getNation()) )
         {
             coorectAnswers++;
             loadQuestion();
             updateCorrectAnswerLabel();
         }
         else
         {
             boolean isNewHighScore = false;
             if(coorectAnswers>DataSource.getInstance().getHighScore()) {
                 DataSource.getInstance().setHighScore(coorectAnswers);
                 isNewHighScore = true;
             }

             Alert alert = new Alert(Alert.AlertType.INFORMATION);
             alert.setTitle("You lose!!!");
             alert.setHeaderText("Information");

             if(isNewHighScore)
                 alert.setContentText("GAME OVER, but you set a new record!!!");
             else
                 alert.setContentText("GAME OVER ");

             Optional<ButtonType> result =  alert.showAndWait();

             if(result.isPresent() && result.get() == ButtonType.OK)
             {
                 loadStartMenu();
             }


         }

    }

    @FXML
    public void goBack()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Go back to main menu");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure? Changes will not be saved in the database!");

        Optional<ButtonType> result =  alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            Parent newView = null;
            try {
                newView = FXMLLoader.load(getClass().getResource("../Views/mainView.fxml"));
                window.getChildren().setAll(newView);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateCorrectAnswerLabel()
    {
        correctLabel.setText("Correct answers :"+coorectAnswers);
    }

    public void loadStartMenu(){
        Parent newView = null;
        try {
            newView = FXMLLoader.load(getClass().getResource("../Views/mainView.fxml"));
            window.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
