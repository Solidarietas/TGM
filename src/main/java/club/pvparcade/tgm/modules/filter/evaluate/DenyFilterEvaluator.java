package club.pvparcade.tgm.modules.filter.evaluate;

import club.pvparcade.tgm.modules.filter.FilterResult;

public class DenyFilterEvaluator implements FilterEvaluator {

    @Override
    public FilterResult evaluate(Object... objects) {
        return FilterResult.DENY;
    }
}
