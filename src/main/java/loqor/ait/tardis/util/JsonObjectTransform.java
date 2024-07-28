package loqor.ait.tardis.util;

import com.google.gson.JsonObject;

import java.nio.file.Files;

public class JsonObjectTransform {

    public JsonObject jsonObject;

    public JsonObjectTransform(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /*public void transform() {
        // Directly transform the existing JsonObject

        // Desktop
        if (jsonObject.has("desktop")) {
            JsonObject desktop = jsonObject.remove("desktop").getAsJsonObject();

            // Transform desktop schema
            if (desktop.has("schema")) {
                jsonObject.addProperty("schema", desktop.remove("schema").getAsString());
            }

            // Transform doorPos
            if (desktop.has("doorPos")) {
                JsonObject doorPos = desktop.remove("doorPos").getAsJsonObject();
                if (doorPos.has("x")) {
                    jsonObject.addProperty("doorPos_x", doorPos.remove("x").getAsInt());
                }
                if (doorPos.has("y")) {
                    jsonObject.addProperty("doorPos_y", doorPos.remove("y").getAsInt());
                }
                if (doorPos.has("z")) {
                    jsonObject.addProperty("doorPos_z", doorPos.remove("z").getAsInt());
                }
                if (doorPos.has("rotation")) {
                    jsonObject.addProperty("doorPos_rotation", doorPos.remove("rotation").getAsInt());
                }
            }

            // Transform corners
            if (desktop.has("corners")) {
                JsonObject corners = desktop.remove("corners").getAsJsonObject();
                if (corners.has("first")) {
                    jsonObject.add("corners_first", corners.remove("first"));
                }
                if (corners.has("second")) {
                    jsonObject.add("corners_second", corners.remove("second"));
                }
            }

            // Transform consolePos
            if (desktop.has("consolePos") && desktop.get("consolePos").isJsonArray()) {
                JsonObject consolePos = desktop.getAsJsonArray("consolePos").get(0).getAsJsonObject();
                if (consolePos.has("x")) {
                    jsonObject.addProperty("consolePos_x", consolePos.remove("x").getAsInt());
                }
                if (consolePos.has("y")) {
                    jsonObject.addProperty("consolePos_y", consolePos.remove("y").getAsInt());
                }
                if (consolePos.has("z")) {
                    jsonObject.addProperty("consolePos_z", consolePos.remove("z").getAsInt());
                }
            }
        }

        // Exterior
        if (jsonObject.has("exterior")) {
            JsonObject exterior = jsonObject.remove("exterior").getAsJsonObject();
            if (exterior.has("exterior")) {
                jsonObject.addProperty("exterior", exterior.remove("exterior").getAsString());
            }
            if (exterior.has("variant")) {
                jsonObject.addProperty("variant", exterior.remove("variant").getAsString());
            }
            jsonObject.addProperty("id", "EXTERIOR");
        }

        // Handlers
        if (jsonObject.has("handlers")) {
            JsonObject handlers = jsonObject.remove("handlers").getAsJsonObject();

            // Gravity
            JsonObject gravity = new JsonObject();
            gravity.addProperty("direction", "DOWN");
            gravity.addProperty("id", "GRAVITY");
            handlers.add("GRAVITY", gravity);

            // Travel
            if (handlers.has("TRAVEL")) {
                JsonObject travel = handlers.remove("TRAVEL").getAsJsonObject();
                travel.addProperty("animationTicks", 0);
                travel.addProperty("flightTicks", 0);
                travel.addProperty("targetTicks", 0);
                travel.addProperty("handbrake", false);
                travel.addProperty("autopilot", travel.remove("autopilot").getAsBoolean());
                travel.addProperty("state", travel.remove("state").getAsString());

                if (travel.has("position")) {
                    JsonObject position = travel.remove("position").getAsJsonObject();
                    if (position.has("dimension")) {
                        JsonObject dimension = position.remove("dimension").getAsJsonObject();
                        dimension.addProperty("registry", "minecraft:dimension");
                        travel.add("position_dimension", dimension);
                    }
                    if (position.has("pos")) {
                        jsonObject.add("position_pos", position.remove("pos"));
                    }
                    if (position.has("rotation")) {
                        jsonObject.addProperty("position_rotation", position.remove("rotation").getAsInt());
                    }
                    travel.add("position", position);
                }

                jsonObject.add("TRAVEL", travel);
            }

            // Door
            if (handlers.has("DOOR")) {
                JsonObject door = handlers.remove("DOOR").getAsJsonObject();
                door.addProperty("id", "DOOR");
                jsonObject.add("DOOR", door);
            }

            // Sonic
            JsonObject sonic = new JsonObject();
            sonic.addProperty("id", "SONIC");
            handlers.add("SONIC", sonic);

            // Permissions
            JsonObject permissions = new JsonObject();
            permissions.add("permissions", new JsonObject());
            permissions.addProperty("p19Loyalty", "COMPANION");
            permissions.addProperty("id", "PERMISSIONS");
            handlers.add("PERMISSIONS", permissions);

            // Loyalty
            JsonObject loyalty = new JsonObject();
            JsonObject loyaltyData = new JsonObject();
            loyaltyData.addProperty("3eec9f18-1d0e-3f17-917c-6994e7d034d1", "OWNER");
            loyalty.add("data", loyaltyData);
            loyalty.addProperty("id", "LOYALTY");
            handlers.add("LOYALTY", loyalty);

            // Engine
            JsonObject engine = new JsonObject();
            engine.addProperty("power", true);
            engine.addProperty("hasEngineCore", true);
            engine.addProperty("id", "ENGINE");
            handlers.add("ENGINE", engine);

            // Flight
            JsonObject flight = new JsonObject();
            flight.addProperty("active", false);
            flight.addProperty("id", "FLIGHT");
            handlers.add("FLIGHT", flight);

            // Biome
            JsonObject biome = new JsonObject();
            biome.addProperty("type", "DEFAULT");
            biome.addProperty("id", "BIOME");
            handlers.add("BIOME", biome);

            // Shields
            JsonObject shields = new JsonObject();
            shields.addProperty("id", "SHIELDS");
            handlers.add("SHIELDS", shields);

            // Stats
            JsonObject stats = new JsonObject();
            JsonObject skybox = new JsonObject();
            skybox.addProperty("registry", "minecraft:dimension");
            skybox.addProperty("value", "ait:tardis_dimension");
            stats.add("skybox", skybox);

            JsonObject unlocks = new JsonObject();
            String[] unlockArray = {
                    "ait:console/toyota_legacy",
                    "ait:console/toyota",
                    "ait:console/hartnell_wooden",
                    "ait:console/coral",
                    "ait:console/hartnell_mint",
                    "ait:console/hartnell_kelt",
                    "ait:console/steam_gilded",
                    "ait:console/alnico_blue",
                    "ait:console/steam_cherry",
                    "ait:console/coral_white",
                    "ait:console/toyota_blue",
                    "ait:console/steam_steel",
                    "ait:console/steam",
                    "ait:console/coral_blue",
                    "ait:console/alnico"
            };
            for (String unlock : unlockArray) {
                unlocks.addProperty(unlock, true);
            }
            stats.add("unlocks", unlocks);
            stats.addProperty("id", "STATS");
            handlers.add("STATS", stats);

            // Crash Data
            JsonObject crashData = new JsonObject();
            crashData.addProperty("id", "CRASH_DATA");
            handlers.add("CRASH_DATA", crashData);

            // Waypoints
            JsonObject waypoints = new JsonObject();
            waypoints.addProperty("id", "WAYPOINTS");
            handlers.add("WAYPOINTS", waypoints);

            // Overgrown
            if (handlers.has("OVERGROWN")) {
                JsonObject overgrown = handlers.remove("OVERGROWN").getAsJsonObject();
                overgrown.addProperty("id", "OVERGROWN");
                jsonObject.add("OVERGROWN", overgrown);
            }
        }
    }*/
}
