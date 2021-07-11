package nl.svenar.powerranks.storage;

import java.util.Collection;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class MYSQLDataHandler extends BaseDataHandler {

    @Override
    public void setup(PowerRanks plugin) {
        super.setup(plugin);
    }

    @Override
    public Collection<PRRank> loadRanks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<PRPlayer> loadPlayers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveRanks(Collection<PRRank> ranks) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void savePlayers(Collection<PRPlayer> players) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveRank(PRRank rank) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void savePlayer(PRPlayer player) {
        // TODO Auto-generated method stub
        
    }
    
}
