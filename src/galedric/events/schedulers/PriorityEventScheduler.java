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

package galedric.events.schedulers;

import java.util.PriorityQueue;

import galedric.events.Event;
import galedric.events.PriorityEvent;

/**
 * Priority-based event scheduler.
 * 
 * This scheduler can handle PriorityEvents and simple Events at the same time.
 * If both event types are given, Event objects will always be delivered before
 * queued PriorityEvents.
 * 
 * You should not mix PriorityEvent types (like PriorityEvent<A> and
 * PriorityEvent<B>) because comparaison between the two can be undefined.
 */
public class PriorityEventScheduler extends QueueEventScheduler {
	PriorityQueue<PriorityEvent<?>> priorityQueue;

	/**
	 * Creates a new priority-based scheduler.
	 */
	public PriorityEventScheduler() {
		priorityQueue = new PriorityQueue<PriorityEvent<?>>();
	}

	/**
	 * Adds an event.
	 */
	public synchronized void addEvent(Event event) {
		if(event instanceof PriorityEvent<?>) {
			priorityQueue.offer((PriorityEvent<?>) event);
		}
		else {
			super.addEvent(event);
		}
	}

	/**
	 * Returns the next event.
	 */
	public synchronized Event nextEvent() {
		Event event;
		if((event = super.nextEvent()) != null) {
			return event;
		}

		return priorityQueue.poll();
	}
}
