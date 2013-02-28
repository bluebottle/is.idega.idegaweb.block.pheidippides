package is.idega.idegaweb.pheidippides.rest;

import is.idega.idegaweb.pheidippides.dao.PheidippidesDao;
import is.idega.idegaweb.pheidippides.rest.pojo.GiftCard;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.util.expression.ELUtil;

@Path("/giftCards/")
public class GiftCards {

	@Autowired
	private PheidippidesDao dao;
	
	@GET
	@Path("/{code}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public GiftCard getGiftCard(@PathParam("code") String code) {
		is.idega.idegaweb.pheidippides.data.GiftCard giftCard = getDao().getGiftCard(code);
		if (giftCard == null) {
			throw new NotFoundException("No gift card found with provided code...");
		}

		int used = getDao().getGiftCardUsageSum(giftCard);
		
		GiftCard card = new GiftCard();
		card.setAmount(giftCard.getAmount());
		card.setCode(code);
		card.setRemainder(giftCard.getAmount() - used);
		card.setValidTo(giftCard.getHeader().getValidTo());
		
		return card;
	}
	
	private PheidippidesDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return dao;
	}
}