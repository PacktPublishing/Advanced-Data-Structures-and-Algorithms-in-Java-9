package Section_5;

import Section_2.LinkedList;
import Section_3.BinarySearchTree;
import Section_3.RedBlackTree;
import Section_4.DoublyLinkedList;
import Section_2.BinaryTree;

public class AdjacencyMatrixGraphWithDenseVertex<V,E> implements Graph<V,E> {
	
	class Vertex extends GraphVertex<V>{
		int internalIndex;
		
		public Vertex(int id, V value, int internalIndex) {
            super(id, value);
            this.internalIndex = internalIndex;
        }

		public int getInternalIndex() {
			return internalIndex;
		}

		public void setInternalIndex(int internalIndex) {
			this.internalIndex = internalIndex;
		}		
	}
	
	private static class NullValue{};
	
	private int nextId;
	
	private NullValue nullEdge = new NullValue();
    Object [][] adjacencyMatrix = new Object[0][];
    RedBlackTree<GraphVertex<V>> vertices = new RedBlackTree<>();
    boolean undirected;
    
    public AdjacencyMatrixGraphWithDenseVertex(boolean undirected){
        this.undirected = undirected;
    }
    
    @Override
    public int addVertex() {
        int id = nextId++;
        int numVertices = adjacencyMatrix.length;
        Object [][] newAdjacencyMatrix = new Object[numVertices+1][];
        for(int i=0;i<numVertices;i++){
            newAdjacencyMatrix[i] = new Object[numVertices+1];
            System.arraycopy(adjacencyMatrix[i],0, newAdjacencyMatrix[i], 0, numVertices);
        }
        newAdjacencyMatrix[numVertices] =
                new Object[numVertices+1];

        vertices.insertValue(new Vertex(id, null, adjacencyMatrix.length));
        adjacencyMatrix = newAdjacencyMatrix;
        return numVertices;
    }
    
    @Override
    public void removeVertex(int id) {
        BinaryTree.Node<GraphVertex<V>> node =vertices.searchValue(new GraphVertex<V>(id, null));
        if(node!=null){
            int internalId = ((Vertex)(node.getValue())).getInternalIndex();
            int numVertices = adjacencyMatrix.length;
            Object [][] newAdjacencyMatrix = new Object[numVertices-1][];
            for(int i=0;i<internalId;i++){
                newAdjacencyMatrix[i] = new Object[numVertices-1];
                System.arraycopy(adjacencyMatrix[i],0, newAdjacencyMatrix[i], 0, internalId);
                System.arraycopy(adjacencyMatrix[i],internalId+1, newAdjacencyMatrix[i], internalId, numVertices-internalId-1);
            }
            for(int i=internalId+1;i<numVertices;i++){
                newAdjacencyMatrix[i-1] = new Object[numVertices-1];
                System.arraycopy(adjacencyMatrix[i],0, newAdjacencyMatrix[i-1], 0, internalId);
                System.arraycopy(adjacencyMatrix[i],internalId+1, newAdjacencyMatrix[i-1], internalId, numVertices-internalId-1);
            }
            adjacencyMatrix = newAdjacencyMatrix;
            vertices.traverseDepthFirstNonRecursive((gv)->{
                if(((Vertex)gv).getInternalIndex()>internalId)
                    ((Vertex)gv).setInternalIndex(((Vertex)gv).getInternalIndex()-1);
            }, BinaryTree.DepthFirstTraversalType.PREORDER);
            vertices.deleteValue(new GraphVertex<>(id, null));
        }else{
            throw new IllegalArgumentException("Vertex with id "+id+" does not exist");
        }
    }
    
    @Override
    public void addEdge(int source, int target) {
        BinaryTree.Node<GraphVertex<V>> sNode = vertices.searchValue(
                new GraphVertex<V>(source, null));
        BinaryTree.Node<GraphVertex<V>> tNode = vertices.searchValue(
                new GraphVertex<V>(target, null));
        if(sNode!=null && tNode!=null) {
            int s = ((Vertex)(sNode.getValue())).getInternalIndex();
            int t = ((Vertex)(tNode.getValue())).getInternalIndex();
            if(adjacencyMatrix[s][t] == null){
                adjacencyMatrix[s][t] = nullEdge;
                if(undirected){
                    adjacencyMatrix[t][s] = nullEdge;
                }
            }else{
                throw new IllegalArgumentException("Edge already exists");
            }
        }else{
            throw new IllegalArgumentException("Non-existent ID");
        }
    }
    
    @Override
    public void removeEdge(int source, int target) {
        BinaryTree.Node<GraphVertex<V>> sNode = vertices.searchValue(
                new GraphVertex<V>(source, null));
        BinaryTree.Node<GraphVertex<V>> tNode = vertices.searchValue(
                new GraphVertex<V>(target, null));
        if(sNode!=null && tNode!=null) {
            int s = ((Vertex)(sNode.getValue())).getInternalIndex();
            int t = ((Vertex)(tNode.getValue())).getInternalIndex();
            adjacencyMatrix[s][t] = null;
        }else{
            throw new IllegalArgumentException("Non-existent ID");
        }
    }
    
    @Override
    public boolean isAdjacent(int source, int target) {
        BinaryTree.Node<GraphVertex<V>> sNode = vertices.searchValue(
                new GraphVertex<V>(source, null));
        BinaryTree.Node<GraphVertex<V>> tNode = vertices.searchValue(
                new GraphVertex<V>(target, null));
        if(sNode!=null && tNode!=null) {
            int s = ((Vertex)(sNode.getValue())).getInternalIndex();
            int t = ((Vertex)(tNode.getValue())).getInternalIndex();
            return adjacencyMatrix[s][t] != null;
        }else{
            throw new IllegalArgumentException("Non-existent ID");
        }
    }
    
    @Override
    public DoublyLinkedList<Integer> getNeighbors(int source) {
        BinaryTree.Node<GraphVertex<V>> node =
                vertices.searchValue(
                        new GraphVertex<V>(source, null));
        if(node!=null){
            DoublyLinkedList<Integer> neighborsList = new DoublyLinkedList<>();
            int sourceInternalIndex = ((Vertex) node.getValue()).getInternalIndex();
            vertices.traverseDepthFirstNonRecursive((gv)->{
                int targetInternalIndex = ((Vertex) gv).getInternalIndex();
                if(adjacencyMatrix[sourceInternalIndex][targetInternalIndex]!=null)
                    neighborsList.appendLast(gv.getId());
            }, BinaryTree.DepthFirstTraversalType.INORDER);
            return neighborsList;
        }else{
            throw new IllegalArgumentException("Vertex with id "+source+" does not exist");
        }
    }
    
    @Override
    public void setVertexValue(int vertex, V value) {
        BinaryTree.Node<GraphVertex<V>> node =
                vertices.searchValue(
                        new GraphVertex<V>(vertex, null));
        if(node!=null){
            node.getValue().setValue(value);
        }else{
            throw new IllegalArgumentException("Vertex with id "+vertex+" does not exist");
        }
    }

    @Override
    public V getVertexValue(int vertex) {
        BinaryTree.Node<GraphVertex<V>> node =
                vertices.searchValue(
                        new GraphVertex<V>(vertex, null));
        if(node!=null){
            return node.getValue().getValue();
        }else{
            throw new IllegalArgumentException("Vertex with id "+vertex+" does not exist");
        }
    }
    
    @Override
    public void setEdgeValue(int source, int target, E value) {
        BinaryTree.Node<GraphVertex<V>> sNode = vertices.searchValue(
                new GraphVertex<V>(source, null));
        BinaryTree.Node<GraphVertex<V>> tNode = vertices.searchValue(
                new GraphVertex<V>(target, null));
        if(sNode!=null && tNode!=null) {
            int s = ((Vertex)(sNode.getValue())).getInternalIndex();
            int t = ((Vertex)(tNode.getValue())).getInternalIndex();
            adjacencyMatrix[s][t] = value;
            if (undirected) {
                adjacencyMatrix[t][s] = value;
            }
        }else{
            throw new IllegalArgumentException("Non-existent ID");
        }
    }
    
    @Override
    public E getEdgeValue(int source, int target) {
        BinaryTree.Node<GraphVertex<V>> sNode = vertices.searchValue(
                new GraphVertex<V>(source, null));
        BinaryTree.Node<GraphVertex<V>> tNode = vertices.searchValue(
                new GraphVertex<V>(target, null));
        if(sNode!=null && tNode!=null) {
            int s = ((Vertex)(sNode.getValue())).getInternalIndex();
            int t = ((Vertex)(tNode.getValue())).getInternalIndex();
            return (E) adjacencyMatrix[s][t];
        }else{
            throw new IllegalArgumentException("Non-existent ID");
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
        return nextId-1;
    }
    
    public static void main(String[] args) {
    	AdjacencyMatrixGraphWithDenseVertex<Character, Integer> graph = 
    	        new AdjacencyMatrixGraphWithDenseVertex<>(true);
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
