package autonomous.comms;

import javax.jms.*;
import javax.naming.InitialContext;
import java.io.Serializable;

public class Publisher {

    private InitialContext ctx = null;
    private TopicConnectionFactory tcf = null;
    private TopicConnection tc = null;
    private TopicSession ts = null;
    private Topic t = null;
    private TopicPublisher tpub = null;
    private ObjectMessage objectMessage = null;

    public Publisher(){
        try{
            ctx = new InitialContext();
            tcf = (TopicConnectionFactory) ctx.lookup("myTopicConnectionFactory");
            tc = tcf.createTopicConnection();
            tc.start();

            ts = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            t = (Topic) ctx.lookup("jms/controlsTopic");

            tpub = ts.createPublisher(t);

            objectMessage = ts.createObjectMessage();

        } catch(Exception e){System.out.println(e.toString());}
    }

    public void publishMessage(Serializable msg){
        try{
            objectMessage.setObject(msg);
            tpub.publish(objectMessage);
        }catch(JMSException e){System.out.println(e.toString());}

    }

    public void closeConnection(){
        try{ tc.close(); }
        catch(Exception e){ System.out.println(e.toString()); }
    }


}
