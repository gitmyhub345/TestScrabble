/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.comp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rider1
 */
public class MyWebField {
    
    private ArrayList<MyWebField> child;
//    private ArrayList<MyWebField> siblingFields;
//    private ArrayList<MyWebField> options;
    private boolean hasChild;
//    private boolean hasSiblings;
    private List<Map<String,String>> attributes;
    
    private fieldType field;
    private String name;
    private int id;
    private String className;
    private int maxLength;
    private int length;
    private String groupName;
    private String value;
    private String label;
    private int rows;
    private int cols;
    private String headerText;
    private String footerText;
    
    public MyWebField(){
        child = new ArrayList<MyWebField>(0);
        hasChild = false;
        this.field = null;
        this.name = null;
        this.id = 0;
        this.className = null;
        this.maxLength = 0;
        this.length = 0;
        this.value = null;
        this.label = null;
        this.rows = 0;
        this.cols = 0;
        this.groupName = null;
        this.headerText = null;
        this.footerText = null;
    }
    public MyWebField(fieldType field,String name,int id, String className,int maxLength,int length,String value, String label, int rows, int cols, String groupname,String headerText, String footerText){
        child = new ArrayList<MyWebField>(0);

//        siblingFields = new ArrayList<MyWebField>(0);
//        options = new ArrayList<MyWebField>(0);
        hasChild = false;
//        hasSiblings = false;
        
        this.field = field;
        this.name = name;
        this.id = id;
        this.className = className;
        this.maxLength = maxLength;
        this.length = length;
        this.value = value;
        this.label = label;
        this.rows = rows;
        this.cols = cols;
        this.groupName = groupname;
        this.headerText = headerText;
        this.footerText = footerText;
    }
    
    public MyWebField getField(){
        return this;
    }

    public ArrayList<MyWebField> getChild(){
        return this.child;
    }

    public List<Map<String,String>> getAttributes(){
        return attributes;
    }
    public void addChild(MyWebField child){
        this.child.add(child);
    }
    
    public String getStrField(){
        String strField = getFieldType();
        strField += getStrFieldAtt();
        if (!child.isEmpty()){
            for (int index = 0; index < child.size(); index++){
                strField += "\n\t"+child.get(index).getStrField();
            }
        } 
        
        switch (this.field){
            case CHECKBOX:
            case RADIO:
                strField+="<br>";
                break;
            case SELECT:
                strField+= "\n</select><span>"+footerText+"</span>";
                break;
            case TEXTAREA:
                strField+="</textarea><span>"+footerText+"</span>";
                break;
            case DIV:
                strField+="\n</div>";
                break;
            case FIELDSET:
                strField+="\n<span>"+footerText+"</span></fieldset>";
                break;
            default: strField += "";
            break;
        }
        return strField;
    }
    
    public String getStrChild(){
        String strChild = "";
        
        for (MyWebField f: child)
            strChild+=f.getStrField();
        
        return strChild;
    }
    
    public String getStrFieldAtt(){
        String  fieldString;
        fieldString = "name=\""+this.name+"\" id=\""+this.id+"\" class=\""+this.className+"\" ";
        switch (this.field){
            case INPUT:
                fieldString+= "maxlength=\""+this.maxLength+"\" length=\""+this.length+"\" placeholder=\""+this.label+"\" ><span>"+footerText+"</span>";
                break;
            case RADIO:
                fieldString+= "group=\""+this.groupName+"\" value=\""+this.value+"\" ><label>"+this.label+"</label>";
                break;
            case CHECKBOX:
                fieldString+="group=\""+this.groupName+"\" value=\""+this.value+"\" ><label>"+this.label+"</label>";
                break;
            case TEXTAREA:
                fieldString+=" rows=\""+this.rows+"\" cols=\""+this.cols+"\" >";
                break;
            case OPTION:
                fieldString+=" >"+this.label+"</option>";
                break;
            case SELECT:
                fieldString+= "class=\""+this.className+"\" >";
                break;
            default:
                fieldString+=" >";
                break;
        }
        return fieldString;
    }
    
    
    private String getFieldType(){
    //INPUT,SELECT,RADIO,CHECKBOX,TEXTAREA,FILE
        String field = "";
        switch (this.field) {
            case DIV:
                field += "<span>"+headerText+"</span><div ";
                break;
            case FIELDSET:
                field += "<fieldset ";
                break;
            case INPUT:
                field += "<span>"+headerText+"</span><input type=\"text\" ";
                break;
            case SELECT:
                field += "<span>"+headerText+"</span><label>"+this.label+"</label><select ";
                break;
            case RADIO:
                field += "<input type=\"radio\" ";
                break;
            case CHECKBOX:
                field += "<input type=\"checkbox\" ";
                break;
            case TEXTAREA:
                field += "<span>"+headerText+"</span><textarea ";
                break;
            case OPTION:
                field += "<option value=\""+this.value+"\" ";
                break;
            case FILE:
                field += "<span>"+headerText+"</span><file ";
                break;
            default:
                field = "<>";
                break;
        }
        return field;
    }
    
    private String getStrAttributes(){
        String att = "";
        
        for (Map m: attributes){
            for (int attIndex = 0; attIndex <m.keySet().toArray().length; attIndex++){
                Object[] key = m.keySet().toArray();
                att += (String)key[attIndex] + "= \""+m.get((String)key[attIndex])+"\" ";
            }
        }
        
        return att;
    }
}
