package simpleThreadPool;

import java.util.concurrent.LinkedBlockingQueue;
/*
 *   @author   Lennart Wissel
 *   @version  16.02.2015
 */

/**
 *   <<-- Thread Pool -->> 
 *   It manages a queue of tasks, starts some pool threads.
 * 		
 *   #1. Create a task queue by using queue data structures, or designing your own data structure. 
 */
public class SimpleThreadPool {
	
	/**
	 *   #1. Initialize your queue (or do so in somewhere)
	 *   #2. Starts some ISimplePoolThreads.
	 */
   private LinkedBlockingQueue<ISimpleTask> taskQueue;
   private Thread[] workerThreads;


   // constructor with a default of 3 threads
   public SimpleThreadPool() {
      this.taskQueue = new LinkedBlockingQueue<ISimpleTask>();
      this.workerThreads = new Thread[3]; // by default we create 3 threads to do the work for us
   }

   // constructor with the possibility to specify the number of threads
   public SimpleThreadPool(int nThreads) {
      this.taskQueue = new LinkedBlockingQueue<ISimpleTask>();
      this.workerThreads = new Thread[nThreads];
   }

   // this methods starts all threads which call run() in the process
	public void start() {
      // start all threads
      for(int i = 0; i < this.workerThreads.length; i++) {
         SimplePoolThread spThread = new SimplePoolThread(taskQueue, i);
         workerThreads[i] = new Thread(spThread);
         System.out.println(spThread.getName() + " started");
         workerThreads[i].start();
      }
   }
	
	/**
	 *   #1. Stops everything after trying to finish the tasks
	 */
	public void stop() {
      // try to finish tasks in queue
      while(!taskQueue.isEmpty()) {
         try{
            Thread.sleep(10);
         } catch (InterruptedException e) {
            // reset the interrupt status
            Thread.currentThread().interrupt();
         }
      }
      for (Thread workerThread : workerThreads) {
         workerThread.interrupt();
         System.out.println(workerThread.getName() + " has been interrupted");
      }
   }
	
	/**
	 *   #1. Add a task to your queue.
    *   The LinkedBlockedQueue is threadsafe by default
	 */
	public void addTask(ISimpleTask task) {
      try {
         taskQueue.put(task);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
      }
   }
}

