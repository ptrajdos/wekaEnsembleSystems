/**
 * 
 */
package weka.classifiers.meta.cvParameterSelectionCustomizable;

import java.io.Serializable;

import weka.classifiers.Evaluation;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.1.1
 *
 */
public class F1_minorQualityCalculator implements QualityCalculator, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8443668767046108136L;

	/* (non-Javadoc)
	 * @see SingleLabelMulticlassifiers.classifiers.meta.cvp.QualityCalculator#getQuality(weka.classifiers.evaluation.Evaluation)
	 */
	@Override
	public double getQuality(Evaluation eval) {
		double[] priors = eval.getClassPriors();
		int minIdx=0;
		double min = Double.MAX_VALUE;
		for(int i=0;i<priors.length;i++){
			if(priors[i]<min){
				min = priors[i];
				minIdx=i;
			}
		}
		double result =1.0 - eval.fMeasure(minIdx); 
		return result;
	}

}
