
public class Event {
	String name;
	String type;
	double max;
	double min;
	String unit;
	int weight;
	
	public Event(String name, String type, double max, double min, String unit, int weight) {
		this.name = name;
		this.type = type;
		this.max = max;
		this.min = min;
		this.unit = unit;
		this.weight = weight;
	}
	
	public String toString(){
		return name;
	}
	

}