package slackbot;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

@Component
@Configuration
@EnableConfigurationProperties(MycroftProperties.class)
public class MycroftListener implements SlackMessagePostedListener {

	private final Logger log = LoggerFactory.getLogger(MycroftListener.class);

	private SlackChannel chan;

	@Autowired
	private SlackProperties slackProperties;

	private String mycroftUri;

	@Autowired
	SlackService slackService;

	WebSocketClient wsconn;

	private SlackPersona bot;

	public MycroftListener(String uri) {
		super();
		mycroftUri = uri;
		connectWebSocket();
	}

	@PostConstruct
	private void setBotName() {
		bot = slackService.getBot();
	}

	@Override
	public void onEvent(SlackMessagePosted event, SlackSession session) {
		String message = event.getMessageContent();
		if (message.contains(bot.getId()) && !event.getSender().isBot()) {

			// we need to remove the bot id, as it confuzzles mycroft
			message = message.replace(bot.getId(), "");
			message = message.replace("<@>", "");
			// set the chan
			chan = event.getChannel();
			// now fire off to the mycroft socket
			sendMycroftMessage(message);
		}
	}

	private void connectWebSocket() {
		try {
			URI uri = new URI("ws://" + mycroftUri + ":8000/events/ws");
			wsconn = new WebSocketClient(uri) {

				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					System.out.println("Websocket Opened");
				}

				@Override
				public void onMessage(String s) {
					JSONObject json = new JSONObject(s);
					if (json.get("message_type").equals("speak")) {
						// oooh mycroft has something to say...
						JSONObject meta = json.getJSONObject("metadata");
						String ut = meta.getString("utterance");
						slackService.sendMessage(ut, chan);
					}
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
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}

	public void sendMycroftMessage(String msg) {

		// let's keep it simple eh?
		final String json = "{\"message_type\":\"recognizer_loop:utterance\", \"context\": null, \"metadata\": {\"utterances\": [\""
				+ msg + "\"]}}";
		if (wsconn == null || wsconn.getConnection().isClosed()) {
			// try and reconnect
			connectWebSocket();
		}
		wsconn.send(json);

	}

}