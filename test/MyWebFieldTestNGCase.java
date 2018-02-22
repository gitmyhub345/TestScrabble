/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import org.dragoncave.home.comp.MyWebField;
import org.dragoncave.home.comp.fieldType;
import org.testng.annotations.Test;

/**
 *
 * @author Rider1
 */
public class MyWebFieldTestNGCase {
    MyWebField webField;
    
    public MyWebFieldTestNGCase() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @BeforeClass
    public void setUpClass() throws Exception {
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
    
    @Test
    public void getStrFieldTest(){
//        System.out.println("\n---------------------------------\n\t\tTest Get String Field\n---------------------------------\n");
        
        webField = new MyWebField(fieldType.INPUT,"input1",9888133, "firstClass",50,25,"", "test input placeholder", 0,0,"","","");
        System.out.println(webField.getStrField());
        webField = new MyWebField(fieldType.TEXTAREA,"input1",9888133, "firstClass",50,25,"", "", 5,50,"","header instr","footer instr");
        System.out.println(webField.getStrField());
    }
    
    @Test
    public void getStrFieldWChild(){
//        System.out.println("\n---------------------------------\n\t\tTest Get String Field with children\n---------------------------------\n");
        webField = new MyWebField(fieldType.SELECT,"input1",9888133, "firstClass",50,25,"", "please select one", 0,0,"","","");
    //    webField = new MyWebField(fieldType.OPTION,"",982348,"secondClass",0,0,"value","label",0);
        webField.addChild(new MyWebField(fieldType.OPTION,"",982348,"secondClass",0,0,"value","label",0,0,"","",""));
        webField.addChild(new MyWebField(fieldType.OPTION,"",982343,"secondClass",0,0,"value2","label2",0,0,"","",""));
        webField.addChild(new MyWebField(fieldType.OPTION,"",982345,"secondClass",0,0,"value3","label3",0,0,"","",""));
        System.out.println(webField.getStrField());

    }
    
    @Test
    public void getMultiNested(){
//        System.out.println("\n---------------------------------\n\t\tTest Get Multiple nested levels\n---------------------------------\n");
        //fieldType field,String name,int id, String className,int maxLength,int length,String value, String label, int rows, int cols
        webField = new MyWebField(fieldType.DIV,"firstDiv",9883,"parentDiv",0,0,"","",0,0,"","","");
        webField.addChild(new MyWebField(fieldType.DIV,"firstDiv",9884,"childDiv",0,0,"","",0,0,"","",""));
        webField.getChild().get(0).addChild(new MyWebField(fieldType.FIELDSET,"firstDiv",9885,"firstFieldSet",0,0,"","",0,0,"","",""));
        webField.getChild().get(0).getChild().get(0).addChild(new MyWebField(fieldType.INPUT,"firstInput",9886,"firstInput",25,15,"","First Name",0,0,"","",""));
        webField.getChild().get(0).getChild().get(0).addChild(new MyWebField(fieldType.INPUT,"secondInput",9887,"secondInput",25,15,"","Last Name",0,0,"","",""));
        webField.addChild(new MyWebField(fieldType.DIV,"firstDiv",9888,"childDiv",0,0,"","",0,0,"","",""));
        webField.getChild().get(1).addChild(new MyWebField(fieldType.FIELDSET,"firstDiv",9889,"firstFieldSet",0,0,"","",0,0,"","",""));
        webField.getChild().get(1).getChild().get(0).addChild(new MyWebField(fieldType.INPUT,"thirdInput",9890,"thirdInput",25,15,"","Date of Birth",0,0,"","",""));
        webField.getChild().get(1).getChild().get(0).addChild(new MyWebField(fieldType.INPUT,"fourthInput",9891,"fourthInput",25,15,"","Place of Birth",0,0,"","",""));

        System.out.println(webField.getStrField());
    }
    
    @Test
    public void getCheckboxes(){
//        System.out.println("\n---------------------------------\n\t\tTest Get Checkboxes\n---------------------------------\n");
        webField = new MyWebField(fieldType.DIV,"firstDiv",9893,"parentDiv",0,0,"","",0,0,"","","");
        webField.addChild(new MyWebField(fieldType.CHECKBOX,"firstCheckBox",9892,"Checkbox one",0,0,"","checkbox label",0,0,"checkboxgroup","",""));
        webField.addChild(new MyWebField(fieldType.CHECKBOX,"firstCheckBox",9893,"Checkbox two",0,0,"","checkbox label2",0,0,"checkboxgroup","",""));
        webField.addChild(new MyWebField(fieldType.CHECKBOX,"firstCheckBox",9894,"Checkbox three",0,0,"","checkbox label3",0,0,"checkboxgroup","",""));
        
        System.out.println(webField.getStrField());
    }
    @Test
    public void getRadio(){
//        System.out.println("\n---------------------------------\n\t\tTest Get Checkboxes\n---------------------------------\n");
        webField = new MyWebField(fieldType.DIV,"firstDiv",9893,"parentDiv",0,0,"","",0,0,"","","");
        webField.addChild(new MyWebField(fieldType.RADIO,"firstCheckBox",9892,"Checkbox one",0,0,"radio1value","checkbox label",0,0,"radiogroup","",""));
        webField.addChild(new MyWebField(fieldType.RADIO,"firstCheckBox",9893,"Checkbox two",0,0,"radio2value","checkbox label2",0,0,"radiogroup","",""));
        webField.addChild(new MyWebField(fieldType.RADIO,"firstCheckBox",9894,"Checkbox three",0,0,"radio3value","checkbox label3",0,0,"radiogroup","",""));
        
        System.out.println(webField.getStrField());
    }
}
