package tourGuide.user;

import java.util.UUID;

import lombok.Data;

@Data
public class UserDTO {

  private UUID userId;
  private String userName;
  private String phoneNumber;
  private String emailAddress;

}
