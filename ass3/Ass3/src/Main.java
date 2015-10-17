import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	static ArrayList<Stat> Stats = new ArrayList<Stat>();
	static ArrayList<Event> Events = new ArrayList<Event>();
	static int nrOfStats;
	static int nrOfEvents;
	
	public static void main(String[] args) throws FileNotFoundException{
		getStats();
		getEvents();
		System.out.println(Stats);
		System.out.println(Events);
		
		

	}
	
	public static void getStats() throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader("Stats.txt"));
		nrOfStats = Integer.parseInt(in.nextLine());
		while(in.hasNext()){
			String ny = in.nextLine();
			String[] nyy = ny.split(":");
			Stats.add(new Stat(nyy[0], parseD(nyy[1]), parseD(nyy[2])));
		}
	}
	
	public static void getEvents() throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader("Events.txt"));
		nrOfEvents = Integer.parseInt(in.nextLine());
		while(in.hasNext()){
			String ny = in.nextLine();
			String[] nyy = ny.split(":");
			Events.add(new Event(nyy[0], nyy[1], parseD(nyy[2]), parseD(nyy[3]), nyy[4], parseI(nyy[5])));
		}
	}
	
	public static double parseD(String s){
		if(!s.isEmpty()){
		return Double.parseDouble(s);
		}
		else{
		return 0;
		}
	}
	
	public static int parseI(String s){
		if(!s.isEmpty()){
		return Integer.parseInt(s);
		}
		else{
		return 0;
		}
	}
	
		
	

}
