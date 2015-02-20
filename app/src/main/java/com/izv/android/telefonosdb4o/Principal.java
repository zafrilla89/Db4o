package com.izv.android.telefonosdb4o;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Principal extends Activity {
    private ArrayList<Telefono> datos;
    private Adaptador ad;
    private ListView lv;
    private EditText etmarca, etmodelo, etprecio, etstock;
    String marca, modelo, precio, stock;
    boolean semaforo=false;
    private ObjectContainer bd;
    private Telefono telefono;
    private final static int ANADIR=1;

    /***********************************************************************/
    /*                                                                     */
    /*                              METODOS ON                             */
    /*                                                                     */
    /***********************************************************************/
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK &&
                requestCode == ANADIR) {
            Telefono t= data.getParcelableExtra("telefono");
            bd.store(t);
            bd.commit();
            datos = new ArrayList();
            leerdatos();
            Collections.sort(datos);
            ad = new Adaptador(this,R.layout.itemlista,datos);
            lv = (ListView)findViewById(R.id.lvlista);
            lv.setAdapter(ad);
            registerForContextMenu(lv);;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index=info.position;
        if (id == R.id.m_editar) {
            editar(index);
        }else {
            if (id == R.id.m_borrar) {
                datos.remove(index);
                ad.notifyDataSetChanged();
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        iniciarComponentes();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menucontextual,menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        bd.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.m_anadir) {
            return anadir();
        }
        return super.onOptionsItemSelected(item);
    }



    /***********************************************************************/
    /*                                                                     */
    /*                        METODOS AUXILIARES                           */
    /*                                                                     */
    /***********************************************************************/

    public boolean anadir(){
        Intent i = new Intent(this,Anadir.class);
        Bundle b = new Bundle();
        b.putSerializable("datos", datos);
        i.putExtras(b);
        startActivityForResult(i, ANADIR);
        return true;
    }

       public boolean comprueba(Telefono tl){
        Telefono tl2;
        for (int i=0;i<datos.size();i++){
            tl2=datos.get(i);
            if(tl.equals(tl2)){
                return false;
            }
        }
        return true;
    }

    public boolean editar(final int index){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.editartelefono);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo, null);
        alert.setView(vista);
        etmarca = (EditText) vista.findViewById(R.id.etmarca);
        etmodelo = (EditText) vista.findViewById(R.id.etmodelo);
        etprecio=(EditText)vista.findViewById(R.id.etprecio);
        etstock=(EditText)vista.findViewById(R.id.etstock);
        telefono=new Telefono();
        telefono=datos.get(index);
        marca=telefono.getMarca();
        modelo=telefono.getModelo();
        precio=telefono.getPrecio();
        stock=telefono.getStock();
        etmarca.setText(marca);
        etmodelo.setText(modelo);
        etprecio.setText(precio);
        etstock.setText(stock);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String id="";
                        etmarca = (EditText) vista.findViewById(R.id.etmarca);
                        etmodelo = (EditText) vista.findViewById(R.id.etmodelo);
                        etprecio=(EditText)vista.findViewById(R.id.etprecio);
                        etstock=(EditText)vista.findViewById(R.id.etstock);
                        marca=etmarca.getText().toString().trim();
                        modelo=etmodelo.getText().toString().trim();
                        precio=etprecio.getText().toString();
                        stock=etstock.getText().toString();
                        if (marca.length() > 0 && modelo.length() > 0 && precio.length() > 0 && stock.length() > 0 ) {
                            Telefono tl2=new Telefono(marca,modelo,precio,stock);
                            boolean comprobar=true;
                            comprobar=comprueba(tl2);
                            if(comprobar==true) {
                                editartelefono(tl2);
                                datos.set(index, tl2);
                                tostada("TELÉFONO EDITADO");
                            }else{
                                tostada("TELÉFONO REPETIDO");
                            }
                            Collections.sort(datos);
                            ad.notifyDataSetChanged();
                            semaforo=true;
                        }else{
                            semaforo=false;
                        }
                        if(semaforo==false) {
                            tostada("NO RELLENASTE TODOS LOS CAMPOS");
                        }
                    }
                });
        alert.setNegativeButton(android.R.string.no,null);
        alert.show();
        return true;
    }

    public void editartelefono(Telefono tl2){
        ObjectSet<Telefono> telefonos = bd.query(
                new Predicate<Telefono>() {
                    @Override
                    public boolean match(Telefono t) {
                        return t.getModelo().compareTo(telefono.getModelo())==0 && t.getMarca().compareTo(telefono.getMarca())==0;
                    }
                });
        if (telefonos.hasNext()){
            Telefono t = telefonos.next();
            t.setModelo(tl2.getModelo());
            t.setMarca(tl2.getMarca());
            t.setPrecio(tl2.getPrecio());
            t.setStock(tl2.getStock());
            bd.store(t);
            bd.commit();
        }
    }

    private void iniciarComponentes() {
        bd = Db4oEmbedded.openFile(
                Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) +
                        "/bd.db4o");
        datos = new ArrayList();
        leerdatos();
        Collections.sort(datos);
        ad = new Adaptador(this,R.layout.itemlista,datos);
        lv = (ListView)findViewById(R.id.lvlista);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
    }

    public void leerdatos(){
        List<Telefono> telefonos= bd.query(Telefono.class);
        for(Telefono t: telefonos){
            datos.add(t);
        }
    }

    public void tostada (String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    /***********************************************************************/
    /*                                                                     */
    /*                METODOS ONCLICK SOBRE LAS IMAGENES                   */
    /*                                                                     */
    /***********************************************************************/

    public void delete(View v){
        int index;
        index=(Integer)v.getTag();
        telefono=datos.get(index);
        ObjectSet<Telefono> telefonos = bd.query(
                new Predicate<Telefono>() {
                    @Override
                    public boolean match(Telefono t) {
                        return t.getModelo().compareTo(telefono.getModelo())==0 && t.getMarca().compareTo(telefono.getMarca())==0;
                    }
                });
        if (telefonos.hasNext()){
            Telefono t = telefonos.next();
            bd.delete(t);
            bd.commit();
        }
        datos.remove(index);
        ad.notifyDataSetChanged();
    }

    public void edit(View v){
        int index=-1;
        index=(Integer)v.getTag();
        editar(index);
    }

}