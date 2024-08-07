package network.warzone.tgm.map;

import com.google.gson.JsonObject;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import network.warzone.tgm.gametype.GameType;
import network.warzone.warzoneapi.models.Author;

/**
 * Created by luke on 4/27/17.
 */
@AllArgsConstructor @Getter
public class MapInfo {
    private String name;
    private String version;
    private String objective;
    private List<Author> authors;
    private GameType gametype;
    private List<ParsedTeam> teams;
    private JsonObject jsonObject;
}
