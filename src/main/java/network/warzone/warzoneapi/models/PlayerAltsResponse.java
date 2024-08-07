package network.warzone.warzoneapi.models;

import java.util.List;
import lombok.Getter;

@Getter
public class PlayerAltsResponse {

  private boolean error;
  private String message;

  private UserProfile lookupUser;
  private List<UserProfile> users;

}
