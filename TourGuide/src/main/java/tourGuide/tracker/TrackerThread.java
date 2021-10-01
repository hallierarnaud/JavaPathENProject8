package tourGuide.tracker;

import tourGuide.service.TourGuideService;
import tourGuide.user.User;

public class TrackerThread extends Thread {

  private User user;
  private TourGuideService tourGuideService;

  public TrackerThread(User user, TourGuideService tourGuideService){
    this.tourGuideService = tourGuideService;
    this.user = user;
  }

  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName() + " Start. UserTracked = " + user);
    tourGuideService.trackUserLocation(user);
    System.out.println(Thread.currentThread().getName() + " End.");
  }

}
