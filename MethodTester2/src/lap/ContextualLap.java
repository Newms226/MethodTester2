package lap;

import context.Context;

public interface ContextualLap extends Lap {
	
	Context getContext();
	
	void setContext(Context context);
	
	boolean isContextSet();
}
