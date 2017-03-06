package de.tudarmstadt.informatik.tk.shhparty.sensing;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by rohit on 02-03-2017.
 */

public class ActivityRecognizedService extends IntentService {

    public static boolean dancing;

    public ActivityRecognizedService() {
        super("de.tudarmstadt.informatik.tk.shhparty.sensing.ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity mostProbableActivity
                    = result.getMostProbableActivity();

            // Get the confidence % (probability)
            int confidence = mostProbableActivity.getConfidence();

            // Get the type
            int activityType = mostProbableActivity.getType();
           /* types:
            * DetectedActivity.IN_VEHICLE 0
            * DetectedActivity.ON_BICYCLE 1
            * DetectedActivity.ON_FOOT 2
            * DetectedActivity.STILL 3
            * DetectedActivity.UNKNOWN 4
            * DetectedActivity.TILTING 5
            * WALKING 7
            * RUNNING 8
            */

            Log.v("ActivityRecognizedSvc", "activityType: " + activityType);
            Log.v("ActivityRecognizedSvc", "activity confidence: " + confidence);

            if(DetectedActivity.STILL == activityType && confidence > 50){
                dancing = false;
            } else {
                dancing = true;
            }
        }
    }
}