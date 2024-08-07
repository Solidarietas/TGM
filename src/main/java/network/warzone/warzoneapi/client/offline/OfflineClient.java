package network.warzone.warzoneapi.client.offline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import kong.unirest.*;
import network.warzone.warzoneapi.client.TeamClient;
import network.warzone.warzoneapi.models.Death;
import network.warzone.warzoneapi.models.DestroyWoolRequest;
import network.warzone.warzoneapi.models.GetPlayerByNameResponse;
import network.warzone.warzoneapi.models.Heartbeat;
import network.warzone.warzoneapi.models.IssuePunishmentRequest;
import network.warzone.warzoneapi.models.IssuePunishmentResponse;
import network.warzone.warzoneapi.models.LeaderboardCriterion;
import network.warzone.warzoneapi.models.LeaderboardResponse;
import network.warzone.warzoneapi.models.Map;
import network.warzone.warzoneapi.models.MapLoadResponse;
import network.warzone.warzoneapi.models.MatchFinishPacket;
import network.warzone.warzoneapi.models.MatchInProgress;
import network.warzone.warzoneapi.models.MatchLoadRequest;
import network.warzone.warzoneapi.models.PlayerAltsResponse;
import network.warzone.warzoneapi.models.PlayerInfoRequest;
import network.warzone.warzoneapi.models.PlayerInfoResponse;
import network.warzone.warzoneapi.models.PlayerLogin;
import network.warzone.warzoneapi.models.PlayerTagsUpdateRequest;
import network.warzone.warzoneapi.models.PlayerTagsUpdateResponse;
import network.warzone.warzoneapi.models.PunishmentsListRequest;
import network.warzone.warzoneapi.models.PunishmentsListResponse;
import network.warzone.warzoneapi.models.Rank;
import network.warzone.warzoneapi.models.RankEditRequest;
import network.warzone.warzoneapi.models.RankList;
import network.warzone.warzoneapi.models.RankManageRequest;
import network.warzone.warzoneapi.models.RankManageResponse;
import network.warzone.warzoneapi.models.RankPermissionsUpdateRequest;
import network.warzone.warzoneapi.models.RankUpdateRequest;
import network.warzone.warzoneapi.models.RankUpdateResponse;
import network.warzone.warzoneapi.models.ReportCreateRequest;
import network.warzone.warzoneapi.models.RevertPunishmentResponse;
import network.warzone.warzoneapi.models.UserProfile;
import org.bson.types.ObjectId;

/**
 * Created by luke on 4/27/17.
 */
public class OfflineClient extends TeamClient {

    public OfflineClient() {
        super();
    }

    @Override
    public void heartbeat(Heartbeat heartbeat) {

    }

    @Override
    public GetPlayerByNameResponse player(String name) {
        return null;
    }

    @Override
    public UserProfile login(PlayerLogin playerLogin) {
        List<String> ranks = new ArrayList<String>();
        return new UserProfile(new ObjectId(), playerLogin.getName(), playerLogin.getName().toLowerCase(),
                playerLogin.getUuid(), new Date().getTime(), new Date().getTime(), Collections.singletonList(playerLogin.getIp()), ranks, new ArrayList<Rank>(), 0, 0, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), null, false);
    }

    @Override
    public MapLoadResponse loadmap(Map map) {
        return new MapLoadResponse(false, new ObjectId().toString());
    }

    @Override
    public void addKill(Death death) {

    }

    @Override
    public MatchInProgress loadMatch(MatchLoadRequest matchLoadRequest) {
        return new MatchInProgress(new ObjectId().toString(), matchLoadRequest.getMap());
    }

    @Override
    public void finishMatch(MatchFinishPacket matchFinishPacket) {

    }

    @Override
    public void destroyWool(DestroyWoolRequest destroyWoolRequest) {

    }

    @Override
    public RankList retrieveRanks() {
        return new RankList();
    }

    @Override
    public RankUpdateResponse updateRank(String player, RankUpdateRequest.Action action, RankUpdateRequest rankUpdateRequest) {
        return null;
    };

    @Override
    public RankManageResponse manageRank(RankManageRequest.Action action, RankManageRequest rankManageRequest) {
        return null;
    }

    @Override
    public RankManageResponse editRank(RankEditRequest.EditableField field, RankEditRequest rankEditRequest) {
        return null;
    }

    @Override
    public RankManageResponse editPermissions(RankPermissionsUpdateRequest.Action action, RankPermissionsUpdateRequest permissionsUpdateRequest) {
        return null;
    }

    @Override
    public void createReport(ReportCreateRequest reportCreateRequest) {
    }

    @Override
    public IssuePunishmentResponse issuePunishment(IssuePunishmentRequest issuePunishmentRequest) {
        return null;
    }

    @Override
    public PunishmentsListResponse getPunishments(PunishmentsListRequest punishmentsListRequest) {
        return null;
    }

    @Override
    public LeaderboardResponse getLeaderboard(LeaderboardCriterion leaderboardCriterion) { return new LeaderboardResponse(); }

    @Override
    public RevertPunishmentResponse revertPunishment(String id) {
        return null;
    }

    @Override
    public PlayerInfoResponse getPlayerInfo(PlayerInfoRequest playerInfoRequest) {
        return null;
    }

    @Override
    public PlayerAltsResponse getAlts(String name) {
        return null;
    }

    @Override
    public PlayerTagsUpdateResponse updateTag(String username, String tag, PlayerTagsUpdateRequest.Action action) {
        return new PlayerTagsUpdateResponse(false, "", "", new ArrayList<>(), null);
    }
}
