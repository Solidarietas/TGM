package network.warzone.warzoneapi.models;

import java.util.List;
import lombok.AllArgsConstructor;

/**
 * Created by Jorge on 2/23/2018.
 */
@AllArgsConstructor
public class RankPermissionsUpdateRequest {
    private String name;
    private List<String> permissions;

    public static enum Action {
        ADD(),
        REMOVE();
    }
}
