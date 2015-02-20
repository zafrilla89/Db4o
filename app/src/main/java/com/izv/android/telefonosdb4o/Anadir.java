package com.izv.android.telefonosdb4o;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Anadir extends Activity {
    private EditText etmarca, etmodelo, etprecio, etstock;
    private String marca, modelo, precio, stock;
    private boolean semaforo=false;
    private ArrayList <Telefono> datos;
    private Telefono tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_anadir);
        Bundle b = getIntent().getExtras();
        if(b !=null ){
            datos = (ArrayList)b.getSerializable("datos");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_anadir, menu);
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


    public boolean comprueba(Telefono tl2){
        Telefono tl;
        for (int i=0;i<datos.size();i++){
            tl=datos.get(i);
            if(tl.equals(tl2)==true){
                return false;
            }
        }
        return true;
    }

    public void guardar(View view){
        etmarca = (EditText) findViewById(R.id.etamarca);
        etmodelo = (EditText) findViewById(R.id.etamodelo);
        etprecio=(EditText)findViewById(R.id.etaprecio);
        etstock=(EditText)findViewById(R.id.etastock);
        marca=etmarca.getText().toString().trim();
        modelo=etmodelo.getText().toString().trim();
        precio=etprecio.getText().toString();
        stock=etstock.getText().toString();
        if (marca.length() > 0 && modelo.length() > 0 && precio.length() > 0 && stock.length() > 0 ) {
            boolean comprobar=true;
            tl = new Telefono(marca, modelo, precio, stock);
            comprobar=comprueba(tl);
            if(comprobar==true){
                datos.add(tl);
                tostada("TELÉFONO REGISTRADO");
                Intent i = new Intent();
                Bundle b = new Bundle();
                b.putParcelable("telefono", tl);
                i.putExtras(b);
                setResult(RESULT_OK,i);
                this.finish();
            }else{
                tostada("TELÉFONO REPETIDO");
            }
            Collections.sort(datos);
            semaforo=true;
        }else{
            semaforo=false;
        }
        if(semaforo==false) {
            tostada("NO RELLENASTE TODOS LOS CAMPOS");
        }
    }

    public void tostada (String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


}
