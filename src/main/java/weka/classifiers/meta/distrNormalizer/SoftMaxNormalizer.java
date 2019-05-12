/**
 * 
 */
package weka.classifiers.meta.distrNormalizer;

import java.io.Serializable;

import weka.core.UtilsPT;

/**
 * Soft-max-based array normalization
 * @author pawel trajdos
 * @since 1.1.0
 * @version 1.1.1
 *
 */
public class SoftMaxNormalizer implements INormalizer, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5777209520573656994L;

	@Override
	public double[] normalize(double[] array) {
		return UtilsPT.softMax(array);
	}

}
