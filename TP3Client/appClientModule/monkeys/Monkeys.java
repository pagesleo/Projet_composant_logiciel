package monkeys;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.StreamMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;

import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

import guybrush.view.Fenetre;
import guybrush.view.GameObserver;
import monkeys.MIRemote;

/**
 * @author Mickael Clavreul
 */
public class Monkeys implements MessageListener, GameObserver  {
	
	static MonkeyIsland island;
	private static Monkeys instance;
	static int id;
	static Fenetre fen;
	static TopicConnection topicCo;
	
	public static void main(String[] args) throws Exception {
		
		fen = new Fenetre("MonkeyIsland");
		instance = new Monkeys();
		topicCo = subscribeTopic();
		topicCo.start();

		MIRemote rm = lookup("ejb:/TP3Server/MonkeyIsland!monkeys.MIRemote?stateful");
		rm.subscribe("1", "2");
		
		id = instance.hashCode();
		
		fen.addObserver(instance);
		fen.setVisible(true);
		//topicCo.close();
		

	}

	public Monkeys() {
		super();
	}
	
	private static TopicConnection subscribeTopic() throws IOException, NamingException, JMSException {
		String file = "META-INF/jndi-topic-client.properties";
    	InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    	Properties properties = new Properties();
    	properties.load(is);
    	
    	Context initContext = new InitialContext(properties);
    	String connectionFactoryURI = properties.getProperty("connectionFactoryURI");
    	String topicURI = properties.getProperty("topicURI");
    	
    	TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) initContext.lookup(connectionFactoryURI);
    	Topic topic = (Topic) initContext.lookup(topicURI);
    	
    	String username = properties.getProperty("java.naming.security.principal");
    	String password = properties.getProperty("java.naming.security.credentials");
    	
    	TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username, password);
    	
    	TopicSession topicSession = topicConnection.createTopicSession(false, 0);
    	
    	MessageConsumer messageConsumer = topicSession.createSharedConsumer(topic, String.valueOf(id));
    	messageConsumer.setMessageListener(instance);
        	
    	return topicConnection;
    	
	}
	
	private static MIRemote lookup(String url) throws Exception {
		
		Properties properties = new Properties();
		
		properties.put("java.remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
		properties.put(Context.URL_PKG_PREFIXES,"org.jboss.ejb.client.naming");
		properties.put("remote.connections","default");
		properties.put("remote.connection.default.host","localhost");
		properties.put("remote.connection.default.port","8080");
		properties.put("remote.connection.default.username","alevanschu");
		properties.put("remote.connection.default.password","network");

		EJBClientConfiguration eJBClientConfiguration = new PropertiesBasedEJBClientConfiguration(properties);
		ConfigBasedEJBClientContextSelector configSelector = new ConfigBasedEJBClientContextSelector(eJBClientConfiguration);
		EJBClientContext.setSelector(configSelector);
		Context context = new InitialContext(properties);
		MIRemote remoteWelcome = (MIRemote) context.lookup(url);

		return remoteWelcome;
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			if (message.getJMSType().equals("map")) {
				System.out.println(message);
				StreamMessage msg = (StreamMessage) message;
				int tailleMatrice = msg.readInt();
				int[][] matrice = new int [tailleMatrice][tailleMatrice];
				for (int i = 0; i < tailleMatrice; i++) {
					for(int j = 0; j<tailleMatrice; j++) {
						matrice[i][j] = msg.readInt();
					}
				}
				fen.creationCarte(matrice);
				fen.repaint();

			};
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void notifyDisconnect() {
		// TODO Auto-generated method stub
		fen.dispose();
		
	}

	@Override
	public void notifyMove(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}