import TCP_UDP.UDPClient;
import domain.Message;
import scokets.UDPClientServer;
import utils.Utils;

import java.util.*;

public class SocketRing {

    public static void main(String[] args) throws InterruptedException {
        int port = 10000;

        //List<UDPClientServer> nodes = new ArrayList<UDPClientServer>();
        Map<Integer, UDPClientServer> nodes = new HashMap<Integer, UDPClientServer>();

        Integer[] a = {1,2,3,4,5};
        Collections.shuffle(Arrays.asList(a));
        System.out.println(Arrays.toString(a));

        for (int i = 0; i < a.length; i++) {
            UDPClientServer udpClientServer;
            if (i == a.length-1) {
                udpClientServer = new UDPClientServer(a[i], port + a[i], a[0], port + a[0]);
            }
            else {
                udpClientServer = new UDPClientServer(a[i], port + a[i], a[i+1], port + a[i+1]);
            }
            udpClientServer.start();
            nodes.put(a[i], udpClientServer);

            Thread.sleep(200);
        }

        Random random = new Random();
        int startingNode = random.nextInt(5) + 1;

        System.out.println("Election started by node: " + startingNode);
        Message messageToken = new Message(startingNode, Utils.ELECTION);
        nodes.get(startingNode).sendMessage(messageToken);

        System.out.println("Main Thread Closed");
    }
}
