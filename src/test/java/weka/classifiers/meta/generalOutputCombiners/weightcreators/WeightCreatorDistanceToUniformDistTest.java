package weka.classifiers.meta.generalOutputCombiners.weightcreators;

public class WeightCreatorDistanceToUniformDistTest extends IWeightCreatorTest {

	@Override
	public IWeightCreator getWeightCreator() {
		return new WeightCreatorDistanceToUniformDist();
	}

	
}
