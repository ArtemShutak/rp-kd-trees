package com.shutart.rpkdtree.rpkdtrees;

import java.util.Random;

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

	public INode multiply(Vector vector) {
		double[] multiplyVec = new double[getProjDimen()];
		for (int i = 0; i < getProjDimen(); i++) {
			for (int j = 0; j < getDimension(); j++) {
				multiplyVec[i]+=myMatrix[i][j]*vector.getKey(j);
			}
		}
		INode resNode = new Node(new VectorI(multiplyVec) , vector);
		return resNode;
	}

}
