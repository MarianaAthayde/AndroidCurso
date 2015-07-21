package br.com.mariana.vendas;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db =  openOrCreateDatabase("vendas.db", Context.MODE_PRIVATE,null);


        StringBuilder sqlProdutos = new StringBuilder();
        sqlProdutos.append("CREATE TABLE IF NOT EXISTS [produtos](");
        sqlProdutos.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT,");
        sqlProdutos.append("nome varchar(100),");
        sqlProdutos.append("preco DOUBLE(10,2));");
        db.execSQL(sqlProdutos.toString());
        db.execSQL("INSERT INTO produtos(nome,preco) VALUES('Coca-Cola','2.50')");
        db.execSQL("INSERT INTO produtos(nome,preco) VALUES('Guarana','1.50')");
        db.execSQL("INSERT INTO produtos(nome,preco) VALUES('Fanta','1.25')");
        db.execSQL("INSERT INTO produtos(nome,preco) VALUES('Sprite','1.25')");

        StringBuilder sqlVendas = new StringBuilder();
        sqlVendas.append("CREATE TABLE IF NOT EXISTS [vendas](");
        sqlVendas.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT,");
        sqlVendas.append("produto INTEGER,");
        sqlVendas.append("preco DOUBLE(10,2),");
        sqlVendas.append("la DOUBLE(10,9),");
        sqlVendas.append("lo DOUBLE(10,9));");
        db.execSQL(sqlVendas.toString());

        db.close();
    }

    public void NovaVenda_Click(View view){
        startActivity(new Intent(getBaseContext(),NovaVendaActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
