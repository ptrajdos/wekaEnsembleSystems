/**
 * 
 */
package weka.classifiers.meta.tools;



import java.lang.reflect.Field;

import weka.classifiers.Classifier;
import weka.classifiers.IteratedSingleClassifierEnhancer;
import weka.classifiers.MultipleClassifiersCombiner;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.1.1
 *
 */
public class CommitteeExtractor {

	
	public static Classifier[] getCommittee(IteratedSingleClassifierEnhancer model)throws Exception{
		Field f = IteratedSingleClassifierEnhancer.class.getDeclaredField("m_Classifiers");
		f.setAccessible(true);
		Classifier[] models = (Classifier[]) f.get(model);
		return models;
	}
	
	public static Classifier[] getCommittee(MultipleClassifiersCombiner model)throws Exception{
		Field f = MultipleClassifiersCombiner.class.getDeclaredField("m_Classifiers");
		f.setAccessible(true);
		Classifier[] models = (Classifier[]) f.get(model);
		return models;
	}
	
}
