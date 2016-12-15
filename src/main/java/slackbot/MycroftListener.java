package slackbot;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import lombok.NonNull;

@Component
public class MycroftListener implements SlackMessagePostedListener {

	private final Logger log = LoggerFactory.getLogger(MycroftListener.class);

	@NonNull
	private final List<MycroftUtterances> utterances = new ArrayList<>();

	@Autowired
	SlackService slackService;

	WebSocketClient wsconn;

	private SlackPersona bot;

	private static final long waitSec = 2;

	public static final int WAIT_TIMEOUT = 3;

	public MycroftListener() throws URISyntaxException {
		super();
		URI uri = new URI("ws://192.168.0.177:8000/events/ws");
		wsconn = new WebSocketClient(uri) {
			@Override
			public void onOpen(ServerHandshake serverHandshake) {
				System.out.println("Websocket Opened");
			}

			@Override
			public void onMessage(String s) {
				// data = new MessageParser(s, new
				// SafeCallback<MycroftUtterances>());
				System.out.println("---------------------------------------------------->>>>>>  " + s);
			}

			@Override
			public void onClose(int i, String s, boolean b) {
				System.out.println("Websocket Closed " + s);

			}

			@Override
			public void onError(Exception e) {
				System.out.println("Websocket Error " + e.getMessage());
			}
		};
		wsconn.connect();

		// try {
		// wsconn = new WebSocketSynchronousConnection();
		// wsconn.start("ws://192.168.0.177:8000/events/ws");
		// } catch (InterruptedException e) {
		// log.error("Failed to initialise websocket");
		// }
	}

	private void addData(MycroftUtterances mu) {
		utterances.add(mu);
	}

	@PostConstruct
	private void setBotName() {
		bot = slackService.getBot();
	}

	@Override
	public void onEvent(SlackMessagePosted event, SlackSession session) {
		String message = event.getMessageContent();
		TextMessage msg = new TextMessage(message);
		log.debug("Message Posted: '{}'", event.getMessageContent());

	}
}
