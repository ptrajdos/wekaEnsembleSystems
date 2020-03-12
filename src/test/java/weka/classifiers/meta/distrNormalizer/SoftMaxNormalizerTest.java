package weka.classifiers.meta.distrNormalizer;

public class SoftMaxNormalizerTest extends INormalizerTest {

	@Override
	public INormalizer getNormalizer() {
		return new SoftMaxNormalizer();
	}

	

}
