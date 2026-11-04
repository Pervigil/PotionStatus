# PotionStatus

A lightweight Fabric client mod for Minecraft Java Edition 1.21.11.

PotionStatus displays your currently active status effects directly on the HUD, including each effect's level and remaining duration. No inventory screen required.

## Features
- Live list of active status effects
- Remaining time shown as minutes and seconds
- Effect amplifier shown as a Roman numeral
- Compact translucent HUD panel
- Automatically hides when there are no active effects
- Respects Minecraft's HUD visibility setting

## Compatibility
- Minecraft Java Edition 1.21.11
- Fabric Loader
- Fabric API
- Java 21

## Build and release
GitHub Actions builds the mod when changes are pushed to main. The workflow publishes the JAR to the v1.0.0 GitHub Release.

For a local build, use Gradle 9.2.1 and run: gradle build

The compiled mod is written to build/libs/potionstatus-1.0.0.jar.

## Install
1. Install Fabric Loader for Minecraft 1.21.11.
2. Install the matching Fabric API.
3. Download potionstatus-1.0.0.jar from the Releases page.
4. Place the JAR in your Minecraft mods folder.
5. Launch the Fabric profile.

## License
MIT. See LICENSE.
