package club.pvparcade.api.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Jorge on 11/01/2019
 */
@AllArgsConstructor @Data
public class PlayerTagsUpdateResponse {

    private boolean error;
    private String message;
    private String player;
    private List<String> tags;
    private String activeTag;

}
