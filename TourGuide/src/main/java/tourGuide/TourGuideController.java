package tourGuide;

import com.jsoniter.output.JsonStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import tourGuide.proxies.GpsProxy;
import tourGuide.service.TourGuideService;
import tourGuide.user.AttractionDTOFromGpsService;
import tourGuide.user.User;
import tourGuide.user.UserDTO;
import tourGuide.user.UserDTOFromGpsService;
import tourGuide.user.VisitedLocationDTOFromGpsService;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;

	@Autowired
    private GpsProxy gpsProxy;
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    /*@RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }*/

    @GetMapping("/getLocation")
    public String getLocationThroughMS(@RequestParam String userName) {
      return gpsProxy.getLocationThroughMS(userName);
    }

    //add an endpoint to get user's location without the back and forth
    @GetMapping("/getLocationWithUser")
    public String getLocationThroughMSWithUser(@RequestParam String userName) {
        return gpsProxy.getLocationThroughMSWithUser(userName);
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
    /*@RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
    	return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
    }*/
    @GetMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        return JsonStream.serialize(gpsProxy.getNearbyAttractionThroughMS(userName));
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
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

    @GetMapping("/getVisitedLocations")
    public List<VisitedLocationDTOFromGpsService> getVisitedLocations(@RequestParam String userName) {
        return gpsProxy.getVisitedLocations(userName);
    }

    @GetMapping("/getAttractions")
    public List<AttractionDTOFromGpsService> getAttractions() {
        return gpsProxy.getAttractions();
    }
    /*@GetMapping("/getAttractions")
    public JsonNode getAttractions() throws IOException {
        String attractions = JsonStream.serialize(gpsProxy.getAttractions());
        JsonNode node = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            node = mapper.readTree(attractions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return node;
    }*/
    /*@GetMapping("/getAttraction")
    public AttractionDTORequest getAttraction(@RequestParam int attractionNumber) {
        return gpsProxy.getAttraction(attractionNumber);
    }*/
    /*@GetMapping("/getAttraction")
    public Attraction getAttraction(@RequestParam int attractionNumber) throws IOException {
        String attractionJsonString = JsonStream.serialize(gpsProxy.getAttraction(attractionNumber));
        Attraction attraction = new ObjectMapper().readValue(attractionJsonString, Attraction.class);
        return attraction;
    }*/
    /*@GetMapping("/getAttraction")
    public JsonNode getAttraction(@RequestParam int attractionNumber) throws IOException {
        String attractionJsonString = JsonStream.serialize(gpsProxy.getAttraction(attractionNumber));
        JsonNode attraction = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            attraction = mapper.readTree(attractionJsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return attraction;
    }*/
    
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
    	return JsonStream.serialize(providers);
    }
    
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }

    //add an endpoint to get user directly through application
    @GetMapping("getUserThroughApplication")
    public User getUserThroughApplication(String userName) {
      return tourGuideService.getUser(userName);
    }



    //Utilisation de cet endpoint
    //add an endpoint to get userDTO directly through application
    @GetMapping("/getUserDTO")
    public UserDTO getUserEndPoint(String userName) {
      return tourGuideService.getUserDTORequest(userName);
    }



    //add two endpoints to check communication between microservice and application
    @GetMapping("/getLocationCheck")
    public String getLocationCheckThroughMS(@RequestParam String userName) {
      return gpsProxy.getLocationCheckThroughMS(userName);
    }
    @GetMapping("/getUserNameCheck")
    public String getUserNameCheckThroughApplication() {
      return "Lucas&Nathan";
    }



    //Création de cette méthode
    @GetMapping("/getUserInGpsIsolation")
    public UserDTOFromGpsService getUserDTOFromApplicationThroughGps(@RequestParam String userName) {
        return gpsProxy.getUserDTOFromApplicationThroughGps(userName);
    }

}