package weka.classifiers.meta;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.WekaGOEChecker;

public class RandomSubSpaceClassifierTunerTest extends SimpleSingleClassifierEnhancerTest {

	public RandomSubSpaceClassifierTunerTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		return new RandomSubSpaceClassifierTuner();
	}

	public void testGlobalInfo() {
		WekaGOEChecker check = new WekaGOEChecker();
		check.setObject(this.getClassifier());
		
		assertTrue("Check Global info", check.checkGlobalInfo());
		assertTrue("Call Global Info", check.checkCallGlobalInfo());
		
		assertTrue("Check tip text", check.checkToolTips());
		assertTrue("Check call tip text", check.checkToolTipsCall());
	}
	
	public void testWrongClassifier() {
		RandomSubSpaceClassifierTuner tuner = (RandomSubSpaceClassifierTuner) this.getClassifier();
		tuner.setClassifier(new J48());
		RandomDataGenerator gen = new RandomDataGenerator();
		
		Instances data = gen.generateData();
		
		try {
			tuner.buildClassifier(data);
			fail("Building should have failed");
		} catch (Exception e) {
			//Nothing to do
		}
	}
	
	public void testTunedParameters() {
		
		double beta=0.5;
		double k = 2.0;
		int numAttrs=1000;
		int numInsts=100;
		
		RandomSubSpaceClassifierTuner tuner = (RandomSubSpaceClassifierTuner) this.getClassifier();
		tuner.setAttribFraction(beta);
		tuner.setOverproductRate(k);
		
		
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setNumNumericAttributes(numAttrs);
		gen.setNumObjects(numInsts);
		
		Instances data = gen.generateData();
		
		try {
			tuner.buildClassifier(data);
			
			RandomSubSpace rssClassifier = (RandomSubSpace) tuner.getClassifier();
			assertEquals("Attrib fraction test",beta*numInsts/numAttrs, rssClassifier.getSubSpaceSize(), 1E-3);
			assertEquals("Overproduce rate",numInsts*k/beta, rssClassifier.getNumIterations(), 1E-3);
			
			
		} catch (Exception e) {
			fail("An exception has been caught: " + e);
		}
		
	}


}
