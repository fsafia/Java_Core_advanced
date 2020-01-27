package lesson6.zadanieOsnovnoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){

        final String IP_ADRES  = "localhost";
        final int PORT = 8188;

        Socket socket =null;
        DataInputStream in;
        DataOutputStream out;

        try {
            socket = new Socket(IP_ADRES, PORT);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() { //отображение входящих сообщений
                @Override
                public void run() {
                    try {
                        while (true){

                            String serverMsg = in.readUTF();

                            if (serverMsg.equals("/end")){
                                out.writeUTF("/end");
                                break;
                            }
                            System.out.println("" + serverMsg + "\n");
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*finally {
                        try {
                            in.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/
                }
            }).start();


            Scanner scanner = new Scanner(System.in);
            while (true) {

                String clientMsg = scanner.nextLine();
                if (clientMsg.equals("/end")) {
                    break;
                }
                try {
                    out.writeUTF(clientMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
