package desafio.backend.uol.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import desafio.backend.uol.domain.GroupType;
import desafio.backend.uol.service.CodinameService;

@Component
public class CodinameHandler {

	@Autowired
	private CodinameService codinameService;
	
	public String findCodiname(GroupType groupType) {
		if (groupType == GroupType.AVENGERS ) {
			String firstMatch = codinameService.getAvangersCodinameList().stream().findFirst()
					.orElseThrow();	
			this.codinameService.getAvangersCodinameList().remove(firstMatch);
			return firstMatch;
		}
		String firstMatch = codinameService.getJusticeLeagueCodinameList().stream().findFirst()
				.orElseThrow();
		this.codinameService.getJusticeLeagueCodinameList().remove(firstMatch);
		return firstMatch;
	}
}