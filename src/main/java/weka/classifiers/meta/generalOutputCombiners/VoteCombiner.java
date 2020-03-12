/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import weka.core.Instance;
import weka.core.Utils;

/**
 * @author pawel trajdos
 * @since 1.4.0
 * @version 1.4.0
 *
 */
public class VoteCombiner extends GeneralCombiner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8816226425218700844L;

	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance,
			double[] weights) throws Exception {
		int numClasses = testInstance.numClasses();
	
		double[] predictions = new double[numClasses];
		
		
		int numClassifiers = rawPredictions.length;
		for(int i=0;i<numClassifiers;i++) {
			int maxIdx = Utils.maxIndex(rawPredictions[i]);
			if(!Utils.eq(rawPredictions[i][maxIdx], 0.0))
				predictions[maxIdx]+= weights[i];
		}
		
		
		double sum = Utils.sum(predictions);
		if(!Utils.eq(sum, 0)) {
			Utils.normalize(predictions, sum);
		}
		
		return predictions;
	}

}
