package it.unibo.risikoop.model.implementations.specification;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

import java.util.Objects;

import it.unibo.risikoop.model.implementations.PlayerGameContext;

/**
 * Specification that checks if a player has killed a specified target player.
 * The specification is satisfied if the target player was killed by the player
 * in the context.
 */
public final class KillPlayerSpec implements Specification<PlayerGameContext> {
    private final Player target;

    /**
     * Constructs a KillPlayerSpec with the specified target player.
     * The target player must not be null.
     *
     * @param target the player that must
     */
    public KillPlayerSpec(final Player target) {
        this.target = Objects.requireNonNull(target, "target cannot be null");
    }

    @Override
    public boolean isSatisfiedBy(final PlayerGameContext ctx) {

        Objects.requireNonNull(ctx, "PlayerGameContext cannot be null");

        return target.getKiller()
                .filter(killer -> killer.equals(ctx.player()))
                .isPresent();
    }
}
