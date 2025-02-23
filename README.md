# Game Lib

Game Lib is an Android application that aggregates your game library from different games stores into a single list.

Currently supported game stores:
- Steam
- GOG
- Epic Games Store
- Amazon Prime Gaming

# Build

To build this application define `api.properties` file in the root of the application with url to game_db service:

```properties
GAME_DB_API_URL="http://localhost:4000"
```