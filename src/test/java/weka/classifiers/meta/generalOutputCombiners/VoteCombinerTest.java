package weka.classifiers.meta.generalOutputCombiners;

public class VoteCombinerTest extends GeneralCombinerTest {

	@Override
	public OutputCombiner getCombiner() {
		return new VoteCombiner();
	}



}
