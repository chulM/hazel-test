package com.chulm.hazel.test.async;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

import java.io.Serializable;
import java.util.concurrent.*;

public class FutureExecutors {
    public static void main(String[] args){
        int n = 10;
        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        IExecutorService executor = hz.getExecutorService("executor");

        Future<Long> future = executor.submit(new FibonacciCallable(n));
        try {
            long result = future.get(10, TimeUnit.SECONDS);
            System.out.println("Result: " + result);
        } catch (TimeoutException e) {
            System.err.println("A timeout occurred!");
            future.cancel(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Hazelcast.shutdownAll();
    }
}



class FibonacciCallable implements Callable<Long>, Serializable {

    private final int input;

    public FibonacciCallable(int input) {
        this.input = input;
    }

    public Long call() {
        return calculate(input);
    }

    private long calculate(int n) {
        if (n <= 1) {
            return n;
        } else {
            return calculate(n - 1) + calculate(n - 2);
        }
    }
}