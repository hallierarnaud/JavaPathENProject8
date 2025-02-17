package tourGuide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import tourGuide.object.AttractionResponse;
import tourGuide.object.LocationResponse;
import tourGuide.object.User;
import tourGuide.object.UserReward;
import tourGuide.object.VisitedLocationResponse;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardsProxy;

@Slf4j
@Service
public class RewardsService {

	@Autowired
	private GpsProxy gpsProxy;

	@Autowired
	private RewardsProxy rewardsProxy;

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	public void calculateRewards(User user, Boolean test) {
		log.info("start calculate reward" + user.getUserName());
		List<VisitedLocationResponse> userLocationResponseList = user.getVisitedLocationResponseList();
		List<AttractionResponse> attractionResponseList = gpsProxy.getAttractions();
		if (test) {
			attractionResponseList.clear();
			attractionResponseList.add(gpsProxy.getAttractions().get(0));
		}
		log.info(String.valueOf(attractionResponseList.size()));
		for(VisitedLocationResponse visitedLocationResponse : userLocationResponseList) {
			for(AttractionResponse attractionResponse : attractionResponseList) {
				if(user.getUserRewards().stream().filter(r -> r.attractionResponse.attractionName.equals(attractionResponse.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocationResponse, attractionResponse)) {
						user.addUserReward(new UserReward(visitedLocationResponse, attractionResponse, getRewardPoints(attractionResponse, user)));
					}
				}
			}
		}
		log.info("end calculate reward" + user.getUserName());
	}

	public void calculateAllRewards(List<User> userList, Boolean test) {
		ExecutorService threadPool = Executors.newFixedThreadPool(200);
		for (User user : userList) {
			threadPool.submit(new Thread(() -> calculateRewards(user, test)));
		}
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(25, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			System.err.println("ThreadPool interrupted");
		}
	}
	
	public boolean isWithinAttractionProximity(AttractionResponse attractionResponse, LocationResponse locationResponse) {
		return getDistance(attractionResponse.latitude, attractionResponse.longitude, locationResponse) > attractionProximityRange ? false : true;
	}

	private boolean nearAttraction(VisitedLocationResponse visitedLocationResponse, AttractionResponse attractionResponse) {
		return getDistance(attractionResponse.latitude, attractionResponse.longitude, visitedLocationResponse.locationResponse) > proximityBuffer ? false : true;
	}
	
	public int getRewardPoints(AttractionResponse attractionResponse, User user) {
		return rewardsProxy.getRewards(attractionResponse.attractionId, user.getUserId());
	}

	public double getDistance(double latitude, double longitude, LocationResponse loc2) {
        double lat1 = Math.toRadians(latitude);
        double lon1 = Math.toRadians(longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                               + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(lon1 - lon2)));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

}
