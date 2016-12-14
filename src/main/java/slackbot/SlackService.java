package slackbot;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;

@Service
public class SlackService {

	private static final Logger logger = LoggerFactory.getLogger(SlackService.class);

	@Autowired
	private SlackSession slackSession;

	@Autowired
	private SlackProperties slackProperties;

	/**
	 * Sends a message to the provided channel
	 *
	 * @param myMessage
	 *            your message
	 * @param slackChannel
	 *            channel to send
	 */
	public void sendMessage(String myMessage, SlackChannel slackChannel) {
		slackSession.sendMessage(slackChannel, myMessage, null);
	}

	/**
	 * Returns the bot that matches the property provided in configuration
	 *
	 * @return SlackPersona of bot
	 */
	public SlackPersona getBot() {
		SlackPersona foundBot = null;
		String botName = slackProperties.getName();
		Collection<SlackBot> bots = slackSession.getBots();
		for (SlackBot bot : bots) {
			if (bot.getUserName().equalsIgnoreCase(botName)) {
				foundBot = bot;
			}
		}
		if (null == foundBot) {
			throw new IllegalStateException("Unable to find bot " + botName);
		}
		return foundBot;
	}

	public void sendMessageToOne(String message, SlackUser usr) {
		logger.debug(message);
		logger.debug(usr.getRealName());
	}

}
