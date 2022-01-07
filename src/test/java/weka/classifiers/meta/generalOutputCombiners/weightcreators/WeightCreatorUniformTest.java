package weka.classifiers.meta.generalOutputCombiners.weightcreators;

public class WeightCreatorUniformTest extends IWeightCreatorTest {

	@Override
	public IWeightCreator getWeightCreator() {
		return new WeightCreatorUniform();
	}


}
