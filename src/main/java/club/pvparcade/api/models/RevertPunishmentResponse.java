package club.pvparcade.api.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;

/**
 * Created by Jorge on 2/4/2018.
 */
@AllArgsConstructor @Getter
public class RevertPunishmentResponse {

    private Punishment punishment;
    private List<LoadedUser> loadedUsers;
    private boolean notFound;

    private boolean success;

    @AllArgsConstructor @Getter
    public static class LoadedUser {

        private String name;
        private ObjectId id;

    }

}
