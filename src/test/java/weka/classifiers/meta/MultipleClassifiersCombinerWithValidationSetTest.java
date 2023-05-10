package weka.classifiers.meta;

import java.util.List;

import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.DistributionChecker;
import weka.tools.tests.SerializationChecker;
import weka.tools.tests.WekaGOEChecker;

public class MultipleClassifiersCombinerWithValidationSetTest extends AbstractClassifierTest {

	public MultipleClassifiersCombinerWithValidationSetTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		/*
		 * For testing classifier 
		 */
		
		MultipleClassifiersCombinerWithValidationSet classifier =new MultipleClassifierCombinerWithValidationSetDummy(); 
		ClassifierWithAlternativeModel alt = new ClassifierWithAlternativeModel();
		alt.setClassifier(new NaiveBayes());
		classifier.setClassifiers(new Classifier[] {new J48(),new NaiveBayes(), new IBk(),alt});
		return classifier;
	}
	
	
	
	public void testTipTexts() {
		WekaGOEChecker check = new WekaGOEChecker();
		check.setObject(this.getClassifier());
		if(check.checkGlobalInfo())
			assertTrue("Global Info call", check.checkCallGlobalInfo());
		
		if(check.checkToolTips())
			assertTrue("Tip Texts call", check.checkToolTipsCall());
	}
	
	public void testCrossvalidate() {
		MultipleClassifiersCombinerWithValidationSet obj = (MultipleClassifiersCombinerWithValidationSet) this.getClassifier();
		
		obj.setCrossvalidate(false);
		assertFalse("No Crossvalidation", obj.isCrossvalidate());
		obj.setCrossvalidate(true);
		assertTrue("Set Crossvalidation", obj.isCrossvalidate());
		assertTrue("Options", obj.getOptions()!=null);
		
	}
	
	
	public void testValidationSet() {
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		MultipleClassifiersCombinerWithValidationSet comb = (MultipleClassifiersCombinerWithValidationSet) this.getClassifier();
		
		try {
			comb.buildClassifier(data);
			Instances valSet = comb.validationSet;
			assertTrue("Not null", valSet!=null);
			assertTrue("Num Instances", valSet.numInstances()>0);
			
			List<double[]>[] valResponses = comb.validationResponses;
			assertTrue("Responses not null", valResponses!=null);
			assertTrue("Num Classifiers", valResponses.length == comb.getClassifiers().length);
			for(int i=0;i<valResponses.length;i++) {
				assertTrue("Num of registered responses", valResponses[i].size() == valSet.numInstances());
				for(int r=0;r<valResponses[i].size();r++) {
					double[] resp = valResponses[i].get(r);
					assertTrue("Num of registered classes", resp.length == data.numClasses());
					assertTrue("Response validity", DistributionChecker.checkDistribution(resp));
				}
			}
		} catch (Exception e) {
			fail("Validation set test. Exception caught: " + e.toString());
		}
		
	}
	
	public void testSplits() {
		MultipleClassifiersCombinerWithValidationSet comb = (MultipleClassifiersCombinerWithValidationSet) this.getClassifier();
		checkSplits(comb, 10, 0.0);
		checkSplits(comb, 10, 1.0);
		checkSplits(comb, 10, 0.5);
	}

	public void testSerialization(){
	  assertTrue("Serialization check", SerializationChecker.checkSerializationCopy(this.getClassifier()));
	}
	
	public void checkSplits(MultipleClassifiersCombinerWithValidationSet comb, int numInstances, double splitFactor) {
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setNumObjects(numInstances);
		comb.setSplitFactor(splitFactor);
		
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		
		try {
			comb.buildClassifier(data);
			double[] distribution = comb.distributionForInstance(testInstance);
			assertTrue("Check distribution",  DistributionChecker.checkDistribution(distribution));
		} catch (Exception e) {
			fail("CheckSplits has failed. num instances: " + numInstances + " split Factor: " + splitFactor + " With exception: " + e.toString());
		}
		
	}
	
	
	



}
