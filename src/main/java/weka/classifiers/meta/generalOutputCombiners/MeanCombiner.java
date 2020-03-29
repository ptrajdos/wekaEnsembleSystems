/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import weka.core.Instance;
import weka.core.RevisionUtils;
import weka.core.Utils;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.5.0
 *
 */
public class MeanCombiner extends GeneralCombiner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8198439029304881890L;

	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance)
			throws Exception {
		this.checkCompatibility(rawPredictions, testInstance);
		int numClasses = testInstance.numClasses();
		double[] finalPredictions = new double[numClasses];
		
		for(int i=0;i<numClasses;i++){
			for(int j=0;j<rawPredictions.length;j++){
				finalPredictions[i]+= rawPredictions[j][i];
			}
			finalPredictions[i]/=rawPredictions.length;
		}
		
		this.normalizeOutput(finalPredictions);
		
		return finalPredictions;
	}

	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance,
			double[] weights) throws Exception {
		this.checkCompatibility(rawPredictions, testInstance, weights);
		int numClasses = testInstance.numClasses();
		double[] finalPrediction = new double[numClasses];
		
		double weightsSum = Utils.sum(weights);
		if(Utils.eq(weightsSum, 0.0))
			return new double[numClasses];
		
		
		for(int i=0;i<numClasses;i++){
			for(int j=0;j<rawPredictions.length;j++){
				finalPrediction[i]+= rawPredictions[j][i]*weights[j];
			}
			finalPrediction[i]/=weightsSum;
		}
		
		this.normalizeOutput(finalPrediction);
		return finalPrediction;
	}

	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1$");
	}

}
