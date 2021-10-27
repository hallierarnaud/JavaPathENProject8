package tourGuide.user;

import org.springframework.stereotype.Service;

@Service
public class MapService {

  public UserDTO convertUserToUserDTORequest(User user) {
    UserDTO userDTO = new UserDTO();
    userDTO.setUserId(user.getUserId());
    userDTO.setUserName(user.getUserName());
    userDTO.setPhoneNumber(user.getPhoneNumber());
    userDTO.setEmailAddress(user.getEmailAddress());
    return userDTO;
  }

}
