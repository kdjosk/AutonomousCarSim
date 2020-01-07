package comms;

import nav.*;
import javax.jms.*;
import javax.naming.InitialContext;

public class Subscriber {

    InitialContext ctx = null;
    TopicConnectionFactory tcf = null;
    TopicConnection tc = null;
    TopicSession ts = null;
    TopicSubscriber tsub = null;
    Topic t;
    Listener listener = null;

    public Subscriber(){
        try {
            InitialContext ctx = new InitialContext();
            TopicConnectionFactory f = (TopicConnectionFactory) ctx.lookup("myTopicConnectionFactory");
            TopicConnection con = f.createTopicConnection();
            con.start();

            TopicSession ses = con.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic t = (Topic) ctx.lookup("myTopic");

            TopicSubscriber receiver = ses.createSubscriber(t);

            Listener listener = new Listener();

            System.out.println("Subscriber is ready, waiting for messages...");

        } catch (Exception e){System.out.println(e.toString());}
    }


    public ObjectMessage getMessage() {
        return listener.getMessage();
    }

}
