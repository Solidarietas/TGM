package network.warzone.tgm.map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import network.warzone.tgm.TGM;

public class RotationDeserializer implements JsonDeserializer<Rotation> {
    @Override
    public Rotation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        String name = json.get("name").getAsString();

        boolean isDefault = false;
        if (json.has("default")) {
            isDefault = json.get("default").getAsBoolean();
        }

        boolean initialShuffle = false;
        if (json.has("shuffle")) {
            initialShuffle = json.get("shuffle").getAsBoolean();
        }

        RotationRequirement requirements = new RotationRequirement(0, 999999);
        if (json.has("requirements")) {
            requirements = TGM.get().getGson().fromJson(json.get("requirements").getAsJsonObject(), RotationRequirement.class);
        }

        List<MapContainer> maps = new ArrayList<>();
        List<String> mapNames = new ArrayList<>();
        for (JsonElement element : json.getAsJsonArray("maps")) {
            final String mapElementName = element.getAsString();
            mapNames.add(mapElementName);
            for (MapContainer mapContainer : TGM.get().getMatchManager().getMapLibrary().getMaps()) {
                if (mapContainer.getMapInfo().getName().equalsIgnoreCase(mapElementName)) {
                    maps.add(mapContainer);
                }
            }
        }

        return new Rotation(name, isDefault, requirements, maps, mapNames, initialShuffle);
    }
}
