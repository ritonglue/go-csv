package io.github.ritonglue.gocsv.annotation;

/**
* Used with the {@link Access} annotation to specify an access
* type to be applied.
* 
* @see Access
*
*/
public enum AccessType {
	/** Field-based access is used. */
	  FIELD
	/** Property-based access is used. */
	, PROPERTY;
}
