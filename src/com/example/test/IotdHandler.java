package com.example.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@SuppressLint("NewApi")
public class IotdHandler extends DefaultHandler {

	private static final String url = "http://www.nasa.gov/rss/image_of_the_day.rss";
	
	private static String ATT_ITEM = "item";
	private static String ATT_TITLE = "title";
	private static String ATT_DESCRIPTION = "description";
	private static String ATT_DATE = "pubDate";
	private static String ATT_ENCLOSURE = "enclosure";
	private static String ATT_URL = "url";
	
	private boolean inTitle = false;
	private boolean inDescription = false;
	private boolean inItem = false;
	private boolean inDate = false;
	
	private Map<String, IotdItem> iotdMap;
	private IotdItem item;
	
	public void processFeed() {
		try {			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();			
			reader.setContentHandler(this);
			InputStream inputStream = new URL(url).openStream();
			InputSource is = new InputSource(inputStream);			
			reader.parse(is);			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private Bitmap getBitmap(String url) {

		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();

			Bitmap bitmap = BitmapFactory.decodeStream(input);
			input.close();
			return bitmap;

		} catch (IOException ioe) {
			return null;
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		iotdMap = new HashMap<String, IotdItem>();
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (localName.startsWith(ATT_ITEM)) {
			item = new IotdItem();
			inItem = true;
		} else if (inItem) {
			
			if (localName.equals(ATT_ENCLOSURE)) {
				item.setImg(getBitmap(attributes.getValue(ATT_URL)));
				return;
			}
			
			if (localName.equals(ATT_TITLE)) {
				inTitle = true;
			} else {
				inTitle = false;
			}
			if (localName.equals(ATT_DESCRIPTION)) {
				inDescription = true;
			} else {
				inDescription = false;
			}
			if (localName.equals(ATT_DATE)) {
				inDate = true;
			} else {
				inDate = false;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if (localName.startsWith(ATT_ITEM)) {
			iotdMap.put(item.getDatum(), item);
		}
	}
	
	public void characters(char ch[], int start, int length) {
		String chars = new String(ch).substring(start, start + length);
		if (inTitle ) {
			item.appendTitle(chars);
		}
		if (inDescription) {
			item.appendDescription(chars);
		}
		if (inDate ) {
			item.appendDatum(chars);
		}
	}

	public Map<String, IotdItem> getMap() {
		return iotdMap;
	}
}
