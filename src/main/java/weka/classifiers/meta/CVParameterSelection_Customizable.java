/**
 * 
 */
package weka.classifiers.meta;

import java.lang.reflect.Field;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.meta.cvParameterSelectionCustomizable.ErrorRateQualityCalculator;
import weka.classifiers.meta.cvParameterSelectionCustomizable.QualityCalculator;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;

/**
 * @author pawel
 *
 */
public class CVParameterSelection_Customizable extends CVParameterSelection {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3078263958163166348L;
	
	protected QualityCalculator qualitCalc = new ErrorRateQualityCalculator(); 
	/**
	 * 
	 */
	public CVParameterSelection_Customizable() {
		super();
	}
	
	/**
	 * Finds the best parameter combination. (recursive for each parameter
	 * being optimised).
	 * 
	 * @param depth the index of the parameter to be optimised at this level
	 * @param trainData the data the search is based on
	 * @param random a random number generator
	 * @throws Exception if an error occurs
	 */
	protected void findParamsByCrossValidation(int depth, Instances trainData,
						     Random random)
	  throws Exception {

	  if (depth < m_CVParams.size()) {
	    CVParameter cvParam = (CVParameter)m_CVParams.elementAt(depth);

	    double upper;
	    switch ((int)(this.getParamLowerBound(cvParam) - this.getParamUpperBound(cvParam) + 0.5)) {
	    case 1:
		upper = m_NumAttributes;
		break;
	    case 2:
		upper = m_TrainFoldSize;
		break;
	    default:
		upper = this.getParamUpperBound(cvParam);
		break;
	    }
	    double increment = (upper - this.getParamLowerBound(cvParam)) / (this.getParamSteps(cvParam) - 1);
	    double upp = this.getParamUpperBound(cvParam);
	    double val = 0;
	    for( val = this.getParamLowerBound(cvParam);val<=upp;val+=increment)
	    	findParamsByCrossValidation(depth + 1, trainData, random);
	    //TODO set Vals
	  } else {
	    
	    Evaluation evaluation = new Evaluation(trainData);

	    // Set the classifier options
	    String [] options = createOptions();
	    if (m_Debug) {
		System.err.print("Setting options for " 
				 + m_Classifier.getClass().getName() + ":");
		for (int i = 0; i < options.length; i++) {
		  System.err.print(" " + options[i]);
		}
		System.err.println("");
	    }
	    ((OptionHandler)m_Classifier).setOptions(options);
	    for (int j = 0; j < m_NumFolds; j++) {

	      // We want to randomize the data the same way for every 
	      // learning scheme.
		Instances train = trainData.trainCV(m_NumFolds, j, new Random(1));
		Instances test = trainData.testCV(m_NumFolds, j);
		m_Classifier.buildClassifier(train);
		evaluation.setPriors(train);
		evaluation.evaluateModel(m_Classifier, test);
	    }
	    double error = this.qualitCalc.getQuality(evaluation);
	    if (m_Debug) {
		System.err.println("Cross-validated error rate: " 
				   + Utils.doubleToString(error, 6, 4));
	    }
	    if ((m_BestPerformance == -99) || (error < m_BestPerformance)) {
		
		m_BestPerformance = error;
		m_BestClassifierOptions = createOptions();
	    }
	  }
	}
		
	protected Class getCVParameterClass(){
		Class class1=null;
		try {
			class1 = Class.forName("weka.classifiers.meta.CVParameterSelection$CVParameter");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return class1;
	}
	
	protected Object getParamFieldByName(CVParameter param, String fieldName) {
		Class cl = this.getCVParameterClass();
		Field f;
		Object value = null;
		
		try {
			f = cl.getDeclaredField(fieldName);
			f.setAccessible(true);
			value = f.get(param);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	protected void setAccField(CVParameter param, String fieldName, Object value) {
		Class cl = this.getCVParameterClass();
		Field f = null;
		try {
			f = cl.getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(param, value);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected String getParamChar(CVParameter param) {
		
		String value = (String) this.getParamFieldByName(param, "m_ParamChar");
		return value;
	}
	
	protected void setParamChar(CVParameter param, String value) {
		this.setAccField(param, "m_ParamChar", value);
	}
	
	protected double getParamLowerBound(CVParameter param) {
		double lower = (Double) this.getParamFieldByName(param, "m_Lower");
		return lower;
	}
	
	protected void setParamLowerBound(CVParameter param, double value) {
		this.setAccField(param, "m_Lower", new Double(value));
	}
	
	protected double getParamUpperBound(CVParameter param) {
		double upper = (Double) this.getParamFieldByName(param, "m_Upper");
		return upper;
	}
	
	protected void setParamUpperBound(CVParameter param, double value) {
		this.setAccField(param, "m_Upper", new Double(value));
	}
	
	protected double getParamSteps(CVParameter param) {
		double steps = (Double) this.getParamFieldByName(param, "m_Steps");
		return steps;
	}
	
	protected void setParamSteps(CVParameter param, double value) {
		this.setAccField(param, "m_Steps", new Double(value));
	}
	
	protected double getParamValue(CVParameter param) {
		double value = (Double) this.getParamFieldByName(param, "m_ParamValue");
		return value;
	}
	
	protected void setParamValue(CVParameter param, double value) {
		this.setAccField(param, "m_ParamValue", new Double(value));
	}
	
	protected boolean getParamAddAtEnd(CVParameter param) {
		boolean val = (Boolean) this.getParamFieldByName(param, "m_AddAtEnd");
		return val;
	}
	
	protected void setParamAddAtEnd(CVParameter param, boolean value) {
		this.setAccField(param, "m_AddAtEnd", new Boolean(value));
	}
	
	protected boolean getParamRoundParam(CVParameter param) {
		boolean val = (Boolean) this.getParamFieldByName(param, "m_RoundParam");
		return val;
	}
	
	protected void setParamRoundParam(CVParameter param, boolean value) {
		this.setAccField(param, "m_RoundParam", new Boolean(value));
	}



	/**
		* @return the qualitCalc
		*/
		public QualityCalculator getQualitCalc() {
			return this.qualitCalc;
		}

		/**
		 * @param qualitCalc the qualitCalc to set
		 */
		public void setQualitCalc(QualityCalculator qualitCalc) {
			this.qualitCalc = qualitCalc;
		}

	

}
