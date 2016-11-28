import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class hashtagcounter {

    static Scanner s = new Scanner(System.in);
    static String str, ht;
    static int count;
    //hashtable to store the hashtag and its corresponding node
    static Hashtable<String,FibonacciHeapNode> h = new Hashtable<String,FibonacciHeapNode>();
    //linkedhashmap to keep track of the order in which elements are removed from the heap
    static LinkedHashMap<String, Integer> hm = new LinkedHashMap<>();
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
      
    //FibonacciHeap initialisation - constructor
    FibonacciHeap H = new FibonacciHeap(); 
    //FibonacciHeapNode initialisation - constructor
    FibonacciHeapNode n = new FibonacciHeapNode(); 
    
    File file = new File(args[0]);                  //gets the input file name from the user
    File fileo = new File("output_file.txt");       //specifies output file name
    FileOutputStream fos = new FileOutputStream(fileo);
    PrintStream ps = new PrintStream(fos);
    Scanner s = new Scanner(file);
    
    str = s.nextLine();                             //gets the first line from the input file
    do {
        /*  ht contains all the alphabetic characters from the input string and
        *   count contains all the numeric characters from the input string
        */
        ht = str.replaceAll("[^a-zA-Z]", ""); 
        count = Integer.parseInt(str.replaceAll("[^0-9]",""));
        
        if(str.charAt(0)=='#')                      //checking if the first character is a "#"
        {    
            if(h.containsKey(ht))                   //checking if the hashtable already contains the hashtag
            {
                //increases the frequency of the hashtag ht
                H.increaseKey(h.get(ht),count);
            }
            else
            {
                //inserts the node into the heap, returns the max node
                n=H.insert(ht,count); 
                //inserts the node into the hashtable
                h.put(ht,n); 
            }
        }
        else                                        //if the first element is not a "#"
        {
            for(int i=0;i<count;i++)
            {
                /*  Removes the max node from the heap
                *   Inserts that node into the hashmap
                */
                n=H.removeMax();
                hm.put(n.hashtag,n.getKey());
            }
            
            /*  Assigns the keys of the hashmap to the string v and then
            *   removes the '[' and ']' from the keySet
            */
            String v = hm.keySet().toString();
            v = v.substring(1, v.length()-1);
            
            System.setOut(ps);                      //to write the output to the file
            System.out.println(v);                  //this is the data which is written to the output file

            /*  Hashtags that are in the hashmap are stored in keyList
            *   and the corresponding frequency is stored in the valueList
            */
            List<String> keyList = new ArrayList<>(hm.keySet()); 
            List<Integer> valueList = new ArrayList<>(hm.values());
        
            for (int i = keyList.size() - 1 ; i >= 0 ; i--) 
            {
                //removed elements are reinserted into the heap
                n=H.insert(keyList.get(i),valueList.get(i));
                
                /*  nodes which were removed from the heap are removed from
                *   the hashtable, and then reinserted
                */
                h.remove(n.hashtag);
                h.put(n.hashtag, n);
            }

            hm.clear();                             //hashmap is cleared
        }
        str = s.nextLine();                         //next line is read from the input file
    } while(!str.equals("stop")&&s.hasNext());      //loop till the word "stop" is read
    }
}