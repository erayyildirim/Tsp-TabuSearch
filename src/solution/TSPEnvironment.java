package solution;

import java.util.ArrayList;

public class TSPEnvironment {
	 public double [][] distances;
	    
	   
	    
	    public  int getObjectiveFunctionValue(int solution[]){ 
	        //uzaklıkları bilinen yol maliyeti hesaplanır.
	      
	        int cost = 0;
	   
	        for(int i = 0 ; i < solution.length-1; i++){
	            cost+= distances[solution[i]][solution[i+1]];
	            
	        }
	   
	        return cost;
	        
	    }

}
