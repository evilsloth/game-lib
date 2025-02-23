# Game Lib

Game Lib is an Android application that aggregates your game library from different games stores into a single list.

Currently supported game stores:
- Steam
- GOG
- Epic Games Store
- Amazon Prime Gaming

![1](https://github.com/user-attachments/assets/b611b5a9-f104-43d6-ba2a-ae0a1db0bb47)
![2](https://github.com/user-attachments/assets/0febf500-a70f-4120-b056-2079f01719fa)
![3](https://github.com/user-attachments/assets/e1f7d3a8-cc21-47aa-9e44-810a88e3f36e)
![4](https://github.com/user-attachments/assets/cda609c0-07dd-4f72-82c9-edaab7c9dc86)


# Build

To build this application define `api.properties` file in the root of the application with url to game_db service:

```properties
GAME_DB_API_URL="http://localhost:4000"
```
