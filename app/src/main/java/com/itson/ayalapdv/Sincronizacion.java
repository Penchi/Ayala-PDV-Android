//Código creado por Aarón Angulo

package com.itson.ayalapdv;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.app.AlertDialog;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.internal.DriveServiceResponse;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by orlan on 24/10/2016.
 */
public class Sincronizacion {
    private static Sincronizacion ourInstance = new Sincronizacion();

    public static Sincronizacion getInstance() {
        return ourInstance;
    }

    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private Query query;
    private String path;

    private Sincronizacion() {

    }

    public void InicializarAPI(GoogleApiClient mGoogleApiClient, Context context, String path)
    {
        this.mGoogleApiClient = mGoogleApiClient;
        this.context = context;
        this.path = path;

        System.out.println("Inicializado");
        VerificarCarpeta();
    }

    public void VerificarCarpeta()
    {
        System.out.println("Verificando carpeta");
        PrincipalActivity.adb.setNegativeButton("", null);
        PrincipalActivity.adb.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "AyalaPDV"))
                .build();

        Drive.DriveApi.query(mGoogleApiClient, query)
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>()
                {
                    @Override
                    public void onResult(DriveApi.MetadataBufferResult result)
                    {
                        if (result.getMetadataBuffer().getCount() > 0)
                        {
                            PrincipalActivity.pdb.setMessage("Buscando carpeta...");
                            VertificarBDD(result.getMetadataBuffer().get(0).getDriveId());
                            result.release();
                        }
                        else
                        {
                            PrincipalActivity.pdb.dismiss();
                            PrincipalActivity.adb.setTitle("Error");
                            PrincipalActivity.adb.setMessage("No se encontró la carpeta de la base de datos.");
                            PrincipalActivity.adb.setIcon(android.R.drawable.stat_notify_error);
                            PrincipalActivity.adb.show();
                        }
                    }
                });

    }

    public void VertificarBDD(DriveId id)
    {
        System.out.println("Verificando archivo");
        DriveFolder folder = Drive.DriveApi.getFolder(mGoogleApiClient, id);
        query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "AyalaBDDMovil.db"))
                .build();
        folder.queryChildren(mGoogleApiClient, query)
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>()
                {
                    @Override
                    public void onResult(DriveApi.MetadataBufferResult result)
                    {
                        System.out.println("Elementos: " + result.getMetadataBuffer().getCount());
                        if (result.getMetadataBuffer().getCount() > 0)
                        {
                            File file = new File(path + "AyalaBDDMovil.db");
                            PrincipalActivity.pdb.setMessage("Buscando base de datos...");

                            if(file.exists())
                            {
                                SharedPreferences sp = context.getSharedPreferences("AyalaPDV", MODE_PRIVATE);
                                long fechaArchivoNuevo = result.getMetadataBuffer().get(0).getModifiedDate().getTime();
                                long fechaArchivoViejo = sp.getLong("fechaArchivo", 0);

                                System.out.println("Nueva:" + fechaArchivoNuevo);
                                System.out.println("Vieja:" + fechaArchivoViejo);

                                if(fechaArchivoNuevo > fechaArchivoViejo)
                                {
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putLong("fechaArchivo", fechaArchivoNuevo);
                                    editor.commit();
                                    file.delete();

                                    DescargarArchivo(result.getMetadataBuffer().get(0).getWebContentLink());
                                }
                                else
                                {
                                    PrincipalActivity.pdb.dismiss();
                                    PrincipalActivity.adb.setTitle("Aviso");
                                    PrincipalActivity.adb.setMessage("Ya se cuenta con la base de datos mas reciente.");
                                    PrincipalActivity.adb.setIcon(android.R.drawable.ic_dialog_info);
                                    PrincipalActivity.adb.show();
                                }
                            }
                            else
                            {
                                DescargarArchivo(result.getMetadataBuffer().get(0).getWebContentLink());
                            }
                            result.release();
                        }
                        else
                        {
                            PrincipalActivity.pdb.dismiss();
                            PrincipalActivity.adb.setTitle("Error");
                            PrincipalActivity.adb.setMessage("No se encontró la base de datos en Google Drive.");
                            PrincipalActivity.adb.setIcon(android.R.drawable.stat_notify_error);
                            PrincipalActivity.adb.show();
                        }
                    }
                });
    }

    public void DescargarArchivo(String url)
    {
        System.out.println("Descargando");
        DownloadManager mgr = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

        //url = "https://drive.google.com/open?id=0B_VizqnC_WN8V19LblhTNTYxQ1k";
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Ayala PDV")
                .setDescription("Descargando...")
                .setMimeType("application/db")

                .setDestinationInExternalPublicDir("", "AyalaBDDMovil.db");

        mgr.enqueue(request);

        PrincipalActivity.pdb.dismiss();
    }

    BroadcastReceiver onComplete=new BroadcastReceiver()
    {
        public void onReceive(Context ctxt, Intent intent)
        {

        }
    };
}