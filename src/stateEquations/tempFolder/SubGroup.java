package unifacCalc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class SubGroup {
	
	static ArrayList<String[]> subgroupTable;
	
	public String name;
	public int V;
	public int mainGroup;
	public double R;
	public double Q;
	public double[] e;
	public Species parent;
	
	public SubGroup(String subGroupName, String groupCount, Species parent, int speciesCount){
		setup();
		this.e = new double[speciesCount];
		this.parent = parent;
		this.V = Integer.parseInt(groupCount);
		for(int i = 0; i < subgroupTable.size(); i++){
			if (subGroupName.equals(subgroupTable.get(i)[0])){
				name = subgroupTable.get(i)[0];
				R = Double.parseDouble(subgroupTable.get(i)[2]);
				Q = Double.parseDouble(subgroupTable.get(i)[3]);
				mainGroup = Integer.parseInt(subgroupTable.get(i)[1]);
				break;
			}
		}
	}
	
	public void setup(){
		String s = System.getProperty("user.dir");
		subgroupTable = tsvr(new File(s + "\\src\\unifacCalc\\subgroups.txt"));
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
