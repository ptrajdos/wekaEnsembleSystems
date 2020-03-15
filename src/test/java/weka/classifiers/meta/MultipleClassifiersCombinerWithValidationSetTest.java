package weka.classifiers.meta;

import java.util.List;

import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.DistributionChecker;
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
		classifier.setClassifiers(new Classifier[] {new J48(),new NaiveBayes(), new IBk()});
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
	



}
