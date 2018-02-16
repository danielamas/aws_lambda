package com.amazonaws.lambda.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.amazonaws.services.lambda.runtime.Context;

public class DockerThread implements Runnable {

	private Integer lambdaNumber = null;

	public DockerThread(Integer lambdaNumber) {
		this.lambdaNumber = lambdaNumber;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();

		ctx.setFunctionName("DB LAMBDA");

		return ctx;
	}


	@Override
	public void run() {
		List<String> result = new ArrayList<>();
		PoolFunctionHandler handler = new PoolFunctionHandler();
		Context ctx = createContext();

		Random gerador = new Random();
		String input = null;

		int i = 0;
		while(i < 1) {
			input = String.valueOf(gerador.nextInt());
			result.add("\nCALL " + i + "\n");
			result.add(handler.handleRequest(input, ctx));
			i++;
		}

		System.out.println("\n Result for Lambda " + this.lambdaNumber + "\n");
		result.forEach(item -> {
			System.out.println(item + "\n");
		});
	}
}
