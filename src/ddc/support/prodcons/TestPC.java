package ddc.support.prodcons;

import ddc.support.util.Chronometer;

import java.util.LinkedList;
import java.util.Map;

public class TestPC extends AbstractPC {
    static int limit = 1000000;
    static int counter = 0;
    private static LinkedList<String> result = new LinkedList<String>();

    public TestPC(int queueCapacity, int numOfProducer, int numOfConsumer) {
        super(queueCapacity, numOfProducer, numOfConsumer);
    }

    @Override
    public synchronized String produceValue() {
        counter++;
        System.out.println("produceValue:" + counter);
        return String.valueOf(counter);
    }

    @Override
    public void consumeValue(String value) {
        System.out.println("consumeValue:" + value);
        Chronometer.sleep(1);
        result.add(value);
    }

    @Override
    public boolean isProducerTerminated() {
        return (counter >= limit);
    }


    public boolean isConsumerTerminated() {
        return (result.size()==limit);
    }

    public static void main(String[] args) throws InterruptedException {
        int QUEUE_SIZE = 1000;
        int NUM_PROD=1;
        int NUM_CONS=1;
        limit = 1000;

        Chronometer chron = new Chronometer();
        TestPC pc = new TestPC(1, 1, 1);
        pc.execute();
        System.out.println("list size: " + result.size() );
        System.out.println("Terminated - LIMIT:[" + limit + "] QUEUE:[" + QUEUE_SIZE +  "] NUM_PROD:[" + NUM_PROD + "] NUM_CONS:[" + NUM_CONS + "] Elapsed: " + chron.toString() );
    }
}
//Terminated - LIMIT:[1000000] QUEUE:[1] NUM_PROD:[1] NUM_CONS:[1] Elapsed: 6.666 secs
//Terminated - LIMIT:[1000] QUEUE:[1000] NUM_PROD:[1] NUM_CONS:[8] Elapsed: 1.337 secs
//Terminated - LIMIT:[1000] QUEUE:[1000] NUM_PROD:[1] NUM_CONS:[16] Elapsed: 1.326 secs