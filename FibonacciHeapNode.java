class FibonacciHeapNode
{
    FibonacciHeapNode child, left, right, parent;    
    int key;
    String hashtag;
    int degree;
    boolean childCut;
 
    //Default constructor that creates an instance of the Node class
    public FibonacciHeapNode()
    {
        this.right = this;
        this.left = this;
        this.child = null;
        this.parent = null;
        this.childCut = false;
        hashtag = null;
    }   
    
    /*  Parameterized constructor that creates an instance of the Node class and
    *   initialises the hashtag field to s and key to c
    */
    public FibonacciHeapNode(String s,int c)
    {
        this.right = this;
        this.left = this;
        this.child = null;
        this.parent = null;
        this.key = c;
        this.hashtag = s;
    }   
    
    //Returns the key value of the node calling the function
    public Integer getKey()
    {
            return key;
    }
    
    /*  Compares the key value of node calling the function and the node being
    *   passed as the parameter.    
    */
    public int compareTo(FibonacciHeapNode other)
    {
        if(this.key > other.key)
            return 1;
        else if(this.key < other.key)
            return -1;
        else
            return 0;
    }
}