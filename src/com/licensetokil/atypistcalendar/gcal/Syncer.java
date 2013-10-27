package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;

import com.google.gson.JsonObject;
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
			JsonObject requestBody;
			
			if(currentSyncAction instanceof AddSyncAction) {
				Task localTask = ((AddSyncAction)currentSyncAction).getLocalTask();
				if(localTask instanceof Schedule) {
					Schedule localSchedule = (Schedule)localTask;
										
					requestBody = new JsonObject();
					requestBody.addProperty("summary", localSchedule.getDescription());
					requestBody.addProperty("description", localSchedule.getDescription());
					requestBody.addProperty("location", localSchedule.getPlace());
					requestBody.add("start", GoogleCalendarUtilities.createDateTimeObject(localSchedule.getStartTime()));
					requestBody.add("end", GoogleCalendarUtilities.createDateTimeObject(localSchedule.getEndTime()));
					requestBody.add("extendedProperties", GoogleCalendarUtilities.createExtendedPropertiesObject(localSchedule.getUniqueID()));
				}
				else if(localTask instanceof Deadline) {
					Deadline localSchedule = (Deadline)localTask;
					
					requestBody = new JsonObject();
					requestBody.addProperty("summary", localSchedule.getDescription());
					requestBody.addProperty("description", localSchedule.getDescription());
					requestBody.addProperty("location", localSchedule.getPlace());
					requestBody.add("start", GoogleCalendarUtilities.createDateTimeObject(localSchedule.getEndTime()));
					requestBody.add("end", GoogleCalendarUtilities.createDateTimeObject(localSchedule.getEndTime()));
					requestBody.add("extendedProperties", GoogleCalendarUtilities.createExtendedPropertiesObject(localSchedule.getUniqueID()));
				}
				else if(localTask instanceof Todo) {
					;
				}
				else {
					assert false;
				}
				
				try {
					HttpsConnectionHelper.sendJsonRequest(
							"https://www.googleapis.com/calendar/v3/calendars/" +
									GoogleCalendarManager.getInstance().getRemoteCalendarId() + 
									"/events",
							HttpsConnectionHelper.REQUEST_METHOD_POST,
							AuthenticationManager.getInstance().getAuthorizationHeader(),
							requestBody);
				} catch (IllegalStateException | JsonParseException | IOException e) {
					// TODO re-enqueue task?
					e.printStackTrace();
				}
			}
			else if(currentSyncAction instanceof UpdateSyncAction) {
				System.out.println("updated");
			}
			else if(currentSyncAction instanceof DeleteSyncAction) {				
				String remoteTaskID = ((DeleteSyncAction)currentSyncAction).getRemoteTaskID();
				try {
					HttpsConnectionHelper.sendJsonRequest(
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
