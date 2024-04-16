package Assignment_5;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MyServer {
    static ServerSocket ss;
    static BufferedReader in;
    static BufferedWriter out;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    SecretCodeGenerator generator = SecretCodeGenerator.getInstance();
    public final String code = generator.getNewSecretCode();

    public MyServer(ServerSocket serverSocket){
        this.ss = serverSocket;
    }

    public void startServer() throws IOException {
        try{
            while(!ss.isClosed()){
                Socket s = ss.accept();

                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

                ClientHandler clientThread = new ClientHandler(s, in, out, code);
                clients.add(clientThread);
                Thread thread = new Thread(clientThread);
                thread.start();
            }

        } catch(IOException e){
            e.printStackTrace();
        } finally {
            ss.close();
        }
    }

    public void closeServerSocket(){
        try{
            if(ss != null){
                ss.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6666);
        MyServer server = new MyServer(ss);
        server.startServer();
        //server.closeServerSocket();
    }
}
