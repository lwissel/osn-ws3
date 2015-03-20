/** continously running worker thread started by SimpleThreadPool
 *
 * @author Lennart Wissel
 * @version 15.02.2015
 */
package simpleThreadPool;

import java.util.concurrent.*;

public class SimplePoolThread implements ISimplePoolThread {
   private LinkedBlockingQueue<ISimpleTask> taskQueue;
   private boolean isStopped = false;
   private String name = "Thread ";

   // default constructor
   public SimplePoolThread(LinkedBlockingQueue<ISimpleTask> taskQueue) {
      this.taskQueue = taskQueue;
   }

   // constructor with possibility to add a thread id
   public SimplePoolThread(LinkedBlockingQueue<ISimpleTask> taskQueue, int n) {
      this.taskQueue = taskQueue;
      this.name = name + n;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public void run() {
      /**
       * here we will use an infinite loop to retrieve new tasks and to perform them
       */
      while(!isStopped) {
         try {
           ISimpleTask task = taskQueue.take(); 
           task.run();
         }
         catch (InterruptedException e) {
            isStopped = true;
         }
      }
   }
}
