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

import galedric.events.schedulers.EventScheduler;
import galedric.events.schedulers.QueueEventScheduler;

/**
 * Async event emitter.
 * 
 * Unlike a simple EventEmitter, this class uses a thread to perform event
 * distribution. Thus, the call to emit() is non-blocking.
 * 
 * This class spawns at most one distribution thread. Thus, even if the emitter
 * doesn't block on emit(), slow event handlers can still induce latency in the
 * whole system by delaying the distribution process.
 */
public class AsyncEventEmitter extends EventEmitter {
	private Event activeEvent;
	private EventScheduler scheduler;

	/**
	 * Creates a new AsyncEventEmitter with default emitter value and scheduler
	 */
	public AsyncEventEmitter() {
		this(null, null);
	}

	/**
	 * Creates a new AsyncEventEmitter with a given emitter value and
	 * the default scheduler
	 */
	public AsyncEventEmitter(EventEmitterInterface emitter) {
		this(emitter, null);
	}

	/**
	 * Creates a new AsyncEventEmitter with the default emitter value and
	 * a given scheduler
	 */
	public AsyncEventEmitter(EventScheduler scheduler) {
		this(null, scheduler);
	}

	/**
	 * Creates a new AsyncEventEmitter with a given emitter value and scheduler
	 */
	public AsyncEventEmitter(EventEmitterInterface emitter, EventScheduler scheduler) {
		super(emitter);

		if(scheduler == null) {
			scheduler = new QueueEventScheduler();
		}

		this.scheduler = scheduler;
	}

	/**
	 * Emits an event asynchronously to all listeners
	 */
	public synchronized void emit(Event event) {
		// Give the event to the scheduler
		scheduler.addEvent(event);

		// If no event are currently being delivered, spawn the distribution thread
		if(activeEvent == null) {
			nextEvent();

			new Thread() {
				public void run() {
					while(activeEvent != null) {
						try {
							AsyncEventEmitter.super.emit(activeEvent);
						}
						finally {
							nextEvent();
						}
					}
				}
			}.start();
		}
	}

	private synchronized void nextEvent() {
		activeEvent = scheduler.nextEvent();
	}
}
