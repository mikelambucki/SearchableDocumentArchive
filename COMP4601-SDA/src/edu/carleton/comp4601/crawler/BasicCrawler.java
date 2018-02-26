
package edu.carleton.comp4601.crawler;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jgrapht.graph.Multigraph;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.carleton.comp4601.dao.Database;
import edu.carleton.comp4601.utility.WebGraph;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Yasser Ganjisaffar
 */
public class BasicCrawler extends WebCrawler {

	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*(\\.(css|js|bmp|vcf|ico" + "|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	
	
	private static final Pattern BINARY_EXTENSIONS = Pattern.compile((".*(\\.(jpeg|tiff|gif|png|pdf|doc|docx|xls|xlsx|ppt|pptx))$"));
	
	public Database db;
	public org.bson.Document dbDoc = new org.bson.Document();

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		// Ignore the url if it has an extension that matches our defined set of
		// image extensions.
		
		String href = url.getURL().toLowerCase();
        boolean check = !IMAGE_EXTENSIONS.matcher(href).matches()
				&& href.startsWith("https://sikaman.dyndns.org/courses/4601/handouts/") || href
							.startsWith("https://sikaman.dyndns.org/courses/4601/resources/");
        return check;
		// Only accept the url if it is in the domain and protocol is "http".
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {

		int docid = page.getWebURL().getDocid();
		int parentDocid = page.getWebURL().getParentDocid();
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();
		String anchor = page.getWebURL().getAnchor();

		logger.debug("Docid: {}", docid);
		logger.info("URL: {}", url);
		logger.debug("Domain: '{}'", domain);
		logger.debug("Sub-domain: '{}'", subDomain);
		logger.debug("Path: '{}'", path);
		logger.debug("Parent page: {}", parentUrl);
		logger.debug("Anchor text: {}", anchor);

		Multigraph<Integer, org.jgrapht.graph.DefaultEdge> mg = WebGraph.getInstance().getMultigraph();
		mg.addVertex(Integer.valueOf(docid));
		if (parentUrl != null) {
			mg.addEdge(Integer.valueOf(docid), Integer.valueOf(parentDocid));
		} else {
			mg.addEdge(Integer.valueOf(docid), Integer.valueOf(0));
		}

		WebGraph.getInstance().print();

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> urls = htmlParseData.getOutgoingUrls();
			ArrayList<String> tags = new ArrayList<String>();
			
			
			Document doc = Jsoup.parse(html);
			String imgSelector = "img[src~=(?i)\\.(png|jpe?g|gif)]";
			Elements imgs = doc.select(imgSelector);
			for (Element e : imgs) {
				//String src = e.attr("src");
				String alt = e.attr("alt");
				//images.add(src);
				tags.add(alt);
			}
			
			String title = doc.title();
			ArrayList<String> links = new ArrayList<>();
			for (Element e : doc.select("a[href]")) {
				// System.out.println("link:"+e.toString());
				String link = e.attr("abs:href");

				if (link != null && !link.isEmpty()) {
					links.add(link);
				}
			}

			db.insertDocument(docid, title, doc.text(),tags, links);
			


			logger.debug("Text length: {}", text.length());
			logger.debug("Html length: {}", html.length());
			logger.debug("Number of outgoing links: {}", urls.size());
		}
		
		if(BINARY_EXTENSIONS.matcher(url).matches()){
			
		}

		Header[] responseHeaders = page.getFetchResponseHeaders();
		if (responseHeaders != null) {
			logger.debug("Response headers:");
			for (Header header : responseHeaders) {
				logger.debug("\t{}: {}", header.getName(), header.getValue());
			}
		}

	}
}
