package club.pvparcade.tgm.util;

import club.pvparcade.api.models.Author;

public class Strings {
    public static String formatTime(double t) {
        double time = t;
        boolean negative = false;
        if (time < 0) {
            negative = true;
            time *= -1;
        }
        int hours = (int) time / 3600;
        int minutes = (int) (time - (hours * 3600)) / 60;
        int seconds = (int) time - (hours * 3600) - (minutes * 60);
        String hoursString = hours + "";
        String minutesString = minutes + "";
        String secondsString = seconds + "";
        while (minutesString.length() < 2) {
            minutesString = "0" + minutesString;
        }
        while (secondsString.length() < 2) {
            secondsString = "0" + secondsString;
        }
        return (negative ? "-" : "") + (hours == 0 ? "" : hoursString + ":") + minutesString + ":" + secondsString;
    }

    public static String getTechnicalName(String s) {
        s = s.toUpperCase().replace(" ", "_");
        return switch (s) {
            case "CONFUSION" -> "NAUSEA";
            case "DAMAGE_RESISTANCE" -> "RESISTANCE";
            case "FAST_DIGGING" -> "HASTE";
            case "HARM" -> "INSTANT_DAMAGE";
            case "HEAL" -> "INSTANT_HEALTH";
            case "INCREASE_DAMAGE" -> "STRENGTH";
            case "SLOW" -> "SLOWNESS";
            case "SLOW_DIGGING" -> "MINING_FATIGUE";
            case "JUMP" -> "JUMP_BOOST";
            default -> s;
        };
    }

    public static String getAgo(long timestamp) {
        double time = (System.currentTimeMillis() - timestamp) / 1000D;

        int t;
        if ((t = (int) Math.floor(time / 31536000)) > 0) {
            return (t == 1) ? "1 year" : t + " years";
        } else if ((t = (int) Math.floor(time / 2592000)) > 0) {
            return (t == 1) ? "1 month" : t + " months";
        } else if ((t = (int) Math.floor(time / 604800)) > 0) {
            return (t == 1) ? "1 week" : t + " weeks";
        } else if ((t = (int) Math.floor(time / 86400)) > 0) {
            return (t == 1) ? "1 day" : t + " days";
        } else if ((t = (int) Math.floor(time / 3600)) > 0) {
            return (t == 1) ? "1 hour" : t + " hours";
        } else if ((t = (int) Math.floor(time / 60)) > 0) {
            return (t == 1) ? "1 minute" : t + " minutes";
        } else if ((t = (int) Math.floor(time)) > 0) {
            return (t == 1) ? "1 second" : t + " seconds";
        } else {
            return "moments";
        }
    }

    public static String getFullAgo(long timestamp) {
        double time = (System.currentTimeMillis() - timestamp) / 1000D;
        StringBuilder result = new StringBuilder();
        int t;
        if ((t = (int) Math.floor(time / 31536000)) > 0) {
            result.append((t == 1) ? "1 year " : t + " years ");
            time -= t * 31536000;
        }
        if ((t = (int) Math.floor(time / 2592000)) > 0) {
            result.append((t == 1) ? "1 month " : t + " months ");
            time -= t * 2592000;
        }
        if ((t = (int) Math.floor(time / 604800)) > 0) {
            result.append((t == 1) ? "1 week " : t + " weeks ");
            time -= t * 604800;
        }
        if ((t = (int) Math.floor(time / 86400)) > 0) {
            result.append((t == 1) ? "1 day " : t + " days ");
            time -= t * 86400;
        }
        if ((t = (int) Math.floor(time / 3600)) > 0) {
            result.append((t == 1) ? "1 hour " : t + " hours ");
            time -= t * 3600;
        }
        if ((t = (int) Math.floor(time / 60)) > 0) {
            result.append((t == 1) ? "1 minute " : t + " minutes ");
            time -= t * 60;
        }
        if ((t = (int) Math.floor(time)) > 0) {
            result.append((t == 1) ? "1 second " : t + " seconds ");
        }
        return result.toString().trim();
    }

    public static String getAuthorUsername(Author author) {
        return author.getDisplayUsername() != null ? author.getDisplayUsername() : author.getUsername() != null ? author.getUsername() : "Unknown";
    }

    public static String capitalizeString(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Allow setting item/entity/block 1.20.5+ Components using their strings
    public static String setComponent(String existingComponents, String newComponent) {
        // Handle empty or null components
        if (existingComponents == null || existingComponents.isEmpty() || existingComponents.equals("[]")) {
            return "[" + newComponent + "]";
        }

        // Extract the tag name from the newComponent
        String tagName = newComponent.substring(0, newComponent.indexOf('='));

        // Check if the tag already exists
        int startIndex = existingComponents.indexOf(tagName);
        if (startIndex != -1) {
            // Find the end index of the existing tag component
            int endIndex = existingComponents.indexOf('}', startIndex) + 1;

            // Replace the existing component with the new one
            return existingComponents.substring(0, startIndex) + newComponent + existingComponents.substring(endIndex);
        } else {
            // If the tag doesn't exist, add it as a new component
            existingComponents = existingComponents.substring(0, existingComponents.length() - 1);
            return existingComponents + ", " + newComponent + "]";
        }
    }
}
