import java.util.*;
import java.awt.*;
import java.applet.*;



public class KMeans extends Applet implements Runnable {
	
	private static final long serialVersionUID = 4621753965498981175L;  // for networking application - IGNORE
	ArrayList<Nodes> featureVector;		//feature Vector (technically array)
	ArrayList<Cluster> cluster;		//list of clusters
	Choice choicePanel;  
	int numOfClusters;
	Button start,reset,run,drawFeatures,restart; 
	Nodes nodes;
	Cluster clust;
	Thread thread;
	Point p;
	int step; //current step of program
	boolean abort;
	static Random random;
	
	public void init(){                                  //applet execution initialization state
		random = new Random();	
		cluster = new ArrayList<Cluster>();
		featureVector = new ArrayList<Nodes>();
		this.setPreferredSize(new Dimension(600,600));
		nodes = new Nodes();
		clust = new Cluster();
		p = new Point();
		/* buttons created */
		start = new Button("Start");
		reset = new Button("Reset");
		run = new Button("Run");
		restart = new Button("Restart");
		drawFeatures = new Button("Draw Features");
		
		/* button added to applet */
		this.add(start);
		this.add(restart);
		this.add(reset);
		this.add(run);
		this.add(drawFeatures);
		start.setEnabled(false);
		reset.setEnabled(false);
		run.setEnabled(false);
		restart.setEnabled(false);
		
		choicePanel = new Choice(); //choice panel - seleting number of gaussian clusters
		
		for(int i=2;i<8;i++)
			choicePanel.add(i+"");
		
		this.add(choicePanel);
		step = -1;
		numOfClusters = 2;
	}
	
	public void paint(Graphics g){   /* graphics object created by java system when created an applet, object therefore passed through function paint */
		g.setColor(Color.BLACK);
		g.drawRect(0,50,500,300);
		int numOfVectors = featureVector.size();
		for(int i=0;i<numOfVectors;i++){     
			s = featureVector.get(i);
			s.draw(g);
		}
		if(step != -1){                               
			for(int i=0;i<cluster.size();i++){
				clust = cluster.get(i);
				clust.draw(g);
			}
		}
	}
	
	public static int randBetween(int max,int min){
		return random.nextInt((max-min)+1)+min;
	}
	
	public void run() {       //called when thread started
		while (true) {
            if      (step ==-1) this.step1();
            else if (step == 1) this.step2();
            else if (step == 2) this.step3();
            else if (step == 3) step = 4;
            else if ((step == 4) && (abort==true)){
                reset.setEnabled(true);
                restart.setEnabled(true);
                step = 5;
                repaint();
                thread.stop();
            }
            else if ((step == 4) && (abort==false))   
            step2();   
            repaint();
			try{			
				Thread.sleep(100);  // for animation
			}
			catch (InterruptedException e) {
			}
		}
	}
	
	public boolean action(Event event,Object object){   //action listener - buttons,mouse,keyboard
		
		if(event.target==start){
			System.out.print("start");
			start.setLabel("Step");
			switch(step){
			case -1:step1();
					break;
			case  1:step2();
					break;
			case  2:step3();
					break;
			case  3: step = 4;
					break;
			case  4:if(abort){
						start.setEnabled(false);
						run.setEnabled(false);
						step = 5;
					}
					else 
						step2();
					break;
			}
			repaint();
			return true;
			}
			
		if(event.target==run){
			thread = new Thread(this);
            thread.start();
            start.setEnabled(false);
            restart.setEnabled(false);
            reset.setEnabled(false);
            run.setEnabled(false);
            return true; 
		}
		
		if(event.target==drawFeatures){
			reset();
			numOfClusters = Integer.parseInt(choicePanel.getSelectedItem());
			//gaussian distributed random numbers
			//Random.nextGaussian() not working
			
			ArrayList<Gaussian> gaus = new ArrayList<Gaussian>();
			for(int i=0;i<numOfClusters;i++){
				Gaussian gau = new Gaussian();
				gau.mux = randBetween(480,30);
				gau.muy = randBetween(280,70);
				gau.sigma = 10+Math.abs(10*random.nextDouble());
				gaus.add(gau);
			}
			reset.setEnabled(true);
			start.setEnabled(true);
			run.setEnabled(true);
			
			for(int i=0;i<numOfClusters;i++){         //gaussian distrbution per cluster
				Gaussian temp = gaus.get(i);
				for(int j=0;j<2000/numOfClusters;j++){
					double r = 5*temp.sigma*Math.pow(random.nextDouble(),2);
					double alpha = 2*Math.PI*random.nextDouble();
					int x = temp.mux + (int)Math.round(r*Math.cos(alpha));
					int y = temp.muy + (int)Math.round(r*Math.sin(alpha));
					
					if(positionAllowed(x,y)){
						Nodes tempNode = new Nodes();
						tempNode.x = x;
						tempNode.y = y;
						tempNode.color=Color.BLACK;
						featureVector.add(tempNode);
					}
				}
			}
			repaint();
			return true;
			
		}
		if(event.target==restart && step!=-1){
			step = -1;
			abort = false;
			cluster.clear();
			Nodes temp;
			for(int i=0;i<featureVector.size();i++){
				temp = featureVector.get(i);
				temp.color=Color.BLACK;
			}
			start.setLabel("Start");
			start.setEnabled(true);
			reset.setEnabled(true);
			run.setEnabled(true);
			
			repaint();
			return true;
		}
		
		if(event.target==reset){
			reset();
			return true;
			
		}
		return true;
			
	}
	
	private void reset() {
		step = -1;
		abort = false;
		cluster.clear();
		Nodes temp;
		for(int i=0;i<featureVector.size();i++){
			temp = featureVector.get(i);
			temp.color=Color.WHITE;
		}
		featureVector.clear();
		start.setLabel("Start");
	    start.setEnabled(false);
	    restart.setEnabled(false);
	    reset.setEnabled(false);
	    run.setEnabled(false);
	    repaint();
	}
	
	private boolean positionAllowed(int x, int y) {
		if((x>=5)&&(y>=55)&&(x<500)&&(y<300))
			return true;
		return false;
	}

		private void step1() {
		abort = false;
		numOfClusters = Integer.parseInt(choicePanel.getSelectedItem());
		int featureSize = featureVector.size();
		boolean ch[] = new boolean[featureSize];
		for(int i=0;i<featureSize;i++)
			ch[i] = true;
			
		for(int i=0;i<numOfClusters;i++){
			Nodes temp;
			Cluster tempCluster = new Cluster();
			int r = Math.abs(random.nextInt() % featureSize);
			if(ch[r]){
				temp = featureVector.get(r);
				tempCluster.x = temp.x;
				tempCluster.y = temp.y;
						if (i == 0) tempCluster.color = Color.GREEN;
	               else if (i == 1) tempCluster.color = Color.RED;
	               else if (i == 2) tempCluster.color = Color.BLUE;
	               else if (i == 3) tempCluster.color = Color.YELLOW;
	               else if (i == 4) tempCluster.color = Color.ORANGE;
	               else if (i == 5) tempCluster.color = Color.MAGENTA;
	               else if (i == 6) tempCluster.color = Color.CYAN;
	               else if (i == 7) tempCluster.color = Color.GRAY;
	            cluster.add(tempCluster);
	            ch[r] = false;
	         }
		}
		step = 1;
	}
	
		private void step2() {
			Nodes temp;
			Cluster tempCluster;
			for(int i=0;i<featureVector.size();i++){
				temp = featureVector.get(i);
				int indexOfMin = 0;
				double min_distance = 99999999.0;
				for(int j=0;j<cluster.size();j++){
				tempCluster = cluster.get(j);
				double dist = Point.distance(temp.x, temp.y,tempCluster.x,tempCluster.y);
					if(dist < min_distance){
						min_distance = dist;
						indexOfMin = j;
					}
				}
				tempCluster = cluster.get(indexOfMin);
				temp.color = tempCluster.color;
			}
			step=2;
		}
	
		private void step3() {
			Cluster tempCluster;
			Nodes temp;
			double change = 0.0;
			for(int i=0;i<cluster.size();i++){
				tempCluster = cluster.get(i);
				p.x=0;
				p.y=0;
				int count = 0;
				for(int j=0;j<featureVector.size();j++){
					temp = featureVector.get(j);
					if(temp.color == tempCluster.color){
						p.x+=temp.x;
						p.y+=temp.y;
						count++;
					}
				}
				if(count>0){
					 change+=Point.distance(tempCluster.x,tempCluster.y,p.x/count,p.y/count);
					 tempCluster.x = p.x/count;
					 tempCluster.y = p.y/count;
				}
			}
			if(change<0.1) abort=true;
			step =3;
		}
	
	
}
