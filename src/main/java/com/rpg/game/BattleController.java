package com.rpg.game;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.userinterface.UserInterface;

public class BattleController {

    // Transient battle state; can be set by abilities later
    private boolean poisonActive = false;


    public void executePlayerTurn(Player player, Enemy enemy) {
        UserInterface.printPlayerHUD(player, enemy);
        PlayerFightDecision playerDecision = UserInterface.askPlayerForActionInTurn();

        if(playerDecision == PlayerFightDecision.ATTACK) {
            var outcome = player.physicalAttack(enemy, poisonActive);

            if (outcome.poisonConsumed()) {
                poisonActive = false;
            }

            UserInterface.renderMessages(outcome.messages());

            if (outcome.targetDefeated()) {
                // trigger rewards? end turn sequence? etc idk
            }



        } else if(playerDecision == PlayerFightDecision.ABILITIES) {
            // TBD
        } else {
            // no op (do something?)
        }
    }

    public void enablePoisonForNextAttack() {
        this.poisonActive = true;
    }

}
