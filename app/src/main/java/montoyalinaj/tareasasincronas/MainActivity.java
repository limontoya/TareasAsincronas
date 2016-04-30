package montoyalinaj.tareasasincronas;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    TextView reloj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference to id created on xml: R id reloj
        reloj = (TextView) findViewById(R.id.reloj);
    }

    public void mostrarMensaje(View view) {
        Toast.makeText(this, "OKK", Toast.LENGTH_SHORT).show();

        TareaAsincrona tarea = new TareaAsincrona();
        tarea.execute();
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
                publishProgress(""+contador);
                delay(1000);
                contador++;
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
}
