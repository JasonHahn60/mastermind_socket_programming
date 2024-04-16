package Assignment_5;

import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.sql.SQLOutput;
import java.util.Scanner;

public class MyClient {
    private Socket s;
    private BufferedReader in;
    private BufferedWriter out;

    public MyClient(Socket s){
        try{
            this.s = s;
            this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
        }catch(Exception e){
            stop(s, in, out);
        }
    }

    public void sendMsg(){
        try{

            Scanner scan = new Scanner(System.in);
            while(s.isConnected()){
                String msgToSend = scan.nextLine();
                out.write(msgToSend);
                out.newLine();
                out.flush();
            }
        }
        catch (IOException e){
            stop(s, in, out);
        }
    }

    public void listenForMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg;

                while(s.isConnected()){
                    try{
                        msg = in.readLine();
                        System.out.println(msg);
                    }
                    catch (IOException e){
                        stop(s, in, out);
                    }
                }
            }
        }).start();
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

    public void listenForInput(Scanner scan){
        while(s.isConnected()) {
            try {
                System.out.println("test");
                String input = scan.nextLine();
                System.out.println(input);
                out.write(input);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        Socket s = new Socket("localhost",6666);
        MyClient client = new MyClient(s);
        while(s.isConnected()){
            client.listenForMsg();
            client.sendMsg();
            //client.listenForInput(scan);
        }
        System.exit(1);
    }
}
