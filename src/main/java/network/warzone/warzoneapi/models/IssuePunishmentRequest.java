package network.warzone.warzoneapi.models;

import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IssuePunishmentRequest {

    private String name;
    private String ip;
    private boolean ip_ban;
    private UUID punisherUuid;

    private String type;
    private long length;

    private String reason;

}
