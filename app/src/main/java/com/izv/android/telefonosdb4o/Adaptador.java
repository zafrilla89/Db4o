package com.izv.android.telefonosdb4o;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by 2dam on 10/10/2014.
 */
public class Adaptador extends ArrayAdapter<Telefono> {
    private Context contexto;
    private ArrayList<Telefono> lista;
    private int recurso;
    private Random r=new Random();
    static LayoutInflater i;

    public Adaptador(Context context, int resource, ArrayList<Telefono> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.lista=objects;
        this.recurso=resource;
        this.i=(LayoutInflater)contexto.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public TextView tvmarca, tvmodelo, tvprecio, tvstock;
        public ImageView ivborrar, iveditar;
        public int posicion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvmarca = (TextView) convertView.findViewById(R.id.tvmarca);
            vh.tvmodelo = (TextView) convertView.findViewById(R.id.tvmodelo);
            vh.tvprecio =(TextView)convertView.findViewById(R.id.tvprecio);
            vh.tvstock=(TextView)convertView.findViewById(R.id.tvstock);
            vh.iveditar =(ImageView)convertView.findViewById(R.id.iveditar);
            vh.ivborrar =(ImageView)convertView.findViewById(R.id.ivborrar);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Telefono tl;
        tl=lista.get(position);
        vh.posicion=position;
        vh.tvmarca.setText(tl.getMarca());
        vh.tvmodelo.setText(tl.getModelo());
        vh.tvprecio.setText(tl.getPrecio()+"€");
        vh.tvstock.setText(tl.getStock());
        vh.ivborrar.setTag(position);
        vh.iveditar.setTag(position);
        return convertView;
    }

}
