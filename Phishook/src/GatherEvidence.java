import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.whois.WhoisClient;

public class GatherEvidence extends Frame implements ActionListener, WindowListener {
	// This is where I will put the beginner gui...when I get some free time
	private Label lblCount; // Declare component Label
	private TextField tfCount; // Declare component TextField
	private Button btnCount; // Declare component Button
	private int count = 0; // Counter's value
	private static Pattern pattern;
	private Matcher matcher;
	
	// regex whois parser
	private static final String WHOIS_SERVER_PATTERN= "Whois Server:\\s(.*)";
	static {
		pattern = Pattern.compile(WHOIS_SERVER_PATTERN);
	}

	/** Constructor to setup GUI components and event handling */
	public GatherEvidence() {
		setLayout(new FlowLayout());
		// "super" Frame sets its layout to FlowLayout, which arranges the
		// components
		// from left-to-right, and flow to next row from top-to-bottom.

		lblCount = new Label("URL:"); // construct Label
		add(lblCount); // "super" Frame adds Label

		tfCount = new TextField("Put url here!"); // construct TextField
		tfCount.setEditable(true); // set to read-only
		add(tfCount); // "super" Frame adds tfCount

		btnCount = new Button("Gather Evidence"); // construct Button
		add(btnCount); // "super" Frame adds Button

		btnCount.addActionListener(this);
		// Clicking Button source fires ActionEvent
		// btnCount registers this instance as ActionEvent listener

		setTitle("PhishHook"); // "super" Frame sets title
		setSize(250, 100); // "super" Frame sets initial window size
		
		addWindowListener(this);

		setVisible(true); // "super" Frame shows

	}	
	
	/** The entry main() method */
	public static void main(String[] args) {
		// Invoke the constructor to setup the GUI, by allocating an instance
		GatherEvidence app = new GatherEvidence();
	}

	/** ActionEvent handler - Called back upon button-click. */
	public void actionPerformed(ActionEvent evt) {
		// retrieve the whois information of the URL entered
		System.out.println(this.getWhois(this.tfCount.getText()));
		//tfCount.setText(count + ""); // convert int to String 
	}
	
	public String getWhois(String domainName) {
		StringBuilder result = new StringBuilder("");
		WhoisClient whois = new WhoisClient();
		try {
			 
			// default is internic.net
			whois.connect(WhoisClient.DEFAULT_HOST);
			
			// whois =domainName
			String whoisData1 = whois.query("=" + domainName);
			
			// append first result
			result.append(whoisData1);
			whois.disconnect();
			
			//get the domain whois server
			String whoisServerURL = getWhoisServer(whoisData1);
			if (!whoisServerURL.equals("")) {
				// whois -h whois.server.com domain
				String whoisData2 = queryWithWhoisServer(domainName, whoisServerURL);
				
				// append 2nd result
				result.append(whoisData2);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	/* This will query with the whois server*/
	private String queryWithWhoisServer(String domainName, String whoisServer) {
		String result = "";
		WhoisClient whois = new WhoisClient();
		try {
			whois.connect(whoisServer);
			result = whois.query(domainName);
			whois.disconnect();
		} catch (SocketException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/* This method will retrieve the whois server */
	private String getWhoisServer(String whois) {
		String result = "";
		matcher = pattern.matcher(whois);
		//get last whois server
		while (matcher.find()) {
			result = matcher.group(1);
		}
		return result;
	}
	
	/* This method is for closing the gui */
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
