package Section_2;

import Section_1.OneArgumentStatement;

public class BinaryTree<E> {

	public static class Node<E>{
		protected E value;
		protected Node<E> left;
		protected Node<E> right;
		protected Node<E> parent;
		protected BinaryTree<E> containerTree;
		
		protected Node(Node<E> parent,BinaryTree<E> containerTree, E value) {
            this.value = value;
            this.parent = parent;
            this.containerTree = containerTree;
        }
		
		public E getValue(){
		    return value;
		}

		public Node<E> getRight() {
		    return right;
		}

		public Node<E> getParent() {
		    return parent;
		}

		public Node<E> getLeft() {
		    return left;
		}

		public BinaryTree<E> getContainerTree() {
		    return containerTree;
		}	
	}
	protected Node<E> root;
	
	public void addRoot(E value){
        if(root==null){
            root = newNode(null, this,  value);
        }else{
            throw new IllegalStateException("Root already exists");
        }
    }
	
	public Node<E> getRoot(){
        return root;
    }
	
	public Node<E> addChild(Node<E> parent, E value, boolean left){
        if(parent == null){
            throw new NullPointerException("Cannot add node to null parent");
        }else if(parent.containerTree != this){
            throw new IllegalArgumentException("Parent does not belong to this tree");
        }else {
            Node<E> child = newNode(parent, this, value);
            if(left){
                parent.left = child;
            }else{
                parent.right = child;
            }
            return child;
        }
    }
	
	public Node<E> addChildLeft(Node<E> parent, E value){
        return addChild(parent, value, true);
    }
	
	public Node<E> addChildRight(Node<E> parent, E value){
        return addChild(parent, value, false);
    }
	
	protected  Node<E> newNode(Node<E> parent,BinaryTree<E> containerTree, E value){
        return new Node<>(parent, containerTree, value);
    }
	
	public static enum DepthFirstTraversalType{
        PREORDER, INORDER, POSTORDER
    }
	
	public void traverseDepthFirst(OneArgumentStatement<E> processor, Node<E> current, DepthFirstTraversalType tOrder){
        if(current==null){
            return;
        }
        if(tOrder == DepthFirstTraversalType.PREORDER){
            processor.doSomething(current.value);
        }
        traverseDepthFirst(processor, current.left, tOrder);
        if(tOrder == DepthFirstTraversalType.INORDER){
            processor.doSomething(current.value);
        }
        traverseDepthFirst(processor, current.right, tOrder);
        if(tOrder == DepthFirstTraversalType.POSTORDER){
            processor.doSomething(current.value);
        }
    }
	
	public void traversePreOrderNonRecursive(OneArgumentStatement<E> processor) {
        Stack<Node<E>> stack = new StackImplLinkedList<>();
        stack.push(getRoot());
        while (stack.peek()!=null){
            Node<E> current = stack.pop();
            processor.doSomething(current.value);
            if(current.right!=null)
                stack.push(current.right);
            if(current.left!=null)
                stack.push(current.left);
        }
    }
	
	public void traverseInOrderNonRecursive(OneArgumentStatement<E> processor) {
        class StackFrame{
            Node<E> node;
            boolean childrenPushed = false;

            public StackFrame(Node<E> node, boolean childrenPushed) {
                this.node = node;
                this.childrenPushed = childrenPushed;
            }
        }
        Stack<StackFrame> stack = new StackImplLinkedList<>();
        stack.push(new StackFrame(getRoot(), false));
        while (stack.peek()!=null){
            StackFrame current = stack.pop();
            if(current.childrenPushed){
                processor.doSomething(current.node.value);
            }else{
                if(current.node.right!=null)
                    stack.push(new StackFrame(current.node.right, false));
                stack.push(new StackFrame(current.node, true));
                if(current.node.left!=null)
                    stack.push(new StackFrame(current.node.left, false));
            }
        }
    }
	
	public void traverseDepthFirstNonRecursive(OneArgumentStatement<E> processor, DepthFirstTraversalType tOrder) {
        class StackFrame {
            Node<E> node;
            int state;

            public StackFrame(Node<E> node, int state) {
                this.node = node;
                this.state = state;
            }
        }

        Stack<StackFrame> stack =
                new StackImplLinkedList<StackFrame>();
        stack.push(new StackFrame(root, 0));
        stackLoop:while (stack.peek() != null){
            StackFrame frame = stack.pop();
            int state = frame.state;
            Node<E> currentNode = frame.node;
            start:while(true) {
                if(currentNode==null){
                    continue stackLoop;
                }
                switch (state) {
                    case 0:
                        if (tOrder ==
                                DepthFirstTraversalType.PREORDER) {
                            processor.doSomething(currentNode.value);
                        }
                        stack.push(new StackFrame(currentNode, 1));
                        state = 0;
                        currentNode = currentNode.left;
                        continue start;
                    case 1:
                        if (tOrder ==
                                DepthFirstTraversalType.INORDER) {
                            processor.doSomething(currentNode.value);
                        }
                        stack.push(new StackFrame(currentNode, 2));
                        state = 0;
                        currentNode = currentNode.right;
                        continue start;
                    case 2:
                        if (tOrder ==
                                DepthFirstTraversalType.POSTORDER) {
                            processor.doSomething(currentNode.value);
                        }
                        break start;

                }

            }
        }
    }
	
	public void traversePostOrderNonRecursive(OneArgumentStatement<E> processor) {
        class StackFrame{
            Node<E> node;
            boolean childrenPushed = false;

            public StackFrame(Node<E> node, boolean childrenPushed) {
                this.node = node;
                this.childrenPushed = childrenPushed;
            }
        }
        Stack<StackFrame> stack = new StackImplLinkedList<>();
        stack.push(new StackFrame(getRoot(), false));
        while (stack.peek()!=null){
            StackFrame current = stack.pop();
            if(current.childrenPushed){
                processor.doSomething(current.node.value);
            }else{
                stack.push(new StackFrame(current.node, true));
                if(current.node.right!=null)
                    stack.push(new StackFrame(current.node.right, false));

                if(current.node.left!=null)
                    stack.push(new StackFrame(current.node.left, false));
            }
        }
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
        displayText(parent.getLeft(),depth+1);
        displayText(parent.getRight(),depth+1);
    }
	
	protected void displayText(){
        displayText(getRoot(), 0);
    }
	
	public void deleteNodeWithSubtree(Node<E> node){
        if(node == null){
            throw new NullPointerException("Cannot delete to null parent");
        }else if(node.containerTree != this){
            throw new IllegalArgumentException("Node does not belong to this tree");
        }else {
            if(node==getRoot()){
                root=null;
                return;
            }else{
                Node<E> parent = node.getParent();
                if(parent.getLeft()==node){
                    parent.left = null;
                }else{
                    parent.right = null;
                }
            }
        }
    }
	
	public void setValue(Node<E> node, E value){
        if(node == null){
            throw new NullPointerException("Cannot add node to null parent");
        }else if(node.containerTree != this){
            throw new IllegalArgumentException("Parent does not belong to this tree");
        }else {
           node.value = value;
        }
    }
	
	public Node<E> setChild(Node<E> parent, Node<E> child, boolean left){
        if(parent == null){
            throw new NullPointerException("Cannot set node to null parent");
        }else if(parent.containerTree != this){
            throw new IllegalArgumentException("Parent does not belong to this tree");
        }else {
            if(left){
                parent.left = child;
            }else{
                parent.right = child;
            }
            if(child!=null) {
                child.parent = parent;
            }
            return child;
        }
    }
	
	protected void rotate(Node<E> node, boolean left){
        if(node == null){
            throw new IllegalArgumentException("Cannot rotate null node");
        }else if(node.containerTree != this){
            throw  new IllegalArgumentException("Node does not belong to the current tree");
        }
        Node<E> child = null;
        Node<E> grandchild = null;
        Node<E> parent = node.getParent();
        boolean parentDirection;
        if(left){
            child = node.getRight();
            if(child!=null){
                grandchild = child.getLeft();
            }

        }else{
            child = node.getLeft();
            if(child!=null){
                grandchild = child.getRight();
            }
        }

        if(node != getRoot()){
            if(parent.getLeft()==node){
                parentDirection = true;
            }else{
                parentDirection = false;
            }
            if(grandchild!=null)
                deleteNodeWithSubtree(grandchild);
            if(child!=null)
                deleteNodeWithSubtree(child);
            deleteNodeWithSubtree(node);
            if(child!=null) {
                setChild(parent, child, parentDirection);
                setChild(child, node, left);
            }
            if(grandchild!=null)
                setChild(node, grandchild, !left);
        }else{
            if(grandchild!=null)
                deleteNodeWithSubtree(grandchild);
            if(child!=null)
                deleteNodeWithSubtree(child);
            deleteNodeWithSubtree(node);
            if(child!=null) {
                root = child;
                setChild(child, node, left);
            }
            if(grandchild!=null)
                setChild(node, grandchild, !left);
            root.parent = null;
        }
    }
	
	public static void main(String[] args) {
		BinaryTree<Integer> tree = new BinaryTree<>();
        tree.addRoot(1);
        Node<Integer> n1 = tree.getRoot();
        Node<Integer> n2 = tree.addChild(n1, 2, true);
        Node<Integer> n3 = tree.addChild(n1, 3, false);
        Node<Integer> n4 = tree.addChild(n2, 4, true);
        Node<Integer> n5 = tree.addChild(n2, 5, false);
        Node<Integer> n6 = tree.addChild(n3, 6, true);
        Node<Integer> n7 = tree.addChild(n3, 7, false);
        Node<Integer> n8 = tree.addChild(n4, 8, true);
        Node<Integer> n9 = tree.addChild(n4, 9, false);
        Node<Integer> n10 = tree.addChild(n5, 10, true);

        tree.traverseDepthFirst((x)->System.out.print(" "+x), tree.getRoot(), DepthFirstTraversalType.PREORDER);
        System.out.println();
        tree.traverseDepthFirst((x)->System.out.print(" "+x), tree.getRoot(), DepthFirstTraversalType.INORDER);
        System.out.println();
        tree.traverseDepthFirst((x)->System.out.print(" "+x), tree.getRoot(), DepthFirstTraversalType.POSTORDER);
        System.out.println();
        
        System.out.println();
        tree.traversePreOrderNonRecursive((x)->System.out.print(" "+x));
        System.out.println();
        tree.traverseInOrderNonRecursive((x)->System.out.print(" "+x));
        System.out.println();
        tree.traversePostOrderNonRecursive((x)->System.out.print(" "+x));
        System.out.println();
        System.out.println();
        System.out.println("==============================================");
        tree.displayText();
	}
}