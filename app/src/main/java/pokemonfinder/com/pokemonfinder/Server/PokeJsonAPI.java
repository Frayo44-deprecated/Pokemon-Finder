package pokemonfinder.com.pokemonfinder.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pokemonfinder.com.pokemonfinder.Constants;
import pokemonfinder.com.pokemonfinder.Pokemon;

public class PokeJsonAPI {

    public static boolean vertifyJson(JSONObject jsonObject) {
        try {
            String status = jsonObject.getString("status");
            if (status.equals("success"))
                return true;
        } catch (JSONException e) {
            return false;
        }

        return false;
    }

    public static String getPokemonNameByID(int id) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(Constants.nameKeyJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String key = "" + id;
        try {
            return jsonObject != null ? jsonObject.getString(key) : null;
        } catch (JSONException e) {
            return "Undefined Pokemon"; // TODO: Handle this error
        } catch (NullPointerException e) {
            return "Undefined Pokemon"; // TODO: Handle this error
        }
    }

    public static ArrayList<Pokemon> getPokemonListByJsonObj(JSONObject jsonObj) {
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        if (!vertifyJson(jsonObj))
            return null;
        try {
            JSONArray arr = jsonObj.getJSONArray("pokemon");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject pokemon = arr.getJSONObject(i);
                String name = getPokemonNameByID(pokemon.getInt("pokemonId"));
                String lat = pokemon.getString("latitude");
                String lon = pokemon.getString("longitude");
                int resourceID = Constants.pokeImageArr[pokemon.getInt("pokemonId")];
                pokemons.add(new Pokemon(name, lat, lon, resourceID));
            }

        } catch (JSONException e) {
            return null;
        }
        return pokemons;
    }
}
