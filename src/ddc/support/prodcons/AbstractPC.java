package ddc.support.prodcons;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractPC {
    public abstract String produceValue();

    public abstract void consumeValue(String value) throws IOException;

    public abstract boolean isProducerTerminated();

    public BlockingQueue<String> blockingQueue = null;

    private int numOfProducer;
    private int numOfConsumer;
    private int queueCapacity;

    private int queueFullCounter=0;
    private final AtomicInteger producerCounter = new AtomicInteger(0);
    private final AtomicInteger consumerCounter = new AtomicInteger(0);
    private boolean isStopRequested = false;

    public AbstractPC(int queueCapacity, int numOfProducer, int numOfConsumer) {
        this.numOfProducer = numOfProducer;
        this.numOfConsumer = numOfConsumer;
        this.queueCapacity = queueCapacity;
        this.blockingQueue = new LinkedBlockingDeque<String>(queueCapacity);
    }

    public int getProducedCount() {
        return producerCounter.get();
    }

    public void stopRequested() {
        isStopRequested = true;
    }

    private void produce() {
        while (true) {
            if (isProducerTerminated() || isStopRequested) break;
            String value = null;
            try {
                value = produceValue();
                if (value != null) {
                    //System.out.println("put...");
                    blockingQueue.put(value);
                    producerCounter.incrementAndGet();
                    if (blockingQueue.size() == queueCapacity) {
                       queueFullCounter++;
                    }
                }
            } catch (Throwable e) {
                isStopRequested = true;
                System.err.println("produce - Exception:" + e + " row#:[" + producerCounter.get() + "] value:[" + value + "]");
                throw new RuntimeException(e);
            }
        }
        System.err.println("produce - end queueFullCounter: " + queueFullCounter);
    }

    private void consume() {
        boolean allElementsAreProcessed = consumerCounter.get() == producerCounter.get();
        while (true) {
            if ((isProducerTerminated() && allElementsAreProcessed) || isStopRequested) break;
            String value = null;
            try {
                //System.out.println("take...");
                value = blockingQueue.poll(1000, TimeUnit.MILLISECONDS);
                if (value != null) {
                    this.consumeValue(value);
                    consumerCounter.incrementAndGet();
                }
            } catch (Throwable e) {
                isStopRequested = true;
                System.err.println("consume - Exception:" + e + " row#:[" + consumerCounter.get() + "] value:[" + value + "]");
                throw new RuntimeException(e);
            }
        }
        System.err.println("consume - end");
    }

    public int execute() throws InterruptedException {
        LinkedList<Thread> list = new LinkedList<>();
        for (int i = 0; i < numOfProducer; i++) {
            Thread producerThread = new Thread(this::produce);
            list.add(producerThread);
        }

        for (int i = 0; i < numOfConsumer; i++) {
            Thread consumerThread = new Thread(this::consume);
            list.add(consumerThread);
        }

        System.out.println("start...");
        for (Thread t : list) {
            t.start();
        }
        System.out.println("join...");
        for (Thread t : list) {
            t.join();
        }
        System.out.println("joined");
        return producerCounter.get();
    }

/*    public void execute() {
        ExecutorService service = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < numOfProducer; i++) {
            Thread producerThread = new Thread(this::produce);
            service.execute(producerThread);
        }

        for (int i = 0; i < numOfConsumer; i++) {
            Thread consumerThread = new Thread(this::consume);
            service.execute(consumerThread);
        }
        service.shutdown();
    }*/
}
