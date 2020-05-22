package scokets;

import domain.Message;
import service.RingLogic;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClientServer extends Thread{

    private final int currentPort;
    private final int nextPort;
    private final int currentNodeId;
    private final int nextNodeId;

    private RingLogic ringLogic = new RingLogic();


    public UDPClientServer(int currentNodeId, int currentPort, int nextNodeId, int nextPort) {
        this.currentNodeId = currentNodeId;
        this.nextNodeId = nextNodeId;
        this.currentPort = currentPort;
        this.nextPort = nextPort;

    }


    @Override
    public void run() {
        receiveMessage();
    }

    public void sendMessage(int nodeId, byte status) {
        try {
            DatagramSocket aSocket = new DatagramSocket();

            InetAddress aHost = InetAddress.getByName("localhost");

            Message mObj = new Message(nodeId, status);

            // Serialization...
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(mObj);

            DatagramPacket request = new DatagramPacket(byteArrayOutputStream.toByteArray(),
                    byteArrayOutputStream.toByteArray().length, aHost, this.nextPort);

            aSocket.send(request);
        } catch (SocketException e) {
            System.out.println(" Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(" IO: " + e.getMessage());
        }
    }


    public void receiveMessage() {
        try {
            DatagramSocket aSocket = new DatagramSocket(this.currentPort);
            byte[] buffer = new byte[1000];
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);

                // Deserialization...
                Message messageToken = null;
                ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
                ObjectInputStream ois = new ObjectInputStream(bis);
                try {
                    messageToken = (Message) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                ringLogic.ringAlgorithm(this.currentNodeId, messageToken);

                //System.out.println(" Request: " + new String(request.getData(), 0, request.getLength()));

                /*DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),
                        request.getAddress(), request.getPort());
                aSocket.send(reply);*/
            }
        } catch (SocketException e) {
            System.out.println(" Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(" IO: " + e.getMessage());
        }
    }


}