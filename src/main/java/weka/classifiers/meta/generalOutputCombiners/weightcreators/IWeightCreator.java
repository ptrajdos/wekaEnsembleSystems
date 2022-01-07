/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners.weightcreators;

import weka.core.Instance;

/**
 * @author pawel trajdos
 * @since 1.12.0
 * @version 1.12.0
 *
 */
public interface IWeightCreator {
	
	/**
	 * Creates weight for each model
	 * @param predictions -- array of model (classifier predictions) [models] x [classes]
	 * @param instance -- instance for predictions
	 * @return -- array of weights [models]
	 */
	public double[] createWeights(double[][] predictions, Instance instance);

}
