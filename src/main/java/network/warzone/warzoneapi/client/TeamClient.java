package network.warzone.warzoneapi.client;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.util.UUID;
import kong.unirest.*;
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
import network.warzone.warzoneapi.models.MojangProfile;
import network.warzone.warzoneapi.models.PlayerAltsResponse;
import network.warzone.warzoneapi.models.PlayerInfoRequest;
import network.warzone.warzoneapi.models.PlayerInfoResponse;
import network.warzone.warzoneapi.models.PlayerLogin;
import network.warzone.warzoneapi.models.PlayerTagsUpdateRequest;
import network.warzone.warzoneapi.models.PlayerTagsUpdateResponse;
import network.warzone.warzoneapi.models.PunishmentsListRequest;
import network.warzone.warzoneapi.models.PunishmentsListResponse;
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
public abstract class TeamClient {
    protected final Gson gson;
    protected final UnirestInstance unirest;

    public TeamClient() {
        GsonBuilder builder = new GsonBuilder();

        // ObjectId
        builder.registerTypeAdapter(ObjectId.class, (JsonDeserializer<ObjectId>) (json, typeOfT, context) -> new ObjectId(json.getAsJsonPrimitive().getAsString()));
        builder.registerTypeAdapter(ObjectId.class, (JsonSerializer<ObjectId>) (src, typeOfT, context) -> new JsonPrimitive(src.toString()));

        builder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);

        this.gson = builder.create();

        Config unirestConfig = new Config();
        
        //serialize objects using gson
        unirestConfig.setObjectMapper(new ObjectMapper() {

            public <T> T readValue(String s, Class<T> aClass) {
                try {
                    return gson.fromJson(s, aClass);
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object o) {
                try {
                    return gson.toJson(o);
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        unirestConfig.addDefaultHeader("accept", "application/json");
        unirestConfig.addDefaultHeader("Content-Type", "application/json");
        
        this.unirest = new UnirestInstance(unirestConfig);
    }

    /**
     * Called every second. Keeps the
     * server up-to-date in the database.
     */
    public abstract void heartbeat(Heartbeat heartbeat);

    public abstract GetPlayerByNameResponse player(String name);

    /**
     * Called when a player logs into the server.
     */
    public abstract UserProfile login(PlayerLogin playerLogin);

    /**
     * Called whenever a map is loaded.
     * Returns the map id.
     */
    public abstract MapLoadResponse loadmap(Map map);

    public abstract void addKill(Death death);

    public abstract MatchInProgress loadMatch(MatchLoadRequest matchLoadRequest);

    public abstract void finishMatch(MatchFinishPacket matchFinishPacket);

    public abstract void destroyWool(DestroyWoolRequest destroyWoolRequest);
    
    public abstract RankList retrieveRanks();

    public abstract RankUpdateResponse updateRank(String player, RankUpdateRequest.Action action, RankUpdateRequest rankUpdateRequest);

    public abstract RankManageResponse manageRank(RankManageRequest.Action action, RankManageRequest rankManageRequest);

    public abstract RankManageResponse editRank(RankEditRequest.EditableField field, RankEditRequest rankEditRequest);

    public abstract RankManageResponse editPermissions(RankPermissionsUpdateRequest.Action action, RankPermissionsUpdateRequest permissionsUpdateRequest);

    public abstract void createReport(ReportCreateRequest reportCreateRequest);

    public abstract IssuePunishmentResponse issuePunishment(IssuePunishmentRequest issuePunishmentRequest);

    public abstract PunishmentsListResponse getPunishments(PunishmentsListRequest punishmentsListRequest);

    public abstract RevertPunishmentResponse revertPunishment(String id);

    public abstract PlayerInfoResponse getPlayerInfo(PlayerInfoRequest playerInfoRequest);

    public abstract PlayerAltsResponse getAlts(String name);

    public abstract PlayerTagsUpdateResponse updateTag(String username, String tag, PlayerTagsUpdateRequest.Action action);

    public abstract LeaderboardResponse getLeaderboard(LeaderboardCriterion leaderboardCriterion);

    public MojangProfile getMojangProfile(String username) {
        try {
            HttpResponse<MojangProfile> response = unirest.get("https://api.ashcon.app/mojang/v2/user/" + username)
                    .asObject(MojangProfile.class);
            if (response.getStatus() != 200) {
                return null;
            }
            return response.getBody();
        } catch (UnirestException e) {
            return null;
        }
    }

    public MojangProfile getMojangProfile(UUID uuid) {
        return getMojangProfile(uuid.toString());
    }

}
