package tourGuide;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import tourGuide.helper.InternalTestHelper;
import tourGuide.object.AttractionResponse;
import tourGuide.object.LocationResponse;
import tourGuide.object.VisitedLocationResponse;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardsProxy;
import tourGuide.service.RewardsService;
import tourGuide.object.User;
import tourGuide.service.TourGuideService;
import tourGuide.tracker.TrackerThreadPool;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TestPerformance {

	@Mock
	private GpsProxy gpsProxy;

	@Mock
	private RewardsProxy rewardsProxy;

	@InjectMocks
	private RewardsService rewardsService;
	
	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */

	@Test
	public void highVolumeTrackLocation() {
		Locale.setDefault(Locale.ENGLISH);
		RewardsService rewardsService = new RewardsService();
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(100);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);
		
	    StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TrackerThreadPool trackerThreadPool = new TrackerThreadPool(tourGuideService);
		trackerThreadPool.run();
		stopWatch.stop();

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void highVolumeGetRewards() {
		Locale.setDefault(Locale.ENGLISH);
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(1000);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		LocationResponse locationResponse = new LocationResponse();
		locationResponse.setLatitude(33.817595D);
		locationResponse.setLongitude(-117.922008D);

		AttractionResponse attractionResponse = new AttractionResponse(UUID.randomUUID(),"Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);
		List<AttractionResponse> attractionResponseList = new ArrayList<>();
		attractionResponseList.add(attractionResponse);
		Mockito.when(gpsProxy.getAttractions()).thenReturn(attractionResponseList);

		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		for (User user : allUsers) {
			user.addToVisitedLocationResponseList(new VisitedLocationResponse(user.getUserId(), locationResponse, new Date()));
			Mockito.when(rewardsProxy.getRewards(attractionResponse.attractionId, user.getUserId())).thenReturn(100);
			rewardsService.calculateRewards(user);
		}

		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		stopWatch.stop();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	
}
