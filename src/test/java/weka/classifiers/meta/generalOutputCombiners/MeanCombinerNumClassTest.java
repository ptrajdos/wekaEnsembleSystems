package weka.classifiers.meta.generalOutputCombiners;

public class MeanCombinerNumClassTest extends GeneralCombinerNumClassTest {

	@Override
	public OutputCombinerNumClass getCombiner() {
		return new MeanCombinerNumClass();
	}

	

}
