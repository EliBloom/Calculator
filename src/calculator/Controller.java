package calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class Controller {
  //  private String result = "";
    private String userInput = "";
    private String Answer = "";
    @FXML
    private TextField textBox;
    @FXML
    private Label resultBox;

    public void handleEnterButton(ActionEvent actionEvent) {

        this.userInput += textBox.getText();
        InputParser parsedText = new InputParser(this.userInput);
        parsedText.setCalculation();
        Answer += parsedText.getCalculation();
        resultBox.setText(parsedText.getCalculation());
        textBox.setText("");
        this.userInput= "";
    }

    public void handleOperatorButtons(ActionEvent actionEvent) {
        HashSet standardButtons = new HashSet(Arrays.asList("+", "-", "/", "*", "%", "^", "!", "(", ")", ".", "log", "ln", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        String buttonValue = ((Button) actionEvent.getSource()).getText();
        resultBox.setText("");
        switch (buttonValue) {
            case ("del"):
                String boxText = textBox.getText();
                String holder = "";
                if (boxText.length() > 0) {
                    holder = boxText.substring(0, boxText.length() - 1);
                }
                textBox.setText(holder);
                break;

            case ("clear"):
                this.userInput = "";
                textBox.setText("");
                break;

            case "\uD835\uDF0B":
                textBox.appendText("pi");
                break;
            case ("√"):
                textBox.appendText("sqrt(");
                break;
            case ("cos"):
                textBox.appendText("cos(");
                break;
            case ("sin"):
                textBox.appendText("sin(");
                break;
            case ("tan"):
                textBox.appendText("tan(");
                break;
            case ("arccos"):
                textBox.appendText("arccos(");
                break;
            case ("arcsin"):
                textBox.appendText("arcsin(");
                break;
            case ("arctan"):
                textBox.appendText("arctan(");
                break;

            case ("Ans"):
                textBox.appendText(this.Answer);
                break;
        }
        if (standardButtons.contains(buttonValue))
            textBox.appendText(buttonValue);
    }
}
