package server;

import java.util.HashMap;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.io.IOException;
import java.net.SocketException;

public class Server {

    protected DatagramSocket socket = null;

    public Server() throws SocketException{
        socket = new DatagramSocket(1027);
    }

    public void execute_ALO() throws IOException {
        while(true) {
            byte[] buffer = new byte[200];

            DatagramPacket client_packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(client_packet);

            byte[] reply_buffer = {}; //reply_buffer = perform_request() function, marshalling and unmarshalling also to be performed within this function
            //code from github for reference
            //byte[] aryOutput = CServerManager.performOperation(objPacket.getData(), objPacket.getAddress().getHostAddress()); 

            InetAddress client_address = client_packet.getAddress();

            int client_port = client_packet.getPort();

            DatagramPacket reply_packet = new DatagramPacket(reply_buffer, reply_buffer.length, client_address, client_port);

            socket.send(reply_packet);
        }
    }
     
    public void execute_AMO() throws IOException {

        HashMap<InetAddress, String> requests = new HashMap<InetAddress, String>();
        HashMap<InetAddress, DatagramPacket> reply_messages = new HashMap<InetAddress, DatagramPacket>();

        while(true) {
            byte[] buffer = new byte[200];
            
            DatagramPacket client_packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(client_packet);

            InetAddress client_address = client_packet.getAddress();

            int client_port = client_packet.getPort();

            String requestID = ""; //to be updated later, retrieve the request ID from the client_packet

            boolean is_duplicate_request = true;

            if (requests.containsKey(client_address)) { //if not a new client
                //check for duplicate request
                String previous_requestID = requests.get(client_address);
                if (requestID != previous_requestID) {
                    is_duplicate_request = false;
                    //else duplicate request = true
                }
            }

            if (is_duplicate_request) {
                DatagramPacket reply_packet = reply_messages.get(client_address);
                socket.send(reply_packet);
            }
            else { //!is_duplicate_request
                //execute request
                byte[] reply_buffer = {}; //to be updated later, execute request and return the reply buffer. marshalling and unmarshalling also to be performed here
                //code from github for reference
                //byte[] aryOutput = CServerManager.performOperation(objPacket.getData(), objPacket.getAddress().getHostAddress()); 

                DatagramPacket reply_packet = new DatagramPacket(reply_buffer, reply_buffer.length, client_address, client_port);
                
                requests.put(client_address, requestID); //store request ID
                reply_messages.put(client_address, reply_packet); //store reply message

                socket.send(reply_packet); //send reply to client
            }
        
        }
    }

}
