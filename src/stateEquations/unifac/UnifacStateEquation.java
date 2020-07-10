package stateEquations.unifac;


import java.util.ArrayList;

import stateEquations.StateEquation;

public class UnifacStateEquation extends StateEquation {
	
	ArrayList<UnifacSpecies> speciesList;
	
	public UnifacStateEquation(String[] speciesNames){
		makeSpeciesList(speciesNames);
		
	}
	
	void makeSpeciesList(String[] speciesNames){
		for(String s : speciesNames){
			speciesList.add(new UnifacSpecies(s));
		}
	}
}
