import java.util.HashMap;
import java.util.Map;

public class SocketRing {

    public static void main(String[] args) {

        Map idToPort = new HashMap();

        int port = 10001;
        for (int i = 1; i <= 5; i++) {
            if (i == 5) {
                idToPort.put(i, port - i);
                break;
            }
            idToPort.put(i, ++port);
        }
        System.out.println("Hello world");

    }
}
