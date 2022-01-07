package weka.classifiers.meta.generalOutputCombiners;

import java.util.Arrays;

import junit.framework.TestCase;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.tools.SerialCopier;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.DistributionChecker;
import weka.tools.tests.OptionHandlerChecker;
import weka.tools.tests.WekaGOEChecker;

public abstract class OutputCombinerTest extends TestCase{

	public abstract OutputCombiner getCombiner();
	
	public void testSerialization() {
		
		try {
			OutputCombiner outC = (OutputCombiner) SerialCopier.makeCopy(this.getCombiner());
		} catch (Exception e) {
			fail("Exception has been caught " + e.toString());
		}
	}
	
	public void testCombinedDistributionForInstance() {
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		
		Classifier[] committee = {new J48(), new NaiveBayes(), new J48()};
		OutputCombiner comb = this.getCombiner();
		try {
		for(int i=0;i<committee.length;i++)
			committee[i].buildClassifier(data);
		
		double[][] rawPredictions = new double[committee.length][];
		for(int i=0;i<rawPredictions.length;i++) {
			rawPredictions[i] = committee[i].distributionForInstance(testInstance);
		}
		
		double[] combinedDist = comb.getCombinedDistributionForInstance(committee, testInstance);
		assertTrue("Combined Distribution check", DistributionChecker.checkDistribution(combinedDist));
		
		double[] weights = new double[committee.length];
		Arrays.fill(weights, 1.0);
		Utils.normalize(weights);
		
		combinedDist = comb.getCombinedDistributionForInstance(committee, testInstance, weights);
		assertTrue("Combined Distribution check", DistributionChecker.checkDistribution(combinedDist));
		
		combinedDist = comb.getCombinedDistributionForInstance(rawPredictions, testInstance);
		assertTrue("Combined Distribution check", DistributionChecker.checkDistribution(combinedDist));
		
		combinedDist = comb.getCombinedDistributionForInstance(rawPredictions, testInstance,weights);
		assertTrue("Combined Distribution check", DistributionChecker.checkDistribution(combinedDist));
		
		
		}catch(Exception e) {
			fail("Exception has been found: " + e.toString());
		}
	}
	
	public void testZeroWeights() {
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		
		Classifier[] committee = {new J48(), new NaiveBayes(), new J48()};
		OutputCombiner comb = this.getCombiner();
		
		try {
		for(int i=0;i<committee.length;i++)
			committee[i].buildClassifier(data);
		
		double[][] rawPredictions = new double[committee.length][];
		for(int i=0;i<rawPredictions.length;i++) {
			rawPredictions[i] = committee[i].distributionForInstance(testInstance);
		}
		
		double[] weights = new double[committee.length];
		Arrays.fill(weights, 0.0);
		
		double[] combinedDist  = comb.getCombinedDistributionForInstance(committee, testInstance,weights);
		assertTrue("Zero weights should result into zero distribution", Utils.eq(Utils.sum(combinedDist),0.0) );
		
		combinedDist = comb.getCombinedDistributionForInstance(rawPredictions, testInstance, weights);
		assertTrue("Zero weights should result into zero distribution", Utils.eq(Utils.sum(combinedDist),0.0) );
		
		}catch(Exception e) {
			fail("Zero weights. Exception has been caught: " + e.toString());
		}
	 
	}
	
	public void testZeroResponses() {
		OutputCombiner comb = this.getCombiner();
		int numClassifiers=3;
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		int numClasses=data.numClasses();
		double[][] zeroResonse = new double[numClassifiers][numClasses];
		
		
		try {
			double[] distribution = comb.getCombinedDistributionForInstance(zeroResonse, testInstance);
			assertTrue("Zero distribution", Utils.eq(Utils.sum(distribution), 0.0));
		} catch (Exception e) {
			fail("Zero Response. Exception has been caught: " + e.toString());
		}
		
	}
	
	public void testOptions() {
		OutputCombiner comb = this.getCombiner();
		
		if(comb instanceof OptionHandler) {
			OptionHandler hand = (OptionHandler) comb;
			OptionHandlerChecker.checkOptions(hand);
			
			WekaGOEChecker goe = new WekaGOEChecker();
			goe.setObject(hand);
			
			
			assertTrue("Check tooltips",goe.checkToolTips());
			assertTrue("Check tooltips call",goe.checkToolTipsCall());
			
		}
	}

}
