package com.shutart.rpkdtree.tests.kdtree;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.shutart.rpkdtree.kdtree.IKDTree;
import com.shutart.rpkdtree.kdtree.INode;
import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.VectorI;

public class SearchTest {

	@Test
	public void simpleTest6Nodes() {
		int numberOfNeighbors = 2;
		Vector queryVector = new VectorI(22, 15);
		IKDTree kdTree = InsertTest.createKDTree();
		List<INode> kdtreeRes = kdTree.nnsearch(numberOfNeighbors, queryVector);
		NNSearcherFull nnSearcherFull = new NNSearcherFull(
				InsertTest.getListOfVectors());
		List<INode> fullRes = nnSearcherFull.nnsearch(numberOfNeighbors,
				queryVector);

		compare(kdtreeRes, fullRes);
	}

	private void compare(List<INode> kdtreeRes, List<INode> fullRes) {
		// TODO Auto-generated method stub
		Iterator<INode> iter = kdtreeRes.iterator();
		if(kdtreeRes.size()==fullRes.size()){
			for (INode node1 : fullRes) {
				INode node2 = iter.next();
				if(!node1.getVector().equals(node2.getVector())){
					Assert.fail();
				}
			}
			return;
		}
		Assert.fail();
		
	}
}
