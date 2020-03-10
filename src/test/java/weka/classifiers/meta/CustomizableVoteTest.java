package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.tools.tests.RevisionChecker;
import weka.tools.tests.WekaGOEChecker;

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
	  
	  public void testRevisionHandler() {
		  assertTrue("Check revision", RevisionChecker.checkRevision(this.getClassifier()));
	  }
	  
	  public void testTipTexts() {
		  WekaGOEChecker check = new WekaGOEChecker();
		  check.setObject(this.getClassifier());
		  assertTrue("Check Tip Texts", check.checkToolTipsCall());
	  }
	  
	  
}
