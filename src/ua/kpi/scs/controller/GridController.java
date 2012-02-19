package ua.kpi.scs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ua.kpi.model.PageResponse;
import ua.kpi.service.DbPageService;

@Controller
public class GridController {
	private static final Logger LOGGER = Logger.getLogger(GridController.class);

	private DbPageService dbPageService;

	@RequestMapping(value = "process.htm")
	public String showPage(final HttpServletRequest request) {
		return "process";
	}

	@RequestMapping(value = "setUrl.htm")
	public @ResponseBody
	final Map<String, Object> setUrl(final HttpServletRequest request) {
		String urlStr = request.getParameter("urlToDb");
		final Map<String, Object> response = new HashMap<String, Object>();
		dbPageService.setUrlToDb(urlStr);
		return response;
	}

	@RequestMapping(value = "getpage.htm")
	public @ResponseBody
	final Map<String, Object> getPage(final HttpServletRequest request) {
		final Map<String, Object> response = new HashMap<String, Object>();
		response.put("page", dbPageService.getPage());
		return response;
	}

	@RequestMapping(value = "getpageByUrl.htm")
	public @ResponseBody
	final Map<String, Object> getPageByUrl(final HttpServletRequest request) {
		String pageUrl = request.getParameter("pageUrl");
		final Map<String, Object> response = new HashMap<String, Object>();
		response.put("page", dbPageService.getPagebyUrl(pageUrl));
		return response;
	}

	@RequestMapping(value = "setresalt.htm")
	public @ResponseBody
	final Map<String, Object> setResponseAndGetNewPage(final HttpServletRequest request) {

		final Map<String, Object> response = new HashMap<String, Object>();
		PageResponse pageResponse = new PageResponse();
		pageResponse.setUrl(request.getParameter("url"));

		try {
			final JSONArray jsonArray = new JSONArray(request.getParameter("urls"));
			final List<String> urlList = new ArrayList<String>();

			int len = jsonArray.length();
			if (jsonArray != null) {
				for (int i = 0; i < len; i++) {
					urlList.add(jsonArray.get(i).toString());
				}
			}

			final String[] urls = new String[urlList.size()];
			int i = 0;
			for (Iterator<String> iterator = urlList.iterator(); iterator.hasNext(); ++i) {
				urls[i] = iterator.next().toString();
			}
			pageResponse.setUrls(urls);
			pageResponse.setUrl(request.getParameter("url"));

		} catch (Exception e) {
			LOGGER.error("JsonParseException", e);
			pageResponse.setUrls(new String[0]);
			pageResponse.setUrl("STOP");
		}

		dbPageService.setResponse(pageResponse);
		response.put("page", dbPageService.getPage());
		return response;
	}

	public DbPageService getDbPageService() {
		return dbPageService;
	}

	@Autowired
	public void setDbPageService(DbPageService dbPageService) {
		this.dbPageService = dbPageService;
	}

}
