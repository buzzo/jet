package br.unicamp.ic.ecj;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import br.unicamp.ic.TestResource;
import br.unicamp.ic.ecj.callable.DoubleEvolution;
import br.unicamp.ic.ecj.callable.FloatEvolution;
import br.unicamp.ic.ecj.callable.IntegerEvolution;
import br.unicamp.ic.ecj.util.Configuration;
import br.unicamp.ic.ecj.util.Solution;

public class GEOECJMultiThreadTest {

    private static final int MAX_GENERATIONS  = 100000;
    private static final int THREAD_POOL_SIZE = 10;

    @Test
    public void testArrayPartition() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.ArrayPartition";
        final String methodName = "run";
        final Class<?>[] parameters = new Class<?>[] { int[].class, int.class, int.class };
        final Class<?> returnType = int.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        paths.add(Arrays.asList(new Integer[] { 0, 1 }));
        paths.add(Arrays.asList(new Integer[] { 0, 2, 4, 5, 7, 6, 7, 8, 9, 11, 4, 3, 4, 5, 7, 8, 10 }));

        final Configuration config = new Configuration(1f, 12, 0, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Integer>>> futures = new ArrayList<Future<Solution<Integer>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Integer>> future = service.submit(new IntegerEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Integer>> future : futures) {
            final Solution<Integer> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

    @Test
    public void testBinarySearch() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.BinarySearch";
        final String methodName = "run";
        final Class<?>[] parameters = new Class<?>[] { int[].class, int.class };
        final Class<?> returnType = boolean.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        // paths.add(Arrays.asList(new Integer[] { 0, 6, 7, 1, 3, 5, 6, 7, 8 }));
        paths.add(Arrays.asList(new Integer[] { 0, 6, 7, 1, 2, 6, 7, 1, 3, 5, 6, 7, 8 }));
        paths.add(Arrays.asList(new Integer[] { 0, 6, 7, 1, 3, 4, 6, 7, 1, 3, 5, 6, 7, 8 }));

        final Configuration config = new Configuration(1f, 100, 0, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Integer>>> futures = new ArrayList<Future<Solution<Integer>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Integer>> future = service.submit(new IntegerEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Integer>> future : futures) {
            final Solution<Integer> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

    @Test
    public void testComplexTriangle() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.ComplexTriangle";
        final String methodName = "run";
        final Class<?>[] parameters = new Class<?>[] { int.class, int.class, int.class };
        final Class<?> returnType = int.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        paths.add(Arrays.asList(new Integer[] { 0, 15, 16 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 6, 7, 8, 16 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 6, 9, 16 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 10, 11, 16 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 10, 12, 13, 16 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 10, 12, 14, 16 }));

        final Configuration config = new Configuration(1f, 100, 0, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Integer>>> futures = new ArrayList<Future<Solution<Integer>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Integer>> future = service.submit(new IntegerEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Integer>> future : futures) {
            final Solution<Integer> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

    @Test
    public void testLinearSearch() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.LinearSearch";
        final String methodName = "run";
        final Class<?>[] parameters = new Class<?>[] { int[].class, int.class };
        final Class<?> returnType = boolean.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        paths.add(Arrays.asList(new Integer[] { 0, 4, 5, 1, 2, 3, 4, 6 }));
        // paths.add(Arrays.asList(new Integer[] { 0, 4, 5, 1, 3, 4, 5, 1, 2, 3, 4, 6 }));
        // paths.add(Arrays.asList(new Integer[] { 0, 4, 5, 1, 3, 4, 5, 1, 3, 4, 5, 1, 2, 3, 4, 6 }));

        final Configuration config = new Configuration(1f, 100, 0, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Integer>>> futures = new ArrayList<Future<Solution<Integer>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Integer>> future = service.submit(new IntegerEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Integer>> future : futures) {
            final Solution<Integer> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

    @Test
    public void testMathUtils() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.MathUtils";
        final String methodName = "addAndCheck";
        final Class<?>[] parameters = new Class<?>[] { int.class, int.class, String.class };
        final Class<?> returnType = int.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        paths.add(Arrays.asList(new Integer[] { 0, 2, 8, 9, 11 }));
        paths.add(Arrays.asList(new Integer[] { 0, 2, 3, 4, 5, 11 }));
        paths.add(Arrays.asList(new Integer[] { 0, 2, 3, 7, 11 }));
        paths.add(Arrays.asList(new Integer[] { 0, 2, 8, 10 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 11 }));

        final Configuration config = new Configuration(1f, 100, -1, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Integer>>> futures = new ArrayList<Future<Solution<Integer>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Integer>> future = service.submit(new IntegerEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Integer>> future : futures) {
            final Solution<Integer> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

    @Test
    public void testMiddleValue() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.MiddleValue";
        final String methodName = "run";
        final Class<?>[] parameters = new Class<?>[] { int.class, int.class, int.class };
        final Class<?> returnType = int.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 9 }));
        paths.add(Arrays.asList(new Integer[] { 0, 3, 6, 7, 8, 9 }));
        paths.add(Arrays.asList(new Integer[] { 0, 3, 4, 5, 9 }));

        final Configuration config = new Configuration(1f, 100, 0, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Integer>>> futures = new ArrayList<Future<Solution<Integer>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Integer>> future = service.submit(new IntegerEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Integer>> future : futures) {
            final Solution<Integer> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

    @Test
    public void testProduct() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.Product";
        final String methodName = "run";
        final Class<?>[] parameters = new Class<?>[] { int.class, int.class };
        final Class<?> returnType = int.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        paths.add(Arrays.asList(new Integer[] { 0, 2, 6 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 3, 5, 4, 5, 6 }));

        final Configuration config = new Configuration(1f, 100, 0, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Integer>>> futures = new ArrayList<Future<Solution<Integer>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Integer>> future = service.submit(new IntegerEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Integer>> future : futures) {
            final Solution<Integer> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

    @Test
    public void testRest() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.Rest";
        final String methodName = "run";
        final Class<?>[] parameters = new Class<?>[] { int.class, int.class };
        final Class<?> returnType = int.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        paths.add(Arrays.asList(new Integer[] { 0, 2, 3, 1, 2, 4 }));

        final Configuration config = new Configuration(1f, 100, 0, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Integer>>> futures = new ArrayList<Future<Solution<Integer>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Integer>> future = service.submit(new IntegerEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Integer>> future : futures) {
            final Solution<Integer> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

    @Test
    public void testSimpleTriangle() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.SimpleTriangle";
        final String methodName = "run";
        final Class<?>[] parameters = new Class<?>[] { int.class, int.class, int.class };
        final Class<?> returnType = int.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        paths.add(Arrays.asList(new Integer[] { 0, 11, 12 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 10, 12 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 6, 7, 8, 12 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 6, 9, 12 }));
        final Configuration config = new Configuration(1f, 100, 0, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Integer>>> futures = new ArrayList<Future<Solution<Integer>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Integer>> future = service.submit(new IntegerEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Integer>> future : futures) {
            final Solution<Integer> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

    @Test
    public void testSimpleTriangleDouble() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.SimpleTriangleDouble";
        final String methodName = "run";
        final Class<?>[] parameters = new Class<?>[] { double.class, double.class, double.class };
        final Class<?> returnType = int.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        paths.add(Arrays.asList(new Integer[] { 0, 11, 12 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 10, 12 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 6, 7, 8, 12 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 6, 9, 12 }));
        final Configuration config = new Configuration(1f, 100, 0, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Double>>> futures = new ArrayList<Future<Solution<Double>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Double>> future = service.submit(new DoubleEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Double>> future : futures) {
            final Solution<Double> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

    @Test
    public void testSimpleTriangleFloat() throws MalformedURLException, ClassNotFoundException, InterruptedException, ExecutionException {
        final String className = "br.unicamp.ic.problems.SimpleTriangleFloat";
        final String methodName = "run";
        final Class<?>[] parameters = new Class<?>[] { float.class, float.class, float.class };
        final Class<?> returnType = int.class;

        final List<List<Integer>> paths = new ArrayList<List<Integer>>();
        paths.add(Arrays.asList(new Integer[] { 0, 11, 12 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 10, 12 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 6, 7, 8, 12 }));
        paths.add(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 6, 9, 12 }));
        final Configuration config = new Configuration(1f, 100, 0, GEOECJMultiThreadTest.MAX_GENERATIONS, TestResource.LOCAL, className,
            methodName, parameters, returnType);

        final ExecutorService service = Executors.newFixedThreadPool(GEOECJMultiThreadTest.THREAD_POOL_SIZE);
        final List<Future<Solution<Float>>> futures = new ArrayList<Future<Solution<Float>>>();

        for (final List<Integer> path : paths) {
            final Future<Solution<Float>> future = service.submit(new FloatEvolution(path, config));
            futures.add(future);
        }

        for (final Future<Solution<Float>> future : futures) {
            final Solution<Float> solution = future.get();
            System.out.println("[" + solution.getTime() + "] Solution for path:[" + solution.getPath() + "] found:[" + solution.isFound()
                + "] generations:[" + solution.getGenerations() + "] solution:[" + Arrays.asList(solution.getSolutions()) + "]");
        }

        service.shutdown();
    }

}
