package plugin.shops;

import com.google.gson.JsonObject;

import rs2.game.model.entity.item.Item;
import rs2.util.GsonParser;

public final class ShopParser extends GsonParser {

	public ShopParser() {
		super("./data/shop/shops");
	}

	@Override
	protected void parse(JsonObject data) {
		
		String name = data.get("name").getAsString();
		
		Item[] items = builder.fromJson(data.get("items"), Item[].class);
		
		boolean restock = data.get("restock").getAsBoolean();
		
		boolean canSell = data.get("canSell").getAsBoolean();
		
		Currency currency = Currency.valueOf(data.get("currency").getAsString());
		
		Shops.shops.put(name, new Shop(name, items, restock, canSell, currency));
		
	}


}
