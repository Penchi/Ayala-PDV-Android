//Código creado por Aarón Angulo

package com.itson.ayalapdv;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by orlan on 19/10/2016.
 */

public class SQLQueryManager
{
    private ConexionSQL conexion;
    private SQLiteDatabase bdd;

    public SQLQueryManager(Context context, String path)
    {
        conexion = new ConexionSQL(context, path + "AyalaBDDMovil.db");
        bdd = conexion.getReadableDatabase();
        System.out.println("Path de BDD: " + bdd.getPath());
    }

    public Cursor SelectQuery(String campo, String valor, String ordenar, boolean escasez)
    {
        try
        {
            //String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String limit
            if(ordenar.matches(""))
                ordenar = null;
            if(campo.matches(""))
                campo = null;
            if(valor.matches(""))
                valor = null;
            else
                if(valor.contains("'"))
                    valor = valor.replaceAll("'", "''");

            String query = "SELECT nombre, distribuidor, existencia, precio, origen, existenciaminima FROM Productos WHERE activado = 1 ";

            if(escasez)
                query += "AND existencia < existenciaminima ";
            if(campo != null)
                query += "AND UPPER(" + campo + ") = UPPER('" + valor + "') " ;
            if(ordenar != null)
                query += "order by " + ordenar;

            System.out.println(query);
            return bdd.rawQuery(query, null);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
