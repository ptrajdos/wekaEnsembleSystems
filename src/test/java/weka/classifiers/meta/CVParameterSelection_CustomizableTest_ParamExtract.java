package weka.classifiers.meta;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.lazy.IBk;

public class CVParameterSelection_CustomizableTest_ParamExtract {
	
	public CVParameterSelection_Customizable classifier = null;

	@Before
	public void setUp() throws Exception {
		this.classifier = new CVParameterSelection_Customizable();
		
		IBk ibk = new IBk();
		
		String KOptString = "K 1 11 6";
		
		this.classifier.setClassifier(ibk);
		this.classifier.addCVParameter(KOptString);
		
	}
	@Test
	public void testGetCVParameterClass() {	
		Class cl = this.classifier.getCVParameterClass();
		assertEquals("weka.classifiers.meta.CVParameterSelection$CVParameter", cl.getName());
	}
	
	@Test
	public void testParamChar() {
		String optChar="K";
		assertEquals(optChar, this.classifier.getParamChar(this.classifier.m_CVParams.get(0)));
	}
	
	@Test
	public void testSetParamChar() {
		String optChar = "G";
		this.classifier.setParamChar(this.classifier.m_CVParams.get(0), optChar);
		assertEquals(optChar, this.classifier.getParamChar(this.classifier.m_CVParams.get(0)));
	}
	
	@Test
	public void testParamLower() {
		double lower = 1;
		assertEquals(lower, this.classifier.getParamLowerBound(this.classifier.m_CVParams.get(0)),1E-6);
	}
	
	@Test 
	public void testSetParamLower() {
		double lower = 5;
		this.classifier.setParamLowerBound(this.classifier.m_CVParams.get(0), lower);
		assertEquals(lower, this.classifier.getParamLowerBound(this.classifier.m_CVParams.get(0)),1E-6);
	}
	
	@Test
	public void testParamUpper() {
		double upper = 11;
		assertEquals(upper, this.classifier.getParamUpperBound(this.classifier.m_CVParams.get(0)),1E-6);
	}
	
	@Test 
	public void testSetParamUpper() {
		double upper = 40;
		this.classifier.setParamLowerBound(this.classifier.m_CVParams.get(0), upper);
		assertEquals(upper, this.classifier.getParamLowerBound(this.classifier.m_CVParams.get(0)),1E-6);
	}
	
	@Test
	public void testParamSteps() {
		double steps = 6;
		assertEquals(steps, this.classifier.getParamSteps(this.classifier.m_CVParams.get(0)),1E-6);
	}
	
	@Test
	public void testSetParamSteps() {
		double steps = 10;
		this.classifier.setParamSteps(this.classifier.m_CVParams.get(0), steps);
		assertEquals(steps, this.classifier.getParamSteps(this.classifier.m_CVParams.get(0)),1E-6);
	}
	
	@Test
	public void testParamValue() {
		double value = 0;
		assertEquals(value, this.classifier.getParamValue(this.classifier.m_CVParams.get(0)),1E-6);
	}
	
	@Test
	public void testSetParamValue() {
		double value = 8;
		this.classifier.setParamValue(this.classifier.m_CVParams.get(0), value);
		assertEquals(value, this.classifier.getParamValue(this.classifier.m_CVParams.get(0)),1E-6);
	}
	
	@Test
	public void testParamRoundParam() {
		boolean value = false;
		assertEquals(value, this.classifier.getParamRoundParam(this.classifier.m_CVParams.get(0)));
	}
	
	@Test
	public void testParamSetRoundParam() {
		boolean value = true;
		this.classifier.setParamRoundParam(this.classifier.m_CVParams.get(0), value);
		assertEquals(value, this.classifier.getParamRoundParam(this.classifier.m_CVParams.get(0)));
	}
	
	@Test
	public void testParamAddAtEnd() {
		boolean value = false;
		assertEquals(value, this.classifier.getParamAddAtEnd(this.classifier.m_CVParams.get(0)));
	}
	
	@Test
	public void testParamSetAddAtEnd() {
		boolean value = true;
		this.classifier.setParamAddAtEnd(this.classifier.m_CVParams.get(0), value);
		assertEquals(value, this.classifier.getParamAddAtEnd(this.classifier.m_CVParams.get(0)));
	}
	

}
