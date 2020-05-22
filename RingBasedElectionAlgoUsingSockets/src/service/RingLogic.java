package service;

import domain.Message;
import utils.Utils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class RingLogic {

    public Message ringAlgorithm(int currentNodeId, Message messageToken, int nextNodeId) {

        try {
            if (Utils.ELECTION == messageToken.getStatus()) {
                if (currentNodeId < messageToken.getNodeId()) {
                    System.out.println(currentNodeId + " (Current Node ID) :: <<<ELECTION  " + messageToken.getNodeId() + ">>> message passed to next Node ID" + " " + "==> " + nextNodeId);

                    this.addInParticipated(currentNodeId);

                    Thread.sleep(2000);

                } else if (currentNodeId > messageToken.getNodeId()) {

                    if (!this.isParticipated(currentNodeId)) {
                        this.addInParticipated(currentNodeId);
                        messageToken.setNodeId(currentNodeId);

                        System.out.println(currentNodeId + " (Current Node ID) :: Looks like I should be nominated," + "So " + "<<<ELECTION " + currentNodeId + ">>> message passed to next Node ID ==> " + nextNodeId);
                    } else {
                        System.out.println(currentNodeId + " (Current Node ID) :: Already Participate, So not passing" +
                                " token to next node");

                        return null;
                    }

                    Thread.sleep(2000);

                } else if (currentNodeId == messageToken.getNodeId()) {
                    System.out.println(currentNodeId + " (Current Node ID) :: Hurray, I am elected. <<<ELECTED " + messageToken.getNodeId() + ">>> " + "message passed on" + " " + "to everyone :)");

                    if (this.isParticipated(currentNodeId)) {
                        this.deleteFromParticipated(currentNodeId);
                    }

                    messageToken.setNodeId(currentNodeId);
                    messageToken.setStatus(Utils.ELECTED);

                    Thread.sleep(2000);
                }

            } else if (Utils.ELECTED == messageToken.getStatus()) { // Elected
                if (currentNodeId != messageToken.getNodeId()) {
                    System.out.println(currentNodeId + " (Current Node ID) :: {{{ELECTED " + messageToken.getNodeId() + "}}}  " + "message set");
                    this.deleteFromParticipated(currentNodeId);
                } else {
                    return null;
                }

                Thread.sleep(2000);
            }

            return messageToken;
        }
        catch (InterruptedException e) {
           return null;
        }
    }


    private Set getParticipatedNodeIds() {
        try {
            FileInputStream fis = new FileInputStream("participants");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Set<Integer> participants;
            participants = (HashSet) ois.readObject();

            ois.close();
            fis.close();

            return participants;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }
    }


    public void setParticipatedNodeIds(Set participated) {
        try {
            FileOutputStream fos = new FileOutputStream("participants");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(participated);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    private void addInParticipated(int nodeId) {
        Set participatedNodeIds = getParticipatedNodeIds();
        participatedNodeIds.add(nodeId);
        setParticipatedNodeIds(participatedNodeIds);
    }

    private void deleteFromParticipated(int nodeId) {
        Set participatedNodeIds = getParticipatedNodeIds();
        participatedNodeIds.remove(nodeId);
        setParticipatedNodeIds(participatedNodeIds);
    }

    public boolean isParticipated(int nodeId) {
        Set participatedNodeIds = getParticipatedNodeIds();

        return participatedNodeIds.contains(nodeId);
    }
}
