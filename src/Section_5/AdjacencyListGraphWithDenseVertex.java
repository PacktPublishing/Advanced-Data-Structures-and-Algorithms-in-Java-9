package Section_5;

import Section_4.DoublyLinkedList;
import Section_2.LinkedList;
import Section_3.BinarySearchTree;
import Section_3.RedBlackTree;
import Section_2.BinaryTree;

public class AdjacencyListGraphWithDenseVertex<V,E> implements Graph<V,E> {
	
	int nextId;
	boolean undirected;
	
	class Edge implements Comparable<Edge>{
        E value;
        int target;
        
        DoublyLinkedList.DoublyLinkedNode<Integer> targetNode;

        public Edge(int target) {
            this.target = target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return target == edge.target;
        }

        @Override
        public int hashCode() {
            return target;
        }

        @Override
        public int compareTo(Edge o) {
            return target - o.target;
        }
    }
	
	class Vertex extends GraphVertex<V>{
        RedBlackTree<Edge> edges = new RedBlackTree<Edge>();
        DoublyLinkedList<Integer> neighbors = new DoublyLinkedList<>();
       
        public Vertex(int id, V value) {
            super(id, value);
        }
    }
	
	public AdjacencyListGraphWithDenseVertex(boolean undirected) {
        this.undirected = undirected;
    }

    RedBlackTree<GraphVertex<V>> vertices = new RedBlackTree<>();
    
    @Override
    public int addVertex() {
        vertices.insertValue(new Vertex(nextId++, null));
        return nextId;
    }

    @Override
    public void removeVertex(int id) {
        vertices.deleteValue(new GraphVertex<V>(id, null));
        vertices.traverseDepthFirstNonRecursive((gv)->{
                    BinaryTree.Node<Edge> edgeNode =
                            ((Vertex) gv).edges.deleteValue(new Edge(id));
                if(edgeNode!=null){
                    Edge edge = edgeNode.getValue();
                    ((Vertex) gv).neighbors.removeNode(edge.targetNode);
                }
        },
                BinaryTree.DepthFirstTraversalType.INORDER);
    }
    
    @Override
    public void addEdge(int source, int target) {
        BinaryTree.Node<GraphVertex<V>> sNode =vertices.searchValue(new GraphVertex<V>(source, null));
        BinaryTree.Node<GraphVertex<V>> tNode =vertices.searchValue(new GraphVertex<V>(target, null));
        if(sNode == null){
            throw new IllegalArgumentException("Vertex ID "+source+" does not exist");
        }else if(tNode == null){
            throw new IllegalArgumentException("Vertex ID "+target+" does not exist");
        }else{
            Vertex sVertex = (Vertex) sNode.getValue();
            Vertex tVertex = (Vertex) tNode.getValue();
            Edge tEdge = new Edge(target);
            sVertex.edges.insertValue(tEdge);
            tEdge.targetNode =
            		(DoublyLinkedList.DoublyLinkedNode<Integer>) sVertex.neighbors
            							.appendLast(tVertex.getId());
            if(undirected) {
                Edge sEdge = new Edge(source);
                tVertex.edges.insertValue(sEdge);
                sEdge.targetNode =
                        (DoublyLinkedList.DoublyLinkedNode<Integer>) tVertex.neighbors
                        		.appendLast(sVertex.getId());
            }
        }
    }
    
    @Override
    public void removeEdge(int source, int target) {
        BinaryTree.Node<GraphVertex<V>> sNode =vertices.searchValue(new GraphVertex<V>(source, null));
        BinaryTree.Node<GraphVertex<V>> tNode =vertices.searchValue(new GraphVertex<V>(target, null));
        if(sNode == null){
            throw new IllegalArgumentException("Vertex ID "+source+" does not exist");
        }else if(tNode == null){
            throw new IllegalArgumentException("Vertex ID "+target+" does not exist");
        }else{
            Vertex sVertex = (Vertex) sNode.getValue();
            Edge deletedEdge = sVertex.edges.deleteValue(new Edge(target)).getValue();
            sVertex.neighbors.removeNode(deletedEdge.targetNode);
            if(undirected) {
                Vertex tVertex = (Vertex) tNode.getValue();
                deletedEdge = tVertex.edges.deleteValue(new Edge(source)).getValue();
                tVertex.neighbors.removeNode(deletedEdge.targetNode);
            }
        }
    }

    @Override
    public boolean isAdjacent(int source, int target) {
        BinaryTree.Node<GraphVertex<V>> sNode =vertices.searchValue(new GraphVertex<V>(source, null));
        BinaryTree.Node<GraphVertex<V>> tNode =vertices.searchValue(new GraphVertex<V>(target, null));
        if(sNode == null){
            throw new IllegalArgumentException("Vertex ID "+source+" does not exist");
        }else if(tNode == null){
            throw new IllegalArgumentException("Vertex ID "+target+" does not exist");
        }else{
            Vertex sVertex = (Vertex) sNode.getValue();
            return sVertex.edges.searchValue(new Edge(target)) != null;
        }
    }

    @Override
    public DoublyLinkedList<Integer> getNeighbors(int source) {
        BinaryTree.Node<GraphVertex<V>> sNode =vertices.searchValue(new GraphVertex<V>(source, null));
        if(sNode == null){
            throw new IllegalArgumentException("Vertex ID "+source+" does not exist");
        }else{
            Vertex sVertex = (Vertex) sNode.getValue();
            return  sVertex.neighbors;
        }
    }

    @Override
    public void setVertexValue(int vertex, V value) {
        BinaryTree.Node<GraphVertex<V>> sNode =
        		vertices.searchValue(
        				new GraphVertex<V>(vertex, null));
        if(sNode == null){
            throw new IllegalArgumentException("Vertex ID "+vertex+" does not exist");
        }else{
            Vertex sVertex = (Vertex) sNode.getValue();
            sVertex.setValue(value);
        }
    }

    @Override
    public V getVertexValue(int vertex) {
        BinaryTree.Node<GraphVertex<V>> sNode =
                vertices.searchValue(
                        new GraphVertex<V>(vertex, null));
        if(sNode == null){
            throw new IllegalArgumentException("Vertex ID "+vertex+" does not exist");
        }else{
            Vertex sVertex = (Vertex) sNode.getValue();
            return sVertex.getValue();
        }
    }

    @Override
    public void setEdgeValue(int source, int target, E value) {
        BinaryTree.Node<GraphVertex<V>> sNode =vertices.searchValue(new GraphVertex<V>(source, null));
        BinaryTree.Node<GraphVertex<V>> tNode =vertices.searchValue(new GraphVertex<V>(target, null));
        if(sNode == null){
            throw new IllegalArgumentException("Vertex ID "+source+" does not exist");
        }else if(tNode == null){
            throw new IllegalArgumentException("Vertex ID "+target+" does not exist");
        }else{
            Vertex sVertex = (Vertex) sNode.getValue();
            BinaryTree.Node<Edge> edgeNode =sVertex.edges.searchValue(new Edge(target));
            if(edgeNode!=null) {
                edgeNode.getValue().value = value;
                if (undirected) {
                    Vertex tVertex = (Vertex) tNode.getValue();
                    edgeNode = tVertex.edges.searchValue(new Edge(source));
                    edgeNode.getValue().value = value;
                }
            }else{
                throw new IllegalArgumentException("No edge exists between the vertices "+ source + " and " + target);
            }
        }
    }

    @Override
    public E getEdgeValue(int source, int target) {
        BinaryTree.Node<GraphVertex<V>> sNode =vertices.searchValue(new GraphVertex<V>(source, null));
        BinaryTree.Node<GraphVertex<V>> tNode =vertices.searchValue(new GraphVertex<V>(target, null));
        if(sNode == null){
            throw new IllegalArgumentException("Vertex ID "+source+" does not exist");
        }else if(tNode == null){
            throw new IllegalArgumentException("Vertex ID "+target+" does not exist");
        }else{
            Vertex sVertex = (Vertex) sNode.getValue();
            BinaryTree.Node<Edge> edgeNode =
            		sVertex.edges.searchValue(new Edge(target));
            if(edgeNode!=null) {
                return edgeNode.getValue().value;
            }else{
                throw new IllegalArgumentException("No edge exists between the vertices "+ source + " and " + target);
            }
        }
    }

    @Override
    public boolean isUndirected() {
        return undirected;
    }

    @Override
    public BinarySearchTree<Integer> getAllVertices() {
        BinarySearchTree<Integer> allVertices = new RedBlackTree<>();
        vertices.traverseDepthFirstNonRecursive((v) -> allVertices.insertValue(v.getId()),
                BinaryTree.DepthFirstTraversalType.PREORDER);
        return allVertices;
    }

    @Override
    public int maxVertexID() {
        return nextId -1;
    }

    public static void main(String [] args){
        AdjacencyListGraphWithDenseVertex<Character, Integer> graph = 
        		new AdjacencyListGraphWithDenseVertex<>(true);
        for(int i=0;i<10;i++){
            graph.addVertex();
        }
        graph.addEdge(0,1); graph.setEdgeValue(0,1, 1);
        graph.addEdge(1,2); graph.setEdgeValue(1,2, 2);
        graph.addEdge(1,3); graph.setEdgeValue(1,3, 0);
        graph.addEdge(2,3); graph.setEdgeValue(2,3, 3);
        graph.addEdge(2,4); graph.setEdgeValue(2,4, 1);
        graph.addEdge(3,4); graph.setEdgeValue(3,4, 2);

        System.out.println(graph.isAdjacent(2,1));
        System.out.println(graph.isAdjacent(2,5));
        graph.getNeighbors(1).forEach(System.out::println);

        graph.removeVertex(1);
        System.out.println(graph.isAdjacent(3,2));
        System.out.println(graph.isAdjacent(2,5));
        graph.getNeighbors(2).forEach(System.out::println);

        graph.removeVertex(3);
        graph.getNeighbors(2).forEach(System.out::println);        
    }
}