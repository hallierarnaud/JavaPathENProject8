package tourGuide.tracker;

import tourGuide.service.TourGuideService;
import tourGuide.object.User;

public class TrackerThread extends Thread {

  private User user;
  private TourGuideService tourGuideService;
  private int i;

  public TrackerThread(User user, TourGuideService tourGuideService, int i){
    this.tourGuideService = tourGuideService;
    this.user = user;
    this.i = i;
  }

  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName() + " Start. UserTracked = " + i + " UserName = " + user.getUserName());
    tourGuideService.trackUserLocation(user);
    System.out.println(Thread.currentThread().getName() + " End. UserTracked = " + i);
  }

}
