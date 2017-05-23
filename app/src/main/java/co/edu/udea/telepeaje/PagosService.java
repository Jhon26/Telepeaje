package co.edu.udea.telepeaje;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.edu.udea.telepeaje.HistorialPagosFragment;
import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.R;

/**
 * Created by JHON on 23/05/2017.
 */

public class PagosService extends Service {

    //Builder para la notificación
    NotificationCompat.Builder mBuilder;

    //Id para la notificación
    int mNotificationId = 123;

    private String TAG = "SERVICIO";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Servicio creado");
    }

    /**
     * Se evalua constantemente el estado de la referencia "Recibos" a la espera de información nueva. Cuando se agregan datos,
     * se envía una notificación con los datos agregados.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio iniciado");

        //Lectura de datos de la referencia Recibos
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        String UID = misPreferencias.getString("UID", "");
        DatabaseReference recibosRef = FirebaseDatabase.getInstance().getReference(FirebaseReferences.RECIBOS_REFERENCE);
        //Cuando se agregan datos, se construye una notificación
        recibosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Configuración de la notificación
                mBuilder =
                        new NotificationCompat.Builder(PagosService.this)
                                .setSmallIcon(R.mipmap.ic_launcher_round)
                                .setContentTitle("¡Pago realizado!")
                                .setAutoCancel(true)
                                .setVisibility(Notification.VISIBILITY_PUBLIC)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentText("Se ha realizado el cobro de $xxx COP del peaje xxx para el auto xxx");

                //Activity que se lanza al hacer click en la notificación
                Intent resultIntent = new Intent(PagosService.this, HistorialPagosFragment.class);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                PagosService.this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return START_REDELIVER_INTENT;//Crea de nuevo el servicio si es finalizado por el sistema, usando el intent que lo lanzó
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Servicio destruído");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
