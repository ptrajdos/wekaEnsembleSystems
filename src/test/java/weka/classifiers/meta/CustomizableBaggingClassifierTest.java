/**
 * 
 */
package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;


/**
 * @author pawel
 *
 */
public class CustomizableBaggingClassifierTest extends BaggingTest {

	public CustomizableBaggingClassifierTest(String name) { super(name);  }

	  /** Creates a default Classifier */
	  public Classifier getClassifier() {
	    return new CustomizableBaggingClassifier();
	  }

	  public static Test suite() {
	    return new TestSuite(CustomizableBaggingClassifierTest.class);
	  }

	  public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }

	
}
