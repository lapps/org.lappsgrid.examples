/**
 * Discriminators are used to distinguish between the various types of
 * data that may be passed around in {@link org.lappsgrid.api.Data Data}
 * objects.
 * <p>
 * Discriminators are intended to be an abstract representation of any type 
 * system. Discriminators are represented with 64-bit (long) integer values and
 * the {@link org.lappsgrid.discriminator.DiscriminatorRegistry DiscriminatorRegistry}
 * class is used to map between type (disciminator) names and their long values.
 * <p>
 * It is expected that future versions of the DiscriminatorRegistry will 
 * obtain type information from a more suitable source (e.g. an ontology).
 * 
 * @author Keith Suderman
 * 
 */
package org.lappsgrid.discriminator;