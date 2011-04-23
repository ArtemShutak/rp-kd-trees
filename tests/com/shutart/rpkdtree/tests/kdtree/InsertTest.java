package com.shutart.rpkdtree.tests.kdtree;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.shutart.rpkdtree.kdtree.IKDTree;
import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.KDTree;
import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.VectorI;

public class InsertTest {
	@Test
	public void testRoot() {
		Vector rootVec = new VectorI(new double[] { 10, 20 });
		IKDTree kdTree = new KDTree(2);
		Vector vector = kdTree.insert(rootVec);
		//System.out.println(node.toString());
		//System.out.println(kdTree);
		String testString = 
				  "vector =    [10.0, 20.0]\n" 
				+ "discrim =   0\n"
				+ "LOSON =     null\n" 
				+ "HISON =     null\n"
				+ "minBounds = [-Infinity, -Infinity]\n"
				+ "maxBounds = [Infinity, Infinity]\n";
		//Assert.assertEquals(node.toString(), testString);
		Assert.assertTrue(vector.equals(rootVec));
	}
	
	@Test
	public void simpleTest(){
		Vector rootVec = new VectorI(new double[] { 10, 20 });
		IKDTree kdTree = new KDTree(2);
		Vector root = kdTree.insert(rootVec);
		Assert.assertTrue(root.equals(rootVec));
		
		Vector loSonVec = new VectorI(new double[] { 7, 18 });
		Vector loSon = kdTree.insert(loSonVec);
		Assert.assertTrue(loSon.equals(loSonVec));


		//System.out.println(kdTree);
		//print(root,loSon);
	}
	
	@Test
	public void simpleTest3Nodes(){
		Vector rootVec = new VectorI(new double[] { 10, 20 });
		IKDTree kdTree = new KDTree(2);
		Vector root = kdTree.insert(rootVec);
		
		Vector loSonVec = new VectorI(new double[] {7,18});
		Vector loSon = kdTree.insert(loSonVec);
		
		Vector hiSonVec = new VectorI(new double[] {22,15});
		Vector hiSon = kdTree.insert(hiSonVec);
		Assert.assertTrue(hiSon.equals(hiSonVec));
		
		//System.out.println(kdTree);
		//print(root,loSon,hiSon);
	}
	
	@Test
	public void simpleTest4NodesAndTestOfMethodIsLoSonSuccessor(){
		Vector rootVec = new VectorI(new double[] { 10, 20 });
		IKDTree kdTree = new KDTree(2);
		Vector root = kdTree.insert(rootVec);
		
		Vector loSonVec = new VectorI(new double[] {7,18});
		Vector loSon = kdTree.insert(loSonVec);
		
		Vector hiSonVec = new VectorI(new double[] {22,15});
		Vector hiSon = kdTree.insert(hiSonVec);
		
		Vector hihiSonVec = new VectorI(new double[] {50,15});
		Vector hihiSon = kdTree.insert(hihiSonVec);
		
		Assert.assertTrue(hihiSon.equals(hihiSonVec));
		
		//System.out.println(kdTree);
		//print(root, loSon,hiSon, hihiSon);
	}
	
	@Test
	public void simpleTest6Nodes(){
		IKDTree kdTree = createKDTree();
		
		System.out.println(kdTree);
		//print(root, loSon,hiSon, hihiSon);
	}

	public static IKDTree createKDTree() {
		IKDTree kdTree = new KDTree(2);
		for (Vector vector: getListOfVectors()) {
			kdTree.insert(vector);
		}
		return kdTree;
	}
	
	public static List<Vector> getListOfVectors() {
		List<Vector> res = new ArrayList<Vector>();
		Vector rootVec =      new VectorI(new double[] {10,20 });		
		Vector loSonVec =     new VectorI(new double[] { 7,18});		
		Vector hiSonVec =     new VectorI(new double[] {22,15});		
		Vector hihiSonVec =   new VectorI(new double[] {50,15});		
		Vector hiloSonVec =   new VectorI(new double[] {15,-5});		
		Vector hilohiSonVec = new VectorI(new double[] {50,-5});
		Vector loloSonVec = new VectorI(new double[] {5, 22});
		
		res.add(rootVec);
		res.add(loSonVec);
		res.add(hiSonVec);
		res.add(hihiSonVec);
		res.add(hiloSonVec);
		res.add(hilohiSonVec);
		res.add(loloSonVec);
		
		return res;
	}
	
	@Test
	public void searchRootVec(){
		IKDTree kdTree = new KDTree(2);
		Vector rootVec = new VectorI(new double[] { 10, 20 });
		Vector root = kdTree.insert(rootVec);
		
		Vector loSonVec = new VectorI(new double[] {7,18});
		Vector loSon = kdTree.insert(loSonVec);
		
		Vector hiSonVec = new VectorI(new double[] {22,15});
		Vector hiSon = kdTree.insert(hiSonVec);
		
		Vector hihiSonVec = new VectorI(new double[] {50,15});
		Vector hihiSon = kdTree.insert(hihiSonVec);
		
		Vector hiloSonVec = new VectorI(new double[] {15,-5});
		Vector hiloSon = kdTree.insert(hiloSonVec);
		
		Vector hilohiSonVec = new VectorI(new double[] {50,-5});
		Vector hilohiSon = kdTree.insert(hilohiSonVec);
		
		Vector findRoot = kdTree.insert(rootVec);
		
		Assert.assertEquals(((KDTree)kdTree).isModifiedInLastTime, false);
		Assert.assertEquals(root.toString(), findRoot.toString());
		Assert.assertTrue(root.equals(findRoot));

		
		System.out.println(kdTree);
		//print(root, loSon,hiSon, hihiSon);
	}
	
	@Test
	public void searchVec(){
		IKDTree kdTree = new KDTree(2);
		Vector rootVec = new VectorI(new double[] { 10, 20 });
		Vector root = kdTree.insert(rootVec);
		
		Vector loSonVec = new VectorI(new double[] {7,18});
		Vector loSon = kdTree.insert(loSonVec);
		
		Vector hiSonVec = new VectorI(new double[] {22,15});
		Vector hiSon = kdTree.insert(hiSonVec);
		
		Vector hihiSonVec = new VectorI(new double[] {50,15});
		Vector hihiSon = kdTree.insert(hihiSonVec);
		
		Vector hiloSonVec = new VectorI(new double[] {15,-5});
		Vector hiloSon = kdTree.insert(hiloSonVec);
		
		Vector hilohiSonVec = new VectorI(new double[] {50,-5});
		Vector hilohiSon = kdTree.insert(hilohiSonVec);
		
		Vector findHilohiSon = kdTree.insert(hilohiSonVec);
		
		Assert.assertEquals(((KDTree)kdTree).isModifiedInLastTime, false);
		Assert.assertEquals(hilohiSon.toString(), findHilohiSon.toString());
		Assert.assertEquals(hilohiSon, findHilohiSon);
		
		System.out.println(kdTree);
		//print(root, loSon,hiSon, hihiSon);
	}


	private void print(Vector... nodes) {
		for (Vector node : nodes) {
			System.out.println(node);
		}
		
	}
}
