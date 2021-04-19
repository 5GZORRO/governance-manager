package eu._5gzorro.governancemanager.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rx.subjects.PublishSubject;
import java.util.concurrent.TimeUnit;


/**
 * Queue that will defer the execution of a Runnable pushed to it by a number of seconds.
 */
@Service
public class DeferredExecutionQueue {

    private static final Logger log = LogManager.getLogger(DeferredExecutionQueue.class);
    private final PublishSubject<Runnable> executionQueue = PublishSubject.create();

    public DeferredExecutionQueue(@Value("${deferredExecutionQueue.delay:5000}") Integer DELAY_MS) {
        this.executionQueue
            .asObservable()
            .delay(DELAY_MS, TimeUnit.MILLISECONDS)
            .subscribe(r -> processDequeuedItem(r));
    }

    /**
     * Push a new runnable to the queue, whose execution you wish to be delayed by
     * the configured number of milliseconds
     * @param r The runnable to defer execution of
     */
    public void push(Runnable r) {
        this.executionQueue.onNext(r);
    }

    private void processDequeuedItem(Runnable r) {
        if (r == null) {
            log.error("Queued item was null. Abandoning execution.");
            return;
        }

        log.info("Executing dequeued item");
        try {
            r.run();
            log.info("Execution of dequeued item complete");
        }
        catch(Exception e)
        {
            log.error("Execution of dequeued item failed", e);
        }
    }
}