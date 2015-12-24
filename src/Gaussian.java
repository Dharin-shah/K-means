

public class Gaussian {
   int mux;
   int muy;
   double sigma;
   double function(int x, int y)
   {
       double ret = Math.exp(-0.5*((this.mux-x)*(this.mux-x)+(this.muy-y)*(this.muy-y))/(this.sigma*this.sigma));
       return ret/(Math.sqrt(2*Math.PI)*this.sigma);
   }
   public String toString(){
	   return "mux= "+mux+" muy= "+muy+" sigma= "+sigma;
   }
}
