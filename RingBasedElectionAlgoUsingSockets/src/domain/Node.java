package domain;

public class Node {

    private int id;

    private int port;

    private boolean participated = false;

    private Node next;

    private Node(int nodeId) {
        this.id = nodeId;
    }

    public Node(int nodeId, int port, int nextId) {
        this.id = nodeId;
        this.port = port;
        this.next = new Node(nextId);
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public boolean isParticipated() {
        return participated;
    }

    public Node getNext() {
        return next;
    }
}
