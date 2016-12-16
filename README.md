# mycroft-slackbot

To set up:

1. Go to https://api.slack.com/bot-users and set up a new bot user for your slack
2. Make a note of the auth token/key. You will need this in the next step
3. Add the token/key into the application.yml file in src/main/resources
4. Give your bot an appropriate name and channel to hang out in. The bot name in config should match the name you gave it in 1.
5. Edit MycroftListener.java and change the line uri = new URI("ws://192.168.0.177:8000/events/ws"); to reflect your Mycroft instance.
5.1 Yes, I am making this a config option.
6. Go back to the root of the project (where the pom.xml file is) and compile the jar with:
    ```mvn clean install package```
7. Execute the Jar (it is a fat jar with embedded Tomcat) with java -jar target/mycroft-slackbot-0.0.1-SNAPSHOT.jar
7.1 Maybe execute in a tmux/screen session, or else make it a service
8. Go to your slack channel and send an @ message to your new bot, asking it any of the Mycroft things. (Make sure you have paired your device as per the Mycroft-core instructions)
