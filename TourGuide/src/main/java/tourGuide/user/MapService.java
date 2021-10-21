package tourGuide.user;

import org.springframework.stereotype.Service;

@Service
public class MapService {

  public UserDTOResponse convertUserToUserDTOResponse(User user) {
    UserDTOResponse userDTOResponse = new UserDTOResponse();
    userDTOResponse.setUserName(user.getUserName());
    userDTOResponse.setPhoneNumber(user.getPhoneNumber());
    userDTOResponse.setEmailAddress(user.getEmailAddress());
    userDTOResponse.setLatestLocationTimestamp(user.getLatestLocationTimestamp());
    userDTOResponse.setVisitedLocations(user.getVisitedLocations());
    userDTOResponse.setUserRewards(user.getUserRewards());
    userDTOResponse.setUserPreferences(user.getUserPreferences());
    userDTOResponse.setTripDeals(user.getTripDeals());
    return userDTOResponse;
  }

}
