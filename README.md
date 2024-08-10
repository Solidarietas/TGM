# TGM ![Minecraft Version](https://img.shields.io/badge/supports%20MC%20version-1.21-brightgreen.svg)[![Discord](https://img.shields.io/badge/chat-on%20discord-blue.svg)](https://discord.gg/DwyzBAfpNS)
Team Game Manager. A Team Oriented Minecraft PVP plugin.

## Project Goals

1. **Advanced Game Engine with game logic implemented through modular programming.** 
Managers should offer hooks and data models to modules. 
Modules should be capable of communicating with one another.
The project should strive to make new game type development as straightforward as possible.

2. **Map.json Scripting Language.**
Maps need access to a baseline scripting service that allows for map-specific dynamic content.
As an example, a map should be able to provide different spawn points as the match time progresses.

3. This project is heavily influenced by [PGM](https://github.com/OvercastNetwork/ProjectAres). Our goal with TGM is to shift more of the game logic to Java as opposed to map configuration files. This allows for the rapid development and modernization of gamemodes over time. 

Here's a basic example of what map configuration files look like:
```json
"spawns": [
    {"teams": ["spectators"], "coords": "59, 48, 184.5, -180"},
    {"teams": ["red"], "coords": "149.5, 7, 184.5, 90"},
    {"teams": ["blue"], "coords": "-30.5, 7, 184.5, -90"}
],
"regions": [
    {"id": "red-spawn-protection", "type": "cuboid", "min": "126, 0, 168", "max": "152, oo, 199"},
    {"id": "blue-spawn-protection", "type": "cuboid", "min": "-8, 0, 198", "max": "-34, oo, 167"},

    {"id": "build-height", "type": "cuboid", "min": "-oo, 40, -oo", "max": "oo, oo, oo"}
],
"filters": [
    {
        "type": "enter", "evaluate": "deny", "teams": ["blue"],
        "regions": ["red-spawn-protection"], "message": "&cYou may not enter this region."
    },
    {
        "type": "enter", "evaluate": "deny", "teams": ["red"],
        "regions": ["blue-spawn-protection"], "message": "&cYou may not enter this region."
    },
    {
        "type": "build", "evaluate": "deny", "teams": ["red", "blue"],
        "regions": ["build-height"], "message": "&cYou have reached the max build height."
    }
]
```
  
  
## Local Server Setup
 
1. Start with the latest stable [Paper (PaperSpigot)](https://papermc.io/downloads) build. 

2. Compile the latest version of TGM or download it from our [Jenkins](https://jenkins.bennydoesstuff.me/job/TGM/).
 
3. Create a `Maps` folder in the root directory of your server and insert a supported TGM map. Make sure you also include a `rotation.json` file with the names of maps you would like to be present in the rotation.
    - You can download our Maps folder as a reference on the Maps repository located [here](https://github.com/pvparcade/Maps).
    - If you would like to load multiple map repositories or simply change the location, you can change the setting in the `plugins/TGM/config.yml` file.
 
4. Start the server. 
   - Additionally, if you would like stats to be saved, you need to set up the API and enable the API feature in the `plugins/TGM/config.yml` file.

5. (Optional) Install WorldEdit for added telelport tool functionallity

## Compiling
### Graphical Setup
0. You will need to have `git` installed. The command line tool is recommended but GitHub Desktop should work and can be used.
1. Clone the repository
   1. Using Github Desktop // TODO document
   2. Using Inteillj IDEA, navigate to  `File -> New -> Project from Version Control...`
      1. Paste the URL of this repo in to the URL field and click next through the menu
2. Edit the file called `gradle.properties` in the folder you just created to contain the following:
   ```
   gpr.user=YOUR_GITHUB_USERNAME
   gpr.key=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN_FOR_DOWNLOADING_PACKAGES
   ```
   This will be needed to download BGM, one of the required dependencies for this project.
   1. Click on this link [here](https://github.com/settings/tokens/new) while signed in to your GitHub account and click on the button that says "read:packages" and then click "Generate token". It should begin with `ghp_`.
   2. If you do not have a GitGub account you can use the default token and username in `gradle.properties`, but this is strongly discouraged.
3. Wait for the project to import and click on the Gradle symbol (looks like an elephant) on the right hand side of the screen. A side-menu should pop up on the right.\
  Click on the drop-down menu for `TGM -> Tasks -> shadow -> shadowJar`. Double-click on shadowJar to compile this plugin.
   1. You may have to click on the refresh button (two arrows in a circle in the top left of the new window) to see shadowJar
4. The compiled `.jar` Minecraft Spigot plugin will be located in the `TGM/build/libs/` folder.
### Command Line:
0. You will need to have `git` installed. Optionally install
1. Open a terminal and download/clone the repo to your computer by running the command `git clone https://github.com/Solidarietas/TGM`
2. Edit the file called `gradle.properties` in the folder you just created to contain the following:
   ```
   gpr.user=YOUR_GITHUB_USERNAME
   gpr.key=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN_FOR_DOWNLOADING_PACKAGES
   ```
   This will be needed to download BGM, one of the required dependencies for this project.
   1. Click on this link [here](https://github.com/settings/tokens/new) while signed in to your GitHub account and click on the button that says "read:packages" and then click "Generate token". It should begin with `ghp_`. 
   2. If you do not have a GitGub account you can use the default token and username in `gradle.properties`, but this is strongly discouraged.
3. Inside the TGM folder you just created, run the following command: \
   Windows: `gradlew.bat shadowJar`\
   MacOS/Linux: `./gradlew shadowJar`
4. The compiled `.jar` Minecraft Spigot plugin will be located in the `TGM/build/libs/` folder.

## Developer Tips

1. We use [Lombok](https://projectlombok.org/). Installing the Lombok plugin in your preferred IDE is optional, as it is included in the gradle dependencies but may provide a slightly better development experience.

## Documentation

This plugin takes advantage of a ``map.json`` required by every map to configure the gamemode and to document the coordinates needed by the gamemode to function.  To learn more about the available gamemodes in the plugin, how to configure a map.json, and what features are currently offered through this file, we recommend checking out the Documentation repository located [here](https://github.com/Solidarietas/TGM-Maps-Docs) or view them online at https://solidarietas.github.io/TGM-Maps-Docs/. If you need any additional examples on how to format the JSON's, consider checking out our Maps repository as well located [here](https://github.com/Solidarietas/TGM-Maps).
