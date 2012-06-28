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

    	AnamnesisCheckTitle title = new AnamnesisCheckTitle();
    	title.setText("Behandlungsgeschichte");
    	title.persist();
    	title.merge();
    	
        AnamnesisCheck check0 = createAnamnesisCheck(14,null, "question1", AnamnesisCheckTypes.QUESTION_TITLE, null ,title);
        AnamnesisCheck check1 = createAnamnesisCheck(15,null, "question2", AnamnesisCheckTypes.QUESTION_TITLE, null ,title);
        AnamnesisCheck check2 = createAnamnesisCheck(16,null, "question3", AnamnesisCheckTypes.QUESTION_TITLE, null ,title);
        AnamnesisCheck check3 = createAnamnesisCheck(17,null, "question4", AnamnesisCheckTypes.QUESTION_TITLE, null ,title);
                       
        check1.moveDown();
                
        Assert.assertEquals(14, AnamnesisCheck.findAnamnesisCheck(check0.getId()).getSort_order().intValue());
        Assert.assertEquals(16, AnamnesisCheck.findAnamnesisCheck(check1.getId()).getSort_order().intValue());
        Assert.assertEquals(15, AnamnesisCheck.findAnamnesisCheck(check2.getId()).getSort_order().intValue());
        Assert.assertEquals(17, AnamnesisCheck.findAnamnesisCheck(check3.getId()).getSort_order().intValue());

    }
    
    @Test
    public void testMoveUp() {

    	AnamnesisCheckTitle title = new AnamnesisCheckTitle();
    	title.setText("Behandlungsgeschichte");
    	title.persist();
    	title.merge();
    	
        AnamnesisCheck check0 = createAnamnesisCheck(14,null, "question1", AnamnesisCheckTypes.QUESTION_TITLE, null ,title);
        AnamnesisCheck check1 = createAnamnesisCheck(15,null, "question2", AnamnesisCheckTypes.QUESTION_TITLE, null ,title);
        AnamnesisCheck check2 = createAnamnesisCheck(16,null, "question3", AnamnesisCheckTypes.QUESTION_TITLE, null ,title);
        AnamnesisCheck check3 = createAnamnesisCheck(17,null, "question4", AnamnesisCheckTypes.QUESTION_TITLE, null ,title);
                       
        check1.moveUp();
                
        Assert.assertEquals(15, AnamnesisCheck.findAnamnesisCheck(check0.getId()).getSort_order().intValue());
        Assert.assertEquals(14, AnamnesisCheck.findAnamnesisCheck(check1.getId()).getSort_order().intValue());
        Assert.assertEquals(16, AnamnesisCheck.findAnamnesisCheck(check2.getId()).getSort_order().intValue());
        Assert.assertEquals(17, AnamnesisCheck.findAnamnesisCheck(check3.getId()).getSort_order().intValue());

    }

    private AnamnesisCheck createAnamnesisCheck(int sort_order, Integer userSpecSO, String text, AnamnesisCheckTypes type, AnamnesisCheck title ,AnamnesisCheckTitle anamnesisCheckTitle){
        AnamnesisCheck ret = new AnamnesisCheck();
        ret.setUserSpecifiedOrder(userSpecSO);
        ret.setSort_order(sort_order);
        ret.setText(text);
        ret.setType(type);
        ret.setTitle(title);
        ret.setAnamnesisCheckTitle(anamnesisCheckTitle);

        return ret.merge();

    }
    
}
