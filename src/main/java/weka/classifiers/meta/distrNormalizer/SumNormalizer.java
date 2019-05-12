/**
 * 
 */
package weka.classifiers.meta.distrNormalizer;

import java.io.Serializable;
import java.util.Arrays;

import weka.core.Utils;

/**
 * Simple sum-based array normalization
 * @author pawel trajdos
 *
 * @since 1.1.0
 * @version 1.1.0
 */
public class SumNormalizer implements INormalizer, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5073266791210018101L;

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.distrNormalizer.INormalizer#normalize(double[])
	 */
	@Override
	public double[] normalize(double[] array) {
		double sum = Utils.sum(array);
		double[] tmp = Arrays.copyOf(array, array.length); 
		if(! Utils.eq(0, sum)) {
			Utils.normalize(array, sum);
		}
		return tmp;
	}

}
