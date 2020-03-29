package weka.classifiers.meta;

import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.RandomSubset;
import weka.tools.SerialCopier;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.WekaGOEChecker;

public class ClassifierWithCopiedDatasetTest extends AbstractClassifierTest {

	public ClassifierWithCopiedDatasetTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		ClassifierWithCopiedDataset cop = new ClassifierWithCopiedDataset();
		return cop;
	}
	
	public void testTipTexts() {
		WekaGOEChecker check = new WekaGOEChecker();
		check.setObject(this.getClassifier());
		if(check.checkGlobalInfo())
			assertTrue("Global Info call", check.checkCallGlobalInfo());
		
		if(check.checkToolTips())
			assertTrue("Tip Texts call", check.checkToolTipsCall());
	}
	
	public void testCopied() {
		ClassifierWithCopiedDataset cop = new ClassifierWithCopiedDataset();
		RandomSubset rs = new RandomSubset();
		rs.setNumAttributes(0.5);
		FilteredClassifier fClass = new FilteredClassifier();
		fClass.setFilter(rs);
		cop.setClassifier(fClass);
		
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		try {
			Instances dataCopy = (Instances) SerialCopier.makeCopy(data);
			cop.buildClassifier(data);
			assertTrue("Equall headers",data.equalHeaders(dataCopy) );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	

}
