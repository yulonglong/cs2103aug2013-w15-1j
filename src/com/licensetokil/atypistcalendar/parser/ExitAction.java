//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;

public class ExitAction extends SystemAction{
	public ExitAction(){
		type = SystemActionType.EXIT;
	}
	public SystemActionType getType(){
		return type;
	}
}
