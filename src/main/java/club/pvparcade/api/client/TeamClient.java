package club.pvparcade.api.client;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.util.UUID;
import kong.unirest.*;
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
import club.pvparcade.api.models.MojangProfile;
import club.pvparcade.api.models.PlayerAltsResponse;
import club.pvparcade.api.models.PlayerInfoRequest;
import club.pvparcade.api.models.PlayerInfoResponse;
import club.pvparcade.api.models.PlayerLogin;
import club.pvparcade.api.models.PlayerTagsUpdateRequest;
import club.pvparcade.api.models.PlayerTagsUpdateResponse;
import club.pvparcade.api.models.PunishmentsListRequest;
import club.pvparcade.api.models.PunishmentsListResponse;
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
