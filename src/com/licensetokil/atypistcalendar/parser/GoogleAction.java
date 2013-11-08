//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;

public class GoogleAction extends Action{
	private GoogleActionType type;

	public GoogleAction(){
		type = null;
	}
	
	public String toString(){
		return ("Type	: " + type + "\n");
	}

	public GoogleActionType getType(){
		return type;
	}


	public void setType(GoogleActionType newActionType){
		type = newActionType;
	}
}
