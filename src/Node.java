import java.util.*;

public class Node {
int x;
int y;
LinkedList<Node> student;
int edgesIncidentUponNode;
int currentElement;

public Node(int x,int y){
this.x = x;
this.y = y;
student=new LinkedList<Node>();	
currentElement=0;
edgesIncidentUponNode=0;
}

public int getX(){
	return x;
}

public int getY(){
	return y;
}
	
	/*
	public String toString(){
		return marks+" ";	
		}
	 */

public LinkedList<Node> getNode(){
	return student;
	}

public void connect(Node e){
	this.student.add(e);
	edgesIncidentUponNode++;
}

public boolean hasNext(){
	if(currentElement<edgesIncidentUponNode){
	 currentElement++;
	 return true;
	}
	else
	currentElement=0;
	return false;
}

public boolean isConnectedTo(Integer e){
	return student.contains(e);
}

public boolean equals(Node x){
	return this.student.equals(x);
	}

public void showEdges(){
	Iterator<Node> it=student.iterator();
	while(it.hasNext()){
		System.out.print(it.next()+" ");
	}
	System.out.println();
}

public static double dist(Node a,Node b){
	return Math.sqrt(Math.pow(a.x-b.x, 2)+Math.pow(a.y-b.y,2));
	}
public int totalEdgesIncidentUpon(){
	return edgesIncidentUponNode;
}
}