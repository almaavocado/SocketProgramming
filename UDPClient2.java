import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * UDP Client will connect to a server using a socket but will need the IP and port number
 */

public class UDPClient2 {

    static final String IPV4_REGEX =
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    static final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);

    static int portNumber;



    //Method to check if the user inputted IP address is valid
    static String validateIPAddress(){
        Scanner in = new Scanner(System.in);

        //Ask user to input an IP address
        System.out.println("Please enter an IP address: ");
        String IpAddress = in.nextLine();
        Matcher verify = IPv4_PATTERN.matcher(IpAddress);

        while (!verify.matches()){
            System.out.println("ERROR: Invalid IP address");
            System.out.println("Please enter an IP address: ");
            IpAddress = in.nextLine();
            verify = IPv4_PATTERN.matcher(IpAddress);

        }

        return IpAddress;
    }


    static int validatePortNumber(){
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the port number of the server: ");
        while (true) {
            try {
                portNumber = in.nextInt();
                while(portNumber > 65535 || portNumber < 1){
                    System.out.println("ERROR: Please enter a valid port number: ");
                    portNumber = in.nextInt();
                }
                break;
            }
            catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a valid port number: ");
                continue;
            }
        }
        return portNumber;
    }


    static boolean newMessage(){

        Scanner in = new Scanner(System.in);

        boolean userMessage = true;

        char answer = ' ';
        while(answer != 'y' && answer != 'n') {
            System.out.println("Would you like to send another message? (y/n)");
            answer = in.nextLine().charAt(0);

            if (answer == 'y') {
                userMessage = true;
            } else if (answer == 'n') {
                userMessage = false;
            }
            else {
                System.out.println("You entered the wrong character. Try again. ");
            }
        }

        return userMessage;

    }



    public static void main(String[] args) {
        //args give message contents and server hostname
        Scanner in = new Scanner(System.in);
        boolean userMessage = true;   //want another message to send?
        String message; //message

        //Sockets are used for the connection between the Client and the Server
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            //Assign the IP address host and check if it is a valid ip address
            InetAddress aHost = InetAddress.getByName(validateIPAddress());
            int portNumber = validatePortNumber();

            boolean mess = newMessage();

            while (mess == true) {

                System.out.println("Enter a message to send to the server: ");
                message = in.nextLine();

                byte[] m = message.getBytes();
                // byte[] m = args[0].getBytes();

                //Sends the request to the server
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, portNumber);
                aSocket.send(request);

                byte[] buffer = new byte[1000];

                //Sends the reply to the server receive reply from server
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);
                System.out.println("Reply: " + new String(reply.getData()));

            }
        }
        catch(SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }
        catch(IOException e){
            System.out.println("IO: " + e.getMessage());
        }
        catch(NumberFormatException e){
            System.out.println("Incorrect input format " + e.getMessage());
        }
        finally{
            if (aSocket != null) {
                aSocket.close();
            }
        }
    }
}
