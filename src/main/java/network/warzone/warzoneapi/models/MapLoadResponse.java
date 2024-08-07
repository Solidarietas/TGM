package network.warzone.warzoneapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MapLoadResponse {
    @Getter private boolean inserted;
    @Getter private String map; //id
}
