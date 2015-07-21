package br.com.mariana.vendas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;


public class NovaVendaActivity extends ActionBarActivity {

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
    }
}
