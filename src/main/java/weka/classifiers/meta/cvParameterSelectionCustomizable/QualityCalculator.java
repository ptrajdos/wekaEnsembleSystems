/**
 * 
 */
package weka.classifiers.meta.cvParameterSelectionCustomizable;

import weka.classifiers.Evaluation;

/**
 * @author pawel
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
