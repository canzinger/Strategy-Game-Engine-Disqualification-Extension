package dev.entze.sge.engine.game.tournament;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.game.MatchResult;
import dev.entze.sge.game.Game;
import java.util.List;
import java.util.concurrent.Callable;

public interface Tournament<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> extends
    Callable<List<MatchResult<G, E>>> {

  String toTextRepresentation();

}
