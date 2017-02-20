package com.norcom;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;

import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import net.seninp.jmotif.sax.SAXException;
import net.seninp.jmotif.sax.SAXProcessor;
import net.seninp.jmotif.sax.alphabet.NormalAlphabet;
import net.seninp.jmotif.sax.datastructure.SAXRecords;

public class Model {

	/**
	 * 
	 * @param size
	 * @return {@link ArrayList}
	 */
	public static ArrayList<Double> generateRandomNumbers(Integer size) {
		Double min = -1.0;
		Double max = 1.0;
		ArrayList<Double> randomList = new ArrayList<>();
		if (size >= 1) {
			for (int i = 0; i < size; i++) {
				Random rand = new Random();
				Double finalX = rand.nextDouble() * (max - min) + min;
				randomList.add(finalX);
				System.out.println(finalX);
			}
		} else {
			System.out.println("the size of the array should be bigger than one!");
		}
		return randomList;
	}

	/**
	 * Alphabet size should be max 20 - 2,4,8,16
	 * @param timeSeriesList
	 * @param windowSize
	 * @param alaphabetSAX list of String of the SAX Alphabet
	 * @return
	 * @throws SAXException 
	 */
	public static SAXRecords generateTimeSeriesSAX(ArrayList<Double> timeSeriesList, Integer windowSize,
			Integer alaphabetSAX, boolean showPlot) throws SAXException {
		//Set<Integer> timeSeriesSAX = new HashSet<>();
		//Integer saxStride = 2;
		Integer saxNBin = alaphabetSAX;
		
		// instantiate classes
		NormalAlphabet na = new NormalAlphabet();
		SAXProcessor sp = new SAXProcessor();

		// read the input file
		// timeSeriesList ;
		Double[] tsObj = new Double[timeSeriesList.size()];
		tsObj = timeSeriesList.toArray(tsObj);
		double[] ts = ArrayUtils.toPrimitive(tsObj);

		// perform the discretization
		// Threshold is 0.01 for normalization
		SAXRecords res =sp.ts2saxByChunking(ts, windowSize, na.getCuts(saxNBin), 0.001);
		
//		SAXRecords res = sp.ts2saxViaWindow(ts, saxStride, saxNBin, 
//		    na.getCuts(saxNBin), NumerosityReductionStrategy.EXACT, 0.01);
//		
		// print the output
//		for(Integer idx : res.getAllIndices()){
//			System.out.println(idx + ", " + String.valueOf(res.getByIndex(idx).getPayload()));
//		}		
		return res;

	}

}
