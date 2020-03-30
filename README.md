# CurseMeta3ModpackDownloader
This is a simple JavaFx-based Curse Modpack Downloader that relies on
[Dries007's CurseMeta v3](https://github.com/dries007/CurseMeta). This project uses Dries007's
[staging version of the CurseMeta v3](https://staging_cursemeta.dries007.net/).

## Setup
In order to build this project, you must have Java 12 installed. This project uses Gradle as its build
system and uses Zyxist's Chainsaw plugin for module management and Beryx's Badass-Jlink-Plugin for
creating distributable packages.

In order to run the program, use
`./gradlew clean run` (on Unix-based systems), or
`gradlew.bat clean run` (on Windows systems).

In order to build a distributable package, use
`./gradlew clean jlinkZip` (on Unix-based systems), or
`gradlew.bat clean jlinkZip` (on Windows systems).
Once built, the distributable package should be a file called `image.zip` in the project's `build` directory.
