package Section_3;

import Section_2.LinkedList;

public class HashTable<E> {
	protected LinkedList<E> [] buckets;
	protected double maximumLoadFactor;
	protected int totalValues;
	
	public HashTable(int initialSize, double maximumLoadFactor){
		buckets = new LinkedList[initialSize];
		this.maximumLoadFactor = maximumLoadFactor;
	}
	
	protected boolean insert(E value, int arrayLength,LinkedList<E>[] array) {
		int hashCode = value.hashCode();
		int arrayIndex = hashCode % arrayLength;
		LinkedList<E> bucket = array[arrayIndex];
		if(bucket == null){
			bucket = new LinkedList<>();
			array[arrayIndex] = bucket;
		}
		for(E element: bucket){
			if(element.equals(value)){
			return false;
			}
		}
		bucket.appendLast(value);
		totalValues++;
		return true;
	}
	
	protected void rehash(){
		double loadFactor = ((double)(totalValues))/buckets.length;
		if(loadFactor>maximumLoadFactor){
			LinkedList<E> [] newBuckets = new LinkedList[buckets.length*2];
			totalValues = 0;
			for(LinkedList<E> bucket:buckets){
				if(bucket!=null) {	
					for (E element : bucket) {
						insert(element, newBuckets.length,newBuckets);
					}
				}
			}
			this.buckets = newBuckets;
		}
	}
	
	public boolean insert(E value){
		int arrayLength = buckets.length;
		LinkedList<E>[] array = buckets;
		boolean inserted = insert(value, arrayLength, array);
		if(inserted)
			rehash();
		return inserted;
	}
	
	public E search(E value){
		int hash = value.hashCode();
		int index = hash % buckets.length;
		LinkedList<E> bucket = buckets[index];
		if(bucket==null){
			return null;
		}else{
			for(E element: bucket){
				if(element.equals(value)){
					return element;
				}
			}
			return null;
		}
	}
	
	public static void main(String [] args){
        HashTable<String> hashTable = new HashTable<>(2, 3);
        for (int i = 0; i < 200; i++) {
            hashTable.insert(String.valueOf((int)(Math.random()*200)));
        }

        for(LinkedList<String> bucket:hashTable.buckets){
            if(bucket!=null)
                for(String x:bucket){
                    System.out.print(x+",");
                }
            System.out.println();
        }

        for (int i = 0; i < 200; i++) {
            String searchedValue = String.valueOf((int)(Math.random()*200));
            System.out.println(searchedValue + ":" + hashTable.search(searchedValue));
        }
    }
}
