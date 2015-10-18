import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;


public class Main {
	static ArrayList<Stat> Stats = new ArrayList<Stat>();
	static ArrayList<Event> Events = new ArrayList<Event>();
	static int nrOfStats;
	static int nrOfEvents;
	// Creates days.txt and appends the names of events to top line
	static PrintWriter day;
	



	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException{
		String eventsDoc = "";//args[0];
		String username = "";//args[1];
		String statsDoc = "";//args[2];
 		int days = 5;//Integer.parseInt(args[3]);
		preprocessing(days);
		System.out.println("processing finished, beginning analysis...");
		initDays(days);
	    ArrayList<Stat> base = produceMeanStd();
	    System.out.println(base);

	}
	
	public static void getStats() throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader("Stats.txt"));
		nrOfStats = Integer.parseInt(in.nextLine());
		while(in.hasNext()){
			String ny = in.nextLine();
			String[] nyy = ny.split(":");
			String name;
			double mean = 0;
			double std = 0;
			if(!nyy[1].isEmpty()){
				mean = Double.parseDouble(nyy[1]);
			}
			else{System.out.println("missing mean, mean set to 0");}
			if(!nyy[2].isEmpty()){
				mean = Double.parseDouble(nyy[2]);
			}
			else{System.out.println("missing std, std set to 0");}
			
			Stats.add(new Stat(nyy[0], mean, std));
		}
	}
	
	public static void getEvents() throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader("Events.txt"));
		String name;
		String type;
		boolean hasMax = true;
		double max;
		boolean hasMin = true;
		double min;
		boolean hasUnit = true;
		String unit;
		boolean hasWeight = true;
		int weigth;
		
		nrOfEvents = Integer.parseInt(in.nextLine());
		while(in.hasNext()){
			String ny = in.nextLine();
			String[] nyy = ny.split(":");
			name = nyy[0];
			type = nyy[1];
			if(nyy[2].isEmpty()){
				hasMin = false;
				min = 0;
			}
			else{min = Double.parseDouble(nyy[2]);}
			if(nyy[3].isEmpty()){
				hasMax = false;
				max = 0;
			}
			else{max = Double.parseDouble(nyy[3]);}
			if(nyy[4].isEmpty()){
				hasUnit = false;
				unit = "";
			}
			else{unit = nyy[1];}
			
			if(nyy[5].isEmpty()){
				hasWeight = false;
				weigth = 0;
			}
			else{weigth = Integer.parseInt(nyy[5]);}
			
			
			
			Events.add(new Event(name, type, hasMax, max, hasMin, min, hasUnit, unit, hasWeight, weigth));
		}
	}
	
	public static ArrayList<Stat> produceMeanStd() throws FileNotFoundException{
		ArrayList<Stat> base = new ArrayList<Stat>();
		Scanner in = new Scanner(new FileReader("Days.txt"));
		String ini = in.nextLine();
		String[] init = ini.split(": "); 
		
		in.close();

		for(int i=1; i<init.length; i++){
			Scanner inn = new Scanner(new FileReader("Days.txt"));
			inn.nextLine();
			ArrayList<Double> read = new ArrayList<Double>();
			while(inn.hasNext()){
				String[] day = inn.nextLine().split(": ");
				read.add(Double.parseDouble(day[i]));
			}
			String name = init[i];
			double mean = calcAvg(read);
			double std = calcStd(read);
			
			base.add(new Stat(name, mean, std));
			
			inn.close();
			
		}
		return base;
		
	}
	
	public static double calcAvg(ArrayList<Double> data){
	double n = data.size();
	double tot = 0;
	for(int i=0; i<data.size(); i++){
		tot += data.get(i); 
	}
	
	double avg = tot/n;
	
	return avg;
	
	}
	
	public static double calcStd(ArrayList<Double> data){
	double avg = calcAvg(data);
	double n = data.size();
	double sum = 0;
	for(int i = 0; i<data.size(); i++){
		double x = data.get(i);
		sum += Math.pow((x-avg), 2);
	}
	
	double std = Math.sqrt(sum/n);
	
	return std;
}
	
	public static void normal(String name,double stdDev, double mean, double min, double max, boolean hasMin, boolean hasMax) {
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
		else{
		//System.out.println("probable statistical anomaly(?)");
		}
	    }
	    
	    day.write(""+val+" : ");

	    return ;
	}

	public static boolean alertEngine(int[] freq, ArrayList<Stat> base) {
	    int threshold = 0;
	    double alert = 0;
	    for(int i = 0; i<nrOfEvents; i++) {
		threshold += Events.get(i).weight;
		alert += Events.get(i).weight*(freq[i]-base.get(i).mean)/base.get(i).std;
	    }
	    if(alert > threshold) {
		return true;
	    }
	    return false;
	}

	public static void preprocessing(int days) throws FileNotFoundException, UnsupportedEncodingException {
	    getStats();
		getEvents();
		checkLists(Stats, Events);
		
	}
		public static void genDay(int dayNum, double[] totals) throws FileNotFoundException, UnsupportedEncodingException {
	    // Initialize all the variables
	    String fileName = "Day"+dayNum+".txt";
	    int[] time = {0,0,0};
	    Random rng = new Random();
	    int eventNum = 0;
	    String type;
	    double size;
	    String name;
	    String unit;
	    LogItem printMe;
	    // Creates the list with the items to print
	    ArrayList<LogItem> log = new ArrayList();

	    // Writes to the file
	    while(eventNum != -1) {
		eventNum = pickEvent(totals); // Chooses which event is going to happen
		time[2] += rng.nextInt(3600);
		time = incTime(time[0],time[1],time[2]);
		// Gets all the data needed about the event
		name = Events.get(eventNum).name;
		unit = Events.get(eventNum).unit;
		type = Events.get(eventNum).type;
		size = eventMagnitude(type, totals[eventNum]);
		// Puts the data into the logItem
		printMe = new LogItem(time[0], time[1], time[2], name, size, unit);
		// Appends to log
		log.add(printMe);
		// Updates totals to avoid infinite loop
		totals[eventNum] -= size;
	    }
	    
	    // Creates the file to write to
	    PrintWriter cDay = new PrintWriter(fileName, "UTF-8");

	    // Writes the log to the file
	    writeToLog(cDay, log);

	    // Saves the file
	    cDay.close();
	}

	public static int pickEvent(double[] totals) {
	    Random rng = new Random();
	    ArrayList<Double> newTotals = new ArrayList<Double>();
	    for(int i = 0; i<totals.length; i++) {
		if(totals[i] > 0) {
		    newTotals.add(totals[i]);
		}
	    }
	    if(!newTotals.isEmpty()) {
		return rng.nextInt(newTotals.size());
	    }
	    return -1;
	}

	public static double eventMagnitude(String type, double total) {
	    Random rng = new Random();
	    if(type.equals("D")) return 1;
	    // This is very ugly
	    double val = rng.nextDouble()*total*1.7;
	    if(val > total) return total;
	    return val;
	}

	public static void writeToLog(PrintWriter file, ArrayList<LogItem> Log) {
	    // Sort the log by time
	    Collections.sort(Log);
	    // Write to file
	    for(int i = 0; i<Log.size(); i++) {
		file.write("<"+Log.get(i).hours+":"+Log.get(i).mins+":"+Log.get(i).secs+"> "+Log.get(i).name+": "+Log.get(i).size+" "+Log.get(i).unit+"\n";
	    }
	}

	public static int[] incTime(int hrs, int min, int sec) {
	    min += sec/60;
	    sec = sec%60;
	    hrs += min/60;
	    min = min%60;
	    hrs = hrs%60;
	    int[] time = new int[] {hrs, min, sec};
	    return time;
	}
	
	public static void initDays(int days) throws FileNotFoundException, UnsupportedEncodingException{
		day = new PrintWriter("Days.txt", "UTF-8");
	    // Initiates the day file with names of events
	    for(int i = 0; i<nrOfEvents; i++) {
		day.write(Events.get(i).name+" : ");
	    }
	    day.write("\n");
	    
	    // Generates the totals for each day and event then writes to days.txt
	    for(int i = 0; i<days; i++) {
		day.write("Day"+(i+1)+": ");
		for(int j = 0; j<nrOfEvents; j++) {
		    normal(Stats.get(j).name,Stats.get(j).std, Stats.get(j).mean, Events.get(j).min, Events.get(j).max, Events.get(j).hasMin, Events.get(j).hasMax);
		}
		day.write("\n");
		
	    }
	    day.close();
	}
	
	public static boolean checkLists(ArrayList<Stat> stats, ArrayList<Event> events){
	if(!(stats.size() == events.size())){
		System.out.println("input inconsistency: stats list is not same size as events list");
		return false;
	}
	for(int i = 0; i<stats.size();i++){
		if(stats.get(i).name.trim() == events.get(i).name.trim()){
		System.out.println("input inconsistency: stats names are not the same as event names");
		return false;
		}
	}
	
	return true;
	}
	


}
