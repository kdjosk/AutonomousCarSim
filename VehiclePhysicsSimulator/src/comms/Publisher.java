package comms;

import nav.MapState;

import javax.jms.*;
import javax.naming.InitialContext;
import java.io.Serializable;

public class Publisher {

    InitialContext ctx = null;
    TopicConnectionFactory tcf = null;
    TopicConnection tc = null;
    TopicSession ts = null;
    Topic t = null;
    TopicPublisher tpub = null;
    ObjectMessage objectMessage = null;

    public Publisher(){
        try{
            ctx = new InitialContext();
            tcf = (TopicConnectionFactory) ctx.lookup("myTopicConnectionFactory");
            tc = tcf.createTopicConnection();
            tc.start();

            ts = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            t = (Topic) ctx.lookup("jms/stateTopic");

            tpub = ts.createPublisher(t);

        } catch(Exception e){System.out.println(e.toString());}
    }

    public void publishMessage(Serializable msg){
        try{
            System.out.println("Attempting to send message");
            objectMessage = ts.createObjectMessage();
            objectMessage.setObject(msg);
            tpub.publish(objectMessage);
            System.out.println("Message successfully sent");
        }catch(JMSException e){System.out.println(e.toString());}

    }

    public void closeConnection(){
        try{ tc.close(); }
        catch(Exception e){ System.out.println(e.toString()); }
    }


}
