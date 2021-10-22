package tourGuide.user;

import org.springframework.stereotype.Service;

@Service
public class MapService {

  public UserDTORequest convertUserToUserDTORequest(User user) {
    UserDTORequest userDTORequest = new UserDTORequest();
    userDTORequest.setUserName(user.getUserName());
    userDTORequest.setPhoneNumber(user.getPhoneNumber());
    userDTORequest.setEmailAddress(user.getEmailAddress());
    userDTORequest.setLatestLocationTimestamp(user.getLatestLocationTimestamp());
    userDTORequest.setVisitedLocations(user.getVisitedLocations());
    userDTORequest.setUserRewards(user.getUserRewards());
    userDTORequest.setUserPreferences(user.getUserPreferences());
    userDTORequest.setTripDeals(user.getTripDeals());
    return userDTORequest;
  }

}
