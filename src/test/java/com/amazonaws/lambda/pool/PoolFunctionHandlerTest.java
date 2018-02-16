package com.amazonaws.lambda.pool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class PoolFunctionHandlerTest {

	private static String input;

	//	@BeforeClass
	//	public static void createInput() throws IOException {
	//		// TODO: set up your sample input object here.
	//		Random gerador = new Random();
	//		input = String.valueOf(gerador.nextInt());
	//	}

//	private Context createContext() {
//		TestContext ctx = new TestContext();
//
//		// TODO: customize your context here if needed.
//		ctx.setFunctionName("Your Function Name");
//
//		return ctx;
//	}

//	@Test
	public void testPoolFunctionHandler() {
		ExecutorService threadPool = Executors.newFixedThreadPool(200);
		Map<Integer, Future<List<String>>> mapFuture = new HashMap<>();

		int i = 0;
		while(i < 100) {
			System.out.println("Lambda " + i + " START!");
			mapFuture.put(i, threadPool.submit(new Docker()));
			i++;
		}

		int totalFuture = mapFuture.size();

		Map<Integer, String> done = new HashMap<>();

		while(totalFuture > 0) {
			List<String> result = null;
			StringBuilder str = null;
			for(int k : mapFuture.keySet()) {
				if(mapFuture.get(k).isDone() && !done.containsKey(k)) {
					try {
						result = mapFuture.get(k).get();
						str = new StringBuilder();
						for(String item : result) {
							str.append(item);
						}

						if(str.length() > 0) {
							done.put(k, str.toString());
						} else {
							done.put(k, "empty");
						}
					} catch (InterruptedException | ExecutionException e) {
					}
					totalFuture--;
				}
			}
		}

		done.forEach((k,v) -> {
			System.out.println("\n Result for Lambda " + k + "\n" + v + "\n");
		});

		Assert.assertTrue(!mapFuture.isEmpty());
	}

	@Test
	public void testThreadHandler() {

		Thread thread = null;
		int i = 0;
		while(i < 100) {
			System.out.println("Lambda " + i + " START!");
			thread = new Thread(new DockerThread(i));
			thread.start();
			i++;
		}

		while(thread.isAlive()) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

//		Assert.assertTrue(!mapFuture.isEmpty());
	}
}
