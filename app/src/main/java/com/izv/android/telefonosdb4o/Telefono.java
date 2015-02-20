package com.izv.android.telefonosdb4o;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 2dam on 10/10/2014.
 */
public class Telefono implements Serializable, Comparable, Parcelable {
    private String marca, modelo, precio, stock;

    public Telefono(String marca, String modelo, String precio, String stock) {
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
    }

    public Telefono() {
    }


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public int compareTo(Object o) {
        Telefono tl = (Telefono) o;
        if (this.getMarca().compareToIgnoreCase(tl.getMarca()) == 0) {
            if (this.getModelo().compareToIgnoreCase(tl.getModelo()) != 0) {
                return this.getModelo().compareToIgnoreCase(tl.getModelo());
            }
        } else {
            return this.getMarca().compareToIgnoreCase(tl.getMarca());
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        Telefono telefono = (Telefono) o;

        if (marca.equalsIgnoreCase(telefono.marca) && modelo.equalsIgnoreCase(telefono.modelo)) return true;

        return false;
    }

    @Override
    public int hashCode() {
        int result = marca.hashCode();
        result = 31 * result + modelo.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //el orden es importante
        parcel.writeString(this.marca);
        parcel.writeString(this.modelo);
        parcel.writeString(this.precio);
        parcel.writeString(this.stock);
    }

    /*    public Contacto(Parcel p) {
            //no se puede hacer porque el orden de los constructor con todo no es igual
            // this(p.readString(), p.readInt(),p.readString(),p.readString());
            this.apellido = p.readString();
            this.edad = p.readInt();
            this.nombre = p.readString();
            this.telefono = p.readString();
        }
    */
    public static final Parcelable.Creator<Telefono> CREATOR = new Parcelable.Creator<Telefono>() {

        @Override
        public Telefono createFromParcel(Parcel p) {
            String marca = p.readString();
            String modelo = p.readString();
            String precio = p.readString();
            String stock = p.readString();
            return new Telefono(marca, modelo, precio, stock);
        }

        @Override
        public Telefono[] newArray(int i) {
            return new Telefono[i];
        }
    };
}
