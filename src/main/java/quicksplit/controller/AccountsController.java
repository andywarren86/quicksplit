package quicksplit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import quicksplit.dao.PlayerDao;
import quicksplit.dao.TransactionDao;
import quicksplit.model.PlayerModel;
import quicksplit.model.Transaction;

@Controller
public class AccountsController {

    @Autowired PlayerDao playerDao;
	@Autowired TransactionDao transactionDao;

    @InitBinder
    public void binder( final WebDataBinder binder ) {
        binder.addCustomFormatter( new DateFormatter( "yyyy-MM-dd" ) );
    }

    @GetMapping( "/accounts" )
    public String accounts( final Model model )
    {
        final Map<PlayerModel, Long> balances = transactionDao.listOutstandingBalances();
        model.addAttribute( "balances", balances )
        	.addAttribute( "total", balances.values().stream().mapToLong( Long::longValue ).sum() );

        return "accounts";
    }

    @GetMapping( "/transactions" )
    public String transactions( @RequestParam( "player-id" ) final long playerId,
                                final Model model )
    {
        final PlayerModel player = playerDao.findById( playerId );

        /*
        if( player == null )
        {
            throw new IllegalStateException( "Invalid player selected" );
        }
        */

        final List<Transaction> transactions =
            transactionDao.listByPlayer( player.getId() );

        // reverse transactions to display in descending order
        final List<Transaction> descTransactions = new ArrayList<>( transactions );
        Collections.reverse( descTransactions );

        model.addAttribute( "transactions", descTransactions )
            .addAttribute( "player", player );

        return "transactions";
    }

    @PostMapping( "/add-transaction" )
    public String addTransaction( @RequestParam( "player-id" ) final long playerId,
                                  @RequestParam( "date" ) final Date date,
                                  @RequestParam( "type" ) final String type,
                                  @RequestParam( "amount" ) final BigDecimal amount,
                                  @RequestParam( "description" ) final String description )
    {
        long centAmount = amount.multiply( BigDecimal.valueOf( 100 ) ).longValue();
        if( "WITHDRAWAL".equals( type ) ) {
            centAmount *= -1;
        }

        final Transaction model = new Transaction();
        model.setPlayerId( playerId );
        model.setDate( date );
        model.setAmount( centAmount );
        model.setDescription( description );

        transactionDao.insert( model );

        return "redirect:/transactions?player-id=" + playerId;
    }

}
