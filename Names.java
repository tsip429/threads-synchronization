package names;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.TreeMap;

public class Names {

	TreeMap<Integer, TreeMap<Integer, String>> boysNames = new TreeMap<Integer, TreeMap<Integer, String>>();
	TreeMap<Integer, TreeMap<Integer, String>> girlsNames = new TreeMap<Integer, TreeMap<Integer, String>>();
	static Object sync = new Object();

	private class Babies implements Runnable {

		URL url;

		public Babies(String newURL) {
			try {
				url = new URL(newURL);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			// put getNames code in here

			URLConnection connect;
			try {
				connect = url.openConnection();
				Scanner s = new Scanner(connect.getInputStream());

				for (int j = 0; j < 11; j++) {
					s.nextLine();
				}

				while (!s.hasNext("</body>")) {
					String str = "";

					int year = s.nextInt();
					String boy = s.next();
					int numBoys = s.nextInt();
					String girl = s.next();

					str = s.next();
					str = str.substring(0, str.length() - 4);
					int numGirls = Integer.parseInt(str);
					synchronized (sync) {
						if (!boysNames.containsKey(year)) {
							boysNames.put(year, new TreeMap<Integer, String>());
							boysNames.get(year).put(numBoys, boy);

							girlsNames.put(year, new TreeMap<Integer, String>());
							girlsNames.get(year).put(numGirls, girl);
						} else {
							boysNames.get(year).put(numBoys, boy);
							girlsNames.get(year).put(numGirls, girl);
						}
					}
					s.nextLine();
				}
				s.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	// use try catch to catch exceptions thrown in the tests

	public int numNamePairs() {
		int num = 0;
		for (Integer i : boysNames.keySet())
			num += boysNames.get(i).size();

		return num;
	}

	public void getNames(String[] urls) {
		// start and join threads in here
		// for loop - giving urls to threads
		if (urls == null) {
			throw new IllegalArgumentException();
		}

		Thread[] threads = new Thread[urls.length];

		for (int i = 0; i < urls.length; i++) {
			Babies baby = new Babies(urls[i]);
			threads[i] = new Thread(baby);
		}

		for (int k = 0; k < urls.length; k++)
			threads[k].start();

		for (int j = 0; j < urls.length; j++) {
			try {
				threads[j].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public String getGirlName(int year, int rank) {
		String str = null;
		if (girlsNames.containsKey(year)) {
			int count = 1;
			for (Integer n : girlsNames.get(year).descendingKeySet()) {
				if (count == rank) {
					str = girlsNames.get(year).get(n);
				}
				count++;
			}
		}
		return str;
	}

	public String getBoyName(int year, int rank) {
		String str = null;
		if (boysNames.containsKey(year)) {
			int count = 1;
			for (Integer n : boysNames.get(year).descendingKeySet()) {
				if (count == rank) {
					str = boysNames.get(year).get(n);
				}
				count++;
			}
		}
		return str;
	}

	public int getGirlRank(int year, String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		int rank = -1;
		int curr = 0;
		if (girlsNames.containsKey(year)) {
			for (Integer y : girlsNames.get(year).descendingKeySet()) {
				curr++;
				if (girlsNames.get(year).get(y).equals(name))
					rank = curr;
			}
		}
		return rank;
	}

	public int getBoyRank(int year, String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		int rank = -1;
		int curr = 0;
		if (boysNames.containsKey(year)) {
			for (Integer y : boysNames.get(year).descendingKeySet()) {
				curr++;
				if (boysNames.get(year).get(y).equals(name))
					rank = curr;
			}
		}
		return rank;
	}

}
