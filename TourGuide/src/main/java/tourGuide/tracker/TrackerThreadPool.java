package tourGuide.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import tourGuide.service.TourGuideService;
import tourGuide.user.User;

public class TrackerThreadPool extends Thread {

  private final TourGuideService tourGuideService;

  public TrackerThreadPool(TourGuideService tourGuideService) {
    this.tourGuideService = tourGuideService;
  }

  @Override
  public void run() {
    List<User> users = tourGuideService.getAllUsers();
    ExecutorService threadPool = Executors.newFixedThreadPool(200);
    for (User user : users) {
      threadPool.submit(new TrackerThread(user, tourGuideService));
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

}
