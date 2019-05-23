package Section_5;

/**
 * Created by debasishc on 20/12/16.
 */
public class GraphVertex<V> implements Comparable<GraphVertex<V>>{
    int id;
    V value;

    public GraphVertex(int id, V value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphVertex<?> that = (GraphVertex<?>) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }


    @Override
    public int compareTo(GraphVertex<V> o) {
        return id - o.id;
    }
}
