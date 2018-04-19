/**
 * 
 */
package weka.classifiers.meta.customizableBagging;

import java.io.Serializable;

import weka.core.OptionHandler;
import weka.core.RevisionHandler;

/**
 * @author pawel
 *
 */
public abstract class OutputCombinerBase implements OutputCombiner, Serializable, RevisionHandler,OptionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5941704616480304723L;

	/**
	 * 
	 */
	public OutputCombinerBase() {
		// TODO Auto-generated constructor stub
	}


}
