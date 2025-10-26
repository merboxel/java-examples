package merboxel.example.activeMQ.handler;

public interface ActiveMQTopicMessageHandler {
    public void handleMessage(String message);
}
