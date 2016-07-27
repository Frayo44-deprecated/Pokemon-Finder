package pokemonfinder.com.pokemonfinder.Server;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import pokemonfinder.com.pokemonfinder.Constants;

public class PokemonServer {

    public static JSONObject getNearPokemonsByCordinates(String lat, String lng) {
        JSONObject jsonObject;
        try {
            jsonObject = new Request("/map/data", lat, lng).execute().get();
            return jsonObject;
        } catch (Exception e) {
            Log.d(Constants.TAG, "Error in getNearPokemonsByCordinates function" + e.getMessage());
            return null;
        }
    }

    private static class Request extends AsyncTask<String, String, JSONObject> {

        String request;
        String lat, lng;

        public Request(String request, String lat, String lng) {
            this.request = request;
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            String myUrl = Constants.REQUEST_PREFIX + "" + request + "/" + lat + "/" + lng;
            try {
                URL url = new URL(myUrl);
                URLConnection urlConnection = url.openConnection();
                InputStream in = urlConnection.getInputStream();

                String result = IOUtils.toString(in, StandardCharsets.UTF_8);

                try {
                    JSONObject obj = new JSONObject(result);
                    Log.d(Constants.TAG, obj.toString());
                    return obj;
                } catch (Throwable t) {
                    Log.e(Constants.TAG, "Could not parse malformed JSON: \"" + json + "\"");
                }
            } catch (ClientProtocolException e) {
                Log.d(Constants.TAG, e.toString() + myUrl);
            } catch (IOException e) {
                Log.d(Constants.TAG, e.toString() + myUrl);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
        }
    }
}