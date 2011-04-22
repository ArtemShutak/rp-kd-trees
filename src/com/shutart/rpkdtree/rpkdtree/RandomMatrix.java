package com.shutart.rpkdtree.rpkdtree;

import java.util.Random;

import com.shutart.rpkdtree.kdtree.Vector;

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

	public Vector multiply(Vector vector) {
		// TODO Auto-generated method stub
		return null;
	}

}
