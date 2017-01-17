package quicksplit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import quicksplit.dao.PlayerDao;
import quicksplit.model.PlayerModel;

@Controller
public class PlayerController {

	@Autowired PlayerDao playerDao;

	@GetMapping( "/players" )
	public String listPlayers( final Model model )
	{
		model.addAttribute( "players", playerDao.list() );
		return "players";
	}

	@PostMapping( "/add-player" )
	public String createPlayer( @RequestParam( "name" ) final String name )
	{
		if( playerDao.findByName( name ) != null )
        {
            throw new IllegalArgumentException( "Player with that name already exists" );
        }

		playerDao.insert( name );
		return "redirect:/players";
	}

	@PostMapping( "/edit-player" )
	public String editPlayer( @RequestParam( "id" ) final Long playerId,
							  @RequestParam( "name" ) final String name )
	{
        final PlayerModel model = new PlayerModel();
        model.setId( playerId );
        model.setName( name );
        playerDao.update( model );

        return "redirect:/players";
   	}
}
