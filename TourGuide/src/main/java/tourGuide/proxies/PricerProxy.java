package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

import tourGuide.object.ProviderResponse;

@FeignClient(name = "microservice-pricer", url = "localhost:9003")
public interface PricerProxy {

  @GetMapping(value = "/tripDeals")
  List<ProviderResponse> getTripDeals(@RequestParam UUID userId, @RequestParam int numberOfAdults,
                                      @RequestParam int numberOfChildren, @RequestParam int tripDuration,
                                      @RequestParam int cumulativeRewardPoints);

}
