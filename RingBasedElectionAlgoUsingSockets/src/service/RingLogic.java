package service;

import domain.Message;
import utils.Utils;

public class RingLogic {

    public Message ringAlgorithm(int currentNodeId, Message messageToken, int nextNodeId) {

        if (Utils.ELECTION == messageToken.getStatus()) {
            if (currentNodeId < messageToken.getNodeId()) {
                System.out.println(currentNodeId + " (Current Node ID) :: <<<ELECTION  " + messageToken.getNodeId() +
                        ">>> message passed to next Node ID" +
                        " " +
                        "==> " + nextNodeId);

                messageToken.addInParticipated(currentNodeId);

            } else if (currentNodeId > messageToken.getNodeId()) {

                if (!messageToken.isParticipated(currentNodeId)) {
                    messageToken.addInParticipated(currentNodeId);
                    messageToken.setNodeId(currentNodeId);

                    System.out.println(currentNodeId + " (Current Node ID) :: Looks like I should be nominated," + "So " +
                            "<<<ELECTION "
                            + currentNodeId + ">>> message passed to next Node ID ==> " + nextNodeId);
                }
                else {
                    return null;
                }

            } else if (currentNodeId == messageToken.getNodeId()) {
                System.out.println(currentNodeId + " (Current Node ID) :: Hurray, I am elected. <<<ELECTED "
                        + messageToken.getNodeId() + ">>> " + "message passed on" + " " + "to everyone :)");

                if (messageToken.isParticipated(currentNodeId)) {
                    messageToken.deleteFromParticipated(currentNodeId);
                }

                messageToken.setNodeId(currentNodeId);
                messageToken.setStatus(Utils.ELECTED);

            }

        } else if (Utils.ELECTED == messageToken.getStatus()) { // Elected
            if (currentNodeId != messageToken.getNodeId()) {
                System.out.println(currentNodeId + " (Current Node ID) :: {{{ELECTED " + messageToken.getNodeId() +
                        "}}}  " +
                        "message set");
                messageToken.deleteFromParticipated(currentNodeId);
            }
            else {
                return null;
            }
        }

        return messageToken;
    }
}
