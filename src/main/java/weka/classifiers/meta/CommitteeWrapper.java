/**
 * 
 */
package weka.classifiers.meta;

import java.lang.reflect.Field;

import weka.classifiers.Classifier;
import weka.classifiers.SingleClassifierEnhancer;
import weka.classifiers.meta.committees.Committee;
import weka.classifiers.meta.tools.CommitteeResponseExtractor;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Wrapper class for classifiers that may contain Committee classifier.
 * @author pawel trajdos
 * @since 1.2.0
 * @version 1.2.0
 *
 */
public class CommitteeWrapper extends SingleClassifierEnhancer implements Committee {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3648442079856127236L;
	
	
	
	/* (non-Javadoc)
	 * @see weka.classifiers.Classifier#buildClassifier(weka.core.Instances)
	 */
	@Override
	public void buildClassifier(Instances data) throws Exception {
		this.m_Classifier.buildClassifier(data);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.Classifier#classifyInstance(weka.core.Instance)
	 */
	@Override
	public double classifyInstance(Instance instance) throws Exception {
		return this.m_Classifier.classifyInstance(instance);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.Classifier#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		return this.m_Classifier.distributionForInstance(instance);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.Classifier#getCapabilities()
	 */
	@Override
	public Capabilities getCapabilities() {
		return this.m_Classifier.getCapabilities();
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.committees.Committee#distributionForInstanceCommittee(weka.core.Instance)
	 */
	@Override
	public double[][] distributionForInstanceCommittee(Instance inst) throws Exception {		
		Classifier[] committee = this.getEnsemble();
		 return CommitteeResponseExtractor.distributionsForInstanceCommittee(committee, inst);

	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.committees.Committee#classifyInstanceCommittee(weka.core.Instance)
	 */
	@Override
	public double[] classifyInstanceCommittee(Instance inst) throws Exception {
		Classifier[] committee = this.getEnsemble();
		return CommitteeResponseExtractor.classifyCommitee(committee, inst);
	}

		
	protected Classifier[] getEnsemble()throws Exception {
		Classifier[] models = null;
		Class cl = this.m_Classifier.getClass();
		while(cl != null) {
			try {
				
				Field f = cl.getDeclaredField("m_Classifiers");
				f.setAccessible(true);
				models = (Classifier[]) f.get(this.m_Classifier);
				return models;
			}
			catch(NoSuchFieldException e ) {cl = cl.getSuperclass();}
			catch(SecurityException e) {cl = cl.getSuperclass();}
			catch(Exception e){
				throw e;
			}
		}
		models = new Classifier[] {this.m_Classifier};
		return models;
		
	}
	
	public String globalInfo() {
		return "Committee Wrapper for wrapping different classifiers.";
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		runClassifier(new CommitteeWrapper(), args);

	}

}
