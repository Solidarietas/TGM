package club.pvparcade.tgm.modules.filter.type;

import club.pvparcade.tgm.match.Match;

public interface FilterType {
    default void load(Match match) {}
    default void unload() {}
}
