package unifacCalc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class TxyDriver {
	public ArrayList<String[]> interactionTable;
	public ArrayList<String[]> subgroupTable;
	public ArrayList<String[]> compoundsTable;
	
	
	public TxyDriver(String[] speciesStrings){
		retrieveTables();
		for(int i = 0; i < speciesStrings.length; i++){
			
		}
	}
	
	public void retrieveTables() {
		String s = System.getProperty("user.dir");
		interactionTable = tsvr(new File(s + "\\src\\unifacCalc\\interaction.txt"));
		compoundsTable = tsvr(new File(s + "\\src\\unifacCalc\\compounds.txt"));
		subgroupTable = tsvr(new File(s+ "\\src\\unifacCalc\\subgroups.txt"));
		// Add reading in species and subgroups table
	}
	public static ArrayList<String[]> tsvr(File test2) {
		ArrayList<String[]> Data = new ArrayList<>();
		try (BufferedReader TSVReader = new BufferedReader(
				new FileReader(test2))) {
			String line = null;
			while ((line = TSVReader.readLine()) != null) {
				String[] lineItems = line.split("\t");
				Data.add(lineItems);
			}
		} catch (Exception e) {
			System.out.println("Something went wrong");
			System.out.println(e);
		}
		return Data;
	}
}
