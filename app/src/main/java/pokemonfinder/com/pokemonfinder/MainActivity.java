package pokemonfinder.com.pokemonfinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import pokemonfinder.com.pokemonfinder.Server.GPSTracker;
import pokemonfinder.com.pokemonfinder.Server.PokeJsonAPI;
import pokemonfinder.com.pokemonfinder.Server.PokemonServer;

public class MainActivity extends AppCompatActivity {

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final int INITIAL_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!canAccessLocation()) {
            ActivityCompat.requestPermissions(this, INITIAL_PERMS, INITIAL_REQUEST);
        } else {
            refresh();
        }
    }

    public void refresh() {
        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
        if (gpsTracker.isGPSActive()) {
            gpsTracker.getLocation(getApplicationContext(), null);
            Location loc = gpsTracker.getLocation();
            if (loc != null) {
                double longitude = loc.getLongitude();
                double latitude = loc.getLatitude();
                Toast.makeText(getApplicationContext(), "Got Location: " + longitude + " " + latitude,
                        Toast.LENGTH_LONG).show();

                JSONObject responseJson = PokemonServer.getNearPokemonsByCordinates(latitude + "", longitude + "");
                if (responseJson != null) {
                    ArrayList<Pokemon> pokemons = PokeJsonAPI.getPokemonListByJsonObj(responseJson);
                    if (pokemons == null)
                        Log.d(Constants.TAG, "Errofrr in server resopnse");

                    else if (pokemons.size() == 0)
                        Log.d(Constants.TAG, "There's no pokemons around you :(");
                    else {
                        for (int i = 0; i < pokemons.size(); i++) {
                            Pokemon poke = pokemons.get(i);
                            Toast.makeText(MainActivity.this, "Pokemon #" + i + "\n" +
                                    "Name: " + poke.getName() + "Latitude: " + poke.getLat() + "Langtiude: " +
                                    poke.getLng(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Log.d(Constants.TAG, "Can't get location for unknown reason");
            }
        } else {
            Log.d(Constants.TAG, "GPS is not active!");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1337:
                if (canAccessLocation()) {
                    refresh();
                }
                break;
        }
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));
    }
}
