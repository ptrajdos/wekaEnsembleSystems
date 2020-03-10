package weka.classifiers.meta.generalOutputCombiners;

public class MaxCombinerTest extends GeneralCombinerTest {

	@Override
	public OutputCombiner getCombiner() {
		return new MaxCombiner();
	}

	
}
