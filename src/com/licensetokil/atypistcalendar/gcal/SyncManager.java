package com.licensetokil.atypistcalendar.gcal;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.licensetokil.atypistcalendar.tasksmanager.Task;

class SyncManager {
	private static SyncManager instance = null;
	
	private ConcurrentLinkedQueue<SyncAction> queue;
	private Syncer syncer;
	private String remoteCalendarId;
	
	private SyncManager() {
		queue = new ConcurrentLinkedQueue<SyncAction>();
		syncer = null;
		remoteCalendarId = null;
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
}
