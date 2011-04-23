package com.shutart.rpkdtree.tests.rpkdtrees;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.shutart.rpkdtree.kdtree.INode;
import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.VectorI;
import com.shutart.rpkdtree.rpkdtrees.RPKDTrees;
import com.shutart.rpkdtree.tests.linear.NNLinearSearcher;

public class RPKDTreesTest {
	
	public static void main(String[] args){
		int dimension = 4;
		int projectidDimension = 2;
		int numberOfTrees = 3;
		int numberOfNeighbors = 10;
		Vector queryVector = new VectorI(2,10,15,8);
		Set<Vector> corpus = createCorpus();
		
		RPKDTrees tree = new RPKDTrees(dimension, projectidDimension, numberOfTrees);
		tree.indexing(corpus);
		List<Vector> approxRes = tree.aproxNNsearch(numberOfNeighbors, queryVector);
		print(approxRes);
		
		List<Vector> exactRes = new NNLinearSearcher(corpus).nnsearch(numberOfNeighbors, queryVector);
		print(exactRes);

	}

	private static void print(List<Vector> list) {
		for (Vector vector : list) {
			System.out.print(vector + "   ");
		}
		System.out.println("\n_______________________________");
	}

	private static Set<Vector> createCorpus() {
		Set<Vector> corpus = new LinkedHashSet<Vector>();
		corpus.add(new VectorI(10,15,87,14));
		corpus.add(new VectorI(2,14,37,82));
		corpus.add(new VectorI(18,13,34,632));
		corpus.add(new VectorI(8,13,4,632));
		corpus.add(new VectorI(218,13,34,33));
		corpus.add(new VectorI(38,34,44,62));
		return corpus;
	}

}
