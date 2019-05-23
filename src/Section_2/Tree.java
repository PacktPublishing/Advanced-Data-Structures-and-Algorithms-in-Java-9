package Section_2;

import Section_1.OneArgumentStatement;

public class Tree<E> {

	public static class Node<E>{
		private E value;
        private LinkedList<Node<E>> children;
        private Tree<E> hostTree;
        private Node<E> parent;
		
        public E getValue() {
			return value;
		}
		public LinkedList<Node<E>> getChildren() {
			return children;
		}
        
		private Node(LinkedList<Node<E>> children, Tree<E> hostTree, E value, Node<E> parent) {
            this.children = children;
            this.hostTree = hostTree;
            this.value = value;
            this.parent = parent;
        }
	}
	private Node<E> root;
	
	public void addRoot(E value){
        if(root == null){
            root = new Node<>(new LinkedList<>(), this, value, null );
        }else{
            throw new IllegalStateException("Trying to add new node to a non empty tree");
        }
    }
	
	public Node<E> addNode(Node<E> parent, E value){
        if(parent==null){
            throw new NullPointerException("Cannot add child to null parent");
        }else if(parent.hostTree != this){
            throw new IllegalArgumentException("Parent node not a part of this tree");
        }else{
            Node<E> newNode = new Node<>(new LinkedList<>(), this, value, parent);
            parent.getChildren().appendLast(newNode);
            return newNode;
        }
    }
	
	public Node<E> getRoot() {
        return root;
    }
	
	protected void traverseDepthFirst(OneArgumentStatement<E> processor, Node<E> current){
        processor.doSomething(current.value);
        current.children.forEach((n)-> traverseDepthFirst(processor, n));
    }
	
	public void traverseDepthFirst(OneArgumentStatement<E> processor){
        traverseDepthFirst(processor, getRoot());
    }
	
	public void traverseDepthFirstUsingStack(OneArgumentStatement<E> processor){
	    Stack<Node<E>> stack = new StackImplLinkedList<>();
	    stack.push(getRoot());
	    while(stack.peek()!=null){
	        Node<E> current = stack.pop();
	        processor.doSomething(current.value);
	        LinkedList<Node<E>> reverseList = new LinkedList<>();
            current.children.forEach((n)->reverseList.appendFirst(n));
            reverseList.forEach((n)->stack.push(n));
	    }
	}
	
	public void traverseBreadthFirst(OneArgumentStatement<E> processor){
        Queue<Node<E>> queue = new QueueImplLinkedList<>();
        queue.enqueue(getRoot());
        while(queue.peek()!=null){
            Node<E> current = queue.dequeue();
            processor.doSomething(current.value);
            current.children.forEach((n)->queue.enqueue(n));
        }
    }
	
	public static void main(String [] args){
	    Tree<Integer> tree = new Tree<>();
	    tree.addRoot(1);
	    Node<Integer> node1 = tree.getRoot();
	    Node<Integer> node2 = tree.addNode(node1, 5);
	    Node<Integer> node3 = tree.addNode(node1, 1);
	    Node<Integer> node4 = tree.addNode(node2, 2);
	    Node<Integer> node5 = tree.addNode(node2, 5);
	    Node<Integer> node6 = tree.addNode(node2, 9);
	    Node<Integer> node7 = tree.addNode(node3, 6);
	    Node<Integer> node8 = tree.addNode(node3, 2);
	    Node<Integer> node9 = tree.addNode(node5, 5);
	    Node<Integer> node10 = tree.addNode(node6, 9);
	    Node<Integer> node11 = tree.addNode(node6, 6);
	    
	    tree.traverseDepthFirst(System.out::print);
        System.out.println();
        tree.traverseDepthFirstUsingStack(System.out::print);
        System.out.println();
        tree.traverseBreadthFirst(System.out::print);
        System.out.println();
	}
}
