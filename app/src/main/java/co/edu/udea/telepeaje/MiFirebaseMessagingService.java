package co.edu.udea.telepeaje;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by JHON on 16/05/2017.
 */

public class MiFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "RECIBOS";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Log.d(TAG, "Mensaje recibido de "+from);
        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Notificación: "+remoteMessage.getNotification().getBody());
        }
    }
}
