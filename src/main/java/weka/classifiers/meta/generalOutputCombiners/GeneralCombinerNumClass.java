/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.io.Serializable;

import weka.core.Instance;

/**
 * @author pawel
 *
 */
public abstract class GeneralCombinerNumClass implements OutputCombinerNumClass, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8725511799618036682L;

	public void checkCompatibility(double[] predictions,Instance testInstance, double[] weights) throws Exception{
		if (!testInstance.classAttribute().isNumeric())throw new Exception("The class is not a numeric one");
		if(predictions.length != weights.length)throw new Exception("Weights and predictions must have the same length");
	}

}
