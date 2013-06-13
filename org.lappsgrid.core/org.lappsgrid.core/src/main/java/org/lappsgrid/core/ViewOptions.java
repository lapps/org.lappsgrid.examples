package org.lappsgrid.core;

/**
 * Specifies how to present each {@link Document}. 
 * Possible options include: 
 * 1) shows document id;
 * 2) shows document span;
 * 3) shows document content;
 * 4) shows annotation;
 * 5) shows annotation span;
 * 
 * @author Di Wang
 */
public abstract class ViewOptions {
  
  /** The document id shown. */
  boolean documentIdShown;

  /** The document span shown. */
  boolean documentSpanShown;

  /** The document content shown. */
  boolean documentContentShown;

  /** The annotation shown. */
  boolean annotationShown;

  /** The annotation span shown. */
  boolean annotationSpanShown;

  /**
   * Checks if is document id shown.
   *
   * @return true, if is document id shown
   */
  public boolean isDocumentIdShown() {
    return documentIdShown;
  }

  /**
   * Sets the document id shown.
   *
   * @param documentIdShown the new document id shown
   */
  public void setDocumentIdShown(boolean documentIdShown) {
    this.documentIdShown = documentIdShown;
  }

  /**
   * Checks if is document span shown.
   *
   * @return true, if is document span shown
   */
  public boolean isDocumentSpanShown() {
    return documentSpanShown;
  }

  /**
   * Sets the document span shown.
   *
   * @param documentSpanShown the new document span shown
   */
  public void setDocumentSpanShown(boolean documentSpanShown) {
    this.documentSpanShown = documentSpanShown;
  }

  /**
   * Checks if is document content shown.
   *
   * @return true, if is document content shown
   */
  public boolean isDocumentContentShown() {
    return documentContentShown;
  }

  /**
   * Sets the document content shown.
   *
   * @param documentContentShown the new document content shown
   */
  public void setDocumentContentShown(boolean documentContentShown) {
    this.documentContentShown = documentContentShown;
  }

  /**
   * Checks if is annotation shown.
   *
   * @return true, if is annotation shown
   */
  public boolean isAnnotationShown() {
    return annotationShown;
  }

  /**
   * Sets the annotation shown.
   *
   * @param annotationShown the new annotation shown
   */
  public void setAnnotationShown(boolean annotationShown) {
    this.annotationShown = annotationShown;
  }

  /**
   * Checks if is annotation span shown.
   *
   * @return true, if is annotation span shown
   */
  public boolean isAnnotationSpanShown() {
    return annotationSpanShown;
  }

  /**
   * Sets the annotation span shown.
   *
   * @param annotationSpanShown the new annotation span shown
   */
  public void setAnnotationSpanShown(boolean annotationSpanShown) {
    this.annotationSpanShown = annotationSpanShown;
  }
}
