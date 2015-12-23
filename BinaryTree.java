import java.util.Iterator;
import java.util.List;


public interface BinaryTree<E extends Comparable>  extends Iterable<E> {
	
	
	boolean add(E item);
	
	
	E max();
	
	
	int size();
	
	
	boolean isEmpty();
	
	
	E min();
	
	
	boolean contains(E item);
	
	
	boolean remove(E item);
	
    
	Iterator<E> iterator();
	
	
    List<E> getPostOrder();
      
   
    List<E> getLevelOrder();
    
   
    List<E> getPreOrder();
    
    
    void clear();
}
