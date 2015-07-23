package br.com.mariana.vendas;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
            strURL.append("http://192.168.1.10/vendas/inserir.php?produto=");
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
        db.close();
    }
}
