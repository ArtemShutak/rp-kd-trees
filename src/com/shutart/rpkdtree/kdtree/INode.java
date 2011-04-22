package com.shutart.rpkdtree.kdtree;

public interface INode {
	double getKey(int index);
	boolean isLoSuccessorOf(INode parent);
	void initBoundsAndDiscrim(INode father, boolean itIsLoSon);
	//Getter methods	
	/**
	 * @return int from 0 to k-1
	 */
	int getDiscriminator();
	
	/**
	 * @param parent
	 * @return
	 */
	
	/**
	 * LeftNode = LOSON (Low Son). 
	 * If d - discriminator, then LOSON.getKey(d) < this.getKey(d)
	 * @return
	 */
	INode getLoSon();
	/**
	 * RightNode = HISON (High Son)
	 * If d - discriminator, then this.getKey(d) < HISON.getKey(d) 
	 * @return
	 */
	INode getHiSon();

	Vector getVector();
	INode getSon(boolean itIsLoSon);

	double getMinBounds(int i);
	double getMaxBounds(int i);

	Vector getMainVector();
	//Setter methods
	void setLoSon(INode insertableNode);
	void setHiSon(INode insertableNode);
	void setSon(boolean loSon, INode insertableNode);
	boolean isLeaf();
	
	double distance(INode node);
	double coordinateDistance(int numberOfCoord, double value2);
	double f_i(int numberOfCoord, double value2);
}
