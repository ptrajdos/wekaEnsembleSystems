package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.core.Utils;

public class StabilityWeightedBaggingTest extends CustomizableBaggingClassifier2Test {

	public StabilityWeightedBaggingTest(String name) {
		super(name);
	}
	
	/** Creates a default Classifier */
	  public Classifier getClassifier() {
	    return new StabilityWeightedBagging();
	  }
	  
	  public void testApriori() {
		  StabilityWeightedBagging bag = (StabilityWeightedBagging) this.getClassifier();
		  assertTrue("Use apriori default false", !bag.isUseApriori());
		  bag.setUseApriori(true);
		  assertTrue("Use apriori default false", bag.isUseApriori());
		  
		  String[] options = bag.getOptions();
		  try {
			assertTrue("Correct Options set", Utils.getFlag("UAPR", options));
		} catch (Exception e) {
			fail("Options get failed!");
		}
	  }

	  public static Test suite() {
		    return new TestSuite(StabilityWeightedBaggingTest.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }

	

}
