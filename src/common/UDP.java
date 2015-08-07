package common;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class UDP<T> {
	private int port;
	
	public UDP() {
		this.port = 9876;
	}
	
	public UDP(int port) {
		this.port = port;
	}	
	
    public boolean startServer(Consumer<T> callable) {
        DatagramSocket serverSocket = null;
        ByteArrayInputStream bis;
        ObjectInput in = null;
        try{
            System.out.println("binding port " + port);
        	serverSocket = new DatagramSocket(port);
            byte[] data;
            byte[] receiveData = new byte[1024];
            while (true) { //TODO: we need a way to teardown the servers
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
        } catch(ClassCastException cce) {
        	cce.printStackTrace();
        }
        		finally {
        			System.out.println("unbinding port " + port);
            serverSocket.close();
        }

        return true;
    }

    public boolean send(T t){
    	return send(t, "localhost", 9876);
    }
    
    public boolean send(T t, String ip, int port) {
        int attempts = 0;  // TODO: You cannot recursively use this function and then reset attempts to 0

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
            System.out.println(iioe.getMessage());
            if(attempts > 3) {
                System.out.println("Failed to send message multiple times...giving up");
                return false;
            } else {
                return send(t);
            }
        } catch(UnknownHostException he){
            System.out.println("Host Exception in UDP: " + he.getMessage());
            return false;
        } catch (IOException ioe) {
            System.out.println("IO Exception in UDP: " + ioe.getMessage());
            return false;
        } finally {
            //      clientSocket.close();
        }
        return true;
    }


}
