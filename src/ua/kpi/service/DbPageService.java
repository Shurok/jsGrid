package ua.kpi.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.kpi.model.Page;
import ua.kpi.model.PageResponse;
import ua.kpi.scs.dao.impl.UrlDaoImpl;
import ua.kpi.scs.entities.Url;

@Component
public class DbPageService {

	private final static Logger LOG = Logger.getLogger(DbPageService.class);

	private UrlDaoImpl dao;

	public Page getPage() {
		String pageSource = null;

		Page page = new Page();
		while ((pageSource == null) || (pageSource.length() == 0)) {

			page.setUrl(dao.findAddressToParse());
			pageSource = getPageHtmlSource(page.getUrl());
		}
		page.setSource(pageSource);
		return page;

	}

	public Page getPagebyUrl(final String url) {
		String pageSource = null;
		final Page page = new Page();

		page.setUrl(url);
		pageSource = getPageHtmlSource(page.getUrl());
		page.setSource(pageSource);

		return page;
	}

	public void setUrlToDb(String urlStr) {
		Url url = new Url();
		url.setUrl(urlStr);
		dao.save(url);
	}

	@Autowired
	public void setDao(UrlDaoImpl dao) {
		this.dao = dao;
	}

	public UrlDaoImpl getDao() {
		return dao;
	}

	public void setResponse(PageResponse pageResponse) {
		String[] urls = pageResponse.getUrls();
		for (int i = 0; i < urls.length; i++) {
			if ((urls[i] == null) || (urls[i].length() == 0) || (urls[i].length() > Url.MAX_URL_LENGTH) || (urls[i] == "")) {
				LOG.info("Cannot save URL " + urls[i]);

			} else {
				Url url = new Url();
				url.setUrl(urls[i]);
				try {
					dao.save(url);
				} catch (Exception e) {
					LOG.warn("Somthing strange with DB Cannot save URL " + url, e);
				}
			}
		}

	}

	private String getPageHtmlSource(String pageUrl)

	{
		StringBuffer buffer = new StringBuffer();
		try {
			URL site = new URL(pageUrl);
			URLConnection yc = site.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
				if (buffer.length() > 1000000) {
					LOG.debug("Page is too long length = " + buffer.length() + ", so we'll ignore it");
					break;
				}
			}
			in.close();
		} catch (Exception e) {
			LOG.debug("Server cannot get page " + pageUrl, e);
		}

		LOG.debug("Length of buffer " + buffer.length());

		return buffer.toString();

	}
}
