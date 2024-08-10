package club.pvparcade.tgm.modules.filter.evaluate;

import club.pvparcade.tgm.modules.filter.FilterResult;

public interface FilterEvaluator {
    FilterResult evaluate(Object... objects);
}
