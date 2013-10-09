package com.licensetokil.atypistcalendar.parser;

public class MalformedUserInputException extends Exception{
	public MalformedUserInputException(){
		super();
	}
	public MalformedUserInputException(String message){
		super(message);
	}
}
