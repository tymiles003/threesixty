package za.co.yellowfire.threesixty.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import za.co.yellowfire.threesixty.MainUI;

public class DashboardEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus;

    public DashboardEventBus() {
    	this.eventBus = new EventBus(this);
    }
    
    public static void post(final Object event) {
    	MainUI.getDashboardEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
    	MainUI.getDashboardEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
    	MainUI.getDashboardEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(
    		final Throwable exception,
            final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}