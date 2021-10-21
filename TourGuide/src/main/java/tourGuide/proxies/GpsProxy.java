package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservice-gps", url = "localhost:9001")
public interface GpsProxy {

  @GetMapping(value = "/getLocation")
  String getLocationThroughMS(@RequestParam String userName);

  //add an endpoint to get user's location without the back and forth
  @GetMapping(value = "/getLocationWithUser")
  String getLocationThroughMSWithUser(@RequestParam String userName);

  //add a new endpoint to check communication between microservice and application
  @GetMapping(value = "/getLocationCheck")
  String getLocationCheckThroughMS(@RequestParam String userName);

  @GetMapping(value = "/getNearbyAttractions")
  String getNearbyAttractionThroughMS(@RequestParam String userName);

}
