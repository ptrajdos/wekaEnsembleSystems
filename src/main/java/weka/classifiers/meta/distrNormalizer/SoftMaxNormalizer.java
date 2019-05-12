/**
 * 
 */
package weka.classifiers.meta.distrNormalizer;

import weka.core.UtilsPT;

/**
 * Soft-max-based array normalization
 * @author pawel trajdos
 * @since 1.1.0
 * @version 1.1.0
 *
 */
public class SoftMaxNormalizer implements INormalizer {


	@Override
	public double[] normalize(double[] array) {
		return UtilsPT.softMax(array);
	}

}
