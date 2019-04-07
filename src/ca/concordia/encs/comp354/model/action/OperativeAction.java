package ca.concordia.encs.comp354.model.action;

import ca.concordia.encs.comp354.controller.Operative;
import ca.concordia.encs.comp354.model.GameAction;

/**
 * Base class for actions that may be produced by {@link Operative}s. May only be extended by classes in this 
 * package, thereby restricting the number of valid actions an {@link Operative.Strategy} may execute.
 * @author Nikita Leonidov
 *
 */
public abstract class OperativeAction extends GameAction {

    OperativeAction(Operative owner) {
        super(owner.getTeam());
    }

}
