//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;

public class MalformedUserInputException extends Exception{
	private static final long serialVersionUID = 1L;
	public MalformedUserInputException(){
		super();
	}
	public MalformedUserInputException(String message){
		super(message);
	}
}
