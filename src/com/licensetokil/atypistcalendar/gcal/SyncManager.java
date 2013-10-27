package com.licensetokil.atypistcalendar.gcal;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.JsonObject;
import com.licensetokil.atypistcalendar.tasksmanager.Task;

public class SyncManager {
	private static SyncManager instance = null;
	
	private ConcurrentLinkedQueue<SyncAction> queue;
	private Syncer syncer;
	
	private SyncManager() {
		queue = new ConcurrentLinkedQueue<SyncAction>();
		syncer = null;
	}
	
	public static SyncManager getInstance() {
		if(instance == null) {
			instance = new SyncManager();
		}
		return instance;
	}
	
	public void addRemoteTask(Task localTask) {
		queue.add(new AddSyncAction(localTask));
		runSyncer();
	}
	
	public void updateRemoteTask(Task localTask, String remoteTaskID) {
		queue.add(new UpdateSyncAction(localTask, remoteTaskID));
		runSyncer();
	}
	
	public void deleteRemoteTask(String remoteTaskID) {
		queue.add(new DeleteSyncAction(remoteTaskID));
		runSyncer();
	}
	
	private void runSyncer() {
		if(syncer == null || !syncer.isAlive()) {
			syncer = new Syncer();
			syncer.start();
		}
		else {
			//Syncer is alive, do nothing
		}
	}

	public ConcurrentLinkedQueue<SyncAction> getQueue() {
		return queue;
	}
}
