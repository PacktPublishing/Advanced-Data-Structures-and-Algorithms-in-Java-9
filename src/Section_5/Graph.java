package Section_5;

import Section_2.LinkedList;
import Section_2.QueueImplLinkedList;
import Section_2.StackImplLinkedList;
import Section_1.OneArgumentExpression;
import Section_3.BinarySearchTree;
import Section_3.RedBlackTree;
import Section_2.BinaryTree;
import Section_4.DoublyLinkedList;
import Section_4.LinkedHeap;

public interface Graph<V,E> {
	int addVertex();
    void removeVertex(int id);
    void addEdge(int source, int target);
    void removeEdge(int source, int target);
    boolean isAdjacent(int source, int target);
    DoublyLinkedList<Integer> getNeighbors(int source);
    void setVertexValue(int vertex, V value);
    V getVertexValue(int vertex);
    void setEdgeValue(int source, int target, E value);
    E getEdgeValue(int source, int target);
    boolean isUndirected();
    BinarySearchTree<Integer> getAllVertices();
    int maxVertexID(); 
}