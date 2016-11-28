import java.util.ArrayList;
import java.util.List;

public class FibonacciHeap
{
    FibonacciHeapNode max;			//Keeps track of the node with the greatest key value in the heap
    int size;						//holds the size of the fibonacci heap
 
    //Default constructor
    public FibonacciHeap()
    {
        max = null;
        size = 0;
    }

    /*  Inserts a node with hashtag value as s and key value as key into the
    *   Fibonacci heap. It then calls the merge function which merges the newly
    *   inserted node with the heap. It returns the node with the maximum key value
    */
    public FibonacciHeapNode insert(String s,Integer key)
    {
        FibonacciHeapNode node = new FibonacciHeapNode(s,key);
        max = merge(max, node);
        size++;
        return node;
    }
    
    /*  This function increases the frequency of a node that already exists in the heap.
    *   It calls the cut function which cuts the node from its parent if its
    *   key value is greater than that of its parent. Cascading cut function
    *   is also called inside this function.    
    */
    public void increaseKey(FibonacciHeapNode node, Integer newKey)
    {
        FibonacciHeapNode p = node.parent;
        node.key = node.key + newKey;
        if (p != null && node.compareTo(p) > 0)
        {
            cut(node, p);
            cascadingCut(p);
        }
        if (node.compareTo(max) > 0)	
        {
        	//If the newly inserted node's key is greater than that of the max node
            max = node;
        }
    }
    
    /*  This function cuts the node from its parent when the node’s key value
    *   exceeds that of its parent. It also decreases the parent’s degree and
    *   calls the merge function.
    */
    public void cut(FibonacciHeapNode n, FibonacciHeapNode p)
    {
        if (p.child == n)
        {
            p.child = n.right;
        }
        
        remove(n);					//node n is removed from the heap
        p.degree--;					//its parent's degree is decremented
        
        if (p.degree == 0)
        {
        	/*	Parent's child pointer is set to
        	*	null if it does not have any other child
        	*/
            p.child = null;			
        }
        
        n.parent = null;
        merge(max, n);
        n.childCut = false;
    }

    /*	Removes the node from the Fibonacci heap by linking
    *	the node's previous and next nodes to each other
    */
    public void remove(FibonacciHeapNode n)
    {
        n.left.right = n.right;
        n.right.left = n.left;
        n.right = n;
        n.left = n;
    }

    //CascadingCut cuts the node from the Fibonacci heap when its childCut is true.
    public void cascadingCut(FibonacciHeapNode n)
    {
        FibonacciHeapNode p = n.parent;
        if (p != null)
        {
            if (n.childCut)			//when childCut is true		
            {
                cut(n, p);
                cascadingCut(p);
            }
            else
            {
                n.childCut = true;
            }
        }
    }

    //Merge adds the node to the root list of the existing Fibonacci heap.
    public FibonacciHeapNode merge(FibonacciHeapNode n1, FibonacciHeapNode n2)
    {
        if (n1 == null && n2 == null)
        {
            return null;
        }
        if (n1 == null)
        {
            return n2;
        }
        if (n2 == null)
        {
            return n1;
        }
        
        //Inserting the new node between the node n1 and its next node
        FibonacciHeapNode t = n1.right;
        n1.right = n2.right;
        n1.right.left = n1;
        n2.right = t;
        n2.right.left = n2;
        
        //Returning the node which has a greater key value
        if(n1.key>n2.key)
            return n1;
        else return n2;
    }
 
    /*  removeMax removes the root node of the Fibonacci max heap. It then calls the
    *   pairwise merge function to merge all the root nodes in the heap.
    */
    public FibonacciHeapNode removeMax()
    {
        FibonacciHeapNode m = max;
        FibonacciHeapNode next;
        if (m != null)
        {    
            if (m.child != null)
            {
                FibonacciHeapNode c = m.child;
                do {
                	//setting all the parent pointers of m's children to null
                    c.parent = null;
                    c = c.right;
                } while (c != m.child);
            }
            if(max.right == max)		//when max is the only root node
                next = null; 
            else
                next = max.right;

            remove(m);
            size--;						//size of the heap is reduced
            max = merge(next, m.child);	//merging the other root nodes with the children of m
            
            if(next == null)			
            {
                next = max.right;
                while(next != max)		//max is the only root node in the heap
                {
                    if(next.key > max.key)
                        max = max.right;
                    next = next.right;
                }
            }
                
            if (next != null)
            {
                next = max.right;
                while(next != max)
                {
                    if(next.key > max.key)
                        max = max.right;
                    next = next.right;
                }
                pairwiseMerge();
            }
        }
        return m;
    }
    
    /*  pairwiseMerge combines the root elements of the heaps pairwise based
    *   on their degree in ascending order. It makes use of a list to keep
    *   track of the degree of the roots. When two roots have the same degree
    *   it merges them. Merging is done in the left to right order.
    */
    public void pairwiseMerge()
    {
    	/*	Array to hold the degrees of the nodes.
    	*	When two nodes have the same degree, they are merged
    	*/
        List<FibonacciHeapNode> arr = new ArrayList<FibonacciHeapNode>();
        
        for (int i = 0; i < 45; i++)
        {
            arr.add(null);
        }
 
        int num = 0; 
        FibonacciHeapNode n = max;

        //counting the number of root nodes in the heap
        if (n != null)
        {
            num++;
            n = n.left;
            while (n != max)
            {
                num++;
                n = n.left;
            }
        }

        while (num > 0)
        {
            int d = n.degree;
            FibonacciHeapNode next = n.right;

            /*	infinite loop. Breaks only when no other
            *	node with the same degree can be found
            */
            for (;;) 
            {
            	//Assigns node with degree d to m
                FibonacciHeapNode m = arr.get(d);
                if (m == null)
                {
                    break;
                }
                /*	if two nodes of the same degree exist,
                *	their key values are compared and they are
                *	linked together based on their key value
                */
                if (n.key < m.key)
                {
                    FibonacciHeapNode t = m;
                    m = n;
                    n = t;
                }

                linkHeaps(m, n);

                /*	For that particular degree, now, there are no nodes.
                *	Hence, it is set to null
                */
                arr.set(d, null);
                d++;
            }

            arr.set(d, n);
            n = next;
            num--;						//number of root nodes is decreased by 1
        }

        //Reconstructing the root list
        max = null;

        for (int i = 0; i < 45; i++)
        {
            FibonacciHeapNode m = arr.get(i);
            if (m == null)
            {
                continue;
            }
            
            //adding the node to the root list
            if (max != null)
            {
                m.left.right = m.right;
                m.right.left = m.left;

                m.left = max;
                m.right = max.right;
                max.right = m;
                m.right.left = m;

                //setting the new max
                if (m.key > max.key)
                {
                    max = m;
                }
            } 
            else
            {
                max = m;
            }
        }
    }
     
    //LinkHeaps merges two nodes which are of the same degree
    public void linkHeaps(FibonacciHeapNode a, FibonacciHeapNode b)
    {
    	//removing a from the root list
        a.left.right = a.right;
        a.right.left = a.left;

        //making b the parent of a
        a.parent = b;
        
        //if b does not have any child previously
        if (b.child == null)
        {
        	//making a the child of b
            b.child = a;
            a.right = a;
            a.left = a;
        }
        else 							//b already has a child/children
        {
        	//adding a to the list of b's children
            a.left = b.child;
            a.right = b.child.right;
            b.child.right = a;
            a.right.left = a;
        }

        b.degree++;						//increasing b's degree since a is b's child
        a.childCut = false;
    }
}