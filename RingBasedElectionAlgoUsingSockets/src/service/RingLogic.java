package service;

import domain.Message;
import utils.Utils;

public class RingLogic {

    public Message ringAlgorithm(int currentNodeId, Message messageToken) {

        if (Utils.ELECTION == messageToken.getStatus()) {
            if (currentNodeId < messageToken.getNodeId()) {
                /*System.out.println(currentNodeId + "(my ID) :: <<<ELECTION>>> message passed on to next Processor ID " +
                        "==> " + NEXT_ID);*/

                messageToken.addInParticipated(currentNodeId);

            } else if (currentNodeId > messageToken.getNodeId()) {
                /*System.out.println(currentNodeId + "(my ID) :: Looks like I should be nominated," + "So <<<ELECTION" +
                        ">>> message passed on to next Processor ID ==> " + NEXT_ID);*/

                if (!messageToken.isParticipated(currentNodeId)) {
                    messageToken.addInParticipated(currentNodeId);
                    messageToken.setNodeId(currentNodeId);
                }
                else {
                    return null;
                }

            } else if (currentNodeId == messageToken.getNodeId()) {
                System.out.println(currentNodeId + "(my ID) :: Hurray, I am elected. <<<ELECTED>>> message passed on " +
                        "to everyone :)");

                if (messageToken.isParticipated(currentNodeId)) {
                    messageToken.deleteFromParticipated(currentNodeId);
                }

                messageToken.setNodeId(currentNodeId);
                messageToken.setStatus(Utils.ELECTED);

            }

        } else if (Utils.ELECTED == messageToken.getStatus()) { // Elected
            if (currentNodeId != messageToken.getNodeId()) {
                System.out.println(currentNodeId + "(my ID) :: {{{ELECTED " + messageToken.getNodeId() + "}}}  " +
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
