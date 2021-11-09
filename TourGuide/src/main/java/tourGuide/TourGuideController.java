package tourGuide;

import com.jsoniter.output.JsonStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardsProxy;
import tourGuide.service.TourGuideService;
import tourGuide.user.AttractionResponse;
import tourGuide.user.ProviderResponse;
import tourGuide.user.User;
import tourGuide.user.VisitedLocationResponse;
import tripPricer.Provider;

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
    
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
    	// TODO: Get a list of every user's most recent location as JSON
    	//- Note: does not use gpsUtil to query for their current location, 
    	//        but rather gathers the user's current location from their stored location history.
    	//
    	// Return object should be the just a JSON mapping of userId to Locations similar to:
    	//     {
    	//        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371} 
    	//        ...
    	//     }
    	
    	return JsonStream.serialize("");
    }
    
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
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

    //Initial endpoints reproduction

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        User user = getUser(userName);
        VisitedLocationResponse visitedLocationResponse = tourGuideService.getUserLocationResponse(user);
        return JsonStream.serialize(visitedLocationResponse.getLocationResponse());
    }

    //  TODO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
        // Name of Tourist attraction,
        // Tourist attractions lat/long,
        // The user's location lat/long,
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        User user = getUser(userName);
        VisitedLocationResponse visitedLocationResponse = tourGuideService.getUserLocationResponse(user);
    	return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocationResponse));
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

}