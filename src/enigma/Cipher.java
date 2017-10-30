/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enigma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author kylespomer
 */
public class Cipher {
    final char[][] rotors = {{'A','U','N','G','H','O','V','B','I','P','W','C','J','Q','X','D','K','R','Y',' ','E','L','S','Z','F','M','T','.'},
                             {'O',' ','J','.','E','T','Y','C','H','M','R','W','A','F','K','P','U','Z','D','I','N','S','X','B','G','L','Q','V'},
                             {'F','B','D','H','J','L','N','P','R','T','V','X','Z','.','A','C','E','G','I',' ','K','M','O','Q','S','U','W','Y'},
                             {'.','H','K','P','D','E','A','C',' ','W','T','V','Q','M','Y','N','L','X','S','U','R','Z','O','J','F','B','G','I'},
                             {'Y','D','N','G','L','C','I','Q','V','E','Z','R','P','T','A','O','X','W','B','M','J','S','U','H','.','K',' ','F'}};

    ArrayList<Character> rotor3 = new ArrayList<>();
    ArrayList<Character> rotor2 = new ArrayList<>();
    ArrayList<Character> rotor1 = new ArrayList<>();
    
    private int counter1, counter2;
    
    public Cipher(int input1, int input2, int input3, int start1, int start2, int start3) {
        
        for(int i = 0; i < 28; i ++){
            rotor1.add(Character.toLowerCase(rotors[input1][i]));
            rotor2.add(Character.toLowerCase(rotors[input2][i]));
            rotor3.add(Character.toLowerCase(rotors[input3][i]));
        }
        
        Collections.rotate(rotor1, 1-start1);
        Collections.rotate(rotor2, 1-start2);
        Collections.rotate(rotor3, 1-start3);
}
    
    public char forwardCipher(char input){
        char outerMatch = rotor3.get(rotor1.indexOf(input));
        return rotor3.get(rotor2.indexOf(outerMatch));
    }
    
    public char reverseCipher(char input){
        char middleMatch = rotor2.get(rotor3.indexOf(input));
        return rotor1.get(rotor3.indexOf(middleMatch));
    }
    
    void rotate(){
        counter1++;
        Collections.rotate(rotor1, 1);
        if (counter1 == 27){
            counter1 = 0;
            counter2++;
            Collections.rotate(rotor2, 1);
        }
        if (counter2 == 27){
            counter2 = 0;
            Collections.rotate(rotor3, 1);
        }
        
    }

}
