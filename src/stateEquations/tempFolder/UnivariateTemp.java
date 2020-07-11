package unifacCalc;

public class UnivariateTemp {
	private double[] gammas;
	private Species[] speciesList;
	private double[] molPercentList;
	private double a1, b1, c1, a2, b2, c2, gamma1, gamma2, x1, pressure;
	public UnivariateTemp(double[] gammas, Species[] speciesList, double[] molPercentList, double pressure){
		 a1 = speciesList[0].A;
		 b1 = speciesList[0].B;
		 c1 = speciesList[0].C;
		 a2 = speciesList[1].A;
		 b2 = speciesList[1].B;
		 c2 = speciesList[1].C;
		 gamma1 = gammas[0];
		 gamma2 = gammas[1];
		 x1 = molPercentList[0];
		 this.pressure = pressure;		   
	}
	public double univariateFunc(double temp){
		return ((x1*gamma1*Math.pow(10, (a1-(b1/(c1+temp))))) + ((1-x1)*gamma2*Math.pow(10, a2-(b2/(c2+temp))))) - pressure;
	}
}
