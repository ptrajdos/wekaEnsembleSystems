package weka.classifiers.meta.generalOutputCombiners;

public class RandomCombinerTest extends GeneralCombinerTest {

	@Override
	public OutputCombiner getCombiner() {
		return new RandomCombiner();
	}



}
