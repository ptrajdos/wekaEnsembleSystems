/**
 * 
 */
package weka.classifiers.meta.cvParameterSelectionCustomizable;

import java.io.Serializable;

import weka.classifiers.Evaluation;

/**
 * @author pawel
 *
 */
public class ErrorRateQualityCalculator implements QualityCalculator, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8443668767046108136L;

	/* (non-Javadoc)
	 * @see SingleLabelMulticlassifiers.classifiers.meta.cvp.QualityCalculator#getQuality(weka.classifiers.evaluation.Evaluation)
	 */
	@Override
	public double getQuality(Evaluation eval) {
		return eval.errorRate();
	}

}
