package club.pvparcade.tgm.modules.time;

public interface TimeSubscriber {
    /**
     * Called every second
     * @param elapsed Time elapsed of match in seconds
     */
    void processSecond(int elapsed);
}
