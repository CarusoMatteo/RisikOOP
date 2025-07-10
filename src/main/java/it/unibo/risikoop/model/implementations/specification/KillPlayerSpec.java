package it.unibo.risikoop.model.implementations.specification;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

import java.util.Objects;

import it.unibo.risikoop.model.implementations.PlayerGameContext;

public final class KillPlayerSpec implements Specification<PlayerGameContext> {
    private final Player target;

    public KillPlayerSpec(final Player target) {
        this.target = Objects.requireNonNull(target, "target cannot be null");
    }

    @Override
    public boolean isSatisfiedBy(final PlayerGameContext ctx) {

        Objects.requireNonNull(ctx, "PlayerGameContext cannot be null");

        return target.getKiller()
                .filter(killer -> killer.equals(ctx.player()))
                .isPresent();

        // 3703685093
    }
}
