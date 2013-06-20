package org.lappsgrid.discriminator.core;


/**
 * Discriminator objects are used to provide type information about
 * {@link Data} objects returned by a {@link DataSource}.
 * <p>
 * Discriminator objects are never exposed directly to users. Rather
 * the user interacts with a {@link DiscriminatorRegistry} to 
 * obtain discriminator id values and to map between discriminator
 * names and discriminator values.
 * 
 * @author Keith Suderman
 *
 */
public interface Discriminator extends java.io.Serializable
{   
   long getId();
   boolean isa(Discriminator discriminator);
}
