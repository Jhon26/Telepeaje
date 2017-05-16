package co.edu.udea.telepeaje;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by JHON on 16/05/2017.
 */

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService{

    public static final String TAG = "RECIBOS";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token: "+token);
    }
}
