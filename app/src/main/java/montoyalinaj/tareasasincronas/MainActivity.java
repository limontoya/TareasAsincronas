package montoyalinaj.tareasasincronas;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainActivity extends Activity {

    TextView reloj;
    TextView salida;
    Button boton_descarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference to id created on xml: R id
        reloj = (TextView) findViewById(R.id.reloj);
        salida = (TextView) findViewById(R.id.salida);
        boton_descarga = (Button) findViewById(R.id.boton_descarga);
    }

    public void mostrarMensaje(View view) {
        Toast.makeText(this, "OKK", Toast.LENGTH_SHORT).show();

        TareaAsincrona tarea = new TareaAsincrona();
        tarea.execute();
    }

    /**
     * Start download
     * @param view
     */
    public void iniciarDescarga(View view) {
        DescargarArchivo descargarArchivo = new DescargarArchivo();
        descargarArchivo.execute("https://docs.oracle.com/javase/specs/jls/se7/jls7.pdf");
    }

    /**
     * Create worker thread
     */
    public class TareaAsincrona extends AsyncTask<Void, String, Void>{

        boolean isAlive = true;
        int contador = 0;

        /**
         * Sets a message before excecuting the task
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reloj.setText("Ha iniciado el proceso");
        }

        /**
         * If something is happening on background it might be useful
         * to use a PREexcecute and POSTexcecute methods
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {

            while (isAlive){

                Calendar calendar = new GregorianCalendar();//= new Calendar.getInstance();
                String year   = ""+calendar.get(Calendar.YEAR);
                String month  = ""+calendar.get(Calendar.MONTH);
                String day    = ""+calendar.get(Calendar.DAY_OF_MONTH);
                String hour   = ""+calendar.get(Calendar.HOUR_OF_DAY);
                String minute = ""+calendar.get(Calendar.MINUTE);
                String second = ""+calendar.get(Calendar.SECOND);

                publishProgress(year + " - " + month +" - "+ day +"\n "+ hour +" : "+ minute +" : "+ second);
                delay(1000);
            }

            return null;
        }

        /**
         * Runs on GUI thread
         * @param values
         */
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            String mensaje = values[0];

            reloj.setText(mensaje);

        }

        /**
         * Delay on the time sent
         * @param time
         */
        public void delay(long time){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * After excecuting the activity set this message
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reloj.setText("Ha finalizado el proceso");
        }
    }

    /**
     * Download a file
     */
    public class DescargarArchivo extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            salida.setText("Ha iniciado la descarga");
            boton_descarga.setText("Descarga en progreso");
            boton_descarga.setEnabled(false);
        }

        /**
         * Receive the file to download
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            String str_url = params[0];

            try {
                URL url = new URL(str_url);
                URLConnection connection = url.openConnection();
                connection.connect();
                //Receive all bytes
                InputStream inputStream = connection.getInputStream();

                File carpeta_descarga = new File(Environment.getExternalStorageDirectory()+"/tareasAsincronas");

                //Create directories if folder does not exist
                if(!carpeta_descarga.exists()){
                    carpeta_descarga.mkdirs();
                }

                File archivo_descargado = new File(carpeta_descarga+"/archivo.pdf");

                FileOutputStream fileOutputStream = new FileOutputStream(archivo_descargado);

                //Set bytes to read
                int bytesLeidos = 0;
                byte [] buffer = new byte[1024];

                //Read bytes
                while ( (bytesLeidos=inputStream.read(buffer)) != -1){
                    //write from buffer, starting on zero, to the bytes read
                    fileOutputStream.write(buffer, 0, bytesLeidos);
                }
                //Close streams
                inputStream.close();
                fileOutputStream.close();

                //Return
                return "OK";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "BAD";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("OK")){
                salida.setText("Ha terminado la descarga");
                boton_descarga.setText("Iniciar Descarga");
                boton_descarga.setEnabled(true);

                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
            }
            else{
                salida.setText("Ha fallado la descarga");
                boton_descarga.setText("Iniciar Descarga");
                boton_descarga.setEnabled(true);
            }
        }
    }

}
