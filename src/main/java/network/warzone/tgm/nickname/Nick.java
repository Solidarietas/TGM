package network.warzone.tgm.nickname;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import network.warzone.warzoneapi.models.Skin;

@Getter @Setter @AllArgsConstructor
public class Nick {

    private final UUID uuid;

    private final String originalName;
    private String name;
    private Skin skin;
    private NickManager.NickDetails details;
    private NickedUserProfile profile;

    private boolean applied;
    private boolean active;

}
