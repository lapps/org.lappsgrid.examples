package org.lapps.annotatedcorpus;

/**
 * The Interface CorpusManager manages corpus. 
 *
 * @author Di Wang
 */
public interface CorpusManager {

  /**
   * Connect to a remote corpus
   *
   * @param connOptions the options that specify how to establish a connection to remote corpus. 
   * @return the annotated corpus
   * @throws NoSuchCorpusException the no such corpus exception
   */
  public AnnotatedCorpus connect(ConnectionOptions connOptions) throws NoSuchCorpusException;

}
