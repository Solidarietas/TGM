package network.warzone.warzoneapi.models;

import java.util.List;
import lombok.Getter;

/**
 * Created by Jorge on 4/2/2018.
 */
@Getter
public class PlayerInfoResponse {

    private boolean error;
    private String message;

    private String queryFilter;
    private List<UserProfile> users;

}
