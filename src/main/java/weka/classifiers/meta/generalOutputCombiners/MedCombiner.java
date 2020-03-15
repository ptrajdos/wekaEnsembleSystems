/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.util.ArrayList;
import java.util.Arrays;

import weka.classifiers.tools.Pair;
import weka.classifiers.tools.StatsUtils;
import weka.core.Instance;
import weka.core.RevisionUtils;
import weka.core.Utils;


/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.3.1
 *
 */
public class MedCombiner extends GeneralCombiner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3655301700877487998L;




	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance,
			double[] weights) throws Exception {
		this.checkCompatibility(rawPredictions, testInstance, weights);		
		int numClasses = testInstance.numClasses();
		
		if(Utils.eq(Utils.sum(weights), 0.0))
			return new double[numClasses];
		
		
		int numModels = rawPredictions.length;
		double[] prediction  = new double[numClasses];
		double[] newWeights = Arrays.copyOf(weights, weights.length);
		this.normalizeOutput(newWeights);
		
		ArrayList<Pair> valWeiList = new ArrayList<Pair>(numModels);
		for(int c=0;c<numClasses;c++){
			valWeiList = new ArrayList<Pair>(numModels);
			for(int m=0;m<numModels;m++){
				valWeiList.add(new Pair(rawPredictions[m][c], newWeights[m]));
			}
			prediction[c] = StatsUtils.weightedMedian(valWeiList);
		}
		
		this.normalizeOutput(prediction);
		return prediction;
	}

	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1$");
	}
	
	
	


}
