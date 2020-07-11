package unifacCalc;

import java.io.BufferedReader;
import java.math.*;

import javax.swing.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;

public class TxyGraph extends Application {
	static double temperature;
	static double[] xVals;
	static double[] tempVals;
	static double[] yVals;
	public static void main(String args[]) {
		
		
		//Species sp1 = new Species(args[0], 2, .75);
		//Species sp2 = new Species(args[1], 2, .25);

		Species sp1 = new Species("ethanol", 2, .75);
		Species sp2 = new Species("methyl acetate", 2, .25);
		temperature = 25; // Celsius
		double pressure = 760.0;
		
		xVals = new double[100];
		tempVals = new double[100];
		yVals = new double[100];
				
		double TOLERANCE = .001;
		double difference;
		double gammas[] = new double[2];
		
		for(int i = 0; i < 99; i++){
			double previousTemp, currentTemp = temperature;
			difference = 1.0;
			sp1.molPercent = .01 +.01*i;
			sp2.molPercent = 1-sp1.molPercent;
			Species[] sList = {sp1,sp2};
			double[] xList = {sp1.molPercent,sp2.molPercent};
			//Finding appropriate Temperature
			while(difference > TOLERANCE){
				previousTemp = currentTemp;
				
				gammas = Equations.uRound(sList, xList, currentTemp);
				currentTemp =  Equations.findTemp(gammas, sList, xList, pressure, currentTemp);
				
				difference = Math.abs(1 - Math.abs(currentTemp / previousTemp));
				
			}
		yVals[i] = ((sp1.molPercent * gammas[0])/pressure) * Math.pow(10, sp1.A-(sp1.B/(sp1.C + currentTemp)));
		xVals[i] = sp1.molPercent;
		tempVals[i] = currentTemp;
		}
		
		//ScatterPlot.start();
		ScatterPlot.launch();
	}
	
	
	@Override public void start(Stage stage) {
		double tempMax = -10000;
        double tempMin = 10000;
		for(int i = 0; i < 99; i++){
			if(tempVals[i] > tempMax){
        		tempMax = tempVals[i];
        	}
        	if(tempVals[i] < tempMin){
        		tempMin = tempVals[i];
        	}
		}
		
        stage.setTitle("Txy Diagram");
        final NumberAxis xAxis = new NumberAxis(0, 1, .1);
        
        final NumberAxis yAxis = new NumberAxis(tempMin, tempMax, (tempMax-tempMin)/5);        
        final ScatterChart<Number,Number> sc = new
            ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("x1,y1");                
        yAxis.setLabel("temperature");
        sc.setTitle("Txy Diagram");
       
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("XVals");
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("YVals");
        
        for(int i = 0; i < 99; i++){
        	series1.getData().add(new XYChart.Data(xVals[i], tempVals[i]));
        	series2.getData().add(new XYChart.Data(yVals[i], tempVals[i]));
        	System.out.println(xVals[i] + " " + yVals[i]);
        }
        
        sc.getData().addAll(series1, series2);
        Scene scene  = new Scene(sc, 500, 400);
        stage.setScene(scene);
        stage.show();
    }
}