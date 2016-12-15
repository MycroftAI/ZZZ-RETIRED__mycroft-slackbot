package slackbot;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

@Configuration
public class SlackConfig {

	private final Logger log = LoggerFactory.getLogger(SlackConfig.class);

	@Autowired
	SlackProperties slackProperties;

	private SlackSession slackSession;

	@Bean
	SlackSession slackSession() throws IOException {
		if (null == slackSession) {
			slackSession = SlackSessionFactory.createWebSocketSlackSession(slackProperties.key);
			slackSession.connect();
			log.debug("New session created: {}", slackSession);
			slackSession.addMessagePostedListener(mycroftListener());
		}
		return slackSession;
	}

	@Bean
	SlackChannel slackChannel() throws IOException {
		return slackSession.findChannelByName(slackProperties.channel);
	}

	@Bean
	MycroftListener mycroftListener() {
		try {
			return new MycroftListener();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}