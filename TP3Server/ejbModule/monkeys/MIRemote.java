package monkeys;

import javax.ejb.Remote;

/**
 * @author Mickael Clavreul
 */
@Remote
public interface MIRemote {
	
	public void subscribe(String id, String idPirate);
	public void disconnect(String pId);
}
