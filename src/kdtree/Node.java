package kdtree;

import java.util.Arrays;

public class Node implements INode {
	private Vector myVector;
	private INode myLOSON = null;
	private int myDiscriminator = -1;
	private INode myHISON = null;
	private double[] myMinBounds;
	private double[] myMaxBounds;

	public Node(Vector vector) {
		myVector = vector;
	}

	@Override
	public boolean isLoSonSuccessorOf(INode parent) {
		assert !this.getVector().equals(parent.getVector());
		int parentDisc = parent.getDiscriminator();
		if (this.getKey(parentDisc) < parent.getKey(parentDisc)) {
			return true;
		}
		if (this.getKey(parentDisc) > parent.getKey(parentDisc)) {
			return false;
		}
		for (int index = (parentDisc + 1)%getDimension(); index != parentDisc; index = (index + 1)
				% getDimension()) {
			if (this.getKey(index) < parent.getKey(index)) {
				return true;
			}
			if (this.getKey(index) > parent.getKey(index)) {
				return false;
			}
		}
		throw new IllegalArgumentException(
				"Current vector equals parent vector!");
	}

	@Override
	public void initBoundsAndDiscrim(INode father, boolean itIsLoSon) {
		myMinBounds = new double[getDimension()];
		myMaxBounds = new double[getDimension()];
		if (father == null) {
			myDiscriminator = 0;
			setBoundsAsIfinities();
		} else {
			myDiscriminator = (father.getDiscriminator() + 1) % getDimension();
			setBounds(father, itIsLoSon);
		}
	}

	private void setBoundsAsIfinities() {
		for (int i = 0; i < getDimension(); i++) {
			myMinBounds[i] = Double.NEGATIVE_INFINITY;
			myMaxBounds[i] = Double.POSITIVE_INFINITY;
		}
	}

	private void setBounds(INode father, boolean itIsLoSon) {
		for (int i = 0; i < getDimension(); i++) {
			myMinBounds[i] = father.getMinBounds(i);
			myMaxBounds[i] = father.getMaxBounds(i);
		}
		int fatherDisc = father.getDiscriminator();
		if (itIsLoSon) {
			myMaxBounds[fatherDisc] = father.getKey(fatherDisc);
		} else {
			myMinBounds[fatherDisc] = father.getKey(fatherDisc);
		}
	}

	@Override
	public int getDiscriminator() {
		return myDiscriminator;
	}

	@Override
	public INode getLoSon() {
		return myLOSON;
	}

	@Override
	public INode getHiSon() {
		return myHISON;
	}

	@Override
	public double getKey(int index) {
		return myVector.getKey(index);
	}

	@Override
	public Vector getVector() {
		return myVector;
	}

	@Override
	public INode getSon(boolean loSon) {
		if (loSon) {
			return getLoSon();
		} else {
			return getHiSon();
		}
	}

	@Override
	public double getMinBounds(int i) {
		return myMinBounds[i];
	}

	@Override
	public double getMaxBounds(int i) {
		return myMaxBounds[i];
	}

	private int getDimension() {
		return myVector.size();
	}

	@Override
	public void setLoSon(INode node) {
		assert myLOSON == null;
		myLOSON = node;
	}

	@Override
	public void setHiSon(INode node) {
		assert myHISON == null;
		myHISON = node;
	}

	@Override
	public void setSon(boolean loSon, INode node) {
		if (loSon) {
			setLoSon(node);
		} else {
			setHiSon(node);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("max = " + Arrays.toString(myMaxBounds) + "\n");
		sb.append("vec = " + myVector + "\n");
		sb.append("min = " + Arrays.toString(myMinBounds)	+ "\n");
		sb.append("dis =   " + myDiscriminator + "\n");
		if (myLOSON == null)
			sb.append("LOS =     " + "null" + "\n");
		else
			sb.append("LOS =     " + myLOSON.getVector() + "\n");
		if (myHISON == null)
			sb.append("HIS =     " + "null" + "\n");
		else
			sb.append("HIS =     " + myHISON.getVector() + "\n");

		return sb.toString();
	}

}
