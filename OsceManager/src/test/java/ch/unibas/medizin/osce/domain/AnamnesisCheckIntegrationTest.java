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


        AnamnesisCheck check0 = createAnamnesisCheck(14,null, "question1", AnamnesisCheckTypes.QUESTION_TITLE, null);
        AnamnesisCheck check1 = createAnamnesisCheck(15,null, "question2", AnamnesisCheckTypes.QUESTION_TITLE, null );
        AnamnesisCheck check2 = createAnamnesisCheck(16,null, "question3", AnamnesisCheckTypes.QUESTION_TITLE, null );
        AnamnesisCheck check3 = createAnamnesisCheck(17,null, "question4", AnamnesisCheckTypes.QUESTION_TITLE, null );

        check1.moveDown();

        Assert.assertEquals(14, AnamnesisCheck.findAnamnesisCheck(check0.getId()).getSort_order().intValue());
        Assert.assertEquals(16, AnamnesisCheck.findAnamnesisCheck(check1.getId()).getSort_order().intValue());
        //TODO expected not equals 6 must equals 5
        Assert.assertEquals(15, AnamnesisCheck.findAnamnesisCheck(check2.getId()).getSort_order().intValue());
        Assert.assertEquals(17, AnamnesisCheck.findAnamnesisCheck(check3.getId()).getSort_order().intValue());

    }

    private AnamnesisCheck createAnamnesisCheck(int sort_order, Integer userSpecSO, String text, AnamnesisCheckTypes type, AnamnesisCheck title ){
        AnamnesisCheck ret = new AnamnesisCheck();
        ret.setUserSpecifiedOrder(userSpecSO);
        ret.setSort_order(sort_order);
        ret.setText(text);
        ret.setType(type);
        ret.setTitle(title);
        ret.persist();

        return ret;

    }

    @Test
    public void testNormalizeTitleOrder() {
         System.out.println("#37 in testNormalizeOrder = ");

        AnamnesisCheck check0 = createAnamnesisCheck(14,2, "Title1", AnamnesisCheckTypes.QUESTION_TITLE, null );
        AnamnesisCheck check4 = createAnamnesisCheck(15,null, "Title2", AnamnesisCheckTypes.QUESTION_TITLE , null);
        AnamnesisCheck check8 = createAnamnesisCheck(16,null, "Title3", AnamnesisCheckTypes.QUESTION_TITLE, null );

        AnamnesisCheck.normalizeOrder();

        Assert.assertEquals(2, AnamnesisCheck.findAnamnesisCheck(check0.getId()).getSort_order().intValue());
        Assert.assertEquals(1, AnamnesisCheck.findAnamnesisCheck(check4.getId()).getSort_order().intValue());
        Assert.assertEquals(3, AnamnesisCheck.findAnamnesisCheck(check8.getId()).getSort_order().intValue());

        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check0.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check4.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check8.getId()).getUserSpecifiedOrder());



    }

    @Test
    public void testNormalizeTitleInvalidOrder() {
         System.out.println("#37 in testNormalizeTitleInvalidOrder = ");

        AnamnesisCheck check0 = createAnamnesisCheck(14,12, "Title1", AnamnesisCheckTypes.QUESTION_TITLE, null );
        AnamnesisCheck check4 = createAnamnesisCheck(15,null, "Title2", AnamnesisCheckTypes.QUESTION_TITLE, null );
        AnamnesisCheck check8 = createAnamnesisCheck(16,null, "Title3", AnamnesisCheckTypes.QUESTION_TITLE, null );

        AnamnesisCheck.normalizeOrder();

        Assert.assertEquals(3, AnamnesisCheck.findAnamnesisCheck(check0.getId()).getSort_order().intValue());
        Assert.assertEquals(1, AnamnesisCheck.findAnamnesisCheck(check4.getId()).getSort_order().intValue());
        Assert.assertEquals(2, AnamnesisCheck.findAnamnesisCheck(check8.getId()).getSort_order().intValue());

        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check0.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check4.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check8.getId()).getUserSpecifiedOrder());

    }




    @Test
    public void testNormalizeOrder() {
         System.out.println("##############################37 in testNormalizeOrder = ");
        AnamnesisCheck check0 = createAnamnesisCheck(14,2, "Title1", AnamnesisCheckTypes.QUESTION_TITLE, null );
        AnamnesisCheck check1 = createAnamnesisCheck(15,3, "1question1", AnamnesisCheckTypes.QUESTION_YES_NO,check0 );
        AnamnesisCheck check2 = createAnamnesisCheck(16,1, "1question2", AnamnesisCheckTypes.QUESTION_YES_NO,check0 );
        AnamnesisCheck check3 = createAnamnesisCheck(17,2, "1question3", AnamnesisCheckTypes.QUESTION_YES_NO,check0 );
        AnamnesisCheck check4 = createAnamnesisCheck(18,null, "Title2", AnamnesisCheckTypes.QUESTION_TITLE, null );
        AnamnesisCheck check5 = createAnamnesisCheck(19,null, "2question1", AnamnesisCheckTypes.QUESTION_YES_NO,check4 );
        AnamnesisCheck check6 = createAnamnesisCheck(20,null, "2question2", AnamnesisCheckTypes.QUESTION_YES_NO,check4  );
        AnamnesisCheck check7 = createAnamnesisCheck(21,null, "2question3", AnamnesisCheckTypes.QUESTION_YES_NO,check4  );
        AnamnesisCheck check8 = createAnamnesisCheck(22,null, "Title3", AnamnesisCheckTypes.QUESTION_TITLE, null );
        AnamnesisCheck check9 = createAnamnesisCheck(23,null, "3question1", AnamnesisCheckTypes.QUESTION_YES_NO,check8 );
        AnamnesisCheck check10 = createAnamnesisCheck(24,null, "3question2", AnamnesisCheckTypes.QUESTION_YES_NO,check8 );
        AnamnesisCheck check11 = createAnamnesisCheck(25,null, "3question3", AnamnesisCheckTypes.QUESTION_YES_NO,check8 );

        AnamnesisCheck.normalizeOrder();

        Assert.assertEquals(1, AnamnesisCheck.findAnamnesisCheck(check4.getId()).getSort_order().intValue());
        Assert.assertEquals(2, AnamnesisCheck.findAnamnesisCheck(check5.getId()).getSort_order().intValue());
        Assert.assertEquals(3, AnamnesisCheck.findAnamnesisCheck(check6.getId()).getSort_order().intValue());
        Assert.assertEquals(4, AnamnesisCheck.findAnamnesisCheck(check7.getId()).getSort_order().intValue());


        Assert.assertEquals(5, AnamnesisCheck.findAnamnesisCheck(check0.getId()).getSort_order().intValue());
        Assert.assertEquals(6, AnamnesisCheck.findAnamnesisCheck(check2.getId()).getSort_order().intValue());
        Assert.assertEquals(7, AnamnesisCheck.findAnamnesisCheck(check3.getId()).getSort_order().intValue());
        Assert.assertEquals(8, AnamnesisCheck.findAnamnesisCheck(check1.getId()).getSort_order().intValue());


        Assert.assertEquals(9, AnamnesisCheck.findAnamnesisCheck(check8.getId()).getSort_order().intValue());
        Assert.assertEquals(10, AnamnesisCheck.findAnamnesisCheck(check9.getId()).getSort_order().intValue());
        Assert.assertEquals(11, AnamnesisCheck.findAnamnesisCheck(check10.getId()).getSort_order().intValue());
        Assert.assertEquals(12, AnamnesisCheck.findAnamnesisCheck(check11.getId()).getSort_order().intValue());

        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check0.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check1.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check2.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check3.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check4.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check5.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check6.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check7.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check8.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check9.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check10.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check11.getId()).getUserSpecifiedOrder());
    }

    @Test
    public void testNormalizeInvalidQOrder() {
         System.out.println("##############################37 in testNormalizeOrder = ");
        AnamnesisCheck check0 = createAnamnesisCheck(14,61, "Title1", AnamnesisCheckTypes.QUESTION_TITLE, null ); // question out of range
        AnamnesisCheck check1 = createAnamnesisCheck(15,7, "1question1", AnamnesisCheckTypes.QUESTION_YES_NO,check0 );// question out of range
        AnamnesisCheck check2 = createAnamnesisCheck(16,-2, "1question2", AnamnesisCheckTypes.QUESTION_YES_NO,check0 );// question out of range
        AnamnesisCheck check3 = createAnamnesisCheck(17,2, "1question3", AnamnesisCheckTypes.QUESTION_YES_NO,check0 );
        AnamnesisCheck check4 = createAnamnesisCheck(18,-3, "Title2", AnamnesisCheckTypes.QUESTION_TITLE, null ); // question out of range
        AnamnesisCheck check5 = createAnamnesisCheck(19,null, "2question1", AnamnesisCheckTypes.QUESTION_YES_NO,check4 );
        AnamnesisCheck check6 = createAnamnesisCheck(20,null, "2question2", AnamnesisCheckTypes.QUESTION_YES_NO,check4  );
        AnamnesisCheck check7 = createAnamnesisCheck(21,null, "2question3", AnamnesisCheckTypes.QUESTION_YES_NO,check4  );
        AnamnesisCheck check8 = createAnamnesisCheck(22,null, "Title3", AnamnesisCheckTypes.QUESTION_TITLE, null );
        AnamnesisCheck check9 = createAnamnesisCheck(23,null, "3question1", AnamnesisCheckTypes.QUESTION_YES_NO,check8 );
        AnamnesisCheck check10 = createAnamnesisCheck(24,null, "3question2", AnamnesisCheckTypes.QUESTION_YES_NO,check8 );
        AnamnesisCheck check11 = createAnamnesisCheck(25,null, "3question3", AnamnesisCheckTypes.QUESTION_YES_NO,check8 );

        AnamnesisCheck.normalizeOrder();

        Assert.assertEquals(1, AnamnesisCheck.findAnamnesisCheck(check4.getId()).getSort_order().intValue());
        Assert.assertEquals(2, AnamnesisCheck.findAnamnesisCheck(check5.getId()).getSort_order().intValue());
        Assert.assertEquals(3, AnamnesisCheck.findAnamnesisCheck(check6.getId()).getSort_order().intValue());
        Assert.assertEquals(4, AnamnesisCheck.findAnamnesisCheck(check7.getId()).getSort_order().intValue());

        Assert.assertEquals(5, AnamnesisCheck.findAnamnesisCheck(check8.getId()).getSort_order().intValue());
        Assert.assertEquals(6, AnamnesisCheck.findAnamnesisCheck(check9.getId()).getSort_order().intValue());
        Assert.assertEquals(7, AnamnesisCheck.findAnamnesisCheck(check10.getId()).getSort_order().intValue());
        Assert.assertEquals(8, AnamnesisCheck.findAnamnesisCheck(check11.getId()).getSort_order().intValue());



        Assert.assertEquals(9, AnamnesisCheck.findAnamnesisCheck(check0.getId()).getSort_order().intValue());
        Assert.assertEquals(10, AnamnesisCheck.findAnamnesisCheck(check2.getId()).getSort_order().intValue());
        Assert.assertEquals(11, AnamnesisCheck.findAnamnesisCheck(check3.getId()).getSort_order().intValue());
        Assert.assertEquals(12, AnamnesisCheck.findAnamnesisCheck(check1.getId()).getSort_order().intValue());




        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check0.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check1.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check2.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check3.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check4.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check5.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check6.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check7.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check8.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check9.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check10.getId()).getUserSpecifiedOrder());
        Assert.assertNull(AnamnesisCheck.findAnamnesisCheck(check11.getId()).getUserSpecifiedOrder());
    }


}
