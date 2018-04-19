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
public class CommitteeExtractorIteratedSingleClassifierEnhancer {

	
	public static Classifier[] getCommittee(IteratedSingleClassifierEnhancer model)throws Exception{
		Field f = IteratedSingleClassifierEnhancer.class.getDeclaredField("m_Classifiers");
		f.setAccessible(true);
		Classifier[] models = (Classifier[]) f.get(model);
		return models;
	}
}
