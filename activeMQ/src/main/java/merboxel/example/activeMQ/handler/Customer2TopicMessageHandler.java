package merboxel.example.activeMQ.handler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Customer2TopicMessageHandler implements ActiveMQTopicMessageHandler {

    public static final List<String> RECEIVED = new CopyOnWriteArrayList<>();

    @Override
    public void handleMessage(String message) {
        System.out.printf("[Customer2] (%s) \n\r", message);
        RECEIVED.add(message);
    }
}
