import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ServerMain {

    public static void main (String[] args){
        Scanner input = new Scanner(System.in);
        System.out.print("Kullanilacak portu girin (Ornek: 25565): ");
        int port = Integer.parseInt(input.nextLine());
        System.out.print("Kirilacak 32 karakterden olusan MD5 sifreyi girin: ");
        new Server(port, input.nextLine());
    }
}

class Server {
    private ServerSocket server;
    private int port;
    //private String hash = "97dfc43fcf0a4b152a93c2aeb5da0986";//9994445
    private String hash;// = "f841c5ebc50899712f64083c4949cd01";//1999444544 sayisi
    private int artis = 100000000;

    public Server(int port, String hash){
        try{
            this.port = port;
            this.hash = hash;
            server = new ServerSocket(port);
            for(long baslangic = 0, hedef = artis; hedef < Long.MAX_VALUE; baslangic += artis, hedef += artis){
                new ServerThread(server.accept(), baslangic, hedef, hash).start();
            }

        }catch(Exception e){}

        //server.close();
    }

}
class ServerThread extends Thread{
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final long baslangic;
    private final long hedef;
    private final String hash;

    public ServerThread(Socket socket, long baslangic, long hedef, String hash) throws Exception{
        this.socket = socket;
        this.baslangic = baslangic;
        this.hedef = hedef;
        this.hash = hash;
        System.out.println("Bir client baglandi " + socket.getPort() + " portuna yonlendirildi.");
        System.out.println(socket.getPort() + " portlu cliente hedef belirlendi (" + baslangic + "-->" + hedef + ")");

        out = new PrintWriter(socket.getOutputStream(),true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    @Override
    public void run(){
        out.println(baslangic + "-" + hedef + "-" + hash);
        try{

            String line = in.readLine();
            if (line == null){
                return;
            }
            System.out.println("Sifre= "+line);
        }catch(Exception e){};
    }

}