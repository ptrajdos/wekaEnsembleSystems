package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;

public class CustomizableBaggingClassifierRandomSubspace2Test extends CustomizableBaggingClassifier2Test {

	public CustomizableBaggingClassifierRandomSubspace2Test(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/** Creates a default Classifier */
	  public Classifier getClassifier() {
	    return new CustomizableBaggingClassifierRandomSubspace2();
	  }

	  public static Test suite() {
		    return new TestSuite(CustomizableBaggingClassifierRandomSubspace2Test.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }
	

}
