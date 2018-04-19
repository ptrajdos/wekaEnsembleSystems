/**
 * 
 */
package weka.classifiers.meta.customizableBagging;



import java.lang.reflect.Field;

import weka.classifiers.Classifier;
import weka.classifiers.IteratedSingleClassifierEnhancer;
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
		Field f = IteratedSingleClassifierEnhancer.class.getDeclaredField("m_Classifiers");
		f.setAccessible(true);
		Classifier[] classifiers = (Classifier[]) f.get(itClassifier);
		int numModels = classifiers.length;
		
		double[][] responses = new double[numModels][];
		for(int m=0;m<numModels;m++)
			responses[m] = classifiers[m].distributionForInstance(instance);
		
		return responses;
	}
	
	public static double[] getClasses(IteratedSingleClassifierEnhancer itClassifier, Instance instance) throws Exception {
		Field f = IteratedSingleClassifierEnhancer.class.getDeclaredField("m_Classifiers");
		f.setAccessible(true);
		Classifier[] classifiers = (Classifier[]) f.get(itClassifier);;
		int numModels = classifiers.length;
		double[] classes = new double[numModels];
		for(int m=0;m<numModels;m++){
			classes[m] = classifiers[m].classifyInstance(instance);
		}
		return classes;
	}

}
