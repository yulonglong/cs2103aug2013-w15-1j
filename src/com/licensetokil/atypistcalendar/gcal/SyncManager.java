package com.licensetokil.atypistcalendar.gcal;

import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.licensetokil.atypistcalendar.tasksmanager.Task;

class SyncManager {
	private static SyncManager instance = null;

	private static Logger logger = Logger.getLogger(SyncManager.class.getName());

	private Lock goToSleepLock = new ReentrantLock();
	private Condition goToSleepCondition = goToSleepLock.newCondition();

	private ConcurrentLinkedQueue<SyncAction> queue;
	private Syncer syncer;
	private String remoteCalendarId;

	//TODO these objects are mutable! need to make them immutable.
	protected static final Calendar REMOTE_TODO_START_END_DATE = Calendar.getInstance();
	protected static final JsonArray REMOTE_TODO_RECURRENCE_PROPERTY = new JsonArray();

	static {
		REMOTE_TODO_START_END_DATE.set(2013, Calendar.JANUARY, 1);
		REMOTE_TODO_RECURRENCE_PROPERTY.add(new JsonPrimitive("RRULE:FREQ=MONTHLY"));
	}

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
		deleteRemoteTask(remoteTaskID);
		addRemoteTask(localTask);
		//TODO temporary measure, cause increasing the iCal sequence number is a pain in the...
		//queue.add(new UpdateSyncAction(localTask, remoteTaskID));
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
		else {
			//Signaling... if needed.
			goToSleepLock.lock();
			goToSleepCondition.signal();
			goToSleepLock.unlock();
		}
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

	protected Lock getGoToSleepLock() {
		return goToSleepLock;
	}

	protected Condition getGoToSleepCondition() {
		return goToSleepCondition;
	}
}
