package club.pvparcade.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;

@AllArgsConstructor @Getter
public class Heartbeat {

    @SerializedName("_id")
    private ObjectId serverId;

    private final String name;
    private final String id;
    private final String motd;
    private Set<String> players; //object ids
    private Set<String> playerNames;
    private int playerCount;
    private int spectatorCount;
    private int maxPlayers;
    private String map;
    private String gametype;

}
