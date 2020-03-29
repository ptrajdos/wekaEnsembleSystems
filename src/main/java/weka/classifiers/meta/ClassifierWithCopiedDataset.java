/**
 * 
 */
package weka.classifiers.meta;

import weka.classifiers.SingleClassifierEnhancer;
import weka.core.Instance;
import weka.core.Instances;
import weka.tools.GlobalInfoHandler;
import weka.tools.SerialCopier;

/**
 * Classifier with copied dataset.
 * The class makes a copy of the training dataset.
 * It prevents some classifiers from modifying the original dataset
 * @author pawel trajdos
 * @since 1.5.0
 * @version 1.5.0
 *
 */
public class ClassifierWithCopiedDataset extends SingleClassifierEnhancer implements GlobalInfoHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4304125234146601640L;

	/**
	 * 
	 */
	public ClassifierWithCopiedDataset() {
		super();
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		
		Instances copiedData;
		
		copiedData = (Instances) SerialCopier.makeCopy(data);
		
		
		this.m_Classifier.buildClassifier(copiedData);

	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		return this.m_Classifier.distributionForInstance(instance);
	}

	@Override
	public String globalInfo() {
		return "A classifier with copied dataset";
	}
	
	
	
	

}
