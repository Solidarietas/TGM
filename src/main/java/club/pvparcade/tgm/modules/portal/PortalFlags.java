package club.pvparcade.tgm.modules.portal;

import java.util.EnumMap;
import java.util.Map;

public class PortalFlags {
  public enum Flag {
    RELATIVE_X_POSITION,
    RELATIVE_Y_POSITION,
    RELATIVE_Z_POSITION,
    RELATIVE_X_VELOCITY,
    RELATIVE_Y_VELOCITY,
    RELATIVE_Z_VELOCITY,
    RELATIVE_YAW,
    RELATIVE_PITCH,
    RETAIN_PASSENGERS,
    RETAIN_VEHICLE,
    RETAIN_OPEN_INVENTORY
  }

  private final Map<Flag, Boolean> flags;

  public PortalFlags() {
    flags = new EnumMap<>(Flag.class);
    // Set default values
    flags.put(Flag.RELATIVE_X_POSITION, false);
    flags.put(Flag.RELATIVE_Y_POSITION, false);
    flags.put(Flag.RELATIVE_Z_POSITION, false);
    flags.put(Flag.RELATIVE_X_VELOCITY, true);
    flags.put(Flag.RELATIVE_Y_VELOCITY, true);
    flags.put(Flag.RELATIVE_Z_VELOCITY, true);
    flags.put(Flag.RELATIVE_YAW, true);
    flags.put(Flag.RELATIVE_PITCH, true);
    flags.put(Flag.RETAIN_PASSENGERS, true);
    flags.put(Flag.RETAIN_VEHICLE, true);
    flags.put(Flag.RETAIN_OPEN_INVENTORY, true);
  }

  public void setFlag(Flag flag, boolean value) {
    flags.put(flag, value);
  }

  public boolean getFlag(Flag flag) {
    return flags.get(flag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("PortalFlags{\n");
    for (Map.Entry<Flag, Boolean> entry : flags.entrySet()) {
      sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }
    sb.append("}");
    return sb.toString();
  }
}