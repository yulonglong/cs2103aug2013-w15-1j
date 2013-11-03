//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;


public class UndoAction extends LocalAction{
	
	public UndoAction(){
		type = LocalActionType.UNDO;
	}
	
	public String toString(){
		return ("Type	: " + type + "\n" );
	}

	public LocalActionType getType(){
		return type;
	}
}