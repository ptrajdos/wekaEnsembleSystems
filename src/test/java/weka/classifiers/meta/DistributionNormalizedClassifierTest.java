package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;

public class DistributionNormalizedClassifierTest extends AbstractClassifierTest{

	public DistributionNormalizedClassifierTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		return new DistributionNormalizedClassifier();
	}
	
	public static Test suite() {
	    return new TestSuite(DistributionNormalizedClassifierTest.class);
	  }

	  public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }

	

	

}
