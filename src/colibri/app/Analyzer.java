package colibri.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.xml.sax.SAXException;

import backend.AttributesSet;
import colibri.io.relation.RelationReaderCON;
import colibri.io.relation.RelationReaderXML;
import colibri.lib.Concept;
import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.Relation;
import colibri.lib.Traversal;
import colibri.lib.TreeRelation;

/**
 * Imports a binary relation from a .con or .xml file and outputs the edges of
 * the corresponding lattice or the edges returned by the violation iterator.
 * 
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class Analyzer {

	public static void main(String[] args) {
		Analyzer analyzer = new Analyzer();
		// Call to function that builds the baseline attribute data
//		analyzer.buildBaseLine();
		// attributes.add("school");
		// attributes.add("morning");
		// attributes.add("office");
		// attributes.add("evening");
		// attributes.add("night");
		Map<Integer, String> events = analyzer.determineDayEvents();
		for (Integer key : events.keySet()) {
			//		if (deviationList.size() >= 1) {
			//			sendEmail(reports.toString());
			//		}
			System.out.println("Key: " + key + "Value: " + events.get(key));
		}
	}

	public static void sendEmail(String report) {
		String to = "depressiondoctor@gmail.com";

		// Sender's email ID needs to be mentioned
		String from = "depresseduser@gmail.com";
		final String username = "depresseduser";// change accordingly
		final String password = "depressiondetector";// change accordingly

		// Assuming you are sending email through relay.jangosmtp.net
		String host = "relay.jangosmtp.net";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "25");

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("Symptoms of depression detected for Mr.Depressed User");

			// Now set the actual message
			message.setText(report);

			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String compare(ArrayList<String> attributes, Integer hourOfDay) throws ClassNotFoundException {
		ArrayList<String> deviation = new ArrayList<String>();
		try {
			FileInputStream fileIn = new FileInputStream("data/baseline.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			@SuppressWarnings("unchecked")
			Map<Integer, ArrayList<String>> map = (HashMap<Integer, ArrayList<String>>) in.readObject();
			ArrayList<String> attributesOfBaseline = map.get(hourOfDay);

			attributes.removeAll(attributesOfBaseline);
			deviation.addAll(attributes);
			if (deviation == null)

				in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return null;
		}

		return deviation.toString();

	}

	public static Map<Integer, String> determineDayEvents(){
		Map<Integer, String> dayEvents = new HashMap<>();
		List<String> deviationList = new ArrayList<>();
		Map<Integer,AttributesSet> map = new HashMap<Integer, AttributesSet>();
		AttributesSet attr = new AttributesSet();
		attr.setActivity("still");
		attr.setLocationType("Education");
		attr.setPartOfDay("night");
		attr.setHourOfDay(15);
		map.put(15, attr);
		List<String> reports = new ArrayList<>();

		for (Entry<Integer, AttributesSet> entry : map.entrySet()) {

			Integer hourOfDay = entry.getKey();
			AttributesSet attrs = entry.getValue();
			ArrayList<String> attributes = new ArrayList<>();
			attributes.add(attrs.getActivity());
			attributes.add(attrs.getLocationType());
			attributes.add(attrs.getPartOfDay());
			//			attributes.add(attrs.getHourOfDay().toString());
			//			attributes.add(attrs.getCallCount().toString());




			// Add event for each hour in events list

			if (findEvents(attributes) != null)
				dayEvents.put(attrs.getHourOfDay(), findEvents(attributes));
			else {

				// if events is null add a flag to indicate a deviation from the
				// normal pattern

				if (!dayEvents.containsValue("Deviation")) {
					dayEvents.put(attrs.getHourOfDay(), "Deviation");

					// To check if person is deviating from the norm
					// consistently over a given period of time
				} else {
					dayEvents.put(attrs.getHourOfDay(), "Deviation");
				}

				// Generate a comparison report based on the difference from
				// current deviation and baseline
				// hourOfDay is used to identify the hour of day where the
				// deviation occured


			}
		}
		return dayEvents;

	}

	public static void buildBaseLine() {

		Map<Integer, String> map = determineDayEvents();
		AttributesSet attr = new AttributesSet();
		attr.setActivity("still");
		attr.setLocationType("Education");
		attr.setPartOfDay("afternoon");
		ArrayList<String> attributes = new ArrayList<>();
		attributes.add(attr.getActivity());
		attributes.add(attr.getLocationType());
		attributes.add(attr.getPartOfDay());
		
		Map<String, ArrayList<String>> eventAttr = new HashMap<String, ArrayList<String>>();
		eventAttr.put(findEvents(attributes), attributes);
		
		Map<Integer, Map<String, AttributesSet>> baseline = new HashMap<Integer, Map<String, AttributesSet>>();
		if (map.)
		baseline.put(key, value)
		
		//attr.setHourOfDay(15);
		//map.put(15, attr);
		try {
			FileOutputStream fileOut = new FileOutputStream("data/baseline.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(map);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in data/baseline.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	/**
	 * Function to find events for a given set of attributes
	 * 
	 * @param attributes
	 * @return Set of unique events
	 */
	private static String findEvents(ArrayList<String> attributes) {
		String inputFormat = null;
		String inputFile = null;
		String outputFormat = null;

		//		System.out.println(attributes);

		inputFormat = System.getProperty("input_format", "con");
		inputFile = System.getProperty("input_file", "data/concepts.con");

		if (outputFormat == null) {
			outputFormat = "xml";
		}

		Relation relation;
		relation = new TreeRelation();

		if (inputFormat.equals("xml")) {
			try {
				RelationReaderXML xmlReader = new RelationReaderXML();
				xmlReader.read(inputFile, relation);
			} catch (SAXException e) {
				System.err.println("Reading xml-file failed.");
				e.printStackTrace();
				// return;
			} catch (IOException e) {
				System.err.println("Reading xml-file failed.");
				e.printStackTrace();
				// return;
			}
		} else if (inputFormat.equals("con")) {
			try {
				RelationReaderCON conReader = new RelationReaderCON();
				conReader.read(inputFile, relation);
			} catch (IOException e) {
				System.err.println("Reading con-file failed.");
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException();
		}

		Lattice lattice = new HybridLattice(relation);

		//		System.out.println(relation.getSizeAttributes() + " attributes");
		//		System.out.println(relation.getSizeObjects() + " objects");

		Iterator<Concept> it = lattice.conceptIterator(Traversal.TOP_ATTRSIZE);

		while (it.hasNext()) {
			Concept c = it.next();
			// If the attributes are found in the current events then store it
			if (checkIfContains(attributes, c) == true && !c.getObjects().isEmpty()) {
				Iterator<Comparable> event_iterator = c.getObjects().iterator();
				String event = event_iterator.next().toString();
				return event;
			}
		}
		return null;
	}

	/**
	 * Function to check if the given attributes are present in a concept
	 * attribute list
	 * 
	 * @param attributes
	 * @param c
	 *            - concept
	 * @return
	 */
	private static boolean checkIfContains(ArrayList<String> attributes, Concept c) {
		boolean found = false;
		for (String attribute : attributes) {
			try{
				if (c.getAttributes().contains(attribute)) {
					found = true;
				} else {
					found = false;
					break;
				}
			}catch(NullPointerException e){
				return found;
			}
		}
		return found;

	}

}
