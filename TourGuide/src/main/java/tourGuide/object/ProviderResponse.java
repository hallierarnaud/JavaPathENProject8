package tourGuide.object;

import java.util.UUID;

import lombok.Data;

@Data
public class ProviderResponse {

  public String name;
  public double price;
  public UUID tripId;

}
