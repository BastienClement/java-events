/*
	Copyright (c) 2013 Bastien Clément

	Permission is hereby granted, free of charge, to any person obtaining a
	copy of this software and associated documentation files (the
	"Software"), to deal in the Software without restriction, including
	without limitation the rights to use, copy, modify, merge, publish,
	distribute, sublicense, and/or sell copies of the Software, and to
	permit persons to whom the Software is furnished to do so, subject to
	the following conditions:

	The above copyright notice and this permission notice shall be included
	in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
	OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
	IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
	CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
	TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
	SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package galedric.events;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An event.
 * 
 * An event can be emitted by an EventEmitter and received by an EventListener.
 * This event class doesn't have any special meaning and should be extended.
 */
@SuppressWarnings("serial")
public class Event implements Cloneable, Serializable {
	/**
	 * This event's emitter.
	 */
	transient private EventEmitterInterface emitter;

	/**
	 * Returns the event's emitter
	 */
	public EventEmitterInterface getEmitter() {
		return emitter;
	}

	/**
	 * Handles event distribution to a listener.
	 * 
	 * @param listener
	 *             The EventListener.
	 * 
	 * @throws UnhandledEventException
	 *             If this event cannot be handled by the given listener
	 * @throws InvocationTargetException
	 *             If an exception was thrown during the event processing.
	 */
	public final void trigger(EventListener listener) throws UnhandledEventException, InvocationTargetException {
		trigger(listener, null);
	}

	/**
	 * Handles event distribution to a listener with a given emitter value.
	 * 
	 * @param listener
	 *             The EventListener.
	 * @param emitter
	 *             The emitter value to use for this event.
	 * 
	 * @throws UnhandledEventException
	 *             If this event cannot be handled by the given listener
	 * @throws InvocationTargetException
	 *             If an exception was thrown during the event processing.
	 */
	public final void trigger(EventListener listener, EventEmitterInterface emitter) throws UnhandledEventException, InvocationTargetException {
		// Event itself is not modified
		Event event = (Event) this.clone();

		// Set the emitter
		event.emitter = emitter;

		// --------------------------------------
		// In memoriam of Generics-powered events
		// --------------------------------------

		// The class of the event
		Class<?> eventClass = event.getClass();

		while(eventClass != null) {
			try {
				// Try to get a handler for the exact class of this event
				Method on = listener.getClass().getMethod("on", eventClass);

				// Ensure accessibility
				on.setAccessible(true); // Inner-class are otherwise unavailable

				// Invoke!
				on.invoke(listener, event);

				// Done
				return;
			}
			catch(InvocationTargetException e) {
				// Invocation throwed an exception, rethrow it
				throw e;
			}
			catch(Exception e) {
				// Exception when getting the handler, handler is probably undefined

				if(eventClass == Event.class) {
					// Event is the super-class of all events
					break;
				}

				// Try!
				eventClass = eventClass.getSuperclass();
			}
		}

		// Event has not be catched
		throw new UnhandledEventException();
	}

	public Object clone() {
		try {
			return super.clone();
		}
		catch(CloneNotSupportedException e) {
			// Should not happen
			return this;
		}
	}
}
