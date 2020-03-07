package weka.classifiers.meta.cvParameterSelectionCustomizable;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.classifiers.Evaluation;

public class ErrorRateQualityCalculatorTest extends QualityCalculatorTest {

	@Override
	public QualityCalculator getQualityCalculator() {
		return new ErrorRateQualityCalculator();
	}

	@Override
	public boolean checkQuality(QualityCalculator calc, Evaluation eval) {
		double qual = calc.getQuality(eval);
		if(qual<0 | qual >1)
			return false;
		return true;
	}

	

}
