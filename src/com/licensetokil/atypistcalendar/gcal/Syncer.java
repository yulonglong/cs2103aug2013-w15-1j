package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;

import com.google.gson.JsonParseException;
import com.licensetokil.atypistcalendar.tasksmanager.Deadline;
import com.licensetokil.atypistcalendar.tasksmanager.Schedule;
import com.licensetokil.atypistcalendar.tasksmanager.Task;
import com.licensetokil.atypistcalendar.tasksmanager.Todo;

public class Syncer extends Thread {
	public Syncer() {
	}
	
	public void run() {
		SyncAction currentSyncAction = SyncManager.getInstance().getQueue().peek();
		
		while(currentSyncAction != null) {
			if(currentSyncAction instanceof AddSyncAction) {
				Task localTask = ((AddSyncAction)currentSyncAction).getLocalTask();
				if(localTask instanceof Schedule) {
					;
				}
				else if(localTask instanceof Deadline) {
					;
				}
				else if(localTask instanceof Todo) {
					;
				}
				else {
					assert false;
				}
			}
			else if(currentSyncAction instanceof UpdateSyncAction) {
				System.out.println("updated");
			}
			else if(currentSyncAction instanceof DeleteSyncAction) {				
				String remoteTaskID = ((DeleteSyncAction)currentSyncAction).getRemoteTaskID();
				try {
					HttpsConnectionHelper.sendUrlencodedFormRequest(
							"https://www.googleapis.com/calendar/v3/calendars/" + 
										GoogleCalendarManager.getInstance().getRemoteCalendarId() + 
										"/events/" + 
										remoteTaskID,
							HttpsConnectionHelper.REQUEST_METHOD_DELETE,
							AuthenticationManager.getInstance().getAuthorizationHeader(),
							null);
				} catch (IllegalStateException | JsonParseException | IOException e) {
					// TODO re-enqueue task?
					e.printStackTrace();
				}
			}
			else {
				assert false;
			}
			
			SyncManager.getInstance().getQueue().poll(); //drop the item that has been sync'ed
			currentSyncAction = SyncManager.getInstance().getQueue().peek();
		}
	}
}
