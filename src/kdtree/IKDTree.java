package kdtree;

import java.util.List;

public interface IKDTree {
	/**
	 * Insert and Search.
	 * @param vector
	 * @return 
	 */
	INode insert(Vector vector);
	/**
	 * Nearest Neighbor Search (k-nn-search)
	 * @param numberOfNeighbors
	 * @param queryVector
	 * @return 
	 */
	List<Vector> nnsearch(int numberOfNeighbors, Vector queryVector);

}
