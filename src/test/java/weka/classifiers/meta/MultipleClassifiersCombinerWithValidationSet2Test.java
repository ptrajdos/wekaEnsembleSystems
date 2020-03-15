/**
 * 
 */
package weka.classifiers.meta;

import weka.classifiers.Classifier;

/**
 * @author pawel
 *
 */
public class MultipleClassifiersCombinerWithValidationSet2Test
		extends MultipleClassifiersCombinerWithValidationSetTest {

	/**
	 * @param name
	 */
	public MultipleClassifiersCombinerWithValidationSet2Test(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Classifier getClassifier() {
		MultipleClassifiersCombinerWithValidationSet comb =(MultipleClassifiersCombinerWithValidationSet) super.getClassifier();
		comb.setCrossvalidate(true);
		return comb;
				
	}
	
	

}
