/**
 * 
 */
package weka.classifiers.meta.distrNormalizer;

/**
 * Interface for response normalizing objects
 * @author pawel trajdos
 * @since 1.1.0
 * @version 1.1.0
 */
public interface INormalizer {
	
	/**
	 * Normalizes array
	 * @param array
	 * @return
	 * 
	 * @author pawel trajdos
	 * @since 1.1.0
	 * @version 1.1.0
	 */
	public double[] normalize(double[] array);

}
