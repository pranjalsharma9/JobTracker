package com.pranjals.nsit.jobtracker;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pranjal on 03-04-2016.
 */
public class CustomFields {
    public static class FieldElement{
        public String datatype;
        public String fieldName;
        public FieldElement(String datatype, String fieldName){
            this.datatype = datatype;
            this.fieldName = fieldName;
        }
    }
    ArrayList<FieldElement> orderFieldList = new ArrayList<>();
    ArrayList<FieldElement> customerFieldList = new ArrayList<>();
    ArrayList<FieldElement> employeeFieldList = new ArrayList<>();

    public int getListLength(int arrayListType){
        int length = 0;
        switch(arrayListType){
            case 1: length = orderFieldList.size();
                break;
            case 2: length = customerFieldList.size();
                break;
            case 3: length = employeeFieldList.size();
                break;
            default: break;
        }
        return length;
    }

    public void addNewFieldElement(FieldElement fieldElement, int arrayListType){
        switch(arrayListType){
            case 1: orderFieldList.add(fieldElement);
                break;
            case 2: customerFieldList.add(fieldElement);
                break;
            case 3: employeeFieldList.add(fieldElement);
                break;
            default: break;
        }
    }

    public void removeFieldElement(int fieldElementIndex, int arrayListType){
        switch(arrayListType){
            case 1: orderFieldList.remove(fieldElementIndex);
                break;
            case 2: customerFieldList.remove(fieldElementIndex);
                break;
            case 3: employeeFieldList.remove(fieldElementIndex);
                break;
            default: break;
        }
    }

    public FieldElement returnFieldElement(int fieldElementIndex, int arrayListType){
        FieldElement fieldElement = null;
        switch (arrayListType){
            case 1: fieldElement = orderFieldList.get(fieldElementIndex);
                break;
            case 2: fieldElement = customerFieldList.get(fieldElementIndex);
                break;
            case 3: fieldElement = employeeFieldList.get(fieldElementIndex);
                break;
            default: break;
        }
        return fieldElement;
    }

    public void refreshList(int arrayListType){
        switch(arrayListType){
            case 1: orderFieldList.clear();
                break;
            case 2: customerFieldList.clear();
                break;
            case 3: employeeFieldList.clear();
                break;
            default: break;
        }
    }
}
