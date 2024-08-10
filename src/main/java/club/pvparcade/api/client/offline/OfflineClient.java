package club.pvparcade.api.client.offline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import kong.unirest.*;
import club.pvparcade.api.client.TeamClient;
import club.pvparcade.api.models.Death;
import club.pvparcade.api.models.DestroyWoolRequest;
import club.pvparcade.api.models.GetPlayerByNameResponse;
import club.pvparcade.api.models.Heartbeat;
import club.pvparcade.api.models.IssuePunishmentRequest;
import club.pvparcade.api.models.IssuePunishmentResponse;
import club.pvparcade.api.models.LeaderboardCriterion;
import club.pvparcade.api.models.LeaderboardResponse;
import club.pvparcade.api.models.Map;
import club.pvparcade.api.models.MapLoadResponse;
import club.pvparcade.api.models.MatchFinishPacket;
import club.pvparcade.api.models.MatchInProgress;
import club.pvparcade.api.models.MatchLoadRequest;
import club.pvparcade.api.models.PlayerAltsResponse;
import club.pvparcade.api.models.PlayerInfoRequest;
import club.pvparcade.api.models.PlayerInfoResponse;
import club.pvparcade.api.models.PlayerLogin;
import club.pvparcade.api.models.PlayerTagsUpdateRequest;
import club.pvparcade.api.models.PlayerTagsUpdateResponse;
import club.pvparcade.api.models.PunishmentsListRequest;
import club.pvparcade.api.models.PunishmentsListResponse;
import club.pvparcade.api.models.Rank;
import club.pvparcade.api.models.RankEditRequest;
import club.pvparcade.api.models.RankList;
import club.pvparcade.api.models.RankManageRequest;
import club.pvparcade.api.models.RankManageResponse;
import club.pvparcade.api.models.RankPermissionsUpdateRequest;
import club.pvparcade.api.models.RankUpdateRequest;
import club.pvparcade.api.models.RankUpdateResponse;
import club.pvparcade.api.models.ReportCreateRequest;
import club.pvparcade.api.models.RevertPunishmentResponse;
import club.pvparcade.api.models.UserProfile;
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
