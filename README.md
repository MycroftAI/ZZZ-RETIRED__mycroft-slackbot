# mycroft-slackbot

## Non-Dockerised version ##

* Go to https://api.slack.com/bot-users and set up a new bot user for your slack
* Make a note of the auth token/key. You will need this in the next step
* Add the token/key into the application.yml file in src/main/resources
* Give your bot an appropriate name and channel to hang out in. The bot name in config should match the name you gave it in 1.
* Add the URL in the form of 192.168.0.177 (for example) to the application.yml file as the mycrofturi
* Go back to the root of the project (where the pom.xml file is) and compile the jar with:
    `mvn clean install package`
* Execute the Jar (it is a fat jar with embedded Tomcat) with `java -jar target/mycroft-slackbot-0.0.1-SNAPSHOT.jar`
* Maybe execute in a tmux/screen session, or else make it a service
* Go to your slack channel and send an @ message to your new bot, asking it any of the Mycroft things. (Make sure you have paired your device as per the Mycroft-core instructions)

------

## Docker Setup ##

* Do steps 1, 2, 3, 4 above
* Go to the root of the project and execute `mvn clean install package docker:build`
* Run the docker image with `docker run -itd -p 9119:9119 mycroftai/slackbot`
* Go to your slack channel and send an @ message to your new bot, asking it any of the Mycroft things. (Make sure you have paired your device as per the Mycroft-core instructions)
