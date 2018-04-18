/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.util.Arrays;

import weka.core.Instance;

/**
 * @author pawel
 *
 */
public class MaxCombiner extends GeneralCombiner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5962421238540037716L;

	/**
	 * 
	 */
	public MaxCombiner() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.generalOutputCombiners.OutputCombiner#getCombinedDistributionForInstance(double[][], weka.core.Instance)
	 */
	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance)
			throws Exception {
		double[] weights = new double[rawPredictions.length];
		Arrays.fill(weights, 1.0);
		return this.getCombinedDistributionForInstance(rawPredictions, testInstance,weights);
	}

	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance,
			double[] weights) throws Exception {
		this.checkCompatibility(rawPredictions, testInstance, weights);
		int numClasses = testInstance.numClasses();
		double[] prediction = new double[numClasses];
		Arrays.fill(prediction, -Double.MAX_VALUE);
		
		for(int c=0;c<numClasses;c++)
			for(int m =0 ;m<rawPredictions.length;m++)
				if(rawPredictions[m][c]>prediction[c] && weights[m]>0)
					prediction[c] = rawPredictions[m][c];
		
		this.normalizeOutput(prediction);
		return prediction;
	}

}
