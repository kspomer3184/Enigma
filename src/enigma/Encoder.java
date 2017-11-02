/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enigma;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author kylespomer
 */
public class Encoder {

    public void Encoder() {

    }

    synchronized public void encodeMessage(Plugboard pb, Cipher cp, Reflector rf, ArrayList<String> message) {
        
        char temp;
        boolean upper;
        boolean special;
        
        for (int i = 0; i < message.size(); i++) {
            String tempString = "";
            for (int j = 0; j < message.get(i).length(); j++) {
                temp = message.get(i).charAt(j);
                upper = Character.isUpperCase(temp);
                special = !(Character.isLetter(temp) || temp == '.' || temp == ' ');

                if (!special) {
                    temp = pb.encodeChar(temp);
                    temp = cp.forwardCipher(temp);
                    temp = rf.encodeChar(temp);
                    temp = cp.reverseCipher(temp);
                    temp = pb.encodeChar(temp);

                    cp.rotate();
                }
                if (upper) {
                    temp = Character.toUpperCase(temp);
                }
                tempString += temp;
            }
            message.set(i, tempString);
        }
    }

    public void writeMessage(File file, ArrayList<String> message) throws IOException {
        
        try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
            for (int i = 0; i < message.size(); i++) {
                bw.write(message.get(i));
                bw.newLine();
            }
        }

    }

    public void numConvert(ArrayList<String> message) {
        
        for (int i = 0; i < message.size(); i++) {
            message.set(i, message.get(i).replace("\n", ""));
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

    public MediaPlayer playMusic() throws URISyntaxException, MalformedURLException {
        
        Media media = new Media(getClass().getResource("nena.mp3").toString());
        MediaPlayer mp = new MediaPlayer(media);
        mp.setAutoPlay(true);
      
        return mp;
    }

}
