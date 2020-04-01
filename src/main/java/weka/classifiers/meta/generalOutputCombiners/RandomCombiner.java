/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.util.Arrays;
import java.util.Random;

import weka.core.Instance;
import weka.core.Randomizable;
import weka.core.Utils;

/**
 * @author pawel trajdos
 * @since 1.6.0
 * @version 1.6.0
 *
 */
public class RandomCombiner extends GeneralCombiner implements Randomizable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2026598234340043360L;
	
	private int seed=0;
	protected Random rnd = new Random(this.seed);

	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance,
			double[] weights) throws Exception {
		int selectedPrediction = this.rnd.nextInt(rawPredictions.length);
		if(Utils.eq(weights[selectedPrediction], 0))
			return new double[rawPredictions[selectedPrediction].length];
		
		double[] result=Arrays.copyOf(rawPredictions[selectedPrediction], rawPredictions[selectedPrediction].length);
		
		
		return result;
	}

	@Override
	public void setSeed(int seed) {
		this.seed = seed;
	}

	@Override
	public int getSeed() {
		return this.seed;
	}

}
