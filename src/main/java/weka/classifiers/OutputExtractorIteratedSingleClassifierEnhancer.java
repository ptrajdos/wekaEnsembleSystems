/**
 * 
 */
package weka.classifiers;

import weka.core.Instance;

/**
 * @author pawel
 *
 */
public class OutputExtractorIteratedSingleClassifierEnhancer {

	/**
	 * 
	 */
	public OutputExtractorIteratedSingleClassifierEnhancer() {
		// TODO Auto-generated constructor stub
	}
	public static double[][] getDistributions(IteratedSingleClassifierEnhancer itClassifier, Instance instance)
			throws Exception{
		Classifier[] classifiers = itClassifier.m_Classifiers;
		int numModels = classifiers.length;
		
		double[][] responses = new double[numModels][];
		for(int m=0;m<numModels;m++)
			responses[m] = classifiers[m].distributionForInstance(instance);
		
		return responses;
	}
	
	public static double[] getClasses(IteratedSingleClassifierEnhancer itClassifier, Instance instance) throws Exception {
		Classifier[] classifiers = itClassifier.m_Classifiers;
		int numModels = classifiers.length;
		double[] classes = new double[numModels];
		for(int m=0;m<numModels;m++){
			classes[m] = classifiers[m].classifyInstance(instance);
		}
		return classes;
	}

}
