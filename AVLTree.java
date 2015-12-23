import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.LinkedList;

public class AVLTree<E extends Comparable> implements BinaryTree<E> {
	
	private class AVLNode<E> {
		private AVLNode<E> right = null;
		private AVLNode<E> left = null;
		private E data;
		private int height = 0;
		public AVLNode (E d) {
			data = d;
		}
	}
	
	
	private class AVLIterator<E> implements Iterator<E>{
		private ArrayList<E> list;
		private int index = 0;
		public AVLIterator (ArrayList<E> list) {
			this.list = list;
		}
		@Override
		public boolean hasNext() {
			if (list.size() > index) {
				return true;
			}
			return false;
		}
		@Override
		public E next() {
			if (hasNext()) {
				index++;
				return list.get(index-1);				
			}
			throw new NoSuchElementException();
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
			
		}
	}
	
	
	private int count = 0;
	private AVLNode<E> root;
	private E removed = null;
	private boolean added;

	@Override
	public boolean add(E item) {
		added = false;
		if (item == null) {
			return false;
		}
		AVLNode<E> add = new AVLNode<E> (item);
		if (isEmpty()) {
			root = add;
			count++;
			return true;
		} 
		root = binaryAdd(item, root);
		root.height = findHeight(root);
		testBF(root);
		return added;
	}

	@Override
	public E max() {
		if (isEmpty()) {
			return null;
		}
		AVLNode<E> node = recursiveMax(root);
		return node.data;
		
		
	}

	@Override
	public int size() {
		return count;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public E min() {
		if (isEmpty()) {
			return null;
		}
		AVLNode<E> node = recursiveMin(root);
		return node.data;
	}

	@Override
	public boolean contains(E item) {
		if (isEmpty()) {
			return false;
		}
		return hasItem(item, root);
		
	}

	@Override
	public boolean remove(E item) {
		if (item == null) {
			return false;
		}
		if (isEmpty()) {
			return false;
		}
		removeHelper(root, item);
		if (removed == null) {
			return false;
		}
		count--;
		return true;
		
	}

	@Override
	public Iterator<E> iterator() {
		listIn = new ArrayList<E>();
		recursiveIn(root);
		return new AVLIterator<E>((ArrayList<E>)listIn);
	}

	@Override
	public List<E> getPostOrder() {
		listPost = new ArrayList<E>();
		recursivePost(root);
		return listPost;
	}

	@Override
	public List<E> getLevelOrder() {
		recursiveLevel(root);
		return listLevel;
	}

	@Override
	public List<E> getPreOrder() {
		listPre = new ArrayList<E>();
		recursivePre(root);
		return listPre;
	}

	@Override
	public void clear() {
		root = null;
		count = 0;
	}
	
	private AVLNode<E> binaryAdd(E item, AVLNode<E> newRoot) {
		AVLNode<E> add = new AVLNode<E> (item);
		if (newRoot == null) {
			added = true;
			count++;
			return add;
		}
		if (newRoot.data.compareTo(item) < 0) {
			newRoot.right = binaryAdd(item, newRoot.right);	
		} 
		if (newRoot.data.compareTo(item) > 0) {
			newRoot.left = binaryAdd(item, newRoot.left);
		}
		newRoot.height = findHeight(newRoot);
		newRoot = testBF(newRoot);
		return newRoot;
	}
	
	private AVLNode<E> recursiveMax(AVLNode<E> theRoot) {
		if (theRoot.right == null) {
			return theRoot;
		} 
		return recursiveMax(theRoot.right);
		
		
	}
	
	private AVLNode<E> recursiveMin(AVLNode<E> theRoot) {
		if (theRoot.left != null) {
			return recursiveMin(theRoot.left);
		}
		return theRoot;
	}
	
	private boolean hasItem(E item, AVLNode<E> theRoot) {
		if (item.compareTo(theRoot.data) == 0) {
			return true;
		}
		if (item.compareTo(theRoot.data) < 0) {
			if (theRoot.left != null) {
				return hasItem(item, theRoot.left);
			}
		}
		if (item.compareTo(theRoot.data) > 0) {
			if (theRoot.right != null) {
				return hasItem(item, theRoot.right);
			}
		}
		return false;
	}
	
	private AVLNode<E> removeHelper(AVLNode<E> theRoot, E item) {
		AVLNode<E> temp;
		if (theRoot == null) {
			return null;
		}
		if (item.compareTo(theRoot.data) < 0) {
			theRoot.left = removeHelper(theRoot.left, item);
			theRoot.height = findHeight(theRoot);
			theRoot = testBF(theRoot);
			
		} else if (item.compareTo(theRoot.data) > 0) {
			theRoot.right = removeHelper(theRoot.right, item);	
			theRoot.height = findHeight(theRoot);
			theRoot = testBF(theRoot);
		} else {
			if (!hasLeft(theRoot) && !hasRight(theRoot)) {
				if (removed == null) {
					removed = theRoot.data;
				}
				return null;			
			} else if (!hasLeft(theRoot)) {
				if (removed == null) {
					removed = theRoot.data;
				}
				theRoot.height = findHeight(theRoot);
				theRoot = testBF(theRoot);
				return theRoot.right;
			} else if (!hasRight(theRoot)) {
				if (removed == null) {
					removed = theRoot.data;
				}
				theRoot.height = findHeight(theRoot);
				theRoot = testBF(theRoot);
				return theRoot.left;
			} else {
				AVLNode<E> successor = recursiveMin(theRoot.right);
				theRoot.data = successor.data;
				theRoot.right = removeHelper(theRoot.right, successor.data);
				theRoot.height = findHeight(theRoot);
				theRoot = testBF(theRoot);
				return theRoot;
			}
		}
		return theRoot;
	}
	
	private boolean hasLeft(AVLNode<E> node) {
		if (node.left != null) {
			return true;
		}
		return false;
	}
	
	private boolean hasRight(AVLNode<E> node) {
		if (node.right != null) {
			return true;
		}
		return false;
	}
	
	List<E> listPre = new ArrayList<E>();
	private void recursivePre(AVLNode<E> node) {
		if (node != null) {
			listPre.add(node.data);
			recursivePre(node.left);
			recursivePre(node.right);
		}
	}
	
	
	List<E> listPost = new ArrayList<E>();
	private void recursivePost(AVLNode<E> node) {
		if (node != null) {
			recursivePost(node.left);
			recursivePost(node.right);
			listPost.add(node.data);
		} 
	}
	
	List<E> listIn = new ArrayList<E>();
	private List<E> recursiveIn(AVLNode<E> node) {
		if (node != null) {
			recursiveIn(node.left);
			listIn.add(node.data);
			recursiveIn(node.right);
		}
		return listIn;
	}	
	
	List<E> listLevel = new ArrayList<E>();
	private void recursiveLevel(AVLNode<E> node) {
		listLevel = new ArrayList<E>();
		LinkedList<AVLNode<E>> queue = new LinkedList<AVLNode<E>>();
		if (node != null) {
			queue.add(node);
		}
		while (queue.size() != 0) {
			AVLNode<E> newNode = queue.poll();
			listLevel.add(newNode.data);
			if (newNode.left != null) {
				queue.add(newNode.left);
			}
			if (newNode.right != null) {
				queue.add(newNode.right);
			}
		}
	}
	
	int height;
	private int height(AVLNode<E> node) {
		return 0;
	}
	
	private AVLNode<E> rightRotation(AVLNode<E> oob) {
		AVLNode<E> newRoot = oob.left;
		oob.left = newRoot.right;
		newRoot.right = oob;
		oob.height = findHeight(oob);
		newRoot.height = findHeight(newRoot);
		return newRoot;
	}
	
	private AVLNode<E> leftRotation(AVLNode<E> oob) {
		AVLNode<E> newRoot = oob.right;
		oob.right = newRoot.left;
		newRoot.left = oob;
		oob.height = findHeight(oob);
		newRoot.height = findHeight(newRoot);
		return newRoot;
	}
	
	private AVLNode<E> leftRight(AVLNode<E> oob) {
		oob.left = leftRotation(oob.left);
		oob = rightRotation(oob);
		return oob;
	}
	
	private AVLNode<E> rightLeft(AVLNode<E> oob) {
		oob.right = rightRotation(oob.right);
		oob = leftRotation(oob);
		return oob;
	}
	
	private int findHeight(AVLNode<E> node) {
		if (!hasLeft(node) && !hasRight(node)) {
			return 0;
		} else if (hasLeft(node) && hasRight(node)) {
			return Math.max(node.left.height, node.right.height) + 1;
		} else if (hasLeft(node)) {
			return node.left.height + 1;
		} else {
			return node.right.height + 1;
		}
		
	}
	
	private int balanceFactor(AVLNode<E> node) {
		if (!hasLeft(node) && !hasRight(node)) {
			return 0;
		} else if (hasLeft(node) && hasRight(node)) {
			return node.left.height - node.right.height;
		} else if (hasLeft(node)) {
			return node.left.height + 1;
		} else {
			return (-1) - node.right.height;
		}
	}
	
	private AVLNode<E> testBF(AVLNode<E> node) {
		if (balanceFactor(node) > 1) {
			if (balanceFactor(node.left) > 0) {
				return rightRotation(node);
			} else {
				return leftRight(node);
			}
		}
		if (balanceFactor(node) < -1) {
			if (balanceFactor(node.right) < 0) {
				return leftRotation(node);
			} else {
				return rightLeft(node);
			}
		}
		return node;
	}
}
