package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.core.RevisionHandler;
import weka.tools.tests.WekaGOEChecker;

public class CustomizableRandomSubspaceClassifierTest extends RandomSubSpaceTest {

	public CustomizableRandomSubspaceClassifierTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Classifier getClassifier() {
		return new CustomizableRandomSubspaceClassifier();
	}
	
	 public static Test suite() {
		    return new TestSuite(CustomizableRandomSubspaceClassifierTest.class);
		  }

		  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
		  }
		  
		  public void testRevision() {
			  Classifier classifier = this.getClassifier();
			  if(classifier instanceof RevisionHandler) {
				  String revision = ((RevisionHandler) classifier).getRevision();
				  assertTrue("Revision not null", revision !=null);
				  assertTrue("Revision Length", revision.length() >0);
			  }else {
				  fail("No revision handler");
			  }
		  }
		  
		  public void testTipTexts() {
			  WekaGOEChecker check = new WekaGOEChecker();
			  check.setObject(this.getClassifier());
			  assertTrue("Call tip texts", check.checkToolTipsCall());
		  }



}
