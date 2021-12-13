package tourGuide;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import tourGuide.helper.InternalTestHelper;
import tourGuide.object.AttractionResponse;
import tourGuide.object.LocationResponse;
import tourGuide.object.User;
import tourGuide.object.VisitedLocationResponse;
import tourGuide.proxies.GpsProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.tracker.TrackerThreadPool;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPerformance {

	@Autowired
	GpsProxy gpsProxy;

	@Autowired
	RewardsService rewardsService;

	@Autowired
	TourGuideService tourGuideService;

	@Value("${host.gps}")
	private String host;

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
		// GIVEN
		Locale.setDefault(Locale.ENGLISH);
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		//InternalTestHelper.setInternalUserNumber(1);
		//TourGuideService tourGuideService = new TourGuideService(rewardsService);
		StopWatch stopWatch = new StopWatch();
		List<User> userList = new ArrayList<>();
		IntStream.range(0, 10000).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			userList.add(user);
		});

		// WHEN
		stopWatch.start();

		TrackerThreadPool trackerThreadPool = new TrackerThreadPool(tourGuideService, userList);
		trackerThreadPool.start();
		stopWatch.stop();

		// THEN
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void highVolumeGetRewards() {
		// GIVEN
		Locale.setDefault(Locale.ENGLISH);
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);
		StopWatch stopWatch = new StopWatch();

		AttractionResponse attractionResponse = gpsProxy.getAttractions().get(0);
		LocationResponse locationResponse = new LocationResponse();
		locationResponse.setLatitude(attractionResponse.getLatitude());
		locationResponse.setLongitude(attractionResponse.getLongitude());
		List<User> allUsers = tourGuideService.getAllUsers();
		allUsers.forEach(u -> u.addToVisitedLocationResponseList(new VisitedLocationResponse(u.getUserId(), locationResponse, new Date())));

		// WHEN
		stopWatch.start();
		rewardsService.calculateAllRewards(allUsers, true);
		stopWatch.stop();

		// THEN
		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}
