<html>
<head>
<title>Grid computing</title>
<script type="text/javascript" src="/jsGrid/dwr/engine.js"></script>
<script type="text/javascript" src="/jsGrid/dwr/util.js"></script>
<script type="text/javascript" src="/jsGrid/dwr/interface/dwrService.js"></script>
<script type="text/javascript" src="/jsGrid/js/jq.js"></script>


<script>
	logiInfo = true;

	function setUrl(url) {
		$.post("setUrl.htm", {
			"urlToDb" : url
		}, function(data) {
			alert("seted");
		});
	}

	function startDb() {
		$.post("startDb.htm", {}, function(data) {
			alert("started");
		});
	}

	function startNode() {
		callTime = new Date().getTime();
		$.post("getpage.htm", {}, function(data) {
			processHTML(data.page);
		});
	}

	function processHTML(page) {
		if (page.url != "STOP") {
			startProcessingTime = new Date().getTime();
			$('#bufHtml')[0].innerHTML = "";
			page.source = page.source.replace(/<head>.*<\/head>/i, "");
			page.source = page.source.replace(/<link.*?>/g, "");
			page.source = page.source.replace(/<script.*?\/script>/gi, "");
			page.source = page.source.replace(/src=.*?"/gi, "src=''");
			page.source = page.source.replace(/src=.*?'/gi, "src=''");
			page.source = page.source.replace(/style=.*?'/gi, "src=''");
			page.source = page.source.replace(/style=.*?"/gi, "src=''");
			page.source = page.source.replace(/href="javascript:.*?"/gi, "");
			page.source = page.source.replace(/href='javascript:.*?'/gi, "");
			page.source = page.source.replace(/href="skype:.*?"/gi, "");
			page.source = page.source.replace(/href='skype:.*?'/gi, "");
			$('#bufHtml')[0].innerHTML = page.source;
			$('#url')[0].value = (page.url);
			var response = new Object();
			response.url = page.url;
			var urls = getURLs(page.url.match(/.*:\/\/.*?\//i)[0]);
			response.urls = JSON.stringify(urls);

			//get Content worlds
			//return results 
			if (logiInfo) {
				currentTime = new Date();
				var processingTime = currentTime.getTime()
						- startProcessingTime;
				var totalTime = currentTime.getTime() - callTime;
				$("#console")[0].value += "\n\n " + currentTime;
				$("#console")[0].value += "\n Request Processed " + page.url;
				$("#console")[0].value += "  " + urls.length
						+ " unical links found.";
				$("#console")[0].value += " Time for processing = "
						+ processingTime;
				$("#console")[0].value += "\n Total time = " + totalTime;
			//	$("textarea")[0].scrollByPages(1);
			}
			$('#bufHtml')[0].innerHTML = "";
			callTime = new Date().getTime();
			$.post("setresalt.htm", response, function(data) {
				processHTML(data.page);
			});
		}
	}

	if (typeof String.prototype.startsWith != 'function') {
		String.prototype.startsWith = function(str) {
			return this.indexOf(str) == 0;
		};
	}

	function getURLs(parent) {
		var locationUrl = location.href.replace("index.html", "");
		var locationDomain = location.origin;
		var aElements = $("a");
		var len = aElements.length;
		var hrefList = new Array(len);
		for ( var i = 0; i < len; ++i) {
			hrefList[i] = aElements[i].href.replace(/#.*/, "");
			if (hrefList[i].startsWith(locationUrl)) {
				hrefList[i] = hrefList[i].replace(locationUrl, parent);
			} else if (hrefList[i].startsWith(locationDomain)) {
				hrefList[i] = hrefList[i].replace(locationDomain + "/", parent);
			}

		}
		return jQuery.unique(hrefList);
		
	}
	function getURLsTest(parent) {
		var locationUrl = location.href.replace("index.html", "");
		var locationDomain = location.origin;
		var aElements = $("a");
		var len = aElements.length;
		hrefList = new Array(len);
		for ( var i = 0; i < len; ++i) {
			hrefList[i] = aElements[i].href.replace(/#.*/, "");
			if (hrefList[i].startsWith(locationUrl)) {
				hrefList[i] = hrefList[i].replace(locationUrl, parent);
			} else if (hrefList[i].startsWith(locationDomain)) {
				hrefList[i] = hrefList[i].replace(locationDomain + "/", parent);
			}

		}
		return jQuery.unique(hrefList);
		
	}
</script>
</head>
<body>
    <b onclick="startNode(); return false;">Process Page</b>
    <br />

    <b onclick="startDb(); return false;">Start Db</b>
    <br />

    <input type="text" id="setUrl" value="http://vk.com/id1" />
    <input type="button" id="setUrlBtn" onclick="setUrl($('#setUrl')[0].value); return false;" value="Set Url" />
    <br />
    <b onclick="setUrl('http://bigmir.net'); return false;">set bigmir to db</b>
    <br />
    <input id="url" type="text"></input>
    <textarea id="console" style="width: 735px; height: 311px;"></textarea>
    <div id="bufHtml"></div>
</body>
</html>