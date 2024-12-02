<div align="center">

# Ferret

![](https://img.shields.io/github/downloads/cattyngmd/ferret/total) ![](https://img.shields.io/github/commit-activity/w/cattyngmd/ferret) <a href="https://discord.gg/dbH47Pg4Mf"><img src="https://img.shields.io/discord/928792065649819698" /></a>

Free and open source Minecraft mod with [Lua](https://en.wikipedia.org/wiki/Lua_(programming_language)) scripting API

Lua API https://cattyn.gitbook.io/ferret-lua-api/

Discord https://discord.gg/dbH47Pg4Mf
  
# Building
  
To build Ferret you need to have [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) installed
  
</div>

1. Download Ferret source code by clicking `Code -> Download ZIP` on [the main page](https://github.com/cattyngmd/Ferret)
2. Extract the source code somewhere and open cmd (on Windows) or Terminal
3. Type `gradlew build` (if you're on Windows) or `chmod +x ./gradlew && ./gradlew build` (if you're on Linux) and wait until the building process finishes
4. After that you should have a file called `Ferret-0.0.1.jar` inside `<ferret src>/build/libs` folder
5. Put it inside .minecraft/mods folder and start the game

<div align="center">
  
# Downloading
  
You can download stable prebuilt JARs from [the releases page](https://github.com/cattyngmd/Ferret/releases)

Unstable prebuilt JARs are available on [our GitHub Actions page](https://github.com/cattyngmd/Ferret/actions), but you need to have a GitHub account to access them
  
Please note that Ferret requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) to be installed

# Usage
  
Download Ferret scripts from [here](https://github.com/cattyngmd/Ferret-Scripts) and put them into your `.minecraft/ferret/scripts` folder

The default command prefix is `.` . You can change it by typing `.setprefix "<prefix>"`
  
# Credits

</div>

- <a href="https://github.com/cattyngmd"> **Cattyn** </a> - lead developer
- <a href="https://github.com/zimnycat"> **ZimnyCat** </a> - second lead developer
- <a href="https://github.com/mr-nv"> **mrnv** </a> - yarn remapper and mixins support
- <a href="https://github.com/luaj/luaj"> **LuaJ** </a> - lua interpreter

<div align="center">
  
# License (MIT)
  
  </div>

```
Copyright 2022 cattyn!

Permission is hereby granted, free of charge,
to any person obtaining a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice
shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
