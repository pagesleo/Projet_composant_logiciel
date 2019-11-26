package monkeys;

import java.io.IOException;

import javax.ejb.Local;

@Local
public interface ConfigurationLocal {
	
	public Island getMap()throws IOException;
	
	public int getServerPort();
	
	public int getTailleGrille();
	
	public int getNbWaterSlot();
	
	public int getNbSingeErratic();
	
	public int getNbSingeChasseur();
}
