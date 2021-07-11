cd Bukkit
mvn release:update-versions -q -DautoVersionSubmodules=true

cd ../Bungeecord
mvn release:update-versions -q -DautoVersionSubmodules=true

cd ..
mvn
