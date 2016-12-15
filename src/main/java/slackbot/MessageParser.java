package slackbot;

import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.NonNull;

class MessageParser implements Runnable {

	@NonNull
	private final String message;
	@NonNull
	private final SafeCallback<MycroftUtterances> callback;

	/**
	 * MessageParser is a simple mechanism for parsing a {@link JSONObject} out
	 * of a String in a way conducive to scheduling.
	 *
	 * @param message
	 *            any String. Must not be null
	 * @param callback
	 *            will be referenced if a {@link MycroftUtterances}'
	 *            {@link MycroftUtterances#utterance} is found within the
	 *            message. May not be null
	 */
	public MessageParser(@NonNull String message, @NonNull SafeCallback<MycroftUtterances> callback) {
		this.message = message;
		this.callback = callback;
	}

	@Override
	public void run() {
		try {
			JSONObject obj = new JSONObject(message);
			String msgType = obj.optString("message_type");
			if (Objects.equals(msgType, "speak")) {
				String ret = obj.getJSONObject("metadata").getString("utterance");
				MycroftUtterances mu = new MycroftUtterances();
				mu.utterance = ret;
				callback.call(mu);
			}

		} catch (JSONException e) {
			System.out.println("The response received did not conform to our expected JSON formats." + e);
		}

	}
}