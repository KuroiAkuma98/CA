package sample;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Button button;
    @FXML
    Label pbcLabel;
    @FXML
    ComboBox<String> pbcComboBox;
    @FXML
    Label ruleLabel;
    @FXML
    ComboBox<String> ruleComboBox;
    @FXML
    Label widthLabel;
    @FXML
    TextField widthTextField;
    @FXML
    Label iterationsLabel;
    @FXML
    TextField iterationsTextField;
    @FXML
    Canvas canvas;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pbcComboBox.setItems(FXCollections.observableArrayList("Still","Rounded","Bounced"));
        pbcComboBox.setValue("Still");
        ruleComboBox.setItems(FXCollections.observableArrayList("30","60","90","120","225"));
        ruleComboBox.setValue("90");
        widthTextField.setText("150");
        iterationsTextField.setText("100");

        widthTextField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                widthTextField.clear();
            }
        });
        iterationsTextField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                iterationsTextField.clear();
            }
        });

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0,0,870,530);
    }
    public void buttonClicked(){
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0,0,870,530);

        String PBC = pbcComboBox.getValue();
        int rule = Integer.parseInt(ruleComboBox.getValue());
        int width = Integer.parseInt(widthTextField.getText());
        int iterations = Integer.parseInt(iterationsTextField.getText());

        String ruleString = Integer.toBinaryString(rule);
        while(ruleString.length() < 8) {
            ruleString = "0" + ruleString;
        }
        int []ruleSet = new int[8];
        for (int i = 0; i < 8; i++) {
            if(ruleString.charAt(i) == '1') ruleSet[7 - i] = 1;
            else ruleSet[7 - i] = 0;
        }

        int []currentState = new int[width];
        currentState[width/2] = 1;

        for (int i = 0; i < iterations ; i++) {

//            for (int j = 0; j < currentState.length; j++) {
//                System.out.print(currentState[j] + " ");
//            }
//            System.out.println();
            draw(currentState,i);
            currentState = generate(currentState,ruleSet,PBC);
        }
    }

    public int [] generate(int []currentState,int []ruleSet,String PBC){
        int []newGeneration = new int[currentState.length];

        if(PBC.equals("Rounded"))
        {
            String neighbours = Integer.toString(currentState[currentState.length - 1]) + Integer.toString(currentState[0]) + Integer.toString(currentState[1]);
            newGeneration[0] = ruleSet[Integer.parseInt(neighbours,2)];
            neighbours = Integer.toString(currentState[currentState.length - 2]) + Integer.toString(currentState[currentState.length - 1]) + Integer.toString(currentState[0]);
            newGeneration[currentState.length  - 1] = ruleSet[Integer.parseInt(neighbours,2)];
        }
        if(PBC.equals("Bounced"))
        {
            String neighbours = Integer.toString(currentState[1]) + Integer.toString(currentState[0]) + Integer.toString(currentState[1]);
            newGeneration[0] = ruleSet[Integer.parseInt(neighbours,2)];
            neighbours = Integer.toString(currentState[currentState.length - 2]) + Integer.toString(currentState[currentState.length - 1]) + Integer.toString(currentState[currentState.length - 2]);
            newGeneration[currentState.length  - 1] = ruleSet[Integer.parseInt(neighbours,2)];
        }
        for (int i = 1; i < currentState.length - 1; i++) {
            String neighbours = Integer.toString(currentState[i - 1]) + Integer.toString(currentState[i]) + Integer.toString(currentState[i + 1]);
            newGeneration[i] = ruleSet[Integer.parseInt(neighbours,2)];
        }
        return newGeneration;
    }
    public void draw(int []currentState,int iterationNo){
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        double cellSize = 4;
        int columnNo = 0;
        for (int i = 0; i < currentState.length; i++) {
            if(currentState[i] == 1)
            {
                double x_coord = cellSize*columnNo;
                double y_coord = cellSize*iterationNo;
                g.fillRect(x_coord,y_coord,cellSize,cellSize);
            }

            columnNo++;
        }
    }
}
