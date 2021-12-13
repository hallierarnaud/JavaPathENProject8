package tourGuide.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import tourGuide.service.TourGuideService;
import tourGuide.object.User;

public class TrackerThreadPool extends Thread {

  private final TourGuideService tourGuideService;
  private List<User> userList;

  public TrackerThreadPool(TourGuideService tourGuideService, List<User> userList) {
    this.tourGuideService = tourGuideService;
    this.userList = userList;
  }

  @Override
  public void run() {
    List<User> users = userList;
    ExecutorService threadPool = Executors.newFixedThreadPool(800);
    int i = 0;
    for (User user : users) {
      i++;
      threadPool.submit(new TrackerThread(user, tourGuideService, i));
    }
    threadPool.shutdown();
    try {
      if (threadPool.awaitTermination(5, TimeUnit.MINUTES)) {
        System.out.println("Threadpool tasks finished");
      } else {
        System.out.println("Timeout");
      }
    } catch (InterruptedException e) {
      System.err.println("ThreadPool interrupted");
    }
  }

  public void start() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(this);
    executorService.shutdown();
    try {
      if (executorService.awaitTermination(5, TimeUnit.MINUTES)) {
        System.out.println("Threadpool tasks finished");
      } else {
        System.out.println("Timeout");
      }
    } catch (InterruptedException e) {
      System.err.println("ThreadPool interrupted");
    }
  }

}
