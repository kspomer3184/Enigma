/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enigma;

import java.util.ArrayList;

/**
 *
 * @author kylespomer
 */
public class Plugboard {
    private ArrayList<String> settings = new ArrayList<>();
    
    
    
    
    void settings (String input){
        for (int i = 0; i < 30; i += 3){
            settings.add(("" + input.charAt(i) + input.charAt(i+1)).toLowerCase());
        }
        
        for (int j = 0; j < 10; j++){
            String temp1, temp2;
            temp1 = settings.get(j);
            temp2 = "" + temp1.charAt(1) + temp1.charAt(0);

            settings.add(temp2);
        }
    }
    
    char encodeChar(char input){
        input = Character.toLowerCase(input);
        
        for(int i = 0; i < settings.size(); i++){
            if(input == settings.get(i).charAt(0)){
                return  settings.get(i).charAt(1);
            }
            

        }
       return input;
    }
    

    
}
