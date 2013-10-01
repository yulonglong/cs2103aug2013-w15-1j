package com.licensetokil.atypistcalendar;
import com.licensetokil.atypistcalendar.parser.Parser;

public class ATypistCalendar {
	public static void main(String[] args) {
		System.out.println("Hello! Welcome to  A typist calendar!");
		System.out.println(Parser.Parse("add swimming at CommunityClub on 21/11 from 1300 to 1400"));
		System.out.println(Parser.Parse("display on 21/11 from 1300 to 1400"));
	}
}
