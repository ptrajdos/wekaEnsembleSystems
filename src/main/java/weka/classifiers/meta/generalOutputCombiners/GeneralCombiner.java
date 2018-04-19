/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.io.Serializable;

import weka.core.Instance;
import weka.core.RevisionHandler;
import weka.core.Utils;

/**
 * @author pawel
 *
 */
public abstract class GeneralCombiner implements OutputCombiner,Serializable, RevisionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 573687151292284645L;

	/**
	 * 
	 */

	
	public void checkCompatibility(double[][] rawPredictions, Instance testInstance) throws Exception{
		int numClasses = testInstance.numClasses();
		if(rawPredictions[0].length != numClasses)
			throw new Exception("Test instance and the rawPredictions are incompatible");
	}
	
	public void checkCompatibility(double[][] rawPredictions, Instance testInstance, double[] weights) throws Exception{
		int numClasses = testInstance.numClasses();
		if(rawPredictions[0].length != numClasses)
			throw new Exception("Test instance and the rawPredictions are incompatible");
		if(rawPredictions.length != weights.length)
			throw new Exception("Test instance and the weights are incompatible");
	}
	
	public void normalizeOutput(double[] output){
		if(Utils.eq(Utils.sum(output), 0)){
			return;
		}
		Utils.normalize(output);
	}

}
