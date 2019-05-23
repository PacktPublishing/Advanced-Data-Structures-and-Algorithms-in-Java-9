package Section_5;

import Section_4.DoublyLinkedList;
import Section_2.LinkedList;
import Section_3.BinarySearchTree;
import Section_3.RedBlackTree;
import Section_2.BinaryTree;

public class AdjacencyListGraphWithSparseVertex<V,E> implements Graph<V,E> {
	boolean undirected;
	
	public AdjacencyListGraphWithSparseVertex(boolean undirected) {
        this.undirected = undirected;
    }
	
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
		RedBlackTree<Edge> edges=new RedBlackTree<>();
		DoublyLinkedList<Integer> neighbors=new DoublyLinkedList<>();
		
		public Vertex(int id, V value) {
            super(id, value);
        }
	}
	
	Object[] vertices = new Object[0];
	
	@Override
    public int addVertex() {
        Object[] newVertices = new Object[vertices.length+1];
        System.arraycopy(vertices, 0, newVertices, 0, vertices.length);
        newVertices[vertices.length] = new Vertex(vertices.length, null);
        vertices=newVertices;
        return newVertices.length-1;
    }
	
	@Override
    public void removeVertex(int id) {
        Vertex sVertex = (Vertex) vertices[id];
        if(sVertex==null){
            throw new IllegalArgumentException("Vertex "+ id +" does not exist");
        }
        DoublyLinkedList<Integer> neighbors = getNeighbors(id);
        Edge dummyEdgeForId = new Edge(id);
        for(int t:neighbors){
            Edge e = ((Vertex)vertices[t]).edges.deleteValue(dummyEdgeForId).getValue();
            ((Vertex)vertices[t]).neighbors.removeNode(e.targetNode);
        }
        vertices[id] = null;
    }
	
	@Override
    public void addEdge(int source, int target) {
        Vertex sVertex = (Vertex) vertices[source];
        Edge sEdge = sVertex.edges.insertValue(new Edge(target)).getValue();
        sEdge.targetNode =
                (DoublyLinkedList.DoublyLinkedNode<Integer>) sVertex.neighbors.appendLast(sEdge.target);
        if(undirected){
            Vertex tVertex = (Vertex) vertices[target];
            Edge tEdge = tVertex.edges.insertValue(new Edge(source)).getValue();
            tEdge.targetNode =
                    (DoublyLinkedList.DoublyLinkedNode<Integer>) tVertex.neighbors.appendLast(tEdge.target);
        }
    }
	
	@Override
    public void removeEdge(int source, int target) {
        Vertex sVertex = (Vertex) vertices[source];
        Edge deletedEdge = sVertex.edges.deleteValue(new Edge(target)).getValue();
        sVertex.neighbors.removeNode(deletedEdge.targetNode);
        if(undirected){
            Vertex tVertex = (Vertex) vertices[target];
            deletedEdge = tVertex.edges.deleteValue(new Edge(source)).getValue();
            tVertex.neighbors.removeNode(deletedEdge.targetNode);
        }
    }
	
	@Override
    public boolean isAdjacent(int source, int target) {
        Vertex sVertex = (Vertex) vertices[source];
        return sVertex.edges.searchValue(new Edge(target))!=null;
    }
	
	@Override
    public DoublyLinkedList<Integer> getNeighbors(int source) {
        Vertex sVertex = (Vertex) vertices[source];
        return sVertex.neighbors;
    }
	
	@Override
    public void setVertexValue(int vertex, V value) {
        Vertex sVertex = (Vertex) vertices[vertex];
        if(sVertex==null){
            throw new IllegalArgumentException("Vertex "+ vertex + "does not exist");
        }else{
            sVertex.setValue(value);
        }
    }
	
	@Override
    public V getVertexValue(int vertex) {
        Vertex sVertex = (Vertex) vertices[vertex];
        if(sVertex==null){
            throw new IllegalArgumentException("Vertex "+ vertex + "does not exist");
        }else{
            return sVertex.getValue();
        }
    }
	
	@Override
    public void setEdgeValue(int source, int target, E value) {
        Vertex sVertex = (Vertex) vertices[source];
        Vertex tVertex = (Vertex) vertices[target];
        if(sVertex==null){
            throw new IllegalArgumentException("Vertex "+ source + "does not exist");
        }else if(tVertex==null){
            throw new IllegalArgumentException("Vertex "+ target + "does not exist");
        }else{
            BinaryTree.Node<Edge> node =
                    sVertex.edges.searchValue(new Edge(target));
            if(node==null){
                throw new IllegalArgumentException("Edge between "+ source + "and"
                        + target + "does not exist");
            }else{
                node.getValue().value = value;
            }
        }
    }
	
	@Override
    public E getEdgeValue(int source, int target) {
        Vertex sVertex = (Vertex) vertices[source];
        Vertex tVertex = (Vertex) vertices[target];
        if(sVertex==null){
            throw new IllegalArgumentException("Vertex "+ source + "does not exist");
        }else if(tVertex==null){
            throw new IllegalArgumentException("Vertex "+ target + "does not exist");
        }else{
            BinaryTree.Node<Edge> node =
                    sVertex.edges.searchValue(new Edge(target));
            if(node==null){
                throw new IllegalArgumentException("Edge between "+ source + "and"
                        + target + "does not exist");
            }else{
                return node.getValue().value;
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
        for(int i=0;i<vertices.length;i++){
            if(vertices[i]!=null){
                allVertices.insertValue(i);
            }
        }
        return allVertices;
    }
	
	@Override
    public int maxVertexID() {
        return vertices.length-1;
    }
	
	public static void main(String [] args){
        AdjacencyListGraphWithSparseVertex<Character, Integer> graph = 
        new AdjacencyListGraphWithSparseVertex<>(true);
        for(int i=0;i<10;i++){
            graph.addVertex();
        }
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(1,3);
        graph.addEdge(2,3);
        graph.addEdge(2,4);
        graph.addEdge(3,4);

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
