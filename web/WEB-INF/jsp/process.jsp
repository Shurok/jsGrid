<html>
<head>
<title>Grid computing</title>
<script type="text/javascript" src="/jsGrid/js/jq.js"></script>


<script>
	logiInfo = true;
	if (typeof String.prototype.startsWith != 'function') {
		String.prototype.startsWith = function(str) {
			return this.indexOf(str) == 0;
		};
	}

	function setUrl(url) {
		$.post("setUrl.htm", {
			"urlToDb" : url
		}, function(data) {
			alert("seted");
		});
	}

	function getPageUrlTest(pageUrl) {
		callTime = new Date().getTime();
		$.post("getpageByUrl.htm", {
			"pageUrl" : pageUrl
		}, function(data) {
			processHTML(data.page);
		});
	}

	function startNode() {
		callTime = new Date().getTime();
		$.post("getpage.htm", {}, function(data) {
			processHTMLAndSendResponse(data.page);
		});
	}

	function processHTML(page) {
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
			response = new Object();
			response.url = page.url;
			$("#console")[0].value += "\n\n Url " + page.url;
			$("#console")[0].value += "\n Url domain " +page.url.match(/.*:\/\/.*?\//i)[0];
			urls = getURLs(page.url.match(/.*:\/\/.*?\//i)[0]);
			response.urls = JSON.stringify(urls);
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
				$("textarea")[0].scrollByPages(1);
			}
			$('#bufHtml')[0].innerHTML = "";
			callTime = new Date().getTime();
			return response;
	}
	
	function processHTMLAndSendResponse(page) {
			processHTML(page);
			setRespose(response);
	}
	
	function setRespose(response){
		$.post("setresult.htm", response, function(data) {
			processHTMLAndSendResponse(data.page);
		});
	}

	function getURLs(parent) {
		var locationUrl = location.href.replace("process.htm", "");
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
		unicalUrls = jQuery.unique(hrefList);
		onlyNewUrls = unicalUrls.diff(oldUrls);
		oldUrls = unicalUrls;
		$("#console")[0].value +=("\n unicalUrls " +unicalUrls.length +"onlyNewUrls "+onlyNewUrls.length+"oldUrls "+ oldUrls.length)
		return onlyNewUrls;
	}
	var oldUrls = new Array();

	Array.prototype.diff = function(a) {
	    return this.filter(function(i) {return !(a.indexOf(i) > -1);});
	};
	
	
	
	function link(url){
		this.url = url;
		this.frequency = 1;
		this.increaseFreguency = function(){
			this.frequency +=1; 
		}
	}
	var oftenUrls = new Array(1000); 

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
    <input type="text" id="pageUrl" value="http://files2.zimbra.com/downloads/zdesktop/beta/10514/zdesktop_2_0_beta4_b10514_win32.msi" />
    <input type="button" id="pageUrlBtn" onclick="getPageUrlTest($('#pageUrl')[0].value); return false;" value="Test page Url parcing" />
    <br />
    
    <b onclick="setUrl('http://bigmir.net'); return false;">set bigmir to db</b>
    <br />
    <input id="url" type="text"></input>
    <textarea id="console" style="width: 735px; height: 311px;"></textarea>
    <div id="bufHtml"></div>
</body>
</html>