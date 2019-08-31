/**
 * 
 */
package weka.classifiers.meta.cvParameterSelectionCustomizable;

import weka.classifiers.Evaluation;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.1.1
 *
 */
public interface QualityCalculator {
	
	/**
	 * Returns classification quality measured by Evaluation
	 * @param evaluation -- evaluation
	 * @return quality criterion.
	 */
	public double getQuality(Evaluation evaluation);

}
