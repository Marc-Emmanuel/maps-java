package maps.directions;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MapsDirectionsParser {
	public static final String MODE_DRIVING = "driving";
	public static final String MODE_WALKING = "walking";
	// Normally it's a pipe, but not handle for character coding, use %C7
	// instead
	private static final String SEPARATOR = "%7C";
	private String requestUrl;

	public MapsDirectionsParser() {
		// TODO Auto-generated constructor stub
	}

	// Take care of query limits on directions. Max 8 waypoints. Including
	// departure and arrival.
	// Previous method consisted in get directions betwwen all consecutive
	// points, but query limit was reached soon too.
	// ArrayList<doube,double>
	// lat,lng
	public Document getDocument(ArrayList<ArrayList<Double>> wayPoints,
			String mode) {
		String url = "http://maps.googleapis.com/maps/api/directions/xml?"
				+ "origin=" + wayPoints.get(0).get(0) + ","
				+ wayPoints.get(0).get(1) + "&destination="
				+ wayPoints.get(wayPoints.size() - 1).get(0) + ","
				+ wayPoints.get(wayPoints.size() - 1).get(1) + "&waypoints=";
		for (int i = 1; i < wayPoints.size() - 1; i++) {
			if (i == (wayPoints.size() - 2)) {
				url += wayPoints.get(i).get(0) + "," + wayPoints.get(i).get(1);
			} else {
				url += wayPoints.get(i).get(0) + "," + wayPoints.get(i).get(1)
						+ SEPARATOR;
			}
		}
		url += "&sensor=false&units=metric&mode=" + mode + "&language=fr";
		requestUrl = url;

		try {
			OutputStreamWriter writer = null;
			String data = "";
			// création de la connection
			URL urlConnection = new URL(url);
			URLConnection conn = urlConnection.openConnection();
			conn.setDoOutput(true);

			// envoi de la requête
			writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();

			// lecture de la réponse
			InputStream in = conn.getInputStream();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(in);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Document getDocument(String departureAddress, String arrivalAddress,
			String mode) {
		String url = "http://maps.googleapis.com/maps/api/directions/xml?"
				+ "origin=" + departureAddress + "&destination="
				+ arrivalAddress + "&sensor=false&units=metric&mode=" + mode
				+ "&language=fr";
		requestUrl = url;
		try {
			OutputStreamWriter writer = null;
			// encodage des paramètres de la requête
			String data = "";
			// création de la connection
			URL urlConnection = new URL(url);
			URLConnection conn = urlConnection.openConnection();
			conn.setDoOutput(true);

			// envoi de la requête
			writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();

			// lecture de la réponse

			InputStream in = conn.getInputStream();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(in);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// List a cause de la signature de la methode prennant des arraylist
	// d'arraylist de double, le runtime crie
	public Document getDocument(List<String> wayPoints, String mode) {

		String url = "http://maps.googleapis.com/maps/api/directions/xml?"
				+ "origin=" + wayPoints.get(0) + "&destination="
				+ wayPoints.get(wayPoints.size() - 1) + "&waypoints=";
		for (int i = 1; i < wayPoints.size() - 1; i++) {
			if (i == (wayPoints.size() - 2)) {
				url += wayPoints.get(i);
			} else {
				url += wayPoints.get(i) + SEPARATOR;
			}
		}
		url += "&sensor=false&units=metric&mode=" + mode + "&language=fr";
		requestUrl = url;
		try {
			OutputStreamWriter writer = null;
			String data = "";
			// création de la connection
			URL urlConnection = new URL(url);
			URLConnection conn = urlConnection.openConnection();
			conn.setDoOutput(true);

			// envoi de la requête
			writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();

			// lecture de la réponse
			InputStream in = conn.getInputStream();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(in);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getHtmlInstruction(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("html_instructions");
		String result = "";
		for (int i = 0; i < nl1.getLength(); i++) {
			result += nl1.item(i).getTextContent() + "\n";
		}
		return result;
	}

	public String getDurationText(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("duration");
		Node node1 = nl1.item(nl1.getLength()-1);
		NodeList nl2 = node1.getChildNodes();
		Node node2 = nl2.item(getNodeIndex(nl2, "text"));
		return node2.getTextContent();
	}

	public int getDurationValue(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("duration");
		Node node1 = nl1.item(0);
		NodeList nl2 = node1.getChildNodes();
		Node node2 = nl2.item(getNodeIndex(nl2, "value"));
		return Integer.parseInt(node2.getTextContent());
	}

	public String getStatusText(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("status");
		Node node1 = nl1.item(0);
		return node1.getTextContent();
	}

	public String getDistanceText(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("distance");
		String totalDistance = "";
		for (int j = 0; j < nl1.getLength(); j++) {
			Node node1 = nl1.item(j);
			NodeList nl2 = node1.getChildNodes();
			totalDistance += (nl2.item(
					getNodeIndex(nl2, "text")).getTextContent())+"\n";
		}
		return totalDistance;
	}

	public int getDistanceValue(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("distance");
		int totalDistance = 0;
		for (int j = 0; j < nl1.getLength(); j++) {
			Node node1 = nl1.item(j);
			NodeList nl2 = node1.getChildNodes();
			totalDistance += Integer.parseInt(nl2.item(
					getNodeIndex(nl2, "value")).getTextContent());
		}
		return totalDistance;
	}

	public String getStartAddress(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("start_address");
		Node node1 = nl1.item(0);
		return node1.getTextContent();
	}

	public String getEndAddress(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("end_address");
		Node node1 = nl1.item(0);
		return node1.getTextContent();
	}

	public String getCopyRights(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("copyrights");
		Node node1 = nl1.item(0);
		return node1.getTextContent();
	}

	public ArrayList<ArrayList<Double>> getDirection(Document doc) {
		NodeList nl1, nl2, nl3;
		ArrayList<ArrayList<Double>> listGeopoints = new ArrayList<ArrayList<Double>>();
		nl1 = doc.getElementsByTagName("step");
		if (nl1.getLength() > 0) {
			for (int i = 0; i < nl1.getLength(); i++) {
				ArrayList<Double> tempGeoPoint = new ArrayList<Double>();
				Node node1 = nl1.item(i);
				nl2 = node1.getChildNodes();

				Node locationNode = nl2
						.item(getNodeIndex(nl2, "start_location"));
				nl3 = locationNode.getChildNodes();
				Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
				double lat = Double.parseDouble(latNode.getTextContent());
				Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
				double lng = Double.parseDouble(lngNode.getTextContent());
				tempGeoPoint.add(lat);
				tempGeoPoint.add(lng);
				listGeopoints.add(tempGeoPoint);

				locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
				nl3 = locationNode.getChildNodes();
				latNode = nl3.item(getNodeIndex(nl3, "points"));
				ArrayList<ArrayList<Double>> arr = decodePoly(latNode
						.getTextContent());
				for (int j = 0; j < arr.size(); j++) {
					listGeopoints.add(arr.get(j));
				}

				locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
				nl3 = locationNode.getChildNodes();
				latNode = nl3.item(getNodeIndex(nl3, "lat"));
				lat = Double.parseDouble(latNode.getTextContent());
				lngNode = nl3.item(getNodeIndex(nl3, "lng"));
				lng = Double.parseDouble(lngNode.getTextContent());
				ArrayList<Double> foo = new ArrayList<Double>();
				foo.add(lat);
				foo.add(lng);
				listGeopoints.add(foo);
			}
		}

		return listGeopoints;
	}

	private int getNodeIndex(NodeList nl, String nodename) {
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeName().equals(nodename))
				return i;
		}
		return -1;
	}

	@SuppressWarnings("unused")
	private ArrayList<Integer> getAllNodesIndexes(NodeList nl, String nodeName) {

		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeName().equals(nodeName)) {
				System.out.println("NodesIndexes: " + i);
				indexes.add(i);
			}
		}
		return indexes;
	}

	private ArrayList<ArrayList<Double>> decodePoly(String encoded) {
		ArrayList<ArrayList<Double>> poly = new ArrayList<ArrayList<Double>>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			ArrayList<Double> temp = new ArrayList<Double>();
			temp.add(lat / 1E5);
			temp.add(lng / 1E5);
			poly.add(temp);
		}
		return poly;
	}

	/**
	 * @return the requestUrl
	 */
	public String getRequestUrl() {
		return requestUrl;
	}

}
