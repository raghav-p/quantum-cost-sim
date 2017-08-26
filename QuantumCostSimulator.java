
package quantum;
import java.util.ArrayList;

public class QuantumCostSimulator
{
    static ArrayList<Node> allStates = new ArrayList(); // ArrayList for all states
    static ArrayList<Node> firstStates = new ArrayList(); // ArrayList for first states
    static ArrayList<Node> inputNodes = new ArrayList();// ArrayList for all inputs
    static ArrayList<String> allStatesStrings = new ArrayList(); // ArrayList for all state strings
    static ArrayList<String> inputNodesStrings = new ArrayList(); // ArrayList for all input strings
    static ArrayList<String> pathList = new ArrayList();// ArrayList for all paths
    static int toffoli = 0; // toffoli counter
    static int feynman = 0; // feynman counter
    static Node start = new Node("Start"); // Start Node
    static Node end = new Node("End"); // End Node
    //static Node end1 = new Node("End1"); // Only for Flowchart 8, 9, 10, Sample, Sample 1, Sample 3
    //static Node end2 = new Node("End2"); // Only for Flowchart 9, 10
    //static Node end3 = new Node("End3"); // Only for Flowchart 9
    
    // Defining Node class
    public static class Node
    {
       private final String nodeTitle;
       Node prevt;
       Node prevf;
       Node t;
       Node f;
       int state;
       Node(String title)
       {
           nodeTitle = title;
       }
       public String getTitle()
       {
           return nodeTitle;
       }
       @Override
       public String toString()
       {
           return nodeTitle;
       }
    }
    
    // Iniializing start and end nodes (both are state nodes)
    public static void initialize()
    {
       start.prevt = null;
       start.prevf = null;
       end.prevt = start;
       end.prevf = start;
       start.t = end;
       start.f = end;
       end.t = null;
       end.f = null;
       start.state = 1;
       end.state = 1;
       allStates.add(start);
       firstStates.add(start);
       allStates.add(end);
       firstStates.add(end);
       //allStates.add(end1); // For Flowchart 8, 9, 10, Sample, Sample 1, Sample 3
       //firstStates.add(end1); // For Flowchart 8, 9, 10, Sample, Sample 1, Sample 3
       //allStates.add(end2); // For Flowchart 9, 10
       //firstStates.add(end2); // For Flowchart 9, 10
       //allStates.add(end3); // For Flowchart 9, 10
       //firstStates.add(end3); // For Flowchart 9, 10
    }
    
    // Inserting input nodes into flowchart
    public static Node insertInput(Node prevt, Node prevf, Node t, Node f, String title)
    {
       Node a = new Node(title);
       a.prevt = prevt;
       a.prevf = prevf;
       a.t = t;
       a.f = f;
       if (a.prevt != null) {
           a.prevt.t = a;
       }
       if (a.t != null) {
           a.t.prevt = a;
       }
       if (a.prevf != null){
           a.prevf.f = a;
       }
        if (a.f != null) {
           a.f.prevf = a;
       }
       a.state = 0;
       inputNodes.add(a);
      
       return a; 
    }
    
    // Creating not input for input nodes
    public static Node createNotInput(Node prevf, Node f, String title)
    {
        Node a = new Node(title);
        a.prevf = prevf;
        a.f = f;
        if (a.prevf != null) {
            a.prevf.f = a;
        }
        if (a.f != null) {
            a.f.prevf = a;
        }
        a.state = 0;
        inputNodes.add(a);
        
        return a;
    }
    
    // Inserting intermediate states only between two input nodes
    public static Node insertState(Node prevt, Node prevf, Node t, Node f, String title)
    {
        Node x = new Node(title);
        //if (prevt.state != 1 && prevf.state != 1 && t.state != 1 && f.state != 1){
            x.prevt = prevt;
            x.prevf = prevf;
            if (x.prevt != null) {
            x.prevt.t = x;
            }
            if (x.prevf != null){
            x.prevf.f = x;
            }
            x.t = t;
            x.f = f;
            if (x.t != null) {
                x.t.prevt = x;
            }
            if (x.f != null) {
                x.f.prevf = x;
            }
        //}
        x.state = 1;
        allStates.add(x);
        return x;
    }
	
    // Returning number of paths in the flowchart
    public static int returnPathNumber()
    {
        int num = 0;
        for (Node a: allStates)
        {
            if (a.t == null && a.f == null)
            {
                continue;
            }
            if (allStates.contains(a.t) || inputNodes.contains(a.t))
            {
                if (allStates.contains(a.t))
                {
                    num++;
                    feynman++;
                }
                else if (allStates.contains(a.t.t))
                {
                    num++;
                    toffoli++;
                }
            }
            if (allStates.contains(a.f) || inputNodes.contains(a.f))
            {
                if (allStates.contains(a.f))
                {
                    if (a.f == a.t) 
                    {
                        continue;
                    }
                    num++;
                    feynman++;
                }
                else if (allStates.contains(a.f.f))
                {
                    num++;
                    toffoli++;
                }
            }
        }
        return num;
    }
    
    // Returning Quantum Cost
    public static int calcCost()
    {
        int cost = toffoli*5 + feynman;
        return cost;
    }
    
    // Creating ArrayList for strings of state nodes
    public static void addStatesStrings()
    {
        for (Node a: allStates)
        {
            allStatesStrings.add(a.toString());
        }
    }
    
    // Creating ArrayList for strings of input nodes
    public static void addInputNodesStrings()
    {
        for (Node a: inputNodes)
        {
            inputNodesStrings.add(a.toString());
        }
    }
    
    // Adding the start paths to ArrayList pathList
    public static void addPaths()
    {
        for (Node a: allStates)
        {
            if (a.t == null && a.f == null)
            {
                continue;
            }
            
            if (allStates.contains(a.t))
            {
                pathList.add(a.toString());
                pathList.add(a.t.toString());
            }
            else if (inputNodes.contains(a.t))
            {
                pathList.add(a.toString());
                pathList.add(a.t.toString());
                if (a.t.t != null) 
                {
                pathList.add(a.t.t.toString());
                }
            }
            
            if (allStates.contains(a.f))
            {
                if (a.f == a.t) 
                    {
                        continue;
                    }
                else
                {
                    pathList.add(a.toString());
                    pathList.add(a.f.toString());
                }
            }
            else if (inputNodes.contains(a.f))
            {
                pathList.add(a.toString());
                pathList.add(a.f.toString());
                if (a.f.f != null) 
                {
                pathList.add(a.f.f.toString());
                }
            }
        }
    }
    
    public static void main(String[] args) 
    {
        /*
        // Flowchart 1
        initialize(); // initializing start and end nodes
        Node a = insertInput(start, start, end, end, "a"); // input node a
        Node aNot = createNotInput(start, end, "aNot"); // input node aNot
        Node b = insertInput(null, a, end, start, "b"); // input node b
        Node bNot = createNotInput(a, start, "bNot"); // input node bNot
        Node b1 = insertInput(a, null, start, end, "b1"); // input node b1
        Node b1Not = createNotInput(null, end, "b1Not"); // input node b1Not
        Node n = insertState(null, aNot, b, bNot, "n"); // intermediate state n
        Node m = insertState(a, null, b1, b1Not, "m"); // intermediate state m
        System.out.println("Flowchart 1");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Flowchart 2
        initialize();
        Node a = insertInput(start, start, end, end, "a"); 
        Node aNot = createNotInput(start, end, "aNot");
        Node b = insertInput(a, null, end, end, "b"); 
        Node bNot = createNotInput(null, end, "bNot"); 
        Node c = insertInput(null, aNot, end, start, "c"); 
        Node cNot = createNotInput(aNot, start, "cNot"); 
        Node c1 = insertInput(null, bNot, end, start, "c1"); 
        Node c1Not = createNotInput(bNot, start, "c1Not");
        Node c2 = insertInput(b, null, start, end, "c2"); 
        Node c2Not = createNotInput(null, end, "c2Not");
        Node m = insertState(null, aNot, c, cNot, "m");
        Node n = insertState(a, null, b, bNot, "n");
        Node o = insertState(null, bNot, c1, c1Not, "o"); 
        Node p = insertState(b, null, c2, c2Not, "p");
        System.out.println("Flowchart 2");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Flowchart 3
        initialize();
        Node a = insertInput(start, start, end, end, "a");
        Node aNot = createNotInput(start, end, "aNot");
        Node b = insertInput(a, null, end, end, "b");
        Node bNot = createNotInput(null, end, "bNot");
        Node c = insertInput(b, null, end, end, "c");
        Node cNot = createNotInput(null, end, "cNot");
        Node d = insertInput(c, null, start, end, "d");
        Node dNot = createNotInput(null, end, "dNot");
        Node d1 = insertInput(null, cNot, end, start, "d1");
        Node d1Not = createNotInput(cNot, start, "d1Not");
        Node d2 = insertInput(null, bNot, end, start, "d2");
        Node d2Not = createNotInput(bNot, start, "d2Not");
        Node d3 = insertInput(null, aNot, end, start, "d3");
        Node d3Not = createNotInput(aNot, start, "d3Not");
        Node p = insertState(a, null, b, bNot, "p");
        Node q = insertState(null, aNot, d3, d3Not, "q");
        Node r = insertState(b, null, c, cNot, "r");
        Node s = insertState(null, bNot, d2, d2Not, "s");
        Node t = insertState(c, null, d, dNot, "t");
        Node u = insertState(null, cNot, d1, d1Not, "u");
        System.out.println("Flowchart 3");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Flowchart 4
        initialize();
        Node a = insertInput(start, start, end, end, "a");
        Node aNot = createNotInput(start, end, "aNot");
        Node b = insertInput(a, null, end, end, "b");
        Node bNot = createNotInput(null, end, "bNot");
        Node b1 = insertInput(null, aNot, end, end, "b1");
        Node b1Not = createNotInput(aNot, end, "b1Not");
        Node c = insertInput(null, bNot, start, end, "c");
        Node cNot = createNotInput(bNot, end, "cNot");
        Node c1 = insertInput(b, null, end, start, "c1");
        Node c1Not = createNotInput(null, start, "c1Not");
        Node c2 = insertInput(null, b1Not, end, start, "c2");
        Node c2Not = createNotInput(b1Not, start, "c2Not");
        Node c3 = insertInput(b1, null, start, end, "c3");
        Node c3Not = createNotInput(null, end, "c3Not");
        Node m = insertState(a, null, b, bNot, "m");
        Node n = insertState(null, aNot, b1, b1Not, "n");
        Node o = insertState(null, bNot, c, cNot, "o");
        Node p = insertState(b, null, c1, c1Not, "p");
        Node q = insertState(null, b1Not, c2, c2Not, "q");
        Node r = insertState(b1, null, c3, c3Not, "r");
        System.out.println("Flowchart 4");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Flowchart 5
        initialize();
        Node a = insertInput(start, start, end, end, "a");
        Node aNot = createNotInput(start, end, "aNot");
        Node b = insertInput(a, null, end, end, "b");
        Node bNot = createNotInput(null, end, "bNot");
        Node c = insertInput(b, null, end, end, "c");
        Node cNot = createNotInput(null, end, "cNot");
        Node c1 = insertInput(null, bNot, end, start, "c1");
        Node c1Not = createNotInput(bNot, start, "c1Not");
        Node c2 = insertInput(null, aNot, end, start, "c2");
        Node c2Not = createNotInput(aNot, start, "c2Not");
        Node d = insertInput(c, null, start, end, "d");
        Node dNot = createNotInput(null, end, "dNot");
        Node d1 = insertInput(null, cNot, end, start, "d1");
        Node d1Not = createNotInput(cNot, start, "d1Not");
        Node d2 = insertInput(c1, null, end, start, "d2");
        Node d2Not = createNotInput(null, start, "d2Not");
        Node d3 = insertInput(c2, null, end, start, "d3");
        Node d3Not = createNotInput(null, start, "d3Not");
        Node p = insertState(a, null, b, bNot, "p");
        Node q = insertState(null, aNot, c2, c2Not, "q");
        Node r = insertState(b, null, c, cNot, "r");
        Node s = insertState(null, bNot, c1, c1Not, "s");
        Node t = insertState(c, null, d, dNot, "t");
        Node u = insertState(null, cNot, d1, d1Not, "u");
        Node v = insertState(c1, null, d2, d2Not, "v");
        Node w = insertState(c2, null, d3, d3Not, "w");
        System.out.println("Flowchart 5");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Flowchart 6
        initialize();
        Node a = insertInput(start, start, end, end, "a");
        Node aNot = createNotInput(start, end, "aNot");
        Node b = insertInput(a, null, end, end, "b");
        Node bNot = createNotInput(null, end, "bNot");
        Node c = insertInput(b, null, end, end, "c");
        Node cNot = createNotInput(null, end, "cNot");
        Node c1 = insertInput(null, bNot, end, end, "c1");
        Node c1Not = createNotInput(bNot, end, "c1Not");
        Node c2 = insertInput(null, aNot, end, end, "c2");
        Node c2Not = createNotInput(aNot, end, "c2Not");
        Node d = insertInput(c, null, end, start, "d");
        Node dNot = createNotInput(null, start, "dNot");
        Node d1 = insertInput(null, cNot, start, end, "d1");
        Node d1Not = createNotInput(cNot, end, "d1Not");
        Node d2 = insertInput(c1, null, start, end, "d2");
        Node d2Not = createNotInput(null, end, "d2Not");
        Node d3 = insertInput(null, c1Not, end, start, "d3");
        Node d3Not = createNotInput(c1Not, start, "d3Not");
        Node d4 = insertInput(null, c2Not, end, start, "d4");
        Node d4Not = createNotInput(c2Not, start, "d4Not");
        Node d5 = insertInput(c2, null, start, end, "d5");
        Node d5Not = createNotInput(null, end, "d5Not");
        Node m = insertState(a, null, b, bNot, "m");
        Node n = insertState(null, aNot, c2, c2Not, "n");
        Node o = insertState(b, null, c, cNot, "o");
        Node p = insertState(null, bNot, c1, c1Not, "p");
        Node q = insertState(c, null, d, dNot, "q");
        Node r = insertState(null, cNot, d1, d1Not, "r");
        Node s = insertState(c1, null, d2, d2Not, "s");
        Node t = insertState(null, c1Not, d3, d3Not, "t");
        Node u = insertState(null, c2Not, d4, d4Not, "u");
        Node v = insertState(c2, null, d5, d5Not, "v");
        System.out.println("Flowchart 6");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Flowchart 7
        initialize();
        Node a = insertInput(start, start, end, end, "a");
        Node aNot = createNotInput(start, end, "aNot");
        Node b = insertInput(null, aNot, end, end, "b");
        Node bNot = createNotInput(aNot, end, "bNot");
        Node c = insertInput(null, bNot, end, end, "c");
        Node cNot = createNotInput(bNot, end, "cNot");
        Node d = insertInput(null, cNot, end, end, "d");
        Node dNot = createNotInput(cNot, end, "dNot");
        Node e = insertInput(null, dNot, end, start, "e");
        Node eNot = createNotInput(dNot, start, "eNot");
        Node m = insertState(null, aNot, b, bNot, "m");
        Node n = insertState(null, bNot, c, cNot, "n");
        Node o = insertState(null, cNot, d, dNot, "o");
        Node p = insertState(null, dNot, e, eNot, "p");
        System.out.println("Flowchart 7");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */

        /*
        // Flowchart 8
        initialize();
        Node a = insertInput(start, start, end, end, "a");
        Node aNot = createNotInput(start, end, "aNot");
        Node m = insertState(a, null, end, end, "m");
        Node n = insertState(null, aNot, end, end, "n");
        Node b = insertInput(n, n, end, end1, "b");
        Node bNot = createNotInput(n, end1, "bNot");
        Node b1 = insertInput(m, m, start, end, "b1");
        Node b1Not = createNotInput(m, end, "b1Not");
        Node p = insertState(null, bNot, end, end, "p");
        Node c = insertInput(p, p, p, end1, "c");
        Node cNot = createNotInput(p, end1, "cNot");
        end.t = start;
        end.f = start;
        end1.t = start;
        end1.f = start;
        start.prevt = end;
        start.prevf = end;
        System.out.println("Flowchart 8");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Flowchart 9
        initialize();
        Node a = insertInput(start, start, end, end, "a");
        Node aNot = createNotInput(start, end, "aNot");
        Node b = insertInput(a, null, end, end1, "b");
        Node bNot = createNotInput(null, end1, "bNot");
        Node b1 = insertInput(null, aNot, end2, end3, "b1");
        Node b1Not = createNotInput(aNot, end3, "b1Not");
        Node m = insertState(a, null, b, bNot, "m");
        Node n = insertState(null, aNot, b1, b1Not, "n");
        end.t = start;
        end.f = start;
        end1.t = start;
        end1.f = start;
        end2.t = start;
        end2.f = start;
        end3.t = start;
        end3.f = start;
        start.prevt = end;
        start.prevf = end;
        System.out.println("Flowchart 9");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Flowchart 10
        initialize();
        Node a = insertInput(start, start, end, end, "a");
        Node aNot = createNotInput(start, end, "aNot");
        Node b = insertInput(null, aNot, end, end2, "b");
        Node bNot = createNotInput(aNot, end2, "bNot");
        Node b1 = insertInput(a, null, end1, start, "b1");
        Node b1Not = createNotInput(null, start, "b1Not");
        Node a1 = insertInput(end, end, end1, end1, "a1");
        Node a1Not = createNotInput(end, end1, "a1Not");
        Node b2 = insertInput(null, a1Not, end, end2, "b2");
        Node b2Not = createNotInput(a1Not, end2, "b2Not");
        Node b3 = insertInput(a1, null, end1, start, "b3");
        Node b3Not = createNotInput(null, start, "b3Not");
        Node a2 = insertInput(end1, end1, end2, end2, "a2");
        Node a2Not = createNotInput(end1, end2, "a2Not");
        Node b4 = insertInput(null, a2Not, end, end2, "b4");
        Node b4Not = createNotInput(a2Not, end2, "b4Not");
        Node b5 = insertInput(a2, null, end1, start, "b5");
        Node b5Not = createNotInput(null, start, "b5Not");
        Node a3 = insertInput(end2, end2, start, start, "a3");
        Node a3Not = createNotInput(end2, start, "a3Not");
        Node b6 = insertInput(null, a3Not, end, end2, "b6");
        Node b6Not = createNotInput(a3Not, end2, "b6Not");
        Node b7 = insertInput(a3, null, end1, start, "b7");
        Node b7Not = createNotInput(null, start, "b7Not");
        Node m = insertState(null, aNot, b, bNot, "m");
        Node n = insertState(a, null, b1, b1Not, "n");
        Node o = insertState(null, a1Not, b2, b2Not, "o");
        Node p = insertState(a1, null, b3, b3Not, "p");
        Node q = insertState(null, a2Not, b4, b4Not, "q");
        Node r = insertState(a2, null, b5, b5Not, "r");
        Node s = insertState(null, a3Not, b6, b6Not, "s");
        Node t = insertState(a3, null, b7, b7Not, "t");
        System.out.println("Flowchart 10");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Sample Flowchart
        initialize();
        Node a = insertInput(start, start, start, end, "a");
        Node aNot = createNotInput(start, end, "aNot");
        Node a1 = insertInput(end, end, end1, end1, "a1");
        Node a1Not = createNotInput(end, end1, "a1Not");
        Node x = insertState(null, a1Not, end, end, "x");
        Node b = insertInput(x, x, end, start, "b");
        Node bNot = createNotInput(x, start, "bNot");
        end1.t = start;
        end1.f = start;
        start.prevt = end1;
        start.prevf = end1;
        System.out.println("Sample Flowchart");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Sample Flowchart 1
        initialize();
        Node a = insertInput(start, start, end, start, "a");
        Node aNot = createNotInput(start, start, "aNot");
        Node b = insertInput(a, null, end, start, "b");
        Node bNot = createNotInput(null, start, "bNot");
        Node a1 = insertInput(end, end, end, end1, "a1");
        Node a1Not = createNotInput(end, end1, "a1Not");
        Node m = insertState(a, null, b, bNot, "m");
        end1.t = start;
        end1.f = start;
        start.prevt = end1;
        start.prevf = end1;
        System.out.println("Sample Flowchart 1");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
        
        /*
        // Sample Flowchart 2
        initialize();
        Node a = insertInput(start, start, end, start, "a");
        Node aNot = createNotInput(start, start, "aNot");
        Node a1 = insertInput(end, end, start, end, "a1");
        Node a1Not = createNotInput(end, end, "a1Not");
        System.out.println("Sample Flowchart 2");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());  
        */
        
        /*
        // Sample Flowchart 3
        initialize();
        Node a = insertInput(start, start, end, end1, "a");
        Node aNot = createNotInput(start, end1, "aNot");
        System.out.println("Sample Flowchart 3");
        System.out.println();
        addPaths();
        addStatesStrings();
        addInputNodesStrings();
        System.out.println("List of Paths: ");
        int count = 0;
        int size;
        for (int i=0; i < pathList.size(); i=i+size)
        {
            size = 0;
            System.out.print(pathList.get(count));
            count++;
            size++;
            if (allStatesStrings.contains(pathList.get(count)))
            {
                System.out.print(" = " + pathList.get(count));
                count++;
                size++;
            }
            else if (inputNodesStrings.contains(pathList.get(count)))
            {
                System.out.print(" ^ " + pathList.get(count));
                count++;
                size++;
                if (allStatesStrings.contains(pathList.get(count)))
                {
                    System.out.print(" = " + pathList.get(count));
                    count++;
                    size++;
                }
            }
            if (size == 2) {
                System.out.println(" (Feynman gate) ");
            }
            else if (size == 3) {
                System.out.println(" (Toffoli gate) ");
            }
        }
        System.out.println("Number of inputs: " + (inputNodes.size()/2));
        System.out.println("Number of outputs: " + firstStates.size());
        System.out.println("Number of ancilla bits: " + allStates.size());
        System.out.println("Number of paths: " + returnPathNumber());
        System.out.println("Number of toffoli gates: " + toffoli);
        System.out.println("Number of feynman gates: " + feynman);
        System.out.println("Quantum Cost: " + calcCost());
        */
    }
}