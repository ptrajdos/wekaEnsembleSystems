package weka.classifiers.meta.generalOutputCombiners;

public class WeightCreatingCombinerTest extends OutputCombinerTest {

	@Override
	public OutputCombiner getCombiner() {
		return new WeightCreatingCombiner();
	}


}
