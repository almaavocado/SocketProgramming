import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * This is the UDP Client that will connect to a server with a Socket
 * Needs the IP and port of the server
 * It will then create a packet with a message to send to the Server
 * This will continue until user says no more.
 * It will also validate the IP address and port entered by the user
 *
 */
public class UDPClient {

    /**
     * This method is used to check if the port is in use so that we can
     * determine if the user inputted the correct port number to the server
     * @param port number entered by the user
     * @throws SocketException if it throws that means there is a port already in use which is good for us
     */
    private static void usedPort(int port)throws SocketException{
        DatagramSocket soc = new DatagramSocket(port);
        soc.close();
    }

    /**
     * This is used to validate the ip address so that it makes sure it is
     * in the correct format and exists
     */
    private static final String IPV4_REGEX =
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private static final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);


    public static void main(String[] args) {
        //args give message contents and server hostname
        Scanner in = new Scanner(System.in);
        boolean nextM = true;   //want another message to send?
        String userMess; //message

        //Sockets are used for the connection between the Client and the Server
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();

            //Prompt user to input the IP address
            System.out.println("Please enter the IP address: ");
            String IPaddress = in.nextLine();

            //Assign the IP address host and check if it is a valid ip address
            InetAddress aHost = InetAddress.getByName(IPaddress);




////        ANOTHER WAY TO CHECK IF IP ADDRESS IS CORRECT
//            if(!aHost.isReachable(5)){      //time out after 5 sec.
//                System.out.println("IP address is not reachable");
//                return;
//            }


/////////////////////////////////////////////////////////////////
            //IP ADDRESS VALIDATION PORTION

            //Check if the user inputted IP address is valid
            Matcher matcher = IPv4_PATTERN.matcher(IPaddress);
            while(matcher.matches() == false){
                System.out.println("Invalid IP address");
                System.out.println("Please enter the IP address: ");
                IPaddress = in.nextLine();
                matcher = IPv4_PATTERN.matcher(IPaddress);
            }

//////////////////////////////////////////////////////////////////


            //Prompt user to input the port number of the server
            System.out.println("Please enter the port number of the server: ");

            int serverPort = Integer.parseInt(in.nextLine());


/*
///////////////////////////////////////////////////////////////////
            //SERVER VALIDATION PORTION

            //Test if the port is used
            boolean correctPort = false;
            try{
                usedPort(serverPort);   //call usedPort method check if its used
            }
            catch (SocketException e){
                correctPort = true;     //its used so its good
            }
            // IF there was no throw that means the number entered is not a port in use so its wrong
            if(correctPort == false){
                System.out.println("The port number entered is incorrect. ");
                return;     //no good the port is not used
            }

//////////////////////////////////////////////////////////////////
*/


            //While loop to get user message as long as they want to send more
            while (nextM == true) {
                //Prompt user to enter message to send to the server

                System.out.println("Enter a message to send to the server: ");
                userMess = in.nextLine();       //enter message

                byte[] m = userMess.getBytes();     //convert message to bytes

                //Sends the request to the server
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);

                aSocket.send(request);      //send

                byte[] buffer = new byte[1000];

                //Sends the reply to the server receive reply from server
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);
                System.out.println("Reply: " + new String(reply.getData()));

                //Send another message? No ends the loop and program
                char answer = ' ';
                while(answer != 'y' && answer != 'n') {
                    System.out.println("Would you like to send another message? (y/n)");
                    answer = in.nextLine().charAt(0);

                    if (answer == 'y') {
                        nextM = true;
                    } else if (answer == 'n') {
                        nextM = false;
                    }
                    else {
                        System.out.println("You entered the wrong character. Try again. ");
                    }
                }
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

