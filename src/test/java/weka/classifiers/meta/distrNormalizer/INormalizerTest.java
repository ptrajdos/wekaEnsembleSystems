package weka.classifiers.meta.distrNormalizer;

import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;
import weka.core.Utils;

public abstract class INormalizerTest extends TestCase {

	public abstract INormalizer getNormalizer();
	
	public void testNormalizer() {
		INormalizer normalizer = this.getNormalizer();
		
		int[] seeds = {1,2,3,4};
		int[] lengths= {1,2,3,10};
		for(int s=0;s<seeds.length;s++)
			for(int l=0;l<lengths.length;l++) {
				double[] preDist = this.getTestData(seeds[s], lengths[l]);
				double[] norm = normalizer.normalize(preDist);
				
				assertTrue("Distribution  [ seed " + s + " len " + l + " ] " , this.checkDistribution(norm));
			}
		
	}
	
	public void testZeroData() {
		
		int[] lengths= {1,2,3};
		for(int i=0;i<lengths.length;i++) {
			double[] preDist = this.generateZeroData(lengths[i]);
			double[] dist = this.getNormalizer().normalize(preDist);
			assertTrue("Denormalized Distribution: ", dist!=null);
		}
	}
	
	
	public double[] getTestData(int seed, int numVals) {
		Random rnd = new Random(seed);
		double[] values = new double[numVals];
		for(int i=0;i<values.length;i++) {
			values[i] = rnd.nextDouble();
		}
		
		return values;
	}
	
	public double[] generateZeroData(int numVals) {
		double[] vals = new double[numVals];
		Arrays.fill(vals, 0.0);
		return vals;
	}
	
	public boolean checkDistribution(double[] distribution) {
		
		if(distribution == null)
			return false;
		
		double sum =Utils.sum(distribution); 
		if( !Utils.eq(sum, 1.0) )
			return false;
		
		for(int i=0;i<distribution.length;i++)
			if(distribution[i]<0 | distribution[i]>1)
				return false;
				
		return true;
	}

}
