/* EE422C Assignment #2 submission by
 * Jason Hahn
 * jh73942
 */

package Assignment_5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

public class Game extends Driver{
    //game variables
    private int guessLeft;
    private boolean play;
    final String SECRETCODE;
    String input;
    Scanner scan;
    boolean test;
    String guess = "";
    boolean win = false;
    String response = "";
    ArrayList<String> hisG = new ArrayList<String>();
    ArrayList<String> hisR = new ArrayList<String>();
    ClientHandler gameClient;
    BufferedReader in;
    BufferedWriter out;
    
    public Game(boolean test, Scanner scan, ClientHandler client, String code){
        this.SECRETCODE = code;
        this.scan = scan;
        this.test = test;
        this.gameClient = client;
        this.in = client.in;
        this.out = client.out;
        this.guessLeft = GameConfiguration.guessNumber;
    }

    public void runGame() throws IOException {
        //BEGIN PROMPT
        case0();
        play = true;
        guessLeft = GameConfiguration.guessNumber;
        while(play){
            if(guessLeft > 0){
                //MAIN INPUT
                case1();
                if(guess.equals("HISTORY")){
                    //PRINT HISTORY
                    case6();
                }
                else{
                    while(!isValid(guess)){
                        //INVALID INPUT
                        case2();
                    }
                    //CALCULATE PINS & ADD TO HISTORY
                    case5();
                    hisG.add(guess);
                    hisR.add(response);
                    if(win){
                        //WIN PROMPT
                        case3();
                    }
                }
            }
            else{
                //LOSE PROMPT
                case4();
            }
        }
    }

    public int guessesRemaining(){
        return this.guessLeft;
    }


     //begin game prompts
    public void case0() throws IOException {
        printstartingprompt();
        out.write("You  have  12  guesses  to  figure  out  the  secret  code  or  you  lose the game. " +
        "Are you ready to play? (Y/N): ");
        out.newLine();
        out.flush();
        //send keyboard input from client to here (server)
        String input = in.readLine().toUpperCase();
        if(!input.equals("Y")){
            System.exit(1);
        }
        out.write("\nGenerating secret code...\n");
        out.newLine();
        out.flush();
    }
    //main input
    public void case1() throws IOException {
        out.write("You have " + guessLeft + " guesses left. \n What is your next guess?" +
        "\n Type in the characters for your guess and press enter.");
        out.newLine();
        out.flush();
        if(this.test){
            out.write("Secret code -> "+ this.SECRETCODE);
            out.newLine();
            out.flush();
        }
        out.write("Enter guess: ");
        out.newLine();
        out.flush();
        guess = in.readLine().toUpperCase();
    }
    //invalid input
    public void case2() throws IOException {
        out.write("\n" + guess + " -> INVALID GUESS\n");
        out.newLine();
        out.flush();
        out.write("What is your next guess? \n Type in the characters for your guess and press enter.\n Enter guess: ");
        out.newLine();
        out.flush();
        guess = in.readLine().toUpperCase();
    }
    //win
    public void case3() throws IOException {
        out.write(" - You Win!");
        out.newLine();
        out.flush();

        gameClient.gameOver();
    }
    //lose
    public void case4() throws IOException {
        out.write("Sorry, you are out of guesses. You lose, boo-hoo.");
        out.newLine();
        out.flush();

        gameClient.removeMe();
        play = false;
    }
    //calculations
    public void case5() throws IOException {
        int blacks = BlackMatch(guess, this.SECRETCODE);
        int whites = WhiteMatch(guess, this.SECRETCODE);
        response = blacks + "B_" + whites + "W";
        out.write("\n" + guess + " -> RESULT: " +  response + "\n");
        out.newLine();
        out.flush();
        if(blacks == 4){
            win = true;
        }
        else{
            guessLeft--;
        }
    }
    //print history
    public void case6() throws IOException {
        //out.write("\n");
        //out.newLine();
        //out.flush();
        for(int i = 0; i < hisG.size(); i++){
            out.write(hisG.get(i) + "    ");
            //out.newLine();
            out.flush();
            out.write(hisR.get(i));
            out.newLine();
            out.flush();
        }
        out.write("\n");
        out.newLine();
        out.flush();
    }

    //returns how many pins of the guess are correct color and location
    public static int BlackMatch(String guess, String SECRETCODE){
        int blacks = 0;
        for(int i = 0; i < GameConfiguration.pegNumber; i++){
            if(guess.charAt(i) == SECRETCODE.charAt(i)){
                blacks++;
            }
        }
        return blacks;
    }
    //returns how many pins of the guess are correct color, not location
    public static int WhiteMatch(String guess, String SECRETCODE){
        int whites = 0;
        for(int i = 0; i < GameConfiguration.pegNumber; i++){
            for(int j = 0; j < GameConfiguration.pegNumber; j++){
                if((guess.charAt(i) == SECRETCODE.charAt(j)) && (i != j) && (guess.charAt(i) != SECRETCODE.charAt(i)) && (guess.charAt(j) != SECRETCODE.charAt(j))){
                    whites++;
                    break;
                }
            }
        }
        return whites;
    }
    public static boolean isValid(String guess){
        if(guess.length() != GameConfiguration.pegNumber){return false;}

        for(int i = 0; i < GameConfiguration.pegNumber; i++){
            boolean x = false;
            for(String element : GameConfiguration.colors){
                if(guess.charAt(i) == element.charAt(0)){
                    x = true;
                    break;
                }
            }
            if(!x){return false;}
        }
        return true;
    }

    public void printstartingprompt() throws IOException {
        out.write("Welcome to Mastermind.  Here are the rules.\n\n" +
 
        "This is a text version of the classic board game Mastermind.\n\n" + 
         
        "The  computer  will  think  of  a  secret  code.  The  code  consists  of  4\n" + 
        "colored  pegs.  The  pegs  MUST  be  one  of  six  colors:  blue,  green, \n" + 
        "orange, purple, red, or yellow. A color may appear more than once in \n" + 
        "the  code.  You  try  to  guess  what  colored  pegs  are  in  the  code  and \n" + 
        "what  order  they  are  in.  After  you  make  a  valid  guess  the  result \n" + 
        "(feedback) will be displayed. \n\n" + 
         
        "The  result  consists  of  a  black  peg  for  each  peg  you  have  guessed \n" + 
        "exactly correct (color and position) in your guess.  For each peg in \n" + 
        "the guess that is the correct color, but is out of position, you get \n" + 
        "a  white  peg.  For  each  peg,  which  is  fully  incorrect,  you  get  no \n" + 
        "feedback.  \n\n" + 
         
        "Only the first letter of the color is displayed. B for Blue, R for \n" + 
        "Red, and so forth. When entering guesses you only need to enter the \n" + 
        "first character of each color as a capital letter. ");
        out.newLine();
        out.flush();
    }
}
