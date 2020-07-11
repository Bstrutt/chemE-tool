package unifacCalc;

import java.util.ArrayList;

public class unifacStateEquation extends stateEquation {
	
	ArrayList<unifacSpecies> speciesList;
	
	public unifacStateEquation(String[] speciesNames){
		makeSpeciesList(speciesNames);
		
	}
	
	void makeSpeciesList(String[] speciesNames){
		for(String s : speciesNames){
			speciesList.add(new unifacSpecies(s));
		}
	}
}
