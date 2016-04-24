package at.joma.stackoverflow.cdi;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class SomeCache {

	private String cacheValue;

	public String getCacheValue() {
		return cacheValue;
	}

	public void setCacheValue(String cacheValue) {
		this.cacheValue = cacheValue;
	}

}
