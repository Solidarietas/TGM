package club.pvparcade.api.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DestroyWoolRequest {
    @Getter private UUID uuid; //player uuid
}
