package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;

public class CVParameterSelection_CustomizableTest extends CVParameterSelectionTest  {

	 public CVParameterSelection_CustomizableTest(String name) {
		super(name);
	}

	/** Creates a default Classifier */
	  public Classifier getClassifier() {
	    return new CVParameterSelection_Customizable();
	  }

	  public static Test suite() {
	    return new TestSuite(CVParameterSelection_CustomizableTest.class);
	  }

	  public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }

}
