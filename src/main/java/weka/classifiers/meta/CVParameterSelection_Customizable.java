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
	
	
	
	@Override
	protected void findParamsByCrossValidation(int depth, Instances trainData,
						     Random random)
	  throws Exception {
		

	  if (depth < this.m_CVParams.size()) {
	    CVParameter cvParam = (CVParameter)this.m_CVParams.elementAt(depth);

	    double upper;
	    switch ((int)(this.getParamLowerBound(cvParam) - this.getParamUpperBound(cvParam) + 0.5)) {
	    case 1:
		upper = this.m_NumAttributes;
		break;
	    case 2:
		upper = this.m_TrainFoldSize;
		break;
	    default:
		upper = this.getParamUpperBound(cvParam);
		break;
	    }
	    double increment = (upper - this.getParamLowerBound(cvParam)) / (this.getParamSteps(cvParam) - 1);
	    double upp = this.getParamUpperBound(cvParam);
	    
	    this.setParamValue(cvParam, this.getParamLowerBound(cvParam));
	    for(double val = this.getParamLowerBound(cvParam);val<=upp;val+=increment) {
	    	this.setParamValue(cvParam, val);
	    	findParamsByCrossValidation(depth + 1, trainData, random);
	    }
	    	
	    
	  } else {
	    
	    Evaluation evaluation = new Evaluation(trainData);

	    // Set the classifier options
	    String [] options = createOptions();
	    if (this.m_Debug) {
		System.err.print("Setting options for " 
				 + this.m_Classifier.getClass().getName() + ":");
		for (int i = 0; i < options.length; i++) {
		  System.err.print(" " + options[i]);
		}
		System.err.println("");
	    }
	    ((OptionHandler)this.m_Classifier).setOptions(options);
	    for (int j = 0; j < this.m_NumFolds; j++) {

	      // We want to randomize the data the same way for every 
	      // learning scheme.
		Instances train = trainData.trainCV(this.m_NumFolds, j, new Random(1));
		Instances test = trainData.testCV(this.m_NumFolds, j);
		this.m_Classifier.buildClassifier(train);
		evaluation.setPriors(train);
		evaluation.evaluateModel(this.m_Classifier, test);
	    }
	    double error = this.qualitCalc.getQuality(evaluation);
	    if (this.m_Debug) {
		System.err.println("Cross-validated error rate: " 
				   + Utils.doubleToString(error, 6, 4));//TODO error is zero?
	    }
	    if ((this.m_BestPerformance == -99) || (error < this.m_BestPerformance)) {
		
		this.m_BestPerformance = error;
		this.m_BestClassifierOptions = createOptions();
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
		Class<?> cl = this.getCVParameterClass();
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
		Boolean val = (Boolean) this.getParamFieldByName(param, "m_AddAtEnd");
		return val.booleanValue();
	}
	
	protected void setParamAddAtEnd(CVParameter param, boolean value) {
		this.setAccField(param, "m_AddAtEnd", new Boolean(value));
	}
	
	protected boolean getParamRoundParam(CVParameter param) {
		Boolean val = (Boolean) this.getParamFieldByName(param, "m_RoundParam");
		return val.booleanValue();
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
