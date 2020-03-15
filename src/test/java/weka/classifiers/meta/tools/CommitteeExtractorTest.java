package weka.classifiers.meta.tools;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import weka.classifiers.Classifier;
import weka.classifiers.MultipleClassifiersCombiner;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.tools.data.RandomDataGenerator;

public class CommitteeExtractorTest {

	@Test
	public void test() {
		CommitteeExtractor extr = new CommitteeExtractor();
		assertTrue("Constructor test!", extr!=null);
		
		MultipleClassifiersCombiner combiner = new Vote();
		Classifier[] baseClassifiers =new Classifier[]{new J48(), new NaiveBayes()}; 
		combiner.setClassifiers(baseClassifiers);
		
		RandomDataGenerator dataGen = new RandomDataGenerator();
		dataGen.setAddClassAttrib(true);
		Instances testData = dataGen.generateData();
		try {
			combiner.buildClassifier(testData);
			Classifier[] classifiers = extr.getCommittee(combiner);
			assertTrue("Not null", classifiers != null);
			assertTrue("Num Classifiers", classifiers.length==2);
			this.compareClassifierClasses(baseClassifiers, classifiers);
			
		} catch (Exception e) {
			fail("Exception has been caught: " + e.toString());
		}
	}
	
	public boolean compareClassifierClasses(Classifier[] a, Classifier[] b) {
		
		if(a.length != b.length)
			return false;
		
		for(int i=0;i<a.length;i++) 
			if(!a[i].getClass().equals(b[i].getClass()))
				return false;
		
		return true;
	}

}
