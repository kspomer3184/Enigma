package enigma;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EnigmaController extends Application {

    ObservableList<String> rotors = FXCollections.observableArrayList("1 - Eins", "2 - Zwei", "3 - Drei", "4 - Vier", "5 - Fünf");

    SpinnerValueFactory<Integer> valueFactoryInner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 28, 1);
    SpinnerValueFactory<Integer> valueFactoryMiddle = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 28, 1);
    SpinnerValueFactory<Integer> valueFactoryOuter = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 28, 1);
    SpinnerValueFactory<Integer> valueFactoryDay = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, 31);
    
    ArrayList<String> message = new ArrayList<>();

    File file;
    
    boolean playing = false;

    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("Enigma.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
        
        

    }
    
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
    private Spinner<Integer> daySpinner;

    @FXML
    private Button encodeButton;

    @FXML
    private Button openButton;
    
    @FXML
    private Button finishButton;
    
    @FXML
    private Button setButton;

    @FXML
    private Label fileLabel;
    
    @FXML
    private Pane finishedPane;

    @FXML
    public Button closeButton;

    @FXML
    public Button repeatButton;
    
    @FXML
    private MediaView mediaView;
    
    @FXML
    private void handleOpenAction(ActionEvent event) throws IOException {
        
        FileChooser fc = new FileChooser();
        fc.setTitle("View Files");
        fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"), new ExtensionFilter("All Files", "*.*"));
        fc.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        file = fc.showOpenDialog(openButton.getScene().getWindow());
        
        if (file != null) { try ( //I found the file
                Scanner sc = new Scanner(file).useDelimiter("\n")) {
            while (sc.hasNext()) { 
                message.add(sc.next());
                
            }
            }
        }
        
        
        fileLabel.setText(file.getName());
    }

    @FXML
    private void handleEncodeAction(ActionEvent event) throws IOException, URISyntaxException, InterruptedException {
        
        encodeButton.setText("Verarbeitung");
        encodeButton.setDisable(true);
        
        int inWheel = Character.getNumericValue(innerWheel.getValue().charAt(0)) - 1;
        int midWheel = Character.getNumericValue(middleWheel.getValue().charAt(0)) - 1;
        int outWheel = Character.getNumericValue(outerWheel.getValue().charAt(0)) - 1;
        
        Cipher cp = new Cipher(inWheel, midWheel, outWheel, innerStart.getValue(), middleStart.getValue(), outerStart.getValue());
        Plugboard pb = new Plugboard();
        Reflector rf = new Reflector();
        Encoder en = new Encoder();
        
        pb.settings(plugboard.getText());
        rf.settings(reflector.getText());

        en.numConvert(message);
        en.encodeMessage(pb, cp, rf, message);
        en.writeMessage(file, message);
        
        finishedPane.setDisable(false);
        finishedPane.setVisible(true);
        
        MediaPlayer mp = en.playMusic();
        mediaView.setMediaPlayer(mp);
        if(!playing){
            mp.play();
            playing = true;
        }
        
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
    public void handleRepeatButtonAction(ActionEvent event) throws IOException, Exception {
        
        Stage stage = (Stage) closeButton.getScene().getWindow();
        
        stage.close();
        message.clear();
        start(new Stage());
        
        
    }
    
    @FXML // this exceptionally long function auto-sets the settings for each day on this given sheet
    public void handleSetButtonAction(ActionEvent event){
        
        if (null != daySpinner.getValue())switch (daySpinner.getValue()) {
            case 31:
                innerWheel.setValue("1 - Eins");
                middleWheel.setValue("5 - Fünf");
                outerWheel.setValue("3 - Drei");
                valueFactoryInner.setValue(14);
                valueFactoryMiddle.setValue(9);
                valueFactoryOuter.setValue(24);
                plugboard.setText("SZ GT DV KU FO MY EW JN IX LQ");
                reflector.setText("KM AX FZ GO DI CN BR PV LT EQ HS UW J. Y ");
                break;
            case 30:
                innerWheel.setValue("4 - Vier");
                middleWheel.setValue("3 - Drei");
                outerWheel.setValue("2 - Zwei");
                valueFactoryInner.setValue(5);
                valueFactoryMiddle.setValue(26);
                valueFactoryOuter.setValue(2);
                plugboard.setText("IS EV MX RW DT UZ JQ AO CH NY");
                reflector.setText("KM AX FZ GO DI CN BR PV LT EQ HS UW J. Y ");
                break;
            case 29:
                innerWheel.setValue("3 - Drei");
                middleWheel.setValue("2 - Zwei");
                outerWheel.setValue("1 - Eins");
                valueFactoryInner.setValue(12);
                valueFactoryMiddle.setValue(24);
                valueFactoryOuter.setValue(3);
                plugboard.setText("DJ AT CV IO ER QS LW PZ FN BH");
                reflector.setText("KM AX FZ GO DI CN BR PV LT EQ HS UW J. Y ");
                break;
            case 28:
                innerWheel.setValue("2 - Zwei");
                middleWheel.setValue("3 - Drei");
                outerWheel.setValue("5 - Fünf");
                valueFactoryInner.setValue(6);
                valueFactoryMiddle.setValue(8);
                valueFactoryOuter.setValue(16);
                plugboard.setText("CR FV AI DK OT MQ EU BX LP GJ");
                reflector.setText("KM AX FZ GO DI CN BR PV LT EQ HS UW J. Y ");
                break;
            case 27:
                innerWheel.setValue("3 - Drei");
                middleWheel.setValue("1 - Eins");
                outerWheel.setValue("4 - Vier");
                valueFactoryInner.setValue(11);
                valueFactoryMiddle.setValue(3);
                valueFactoryOuter.setValue(7);
                plugboard.setText("DY IN BV GR AM LO FP HT EX UW");
                reflector.setText("KM AX FZ GO DI CN BR PV LT EQ HS UW J. Y ");
                break;
            case 26:
                innerWheel.setValue("1 - Eins");
                middleWheel.setValue("4 - Vier");
                outerWheel.setValue("5 - Fünf");
                valueFactoryInner.setValue(17);
                valueFactoryMiddle.setValue(22);
                valueFactoryOuter.setValue(19);
                plugboard.setText("VZ AL RT KO CG EI BJ DU FS HP");
                reflector.setText("KM AX FZ GO DI CN BR PV LT EQ HS UW J. Y ");
                break;
            case 25:
                innerWheel.setValue("4 - Vier");
                middleWheel.setValue("3 - Drei");
                outerWheel.setValue("1 - Eins");
                valueFactoryInner.setValue(8);
                valueFactoryMiddle.setValue(25);
                valueFactoryOuter.setValue(12);
                plugboard.setText("OR PV AD IT FK HJ LZ NS EQ CW");
                reflector.setText("KM AX FZ GO DI CN BR PV LT EQ HS UW J. Y ");
                break;
            case 24:
                innerWheel.setValue("5 - Fünf");
                middleWheel.setValue("1 - Eins");
                outerWheel.setValue("4 - Vier");
                valueFactoryInner.setValue(5);
                valueFactoryMiddle.setValue(18);
                valueFactoryOuter.setValue(14);
                plugboard.setText("TY AS OW KV JM DR HX GL CZ NU");
                reflector.setText("IU AS DV GL F. HT OX EZ CY J  MR KN BQ PW");
                break;
            case 23:
                innerWheel.setValue("4 - Vier");
                middleWheel.setValue("2 - Zwei");
                outerWheel.setValue("1 - Eins");
                valueFactoryInner.setValue(24);
                valueFactoryMiddle.setValue(12);
                valueFactoryOuter.setValue(4);
                plugboard.setText("QV FR AK EO DH CJ MZ SX GN LT");
                reflector.setText("IU AS DV GL F. HT OX EZ CY J  MR KN BQ PW");
                break;
            case 22:
                innerWheel.setValue("2 - Zwei");
                middleWheel.setValue("4 - Vier");
                outerWheel.setValue("5 - Fünf");
                valueFactoryInner.setValue(1);
                valueFactoryMiddle.setValue(9);
                valueFactoryOuter.setValue(21);
                plugboard.setText("FJ ES IM RX LV AY OU BG WZ CN");
                reflector.setText("IU AS DV GL F. HT OX EZ CY J  MR KN BQ PW");
                break;
            case 21:
                innerWheel.setValue("1 - Eins");
                middleWheel.setValue("5 - Fünf");
                outerWheel.setValue("2 - Zwei");
                valueFactoryInner.setValue(13);
                valueFactoryMiddle.setValue(5);
                valueFactoryOuter.setValue(19);
                plugboard.setText("RU HL FY OS GZ DM AW CE TV NX");
                reflector.setText("IU AS DV GL F. HT OX EZ CY J  MR KN BQ PW");
                break;
            case 20:
                innerWheel.setValue("3 - Drei");
                middleWheel.setValue("4 - Vier");
                outerWheel.setValue("5 - Fünf");
                valueFactoryInner.setValue(24);
                valueFactoryMiddle.setValue(1);
                valueFactoryOuter.setValue(10);
                plugboard.setText("DF MO QZ AU RY SV JL GX BE TW");
                reflector.setText("IU AS DV GL F. HT OX EZ CY J  MR KN BQ PW");
                break;
            case 19:
                innerWheel.setValue("5 - Fünf");
                middleWheel.setValue("3 - Drei");
                outerWheel.setValue("1 - Eins");
                valueFactoryInner.setValue(17);
                valueFactoryMiddle.setValue(25);
                valueFactoryOuter.setValue(20);
                plugboard.setText("OX PRFH MY DL CM AE TZ JS GI");
                reflector.setText("IU AS DV GL F. HT OX EZ CY J  MR KN BQ PW");
                break;
            case 18:
                innerWheel.setValue("4 - Vier");
                middleWheel.setValue("2 - Zwei");
                outerWheel.setValue("5 - Fünf");
                valueFactoryInner.setValue(15);
                valueFactoryMiddle.setValue(23);
                valueFactoryOuter.setValue(26);
                plugboard.setText("EJ OY IV AQ KW FX MT PS LU BD");
                reflector.setText("IU AS DV GL F. HT OX EZ CY J  MR KN BQ PW");
                break;
            case 17:
                innerWheel.setValue("1 - Eins");
                middleWheel.setValue("4 - Vier");
                outerWheel.setValue("2 - Zwei");
                valueFactoryInner.setValue(21);
                valueFactoryMiddle.setValue(10);
                valueFactoryOuter.setValue(6);
                plugboard.setText("IR KZ LS EM OV GY QX AF JP BU");
                reflector.setText("IU AS DV GL F. HT OX EZ CY J  MR KN BQ PW");
                break;
            case 16:
                innerWheel.setValue("5 - Fünf");
                middleWheel.setValue("2 - Zwei");
                outerWheel.setValue("3 - Drei");
                valueFactoryInner.setValue(8);
                valueFactoryMiddle.setValue(16);
                valueFactoryOuter.setValue(13);
                plugboard.setText("HM JO DI NR BY XZ GS PU FQ CT");
                reflector.setText("AI BT MV HU FW EL DG KN YZ OQ CP SX J. R ");
                break;
            case 15:
                innerWheel.setValue("2 - Zwei");
                middleWheel.setValue("4 - Vier");
                outerWheel.setValue("1 - Eins");
                valueFactoryInner.setValue(1);
                valueFactoryMiddle.setValue(3);
                valueFactoryOuter.setValue(7);
                plugboard.setText("DS HY MR GW LX AJ BQ CO IP NT");
                reflector.setText("AI BT MV HU FW EL DG KN YZ OQ CP SX J. R ");
                break;
            case 14:
                innerWheel.setValue("4 - Vier");
                middleWheel.setValue("1 - Eins");
                outerWheel.setValue("5 - Fünf");
                valueFactoryInner.setValue(15);
                valueFactoryMiddle.setValue(11);
                valueFactoryOuter.setValue(5);
                plugboard.setText("GM JR KS IY HZ PL AX BT CQ NV");
                reflector.setText("AI BT MV HU FW EL DG KN YZ OQ CP SX J. R ");
                break;
            case 13:
                innerWheel.setValue("1 - Eins");
                middleWheel.setValue("3 - Drei");
                outerWheel.setValue("2 - Zwei");
                valueFactoryInner.setValue(13);
                valueFactoryMiddle.setValue(20);
                valueFactoryOuter.setValue(3);
                plugboard.setText("LY AG KM BR IQ JU HV SW ET CX");
                reflector.setText("AI BT MV HU FW EL DG KN YZ OQ CP SX J. R ");
                break;
            case 12:
                innerWheel.setValue("5 - Fünf");
                middleWheel.setValue("2 - Zwei");
                outerWheel.setValue("4 - Vier");
                valueFactoryInner.setValue(18);
                valueFactoryMiddle.setValue(10);
                valueFactoryOuter.setValue(7);
                plugboard.setText("MU BP CY RZ KX AN JT DG IL FW");
                reflector.setText("AI BT MV HU FW EL DG KN YZ OQ CP SX J. R ");
                break;
            case 11:
                innerWheel.setValue("2 - Zwei");
                middleWheel.setValue("4 - Vier");
                outerWheel.setValue("3 - Drei");
                valueFactoryInner.setValue(2);
                valueFactoryMiddle.setValue(26);
                valueFactoryOuter.setValue(15);
                plugboard.setText("KN UY HR PW FM BO EZ QT DX JV");
                reflector.setText("AI BT MV HU FW EL DG KN YZ OQ CP SX J. R ");
                break;
            case 10:
                innerWheel.setValue("3 - Drei");
                middleWheel.setValue("5 - Fünf");
                outerWheel.setValue("4 - Vier");
                valueFactoryInner.setValue(23);
                valueFactoryMiddle.setValue(21);
                valueFactoryOuter.setValue(1);
                plugboard.setText("LR IK MS QU HW PT GO VX FZ EN");
                reflector.setText("AI BT MV HU FW EL DG KN YZ OQ CP SX J. R ");
                break;
            case 9:
                innerWheel.setValue("5 - Fünf");
                middleWheel.setValue("1 - Eins");
                outerWheel.setValue("3 - Drei");
                valueFactoryInner.setValue(16);
                valueFactoryMiddle.setValue(4);
                valueFactoryOuter.setValue(8);
                plugboard.setText("QY BS LN KT AP IU DW HO RV JZ");
                reflector.setText("AI BT MV HU FW EL DG KN YZ OQ CP SX J. R ");
                break;
            case 8:
                innerWheel.setValue("4 - Vier");
                middleWheel.setValue("2 - Zwei");
                outerWheel.setValue("5 - Fünf");
                valueFactoryInner.setValue(13);
                valueFactoryMiddle.setValue(19);
                valueFactoryOuter.setValue(25);
                plugboard.setText("FI NQ SY CU BZ AH EL TX DO KP");
                reflector.setText("IL AP EU HO QT WZ KV GM YF NR DX CJ S. B ");
                break;
            case 7:
                innerWheel.setValue("1 - Eins");
                middleWheel.setValue("4 - Vier");
                outerWheel.setValue("2 - Zwei");
                valueFactoryInner.setValue(9);
                valueFactoryMiddle.setValue(3);
                valueFactoryOuter.setValue(22);
                plugboard.setText("UX IZ HN BK GQ CP FT JY MW AR");
                reflector.setText("IL AP EU HO QT WZ KV GM YF NR DX CJ S. B ");
                break;
            case 6:
                innerWheel.setValue("3 - Drei");
                middleWheel.setValue("1 - Eins");
                outerWheel.setValue("5 - Fünf");
                valueFactoryInner.setValue(11);
                valueFactoryMiddle.setValue(18);
                valueFactoryOuter.setValue(14);
                plugboard.setText("DQ GU BW NP HK AZ CI PO JX VY");
                reflector.setText("IL AP EU HO QT WZ KV GM YF NR DX CJ S. B ");
                break;
            case 5:
                innerWheel.setValue("5 - Fünf");
                middleWheel.setValue("2 - Zwei");
                outerWheel.setValue("4 - Vier");
                valueFactoryInner.setValue(23);
                valueFactoryMiddle.setValue(2);
                valueFactoryOuter.setValue(25);
                plugboard.setText("MV CL GK OQ BI FU HS PX NW EY");
                reflector.setText("IL AP EU HO QT WZ KV GM YF NR DX CJ S. B ");
                break;
            case 4:
                innerWheel.setValue("2 - Zwei");
                middleWheel.setValue("4 - Vier");
                outerWheel.setValue("1 - Eins");
                valueFactoryInner.setValue(4);
                valueFactoryMiddle.setValue(21);
                valueFactoryOuter.setValue(9);
                plugboard.setText("AC BL OZ EK QW GP SU DH JM TX");
                reflector.setText("IL AP EU HO QT WZ KV GM YF NR DX CJ S. B ");
                break;
            case 3:
                innerWheel.setValue("5 - Fünf");
                middleWheel.setValue("1 - Eins");
                outerWheel.setValue("2 - Zwei");
                valueFactoryInner.setValue(19);
                valueFactoryMiddle.setValue(11);
                valueFactoryOuter.setValue(6);
                plugboard.setText("KR MP CN BF EH DZ IW AV GJ LO");
                reflector.setText("IL AP EU HO QT WZ KV GM YF NR DX CJ S. B ");
                break;
            case 2:
                innerWheel.setValue("4 - Vier");
                middleWheel.setValue("5 - Fünf");
                outerWheel.setValue("1 - Eins");
                valueFactoryInner.setValue(16);
                valueFactoryMiddle.setValue(13);
                valueFactoryOuter.setValue(2);
                plugboard.setText("BN HU EG PY KQ CP OS JW AI VZ");
                reflector.setText("IL AP EU HO QT WZ KV GM YF NR DX CJ S. B ");
                break;
            case 1:
                innerWheel.setValue("2 - Zwei");
                middleWheel.setValue("1 - Eins");
                outerWheel.setValue("3 - Drei");
                valueFactoryInner.setValue(23);
                valueFactoryMiddle.setValue(12);
                valueFactoryOuter.setValue(10);
                plugboard.setText("DP GM NZ CK GV HQ AF UY SW JO");
                reflector.setText("IL AP EU HO QT WZ KV GM YF NR DX CJ S. B ");
                break;
            
            default:
                break;
        }
        
    }

    @FXML
    void initialize() {

        innerWheel.setItems(rotors);
        innerWheel.setValue("1 - Eins");
        middleWheel.setItems(rotors);
        middleWheel.setValue("1 - Eins");
        outerWheel.setItems(rotors);
        outerWheel.setValue("1 - Eins");

        innerStart.setValueFactory(valueFactoryInner);
        middleStart.setValueFactory(valueFactoryMiddle);
        outerStart.setValueFactory(valueFactoryOuter);
        daySpinner.setValueFactory(valueFactoryDay);

    }
    



}
