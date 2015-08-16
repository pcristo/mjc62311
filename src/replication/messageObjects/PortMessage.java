package replication.messageObjects;

import java.io.Serializable;
import java.util.HashMap;

public class PortMessage implements Serializable{

    HashMap<Long, Integer> ports;

    public PortMessage(HashMap<Long, Integer> p) {
        ports = p;
    }

    public HashMap<Long, Integer> getPorts() {
        return ports;
    }



}
