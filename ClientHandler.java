package Assignment_5;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    protected Socket socket;
    public BufferedReader in;
    public BufferedWriter out;
    int clientNum;
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public String code;
    public ClientHandler(Socket clientSocket, BufferedReader in, BufferedWriter out, String code) throws IOException{
        try {
            this.socket = clientSocket;
            this.in = in;
            this.out = out;
            this.code = code;
            clientHandlers.add(this);
            this.clientNum = clientHandlers.size();
            System.out.println( "player " + this.clientNum + " has entered the game!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        boolean test = false;
        Scanner scan = new Scanner(System.in);
        Game myGame = new Game(test, scan, this, code);
        try {
            myGame.runGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcastMsg(String msg){
        for(ClientHandler client: clientHandlers){
            try {
                client.out.write(msg);
                client.out.newLine();
                client.out.flush();
            }
            catch (IOException e){
                //stop(socket, in, out);
            }
        }
    }

    public void stop(Socket socket, BufferedReader in, BufferedWriter out){
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if(socket != null){
                socket.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void gameOver() throws IOException {
        for(ClientHandler client: this.clientHandlers){
            if(client.clientNum != this.clientNum) {
                client.out.write("Sorry, you lose!\nAnother player has won the game!");
                client.out.newLine();
                client.out.flush();
            }
            //stop(client.socket, client.in, client.out);
            //mad errors ^
        }
        System.exit(1);
    }

    public void removeMe(){
        clientHandlers.remove(this);
    }

}
