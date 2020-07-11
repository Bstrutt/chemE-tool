package unifacCalc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class unifacSpecies {
	
	static {
		readCompoundTable();
	}
	ArrayList<unifacSubGroup> subGroupList;
	double A, B, C, r, q, j, l;
	double molPercent;
	String speciesName;
	int subGroupCount;
	static ArrayList<String[]> compoundTable;

	public unifacSpecies(String speciesName){
		this.speciesName = speciesName;
		fillSpeciesValues(speciesName);
		makeSubGroups();
	}
	
	private void fillSpeciesValues(String compoundName){
		String[] compound = findCompoundInTable(compoundName);
		
		A = Double.parseDouble(compound[1]);
		B = Double.parseDouble(compound[2]);
		C = Double.parseDouble(compound[3]);
		subGroupCount = compound.length - 4 /2; 
		for(int i = 0; i < subGroupCount; i++) {
			subGroupList.add()
		}
	}
	
	public void readCompoundTable() {
		String s = System.getProperty("user.dir");
		compoundTable = Equations.tsvr(new File(s + "\\src\\unifacCalc\\compounds.txt"));
	}
	
	//Improve to binary search N -> log N
	private String[] findCompoundInTable(String compoundName) {
		for(int i = 0; i < compoundTable.size(); i++){
			if(compoundName.equals(compoundTable.get(i)[0])){
				return compoundTable.get(i);
			}
		}	
	}
	
	

}
