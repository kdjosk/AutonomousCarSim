package autonomous.comms;

import javax.jms.*;
import javax.naming.InitialContext;

public class Subscriber {

    InitialContext ctx = null;
    TopicConnectionFactory tcf = null;
    TopicConnection tc = null;
    TopicSession ts = null;
    TopicSubscriber tsub = null;
    Topic t = null;
    Listener listener = null;

    public Subscriber(){
        try {
            ctx = new InitialContext();
            tcf = (TopicConnectionFactory) ctx.lookup("myTopicConnectionFactory");
            tc = tcf.createTopicConnection();
            tc.start();

            ts = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            t = (Topic) ctx.lookup("jms/stateTopic");

            tsub = ts.createSubscriber(t);

            listener = new Listener();

            tsub.setMessageListener(listener);

            //System.out.println("Subscriber is ready, waiting for messages...");

        } catch (Exception e){System.out.println(e.toString());}
    }


    public ObjectMessage getMessage() {
        return listener.getMessage();
    }

}
