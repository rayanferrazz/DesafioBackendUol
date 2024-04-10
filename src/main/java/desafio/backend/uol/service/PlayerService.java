package desafio.backend.uol.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import desafio.backend.uol.config.CodinameHandler;
import desafio.backend.uol.domain.GroupType;
import desafio.backend.uol.domain.Player;
import desafio.backend.uol.dto.PlayerDTO;
import desafio.backend.uol.repository.PlayerRepository;

@Service
public class PlayerService {

	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private CodinameHandler handler;
	
	public Player createPlayer(PlayerDTO dto) {
		Player newPlayer = new Player(dto);
		String codiname = getCodiname(dto.groupType());
		newPlayer.setCodiname(codiname);
		return playerRepository.save(newPlayer);
	}
	
	private String getCodiname(GroupType groupType) {
		return handler.findCodiname(groupType);
	}
	
	public List<Player> getAllPlayers() {
		return playerRepository.findAll();
	}
}