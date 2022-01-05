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


}
