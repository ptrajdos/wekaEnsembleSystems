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
 * @version 1.1.1
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
		Utils.normalize(weights);
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
		
		ArrayList<Pair> valWeiList = new ArrayList<Pair>(numModels);
		for(int c=0;c<numClasses;c++){
			valWeiList = new ArrayList<Pair>(numModels);
			for(int m=0;m<numModels;m++){
				valWeiList.add(new Pair(rawPredictions[m][c], weights[m]));
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
