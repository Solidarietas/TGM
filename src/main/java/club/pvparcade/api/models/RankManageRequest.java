package club.pvparcade.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.AllArgsConstructor;

/**
 * Created by Jorge on 2/22/2018.
 */
@AllArgsConstructor
public class RankManageRequest {

    private String name;
    private int priority;
    private String prefix;
    private List<String> permissions;
    private boolean staff;
    @SerializedName("default")
    private boolean def;

    public RankManageRequest(String name) {
        this.name = name;
    }

    public enum Action {
        CREATE(),
        DELETE()
    }
}
