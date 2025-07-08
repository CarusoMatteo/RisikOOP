package it.unibo.risikoop.model.implementations.specification;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

import java.util.Objects;

import it.unibo.risikoop.model.implementations.PlayerGameContext;

public class KillPlayerSpec implements Specification<PlayerGameContext> {
    private final Player target;

    public KillPlayerSpec(Player target) {
        this.target = Objects.requireNonNull(target, "target cannot be null");
    }

    @Override
    public boolean isSatisfiedBy(PlayerGameContext ctx) {
        return target.getKiller()
                .filter(killer -> killer.equals(ctx.player()))
                .isPresent();
    }
}
