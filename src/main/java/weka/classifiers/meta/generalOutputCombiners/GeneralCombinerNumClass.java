/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.io.Serializable;

import weka.classifiers.Classifier;
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

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.generalOutputCombiners.OutputCombinerNumClass#getClass(weka.classifiers.Classifier[], weka.core.Instance)
	 */
	@Override
	public double getClass(Classifier[] committee, Instance testInstance) throws Exception {
		double[] outputs = new double[committee.length];
		for(int c=0;c<committee.length;c++) 
			outputs[c]=committee[c].classifyInstance(testInstance);
		
		return this.getClass(outputs, testInstance);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.generalOutputCombiners.OutputCombinerNumClass#getClass(weka.classifiers.Classifier[], weka.core.Instance, double[])
	 */
	@Override
	public double getClass(Classifier[] committee, Instance testInstance, double[] weights) throws Exception {
		double[] outputs = new double[committee.length];
		for(int c=0;c<committee.length;c++) 
			outputs[c]=committee[c].classifyInstance(testInstance);
		
		return this.getClass(outputs, testInstance,weights);
	}
	
	

}
