package holdem.entity.dawson;

import holdem.engine.dawson.Blind;
import holdem.hud.dawson.Text;
import holdem.utils.dawson.Color;

public class PlayerInfo {
	private final String name;
	private double onTable;
	private double money;
	private Blind blind = Blind.NONE;
	
	public PlayerInfo(String name, double startingMoney) {
		this.name = name;
		this.money = startingMoney;
		this.onTable = 0;
	}

	public boolean canSpend(double amount) {
		return (this.money - amount > 0);
	}

	public String getName() {
		return this.name;
	}


	public void incrementMoney(double quantity) {
	    this.money += quantity;
	}
	
	public String[] asStringArray() {
		String array[] = new String[4];
		array[0] = "Name: " + this.name;
		array[1] = "Money: " + Double.toString(this.money);
		array[2] = "On table: " + Double.toString(onTable);
		if (this.getBlind() == Blind.NONE) {
			array[3] = "";
		} else {
			array[3] = "Blind " + this.blind.name();
		}
		return array;
	}

	public Text[] asTextArray(Color color) {
		Text array[] = new Text[4];
		array[0] = new Text("Name: " + this.name, color);
		array[1] = new Text("Money: " + Double.toString(this.money), color);
		array[2] = new Text("On table: " + Double.toString(onTable), color);
		if (this.getBlind() == Blind.NONE) {
			array[3] = new Text("", color);
		} else {
			array[3] = new Text("Blind: " + this.blind.name(), Color.GREEN);
		}
		return array;
	}

	public void setBlind(Blind blind) {
		this.blind = blind;
	}

	public Blind getBlind() {
		return this.blind;
	}

	public double getMoney() {
		return this.money;
	}

	public void incrementOnTable(double quantity) {
		this.onTable += quantity;
	}

	public void setOnTable(double quantity) {
		this.onTable = quantity;
	}

	public double getOnTable() {
		return this.onTable;
	}
}
