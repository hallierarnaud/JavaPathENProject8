package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "microservice-rewards", url = "${host.reward}")
public interface RewardsProxy {

  @GetMapping(value = "/rewards")
  int getRewards(@RequestParam UUID attractionId, @RequestParam UUID userId);

}
