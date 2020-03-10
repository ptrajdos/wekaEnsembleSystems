package weka.classifiers.meta.distrNormalizer;

import static org.junit.Assert.*;

import org.junit.Test;

public class SoftMaxNormalizerTest extends INormalizerTest {

	@Override
	public INormalizer getNormalizer() {
		return new SoftMaxNormalizer();
	}

	

}
