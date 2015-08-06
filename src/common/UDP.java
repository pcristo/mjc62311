package common;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class UDP<T> {

    public boolean startServer(Consumer<T> callable) {
        DatagramSocket serverSocket = null;
        ByteArrayInputStream bis;
        ObjectInput in = null;
        try{
            serverSocket = new DatagramSocket(9876);
            byte[] data;
            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                data = new byte[receivePacket.getLength()];
                System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());

                bis = new ByteArrayInputStream(data);
                in = new ObjectInputStream(bis);
                T o = (T) in.readObject();
                new Thread(() -> {
                    callable.accept(o);
                }).start();


                serverSocket.send(receivePacket);

            }
        } catch(IOException ioe) {
            System.out.println("IO Exception in host: " + ioe.getMessage());
        } catch(ClassNotFoundException cne){
            cne.printStackTrace();
        } finally {
            serverSocket.close();
        }

        return true;
    }

    public boolean send(T t) {

        String ip = "localhost";
        int port = 9876;
        int attempts = 0;

        ByteArrayOutputStream bos;
        ObjectOutput out = null;

        try {
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(2500);
            byte[] sendData = new byte[1024];

            bos = new ByteArrayOutputStream();

            out = new ObjectOutputStream(bos);
            out.writeObject(t);
            sendData = bos.toByteArray();

            InetAddress host = InetAddress.getByName(ip);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
            clientSocket.send(sendPacket);

            byte[] buffer2 = new byte[1024];
            DatagramPacket receivedPacket = new DatagramPacket(buffer2, sendData.length, host, port);
            clientSocket.receive(receivedPacket);
            clientSocket.close();
        }catch (InterruptedIOException iioe) {
            attempts++;
            System.out.println("Remote connection exception - resending attempts number " + attempts + ":");
            if(attempts > 3) {
                System.out.println("Failed to send message multiple times...giving up");
                return false;
            } else {
                return send(t);
            }
        } catch(UnknownHostException he){
            System.out.println("Host Exception in common.logger client: " + he.getMessage());
            return false;
        } catch (IOException ioe) {
            System.out.println("IO Exception in common.logger client: " + ioe.getMessage());
            return false;
        } finally {
            //      clientSocket.close();
        }
        return true;
    }


}
