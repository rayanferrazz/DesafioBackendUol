package desafio.backend.uol.dto;

import desafio.backend.uol.domain.GroupType;

public record PlayerDTO(String name, String email, Integer phoneNumber, GroupType groupType) {
}