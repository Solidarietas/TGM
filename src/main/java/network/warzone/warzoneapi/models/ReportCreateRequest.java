package network.warzone.warzoneapi.models;

import java.util.ArrayList;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReportCreateRequest {

    private int amount;

    private String reporterName;
    private String reportedName;

    private UUID reporterUuid;
    private UUID reportedUuid;

    private String reason;
    private long timestamp;

    private ArrayList<String> onlineStaff;

}
