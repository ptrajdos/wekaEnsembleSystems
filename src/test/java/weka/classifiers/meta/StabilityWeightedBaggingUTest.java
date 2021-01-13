package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesUpdateable;

public class StabilityWeightedBaggingUTest extends CustomizableBaggingClassifier2Test {

	public StabilityWeightedBaggingUTest(String name) {
		super(name);
	}
	
	/** Creates a default Classifier */
	  public Classifier getClassifier() {
		  StabilityWeightedBagging bag =new StabilityWeightedBagging();
		  NaiveBayesUpdateable updB = new NaiveBayesUpdateable();
		  updB.setUseKernelEstimator(true);
		  bag.setClassifier(updB);
		  
	    return bag; 
	  }

	  public static Test suite() {
		    return new TestSuite(StabilityWeightedBaggingUTest.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }

	

}
