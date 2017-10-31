package enigma;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EnigmaController extends Application {

    ObservableList<String> rotors = FXCollections.observableArrayList("1 - Eins", "2 - Zwei", "3 - Drei", "4 - Vier", "5 - Fünf");

    SpinnerValueFactory<Integer> valueFactoryInner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 28, 1);
    SpinnerValueFactory<Integer> valueFactoryMiddle = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 28, 1);
    SpinnerValueFactory<Integer> valueFactoryOuter = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 28, 1);

    ArrayList<String> message = new ArrayList<>();

    File file;
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Enigma.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
        finishedPane.setDisable(true);

    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> innerWheel;

    @FXML
    private ChoiceBox<String> middleWheel;

    @FXML
    private ChoiceBox<String> outerWheel;

    @FXML
    private TextField plugboard;

    @FXML
    private TextField reflector;

    @FXML
    private Spinner<Integer> middleStart;

    @FXML
    private Spinner<Integer> outerStart;

    @FXML
    private Spinner<Integer> innerStart;

    @FXML
    private Button encodeButton;

    @FXML
    private Button openButton;
    
    @FXML
    private Button finishButton;

    @FXML
    private Label fileLabel;
    
    @FXML
    private Pane finishedPane;

    @FXML
    public Button closeButton;

    @FXML
    void eeee(ActionEvent event) {

    }

    void numConvert(){
        for (int i = 0; i < message.size(); i++) {
            
                        message.set(i, message.get(i).replace("1", "ONE"));
                        message.set(i, message.get(i).replace("2", "TWO"));
                        message.set(i, message.get(i).replace("3", "THREE"));
                        message.set(i, message.get(i).replace("4", "FOUR"));
                        message.set(i, message.get(i).replace("5", "FIVE"));
                        message.set(i, message.get(i).replace("6", "SIX"));
                        message.set(i, message.get(i).replace("7", "SEVEN"));
                        message.set(i, message.get(i).replace("8", "EIGHT"));
                        message.set(i, message.get(i).replace("9", "NINE"));
                        message.set(i, message.get(i).replace("0", "ZERO"));
                    }
    }
    
    @FXML
    private void handleOpenAction(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("View Files");
        fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"), new ExtensionFilter("All Files", "*.*"));
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        file = fc.showOpenDialog(openButton.getScene().getWindow());
        
        if (file != null) { //I found the file
            Scanner sc = new Scanner(file).useDelimiter("\n");
            while (sc.hasNext()) {
                message.add(sc.next().trim());
                
            }
            sc.close();
        }
        
        numConvert();
        fileLabel.setText(file.getName());
    }

    @FXML
    private void handleEncodeAction(ActionEvent event) throws IOException {

        int inWheel = Character.getNumericValue(innerWheel.getValue().charAt(0)) - 1;
        int midWheel = Character.getNumericValue(middleWheel.getValue().charAt(0)) - 1;
        int outWheel = Character.getNumericValue(outerWheel.getValue().charAt(0)) - 1;

        Plugboard pb = new Plugboard();
        pb.settings(plugboard.getText());

        Cipher cp = new Cipher(inWheel, midWheel, outWheel, innerStart.getValue(), middleStart.getValue(), outerStart.getValue());

        Reflector rf = new Reflector();
        rf.settings(reflector.getText());

        Encoder en = new Encoder();
        en.encodeMessage(pb, cp, rf, message);
        en.writeMessage(file, message);
        
        System.err.println(message);
        
        finishedPane.setDisable(false);
        finishedPane.setVisible(true);
        //String path = Test.class.getResource("/nena.mp3").toString();
        Media media = new Media("file:/nena.mp3");
        MediaPlayer mp = new MediaPlayer(media);
        mp.play();
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void handleFinishButtonAction(ActionEvent event) throws IOException {
        Desktop.getDesktop().open(file);
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        assert innerWheel != null : "fx:id=\"innerWheel\" was not injected: check your FXML file 'Enigma.fxml'.";
        assert middleWheel != null : "fx:id=\"middleWheel\" was not injected: check your FXML file 'Enigma.fxml'.";
        assert outerWheel != null : "fx:id=\"outerWheel\" was not injected: check your FXML file 'Enigma.fxml'.";
        assert plugboard != null : "fx:id=\"plugboard\" was not injected: check your FXML file 'Enigma.fxml'.";
        assert reflector != null : "fx:id=\"reflector\" was not injected: check your FXML file 'Enigma.fxml'.";
        assert middleStart != null : "fx:id=\"middleStart\" was not injected: check your FXML file 'Enigma.fxml'.";
        assert outerStart != null : "fx:id=\"outerStart\" was not injected: check your FXML file 'Enigma.fxml'.";
        assert innerStart != null : "fx:id=\"innerStart\" was not injected: check your FXML file 'Enigma.fxml'.";

        innerWheel.setItems(rotors);
        innerWheel.setValue("1 - Eins");
        middleWheel.setItems(rotors);
        middleWheel.setValue("1 - Eins");
        outerWheel.setItems(rotors);
        outerWheel.setValue("1 - Eins");

        innerStart.setValueFactory(valueFactoryInner);
        middleStart.setValueFactory(valueFactoryMiddle);
        outerStart.setValueFactory(valueFactoryOuter);

    }
    



}
