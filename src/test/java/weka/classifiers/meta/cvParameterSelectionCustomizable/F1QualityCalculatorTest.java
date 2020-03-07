package weka.classifiers.meta.cvParameterSelectionCustomizable;

import weka.classifiers.Evaluation;

public class F1QualityCalculatorTest extends QualityCalculatorTest {

	@Override
	public QualityCalculator getQualityCalculator() {
		return new F1QualityCalculator();
	}

	@Override
	public boolean checkQuality(QualityCalculator calc, Evaluation eval) {
		double quality = calc.getQuality(eval);
		
		if(quality <0 | quality >1)
			return false;
		
		return true;
	}

	

}
