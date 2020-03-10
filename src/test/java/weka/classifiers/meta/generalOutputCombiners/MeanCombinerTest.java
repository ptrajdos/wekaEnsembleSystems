package weka.classifiers.meta.generalOutputCombiners;

public class MeanCombinerTest extends GeneralCombinerTest {

	@Override
	public OutputCombiner getCombiner() {
		return new MeanCombiner();
	}

	

}
