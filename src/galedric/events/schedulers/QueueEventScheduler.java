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

import java.util.ArrayDeque;
import java.util.Queue;

import galedric.events.Event;

/**
 * Simple FIFO event scheduler.
 */
public class QueueEventScheduler implements EventScheduler {
	private Queue<Event> queue;

	/**
	 * Creates a new scheduler with an ArrayDeque queue.
	 */
	public QueueEventScheduler() {
		this(new ArrayDeque<Event>());
	}

	/**
	 * Creates a new scheduler with a specifi queue object.
	 */
	public QueueEventScheduler(Queue<Event> queue) {
		this.queue = queue;
	}

	/**
	 * Return the next event in the queue.
	 */
	public synchronized Event nextEvent() {
		return queue.poll();
	}

	/**
	 * Adds a new event to the queue.
	 */
	public synchronized void addEvent(Event event) {
		queue.offer(event);
	}
}
