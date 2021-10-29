package tourGuide.user;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class UserDTOFromGpsService {

  private UUID userId;
  private String userName;
  private String phoneNumber;
  private String emailAddress;
  private Date latestLocationTimestamp;

}
