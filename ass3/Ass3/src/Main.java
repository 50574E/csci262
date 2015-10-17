import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;


public class Main {
	static ArrayList<Stat> Stats = new ArrayList<Stat>();
	static ArrayList<Event> Events = new ArrayList<Event>();
	static int nrOfStats;
	static int nrOfEvents;
	// Creates days.txt and appends the names of events to top line
	PrintWriter day = new PrintWriter("days.txt", "UTF-8");


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

	public static void normal(double stdDev, double mean, double min, double max, boolean hasMin, boolean hasMax) {
	    Random rng = new Random();
	    double val = 0;

	    // Bad variable name and stupid if-else logic.
	    boolean works = true;
	    while(works) {
		val = rng.nextGaussian()*stdDev + mean;
		
		if(!hasMin && !hasMax) {
		    works = false;
		}
		else if(hasMin && min < val && !hasMax) {
		    works = false;
		}
		else if(!hasMin && hasMax && val < max) {
		    works = false;
		}
		else if(hasMin && hasMax && min < val && val < max) {
		    works = false;
		}
	    }
	    day.write(""+val+" ");

	    return ;
	}

	public static void preprocessing(int days) {
	    // Initiates the day file with names of events
	    for(int i = 0; i<nrOfEvents; i++) {
		day.write(Events.get(i).name+" ");
	    }
	    day.write("\n");
	    
	    // Generates the totals for each day and event then writes to days.txt
	    for(int i = 0; i<days; i++) {
		day.write("Day"+(i+1)+": ");
		for(int i = 0; i<nrOfEvents; i++) {
		    normal(Stats.get(i).std, Stats.get(i).mean, Events.get(i).min, Events.get(i).max, Events.get(i).hasMin, Events.get(i).hasMax);
		}
		day.write("\n");
	    }
	    return;
	}

		
	

}
