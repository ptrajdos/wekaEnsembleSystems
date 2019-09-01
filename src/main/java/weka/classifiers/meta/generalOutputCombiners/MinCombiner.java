/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.util.Arrays;

import weka.core.Instance;
import weka.core.RevisionUtils;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.1.1
 *
 */
public class MinCombiner extends GeneralCombiner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2519993314111855577L;


	/* (non-Javadoc)
	 * @see weka.classifiers.meta.generalOutputCombiners.OutputCombiner#getCombinedDistributionForInstance(double[][], weka.core.Instance)
	 */
	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance)
			throws Exception {
			int numModels = rawPredictions.length;
			double[] weights = new double[numModels];
			Arrays.fill(weights, 1.0);
			
		return this.getCombinedDistributionForInstance(rawPredictions, testInstance, weights);
	}

	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance,
			double[] weights) throws Exception {
		this.checkCompatibility(rawPredictions, testInstance, weights);
		int numClasses = testInstance.numClasses();
		double[] predictions = new double[numClasses];
		Arrays.fill(predictions, Double.MAX_VALUE);
	
		
		for(int c =0;c<numClasses;c++){
			for(int m=0;m<rawPredictions.length;m++){
				if(rawPredictions[m][c]<predictions[c] && weights[m]>0){
					predictions[c] = rawPredictions[m][c];
				}
			}
		}
		this.normalizeOutput(predictions);
		return predictions;
	}

	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1$");
	}

}
