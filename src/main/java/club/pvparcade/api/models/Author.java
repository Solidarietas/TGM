package club.pvparcade.api.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jorge on 09/08/2019
 */
@Getter @AllArgsConstructor
public class Author {

    private final UUID uuid;
    private final String username; // Fallback
    @Setter private String displayUsername;

    public Author(UUID uuid) {
        this(uuid, null, null);
    }

    public Author(String username) {
        this(null, username, null);
    }

}
