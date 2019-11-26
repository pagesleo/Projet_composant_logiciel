package monkeys;

import javax.persistence.Entity;

@Entity
public class Pirate extends Element {

	int energy;
	
	public Pirate(int posX, int posY, int energy) {
		super();
		setEnergy(energy);
		setPosX(posX);
		setPosY(posY);
		// TODO Auto-generated constructor stub
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
}
