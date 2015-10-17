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
		produceMeanStd();

		
		

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
	
	public static void produceMeanStd() throws FileNotFoundException{
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
	
	
		
	

}
