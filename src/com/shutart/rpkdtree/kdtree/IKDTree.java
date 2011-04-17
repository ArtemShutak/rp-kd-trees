package com.shutart.rpkdtree.kdtree;

import java.util.List;

public interface IKDTree {
	/**
	 * Insert and Search.
	 * @param vector
	 * @return 
	 */
	INode insert(final Vector vector);
	/**
	 * Nearest Neighbor Search (k-nn-search)
	 * @param numberOfNeighbors - number of best matches
	 * @param queryVector
	 * @return 
	 */
	List<INode> nnsearch(final int numberOfNeighbors,final Vector queryVector);

}
