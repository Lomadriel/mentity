/*
 * mentity
 *
 * Copyright (c) 2016 Jérôme BOULMIER
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */

package org.lomadriel.mentity.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Auto-resizable array.
 *
 * @param <E> type of the elements in this list.
 * @author Jérôme BOULMIER
 * @since 0.4
 */
public class Bag<E> implements Serializable {
	private static final long serialVersionUID = 3314881599845413840L;

	/**
	 * Elements of this collection.
	 * {@link #writeObject(ObjectOutputStream)} and {@link #readObject(ObjectInputStream)}
	 */
	private transient E[] elements;
	private int highestElement;

	public Bag() {
		this(16);
	}

	@SuppressWarnings("unchecked")
	public Bag(int capacity) {
		this.elements = (E[]) new Object[capacity];
	}

	/**
	 * Returns the element at the specified position.
	 *
	 * @param index index of the element to return.
	 * @return the element at the specified position.
	 * @throws ArrayIndexOutOfBoundsException if index is negative.
	 */
	public E get(int index) {
		if (index >= this.elements.length) {
			return null;
		}

		return this.elements[index];
	}

	/**
	 * Sets the element at the specified position
	 *
	 * @param index index of the element to set.
	 * @param value value of the element.
	 * @throws ArrayIndexOutOfBoundsException if index is negative.
	 */
	public void set(int index, E value) {
		if (value == null) {
			return;
		}

		if (index > this.highestElement) {
			this.highestElement = index;

			// if index < this.highestElement, there is no need to ensure the capacity.
			ensureCapacity(index);
		}

		this.elements[index] = value;
	}

	/**
	 * Clears the collection.
	 */
	public void clear() {
		for (int i = 0; i < Math.max(this.elements.length, this.highestElement); ++i) {
			this.elements[i] = null;
		}
	}

	/**
	 * Returns the size of this collection.
	 *
	 * @return the size of this collection.
	 */
	public int size() {
		return this.highestElement + 1;
	}

	/**
	 * Returns the current capacity of this collection.
	 *
	 * @return the current capacity of this collection.
	 */
	public int capacity() {
		return this.elements.length;
	}

	private void ensureCapacity(int index) {
		if (index >= this.elements.length) {
			// It might be more interresting to double the array length rather than compute the next power of two.
			int newCapacity = Bag.nextPowerOfTwo(index + 1);
			@SuppressWarnings("unchecked") E[] elements = (E[]) new Object[newCapacity];
			System.arraycopy(this.elements, 0, elements, 0, this.elements.length);
			this.elements = elements;
		}
	}

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();

		for (int i = 0; i <= this.highestElement; i++) {
			stream.writeObject(this.elements[i]);
		}
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		int newCapacity = Bag.nextPowerOfTwo(this.highestElement + 1);
		this.elements = (E[]) new Object[newCapacity];

		for (int i = 0; i <= this.highestElement; i++) {
			this.elements[i] = (E) stream.readObject();
		}

	}

	private static int nextPowerOfTwo(int value) {
		if (value == 0) {
			return 1;
		}

		value--;

		value |= value >>> 1;
		value |= value >>> 2;
		value |= value >>> 4;
		value |= value >>> 8;
		value |= value >>> 16;

		return value + 1;
	}
}
