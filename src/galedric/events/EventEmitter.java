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

import java.util.HashSet;
import java.util.Set;

/**
 * An event emitter.
 * This class handles every bookkeeping related to EventListeners.
 * 
 * This class uses a synchronous event delivery method. The emitter thread is
 * blocked until event processing is completed.
 */
public abstract class EventEmitter implements EventEmitterInterface {
	/**
	 * The set of every EventListeners bounds to this emitter.
	 */
	protected Set<EventListener> listeners = new HashSet<EventListener>();

	/**
	 * The emitter value used for events emitted by this emitter.
	 */
	protected EventEmitterInterface emitter;

	/**
	 * If TRUE, the onException() method will be called for each exception
	 * thrown during the delivery process.
	 */
	protected boolean catchExceptions = false;

	/**
	 * Creates a new EventEmitter.
	 */
	public EventEmitter() {
		this(null);
	}

	/**
	 * Creates a new EventEmitter with a specific emitter value for emitted events.
	 * 
	 * @param emitter
	 *            The emitter value, self if null.
	 */
	public EventEmitter(EventEmitterInterface emitter) {
		this.emitter = emitter != null ? emitter : this;
	}

	/**
	 * Binds a listener to this emitter
	 */
	public void addListener(EventListener listener) {
		if(listener != null) {
			listeners.add(listener);
		}
	}

	/**
	 * Removes a listener from this emitter.
	 */
	public void removeListener(EventListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Emits an event.
	 */
	public void emit(Event event) {
		for(EventListener listener : listeners) {
			try {
				event.trigger(listener, emitter);
			}
			catch(Exception e) {
				if(this.catchExceptions) {
					this.onException(e);
				}
			}
		}
	}

	/**
	 * Extend this if you are interested in exceptions during the delivery process
	 */
	protected void onException(Exception e) {}
}
