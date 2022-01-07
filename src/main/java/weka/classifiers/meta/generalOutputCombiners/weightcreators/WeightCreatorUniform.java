/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners.weightcreators;

import java.io.Serializable;
import java.util.Arrays;

import weka.core.Instance;
import weka.core.Utils;

/**
 * @author pawel trajdos
 * @since 1.12.0
 * @version 1.12.0
 *
 */
public class WeightCreatorUniform implements IWeightCreator, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8837872922571833558L;

	@Override
	public double[] createWeights(double[][] predictions, Instance instance) {
		double[] weights = new double[predictions.length];
		Arrays.fill(weights, 1.0);
		Utils.normalize(weights);
		return weights;
	}

}
