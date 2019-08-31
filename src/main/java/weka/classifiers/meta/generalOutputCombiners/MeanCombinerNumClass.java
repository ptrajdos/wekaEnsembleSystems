/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import weka.classifiers.tools.StatsUtils;
import weka.core.Instance;
import weka.core.Utils;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.1.1
 *
 */
public class MeanCombinerNumClass extends GeneralCombinerNumClass {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2528233896978512186L;


	@Override
	public double getClass(double[] predictions, Instance testInstance) throws Exception {
		double[] weights = StatsUtils.generateUniformWeights(predictions.length);
		return this.getClass(predictions, testInstance, weights);
	}

	@Override
	public double getClass(double[] predictions, Instance testInstance, double[] weights) throws Exception {
		this.checkCompatibility(predictions, testInstance, weights);
		
		double prediction	= 0;
		double weightSum	= 0;
		int numModels = predictions.length;
		int numCounted =0;
		for(int i=0;i< numModels;i++){
			if(!Utils.isMissingValue(predictions[i])){
				prediction+= predictions[i]*weights[i];
				weightSum+= weights[i];
				numCounted++;
			}
			
		}
		if(numCounted ==0){
			return Utils.missingValue();
		}
		if(Utils.eq(weightSum, 0)){
			return prediction;
		}
		prediction/=weightSum;
		return prediction;
	}

}
