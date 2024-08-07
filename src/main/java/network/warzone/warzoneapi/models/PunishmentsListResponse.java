package network.warzone.warzoneapi.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;

/**
 * Created by Jorge on 2/4/2018.
 */
@AllArgsConstructor @Getter
public class PunishmentsListResponse {

    private List<Punishment> punishments;
    private List<LoadedUser> loadedUsers;

    private boolean notFound;

    @AllArgsConstructor @Getter
    public static class LoadedUser {

        private String name;
        private ObjectId id;

    }

}
