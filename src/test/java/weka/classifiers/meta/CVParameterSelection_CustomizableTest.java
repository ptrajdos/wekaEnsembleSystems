package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.cvParameterSelectionCustomizable.QualityCalculator;
import weka.tools.tests.WekaGOEChecker;

public class CVParameterSelection_CustomizableTest extends CVParameterSelectionTest  {

	 public CVParameterSelection_CustomizableTest(String name) {
		super(name);
	}

	/** Creates a default Classifier */
	  public Classifier getClassifier() {
		  
		   CVParameterSelection_Customizable custClassifier = new CVParameterSelection_Customizable();
		  	IBk ibk = new IBk();
			
			String KOptString = "K 1 11 6";
			
			custClassifier.setClassifier(ibk);
			custClassifier.setDebug(true);
			try {
				custClassifier.addCVParameter(KOptString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
	    return custClassifier;
	  }
	  
	  public void testTipTexts() {
			WekaGOEChecker check = new WekaGOEChecker();
			check.setObject(this.getClassifier());
			if(check.checkGlobalInfo())
				assertTrue("Global Info call", check.checkCallGlobalInfo());
			
			if(check.checkToolTips())
				assertTrue("Tip Texts call", check.checkToolTipsCall());
		}
	  
	  public void testQualityCalc() {
		  CVParameterSelection_Customizable custClass = (CVParameterSelection_Customizable) this.getClassifier();
		  QualityCalculator qcalc = custClass.getQualitCalc();
		  assertTrue("Quality Calc not null", qcalc !=null);
	  }

	  public static Test suite() {
	    return new TestSuite(CVParameterSelection_CustomizableTest.class);
	  }

	  public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }

}
