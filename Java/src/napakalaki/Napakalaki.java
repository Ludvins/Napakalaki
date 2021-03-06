
package napakalaki;
import java.util.ArrayList;
import java.util.Random;


public class Napakalaki {
    
    private static Napakalaki instance = null;
    private Monster currentMonster;
    private final CardDealer cardDealer = CardDealer.getInstance();
    private int currentPlayer;
    private ArrayList<Player> players; 
    
    private Napakalaki () {} 
    
    public static Napakalaki getInstance () {
        
        if (instance == null) instance = new Napakalaki();
       
        return instance;
    }
    
    private void initPlayers(ArrayList<String> names){
        
        this.players = new ArrayList();

        for (String a : names){
            this.players.add(new Player(a));
        }
        
        Random rd = new Random();
        this.currentPlayer = rd.nextInt(this.players.size());
        
    }
    
    private void nextPlayer(){
        
       this.currentPlayer = (this.currentPlayer + 1) % this.players.size();
    }
    
    public boolean nextTurnIsAllowed(){
 
        return this.players.get(currentPlayer).ValidState();

    }
    
    private void setEnemies() {
        
        Random rd = new Random();
        int n;
        
        for (int i = 0; i < this.players.size(); i++){
     
            do {
            
                n = rd.nextInt( this.players.size());
        
            }
            while(n == i); 
            
            players.get(i).setEnemy(this.players.get(n));
        }
    }
    
    public CombatResult developCombat(){
        
        CombatResult combat = this.players.get(currentPlayer).combat(this.currentMonster);
        
        if (combat == CombatResult.LOSEANDCONVERT){
            CultistPlayer c = new CultistPlayer(this.players.get(this.currentPlayer), this.cardDealer.nextCultist());
            
            for (Player p : this.players){
                
                if (p.getEnemy() == players.get(currentPlayer)) p.setEnemy(c);
                
            }
            
            players.set(currentPlayer, c);
            
        }
        
        return combat;
    }   
    /*
    These three methods do just loop through an Array and apply another method over the elements.
    I'll look for a way of make this better.    
    */
    public void discardVisibleTreasures(ArrayList<Treasure> treasures){
        
        for (Treasure t : treasures){
            
            this.players.get(currentPlayer).discardVisibleTreasure(t);
            
        }
        
    }
    
    public void discardHiddenTreasures(ArrayList<Treasure> treasures){
        
        for (Treasure t: treasures){
            
            this.players.get(currentPlayer).discardHiddenTreasure(t);
        
        }
    }
    
    public void makeTreasureVisible(ArrayList<Treasure> treasure){
        
        for (Treasure t : treasure){
            
            this.players.get(currentPlayer).makeTreasureVisible(t);
        }
    }
    
    public void initGame (ArrayList<String> names) {
        
        this.initPlayers(names);
        
        this.setEnemies();
        
        this.cardDealer.initCards();
        
        this.nextTurn();
    }
    
    public Player getCurrentPlayer(){
        
        return this.players.get(currentPlayer);
    }
    
    public Monster getCurrentMonster() {
        
        return this.currentMonster;
        
    }
    
    public boolean nextTurn(){
        
        boolean ret = this.nextTurnIsAllowed();
        
        if(ret){
            
            this.currentMonster = this.cardDealer.nextMonster();
            this.nextPlayer();
                     
            if (this.players.get(currentPlayer).isDead())this.players.get(currentPlayer).initTreasures();
            
        }
        
        return ret;
    }
    
    public boolean endOfGame(CombatResult cr){
        
        return cr == CombatResult.WINGAME;
    }
    
}
