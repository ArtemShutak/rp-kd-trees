package com.shutart.rpkdtree.tests.kdtree;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.shutart.rpkdtree.kdtree.IKDTree;
import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.VectorI;
import com.shutart.rpkdtree.tests.linear.NNLinearSearcher;

public class SearchTest {
	private void compare(List<Vector> kdtreeRes, List<Vector> fullRes) {
		// TODO Auto-generated method stub
		Iterator<Vector> iter = kdtreeRes.iterator();
		if(kdtreeRes.size()==fullRes.size()){
			for (Vector node1 : fullRes) {
				Vector node2 = iter.next();
				Assert.assertEquals(node1, node2);
			}
			return;
		}
		Assert.fail();
		
	}

	@Test
	public void fullSearch() {
		System.out.println("Start fullSearch");
		int numberOfNeighbors = 10;
		Vector queryVector = new VectorI(22, 15);
		IKDTree kdTree = InsertTest.createKDTree();
		List<Vector> kdtreeRes = kdTree.nnsearch(numberOfNeighbors, queryVector);
		NNLinearSearcher nnSearcherFull = new NNLinearSearcher(
				InsertTest.getListOfVectors());
		List<Vector> fullRes = nnSearcherFull.nnsearch(numberOfNeighbors,
				queryVector);

		// dist:34.40930106817051  Node:[50.0, -5.0]
		// dist:28.0               Node:[50.0, 15.0]
		// dist:21.18962010041709  Node:[15.0, -5.0]
		// dist:15.297058540778355 Node:[7.0, 18.0]
		// dist:13.0               Node:[10.0, 20.0]
		// dist:0.0                Node:[22.0, 15.0]
		compare(kdtreeRes, fullRes);
		System.out.println("Finished fullSearch");
	}
	
	@Test
	public void exactlySearch() {
		System.out.println("Start exactlySearch");
		int numberOfNeighbors = 1;
		Vector queryVector = new VectorI(18,0);
		IKDTree kdTree = InsertTest.createKDTree();
		List<Vector> kdtreeRes = kdTree.nnsearch(numberOfNeighbors, queryVector);
		NNLinearSearcher nnSearcherFull = new NNLinearSearcher(
				InsertTest.getListOfVectors());
		List<Vector> fullRes = nnSearcherFull.nnsearch(numberOfNeighbors,
				queryVector);

		compare(kdtreeRes, fullRes);
		System.out.println("Finished exactlySearch");

	}

	@Test
	public void doubleSearch() {
		System.out.println("Start doubleSearch");
		int numberOfNeighbors = 2;
		Vector queryVector = new VectorI(8,-5);
		IKDTree kdTree = InsertTest.createKDTree();
		List<Vector> kdtreeRes = kdTree.nnsearch(numberOfNeighbors, queryVector);
		NNLinearSearcher nnSearcherFull = new NNLinearSearcher(
				InsertTest.getListOfVectors());
		List<Vector> fullRes = nnSearcherFull.nnsearch(numberOfNeighbors,
				queryVector);
	
		compare(kdtreeRes, fullRes);
		System.out.println("Finished doubleSearch");
	
	}
	
	@Test
	public void tripleSearch() {
		System.out.println("Start tripleSearch");
		int numberOfNeighbors = 3;
		Vector queryVector = new VectorI(18,17);
		IKDTree kdTree = InsertTest.createKDTree();
		List<Vector> kdtreeRes = kdTree.nnsearch(numberOfNeighbors, queryVector);
		NNLinearSearcher nnSearcherFull = new NNLinearSearcher(
				InsertTest.getListOfVectors());
		List<Vector> fullRes = nnSearcherFull.nnsearch(numberOfNeighbors,
				queryVector);
	
		compare(kdtreeRes, fullRes);
		System.out.println("Finished tripleSearch");
	
	}
	
	@Test
	public void someSearch() {
		System.out.println("Start tripleSearch");
		int numberOfNeighbors = 2;
		Vector queryVector = new VectorI(22,5);
		IKDTree kdTree = InsertTest.createKDTree();
		List<Vector> kdtreeRes = kdTree.nnsearch(numberOfNeighbors, queryVector);
		NNLinearSearcher nnSearcherFull = new NNLinearSearcher(
				InsertTest.getListOfVectors());
		List<Vector> fullRes = nnSearcherFull.nnsearch(numberOfNeighbors,
				queryVector);
	
		compare(kdtreeRes, fullRes);
		System.out.println("Finished tripleSearch");
	
	}


}
