import java.util.ArrayList;


public class Graph {
	ArrayList<Node> cluster;

public Graph(){
	cluster = new ArrayList<Node>();
}

public int size(){
	return cluster.size(); 
}

public void add(Node x){
	cluster.add(x);	
	//cluster.
}

public void add(Node x,int i){
	cluster.get(i).connect(x);
}
public Node get(int index){
	return cluster.get(index);
}

public void setSize(int N){
	cluster.ensureCapacity(N);
}

public void remove(int index){
	cluster.remove(index);
}

public void showGraph(){
	for(int i=0;i<cluster.size();i++){
		System.out.print("class "+(i+1)+" :- ");
		cluster.get(i).showEdges();
	}
}

}
