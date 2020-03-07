package weka.classifiers.meta.generalOutputCombiners;

public class MedCombinerTest extends GeneralCombinerTest {

	@Override
	public OutputCombiner getCombiner() {
		return new MedCombiner();
	}



}
