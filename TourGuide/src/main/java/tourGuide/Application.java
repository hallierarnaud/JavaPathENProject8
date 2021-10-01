package tourGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tourGuide.service.TourGuideService;
import tourGuide.tracker.Tracker;

@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    private TourGuideService tourGuideService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        Tracker tracker = new Tracker(tourGuideService);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                tracker.stopTracking();
            }
        });
    }
}
