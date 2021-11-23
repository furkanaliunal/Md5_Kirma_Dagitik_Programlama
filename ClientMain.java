import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        System.out.println(Client.getMd5(1999444544));

        Scanner input = new Scanner(System.in);
        System.out.print("Baglanilacak Ip yi girin (Ornek Format: 127.0.0.1:25565): ");
        String[] veriler = input.nextLine().split(":");
        String ip = veriler[0];
        int port = Integer.parseInt(veriler[1]);
        new Client(ip, port);
    }

}

class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String ip;
    private int port;

    public Client(String ip, int port) throws Exception{

        this.ip = ip;
        this.port = port;
        do{
            System.out.println("Baglaniliyor");
            connect();
            System.out.println("Baglanildi!\n\n");
        }while(!decrypt());


    }

    public Boolean decrypt() throws Exception{
        String gelen = in.readLine();
        String[] gelenler = gelen.split("-");
        long baslangic = Long.parseLong(gelenler[0]);
        long hedef = Long.parseLong(gelenler[1]);
        String hash = gelenler[2];
        System.out.println("Baslangic: " + baslangic +"\nHedef: " + hedef + "\nHash: " + hash +"\n");

        for(long i = baslangic; i < hedef; i++){
            if (getMd5(i).equals(hash)){
                out.println(i);
                return true;
            }
            if (i % 10000000 == 0){
                System.out.println("Girilen aralik: " + i + " --> " + (i+10000000));
            }
        }
        System.out.println("Sifre bulunamadi yeniden baglaniliyor..");
        return false;
    }

    public void connect() throws Exception{
        socket = new Socket(ip, port);

        out = new PrintWriter(socket.getOutputStream(),true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static String getMd5(long sayi) throws Exception{
        String yourString = Long.toString(sayi);
        byte[] bytesOfMessage = yourString.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        BigInteger bigInt = new BigInteger(1,thedigest);
        String hashText = bigInt.toString(16);
        while(hashText.length() < 32 ){
            hashText = "0"+hashText;
        }
        return hashText;
    }
}