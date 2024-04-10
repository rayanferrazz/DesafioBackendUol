package desafio.backend.uol.domain;

import desafio.backend.uol.dto.PlayerDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;

@Entity(name = "players")
@Table(name = "players")
@EqualsAndHashCode(of = "id")
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	@Email
	private String email;
	
	private Integer phoneNumber;
	
	private String codiname;
	
	private GroupType groupType;

	public Player() {
	}
	
	public Player(PlayerDTO dto) {
		this.name = dto.name();
		this.email = dto.email();
		this.phoneNumber = dto.phoneNumber();
		this.groupType = dto.groupType();
	}
	
	public Player(Long id, @NotBlank String name, @NotBlank @Email String email, Integer phoneNumber, String codiname,
			GroupType groupType) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.codiname = codiname;
		this.groupType = groupType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCodiname() {
		return codiname;
	}

	public void setCodiname(String codiname) {
		this.codiname = codiname;
	}

	public GroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(GroupType groupType) {
		this.groupType = groupType;
	}
}