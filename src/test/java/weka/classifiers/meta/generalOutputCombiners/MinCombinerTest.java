package weka.classifiers.meta.generalOutputCombiners;

public class MinCombinerTest extends GeneralCombinerTest {

	@Override
	public OutputCombiner getCombiner() {
		return new MinCombiner();
	}


}
