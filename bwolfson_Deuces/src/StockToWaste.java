import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;

/**
 * Move card from top of deck to waste piles
 * @author Barrett
 *
 */
public class StockToWaste extends Move {

	MultiDeck stock;
	Pile waste;
	
	public StockToWaste(MultiDeck stock, Pile waste){
		this.stock = stock;
		this.waste = waste;
	}
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){return false;}
		
		Card c = stock.get();
		waste.add(c);
		game.updateNumberCardsLeft(-1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		Card c = waste.get();
		stock.add(c);
		game.updateNumberCardsLeft(+1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		return !stock.empty();
	}

}
