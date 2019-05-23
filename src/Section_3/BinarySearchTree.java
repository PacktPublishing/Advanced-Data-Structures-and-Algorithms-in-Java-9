package Section_3;

import Section_2.BinaryTree;

public class BinarySearchTree<E extends Comparable<E>> extends BinaryTree<E> {

	protected Node<E> searchValue(E value, Node<E> root){
        if(root==null){
            return null;
        }
        int comp = root.getValue().compareTo(value);
        if(comp == 0){
            return root;
        }else if(comp>0){
            return searchValue(value, root.getLeft());
        }else{
            return  searchValue(value, root.getRight());
        }
    }
	
	public Node<E> searchValue(E value){
        if(getRoot()==null){
            return null;
        }else{
            return searchValue(value, getRoot());
        }
    }
	
	protected Node<E> insertValue(E value, Node<E> node){
        int comp = node.getValue().compareTo(value);
        Node<E> child;
        if(comp<0){
            child = node.getRight();
            if(child==null){
                return addChild(node,value,false);
            }else{
                return insertValue(value, child);
            }
        }else if(comp>0){
            child = node.getLeft();
            if(child==null){
                return addChild(node,value,true);
            }else{
                return insertValue(value, child);
            }
        }else{
            return null;
        }
    }

    public Node<E> insertValue(E value){
        if(getRoot()==null){
            addRoot(value);
            return getRoot();
        }else{
            return insertValue(value, getRoot());
        }
    }
	
	protected Node<E> getLeftMost(Node<E> node){
        if(node==null){
            return null;
        }else if(node.getLeft()==null){
            return node;
        }else{
            return getLeftMost(node.getLeft());
        }
    }
	
	private Node<E> deleteNode(Node<E> nodeToBeDeleted){
		boolean direction;
		if(nodeToBeDeleted.getParent()!=null
                && nodeToBeDeleted.getParent().getLeft()==nodeToBeDeleted){
            direction = true;
        }else{
            direction = false;
        }
		if(nodeToBeDeleted.getLeft()==null && nodeToBeDeleted.getRight()==null){
            deleteNodeWithSubtree(nodeToBeDeleted);
            return nodeToBeDeleted;
        }else if(nodeToBeDeleted.getLeft()==null){
            if(nodeToBeDeleted.getParent() == null){
                root = nodeToBeDeleted.getRight();
            }else {
                setChild(nodeToBeDeleted.getParent(),
                        nodeToBeDeleted.getRight(), direction);
            }
            return nodeToBeDeleted;
        }else if(nodeToBeDeleted.getRight()==null){
            if(nodeToBeDeleted.getParent() == null){
                root = nodeToBeDeleted.getLeft();
            }else {
                setChild(nodeToBeDeleted.getParent(),
                        nodeToBeDeleted.getLeft(), direction);
            }
             return nodeToBeDeleted;
         }else{
             Node<E> nodeToBeReplaced = getLeftMost(nodeToBeDeleted.getRight());
             setValue(nodeToBeDeleted, nodeToBeReplaced.getValue());
             deleteNode(nodeToBeReplaced);
             return nodeToBeReplaced;
         } 
	}
	
	public Node<E> deleteValue(E value){
        Node<E> nodeToBeDeleted = searchValue(value);
        if(nodeToBeDeleted==null){
            return null;
        }else{

           return deleteNode(nodeToBeDeleted);
        }
    }
	
	public static void main(String[] args) {
		BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        int stored = -1;
        for(int i=0;i<20;i++){
            int value = (int) (100*Math.random());
            tree.insertValue(value);
            if(stored == -1 && (Math.random()>0.7 || i == 19)){
                stored = value;
            }
        }
        tree.traverseDepthFirst((x)->
        System.out.print(" "+x), tree.getRoot(), DepthFirstTraversalType.INORDER);
        System.out.println();
        System.out.println("==========================================");        
        tree.displayText();
	}
}