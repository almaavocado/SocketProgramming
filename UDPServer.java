import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * The UDP Server will communicate and connect with the client using a socket
 */

public class UDPServer {

    public static void main(String[] args) {
        System.out.println("---- UDP Server ---- ");
        Scanner input = new Scanner(System.in);

        DatagramSocket aSocket = null;

        try{
            System.out.println("Enter the port number: ");
            System.out.println("Test with port number 6789");
            int portNumber = input.nextInt();
            System.out.println("Port Number You Entered: " + portNumber);
            System.out.println("Waiting for UDP client: ");
            aSocket = new DatagramSocket(portNumber);

            // Requirement #3:
            // The server should be able to send multiple messages to the client. You may
            // need to consider using the infinite loop as we discussed in the class
            while(true){

                // buffer
                byte[] buffer = new byte[1000];


                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request); // receive request

                // Requirement #1: Receives the message from the client
                String messageReceived = new String(request.getData());

                // Let the client know what the message received was
                System.out.println("Message Received : " + messageReceived);

                // Requirement #2: Change the letters of the message to “capital letters” and send it back to the
                //client by using the same socket
                messageReceived = messageReceived.toUpperCase();


                //message received to bytes
                byte [] messageBytes = messageReceived.getBytes();

                // Send the reply back to the client in a packet
                System.out.println("Sending Reply: " + messageReceived);

                // create reply
                DatagramPacket reply = new DatagramPacket(messageBytes, messageBytes.length, request.getAddress(), request.getPort());

                // send reply
                aSocket.send(reply);

            }
        }
        catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }
        catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }
        finally {
            if(aSocket != null){
                aSocket.close();
            }
        }
    }
}
