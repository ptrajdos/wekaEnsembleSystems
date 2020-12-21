package weka.classifiers.meta.generalOutputCombiners;

public class RandomCombinerTest extends GeneralCombinerTest {

	@Override
	public OutputCombiner getCombiner() {
		return new RandomCombiner();
	}
	
	public void testSeeds() {
		RandomCombiner rndComb = (RandomCombiner) this.getCombiner();
		int seed = 1;
		rndComb.setSeed(seed);
		assertTrue("Seed setting", seed == rndComb.getSeed());
		
	}



}
