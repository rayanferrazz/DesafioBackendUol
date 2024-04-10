package desafio.backend.uol.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import desafio.backend.uol.domain.Player;
import desafio.backend.uol.dto.PlayerDTO;
import desafio.backend.uol.service.PlayerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/players")
public class PlayerController {

	@Autowired
	private PlayerService playerService;
	
	@PostMapping
	public ResponseEntity<Player> createPlayer(@RequestBody @Valid PlayerDTO dto) {
		Player newPlayer = playerService.createPlayer(dto);
		return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Player>> getAllPlayers() {
		return new ResponseEntity<>(playerService.getAllPlayers(), HttpStatus.OK);
	}
}