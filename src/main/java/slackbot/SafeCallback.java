package slackbot;

import lombok.NonNull;

public interface SafeCallback<T> {

	void call(@NonNull T param);
}
