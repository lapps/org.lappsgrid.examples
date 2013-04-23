package org.lapps.annotatedcorpus;

import java.io.Serializable;

/**
 * The Class AnnotatedDocument stores an annotated document. (temporary)
 *
 * @author Keith Suderman
 * @author Di Wang
 */
public abstract class AnnotatedDocument implements Serializable {

  Object discriminator;

  /** The payload. */
  String payload;

  /**
   * Gets the discriminator that tells us what exactly is contained in the payload, eg. a JSON file with
   * a list of named entities.
   * 
   * @return the discriminator
   */
  public Object getDiscriminator() {
    return discriminator;
  }

  /**
   * Sets the discriminator that tells us what exactly is contained in the payload, eg. a JSON file with
   * a list of named entities.
   * 
   * @param discriminator
   *          the new discriminator
   */
  public void setDiscriminator(Object discriminator) {
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
