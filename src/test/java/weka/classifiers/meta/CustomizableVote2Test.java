/**
 * 
 */
package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;

/**
 * @author pawel trajdos
 *
 */
public class CustomizableVote2Test extends CustomizableVoteTest {

	/**
	 * @param name
	 */
	public CustomizableVote2Test(String name) {
		super(name);
	}
	
	  public static Test suite() {
		    return new TestSuite(CustomizableVote2Test.class);
		  }

		  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
		  }

	@Override
	public Classifier getClassifier() {
		CustomizableVote vot = new CustomizableVote();
		vot.setUseCombiningObject(true);
		return vot;
	}

}
