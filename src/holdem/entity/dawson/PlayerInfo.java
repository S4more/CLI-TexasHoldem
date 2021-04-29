package holdem.entity.dawson;

public class PlayerInfo {
	private final String name;
	private double money;
	
	public PlayerInfo(String name, double startingMoney) {
		this.name = name;
		this.money = startingMoney; 
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getMoney() {
		return this.money;
	}
	
	public void incrementMoney(double quantity) {
		this.incrementMoney(quantity);
	}
	
	public String[] asStringArray() {
		String array[] = new String[2];
		array[0] = "Name: " + this.name;
		array[1] = "Money: " + Double.toString(this.money);
		return array;	
	}
}
