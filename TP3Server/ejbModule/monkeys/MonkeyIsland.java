package monkeys;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class MonkeyIsland
 * @author Mickael Clavreul
 */
@Stateful
public class MonkeyIsland implements MIRemote {

	private Island myIsland;
	
	private Pirate pirate;
	
	@EJB
	private Communication comm;
	
	@EJB
	private Configuration config;
	
	@PersistenceContext (unitName="MonkeysDS")
	private EntityManager manager;
	
	
	/**
     * Default constructor
     */
    public MonkeyIsland() {}

	@Override
	public void subscribe(String id, String idPirate) {
	try {
		newGame(id);
		newPirate(idPirate);

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	@Override
	public void disconnect(String id) {
		
	}

	private void newGame(String idIsland) throws IOException {
		if (manager.find(Island.class,Integer.valueOf(idIsland)) != null) {
			myIsland = manager.find(Island.class,Integer.valueOf(idIsland));
			System.out.println("id : " + myIsland.getId() + " map : " + myIsland.getMap());
		}else {
			myIsland = config.getMap();
			//myIsland.setId(Integer.valueOf(idIsland));
			manager.persist(myIsland);
		}
		comm.sendMap(myIsland.getMap(), String.valueOf(manager.find(Island.class,Integer.valueOf(idIsland)).getId()));
		
		System.out.println(myIsland.toString());
	}
	
	private void newPirate(String idPirate) {
		if (manager.find(Island.class,Integer.valueOf(idPirate)) != null){
			pirate = manager.find(Pirate.class,Integer.valueOf(idPirate));
		}else {
			pirate = new Pirate(1, 1, 100);
			manager.persist(pirate);
		}
		
		comm.sendPirate(idPirate, 2, 2, 100, "pirate");
		
	}
}
