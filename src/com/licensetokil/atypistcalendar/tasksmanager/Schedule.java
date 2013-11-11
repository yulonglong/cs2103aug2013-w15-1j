//@author A0103494J
package com.licensetokil.atypistcalendar.tasksmanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Schedule extends Task implements Comparable<Schedule>, Cloneable {
	private static final String OUTPUT_FORMAT = "[%s] [%s - %s] %s";

	private String remoteId;
	private TaskType taskType;
	private int uniqueId;
	private Calendar lastModifiedDate;
	private Calendar startTime;
	private Calendar endTime;
	private String description;
	private String place;

	public Schedule(int uniqueId, Calendar startTime, Calendar endTime,
			String description, String place, Calendar lastModifiedDate) {
		this.taskType = TaskType.SCHEDULE;
		this.uniqueId = uniqueId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.place = place;
		this.lastModifiedDate = lastModifiedDate;
	}

	public Schedule() {
	}

	public String getRemoteId() {
		return remoteId;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public Calendar getLastModifiedDate() {
		return lastModifiedDate;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public String getDescription() {
		return description;
	}

	public String getPlace() {
		return place;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	public void setTaskType(TaskType t) {
		this.taskType = t;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public void setStartTime(Calendar st) {
		this.startTime = st;
	}

	public void setEndTime(Calendar st) {
		this.endTime = st;
	}

	public void setDescription(String d) {
		this.description = d;
	}

	public void setPlace(String p) {
		this.place = p;
	}

	public void setLastModifiedDate(Calendar lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String toString() {
		if (place.equals(EMPTY_STRING)) {
			return TaskType.SCHEDULE + DELIMITER + uniqueId + DELIMITER
					+ startTime.getTime() + DELIMITER + endTime.getTime()
					+ DELIMITER + description + DELIMITER + BLANK_SPACE
					+ DELIMITER + lastModifiedDate.getTime() + DELIMITER
					+ remoteId;
		}
		return TaskType.SCHEDULE + DELIMITER + uniqueId + DELIMITER
				+ startTime.getTime() + DELIMITER + endTime.getTime()
				+ DELIMITER + description + DELIMITER + place + DELIMITER
				+ lastModifiedDate.getTime() + DELIMITER + remoteId;
	}

	public String outputStringForDisplay() {
		SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
		SimpleDateFormat formatDay = new SimpleDateFormat("EEE, MMM dd, ''yy");

		String output = String.format(OUTPUT_FORMAT,
				formatDay.format(startTime.getTime()),
				formatTime.format(startTime.getTime()),
				formatTime.format(endTime.getTime()), description);
		if (!place.equals(EMPTY_STRING)) {
			output = String.format(DISPLAY_PLACE_NOT_EMPTY, output,
					this.getPlace());
		}

		return output;
	}

	public int compareTo(Schedule s) {
		return startTime.compareTo(s.getStartTime());
	}

	@Override
	public Object clone() {
		Schedule clonedObject = new Schedule();
		clonedObject.remoteId = this.remoteId;
		clonedObject.taskType = this.taskType;
		clonedObject.uniqueId = this.uniqueId;
		clonedObject.description = this.description;
		clonedObject.place = this.place;
		clonedObject.startTime = (Calendar) this.startTime.clone();
		clonedObject.endTime = (Calendar) this.endTime.clone();
		clonedObject.lastModifiedDate = (Calendar) this.lastModifiedDate
				.clone();

		return clonedObject;
	}
}
//@author A0103494J
