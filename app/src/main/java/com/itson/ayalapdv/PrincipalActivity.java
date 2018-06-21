//Código creado por Aarón Angulo

package com.itson.ayalapdv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.io.File;


public class PrincipalActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    public static ProgressDialog pdb;
    public static AlertDialog.Builder adb;

    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private String path;
    private String ordenarPor;
    private String whereCampo;
    private EditText etValor;
    private Spinner spinCampo;
    private Button btnReporte;
    private Switch schEscasez;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();

        etValor = (EditText) findViewById(R.id.TxtValor);
        spinCampo = (Spinner) findViewById(R.id.SpinCampo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.campos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinCampo.setAdapter(adapter);
        spinCampo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0: whereCampo = ""; break;
                    case 1: whereCampo = "nombre"; break;
                    case 2: whereCampo = "distribuidor"; break;
                    case 3: whereCampo = "existencia"; break;
                    case 4: whereCampo = "precio"; break;
                    case 5: whereCampo = "origen"; break;
                    case 6: whereCampo = "existenciaminima"; break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnReporte = (Button) findViewById(R.id.BtnReporte);
        btnReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                GenerarReporte();
            }
        });

        schEscasez = (Switch) findViewById(R.id.SchEscasez);

        ordenarPor = "";

        path = Environment.getExternalStorageDirectory() + "/";
        System.out.println("Path general: " + path);

        pdb = new ProgressDialog(this);
        pdb.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        adb = new AlertDialog.Builder(this);
    }

    public void GenerarReporte()
    {
        if(new File(path + "AyalaBDDMovil.db").exists())
        {
            SQLQueryManager manager = new SQLQueryManager(getApplicationContext(), path);
            Cursor cursor = null;

            cursor = manager.SelectQuery(whereCampo, etValor.getText().toString(), ordenarPor, schEscasez.isChecked());

            if(cursor != null)
            {
                pdb.setTitle("Cargando");
                pdb.setMessage("Se está generando la tabla de datos...");
                pdb.show();
                TableLayout tlayout = (TableLayout) findViewById(R.id.TblLayout);
                tlayout.removeAllViews();

                tlayout.setGravity(Gravity.CENTER);

                TableRow fila = new TableRow(context);

                Button BtnNombre = new Button(context);
                Button BtnDistribuidor = new Button(context);
                Button BtnExistencia = new Button(context);
                Button BtnPrecio = new Button(context);
                Button BtnOrigen = new Button(context);
                Button BtnExistMin = new Button(context);

                BtnNombre.setTextColor(Color.BLACK);
                BtnDistribuidor.setTextColor(Color.BLACK);
                BtnExistencia.setTextColor(Color.BLACK);
                BtnPrecio.setTextColor(Color.BLACK);
                BtnOrigen.setTextColor(Color.BLACK);
                BtnExistMin.setTextColor(Color.BLACK);

                BtnNombre.setBackgroundResource(R.drawable.exist_normal);
                BtnDistribuidor.setBackgroundResource(R.drawable.exist_normal);
                BtnExistencia.setBackgroundResource(R.drawable.exist_normal);
                BtnPrecio.setBackgroundResource(R.drawable.exist_normal);
                BtnOrigen.setBackgroundResource(R.drawable.exist_normal);
                BtnExistMin.setBackgroundResource(R.drawable.exist_normal);

                BtnNombre.setText(" Nombre ");
                BtnDistribuidor.setText(" Distribuidor ");
                BtnExistencia.setText(" Existencia ");
                BtnPrecio.setText(" Precio ");
                BtnOrigen.setText(" Origen ");
                BtnExistMin.setText(" ExistMin ");

                BtnNombre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                BtnDistribuidor.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                BtnExistencia.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                BtnPrecio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                BtnOrigen.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                BtnExistMin.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                BtnNombre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ordenarPor = "nombre";
                        GenerarReporte();
                    }
                });

                BtnDistribuidor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ordenarPor = "distribuidor";
                        GenerarReporte();
                    }
                });

                BtnExistencia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ordenarPor = "existencia";
                        GenerarReporte();
                    }
                });

                BtnPrecio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ordenarPor = "precio";
                        GenerarReporte();
                    }
                });

                BtnOrigen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ordenarPor = "origen";
                        GenerarReporte();
                    }
                });

                BtnExistMin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ordenarPor = "existenciaminima";
                        GenerarReporte();
                    }
                });

                fila.addView(BtnNombre);
                fila.addView(BtnDistribuidor);
                fila.addView(BtnExistencia);
                fila.addView(BtnPrecio);
                fila.addView(BtnOrigen);
                fila.addView(BtnExistMin);

                tlayout.addView(fila);

                cursor.moveToFirst();
                int filas = cursor.getCount();

                TextView TxtNombre;
                TextView TxtDistribuidor;
                TextView TxtExistencia;
                TextView TxtPrecio;
                TextView TxtOrigen;
                TextView TxtExistMin;

                for(int i = 0; i < filas; i++)
                {
                    fila = new TableRow(this.getApplicationContext());

                    TxtNombre = new TextView(context);
                    TxtDistribuidor = new TextView(context);
                    TxtExistencia = new TextView(context);
                    TxtPrecio = new TextView(context);
                    TxtOrigen = new TextView(context);
                    TxtExistMin = new TextView(context);

                    int existencia = cursor.getInt(2);
                    int existenciaminima = cursor.getInt(5);

                    TxtNombre.setTextColor(Color.BLACK);
                    TxtDistribuidor.setTextColor(Color.BLACK);
                    TxtExistencia.setTextColor(Color.BLACK);
                    TxtPrecio.setTextColor(Color.BLACK);
                    TxtOrigen.setTextColor(Color.BLACK);
                    TxtExistMin.setTextColor(Color.BLACK);

                    int recurso;
                    if(existencia < existenciaminima)
                    {
                        if(existencia < (existenciaminima / 2))
                            recurso = R.drawable.exist_critica;
                        else
                            recurso = R.drawable.exist_advertencia;
                    }
                    else
                        recurso = R.drawable.exist_normal;

                    TxtNombre.setBackgroundResource(recurso);
                    TxtDistribuidor.setBackgroundResource(recurso);
                    TxtExistencia.setBackgroundResource(recurso);
                    TxtPrecio.setBackgroundResource(recurso);
                    TxtOrigen.setBackgroundResource(recurso);
                    TxtExistMin.setBackgroundResource(recurso);

                    TxtNombre.setText(" " + cursor.getString(0) + " ");
                    TxtDistribuidor.setText(" " + cursor.getString(1) + " ");
                    TxtExistencia.setText(" " + existencia + " ");
                    TxtPrecio.setText(" " + cursor.getDouble(3) + " ");
                    TxtOrigen.setText(" " + cursor.getString(4) + " ");
                    TxtExistMin.setText(" " + existenciaminima + " ");

                    fila.addView(TxtNombre);
                    fila.addView(TxtDistribuidor);
                    fila.addView(TxtExistencia);
                    fila.addView(TxtPrecio);
                    fila.addView(TxtOrigen);
                    fila.addView(TxtExistMin);

                    tlayout.addView(fila);
                    cursor.moveToNext();
                }
                pdb.dismiss();
            }
        }
        else
        {
            adb.setTitle("No se encontró la base de datos");
            adb.setMessage("¿Deseas descargar ahora la base de datos de Google Drive?");
            adb.setIcon(android.R.drawable.ic_dialog_info);
            adb.setPositiveButton("Sí", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    DescargarBDD();
                }
            });

            adb.setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });
            adb.show();
        }
    }

    public void DescargarBDD()
    {
        pdb.setTitle("Espere");
        pdb.setMessage("Preparandose para descargar la base de datos");
        pdb.show();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    /*
    @Override
    public void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        return true;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            DescargarBDD();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Sincronizacion.getInstance().InicializarAPI(mGoogleApiClient, getApplicationContext(), path);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        mGoogleApiClient = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        if (connectionResult.hasResolution())
            try
            {
                connectionResult.startResolutionForResult(this, 1000);
            }
            catch (IntentSender.SendIntentException e)
            {
                // Unable to resolve, message user appropriately
            }
        else
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode)
        {
            case 1000:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }
}
