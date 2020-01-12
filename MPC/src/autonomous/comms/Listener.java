package autonomous.comms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class Listener implements MessageListener {

    ObjectMessage message;

    public Listener(){
        message = null;
    }

    @Override
    public void onMessage(Message msg) {
        message = (ObjectMessage) msg;
    }

    public ObjectMessage getMessage(){
        return message;
    }
}