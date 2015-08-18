package replication.messageObjects;

import java.io.Serializable;
import java.util.HashMap;

public class PortMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	HashMap<Long, Integer> ports;

    public PortMessage(HashMap<Long, Integer> p) {
        ports = p;
    }

    public HashMap<Long, Integer> getPorts() {
        return ports;
    }



}
