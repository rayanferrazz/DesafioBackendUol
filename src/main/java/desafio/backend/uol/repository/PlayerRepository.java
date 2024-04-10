package desafio.backend.uol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import desafio.backend.uol.domain.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}