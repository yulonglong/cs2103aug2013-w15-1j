package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;

public class Todo extends Task implements Comparable<Todo>, Cloneable {
	private static final String OUTPUT_FORMAT = "[Status: %s] %s";
	private String remoteId;
	private TaskType taskType;
	private int uniqueId;
	private Calendar lastModifiedDate;
	private String description;
	private String place;
	private String status;

	public Todo(int uniqueId, String description, String place, String status,
			Calendar lastModifiedDate) {
		this.uniqueId = uniqueId;
		this.taskType = TaskType.TODO;
		this.description = description;
		this.place = place;
		this.status = status;
		this.lastModifiedDate = lastModifiedDate;
	}

	public Todo(Todo td) {
		this.taskType = TaskType.TODO;
		this.description = td.getDescription();
		this.place = td.getPlace();
		this.uniqueId = td.getUniqueId();
		this.status = td.getStatus();
	}

	public Todo() {
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

	public String getDescription() {
		return description;
	}

	public String getPlace() {
		return place;
	}

	public String getStatus() {
		return status;
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

	public void setLastModifiedDate(Calendar lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public void setTaskType(TaskType t) {
		this.taskType = t;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String toString() {
		if (place.equals(EMPTY_STRING)) {
			return TaskType.TODO + DELIMITER + uniqueId + DELIMITER
					+ description + DELIMITER + BLANK_SPACE + DELIMITER
					+ status + DELIMITER + lastModifiedDate.getTime()
					+ DELIMITER + remoteId;
		}
		return TaskType.TODO + DELIMITER + uniqueId + DELIMITER + description
				+ DELIMITER + place + DELIMITER + status + DELIMITER
				+ lastModifiedDate.getTime() + DELIMITER + remoteId;
	}

	public String outputStringForDisplay() {
		String stringStatus = status;
		if (stringStatus.equals(DONE_NO_ALIGN)) {
			stringStatus = DONE_ALIGN;
		}

		String output = String.format(OUTPUT_FORMAT, stringStatus, description);
		if (!place.equals(EMPTY_STRING)) {
			output = String.format(DISPLAY_PLACE_NOT_EMPTY, output,
					this.getPlace());
		}

		return output;
	}

	public int compareTo(Todo td) {
		return description.compareTo(td.getDescription());
	}

	@Override
	public Object clone() {
		Todo clonedObject = new Todo();
		clonedObject.remoteId = this.remoteId;
		clonedObject.taskType = this.taskType;
		clonedObject.uniqueId = this.uniqueId;
		clonedObject.description = this.description;
		clonedObject.place = this.place;
		clonedObject.status = this.status;
		clonedObject.lastModifiedDate = (Calendar) this.lastModifiedDate
				.clone();

		return clonedObject;
	}
}
