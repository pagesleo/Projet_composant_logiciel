package monkeys;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.StreamMessage;
import javax.jms.Topic;

/**
 * Session Bean implementation class Communication
 */
@Stateless
@LocalBean
public class Communication implements CommunicationLocal {

	@Inject
	private JMSContext context;
	
	@Resource(mappedName = "java:jboss/exported/topic/monkeys")
    private Topic topic;
	
    /**
     * Default constructor. 
     */
    public Communication() {
    }

	@Override
	public void sendMap(int[][] map, String id) {
		sendIntArrayMessage(map, id, "map");
	}
	
	private void sendIntArrayMessage(int[][] array, String id, String type){
    	StreamMessage message = context.createStreamMessage();
    	try {
    		message.setStringProperty("id", id);
    		message.setJMSType(type);
    		message.writeInt(array.length);
    		for(int i=0;i<array.length;i++){
    			for(int j=0;j<array[i].length;j++){
            		message.writeInt(array[i][j]);
    			}
    		}
		} catch (JMSException e) {
			e.printStackTrace();
		}
    	context.createProducer().send(topic, message);
    }
	
	public void sendPirate(String id, int posX, int posY, int energy, String type ) {
    	StreamMessage message = context.createStreamMessage();
    	
    	try {
    		message.setStringProperty("id", id);
    		message.setStringProperty("posX", String.valueOf(posX));
    		message.setStringProperty("posY", String.valueOf(posY));
    		message.setJMSType(type);
    		    		
		} catch (JMSException e) {
			e.printStackTrace();
		}
    	context.createProducer().send(topic, message);
	}
}