/* EE422C Assignment #2 submission by
 * Jason Hahn
 * jh73942
 */

package Assignment_5;

import java.util.Scanner;
public class Driver {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        boolean test = true;
        //if first argument is '1' enter testing mode
        if(args.length == 1){
            if(args[0].equals("1")){
                test = true;
            }
        }else{
            test = false;
        }
        while(true){
            //Game myGame = new Game(test, scan);
            //myGame.runGame();
        }
    }
}
