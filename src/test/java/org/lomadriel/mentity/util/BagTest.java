package org.lomadriel.mentity.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BagTest {
	private Bag<Integer> bag;

	@Before
	public void setUp() throws Exception {
		this.bag = new Bag<>();

		for (int i = 0; i < 10; i++) {
			this.bag.set(i, new Integer(i));
		}

		for (int i = 15; i < 20; i++) {
			this.bag.set(i, new Integer(i));
		}
	}

	@Test
	public void serialization() throws Exception {
		File file = new File("bag");
		file.deleteOnExit();

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(this.bag);
		}

		Bag<Integer> loadedBag;

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			loadedBag = (Bag<Integer>) ois.readObject();
		}

		Assert.assertEquals(this.bag.size(), loadedBag.size());
		Assert.assertEquals(this.bag.capacity(), loadedBag.capacity());

		for (int i = 0; i < this.bag.size(); i++) {
			Assert.assertEquals(this.bag.get(i), loadedBag.get(i));
		}
	}
}
