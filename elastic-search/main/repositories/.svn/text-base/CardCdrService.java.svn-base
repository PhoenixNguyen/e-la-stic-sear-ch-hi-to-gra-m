package main.repositories;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import vn.onepay.search.entities.CardCdr;

@Service
public class CardCdrService {
	@Resource
	CardCdrRepository cardCdrRepository;

	public CardCdrRepository getCardCdrRepository() {
		return cardCdrRepository;
	}

	public void setCardCdrRepository(CardCdrRepository cardCdrRepository) {
		this.cardCdrRepository = cardCdrRepository;
	}

	public void save(CardCdr object) {
		cardCdrRepository.save(object);
    }
	
	public void bulkSave(List<CardCdr> objects) {
		cardCdrRepository.save(objects);
    }
	
	public boolean checkExist(){
		return cardCdrRepository.exists("id");
	}
	
	public void deleteAll(){
		cardCdrRepository.deleteAll();
	}
}
