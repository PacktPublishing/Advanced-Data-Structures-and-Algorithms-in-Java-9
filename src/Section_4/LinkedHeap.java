package Section_4;

import java.util.Comparator;
import java.util.Iterator;

public class LinkedHeap<E> implements PriorityQueue<E> {
	protected static class Node<E>{
		protected E value;
        protected Node<E> left;
        protected Node<E> right;
        protected Node<E> parent;
        
        public Node(E value, Node<E> parent){
            this.value = value;
            this.parent = parent;
        }
	}
	
	protected Node<E> root;
    protected int numElements;
    
    protected Node<E> findNodeAtPostion(int position){
        if(position == 1){
            return root;
        }else{
            int side = position % 2;
            int parentPosition = position / 2;
            Node<E> parent = findNodeAtPostion(parentPosition);
            switch (side){
                case 0:
                    return parent.left;
                case 1:
                    return parent.right;
            }
        }
        return null;
    }
    
    protected void swapWithParent(Node<E> parent,boolean left) {
    	Node<E> node = left? parent.left:parent.right;
        Node<E> leftChild = node.left;
        Node<E> rightChild = node.right;
        Node<E> sibling = left? parent.right:parent.left;
        Node<E> grandParent = parent.parent;
        parent.left = leftChild;
        if(leftChild!=null){
            leftChild.parent = parent;
        }
        parent.right = rightChild;
        if(rightChild!=null){
            rightChild.parent = parent;
        }
        parent.parent = node;
        if(left){
            node.right = sibling;
            node.left = parent;
        }else{
            node.left = sibling;
            node.right = parent;
        }
        node.parent = grandParent;
        if(sibling!=null)
            sibling.parent = node;

        if(parent == root){
            root = node;
        }else{
            boolean parentLeft = grandParent.left==parent;
            if(parentLeft){
                grandParent.left = node;
            }else{
                grandParent.right = node;
            }
        }
    }
    
    protected Comparator<E> comparator;
    
    protected void trickleUp(Node<E> node){
        if(node==root){
            return;
        }else if(comparator.compare(node.value, node.parent.value)<0){
            swapWithParent(node.parent, node.parent.left == node);
            trickleUp(node);
        }
    }
    
    public void insert(E value){
        if(root==null){
            root = new Node<>(value, null);
        }else{
            Node<E> parent = findNodeAtPostion((numElements+1)/2);
            int side = (numElements+1)%2;
            Node<E> newNode = new Node<>(value, parent);
            switch (side){
                case 0:
                    parent.left = newNode;
                    break;
                case 1:
                    parent.right = newNode;
                    break;
            }
            trickleUp(newNode);
        }
        numElements++;
    }
    
    protected void trickleDown(Node<E> node){
        if(node==null){
            return;
        }
        if(node.left == null){
            return;
        }else if(node.right == null){
            if(comparator.compare(node.left.value, node.value)<0){
                swapWithParent(node, true);
                trickleDown(node);
            }
        }else{
            if(comparator.compare(node.left.value, node.right.value)<0){
                if(comparator.compare(node.left.value, node.value)<0){
                    swapWithParent(node, true);
                    trickleDown(node);
                }
            }else{
                if(comparator.compare(node.right.value, node.value)<0){
                    swapWithParent(node, false);
                    trickleDown(node);
                }
            }
        }
    }
    
    public E removeMin(){
        if(root==null){
            return null;
        }
        Node<E> lastElement = findNodeAtPostion(numElements);
        if(lastElement==root){
            root = null;
            numElements--;
            return lastElement.value;
        }
        E value = root.value;
        root.value = lastElement.value;
        Node<E> parent = lastElement.parent;
        if(parent.left==lastElement){
            parent.left = null;
        }else{
            parent.right=null;
        }
        numElements--;
        trickleDown(root);
        return value;
    }
    
    @Override
    public E checkMinimum() {
        return root==null? null : root.value;
    }
    
    @Override
    public E dequeueMinimum() {
        return removeMin();
    }
    
    @Override
    public void enqueue(E value) {
         insert(value);
    }
    
    protected void displayText(Node<E> parent, int depth){

        for(int i=0;i<depth;i++){
            System.out.print("    ");
        }
        if(parent==null){
            System.out.println("*");
            return;
        }
        System.out.println(parent.value);
        displayText(parent.left,depth+1);
        displayText(parent.right,depth+1);
    }
    
    protected void displayText(){
        displayText(root, 0);
    }
    
    public LinkedHeap(Comparator<E> comparator){
        this.comparator = comparator;
    }
    
    public static void main(String [] args){
        LinkedHeap<Integer> heap = new LinkedHeap<>((a,b)-> b-a);
        for(int i=1;i<=100;i++){
            heap.insert((int)(Math.random()*1000));
        }
        System.out.println(heap.numElements);
        Integer value = null;
        while((value = heap.removeMin())!=null){
           System.out.print(value+", ");
        }
    }
}
