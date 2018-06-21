//Código creado por Aarón Angulo

package com.itson.ayalapdv;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Created by orlan on 19/10/2016.
 */

public class ConexionSQL extends SQLiteOpenHelper
{
    public ConexionSQL(Context context, String path)
    {
        super(context, path, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}