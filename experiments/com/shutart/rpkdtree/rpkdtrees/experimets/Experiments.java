package com.shutart.rpkdtree.rpkdtrees.experimets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.VectorI;
import com.shutart.rpkdtree.rpkdtrees.RPKDTrees;
import com.shutart.rpkdtree.rpkdtrees.RandomMatrix;
import com.shutart.rpkdtree.tests.linear.NNLinearSearcher;

public class Experiments {
	private static final String DATA_PATH = "datas\\";
	private static final String EXPERIMENTS_PATH = DATA_PATH + "experiments\\";
	public static final int dimension = 41;
	/**
	 * parameter number of trees from 1 to <tt>maxNumberOfTrees</tt>
	 */
	private static final int maxNumberOfTrees =  20;
	private static final int numberOfNeighbors = 10;//------------------------------------------------------------------
	private static final int divisorForQVector = 500;//-------------
	private static final int[] projectedDimensions = new int[] {1,3,5};
	private static String attributesForResFile;
	static{
		attributesForResFile = "d="+dimension+"_d1=";
		for (int i = 0; i < projectedDimensions.length; i++) {
			attributesForResFile+=projectedDimensions[i];
		}
		attributesForResFile+="_m=1-"+maxNumberOfTrees + "_nNeig="+numberOfNeighbors + "_qVec"+divisorForQVector;
		System.out.println("attributesForResFile= " + attributesForResFile);
	}
	private static final String CUR_EXPERIMENTS_PATH = EXPERIMENTS_PATH + attributesForResFile + "\\";
	private static final String COMPLEXITY_OF_SEARCH_FILE_NAME = 
		CUR_EXPERIMENTS_PATH + "complexityOfSearch_" + attributesForResFile + ".csv";
	private static final String TIME_SPEED_OF_SEARCH_FILE_NAME = 
		CUR_EXPERIMENTS_PATH + "timeSpeedOfSearch_"+attributesForResFile + ".csv";
	private static final String PRECISION_FILE_NAME =
		CUR_EXPERIMENTS_PATH + "precision_"+attributesForResFile+".csv";

	private static Vector queryVector = getQueryVector();
	
	//results
	//speed of search
	private static Integer[] complexity = new Integer [maxNumberOfTrees];
	private static Long[] timeComplexityInSec = new Long [maxNumberOfTrees];
	
	private static int complexityForLinearSearch;
	private static long timeComplexityInSecForLinearSearch;
	//average precision
	private static Double[] precision = new Double [maxNumberOfTrees];
	private static double precisionForLinearSearch;
	

	/**
	 * @param args
	 */
	public static void main(String[] args){
		clearResultsFiles();
		Set<Vector> corpus = getCorpus();
		//print(corpus);
		
		Timer timer = new Timer();
		timer.run();
		List<Vector> exactRes = new NNLinearSearcher(corpus).nnsearch(numberOfNeighbors, queryVector);
		timer.stop();
		complexityForLinearSearch = corpus.size();
		timeComplexityInSecForLinearSearch = timer.getTime();
		precisionForLinearSearch = Precision.averagePrecision(exactRes, exactRes);
		writeResultsOfLinearSearchInFile();
		for (int iProjDim = 0; iProjDim < projectedDimensions.length; iProjDim++) {
			for (int numOfTrees = 1; numOfTrees <= maxNumberOfTrees; numOfTrees++) {
				RPKDTrees tree = new RPKDTrees(dimension, projectedDimensions[iProjDim], numOfTrees);
				tree.indexing(corpus);
				timer.run();
				List<Vector> approxRes = tree.aproxNNsearch(numberOfNeighbors, queryVector);
				timer.stop();
				complexity[numOfTrees-1] = tree.getComplexityOfLastSearch();
				timeComplexityInSec[numOfTrees-1] = timer.getTime();	
				
				precision[numOfTrees-1] = Precision.averagePrecision(exactRes, approxRes);
				
				System.out.println("Precision = " + Precision.precision(exactRes, approxRes));
				System.out.println("ArgPrecision=" + precision[numOfTrees-1]);
			}			
			writeAllResultsInFiles();
		}
	}

	private static void writeResultsOfLinearSearchInFile() {
		try {
			Writer outputSpeed = new BufferedWriter(new FileWriter(COMPLEXITY_OF_SEARCH_FILE_NAME));
			Writer outputTime = new BufferedWriter(new FileWriter(TIME_SPEED_OF_SEARCH_FILE_NAME));
			Writer outputPrecision = new BufferedWriter(new FileWriter(PRECISION_FILE_NAME));
			for (int i = 0; i < maxNumberOfTrees; i++) {
				outputSpeed.write(complexityForLinearSearch + ", ");
				outputTime.write(timeComplexityInSecForLinearSearch + ", ");
				outputPrecision.write(precisionForLinearSearch + ", ");
			}
			outputSpeed.write("\n");
			outputTime.write("\n ");
			outputPrecision.write("\n");
			outputSpeed.close();
			outputTime.close();
			outputPrecision.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void writeAllResultsInFiles() {
		writeResultsInFile(COMPLEXITY_OF_SEARCH_FILE_NAME,complexity);
		writeResultsInFile(TIME_SPEED_OF_SEARCH_FILE_NAME,timeComplexityInSec);
		writeResultsInFile(PRECISION_FILE_NAME,precision);	
	}

	private static void writeResultsInFile(String fileName, Object[] results) {
		try {
			Writer output = new BufferedWriter(new FileWriter(fileName,true));
			for (int i = 0; i < maxNumberOfTrees; i++) {
				output.write(results[i].toString()+", ");
			}
			output.write("\n");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void clearResultsFiles() {
		// TODO Auto-generated method stub
		
	}

	private static void print(Collection<Vector> list) {
		int number = 1;
		for (Vector vector : list) {
			System.out.println(number + ") " + vector);
			number++;
		}
		System.out.println("_______________________________");
	}

	private static Vector getQueryVector() {
		String queryVectorFileName = "queryVector" + "_d=" + dimension + ".txt";
		File queryVectorFile = new File(EXPERIMENTS_PATH + queryVectorFileName);
		if (!queryVectorFile.exists()) {
			return createQueryVectorFile(queryVectorFile, dimension);
		}
		return readQueryVectorFile(queryVectorFile, dimension);
	}

	private static Vector readQueryVectorFile(File queryVectorFile,int dimension) {
		try {
			Scanner sc = new Scanner(queryVectorFile);
			double[] keys = new double[dimension];
			int counter = 0;
			while (sc.hasNextLine()) {
				keys[counter] = new Double(sc.nextLine());
				counter++;
			}
			return new VectorI(keys);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Vector createQueryVectorFile(File fileName,
			int dimension) {
		try {
			Writer output = new BufferedWriter(new FileWriter(fileName));
			double[] keys = new double[dimension];
			Random random = new Random();
			for (int i = 0; i < keys.length; i++) {
				keys[i] = random.nextDouble()/divisorForQVector;
				//if(keys[i]>0.4){
					//keys[i]/=10;
				//}
				output.write(keys[i]+"\n");
			}
			output.close();
			return new VectorI(keys);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Set<Vector> getCorpus() {
		Set<Vector> corpus = new HashSet<Vector>();
		try {
			File corpusFile = new File(EXPERIMENTS_PATH + "ColorHist.csv");
			Scanner sc = new Scanner(corpusFile);
			int curNumberOfVec = 1;
			
			int nextNumbOfVec = -1;
			int numberOfAttribute = -1;
			double value = -1;
			for(StringTokenizer sTokenizer = new StringTokenizer(sc.nextLine(), ", :;");sTokenizer.hasMoreTokens();){
				nextNumbOfVec = new Integer(sTokenizer.nextToken());
				numberOfAttribute = new Integer(sTokenizer.nextToken());
				value = new Double(sTokenizer.nextToken());
			}
			while(sc.hasNextLine()){
				double[] keys = new double[dimension];
				while(true){
					keys[numberOfAttribute-1] = value;
					if(sc.hasNextLine()){
						for(StringTokenizer sTokenizer = new StringTokenizer(sc.nextLine(), ", :;");sTokenizer.hasMoreTokens();){
							nextNumbOfVec = new Integer(sTokenizer.nextToken());
							numberOfAttribute = new Integer(sTokenizer.nextToken());
							value = new Double(sTokenizer.nextToken());
						}
						if(nextNumbOfVec!=curNumberOfVec){
							break;
						}
					}else{
						break;
					}
				}
				Vector newVector = new VectorI(keys);
				if(!vecIsZero(newVector)){
					corpus.add(newVector);	
				}else{
					System.out.println("zeroVector__________________");
				}
				//System.out.println(newVector);
				curNumberOfVec++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return corpus;
	}

	private static boolean vecIsZero(Vector vector) {
		for (int i = 0; i < vector.size(); i++) {
			if(vector.getKey(i)!=0){
				return false;
			}
		}
		return true;
	}

}
