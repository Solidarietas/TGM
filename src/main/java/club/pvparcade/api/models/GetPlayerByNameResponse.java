package club.pvparcade.api.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class GetPlayerByNameResponse {

  private UserProfile user;
  private List<Death> deaths;

}
