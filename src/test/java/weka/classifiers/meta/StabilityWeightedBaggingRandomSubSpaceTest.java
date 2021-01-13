package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;

public class StabilityWeightedBaggingRandomSubSpaceTest extends StabilityWeightedBaggingTest {

	public StabilityWeightedBaggingRandomSubSpaceTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Classifier getClassifier() {
		return new StabilityWeightedBaggingRandomSubSpace();
	}
	
	 public static Test suite() {
		    return new TestSuite(StabilityWeightedBaggingRandomSubSpaceTest.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }
	  

	

}
