package comms;
import javax.jms.*;
import nav.MapState;

public class Listener implements MessageListener {

    ObjectMessage message;

    public Listener(){
        message = null;
    }

    @Override
    public void onMessage(Message msg) {
        message = (ObjectMessage) msg;
        System.out.println("Message is received");
    }

    public ObjectMessage getMessage(){
        return message;
    }
}