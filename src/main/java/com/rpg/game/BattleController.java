package com.rpg.game;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.characters.abilitymanagement.AbilitySlot;
import com.rpg.game.outcome.AbilityOutcome;
import com.rpg.game.outcome.BattleResult;
import com.rpg.userinterface.UserInterface;
import java.util.List;
import lombok.Getter;

public class BattleController {

    // Transient battle state; can be set by abilities later
    private boolean poisonActive = false;
    @Getter
    private boolean enemyDisabledNextTurn = false;

    public BattleResult runBattle(Player player, Enemy enemy) {
        boolean playerDied = false;
        boolean enemyDied = false;

        // Battle loop
        while(player.getPlayerStats().getHealthPoints() > 0 && enemy.getHealthPoints() > 0) {
            executePlayerTurn(player, enemy);

            if(enemy.getHealthPoints() <= 0) {
                enemyDied = true;
                break;
            }

            executeEnemyTurn(player, enemy);

            if(player.getPlayerStats().getHealthPoints() <= 0) {
                playerDied = true;
                break;
            }
        }
        // TODO: write proper end battle messages depending on status
        List<String> messages = List.of("Battle over!");

        return new BattleResult(
                playerDied,
                enemyDied,
                enemyDied ? enemy.getExpAmountWhenKilled() : 0,
                messages
        );
    }

    public void executePlayerTurn(Player player, Enemy enemy) {
        UserInterface.printPlayerHUD(player, enemy);
        UserInterface.showEquippedAbilities(player);

        PlayerFightDecision playerDecision = UserInterface.askPlayerForActionInTurn();

        if (playerDecision == PlayerFightDecision.ATTACK) {
            var outcome = player.physicalAttack(enemy, poisonActive);

            if (outcome.poisonConsumed()) {
                poisonActive = false;
            }

            UserInterface.renderMessages(outcome.messages());

        } else if (playerDecision == PlayerFightDecision.ABILITIES) {
            // Ask which slot to use (0..3)
            AbilitySlot slot = askForAbilitySlot();
            if (slot != null) {
                AbilityOutcome abilityOutcome = switch (slot) {
                    case SLOT_1 -> player.useFirstSlotAbility(enemy);
                    case SLOT_2 -> player.useSecondSlotAbility(enemy);
                    case SLOT_3 -> player.useThirdSlotAbility(enemy);
                    case SLOT_4 -> player.useFourthSlotAbility(enemy);
                };

                // Apply transient flags based on outcome
                if (abilityOutcome.preparedPoisonForNextAttack()) {
                    poisonActive = true;
                }
                if (abilityOutcome.targetDisabled()) {
                    enemyDisabledNextTurn = true;
                }

                UserInterface.renderMessages(abilityOutcome.messages());
            }
        } else {
            // no-op
        }

        // End-of-turn cooldown tick (player side)
        player.abilitiesFacade().tickTurnEnd();
    }

    public void consumeEnemyDisableIfAny() {
        // Call this at enemy turn start
        if (enemyDisabledNextTurn) {
            enemyDisabledNextTurn = false;
        }
    }

    public void executeEnemyTurn(Player player, Enemy enemy) {
        if (enemyDisabledNextTurn) {
            UserInterface.renderMessages(java.util.List.of("Enemy is disabled this turn."));
            enemyDisabledNextTurn = false; // consume the disable
            // Still tick cooldowns to keep pacing consistent across turns
            player.abilitiesFacade().tickTurnEnd();
            return;
        }

        // TODO:
        // Perform enemy action (existing logic)
        // Example placeholder – keep my existing enemy attack flow here
        // int damage = enemy.calculateAndApplyDamage(player.getPlayerStats(), 0);
        // UserInterface.renderMessages(java.util.List.of(enemy.getName() + " attacks for " + damage + " damage."));

        // End-of-enemy-turn cooldown tick
        player.abilitiesFacade().tickTurnEnd();
    }

    private AbilitySlot askForAbilitySlot() {
        UserInterface.renderMessages(
                List.of("Choose ability slot: 0, 1, 2, or 3. Press 'x' to cancel.")
        );
        while (true) {
            String input = UserInterface.userInput.next();
            if (input == null || input.isEmpty()) {
                UserInterface.renderMessages(List.of("Please enter 0, 1, 2, 3 or 'x'."));
                continue;
            }
            char c = input.charAt(0);
            if (c == 'x' || c == 'X') {
                return null; // explicit cancel
            }
            if (c >= '0' && c <= '3') {
                int idx = c - '0';
                return AbilitySlot.values()[idx];
            }
            UserInterface.renderMessages(List.of("Invalid choice. Enter 0, 1, 2, 3 or 'x' to cancel."));
        }
    }
}