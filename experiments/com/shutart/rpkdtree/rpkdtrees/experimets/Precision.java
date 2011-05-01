package com.shutart.rpkdtree.rpkdtrees.experimets;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.shutart.rpkdtree.kdtree.Vector;

public class Precision {
	
	public static double averagePrecision(List<Vector> expertList, List<Vector> examList){
		double avgPrec = 0;
		for (int i = 0; i < examList.size(); i++) {
			avgPrec+=precisionRelevant(i, expertList, examList);
		}
		return avgPrec/examList.size();
	}

	private static double precisionRelevant(int i,List<Vector> expertList, List<Vector> examList) {
		return precision(position(expertList.get(i),examList)+1,expertList,examList);
	}

	/**
	 * 
	 * @param n - from 1. if n = 0 then precision = 0
	 * @param expertList
	 * @param examList
	 * @return
	 */
	private static double precision(int n, List<Vector> expertList, List<Vector> examList) {
		if (n != 0) {
			double numberOfRelevant = 0;
			int counter = 0;
			for (Vector vector : examList) {
				if (counter == n) {
					return numberOfRelevant / n;
				}
				if (expertList.contains(vector)) {
					numberOfRelevant++;
				}
				counter++;
			}
			return numberOfRelevant / n;
		}
		return 0;
	}

	/**
	 * 
	 * @param vectorForSearch
	 * @param examList
	 * @return number of position vectorForSearch in examList(from 0 to examList.size()-1). 
	 * If vectorForSearch don't contain in examList, then returned -1
	 */
	private static int position(Vector vectorForSearch, List<Vector> examList) {
		int position = 0;
		for (Vector vector : examList) {
			if(vector.equals(vectorForSearch)){
				return position;
			}
			position++;
		}
		return -1;
	}
	
	

}
