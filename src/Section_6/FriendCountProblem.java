package Section_6;

import Section_3.AVLTree;
import Section_2.BinaryTree;
import Section_1.ArraySorter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


public class FriendCountProblem {
	private static final String USER_LIST_FILE = "E:\\Workspace\\Advanced Data Structures And Algorithms In Java 9\\src\\Section_6\\ulist.txt";
    private static final String EDGES_PATH = "E:\\Workspace\\Advanced Data Structures And Algorithms In Java 9\\src\\Section_6\\com-orkut.ungraph.txt";
    private static final String OUTPUT_FILE_PATH = "E:\\Workspace\\Advanced Data Structures And Algorithms In Java 9\\src\\Section_6\\output.txt";
    
    public static void main(String[] args) throws Exception{
    	long start = System.currentTimeMillis();
        FileReader userListReader = new FileReader(USER_LIST_FILE);
        int count = 0;
        
        while(true){
            int lineValue = userListReader.readIntFromText();
            if(lineValue==0){
                break;
            }
            count++;
        }
        
        Integer [] keys = new Integer[count];
        AtomicInteger [] values = new AtomicInteger[count];
        
        userListReader = new FileReader(USER_LIST_FILE);
        int index=0;
        
        while(true){
            int uid = userListReader.readIntFromText();
            if(uid==0){
                break;
            }
            keys[index] = uid;
            values[index] =  new AtomicInteger(0);
            index++;
        }
        
        ArraySorter.quicksort(keys,(a,b)->a-b);
        
        ProducerConsumerQueue<Integer> queue = new ProducerConsumerQueue<>(4092, 2, (v)->{
            int pos  = ArraySearcher.binarySearch(keys,v);
            if(pos<0){
                return;
            }
            values[pos].incrementAndGet();
        });
        
        FileReader edgeListFileReader = new FileReader(EDGES_PATH);
        while(true){
            int val = edgeListFileReader.readIntFromText();
            if(val == 0){
                break;
            }
            queue.produce(val);
        }
        
        queue.markCompleted();
        queue.joinThreads();
        
        PrintStream out = new PrintStream(OUTPUT_FILE_PATH);
        for(int i=0;i<count;i++){
            out.println(keys[i] +" : "+values[i].get());
        }
        out.flush();
        System.out.println("Time taken: "+(System.currentTimeMillis() - start));
    }
}
