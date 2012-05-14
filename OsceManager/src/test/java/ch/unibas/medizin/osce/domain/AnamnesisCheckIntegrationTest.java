package ch.unibas.medizin.osce.domain;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;


@RooIntegrationTest(entity = AnamnesisCheck.class)
public class AnamnesisCheckIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
    
   
    @Test
    public void testMoveDown() {
    	
    	
    	AnamnesisCheck check0 = createAnamnesisCheck(14, "question1", AnamnesisCheckTypes.QUESTION_TITLE );
    	AnamnesisCheck check1 = createAnamnesisCheck(15, "question2", AnamnesisCheckTypes.QUESTION_TITLE );
    	AnamnesisCheck check2 = createAnamnesisCheck(16, "question3", AnamnesisCheckTypes.QUESTION_TITLE );
    	AnamnesisCheck check3 = createAnamnesisCheck(17, "question4", AnamnesisCheckTypes.QUESTION_TITLE );
    	
    	check1.moveDown();
    	
    	Assert.assertEquals(14, AnamnesisCheck.findAnamnesisCheck(check0.getId()).getSort_order().intValue());
    	Assert.assertEquals(16, AnamnesisCheck.findAnamnesisCheck(check1.getId()).getSort_order().intValue());
    	//TODO expected not equals 6 must equals 5
    	Assert.assertEquals(15, AnamnesisCheck.findAnamnesisCheck(check2.getId()).getSort_order().intValue());
    	Assert.assertEquals(17, AnamnesisCheck.findAnamnesisCheck(check3.getId()).getSort_order().intValue());
  
    }
    
    private AnamnesisCheck createAnamnesisCheck(int sort_order, String text, AnamnesisCheckTypes type ){
    	AnamnesisCheck ret = new AnamnesisCheck();
    	ret.setSort_order(sort_order);
    	ret.setText(text);
    	ret.setType(type);
    	ret.persist();
    	
    	return ret;
    	
    }
    
}
