package org.lapps.datasource;

import java.io.Serializable;

/**
 * Stores data transfered within LAPPS grid
 *
 * @author Keith Suderman
 */
public class Data implements Serializable {

  protected String discriminator;

  /** The payload. */
  protected String payload;

  /**
   * Gets the discriminator that tells us what exactly is contained in the payload, eg. a JSON file with
   * a list of named entities.
   * 
   * @return the discriminator
   */
  public String getDiscriminator() {
    return discriminator;
  }

  /**
   * Sets the discriminator that tells us what exactly is contained in the payload, eg. a JSON file with
   * a list of named entities.
   * 
   * @param discriminator
   *          the new discriminator
   */
  public void setDiscriminator(String discriminator) {
    this.discriminator = discriminator;
  }

  /**
   * Gets the payload.
   * 
   * @return the payload
   */
  public String getPayload() {
    return payload;
  }

  /**
   * Sets the payload.
   * 
   * @param payload
   *          the new payload
   */
  public void setPayload(String payload) {
    this.payload = payload;
  }

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 8317466190753467952L;

}
