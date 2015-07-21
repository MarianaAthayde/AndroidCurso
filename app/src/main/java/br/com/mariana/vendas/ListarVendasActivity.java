package br.com.mariana.vendas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ListarVendasActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_vendas);

        SQLiteDatabase db = openOrCreateDatabase("vendas.db", Context.MODE_PRIVATE,null);
        ListView lvwVendas = (ListView)findViewById(R.id.ltw_Vendas);
        Cursor cursor = db.rawQuery("SELECT vendas._id,vendas.preco, vendas.la, vendas.lo, produtos.nome FROM vendas INNER JOIN produtos on produtos._id=vendas.produto ORDER BY nome asc", null);

        String[] from = {"_id", "preco","nome","la","lo"};
        int [] to = {R.id.txvId,R.id.txvPreco,R.id.txvNome,R.id.txvLa,R.id.txvLo};

        SimpleCursorAdapter ad = new SimpleCursorAdapter(getBaseContext(),R.layout.model_listar,cursor,from,to);

        lvwVendas.setAdapter(ad);
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listar_vendas, menu);
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
}
