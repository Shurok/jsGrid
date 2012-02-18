package ua.kpi.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import ua.kpi.model.Page;
import ua.kpi.model.PageResponse;

public class PageService {

	private final static Logger LOG = Logger.getLogger(PageService.class);

	private final static int atemptLimit = 5;

	public Page getPage() {
		int iattempt = 0;
		String page = "http://www.oracle.com/";
		String source = getPageHtmlSource(page);
		while (source.length() == 1) {
			page = "http://www.oracle.com/";
			source = getPageHtmlSource(page);
			if (++iattempt < atemptLimit) {
				source = "STOP";
				LOG.info("Server cannot start working with new node, setting STOP command"
						+ page);
				break;
			}
		}
		return new Page(page, source);
	}

	public Page setResponse(PageResponse response) {
		int iattempt = 0;
		LOG.info("Server got response from " + response.getUrl());
		// set to db
		String page = "http://www.yandex.ru/";
		if (response.getUrls().length > 0) {
			page = response.getUrls()[(int) (Math.random() * response.getUrls().length)];
		}
		String source = getPageHtmlSource(page);
		while (source.length() == 1) {
			page = response.getUrls()[(int) (Math.random() * response.getUrls().length)];
			source = getPageHtmlSource(page);
			if (++iattempt < atemptLimit) {
				source = "STOP";
				LOG.info("Server cannot start working with new node, setting STOP command"
						+ page);
				break;
			}
		}
		return new Page(page, source);
	}

	private String getPageHtmlSource(String pageUrl)

	{
		StringBuffer buffer = new StringBuffer();
		try {
			URL site = new URL(pageUrl);
			URLConnection yc = site.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			LOG.debug("Server cannot get page " + pageUrl, e);
		}
		return buffer.toString();

	}
}