//Código creado por Aarón Angulo

package com.itson.ayalapdv;

/**
 * Created by orlan on 19/10/2016.
 */

public class Producto
{
    private long id;
    private String nombre;
    private String distribuidor;
    private int existencia;
    private String codigo;
    private double precio;
    private String origen;

    public Producto()
    {

    }

    public Producto(long id, String nombre, String distribuidor, int existencia, String codigo, double precio, String origen)
    {
        this.id = id;
        this.nombre = nombre;
        this.distribuidor = distribuidor;
        this.existencia = existencia;
        this.codigo = codigo;
        this.precio = precio;
        this.origen = origen;
    }

    public long getId()
    {
        return id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getDistribuidor()
    {
        return distribuidor;
    }

    public int getExistencia()
    {
        return existencia;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public double getPrecio()
    {
        return precio;
    }

    public String getOrigen()
    {
        return origen;
    }

    @Override
    public String toString()
    {
        return "ID:" + id + ", nombre:" + nombre + ", distribuidor:" + "existencia:" + existencia + ", codigo:" + codigo + ", precio:" + precio + ", origen:" + origen;
    }
}
