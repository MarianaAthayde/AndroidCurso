package br.com.mariana.vendas;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class NovaVendaActivity extends ActionBarActivity implements LocationListener {

    private double la;
    private double lo;
    LocationManager locationManager;
    ProgressDialog pgd = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_venda);

        Spinner spProdutos = (Spinner)findViewById(R.id.sp_produto);

        SQLiteDatabase db = openOrCreateDatabase("vendas.db", Context.MODE_PRIVATE,null);

        Cursor cursor = db.rawQuery("SELECT * FROM produtos ORDER BY nome asc", null);

        String[] from = {"_id", "nome","preco"};
        int [] to = {R.id.txvsp_id,R.id.txvsp_nome,R.id.txvsp_preco};

        SimpleCursorAdapter ad = new SimpleCursorAdapter(getBaseContext(),R.layout.spinner,cursor,from,to);

        spProdutos.setAdapter(ad);

        db.close();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nova_venda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Salvar_Click(View view){
        //LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Criteria criteria = new Criteria();
        //String provider = locationManager.getBestProvider(criteria, false);
        String provider = "gps";
        //Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 5000,0,this);
        pgd = ProgressDialog.show(NovaVendaActivity.this,"Aguarde...", "Buscando a Localização!", true,false,null);

        }

    @Override
    public void onLocationChanged(Location location) {
        pgd.dismiss();
        la = location.getLatitude();
        lo = location.getLongitude();


        SQLiteDatabase db = openOrCreateDatabase("vendas.db", Context.MODE_PRIVATE,null);

        Spinner spProdutos = (Spinner)findViewById(R.id.sp_produto);
        SQLiteCursor dados = (SQLiteCursor)spProdutos.getAdapter().getItem(spProdutos.getSelectedItemPosition());

        ContentValues ctv = new ContentValues();
        ctv.put("produto", dados.getInt(0));
        ctv.put("preco", dados.getDouble(2));
        ctv.put("la", la);
        ctv.put("lo", lo);
        if(db.insert("vendas","_id",ctv) > 0){
            Toast.makeText(getBaseContext(),"Sucesso!",Toast.LENGTH_LONG).show();
        }
        db.close();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getBaseContext(),"GPS Desativado!",Toast.LENGTH_LONG);
    }
}
