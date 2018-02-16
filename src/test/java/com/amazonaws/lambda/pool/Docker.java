package com.amazonaws.lambda.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import com.amazonaws.services.lambda.runtime.Context;

public class Docker implements Callable<List<String>> {

	private List<String> result = new ArrayList<>();

	private Context createContext() {
		TestContext ctx = new TestContext();

		ctx.setFunctionName("DB LAMBDA");

		return ctx;
	}

	@Override
	public List<String> call() throws Exception {

		PoolFunctionHandler handler = new PoolFunctionHandler();
		Context ctx = createContext();

		Random gerador = new Random();
		String input = null;

		int i = 0;
		while(i < 10) {
			input = String.valueOf(gerador.nextInt());
			result.add("\nCALL " + i + "\n");
			result.add(handler.handleRequest(input, ctx));
			i++;
		}

		return result;
	}

}
