import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
		
		
		


		System.out.println(Stats);
		System.out.println(Events);
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
				hasMax = false;
				max = 0;
			}
			else{max = Double.parseDouble(nyy[2]);}
			if(nyy[3].isEmpty()){
				hasMin = false;
				min = 0;
			}
			else{min = Double.parseDouble(nyy[3]);}
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
	
	public static String produceMeanStd() throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader("Days.txt"));
		String ini = in.nextLine();
		String[] init = ini.split(" ");
		in.close();
		for(int i=1; i<init.length; i++){
			Scanner inn = new Scanner(new FileReader("Days.txt"));
			inn.nextLine();
			ArrayList<Double> read = new ArrayList<Double>();
			while(inn.hasNext()){
				String[] day = inn.nextLine().split(" ");
				read.add(Double.parseDouble(day[i]));
			}
			System.out.println(init[i]);
			System.out.println(calcAvg(read));
			System.out.println(calcStd(read));
			inn.close();
			
		}
		return "";
		
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

	public static String preprocessing(int days) throws FileNotFoundException, UnsupportedEncodingException {
	    getStats();
		getEvents();
		initDays(days);
	    String fileName = produceMeanStd();
	    return fileName;
	}
	
	public static void initDays(int days) throws FileNotFoundException, UnsupportedEncodingException{
		day = new PrintWriter("days.txt", "UTF-8");
	    // Initiates the day file with names of events
	    for(int i = 0; i<nrOfEvents; i++) {
		day.write(Events.get(i).name+" ");
	    }
	    day.write("\n");
	    
	    // Generates the totals for each day and event then writes to days.txt
	    for(int i = 0; i<days; i++) {
		day.write("Day"+(i+1)+": ");
		for(int j = 0; j<nrOfEvents; j++) {
		    normal(Stats.get(j).std, Stats.get(j).mean, Events.get(j).min, Events.get(j).max, Events.get(j).hasMin, Events.get(j).hasMax);
		}
		day.write("\n");
	    }
	    
	}


}