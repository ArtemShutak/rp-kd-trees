package kdtree;

import java.util.ArrayList;
import java.util.List;

public class KDTree implements IKDTree {
	private INode root = null;
	public boolean isModifiedInLastTime = false;

	public KDTree() {

	}

	@Override
	public INode insert(Vector vector) {
		isModifiedInLastTime = false;
		INode insertableNode = new Node(vector);
		if (root == null) {
			insertableNode.initBoundsAndDiscrim(null, false);
			root = insertableNode;
			return root;
		}
		INode curNode = root;
		while (true) {
			if(curNode.getVector().equals(vector)){
				return curNode;
			}
			boolean itIsLoSon = insertableNode.isLoSonSuccessorOf(curNode);
			INode successor = curNode.getSon(itIsLoSon);
			if(successor == null){
				isModifiedInLastTime = true;
				insertableNode.initBoundsAndDiscrim(curNode, itIsLoSon);
				curNode.setSon(itIsLoSon, insertableNode);
				return insertableNode;
			}else{
				curNode = successor;
			}
		}
	}

	@Override
	public List<Vector> nnserch(int numberOfNeighbors, Vector queryVector) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		if(root!=null){
			INode saveRoot = root;
			root = saveRoot.getLoSon();
			String losonTreeString = this.toString();
			root = saveRoot.getHiSon();
			String hisonTreeString = this.toString();
			root = saveRoot;
			StringBuilder sb = new StringBuilder();
			sb.append(root.toString()+ "\n");
			if(losonTreeString!=null){
				sb.append("_LOSON_"+ root.getVector() +"\n{\n" + losonTreeString + "}\n");
			}
			if(hisonTreeString!=null){
				sb.append("_HISON_"+ root.getVector() +"\n{\n" + hisonTreeString + "}\n");
			}
			return sb.toString();
		}
		return null;
	}

}
