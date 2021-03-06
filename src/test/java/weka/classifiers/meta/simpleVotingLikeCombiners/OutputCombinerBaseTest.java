package weka.classifiers.meta.simpleVotingLikeCombiners;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.RandomSubSpace;
import weka.classifiers.meta.Stacking;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.OptionHandlersTest.OptionHandlerTest;
import weka.core.RevisionHandler;
import weka.tools.SerialCopier;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.DistributionChecker;
import weka.tools.tests.WekaGOEChecker;

public abstract class OutputCombinerBaseTest extends OptionHandlerTest {

	public OutputCombinerBaseTest(String name, String classname) {
		super(name, classname);
	}
	
	public OutputCombinerBaseTest(String name) {
		super(name, OutputCombinerBase.class.getCanonicalName());
	}
	
	public void testSerialization() {
		OutputCombiner comb = (OutputCombiner) this.getOptionHandler();
		try {
			OutputCombiner comb2 = (OutputCombiner) SerialCopier.makeCopy(comb);
		} catch (Exception e) {
			fail("An exception has been caught:" + e.getLocalizedMessage());
		}
	}
	
	public void testTipsCalls() {
		WekaGOEChecker goe = new WekaGOEChecker();
		goe.setObject(this.getOptionHandler());
		assertTrue("Tip Text calls", goe.checkToolTipsCall());
	}
	
	public void testRavision() {
		OptionHandler opt = this.getOptionHandler();
		if(opt instanceof RevisionHandler) {
			String revision = ((RevisionHandler) opt).getRevision();
			assertTrue("Not null Revision", revision !=null);
			assertTrue("Revision length ", revision.length()>0);
		}
	}
	
	public void testGetMultipleDistr() {
		Vote vote = new Vote();
		Classifier[] baseClassifiers = {new J48(), new NaiveBayes()};
		vote.setClassifiers(baseClassifiers);
		
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances insts = gen.generateData();
		Instance testInstance = insts.get(0);
		
		try {
			vote.buildClassifier(insts);
			OutputCombiner comb = (OutputCombiner) this.getOptionHandler();
			
			
			
			double[] distr = comb.getDistributionForInstance(vote, testInstance);
			assertTrue("Num classes distr", distr.length == insts.numClasses());
			assertTrue("Distribution properties", this.checkDistribution(distr));
			
			
		} catch (Exception e) {
			fail("Exception has been caught" + e.toString());
		}
		
		
		
	}
	
	public void testGetMultipleNumeric() {
		Stacking stacking = new Stacking();
		Classifier[] baseClassifiers = {new LinearRegression(), new LinearRegression()};
		stacking.setClassifiers(baseClassifiers);
		
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setAddClassAttrib(false);
		gen.setNumNominalAttributes(0);
		
		Instances insts = gen.generateData();
		insts.setClassIndex(0);
		Instance testInstance = insts.get(0);
		
		try {
			stacking.buildClassifier(insts);
			OutputCombiner comb = (OutputCombiner) this.getOptionHandler();
			
			double gClass = comb.getClass(stacking, testInstance);
			
						
			
		} catch (Exception e) {
			fail("Exception has been caught" + e.toString());
		}
	}
	
	public void testIteratedSingleDistribution(){
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data  = gen.generateData();
		Instance testInstance = data.get(0);
		
		Bagging bag = new Bagging();
		RandomSubSpace rs = new RandomSubSpace();
		
		try {
			bag.buildClassifier(data);
			rs.buildClassifier(data);
			OutputCombiner comb = (OutputCombiner) this.getOptionHandler();
			
			double[] distribution = comb.getDistributionForInstance(bag, testInstance);
			assertTrue("Check distribution", DistributionChecker.checkDistribution(distribution));
			
			distribution = comb.getDistributionForInstance(rs, testInstance);
			assertTrue("Check distribution", DistributionChecker.checkDistribution(distribution));
			
			
		} catch (Exception e) {
			fail("An exception has been caught!" + e.getMessage());
		}
		
	}
	
	
	public boolean compareClassifierArrays(Classifier[] a, Classifier[] b) {
		if(a.length != b.length)
			return false;
		
		for(int i=0;i<a.length;i++) 
			if(! a[i].getClass().equals(b[i].getClass()))
				return false;
		
		return true;
	}

	public boolean checkDistribution(double[] dist) {
		return DistributionChecker.checkDistribution(dist);
	}
	

}
