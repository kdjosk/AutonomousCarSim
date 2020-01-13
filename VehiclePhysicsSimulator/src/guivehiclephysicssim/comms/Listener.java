package guivehiclephysicssim.comms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class Listener implements MessageListener {

    private ObjectMessage message;

    public Listener(){
        ObjectMessage message = null;
    }

    @Override
    public void onMessage(Message msg) {
        message = (ObjectMessage) msg;
    }

    public ObjectMessage getMessage(){
        return message;
    }
}