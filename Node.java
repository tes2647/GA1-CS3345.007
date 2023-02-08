//Thang Tran 
//tdt200004

public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
	private T data;
	private Node<T> left;
	private Node<T> right;
	
	//constructors
	public Node (T data) {
	    this.data = data;
	    left = null;
	    right = null;
	}
	//accessor
	public Node<T> getLeft() {
	    return this.left;
	}
	public Node<T> getRight() {
	    return this.right;
	}
	public T getData() {
	    return this.data;
	}
	//mutators
	public void setLeft(Node<T> left) {
	    this.left = left;
	}
	public void setRight(Node<T> right) {
	    this.right = right;
	}
	public void setData(T data) {
	    this.data = data;
	}
	//comparator
	public int compareTo(Node<T> n) {
	    return this.data.compareTo(n.getData());
	}
}