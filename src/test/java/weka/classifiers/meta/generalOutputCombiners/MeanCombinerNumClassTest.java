package weka.classifiers.meta.generalOutputCombiners;

import static org.junit.Assert.*;

import org.junit.Test;

public class MeanCombinerNumClassTest extends GeneralCombinerNumClassTest {

	@Override
	public OutputCombinerNumClass getCombiner() {
		return new MeanCombinerNumClass();
	}

	

}
