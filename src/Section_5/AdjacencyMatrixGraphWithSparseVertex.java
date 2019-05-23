package Section_5;

import Section_2.LinkedList;
import Section_3.BinarySearchTree;
import Section_3.RedBlackTree;
import Section_4.DoublyLinkedList;

public class AdjacencyMatrixGraphWithSparseVertex<V,E> implements Graph<V,E> {
	private static class NullEdgeValue{};
	
	private NullEdgeValue nullEdge = new NullEdgeValue();
    private NullEdgeValue nullVertex = new NullEdgeValue();
    
    Object [][] adjacencyMatrix = new Object[0][];
    Object[] vertexValues = new Object[0];
    boolean undirected;
    
    public AdjacencyMatrixGraphWithSparseVertex(boolean undirected){
        this.undirected = undirected;
    }
    
    @Override
    public int addVertex() {
        int numVertices = adjacencyMatrix.length;
        Object [][] newAdjacencyMatrix = new Object[numVertices+1][];
        for(int i=0;i<numVertices;i++){
            newAdjacencyMatrix[i] = new Object[numVertices+1];
            System.arraycopy(adjacencyMatrix[i],0, newAdjacencyMatrix[i], 0, numVertices);
        }
        newAdjacencyMatrix[numVertices] =
                new Object[numVertices+1];
        adjacencyMatrix = newAdjacencyMatrix;
        Object [] vertexValuesNew = new Object[vertexValues.length+1];
        System.arraycopy(vertexValues,0, vertexValuesNew, 0, vertexValues.length);
        vertexValuesNew[vertexValues.length] = nullVertex;
        vertexValues = vertexValuesNew;
        return numVertices;
    }
    
    @Override
    public void removeVertex(int id) {
        vertexValues[id] = null;
        for(int i=0;i<adjacencyMatrix.length;i++){
            adjacencyMatrix[id][i] = null;
            adjacencyMatrix[i][id] = null;
        }
    }
    
    @Override
    public void addEdge(int source, int target) {
        if(adjacencyMatrix[source][target] == null){
            adjacencyMatrix[source][target] = nullEdge;
            if(undirected){
                adjacencyMatrix[target][source] = nullEdge;
            }
        }else{
            throw new IllegalArgumentException("Edge already exists");
        }
    }
    
    @Override
    public void removeEdge(int source, int target) {
        adjacencyMatrix[source][target] = null;
        if(undirected){
            adjacencyMatrix[target][source] = null;
        }
    }
    
    @Override
    public boolean isAdjacent(int source, int target) {
        return adjacencyMatrix[source][target] != null;
    }
    
    @Override
    public DoublyLinkedList<Integer> getNeighbors(int source) {
        DoublyLinkedList<Integer> neighborList = new DoublyLinkedList<>();
        for(int i=0;i<adjacencyMatrix.length;i++){
            if(adjacencyMatrix[source][i]!=null){
                neighborList.appendLast(i);
            }
        }
        return neighborList;
    }
    
    @Override
    public void setVertexValue(int vertex, V value) {
        vertexValues[vertex] = value;
    }
    
    @Override
    public V getVertexValue(int vertex) {
        if(vertexValues[vertex]!=nullVertex)
            return (V)vertexValues[vertex];
        else
            throw new IllegalArgumentException("Vertex "+vertex+" does not exist");
    }
    
    @Override
    public void setEdgeValue(int source, int target, E value) {
        adjacencyMatrix[source][target] = value;
        if(undirected){
            adjacencyMatrix[target][source] = value;
        }
    }
    
    @Override
    public E getEdgeValue(int source, int target) {
        if(adjacencyMatrix[source][target] != nullEdge) {
            return (E) adjacencyMatrix[source][target];
        }else {
            return null;
        }
    }
    
    @Override
    public boolean isUndirected() {
        return undirected;
    }
    
    @Override
    public BinarySearchTree<Integer> getAllVertices() {
        BinarySearchTree<Integer> allVertices = new RedBlackTree<>();
        for(int i=0;i<vertexValues.length;i++){
            if(vertexValues[i]!=null){
                allVertices.insertValue(i);
            }
        }
        return allVertices;
    }
    
    @Override
    public int maxVertexID() {
        return vertexValues.length-1;
    }
    
    public static void main(String[] args) {
    	AdjacencyMatrixGraphWithSparseVertex<Character, Integer>graph = new AdjacencyMatrixGraphWithSparseVertex<>(true);
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
