package Section_1;

import java.util.Arrays;
import java.util.Comparator;

public class ArraySorter {					
		
	private static <E> void merge(E[] arrayL, E[] arrayR,int start, int mid, int end,E[] targetArray,Comparator<E> comparator) {
		int i = start;
		int j = mid;
		int k = start;
		while (k < end) {
			if (i == mid) {
				targetArray[k] = arrayR[j];
				j++;
			} else if (j == end) {
				targetArray[k] = arrayL[i];
						i++;
			} else if (comparator.compare(arrayL[i], arrayR[j]) > 0) {
				targetArray[k] = arrayR[j];
				j++;
			} else {
				targetArray[k] = arrayL[i];
				i++;
			}
			k++;
		}
	}	
	
	private static <E> void merge(E[] array,int start, int mid, int end,E[] targetArray,Comparator<E> comparator) {
        merge(array, array, start, mid, end,targetArray, comparator);
    }
	
	public static <E> E[] mergeSortNoCopy(E[] sourceArray, int start,int end,E[] tempArray,Comparator<E> comparator) {
		if (start >= end - 1) {
				return sourceArray;
		}
		int mid = (start + end) / 2;
		E[] sortedPart1 =mergeSortNoCopy(sourceArray, start, mid, tempArray,comparator);
		E[] sortedPart2 =mergeSortNoCopy(sourceArray, mid, end, tempArray,comparator);

		if (sortedPart2 == sortedPart1) {
			if (sortedPart1 == sourceArray) {
					merge(sortedPart1, sortedPart2, start, mid, end,tempArray, comparator);
					return tempArray;
				} else {
					merge(sortedPart1, sortedPart2, start, mid, end,sourceArray, comparator);
					return sourceArray;
				}
		} else {            
			merge(sortedPart1, sortedPart2, start, mid, end,sortedPart2, comparator);
			return sortedPart2;
		}
	}
	
	public static <E> void mergeSort(E[] sourceArray, int start,int end,E[] tempArray,Comparator<E> comparator) {
		if (start >= end - 1) {
			return;
		}
		int mid = (start + end) / 2;
		mergeSort(sourceArray, start, mid, tempArray, comparator);
		mergeSort(sourceArray, mid, end, tempArray, comparator);

		merge(sourceArray, start, mid, end, tempArray, comparator);
		System.arraycopy(tempArray, start, sourceArray, start,end - start);
	}
	
	public static <E> void quicksort(E[] array,int start,int end,Comparator<E> comparator) {
		
		if (end - start <= 0) {
            return;
        }
		int pivotIndex=(int)((end-start)*Math.random())+start;
		swap(array,pivotIndex,end-1);
		int i = start;
        int j = end - 1;
        boolean movingI = true;
        
        while (i < j) {
            if (comparator.compare(array[i], array[j]) > 0) {
                swap(array, i, j);
                movingI = !movingI;
            } 
            else {
                if (movingI) {
                    i++;
                } 
                else {
                    j--;
                }
            }
        } 
        quicksort(array, start, i, comparator);
        quicksort(array, i + 1, end, comparator);
	}
	
	public static <E> void quicksort(E[] array,Comparator<E> comparator) {
		quicksort(array, 0, array.length, comparator);
	}
	
	public static <E> void swap(E[] array, int i, int j) {
        if (i == j)
            return;
        E temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
	
	public static void main(String[] args) {
		Integer[] array =new Integer[]{10, 5, 2, 3, 78, 53, 3, 1, 1, 24, 1, 35,35, 2, 67, 4, 33, 30};
		quicksort(array, (a, b) -> a - b);
		System.out.println(Arrays.toString(array));
		
		Integer[] anotherArray=new Integer[array.length];
		array = mergeSortNoCopy(array, 0, array.length, anotherArray,(a, b) -> a - b);
		System.out.println(Arrays.toString(array));
		
		mergeSort(array, 0, array.length, anotherArray,(a, b) -> a - b);
		System.out.println(Arrays.toString(array));
	}
}
