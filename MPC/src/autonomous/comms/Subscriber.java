package autonomous.comms;

import javax.jms.*;
import javax.naming.InitialContext;

public class Subscriber {

    private InitialContext ctx = null;
    private TopicConnectionFactory tcf = null;
    private TopicConnection tc = null;
    private TopicSession ts = null;
    private TopicSubscriber tsub = null;
    private Topic t = null;
    private Listener listener = null;

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

        } catch (Exception e){System.out.println(e.toString());}
    }


    public ObjectMessage getMessage() {
        return listener.getMessage();
    }

}
