package unifacCalc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Species {
	
	public SubGroup[] subGroups;
	double r, q, j, l;
	double A, B, C;
	int numSubGroups;
	double molPercent;
	static ArrayList<String[]> interactionTable;
	static ArrayList<String[]> subgroupTable;
	static ArrayList<String[]> compoundsTable;
	String name;
	
	Species(String compoundName, int speciesCount, double molPercent) {
		setup();
		// Reads compound in and makes appropriate subgroups
		readCompound(compoundName, this, speciesCount);
		for (int i = 0; i < numSubGroups; i++) {
			r = r + (subGroups[i].V * subGroups[i].R);
			q = q + (subGroups[i].V * subGroups[i].Q);
		}
		this.molPercent = molPercent;
	}
	private static void readCompound(String compoundName, Species s, int speciesCount){
		String[] compound = null;
		for(int i = 0; i < compoundsTable.size(); i++){
			if(compoundName.equals(compoundsTable.get(i)[0])){
				compound = compoundsTable.get(i);
				break;
			}
		}
		
		s.name = compound[0];
		s.A = Double.parseDouble(compound[1]);
		s.B = Double.parseDouble(compound[2]);
		s.C = Double.parseDouble(compound[3]);
		s.numSubGroups = (compound.length-4)/2; 
		s.subGroups = new SubGroup[s.numSubGroups];
		for(int i = 0; i < s.numSubGroups; i++){
			s.subGroups[i] = new SubGroup(compound[4+i*2], compound[5+i*2], s, speciesCount);
		}
	}
	public static void setup() {
		String s = System.getProperty("user.dir");
		interactionTable = tsvr(new File(s + "\\src\\unifacCalc\\interaction.txt"));
		compoundsTable = tsvr(new File(s + "\\src\\unifacCalc\\compounds.txt"));
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
