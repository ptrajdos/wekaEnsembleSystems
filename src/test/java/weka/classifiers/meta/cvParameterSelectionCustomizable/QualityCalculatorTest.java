package weka.classifiers.meta.cvParameterSelectionCustomizable;

import junit.framework.TestCase;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.output.prediction.XML;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.tools.data.RandomDataGenerator;

public abstract class QualityCalculatorTest extends TestCase {


		public abstract QualityCalculator getQualityCalculator(); 
		
		public void testQualityCalculator() {
			RandomDataGenerator gen = new RandomDataGenerator();
			Instances data = gen.generateData();
			Classifier  classifier = new J48();
			
			try {
				Evaluation eval = new Evaluation(data);
				
				
				classifier.buildClassifier(data);
				XML xml = new XML();
				xml.setBuffer(new StringBuffer());
				xml.setHeader(data);
				
				
				eval.evaluateModel(classifier, data, xml);
				QualityCalculator calc = this.getQualityCalculator();
				assertTrue("Quality measure check", checkQuality(calc,eval));
				
				
			} catch (Exception e) {
				fail("Exception has been caught:" + e.toString());
			}
		}
		
		public abstract boolean checkQuality(QualityCalculator calc, Evaluation eval);

}
