/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import weka.core.Instance;
import weka.core.Utils;


/**
 * @author pawel
 *
 */
public class MedCombiner extends GeneralCombiner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3655301700877487998L;

	/**
	 * 
	 */
	public MedCombiner() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.generalOutputCombiners.OutputCombiner#getCombinedDistributionForInstance(double[][], weka.core.Instance)
	 */
	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance)
			throws Exception {
		double[] weights = new double[rawPredictions.length];
		Arrays.fill(weights, 1);
		return this.getCombinedDistributionForInstance(rawPredictions, testInstance, weights);
	}

	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance,
			double[] weights) throws Exception {
		this.checkCompatibility(rawPredictions, testInstance, weights);
		int numClasses = testInstance.numClasses();
		int numModels = rawPredictions.length;
		double[] prediction  = new double[numClasses];
		double[] newWeights = Arrays.copyOf(weights, weights.length);
		this.normalizeOutput(newWeights);
		
		ArrayList<Pair> valWeiList = new ArrayList<MedCombiner.Pair>(numModels);
		for(int c=0;c<numClasses;c++){
			for(int m=0;m<numModels;m++){
				valWeiList.add(new Pair(rawPredictions[m][c], weights[m]));
			}
			prediction[c] = this.weightedMedian(valWeiList);
		}
		
		this.normalizeOutput(prediction);
		return prediction;
	}
	
	double weightedMedian(List<Pair> list){
		List<Pair> tmpList = new ArrayList<Pair>(list.size());
		Collections.copy(tmpList, list);
		Collections.sort(tmpList, new Comparator<Pair>() {

			@Override
			public int compare(Pair o1, Pair o2) {
				Double v1 = new Double(o1.value);
				Double v2 = new Double(o2.value);
				return v1.compareTo(v2);
			}
		});
		
		double lSum=0;
		double rSum=0;
		LinkedList<Integer> medianIndices = new LinkedList<Integer>();
		int listLen = tmpList.size();
				
		for(int i=1;i<listLen;i++){
			rSum+=tmpList.get(i).weight;
		}
		
		Pair tmpPair = null;
		for(int i=0;i<listLen;i++){
			if(lSum<=0.5 && rSum<=0.5){
				medianIndices.add(i);
			}
			lSum+= tmpList.get(i).weight;
			if(i<(listLen-1)){
				rSum-= tmpList.get(i+1).weight;
			}
			
		}
		
		double median =0;
		double sumOfWeights=0;
		int medSize = medianIndices.size();
		int indice =0;
		
		for(int i=0;i<medSize;i++){
			indice = medianIndices.get(i);
			tmpPair = tmpList.get(indice);
			median+= tmpPair.value * tmpPair.weight;
			sumOfWeights += tmpPair.weight;
		}
		
		if(!Utils.eq(sumOfWeights, 0)){
			median/=sumOfWeights;
		}
		
		
		return median;
	}
	
	class Pair{
		double value=0;
		double weight=0;
		public Pair(double value,double weight) {
			this.value = value;
			this.weight = weight;
		}
	}

}
