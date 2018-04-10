Ducky v5, written in Kotlin

## Running

First, copy `config.json.example` to `config.json`, fill it in, then:

* `./gradlew run` to run
* `./gradlew shadowJar` to build a jar with dependencies, it will be in `build/libs/ducky-VERSION-all.jar`

## Config explanation
* `token` - a string, get it from [here](https://discordapp.com/developers/applications/me/)
* `accountType` - a string, either `BOT` or `CLIENT`
* `botName` - a string - a single word used by the command parser as one of the valid options for %bot%. Only alphanumeric characters are allowed

Licensed under [The GNU General Public License v3.0](LICENSE.md)

## How to add a command
Don't. /s // TODO