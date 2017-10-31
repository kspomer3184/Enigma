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
import java.util.ArrayList;

/**
 *
 * @author kylespomer
 */
public class Encoder {

    public void Encoder() {

    }

    public void encodeMessage(Plugboard pb, Cipher cp, Reflector rf, ArrayList<String> message) {
        for (int i = 0; i < message.size(); i++) {
            String tempString = "";
            for (int j = 0; j < message.get(i).length(); j++) {

                char temp = message.get(i).charAt(j);
                boolean upper = Character.isUpperCase(temp);
                boolean special = !(Character.isLetter(temp) || temp == '.' || temp == ' ');

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
    
    public void writeMessage(File file, ArrayList<String> message) throws IOException{
        try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
            for(int i = 0; i < message.size(); i++){
                bw.write(message.get(i));
                bw.newLine();
            }
        }
        
    }

}
