package com.shutart.rpkdtree.rpkdtrees;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;
import java.util.Scanner;

import com.shutart.rpkdtree.kdtree.INode;
import com.shutart.rpkdtree.kdtree.Node;
import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.VectorI;

public class RandomMatrix {
	private final int[][] myMatrix;
	
	public RandomMatrix(int d, int d1){
		if(d<=d1){
			throw new IllegalArgumentException();
		}
		myMatrix = new int[d1][d];
		initMatrix(d, d1);
		while(myMatrixIsZero(d,d1)){
			
		}
		//System.out.println(this);
	}

	public RandomMatrix(int[][] matr) {
		myMatrix = matr;
	}

	private boolean myMatrixIsZero(int d, int d1) {
		for (int i = 0; i < d1; i++) {
			for (int j = 0; j < d; j++) {
				if(myMatrix[i][j]!= 0){
					return false;
				}
			}
		}
		return true;
	}

	private void initMatrix(int d, int d1) {
		Random random = new Random();
		for (int i = 0; i < d1; i++) {
			for (int j = 0; j < d; j++) {
				myMatrix[i][j]=  myRandom(random)  ;
			}
		}
	}

	private int myRandom(Random random) {
		double nextRand = random.nextDouble();
		if(nextRand < 1.0/6)
			return 1;
		if(nextRand < 2.0/6)
			return -1;
		return 0;
	}
	
	private int getDimension(){
		return myMatrix[0].length;
	}
	private int getProjDimen(){
		return myMatrix.length;
	}

	public Vector multiply(Vector vector) {
		double[] multiplyVec = new double[getProjDimen()];
		for (int i = 0; i < getProjDimen(); i++) {
			for (int j = 0; j < getDimension(); j++) {
				multiplyVec[i]+=myMatrix[i][j]*vector.getKey(j);
			}
		}
		//INode resNode = new Node(new VectorI(multiplyVec) , vector);
		return new VectorI(multiplyVec);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getProjDimen(); i++) {
			for (int j = 0; j < getDimension(); j++) {
				sb.append(myMatrix[i][j] + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static RandomMatrix[] getMatrixes(int dimension,
			int projectidDimension, int numberOfTrees) {
		String matrixesFileName = "matrixes" + "_d="+dimension+
											   "_d1=" + projectidDimension+
											   "_m=" + numberOfTrees+
											   ".txt";
		File thisMatrFile = new File("matrixes\\" + matrixesFileName );
		if(!thisMatrFile.exists()){
			return createMatrixesFile(thisMatrFile, dimension, projectidDimension,numberOfTrees);
		}
		return readMatrixesFromFile(thisMatrFile, dimension,projectidDimension, numberOfTrees);
	}

	private static RandomMatrix[] readMatrixesFromFile(File matrFile,
			int dimension, int projectidDimension, int numberOfTrees) {
		try {
			Scanner sc = new Scanner(matrFile);
			RandomMatrix[] resRandMatrs = new RandomMatrix[numberOfTrees];
			while(sc.hasNextInt()){
				for (int numOfMatr = 0; numOfMatr < resRandMatrs.length; numOfMatr++) {
					int[][] newMatr = new int[projectidDimension][dimension];
					for (int i = 0; i < projectidDimension; i++) {
						for (int j = 0; j < dimension; j++) {
							newMatr[i][j] = sc.nextInt();
						}
					}
					resRandMatrs[numOfMatr] = new RandomMatrix(newMatr);					
				}
			}
			return resRandMatrs;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static RandomMatrix[] createMatrixesFile(File fileName, int dimension,
			int projectidDimension, int numberOfTrees) {
		try {
			RandomMatrix[] resRandMatrs = new RandomMatrix[numberOfTrees];
			Writer output = new BufferedWriter(new FileWriter(fileName));
			for (int i = 0; i < resRandMatrs.length; i++) {
				RandomMatrix newMatr = new RandomMatrix(dimension, projectidDimension);
				output.write(newMatr.toString()+"\n\n");
				resRandMatrs[i] = newMatr;					
			}
			output.close();
			return resRandMatrs;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
