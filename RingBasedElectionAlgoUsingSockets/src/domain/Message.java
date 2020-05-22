package domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Message implements Serializable {

    private int nodeId;

    private byte status;

    private Set participated = new HashSet();

    public Message() {}

    public Message(int nodeId, byte status) {
        this.nodeId = nodeId;
        this.status = status;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void addInParticipated(int nodeId) {
        participated.add(nodeId);
    }

    public void deleteFromParticipated(int nodeId) {
        participated.remove(nodeId);
    }

    public boolean isParticipated(int nodeId) {
        return participated.contains(nodeId);
    }

    public int getNodeId() {
        return nodeId;
    }

    public byte getStatus() {
        return status;
    }
}
