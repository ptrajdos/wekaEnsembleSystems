/**
 * 
 */
package weka.classifiers.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import weka.core.Utils;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.1.1
 *
 */
public class StatsUtils {

	
		
		public static double weightedMedian(List<Pair> list) throws Exception{
			StatsUtils.checkWeights(list);
			List<Pair> tmpList = new ArrayList<Pair>(list);
			Collections.sort(tmpList, new Comparator<Pair>() {

				@Override
				public int compare(Pair o1, Pair o2) {
					Double v1 = new Double(o1.getValue());
					Double v2 = new Double(o2.getValue());
					return v1.compareTo(v2);
				}
			});
			
			double lSum=0;
			double rSum=0;
			LinkedList<Integer> medianIndices = new LinkedList<Integer>();
			int listLen = tmpList.size();
					
			for(int i=1;i<listLen;i++){
				rSum+=tmpList.get(i).getWeight();
			}
			
			Pair tmpPair = null;
			for(int i=0;i<listLen;i++){
				if(lSum<=0.5 && rSum<=0.5){
					medianIndices.add(i);
				}
				lSum+= tmpList.get(i).getWeight();
				if(i<(listLen-1)){
					rSum-= tmpList.get(i+1).getWeight();
				}
				
			}
			
			double median =0;
			double sumOfWeights=0;
			int medSize = medianIndices.size();
			int indice =0;
			
			for(int i=0;i<medSize;i++){
				indice = medianIndices.get(i);
				tmpPair = tmpList.get(indice);
				median+= tmpPair.getValue() * tmpPair.getWeight();
				sumOfWeights += tmpPair.getWeight();
			}
			
			if(!Utils.eq(sumOfWeights, 0)){
				median/=sumOfWeights;
			}
			
			return median;
		}
		
		public static double[] generateUniformWeights(int length){
			double[] weights = new double[length];
			Arrays.fill(weights, 1.0);
			return weights;
		}
		
		public static void checkWeights(List<Pair> list)throws Exception{
			double weightsSum = 0;
			for (Pair pair : list) {
				weightsSum += pair.getWeight();
			}
			if(! Utils.eq(weightsSum, 1))throw new Exception("Weights do not sum up to 1!");
	}
	

}
