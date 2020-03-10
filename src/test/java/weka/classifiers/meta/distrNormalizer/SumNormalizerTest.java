package weka.classifiers.meta.distrNormalizer;

public class SumNormalizerTest extends INormalizerTest {

	@Override
	public INormalizer getNormalizer() {
		return new SumNormalizer();
	}



}
