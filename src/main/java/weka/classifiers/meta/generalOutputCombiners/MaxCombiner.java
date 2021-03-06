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

	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1$");
	}

}
