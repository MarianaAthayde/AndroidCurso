package br.com.mariana.vendas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExportarVendasService extends Service implements Runnable{
    public void onCreate() {
        new Thread(ExportarVendasService.this).start();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void run() {
        SQLiteDatabase db = openOrCreateDatabase("vendas.db", Context.MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("SELECT * FROM vendas", null);

        int totalDB = cursor.getCount();
        int totalReplicado = 0;

        while(cursor.moveToNext()){
            StringBuilder strURL = new StringBuilder();
            //http://mathayde.net46.net/vendas/inserir.php?produto=1&preco=2.50&latitude=1333333&longitude=1999999
            //http://192.168.1.10/vendas/inserir.php?produto=1&preco=2.50&latitude=1333333&longitude=1999999
            strURL.append("http://mathayde.byethost9.com/vendas/inserir.php?produto=");
            strURL.append(cursor.getInt(cursor.getColumnIndex("produto")));
            strURL.append("&preco=");
            strURL.append(cursor.getDouble(cursor.getColumnIndex("preco")));
            strURL.append("&latitude=");
            strURL.append(cursor.getDouble(cursor.getColumnIndex("la")));
            strURL.append("&longitude=");
            strURL.append(cursor.getDouble(cursor.getColumnIndex("lo")));
            //Log.d("URL:", strURL.toString());
            try{
                URL url = new URL(strURL.toString());
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                InputStreamReader ips = new InputStreamReader(http.getInputStream());
                BufferedReader line = new BufferedReader(ips);

                if(line.readLine().equals("Y")){
                    db.delete("vendas" , "_id=?" , new String[]{String.valueOf(cursor.getInt(0))});
                    totalReplicado ++;
                    Log.d("URL:", String.valueOf(totalReplicado));
                }

            }catch(Exception e){
            }
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification nt = new Notification(R.drawable.sync,"Status Replicação", System.currentTimeMillis());

        if( totalDB == totalReplicado){
            nt.flags |= Notification.FLAG_AUTO_CANCEL;
            PendingIntent p = PendingIntent.getActivity(this,0,new Intent(this.getApplicationContext(),MainActivity.class), 0);

            nt.setLatestEventInfo(this, "Status Replicação", "Replicação efetuada com Sucesso! Total: " + totalReplicado, p);
        }else{
            nt.flags |= Notification.FLAG_AUTO_CANCEL;
            PendingIntent p = PendingIntent.getActivity(this,0,new Intent(this.getApplicationContext(),MainActivity.class), 0);

            nt.setLatestEventInfo(this, "Status Replicação", "Replicação não foi efetuada com Sucesso! Total: " + totalReplicado + "de" + totalDB, p);

        }

        nt.vibrate = new long[]{100,2000,1000,2000};
        notificationManager.notify((int)Math.round(Math.random()),nt);

        db.close();
        stopSelf();
    }
}
