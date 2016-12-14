package slackbot;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

@Component
public class MycroftListener implements SlackMessagePostedListener {

	private final Logger log = LoggerFactory.getLogger(MycroftListener.class);

	@Autowired
	SlackService slackService;

	WebSocketSynchronousConnection wsconn;

	private SlackPersona bot;

	private static final long waitSec = 2;

	public static final int WAIT_TIMEOUT = 3;

	public MycroftListener() {
		super();
		try {
			wsconn = new WebSocketSynchronousConnection();
			wsconn.start("ws://192.168.0.177:8000/events/ws");
		} catch (InterruptedException e) {
			log.error("Failed to initialise websocket");
		}
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
		// String sender = event.getSender().getUserName();
		try {
			TextMessage res = wsconn.sendMessage(msg, waitSec);
			System.out.println(res.getPayload());
			TextMessage resf = wsconn.pollMessage(waitSec);
			System.out.println(resf.getPayload());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
