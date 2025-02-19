
package mage.cards.d;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.triggers.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.keyword.TrampleAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Outcome;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetSacrifice;

/**
 *
 * @author jeffwadsworth
 *
 */
public final class Doomgape extends CardImpl {

    public Doomgape(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{4}{B/G}{B/G}{B/G}");
        this.subtype.add(SubType.ELEMENTAL);

        this.power = new MageInt(10);
        this.toughness = new MageInt(10);

        // Trample
        this.addAbility(TrampleAbility.getInstance());

        // At the beginning of your upkeep, sacrifice a creature. You gain life equal to that creature's toughness.
        this.addAbility(new BeginningOfUpkeepTriggeredAbility(new DoomgapeEffect()));

    }

    private Doomgape(final Doomgape card) {
        super(card);
    }

    @Override
    public Doomgape copy() {
        return new Doomgape(this);
    }
}

class DoomgapeEffect extends OneShotEffect {

    DoomgapeEffect() {
        super(Outcome.GainLife);
        this.staticText = "sacrifice a creature. You gain life equal to that creature's toughness";
    }

    private DoomgapeEffect(final DoomgapeEffect effect) {
        super(effect);
    }

    @Override
    public DoomgapeEffect copy() {
        return new DoomgapeEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            TargetSacrifice target = new TargetSacrifice(StaticFilters.FILTER_PERMANENT_CREATURE);
            if (controller.choose(Outcome.Sacrifice, target, source, game)) {
                Permanent creature = game.getPermanent(target.getFirstTarget());
                if (creature != null) {
                    if (creature.sacrifice(source, game)) {
                        controller.gainLife(creature.getToughness().getValue(), game, source);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
