package weka.classifiers.meta.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommitteeResponseExtractorTest {

	@Test
	public void test() {
		CommitteeResponseExtractor extr = new CommitteeResponseExtractor();
		assertTrue("Not null Extractor: ", extr!=null);
		
		String rev = extr.getRevision();
		assertTrue("Revision not null!", rev!=null);
		
	}

}
