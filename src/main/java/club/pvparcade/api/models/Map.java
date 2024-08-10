package club.pvparcade.api.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Map {
    @Getter private String name;
    @Getter private String version;
    @Getter private List<Author> authors;
    @Getter private String gametype;
    @Getter private List<Team> teams;
}
