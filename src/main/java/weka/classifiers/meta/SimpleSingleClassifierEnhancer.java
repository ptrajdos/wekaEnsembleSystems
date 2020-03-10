/**
 * 
 */
package weka.classifiers.meta;

import weka.classifiers.SingleClassifierEnhancer;
import weka.core.Instance;
import weka.core.Instances;
import weka.tools.GlobalInfoHandler;

/**
 * Simple enhancer that holds only one classifier
 * @author pawel trajdos
 * @since 1.3.0
 * @version 1.3.0
 * 
 *
 */
public class SimpleSingleClassifierEnhancer extends SingleClassifierEnhancer implements GlobalInfoHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6283511661563678701L;

	/**
	 * 
	 */
	public SimpleSingleClassifierEnhancer() {
		super();
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.Classifier#buildClassifier(weka.core.Instances)
	 */
	@Override
	public void buildClassifier(Instances data) throws Exception {
		this.m_Classifier.buildClassifier(data);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		return this.m_Classifier.distributionForInstance(instance);
	}

	@Override
	public String globalInfo() {
		return "Classifier that stores another classifier.";
	}
	
	

}
