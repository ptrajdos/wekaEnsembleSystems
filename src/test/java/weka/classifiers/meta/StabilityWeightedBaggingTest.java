package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;

public class StabilityWeightedBaggingTest extends CustomizableBaggingClassifier2Test {

	public StabilityWeightedBaggingTest(String name) {
		super(name);
	}
	
	/** Creates a default Classifier */
	  public Classifier getClassifier() {
	    return new StabilityWeightedBagging();
	  }

	  public static Test suite() {
		    return new TestSuite(StabilityWeightedBaggingTest.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }

	

}
