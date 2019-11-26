package monkeys;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class Configuration
 */
@Stateless
@LocalBean
public class Configuration implements ConfigurationLocal {

    /**
     * Default constructor. 
     */
    public Configuration() {
        // TODO Auto-generated constructor stub
    	
    }
    
    @Override
    public Island getMap() throws IOException {
    	Island island = new Island();
    	
    	String file = "monkeys.properties";
    	InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    	Properties properties = new Properties();
    	properties.load(is);
    	
    	//properties.load(new FileInputStream(file));
    	String mapString = properties.getProperty("MONKEYS_MAP");
    	mapString = mapString.replace("\"","");
    	String[] partsLine = mapString.split(";");

    	int[][] map = new int [partsLine.length][partsLine.length];
    	
    	for (int i = 0;i<partsLine.length;i++) {
    		String[] partsCase = partsLine[i].split(",");
    		for (int j=0;j<partsCase.length;j++) {
    			map[i][j] = Integer.parseInt(partsCase[j]);
    		}
    	}
    	
    	island.setMap(map);
    	
    	return island;
    	
    }

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTailleGrille() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNbWaterSlot() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNbSingeErratic() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNbSingeChasseur() {
		// TODO Auto-generated method stub
		return 0;
	}

}
