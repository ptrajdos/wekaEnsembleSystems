/**
 * 
 */
package weka.classifiers.tools;

import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author pawel
 *
 */
public class FailingClassifier2 extends AbstractClassifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5674438547909806960L;

	/**
	 * 
	 */
	public FailingClassifier2() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		

	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		throw new Exception("Exception");
	}
	
	
	
	
	
	

}
