package guivehiclephysicssim.view;

import autonomous.pid.Pid;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import autonomous.mpc.Mpc;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class Main extends Application {

    Stage window;
    String pathFilePath;
    String mapFilePath;
    String startX, startY, startAngle;

    public static void main(String[] args){
        launch(args);
    }

    private boolean isNumeric(String val){
        try{
            Integer.parseInt(val);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Autonomous Vehicle Simulator");

        VBox menu = new VBox();
        Button buttonStartSimulation = new Button("Start Simulation");
        Button buttonStartController = new Button("Start Controller");
        Button buttonLoadPath = new Button("Load path file (*.csv)");
        Button buttonLoadMap = new Button("Load map file (*.png)");
        Button buttonChooseStartPoint = new Button("Choose start point");
        Button buttonQuit = new Button("Quit");
        Button buttonStartServer = new Button("Start server (glassfish5)");


        window.setOnCloseRequest(e->{
            e.consume();
            boolean quit = ConfirmBox.display("Quit application", "Are you sure you want to quit?");
            if(quit){
                window.close();
            }
        });

        buttonQuit.setOnAction(e->{
            e.consume();
            boolean quit = ConfirmBox.display("Quit application", "Are you sure you want to quit?");
            if(quit){
                window.close();
                System.exit(0);
            }
        });

        FileChooser choosePath = new FileChooser();
        choosePath.setTitle("Choose path file(*.csv)");

        buttonLoadPath.setOnAction(e->{
            try{
                File pathFile = choosePath.showOpenDialog(window);
                pathFilePath = pathFile.getPath();
                System.out.println(pathFilePath);
            }catch(Exception ex){System.out.println("No file chosen");}

        });

        FileChooser chooseMap = new FileChooser();
        choosePath.setTitle("Choose map file(*.png)");

        buttonLoadMap.setOnAction(e->{
            try{
                File mapFile = chooseMap.showOpenDialog(window);
                mapFilePath = mapFile.getPath();
                System.out.println(mapFilePath);
            }catch(Exception ex){System.out.println("No file chosen");}
        });


        Dialog<ArrayList<String>> startPointInputDialog = new Dialog<>();
        startPointInputDialog.setTitle("Start point input");
        startPointInputDialog.setHeaderText("Please, input initial x, y and angle values (Integers)");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        startPointInputDialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10.0);
        grid.setVgap(10.0);
        grid.setPadding(new Insets(20.0, 150.0, 10.0, 10.0));

        TextField xInput = new TextField();
        xInput.setPromptText("X coordinate");
        TextField yInput = new TextField();
        yInput.setPromptText("Y coordinate");
        TextField angleInput = new TextField();
        angleInput.setPromptText("Angle with respect to X axis in degrees");

        grid.add(new Label("X coordinate:"), 0, 0);
        grid.add(xInput, 1, 0);
        grid.add(new Label("Y coordinate:"), 0, 1);
        grid.add(yInput, 1, 1);
        grid.add(new Label("Angle:"), 0, 2);
        grid.add(angleInput, 1, 2);

        Node okButton = startPointInputDialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        ObservableBooleanValue areAllNumeric = new SimpleBooleanProperty(isNumeric(xInput.getText()) && isNumeric(yInput.getText()) && isNumeric(angleInput.getText()));
        BooleanBinding booleanBind = xInput.textProperty().isEmpty().or(yInput.textProperty().isEmpty()).or(
                angleInput.textProperty().isEmpty()).or(areAllNumeric);

        okButton.disableProperty().bind(booleanBind);

        startPointInputDialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> xInput.requestFocus());

        startPointInputDialog.setResultConverter(dialogButton -> {
            if(dialogButton == okButtonType) {
                ArrayList<String> result = new ArrayList<String>();
                result.add(xInput.getText());
                result.add(yInput.getText());
                result.add(angleInput.getText());
                return  result;
            }
            else return null;
        });

        buttonChooseStartPoint.setOnAction(e ->{
            Optional<ArrayList<String>> result = startPointInputDialog.showAndWait();
            result.ifPresent(resultList -> {
                startX = resultList.get(0);
                startY = resultList.get(1);
                startAngle = resultList.get(2);
                System.out.println(startX + " " + startY + " " + startAngle);
            });
        });

        buttonStartSimulation.setOnAction(e -> {
            String[] args = new String[5];
            args[0] = pathFilePath;
            args[1] = mapFilePath;
            args[2] = startX;
            args[3] = startY;
            args[4] = startAngle;
            Thread simThread = new Thread(() -> ViewManager.main(args));
            simThread.start();
            buttonStartSimulation.setDisable(true);
        });

        final ToggleGroup chooseController = new ToggleGroup();
        RadioButton chooseMpc = new RadioButton("MPC");
        chooseMpc.setToggleGroup(chooseController);
        chooseMpc.setSelected(true);

        RadioButton choosePid = new RadioButton("PID");
        choosePid.setToggleGroup(chooseController);

        HBox chooseControllerMenu = new HBox();
        chooseControllerMenu.getChildren().addAll(chooseMpc, choosePid);
        chooseControllerMenu.setAlignment(Pos.CENTER);

        buttonStartController.setOnAction(e -> {
            Thread ctrlThread = null;
            if(chooseMpc.isSelected()){
                ctrlThread = new Thread(() -> Mpc.main(null));

            }
            else if(choosePid.isSelected()){
                ctrlThread = new Thread(() -> Pid.main(null));
            }
            ctrlThread.start();
            buttonStartController.setDisable(true);
        });



        menu.getChildren().addAll(buttonStartSimulation, buttonStartController, buttonStartServer, chooseControllerMenu, buttonLoadPath, buttonLoadMap, buttonChooseStartPoint, buttonQuit);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(20.0);
        Scene scene = new Scene(menu, 300, 380);
        window.setScene(scene);
        window.show();

    }

}
