# Strategy Game Engine
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Entze/strategy-game-engine/Java%20CI?logo=github&style=for-the-badge)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Entze/Strategy-Game-Engine/Manual%20CI?color=lightgrey&label=Manual&logo=github&style=for-the-badge)

![Gradle 7.6](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white&label=7.6) ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white&label=1.11)

A strategy game engine written in Java.

## Installation
### Gradle
#### Jitpack (no GitHub Account required)
Add the following to your `build.gradle`:

```build.gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation("com.github.Entze:Strategy-Game-Engine:v1.0.4")
}

```

#### GitHub Packages (GitHub Account required)
Add the following to your `build.gradle`:

```build.gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/Entze/Strategy-Game-Engine")
        credentials {
            username = project.findProperty("gpr.user") ?: findProperty("github.actor") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") ?: findProperty("github.token") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("at.ac.tuwien.ifs.sge:sge:1.0.4")
}
```

This *requires* that either `gpr.user` or `github.actor` are set in the gradle config or the environment variable `GITHUB_ACTOR` is set (equivalently for the key/token).

Usually this can be done by adding a `gradle.properties` with the (unquoted) key value pairs like so:

```gradle.properties
github.actor=MyUserName
github.token=ghp_...
```

### Maven
See the [GitHub packages](https://github.com/Entze/Strategy-Game-Engine/packages/) page for a guide how to add sge as a dependency in a maven project.

### From Source
```bash
./gradlew jar shadowJar sourcesJar javadocJar
```

This produces four jar files (in `build/libs/`):

One executable

- `sge-1.0.4-exe.jar` (Executable)

And three jars usually used for development

- `sge-1.0.4.jar` (Library)
- `sge-1.0.4-sources.jar` (Sources)
- `sge-1.0.4-javadoc.jar` (Documentation)

## Usage
For an extensive overview see:
```bash
java -jar sge-1.0.4-exe.jar --help
```

If you want to let two agents `agent1.jar` and `agent2.jar` play a game of `game.jar` against each other run the command:

```bash
java -jar sge-1.0.4-exe.jar match game.jar agent1.jar agent2.jar
```

There is also a [manual](https://github.com/Entze/Strategy-Game-Engine/releases/download/v1.0.4/SGE-MANUAL.pdf) available.

## Available Games
| Name | Latest Version |
|---|:---:|
| [Risk](https://github.com/Entze/sge-risk) | ![GitHub release (latest by date)](https://img.shields.io/github/v/release/Entze/sge-risk?label=&style=for-the-badge&sort=semver) | 
| [Hexapawn](https://github.com/Entze/sge-hexapawn) | ![GitHub release (latest by date)](https://img.shields.io/github/v/release/Entze/sge-hexapawn?label=&style=for-the-badge&sort=semver) |
| [Kalaha](https://github.com/Entze/sge-kalaha) | ![GitHub release (latest by date)](https://img.shields.io/github/v/release/Entze/sge-kalaha?label=&style=for-the-badge&sort=semver) |
| [Dicepoker](https://github.com/Entze/sge-dicepoker) | ![GitHub release (latest by date)](https://img.shields.io/github/v/release/Entze/sge-dicepoker?label=&style=for-the-badge&sort=semver) |

## Available Agents

| Name | Latest Version |
|---|:---:|
| [Alpha-Beta](https://github.com/Entze/sge-alphabetaagent) | ![GitHub release (latest by date)](https://img.shields.io/github/v/release/Entze/sge-alphabetaagent?label=&style=for-the-badge&sort=semver) |
| [Monte-Carlo-Tree-Search](https://github.com/Entze/sge-mctsagent) | ![GitHub release (latest by date)](https://img.shields.io/github/v/release/Entze/sge-mctsagent?label=&style=for-the-badge&sort=semver) |
| [Random](https://github.com/Entze/sge-randomagent) | ![GitHub release (latest by date)](https://img.shields.io/github/v/release/Entze/sge-randomagent?label=&style=for-the-badge&sort=semver) |

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[GNU AGPLv3](https://choosealicense.com/licenses/agpl-3.0/)
