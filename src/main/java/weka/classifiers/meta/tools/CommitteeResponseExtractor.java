/**
 * 
 */
package weka.classifiers.meta.tools;

import java.io.Serializable;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.RevisionHandler;
import weka.core.RevisionUtils;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.1.1
 *
 */
public class CommitteeResponseExtractor implements Serializable, RevisionHandler {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7461930472418720963L;

	public static double[][] distributionsForInstanceCommittee(Classifier[] committee, Instance instance)throws Exception{
		int numModels = committee.length;
		
		double[][] responses = new double[numModels][];
		for(int m=0;m<numModels;m++)
			responses[m] = committee[m].distributionForInstance(instance);
		
		return responses;
	}
	public static double[] classifyCommitee(Classifier[] committee, Instance instance)throws Exception{
		int numModels = committee.length;
		double[] classes = new double[numModels];
		for(int m=0;m<numModels;m++){
			classes[m] = committee[m].classifyInstance(instance);
		}
		return classes;
	}

	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1.1.1$");
	}

}
