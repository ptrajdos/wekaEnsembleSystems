/**
 * 
 */
package weka.classifiers.tools;

import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author pawel
 *
 */
public class FailingClassifier extends AbstractClassifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5674438547909806960L;

	/**
	 * 
	 */
	public FailingClassifier() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
			throw new Exception("Exception");

	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		throw new Exception("Exception");
	}
	
	
	
	

}
