package weka.classifiers.meta;

import java.awt.geom.GeneralPath;


import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Capabilities;
import weka.core.Instances;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.RandomDataChecker;
import weka.tools.tests.WekaGOEChecker;

public class ClassifierWithAlternativeModelTest extends AbstractClassifierTest {

	

	public ClassifierWithAlternativeModelTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		ClassifierWithAlternativeModel alt = new ClassifierWithAlternativeModel();
		alt.setClassifier(new J48());
		return alt;
	}
	
	
	public void testAgainstRandomData() {
		 Classifier altModel =  this.getClassifier();
		 assertTrue("Total Random data", RandomDataChecker.checkAgainstRandomData(altModel, -0.1, 0.1));
		 assertTrue("Test, Well-separated data", RandomDataChecker.checkAgainstWellSeparatedData(altModel, 0.9));
	 }
	
	public void testTipTexts() {
		WekaGOEChecker check = new WekaGOEChecker();
		check.setObject(this.getClassifier());
		if(check.checkGlobalInfo())
			assertTrue("Global Info call", check.checkCallGlobalInfo());
		
		if(check.checkToolTips())
			assertTrue("Tip Texts call", check.checkToolTipsCall());
	}
	
	public void testCapabilities() {
		ClassifierWithAlternativeModel classifier  = (ClassifierWithAlternativeModel) this.getClassifier();
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setNumObjects(10);
		Instances data = gen.generateData();
		
		try {
			classifier.buildClassifier(data);
			Capabilities caps = classifier.getCapabilities();
			assertTrue("Capabilities not null", caps !=null);
		} catch (Exception e) {
			fail("Capabilities. Exception has been caught");
		}
		
		
		
		
		
	}

}
