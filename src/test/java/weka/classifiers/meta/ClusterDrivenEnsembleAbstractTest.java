package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.AbstractClassifierTest;
import weka.tools.tests.WekaGOEChecker;

public abstract class ClusterDrivenEnsembleAbstractTest extends AbstractClassifierTest {

	public ClusterDrivenEnsembleAbstractTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void testTipTexts() {
		WekaGOEChecker check = new WekaGOEChecker();
		check.setObject(this.getClassifier());
		if(check.checkGlobalInfo())
			assertTrue("Global Info call", check.checkCallGlobalInfo());
		
		if(check.checkToolTips())
			assertTrue("Tip Texts call", check.checkToolTipsCall());
	}
		  
	  
	  public static Test suite() {
		    return new TestSuite(ClusterDrivenEnsembleAbstractTest.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }

	

}
