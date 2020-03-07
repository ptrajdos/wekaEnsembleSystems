package weka.classifiers.meta.generalOutputCombiners;

import static org.junit.Assert.*;

import org.junit.Test;

public class MinCombinerTest extends GeneralCombinerTest {

	@Override
	public OutputCombiner getCombiner() {
		return new MinCombiner();
	}


}
