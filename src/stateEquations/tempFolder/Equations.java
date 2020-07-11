package stateEquations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Equations {
	static ArrayList<String[]> interactionTable;
	static ArrayList<String[]> subgroupTable;
	
	public static double findTemp(double[] gammas, Species[] speciesList, 
			double[] molPercentList, double pressure, double temperature){
		UnivariateTemp a = new UnivariateTemp(gammas, speciesList, molPercentList, pressure);
		double tempMin = -273.15;
		double tempMax = 500;
		double tolerance = 0.000001;
		double difference = 1.0;
		while(Math.abs(difference) > tolerance){
			difference = a.univariateFunc((tempMin+tempMax)/2);
			if(difference > 0){
				tempMin = tempMin;
				tempMax = (tempMin+tempMax)/2;
			} else if (difference < 0){
				tempMin = (tempMin+tempMax)/2;
				tempMax = tempMax;
			}
		}
		return (tempMin+tempMax)/2;
	}
	
	public static double[] uRound(Species[] speciesList, double[] molPercentList, double temperature){
		
		setup();
		int totalSubGroups = totalSubGroups(speciesList);
		double[][] tauBetaTable = new double[totalSubGroups + speciesList.length + 2 ][totalSubGroups];
		SubGroup[] subGroupList = listSubGroups(speciesList, totalSubGroups);
		double[] gammas = new double[speciesList.length];
		
		calculateJointVar(speciesList, molPercentList);
		calculateEs(subGroupList, speciesList);
		tauBetaTable = findTaus(subGroupList, tauBetaTable, temperature + 273.15);
		tauBetaTable = findBetas(speciesList, subGroupList, tauBetaTable);
		tauBetaTable = findThetas(speciesList, subGroupList, tauBetaTable);
		tauBetaTable = findS(speciesList, subGroupList, tauBetaTable);
		gammas = findGamma(tauBetaTable, speciesList, subGroupList);
		
		return gammas;
		
	}
	public static double[] findGamma(double[][] tauBetaTable, Species[] speciesList, SubGroup[] subGroupList){
		double[] gamma = new double[speciesList.length];
		int scol = speciesList.length + subGroupList.length + 1;
		int theta = speciesList.length + subGroupList.length;
		int beta = subGroupList.length;
		for(int i = 0; i < speciesList.length; i++){
			
			double gammaC = 1 - speciesList[i].j + Math.log(speciesList[i].j) - 5*speciesList[i].q*(1
					- (speciesList[i].j / speciesList[i].l) + Math.log(speciesList[i].j/speciesList[i].l));
			double tempSum = 0;
			for( int j = 0; j < subGroupList.length; j++){
				tempSum += (((tauBetaTable[theta][j] * tauBetaTable[beta + i][j]) / tauBetaTable[scol][j])
						- (subGroupList[j].e[i] * Math.log(tauBetaTable[beta + i][j] / tauBetaTable[scol][j])));
			}
			double gammaR = speciesList[i].q * (1-tempSum);
			gamma[i] = Math.exp(gammaC + gammaR);
		}
		
		return gamma;
	}
	public static double[][] findS(Species[] speciesList, SubGroup[] subGroupList, double[][] tauBetaTable){
		
		for(int i = 0; i < subGroupList.length; i++){
			double sum = 0;
			for(int j = 0; j < subGroupList.length; j++){
				sum += tauBetaTable[subGroupList.length + speciesList.length][j]
						* tauBetaTable[j][i];
			}
			tauBetaTable[subGroupList.length + speciesList.length + 1][i] = sum;
		}
		
		return tauBetaTable;
	}
	public static double[][] findThetas(Species[] speciesList, SubGroup[] subGroupList, double[][] tauBetaTable){
		for(int i = 0; i < subGroupList.length; i++){
			double topSum = 0;
			double botSum = 0;
			for(int j = 0; j < speciesList.length; j++){
				topSum += speciesList[j].molPercent * speciesList[j].q * subGroupList[i].e[j];
				botSum += speciesList[j].molPercent * speciesList[j].q;
			}
			tauBetaTable[subGroupList.length + speciesList.length][i] = topSum/botSum;
		}
		
		return tauBetaTable;
	}
	
	public static SubGroup[] listSubGroups(Species[] speciesList, int totalSubGroups){
		int allSubsCount = 0;
		SubGroup[] allSubs = new SubGroup[totalSubGroups];
		for(int i = 0; i < speciesList.length; i++){
			for(int j = 0; j < speciesList[i].numSubGroups; j++){
				allSubs[allSubsCount] = speciesList[i].subGroups[j];
				allSubsCount++;
			}
		}
		return allSubs;
	}
	public static int totalSubGroups(Species[] speciesList){
		int totalSubGroups = 0;
		for(int i = 0; i < speciesList.length; i++){
			totalSubGroups += speciesList[i].numSubGroups;
		}
		return totalSubGroups;
	}
	public static void calculateEs(SubGroup[] subGroupList, Species[] speciesList){
		for (int i = 0; i < subGroupList.length; i++){
			for (int j = 0; j < speciesList.length; j++){
				if(subGroupList[i].parent == speciesList[j]){
					int V = subGroupList[i].V;
					double Q = subGroupList[i].Q;
					double q = subGroupList[i].parent.q;
					subGroupList[i].e[j] = (V * Q)/q;
				} else {
					subGroupList[i].e[j] = 0.0;
				}
			}
			
		}
		
	}
	public static void calculateJointVar(Species[] speciesList, double[] x){
		double jSum;
		double lSum;
		for(int i = 0; i < speciesList.length; i++){
			jSum = 0;
			lSum = 0;
			for(int j = 0; j < speciesList.length; j++){
				jSum = jSum + (speciesList[j].r * x[j]);
				lSum = lSum + (speciesList[j].q * x[j]);
			}
			speciesList[i].j = speciesList[i].r/jSum;
			speciesList[i].l = speciesList[i].q/lSum;
		}
	}
	public static double[][] findBetas(Species[] sList, SubGroup[] subGroupList, double[][] tauBetaTable){
		int tracker = 0;
		for (int i = 0; i < sList.length; i++){
			for (int j = 0; j < subGroupList.length; j++){
				double tempBeta = 0;
				for (int k = 0; k < sList[i].subGroups.length; k++){
					tempBeta = tempBeta + (sList[i].subGroups[k].e[i] * tauBetaTable[k + tracker][j]);
				}
				tauBetaTable[subGroupList.length + i][j] = tempBeta;
			}
			tracker += sList[i].subGroups.length;
			
		}
		return tauBetaTable;
	}
	public static double[][] findTaus(SubGroup[] subGroupList, double[][] tauBetaTable, double temperature){
		for(int i = 0; i < subGroupList.length; i++){
			for(int j = 0; j < subGroupList.length; j++){
				tauBetaTable[i][j] = tau(subGroupList[i], subGroupList[j], temperature);
			}
		}
		return tauBetaTable;
	}
	public static double tau(SubGroup a, SubGroup b, double temperature){
		double tau = 0.0;
		if(a.mainGroup == b.mainGroup){
			return tau = 1.0;
		}
		for(int i = 0; i < interactionTable.size(); i++){
			if(Integer.parseInt(interactionTable.get(i)[1]) == a.mainGroup && Integer.parseInt(interactionTable.get(i)[2]) == b.mainGroup){
				tau = Math.exp(-Double.parseDouble((interactionTable.get(i)[3]))/temperature);
				break;
			} else if (Integer.parseInt(interactionTable.get(i)[1]) == b.mainGroup && Integer.parseInt(interactionTable.get(i)[2]) == a.mainGroup){
				tau = Math.exp(-Double.parseDouble((interactionTable.get(i)[4]))/temperature);
				break;
			} 
		}
		return tau;
	}
	public static void setup() {
		String s = System.getProperty("user.dir");
		interactionTable = tsvr(new File(s + "\\src\\unifacCalc\\interaction.txt"));
		subgroupTable = tsvr(new File(s + "\\src\\unifacCalc\\subgroups.txt"));
		
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
