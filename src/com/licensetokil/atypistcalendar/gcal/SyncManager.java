package com.licensetokil.atypistcalendar.gcal;

import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.licensetokil.atypistcalendar.tasksmanager.Task;

class SyncManager {
	private static SyncManager instance = null;
	
	private ConcurrentLinkedQueue<SyncAction> queue;
	private Syncer syncer;
	private String remoteCalendarId;
	
	//TODO field-like objects - how?
	private final Calendar remoteTodoStartEndDate;
	private final JsonArray remoteTodoRecurrenceProperty;
	
	private SyncManager() {
		queue = new ConcurrentLinkedQueue<SyncAction>();
		syncer = null;
		remoteCalendarId = null;
		
		remoteTodoRecurrenceProperty = new JsonArray();
		remoteTodoRecurrenceProperty.add(new JsonPrimitive("RRULE:FREQ=MONTHLY"));
		
		remoteTodoStartEndDate = Calendar.getInstance();
		remoteTodoStartEndDate.set(2013, 1, 1);
	}
	
	protected static SyncManager getInstance() {
		if(instance == null) {
			instance = new SyncManager();
		}
		return instance;
	}
	
	protected void initialiseRemoteCalendar() {
		queue.add(new InitialiseRemoteCalendarSyncAction());
		runSyncer();
	}
	
	protected void addRemoteTask(Task localTask) {
		queue.add(new AddSyncAction(localTask));
		runSyncer();
	}
	
	protected void updateRemoteTask(Task localTask, String remoteTaskID) {
		queue.add(new UpdateSyncAction(localTask, remoteTaskID));
		runSyncer();
	}
	
	protected void deleteRemoteTask(String remoteTaskID) {		
		queue.add(new DeleteSyncAction(remoteTaskID));
		runSyncer();
	}
	
	protected void doCompleteSync() {
		queue.add(new DoCompleteSyncAction());
		runSyncer();
	}
	
	private void runSyncer() {
		if(syncer == null || !syncer.isAlive()) {
			syncer = new Syncer();
			syncer.start();
		}
		//else, Syncer is alive, do nothing
	}

	protected ConcurrentLinkedQueue<SyncAction> getQueue() {
		return queue;
	}
	
	protected String getRemoteCalendarId() {
		return remoteCalendarId;
	}

	protected void setRemoteCalendarId(String remoteCalendarId) {
		this.remoteCalendarId = remoteCalendarId;
	}
	
	protected JsonArray getRemoteTodoRecurrenceProperty() {
		return remoteTodoRecurrenceProperty;
	}
	
	protected Calendar getRemoteTodoStartEndDate() {
		return remoteTodoStartEndDate;
	}
}
