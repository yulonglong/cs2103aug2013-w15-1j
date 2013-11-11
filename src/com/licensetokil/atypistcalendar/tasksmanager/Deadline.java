//@author A0103494J
package com.licensetokil.atypistcalendar.tasksmanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Deadline extends Task implements Comparable<Deadline>, Cloneable {
	private static final String OUTPUT_FORMAT = "[%s] [by %s] [Status: %s] %s";
	private String remoteId;
	private TaskType taskType;
	private int uniqueId;
	private Calendar lastModifiedDate;
	private Calendar endTime;
	private String description;
	private String place;
	private String status;

	public Deadline(int uniqueId, Calendar endTime, String description,
			String place, String status, Calendar lastModifiedDate) {
		this.taskType = TaskType.DEADLINE;
		this.endTime = endTime;
		this.description = description;
		this.place = place;
		this.uniqueId = uniqueId;
		this.status = status;
		this.lastModifiedDate = lastModifiedDate;
	}

	public Deadline() {
	}

	public String getRemoteId() {
		return remoteId;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public Calendar getLastModifiedDate() {
		return lastModifiedDate;
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

	public String getStatus() {
		return status;
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

	public void setStatus(String s) {
		this.status = s;
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

	public void setLastModifiedDate(Calendar lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String toString() {
		if (place.equals(EMPTY_STRING)) {
			return TaskType.DEADLINE + DELIMITER + uniqueId + DELIMITER
					+ endTime.getTime() + DELIMITER + description + DELIMITER
					+ BLANK_SPACE + DELIMITER + status + DELIMITER
					+ lastModifiedDate.getTime() + DELIMITER + remoteId;
		}
		return TaskType.DEADLINE + DELIMITER + uniqueId + DELIMITER
				+ endTime.getTime() + DELIMITER + description + DELIMITER
				+ place + DELIMITER + status + DELIMITER
				+ lastModifiedDate.getTime() + DELIMITER + remoteId;
	}

	public String outputStringForDisplay() {
		SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
		SimpleDateFormat formatDay = new SimpleDateFormat("EEE, MMM dd, ''yy");

		String stringStatus = status;
		if (stringStatus.equals(DONE_NO_ALIGN)) {
			stringStatus = DONE_ALIGN;
		}

		String output = String
				.format(OUTPUT_FORMAT, formatDay.format(endTime.getTime()),
						formatTime.format(endTime.getTime()), stringStatus,
						description);
		if (!place.equals(EMPTY_STRING)) {
			output = String.format(DISPLAY_PLACE_NOT_EMPTY, output,
					this.getPlace());
		}

		return output;
	}

	public int compareTo(Deadline d) {
		return endTime.compareTo(d.getEndTime());
	}

	@Override
	public Object clone() {
		Deadline clonedObject = new Deadline();
		clonedObject.remoteId = this.remoteId;
		clonedObject.taskType = this.taskType;
		clonedObject.uniqueId = this.uniqueId;
		clonedObject.description = this.description;
		clonedObject.status = this.status;
		clonedObject.place = this.place;
		clonedObject.endTime = (Calendar) this.endTime.clone();
		clonedObject.lastModifiedDate = (Calendar) this.lastModifiedDate
				.clone();

		return clonedObject;
	}
}
//@author A0103494J
