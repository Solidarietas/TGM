package club.pvparcade.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Jorge on 10/03/2019
 */
@AllArgsConstructor @Getter
public class MojangProfile {

    private String username;
    private UUID uuid;
    private List<Username> username_history;
    private Textures textures;

    @AllArgsConstructor @Getter
    public static class Username {
        private String username;
        private String changed_at;
    }

    @AllArgsConstructor @Getter
    public static class Textures {
        @SerializedName("raw")
        private Skin skin;
    }

}
