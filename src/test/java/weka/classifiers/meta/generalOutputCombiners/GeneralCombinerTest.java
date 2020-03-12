package weka.classifiers.meta.generalOutputCombiners;

import java.util.Arrays;

import junit.framework.TestCase;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionHandler;
import weka.core.Utils;
import weka.tools.SerialCopier;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.DistributionChecker;

public abstract class GeneralCombinerTest extends TestCase {

	public abstract OutputCombiner getCombiner();
	
	public void testSerialization() {
		
		try {
			OutputCombiner outC = (OutputCombiner) SerialCopier.makeCopy(this.getCombiner());
		} catch (Exception e) {
			fail("Exception has been caught " + e.toString());
		}
	}
	
	public void testRevision() {
		OutputCombiner comb = this.getCombiner();
		if(comb instanceof RevisionHandler) {
			String revision = ((RevisionHandler) comb).getRevision();
			assertTrue("Revision not null", revision !=null);
			assertTrue("Revision string length", revision.length()>0);
		}
	}
	
	public void testCompatibility() {
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		int numClasses = data.numClasses();
		
		GeneralCombiner comb = (GeneralCombiner) this.getCombiner();
		
		int numPredictors =10;
		double[][] rawPreds = new double[10][numClasses];
		double[] weights = new double[numPredictors];
		
		try {
			comb.checkCompatibility(rawPreds, testInstance);
			comb.checkCompatibility(rawPreds, testInstance, weights);
		}catch(Exception e) {
			fail("check compatibility Failed:"  + e.toString());
		}
		
		try {
			comb.checkCompatibility(rawPreds, testInstance,new double[numPredictors+1]);
			fail("Invalid weights no exception");
		} catch (Exception e) {
			// OK
		}
		
		try {
			comb.checkCompatibility(new double[numPredictors][numClasses+1], testInstance);
			fail("Invalid raw predictions. No exception has been thrown");
		} catch (Exception e) {
			//OK
		}
		
		try {
			comb.checkCompatibility(new double[numPredictors][numClasses+1], testInstance, weights);
			fail("Testi instance and raw predictions incompatible. No exception has been thrown.");
		} catch (Exception e) {
			//OK
		}
		
	}
	
	public void testCombinedDistributionForInstance() {
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		
		Classifier[] committee = {new J48(), new NaiveBayes(), new J48()};
		GeneralCombiner comb = (GeneralCombiner) this.getCombiner();
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
		GeneralCombiner comb = (GeneralCombiner) this.getCombiner();
		
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
		GeneralCombiner comb = (GeneralCombiner) this.getCombiner();
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
	
	public void testNormalizaOutput() {
		GeneralCombiner comb = (GeneralCombiner) this.getCombiner();
		int[] numClasses = {1,2,3,10};
		for(int i=0;i<numClasses.length;i++) {
			double[] properDist = new double[numClasses[i]];
			Arrays.fill(properDist, 1.0);
			
			comb.normalizeOutput(properDist);
			assertTrue("Normalization prperties", DistributionChecker.checkDistribution(properDist));
			
			double[] zeroDist = new double[numClasses[i]];
			Arrays.fill(zeroDist, 0.0);
			comb.normalizeOutput(zeroDist);
			assertTrue("Normalization prperties", DistributionChecker.checkDistribution(zeroDist));
			
			
		}
	}
	
}
