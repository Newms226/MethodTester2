package context;

import java.nio.file.Path;
import java.time.Instant;

public interface Context<T> {
	
	T getContext();
	
	T getSerializedContext(Path serialization);
	
	Instant getTimeStampInstant();
}
