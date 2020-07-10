package stateEquations.unifac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import stateEquations.HelperEquations;

public class UnifacSpecies {
	
	static {
		readCompoundTable();
	}
	ArrayList<UnifacSubGroup> subGroupList;
	double A, B, C, r, q, j, l;
	double molPercent;
	String speciesName;
	int subGroupCount;
	static ArrayList<String[]> compoundTable;

	public UnifacSpecies(String speciesName){
		this.speciesName = speciesName;
		fillSpeciesValues(speciesName); // Need to check for null value here
		makeSubGroups();
	}
	
	private void fillSpeciesValues(String compoundName){
		String[] compound = findCompoundInTable(compoundName);
		
		A = Double.parseDouble(compound[1]);
		B = Double.parseDouble(compound[2]);
		C = Double.parseDouble(compound[3]);
		subGroupCount = compound.length - 4 /2; 
		for(int i = 0; i < subGroupCount; i++) {
			//subGroupList;
		}
	}
	public void makeSubGroups() {
		
	}
	public static void readCompoundTable() {
		String s = System.getProperty("user.dir");
		compoundTable = HelperEquations.tsvr(new File(s + "\\src\\unifacCalc\\compounds.txt"));
	}
	
	//Improve to binary search N -> log N
	private String[] findCompoundInTable(String compoundName) {
		for(int i = 0; i < compoundTable.size(); i++){
			if(compoundName.equals(compoundTable.get(i)[0])){
				return compoundTable.get(i);
			}
		}
		//throw CompoundNotFound
		return null;
	}
	
	

}
