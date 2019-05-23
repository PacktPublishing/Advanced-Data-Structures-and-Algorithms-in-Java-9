package Section_4;

import java.util.Comparator;
import java.util.Iterator;

public class BinomialForest<E> implements PriorityQueue<E> {
	protected Comparator<E> comparator;
	
	protected static class BinomialTree<E>{
		E value;
        LinkedList<BinomialTree<E>> subTrees = new LinkedList<>();
        
        public BinomialTree(E value){
            this.value = value;
        }
        
        public void displayTree(int depth){
            for(int i=0;i<depth;i++){
                System.out.print("    ");
            }
            System.out.println(value);
            for(BinomialTree<E> subTree: subTrees){
                subTree.displayTree(depth+1);
            }
            System.out.println();
        }
	}
	public BinomialForest(Comparator<E> comparator){
        this.comparator = comparator;
    }
	
	DoublyLinkedList<BinomialTree<E>> allTrees = new DoublyLinkedList<>();
	
	protected BinomialTree<E> merge(BinomialTree<E> left, BinomialTree<E> right){
        if(left==null){
            return right;
        }else if(right==null){
            return left;
        }
        if(left.subTrees.getLength() != right.subTrees.getLength()){
            throw new IllegalArgumentException("Trying to merge two unequal trees of sizes " +
                    left.subTrees.getLength() + " and " + right.subTrees.getLength());
        }
        if(comparator.compare(left.value, right.value)<0){
            left.subTrees.appendLast(right);
            return left;
        }else{
            right.subTrees.appendLast(left);
            return right;
        }
    }
	
	BinomialTree<E> minTree = null;
    int minTreeIndex = -1;
    
    protected void updateMinTree(){
        if(allTrees.getLength()==0){
            minTree = null;
            minTreeIndex = -1;
        }
        E min = null;
        int index = 0;
        for(BinomialTree<E> tree:allTrees){
            if(tree==null){
                index++;
                continue;
            }
            if(min == null || comparator.compare(min, tree.value)>0){
                min = tree.value;
                minTree = tree;
                minTreeIndex = index;
            }
            index++;
        }
    }
    
    protected BinomialTree<E> computeOutputWithoutCarry(BinomialTree<E> lhs, BinomialTree<E> rhs, BinomialTree<E> carry){
        if(carry==null){
            if(lhs==null){
                return rhs;
            }else if(rhs==null){
                return lhs;
            }else{
                return null;
            }
        }else{
            if(lhs==null && rhs==null){
                return carry;
            }else if(lhs == null){
                return null;
            }else if(rhs == null){
                return null;
            }else{
                return carry;
            }
        }
    }
    
    protected BinomialTree<E>  computeCarry(BinomialTree<E> lhs, BinomialTree<E> rhs, BinomialTree<E> carry){
        if(carry==null){
            if(lhs!=null && rhs!=null){
                return merge(lhs, rhs);
            }else{
                return null;
            }
        }else{
            if(lhs==null && rhs==null){
                return null;
            }else if(lhs == null){
                return merge(carry, rhs);
            }else if(rhs == null){
                return merge(carry, lhs);
            }else{
                return merge(lhs, rhs);
            }
        }
    }
    
    protected void merge(LinkedList<BinomialTree<E>> rhs){
        LinkedList<BinomialTree<E>>.ListIterator lhsIter
                = (LinkedList<BinomialTree<E>>.ListIterator)allTrees.iterator();
        Iterator<BinomialTree<E>> rhsIter = rhs.iterator();
        BinomialTree<E> carry = null;
        while(lhsIter.hasNext() || rhsIter.hasNext()){
            boolean lhsHasValue = lhsIter.hasNext();
            BinomialTree<E> lhsTree = lhsHasValue? lhsIter.next():null;
            BinomialTree<E> rhsTree = rhsIter.hasNext()? rhsIter.next():null;
            BinomialTree<E> entry = computeOutputWithoutCarry(lhsTree, rhsTree, carry);
            carry = computeCarry(lhsTree, rhsTree, carry);
            if(lhsHasValue) {
                lhsIter.setValue(entry);
            }else{
                this.allTrees.appendLast(entry);
            }
        }
        if(carry!=null){
            this.allTrees.appendLast(carry);
        }
        updateMinTree();
    }
    
    public void insert(E value){
        BinomialTree<E> newTree = new BinomialTree<E>(value);
        DoublyLinkedList<BinomialTree<E>> newList = new DoublyLinkedList<>();
        newList.appendLast(newTree);
        merge(newList);
    }
    
    public E removeMin(){
        if(allTrees.getLength()==0){
            return null;
        }
        E min = minTree.value;
        if(minTreeIndex==allTrees.getLength()-1){
            allTrees.removeLast();
        }else {
            allTrees.setValueAtIndex(minTreeIndex, null);
        }
        merge(minTree.subTrees);
        return min;
    }
    
    @Override
    public E dequeueMinimum() {
        return removeMin();
    }
    
    @Override
    public void enqueue(E value) {
        insert(value);
    }
    
    @Override
    public E checkMinimum(){
        return minTree==null? null:minTree.value;
    }
    
    public static void main(String [] args){
        BinomialForest<Integer> binomialForest = new BinomialForest<>((a,b)->a-b);
        for(int i=1;i<100;i++){
            binomialForest.insert((int)(Math.random()*1000));
        }
        for(BinomialTree<Integer> tree: binomialForest.allTrees){
           if(tree!=null)
               tree.displayTree(0);
           System.out.println("==================");
       }
        System.out.println(binomialForest.checkMinimum());
        while (binomialForest.checkMinimum()!=null){
            Integer min = binomialForest.removeMin();
            if(min==null){
                break;
            }
            System.out.print(" "+min+",");
        }
        System.out.println();
    }
}