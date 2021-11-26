package tourGuide;

import com.jsoniter.output.JsonStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import tourGuide.object.NearbyAttraction;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardsProxy;
import tourGuide.service.TourGuideService;
import tourGuide.object.AttractionResponse;
import tourGuide.object.ProviderResponse;
import tourGuide.object.User;
import tourGuide.object.VisitedLocationResponse;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;

	@Autowired
    private GpsProxy gpsProxy;

	@Autowired
    private RewardsProxy rewardsProxy;
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }

    @RequestMapping("/getAllCurrentLocations")
    public List<VisitedLocationResponse> getAllCurrentLocations() {
    	return tourGuideService.getAllCurrentLocations();
    }

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        User user = getUser(userName);
        VisitedLocationResponse visitedLocationResponse = tourGuideService.getUserLocationResponse(user);
        return JsonStream.serialize(visitedLocationResponse.getLocationResponse());
    }

    @RequestMapping("/getNearbyAttractions")
    public List<NearbyAttraction> getNearbyAttractions(@RequestParam String userName) {
        User user = getUser(userName);
    	return tourGuideService.getNearByAttractions(user);
    }

    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
      return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<ProviderResponse> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    //My endpoints

    @GetMapping("/trackUserLocations")
    public VisitedLocationResponse getUserLocation(@RequestParam UUID userId) {
        return gpsProxy.getUserLocation(userId);
    }

    @GetMapping("/attractions")
    public List<AttractionResponse> getAttractions() {
        return gpsProxy.getAttractions();
    }

    @GetMapping("/rewards")
    public int getRewards(@RequestParam UUID attractionId, @RequestParam UUID userId) {
        return rewardsProxy.getRewards(attractionId, userId);
    }

    @GetMapping("/users")
    public User getUserByUserName(@RequestParam String userName) {
        return tourGuideService.getUser(userName);
    }

}