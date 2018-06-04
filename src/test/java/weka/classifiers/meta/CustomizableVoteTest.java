package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;

public class CustomizableVoteTest extends VoteTest{

	 public CustomizableVoteTest(String name) {
		super(name);
	}

	/** Creates a default Classifier */
	  public Classifier getClassifier() {
	    return new CustomizableVote();
	  }

	  public static Test suite() {
	    return new TestSuite(CustomizableVoteTest.class);
	  }

	  public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }
}
